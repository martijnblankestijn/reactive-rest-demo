package nl.ordina.reactive.rest;

import javax.json.JsonArray;
import javax.ws.rs.client.InvocationCallback;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

class CompletableFutureInvocationCallback
    implements InvocationCallback<JsonArray> {
  private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  final CompletableFuture<JsonArray> cf;

  CompletableFutureInvocationCallback(CompletableFuture<JsonArray> cf) {
    this.cf = cf;
  }

  @Override
  public void completed(JsonArray r) {
    log.log(Level.INFO, "Received " + r);
    cf.complete(r);
  }

  @Override
  public void failed(Throwable throwable) {
    log.log(Level.WARNING, "Error while processing response", throwable);
    cf.completeExceptionally(throwable);
  }
}
