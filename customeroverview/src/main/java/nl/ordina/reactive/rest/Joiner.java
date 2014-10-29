package nl.ordina.reactive.rest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.LocalDateTime;

public class Joiner {
  public JsonObject join(JsonObject customer, JsonArray contracts, JsonArray communications) {
    JsonObjectBuilder customerContract = withContracts(customer, contracts);

    return withCommunications(customerContract, communications);
  }

  public JsonObject withCommunications(JsonObjectBuilder builder,
                                       JsonArray communications) {
    return Json.createObjectBuilder()
        .add("customerOverview",
            builder.add("communications", communications)
                .build())
        .add("retrievalTimestamp", LocalDateTime.now().toString())
        .build();
  }

  public JsonObjectBuilder withContracts(JsonObject info,
                                         JsonArray contracts) {
    return Json.createObjectBuilder()
        .add("customer", info)
        .add("contracts", contracts);
  }
}
