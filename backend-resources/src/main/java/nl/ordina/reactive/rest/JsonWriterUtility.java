package nl.ordina.reactive.rest;

import javax.json.JsonStructure;
import javax.json.JsonWriter;
import java.io.StringWriter;

import static javax.json.Json.createWriter;

public class JsonWriterUtility {
  public static String mapToString(JsonStructure obj) {
    StringWriter stringWriter = new StringWriter();
    try (JsonWriter writer = createWriter(stringWriter)) {
      writer.write(obj);
    }
    return stringWriter.toString();
  }
}
