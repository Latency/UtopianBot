package org.rsbot.script.methods;

import java.awt.image.BufferedImage;

import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.internal.ScriptHandler;
import org.rsbot.script.provider.MethodContext;

/**
 * Bot environment related operations.
 */
public class Environment extends ScriptHandler {
  public static final int INPUT_MOUSE    = 1;
  public static final int INPUT_KEYBOARD = 2;

  public Environment(final MethodContext ctx) {
    super(ctx);
  }

  /**
   * Controls the available means of user input when user input is disabled.
   * <p/>
   * <br />
   * Disable all: <code>setUserInput(0);</code> <br />
   * Enable keyboard only: <code>setUserInput(Environment.INPUT_KEYBOARD);</code> <br />
   * Enable mouse & keyboard: <code>setUserInput(Environment.INPUT_MOUSE | Environment.INPUT_KEYBOARD);</code>
   * 
   * @param mask flags indicating which types of input to allow
   */
  public void setUserInput(final int mask) {
    super.updateInput(mask);
  }

  /**
   * Takes and saves a screenshot.
   * 
   * @param hideUsername <tt>true</tt> to cover the player's username; otherwise <tt>false</tt>
   */
  public void saveScreenshot(final boolean hideUsername) {
    ScreenShot.saveScreenshot(methods.bot, hideUsername);
  }

  public void saveScreenshot(final boolean hideUsername, final String filename) {
    ScreenShot.saveScreenshot(methods.bot, hideUsername, filename);
  }

  /**
   * Takes a screenshot.
   * 
   * @param hideUsername <tt>true</tt> to cover the player's username; otherwise <tt>false</tt>
   * @return The screen capture image.
   */
  public BufferedImage takeScreenshot(final boolean hideUsername) {
    return ScreenShot.takeScreenshot(methods.bot, hideUsername);
  }

  /**
   * Enables a random event solver.
   * 
   * @param name the anti-random's (manifest) name (case insensitive)
   * @return <tt>true</tt> if random was found and set to enabled; otherwise <tt>false</tt>
   */
  public boolean enableRandom(final String name) {
    for (final Random random : super.getRandoms()) {
      if (random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
        if (random.isEnabled())
          return true;
        random.setEnabled(true);
        return true;
      }
    }
    return false;
  }

  /**
   * Disables a random event solver.
   * 
   * @param name the anti-random's (manifest) name (case insensitive)
   * @return <tt>true</tt> if random was found and set to disabled; otherwise <tt>false</tt>
   */
  public boolean disableRandom(final String name) {
    for (final Random random : super.getRandoms()) {
      if (random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
        if (!random.isEnabled()) {
          return true;
        }
        random.setEnabled(false);
        return true;
      }
    }
    return false;
  }

  /**
   * Enables all random event solvers.
   */
  public void enableRandoms() {
    for (final Random random : super.getRandoms()) {
      if (!random.isEnabled()) {
        random.setEnabled(true);
      }
    }
  }

  public void disableRandoms() {
    for (final Random random : super.getRandoms()) {
      if (random.isEnabled()) {
        random.setEnabled(false);
      }
    }
  }
}
