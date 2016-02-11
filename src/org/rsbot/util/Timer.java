package org.rsbot.util;

/**
 * A Timer
 */
public class Timer extends java.util.Timer {
  public static final int SECS_PER_MIN = 60,
                                       MINS_PER_HOUR = 60,
                                       SECS_PER_HOUR = SECS_PER_MIN * MINS_PER_HOUR,
                                       MILLISECS_PER_SEC = 1000,
                                       MILLISECS_PER_MIN = MILLISECS_PER_SEC * SECS_PER_MIN,
                                       MILLISECS_PER_HOUR = MILLISECS_PER_MIN * MINS_PER_HOUR;

  private long            end;
  private final long      start;
  private final long      period;

  /**
   * Instantiates a new Timer to run indefinitely (Long.MAX_VALUE).
   * 
   */
  public Timer() {
    this(Long.MAX_VALUE);
  }

  /**
   * Instantiates a new Timer with a given time period in milliseconds.
   * 
   * @param period Time period in milliseconds.
   */
  public Timer(final long period) {
    this.period = period;
    start = System.currentTimeMillis();
    end = start + period;
  }

  /**
   * Returns the number of milliseconds elapsed since the start time.
   * 
   * @return The elapsed time in milliseconds.
   */
  public long getElapsed() {
    return System.currentTimeMillis() - start;
  }

  /**
   * Returns the number of milliseconds remaining until the timer is up.
   * 
   * @return The remaining time in milliseconds.
   */
  public long getRemaining() {
    if (isRunning()) {
      return end - System.currentTimeMillis();
    }
    return 0;
  }

  /**
   * Returns the total number of seconds since Epoch time.
   * 
   * @return The number of seconds since time 00:00:00 UTC on 1 January 1970 (or 1970-01-01T00:00:00Z ISO 8601).
   */
  public static final long getEpochSecs() {
    return System.currentTimeMillis() / MILLISECS_PER_SEC;
  }

  /**
   * The number of seconds in <code>time</code>.
   * 
   * @param time The number of seconds.
   * @return The number of milliseconds.
   */
  public static final long Seconds(final long time) {
    return MILLISECS_PER_SEC * time;
  }

  /**
   * The number of minutes in time
   * 
   * @param time The number of minutes.
   * @return The number of milliseconds.
   */
  public static final long Minutes(final long time) {
    return MILLISECS_PER_MIN * time;
  }

  /**
   * The number of hours in time
   * 
   * @param time The number of hours.
   * @return The number of milliseconds.
   */
  public static final long Hours(final long time) {
    return MILLISECS_PER_HOUR * time;
  }

  /**
   * The number of seconds in <code>time</code>.
   * 
   * @param time in milliseconds.
   * @return The number of seconds.
   */
  public static final long toSeconds(final long time) {
    return time / MILLISECS_PER_SEC;
  }

  /**
   * The number of minutes in <code>time</code>.
   * 
   * @param time in milliseconds.
   * @return The number of minutes.
   */
  public static final long toMinutes(final long time) {
    return toSeconds(time) / SECS_PER_MIN;
  }

  /**
   * The number of hours in <code>time</code>.
   * 
   * @param time in milliseconds.
   * @return The number of hours.
   */
  public static final long toHours(final long time) {
    return toMinutes(time) / MINS_PER_HOUR;
  }

  /**
   * Returns <tt>true</tt> if this timer's time period has not yet elapsed.
   * 
   * @return <tt>true</tt> if the time period has not yet passed.
   */
  public boolean isRunning() {
    return System.currentTimeMillis() < end;
  }

  /**
   * Restarts this timer using its period.
   */
  public void reset() {
    end = System.currentTimeMillis() + period;
  }

  /**
   * Sets the end time of this timer to a given number of milliseconds from the time it is called. This does not edit the period of the timer (so will not affect operation after reset).
   * 
   * @param ms The number of milliseconds before the timer should stop running.
   * @return The new end time.
   */
  public long setEndIn(final long ms) {
    end = System.currentTimeMillis() + ms;
    return end;
  }

  /**
   * Returns a formatted String of the time elapsed.
   * 
   * @return The elapsed time formatted hh:mm:ss.
   */
  public String toElapsedString() {
    return format(getElapsed());
  }

  /**
   * Returns a formatted String of the time remaining.
   * 
   * @return The remaining time formatted hh:mm:ss.
   */
  public String toRemainingString() {
    return format(getRemaining());
  }

  /**
   * Converts milliseconds to a String in the format hh:mm:ss.
   * 
   * @param time The number of milliseconds.
   * @return The formatted String.
   */
  public final static String format(final long time) {
    String t = "";
    final long secs = toSeconds(time) % 60;
    final long mins = toMinutes(time) % 60;
    final long hrs = toHours(time) % 60;
    if (hrs < 10)
      t += "0";
    t += hrs + ":";
    if (mins < 10)
      t += "0";
    t += mins + ":";
    if (secs < 10)
      t += "0";
    t += secs;
    return t;
  }

  /**
   *
   */
  @Deprecated
  @Override
  public String toString() {
    return super.toString();
  }
}