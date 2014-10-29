package nl.ordina.reactive.rest;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class Sleeper {
  private final static Random randomGenerator = new Random();
  public static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(16);
  private final int delayInMs;
  private final boolean random;

  private Sleeper(int delayInMs, boolean random) {
    this.delayInMs = delayInMs;
    this.random = random;
  }

  public static Sleeper createFixedDelay(int delayInMs) {
    return new Sleeper(delayInMs, false);
  }

  public static Sleeper createRandomDelay(int delayInMs) {
    return new Sleeper(delayInMs, true);
  }

  public void sleep() {
    try {
      Thread.sleep(getDelayInMs());
    } catch (InterruptedException e) {
      // Restore the interrupted status
      Thread.currentThread().interrupt();
    }
  }

  public int getDelayInMs() {
    return (random ? randomGenerator.nextInt(10) : 1) * delayInMs;
  }

  public void schedule(Runnable r) {
    executor.schedule(r, getDelayInMs(), MILLISECONDS);
  }
}
