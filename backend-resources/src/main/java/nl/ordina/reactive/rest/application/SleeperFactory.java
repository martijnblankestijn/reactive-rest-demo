package nl.ordina.reactive.rest.application;

import nl.ordina.reactive.rest.Sleeper;
import org.glassfish.hk2.api.Factory;

import static java.lang.String.format;
import static nl.ordina.reactive.rest.Sleeper.createFixedDelay;
import static nl.ordina.reactive.rest.Sleeper.createRandomDelay;

public class SleeperFactory implements Factory<Sleeper> {
  private final int delayInMs;
  private final boolean random;

  public SleeperFactory(int delayInMs, boolean random) {
    this.delayInMs = delayInMs;
    this.random = random;
  }

  @Override
  public Sleeper provide() {
    return random ? createRandomDelay(delayInMs) : createFixedDelay(delayInMs);
  }

  @Override
  public void dispose(Sleeper instance) {
    // nothing
  }

  @Override
  public String toString() {
    return format("%s{delayInMs=%d, random=%s}", SleeperFactory.class.getSimpleName(), delayInMs, random);
  }
}
