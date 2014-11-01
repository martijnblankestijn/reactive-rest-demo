package nl.ordina.reactive.rest.communication;

import nl.ordina.reactive.rest.Sleeper;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Stream.of;
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
    db.put("100",
        createCommunications(
            build("email", "martinsmit@gmail.com"),
            build("telephone", "0612345678")));
    db.put("101",
        createCommunications(
            build("email", "karelboven@gmail.com"),
            build("email", "karelboven@hotmail.com"),
            build("telephone", "0687654321")));
    db.put("102", createCommunications());
    return db;
  }

  private static String createCommunications(JsonObject... elements) {
    JsonArrayBuilder combined = of(elements)
        .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add);

    return mapToString(combined.build());
  }

  private static JsonObject build(String type, String value) {
    return createObjectBuilder()
        .add("type", type)
        .add("value", value)
        .build();
  }
}
