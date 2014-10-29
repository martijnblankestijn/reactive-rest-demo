package nl.ordina.reactive.rest.communication;

import nl.ordina.reactive.rest.Sleeper;

import javax.inject.Inject;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.HashMap;
import java.util.Map;

import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;
import static nl.ordina.reactive.rest.JsonWriterUtility.mapToString;

@Path("communications")
public class CommunicationResource {
  private final static Map<String, String> db = createDatabase();
  @Inject
  Sleeper sleeper;

  @GET
  @Path("{customerId}")
  @Produces(APPLICATION_JSON)
  public void withExecutorService(
      @Suspended AsyncResponse response,
      @PathParam("customerId") String customerId) {
    sleeper.schedule(
        () -> response.resume(
            db.containsKey(customerId) ? db.get(customerId) : status(NOT_FOUND).build()));
  }

  private static Map<String, String> createDatabase() {
    Map<String, String> db = new HashMap<>();
    db.put("100", createCommunications("100", "gmail.com", "hotmail.com"));
    db.put("101", createCommunications("101", "gmail.com"));
    db.put("102", createCommunications("102"));
    return db;
  }

  private static String createCommunications(String customerId, String... communications) {
    JsonArrayBuilder builder = createArrayBuilder();
    for (String communication : communications) {
      builder.add(
          createObjectBuilder()
              .add("type", "email")
              .add("value", customerId + "@" + communication)
              .build());
    }
    return mapToString(builder.build());
  }
}
