package nl.ordina.reactive.rest.config;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@ApplicationScoped
public class CdiFactory {
  @Resource(name = "url/backendRestService")
  private URL url; // JNDI lookup for the url to reach the backend services

  @Produces
  public WebTarget createBackendServiceTarget() {
    URI uri = createUri();
    return ClientBuilder.newClient().target(uri);
  }

  private URI createUri() {
    try {
      return url.toURI();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
