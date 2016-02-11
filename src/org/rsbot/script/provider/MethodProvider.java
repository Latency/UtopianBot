package org.rsbot.script.provider;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that provides methods that use data from the game client. For internal use.
 * 
 * @author Latency
 */
public abstract class MethodProvider {
  protected MethodContext   methods;
  
  public MethodProvider(final MethodContext ctx) {
    this.methods = ctx;
  }
  
  public final MethodContext getMethodContext() {
    return methods;
  }
  
  public void initialize(final MethodContext ctx) {
    this.methods = ctx;
  }

  /**
   * The logger instance
   */
  protected final Logger log = Logger.getLogger(getClass().getName());
  
  /**
   * Returns a linearly distributed pseudorandom integer.
   * 
   * @param min The inclusive lower bound.
   * @param max The exclusive upper bound.
   * @return Random integer min <= n < max.
   */
  public int random(final int min, final int max) {
    return min + (max == min ? 0 : methods.random.nextInt(max - min));
  }

  /**
   * Returns a normally distributed pseudorandom integer about a mean centered between min and max with a provided standard deviation.
   * 
   * @param min The inclusive lower bound.
   * @param max The exclusive upper bound.
   * @param sd The standard deviation. A higher value will increase the probability of numbers further from the mean being returned.
   * @return Random integer min <= n < max from the normal distribution described by the parameters.
   */
  public int random(final int min, final int max, final int sd) {
    final int mean = min + (max - min) / 2;
    int rand;
    do {
      rand = (int) (methods.random.nextGaussian() * sd + mean);
    } while (rand < min || rand >= max);
    return rand;
  }

  /**
   * Returns a normally distributed pseudorandom integer with a provided standard deviation about a provided mean.
   * 
   * @param min The inclusive lower bound.
   * @param max The exclusive upper bound.
   * @param mean The mean (>= min and < max).
   * @param sd The standard deviation. A higher value will increase the probability of numbers further from the mean being returned.
   * @return Random integer min <= n < max from the normal distribution described by the parameters.
   */
  public int random(final int min, final int max, final int mean, final int sd) {
    int rand;
    do {
      rand = (int) (methods.random.nextGaussian() * sd + mean);
    } while (rand < min || rand >= max);
    return rand;
  }

  /**
   * Returns a random double with min as the inclusive lower bound and max as the exclusive upper bound.
   * 
   * @param min The inclusive lower bound.
   * @param max The exclusive upper bound.
   * @return Random double min <= n < max.
   */
  public double random(final double min, final double max) {
    return Math.min(min, max) + methods.random.nextDouble() * Math.abs(max - min);
  }

  /**
   * Pauses execution for a given number of milliseconds.
   * 
   * @param l The time to sleep in milliseconds.
   */
  public static void sleep(final long l) {
    try {
      final long start = System.currentTimeMillis();
      Thread.sleep(l);

      // Guarantee minimum sleep
      long now;
      while (start + l > (now = System.currentTimeMillis())) {
        Thread.sleep(start + l - now);
      }
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Pauses execution for a random amount of time between two values.
   * 
   * @param minSleep The minimum time to sleep.
   * @param maxSleep The maximum time to sleep.
   * @see #sleep(int)
   * @see #random(int, int)
   */
  public void sleep(final int minSleep, final int maxSleep) {
    sleep(random(minSleep, maxSleep));
  }

  /**
   * Prints to the RSBot log.
   * 
   * @param message Object to log.
   */
  public void log(final Object message) {
    log.info(methods.locale.OS.toString(message.toString()));
  }

  /**
   * Prints to the RSBot log with a font color
   * 
   * @param color The color of the font
   * @param message Object to log
   */
  public void log(final Color color, final Object message) {
    final Object[] parameters = { color };
    log.log(Level.INFO, methods.locale.OS.toString(message.toString()), parameters);
  }
}
