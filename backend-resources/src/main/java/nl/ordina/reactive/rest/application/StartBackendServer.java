package nl.ordina.reactive.rest.application;

import nl.ordina.reactive.rest.Sleeper;
import nl.ordina.reactive.rest.communication.CommunicationResource;
import nl.ordina.reactive.rest.contract.ContractResource;
import nl.ordina.reactive.rest.customer.CustomerResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.logging.Logger;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;

public class StartBackendServer {
  private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  private final static String BACKEND_DELAY_RANDOM = "backend.delay.random";
  private final static String BACKEND_DELAY_MS = "backend.delay.ms";
  public static final String DEFAULT_DELAY_IN_MS = "200";

  private final SleeperFactory factory;

  public StartBackendServer(SleeperFactory factory) {
    this.factory = factory;
  }

  public void start() {
    log.log(INFO, "Java Runtime Version {0}", System.getProperty("java.runtime.version"));
    // must be 0.0.0. instead of localhost to allow remote connections
    URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(9998).build();

    ResourceConfig config = new ResourceConfig(CommunicationResource.class, ContractResource.class, CustomerResource.class);
    config.register(JsonProcessingFeature.class); // to activate JSR 353: Java API for JSON Processing
    config.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(factory).to(Sleeper.class);
      }
    });

    log.log(INFO, "Starting server on {0} with {1}", new Object[]{baseUri, factory});
    HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, true);

    Runnable shutdownProcedure = () -> {
      log.log(INFO, "Shutting down server");
      httpServer.shutdown(2, SECONDS);
      log.log(INFO, "Server shut down");
    };

    Runtime.getRuntime().addShutdownHook(new Thread(shutdownProcedure));
    try {
      waitForStopSignalFromSystemIn(shutdownProcedure);
    } catch (IOException e) {
      handleNoSystemIn(e);
    }

  }

  private void waitForStopSignalFromSystemIn(Runnable shutdownProcedure) throws IOException {
    log.log(INFO, "For stopping the server enter a keystroke");

    // value from System.in is ignored
    System.in.read();

    shutdownProcedure.run();
  }

  private void handleNoSystemIn(IOException e) {
    // IO Exception happens when System.in is closed.
    // System.in is closed when started by the '&' addition on the end of the command-line.
    log.log(INFO, "IO Exception when reading from System.in. Probably started with command line like this: 'java ..... & '");
    log.log(FINE, "IO Exception when reading from System.in. ", e);
    try {
      currentThread().join();
    } catch (InterruptedException e1) {
      // Restore the interrupted status
      currentThread().interrupt();
    }
  }

  /**
   * To activate all logging from IntelliJ project.
   * -Djava.util.logging.config.file=backend-server/src/main/resources/applogging.properties
   */
  public static void main(String[] args) throws IOException {
    SleeperFactory factory = new SleeperFactory(
        parseInt(getProperty(BACKEND_DELAY_MS, DEFAULT_DELAY_IN_MS)),
        valueOf(getProperty(BACKEND_DELAY_RANDOM, FALSE.toString())));

    StartBackendServer server = new StartBackendServer(factory);
    server.start();
  }
}
