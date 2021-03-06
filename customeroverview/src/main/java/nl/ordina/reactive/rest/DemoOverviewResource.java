package nl.ordina.reactive.rest;

import nl.ordina.reactive.rest.domain.Communication;
import nl.ordina.reactive.rest.domain.Contract;
import nl.ordina.reactive.rest.domain.Customer;
import nl.ordina.reactive.rest.domain.CustomerOverview;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;

import static java.time.LocalDateTime.now;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("customers")
public class DemoOverviewResource {
  @Inject WebTarget backendServices;

  @GET @Path("{username}") @Produces(APPLICATION_JSON)
  public CustomerOverview retrieve(
      @PathParam("username") String username) {

    Customer customer = getCustomerInfo(username);

    Contract[] contracts = getContracts(customer);
    Communication[] communications = getCommunications(customer);

    return createOverview(
        customer,
        contracts,
        communications);
  }

  private Customer getCustomerInfo(final String username) {
    return backendServices.path("customers").path(username)
        .request()
        .get(Customer.class);
  }

  private Contract[] getContracts(Customer customer) {
    String path = "contracts";

    return backendServices.path(path).path(customer.id)
        .request()
        .get(Contract[].class);
  }

  private Communication[] getCommunications(Customer customer) {
    String path = "communications";

    return backendServices.path(path).path(customer.id)
        .request()
        .get(Communication[].class);
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
