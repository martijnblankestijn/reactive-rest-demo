package nl.ordina.reactive.rest.contract;

import nl.ordina.reactive.rest.Sleeper;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Stream.of;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;
import static nl.ordina.reactive.rest.JsonWriterUtility.mapToString;

@Path("contracts")
public class ContractResource {
  private final static SecureRandom random = new SecureRandom();
  private final static Map<String, String> db = createDatabase();

  @Inject Sleeper sleeper;

  @GET @Path("{customerId}")
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
    db.put("100", createContracts("100", "1234.35", "-34.00"));
    db.put("101", createContracts("101", "100.00"));
    db.put("102", createContracts("102"));
    return db;
  }

  private static String createContracts(String customerId, String... balances) {
    JsonArray jsonArray = of(balances)
        .map(balance -> createContract(customerId, balance))
        .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
        .build();
    return mapToString(jsonArray);
  }

  private static JsonObjectBuilder createContract(String customerId, String balance) {
    return createObjectBuilder()
        .add("contractNumber", createContractNumber(customerId))
        .add("balance", createObjectBuilder()
                .add("currency", "EUR")
                .add("amount", balance)
                .build()
        );
  }

  private static String createContractNumber(String customerId) {
    return customerId + random.nextInt(1000);
  }
}
