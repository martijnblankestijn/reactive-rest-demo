package nl.ordina.reactive.rest;

import nl.ordina.reactive.rest.domain.Communication;
import nl.ordina.reactive.rest.domain.Contract;
import nl.ordina.reactive.rest.domain.Customer;
import nl.ordina.reactive.rest.domain.CustomerOverview;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import java.util.concurrent.*;

import static java.time.LocalDateTime.now;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

@Path("customers")
public class DemoOverviewResource {
  @Inject WebTarget backendServices;
  @Resource ManagedExecutorService executor;

  @GET @Path("{username}") @Produces(APPLICATION_JSON)
  public void retrieve(
      @Suspended AsyncResponse response,
      @PathParam("username") String username) throws InterruptedException, ExecutionException, TimeoutException {

    // The asynchronous call to get the customer is extra
    // in comparison to the demo in the presentation
    CompletableFuture<Customer> customerFuture =
        CompletableFuture.completedFuture(username)
            .thenComposeAsync(this::getCustomerInfo, executor);

    CompletableFuture<Contract[]> contractFuture = customerFuture.thenComposeAsync(this::getContracts, executor);
    CompletableFuture<Communication[]> commFuture = customerFuture.thenComposeAsync(this::getCommunications, executor);

    customerFuture
        .thenApplyAsync(CustomerOverview::new, executor)
        .thenCombineAsync(contractFuture, CustomerOverview::add, executor)
        .thenCombineAsync(commFuture, CustomerOverview::add, executor)
        .whenCompleteAsync(
            (overview, throwable) -> {
              boolean b = overview == null ? response.resume(throwable) : response.resume(overview);
            }
        );

    response.setTimeout(1, SECONDS);
    response.setTimeoutHandler(
        r -> r.resume(new WebApplicationException(SERVICE_UNAVAILABLE)));
  }

  private CompletableFuture<Customer> getCustomerInfo(final String username) {
    CompletableFuture cf = new CompletableFuture();

    backendServices.path("customers").path(username)
        .request().async()
        .get(new InvocationCallback<Customer>() {
          @Override public void completed(Customer customer) {
            cf.complete(customer);
          }

          @Override public void failed(Throwable throwable) {
            cf.completeExceptionally(throwable);
          }
        });

    return cf;
  }

  private CompletableFuture<Contract[]> getContracts(Customer customer) {
    String path = "contracts";

    CompletableFuture cf = new CompletableFuture();

    backendServices.path(path).path(customer.id)
        .request().async()
        .get(new InvocationCallback<Contract[]>() {
          @Override public void completed(Contract[] contracts) {
            cf.complete(contracts);
          }

          @Override public void failed(Throwable throwable) {
            cf.completeExceptionally(throwable);
          }
        });

    return cf;
  }

  private CompletableFuture<Communication[]> getCommunications(Customer customer) {
    String path = "communications";

    CompletableFuture cf = new CompletableFuture();

    backendServices.path(path).path(customer.id)
        .request().async()
        .get(new InvocationCallback<Communication[]>() {
          @Override public void completed(Communication[] contracts) {
            cf.complete(contracts);
          }

          @Override public void failed(Throwable throwable) {
            cf.completeExceptionally(throwable);
          }
        });

    return cf;
  }

  private CustomerOverview createOverview(Customer customer, Contract[] contracts, Communication[] communications) {
    CustomerOverview overview = new CustomerOverview();
    overview.customer = customer;
    overview.contracts = contracts;
    overview.communications = communications;
    overview.retrievalTimestamp = now();
    return overview;
  }
}
