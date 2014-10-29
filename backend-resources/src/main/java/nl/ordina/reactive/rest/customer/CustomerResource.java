package nl.ordina.reactive.rest.customer;

import nl.ordina.reactive.rest.Sleeper;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDate.of;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;
import static nl.ordina.reactive.rest.JsonWriterUtility.mapToString;

@Path("customers")
public class CustomerResource {
  private final static Map<String, String> db = createDatabase();
  @Inject
  Sleeper sleeper;

  @GET
  @Path("{username}")
  @Produces(APPLICATION_JSON)
  public void getCustomer(
      @Suspended AsyncResponse response,
      @PathParam("username") String username) {
    sleeper.schedule(
        () -> response.resume(
            db.containsKey(username) ? db.get(username) : status(NOT_FOUND).build()));
  }

  private static Map<String, String> createDatabase() {
    Map<String, String> db = new HashMap<>();
    db.put("bbb", createCustomer(100, "Martin", "Smit"));
    db.put("ccc", createCustomer(101, "Karel", "Boven"));
    db.put("ddd", createCustomer(102, "Daan", "Vogel"));
    return db;
  }

  private static String createCustomer(int id, String firstName, String lastName) {
    return mapToString(
        createObjectBuilder()
            .add("id", "" + id)
            .add("firstName", firstName)
            .add("lastName", lastName)
            .add("birthDate", of(1850 + id, id % 12 + 1, id % 28).toString())
            .build());
  }
}
