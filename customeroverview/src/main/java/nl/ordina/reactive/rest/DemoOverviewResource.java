package nl.ordina.reactive.rest;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.WebTarget;
import java.util.logging.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Logger.getLogger;

@Path("customers")
public class DemoOverviewResource {
  @Inject WebTarget backendServices;
  @Inject Joiner joiner;

  @GET @Path("{username}") public JsonObject retrieve(
      @PathParam("username") String username) {

    JsonObject customer = getCustomerInfo(username);
    auditLog(customer);

    String customerId = customer.getString("id");
    JsonArray contracts = getContracts(customerId);
    JsonArray communications = getCommunications(customerId);

    return joiner.join(customer,
        contracts,
        communications);
  }

  private void auditLog(JsonObject c) {
    getLogger("AUDIT")
        .log(FINEST, "Customer {0} retrieved [{1}]",
            new Object[]{c.getString("id"), c});
  }


  //////////////////////////////////////////////////
  private JsonObject getCustomerInfo(final String username) {
    return backendServices
        .path("customers")
        .path("{username}")
        .resolveTemplate("username", username)
        .request().get(JsonObject.class);
  }

  private JsonArray getContracts(final String customerId) {
    String path = "contracts";
    return backendServices
        .path(path)
        .path(customerId)
        .request()
        .get(JsonArray.class);
  }

  private JsonArray getCommunications(final String customerId) {
    String path = "communications";
    return backendServices
        .path(path)
        .path(customerId)
        .request()
        .get(JsonArray.class);
  }
}
