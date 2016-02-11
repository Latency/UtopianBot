package org.rsbot.script;

import java.util.EventListener;

import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;

/**
 * A background script.
 * 
 * @author Timer
 */
public abstract class BackgroundScript extends MethodProvider implements EventListener, Runnable {
  public BackgroundScript(MethodContext ctx) {
    super(ctx);
  }

  protected String         name    = "";
  private volatile boolean running = false;
  private int              id      = -1;

  public abstract boolean activateCondition();

  public abstract int loop();

  public abstract int iterationSleep();

  public static boolean onStart() {
    return true;
  }

  public void onFinish() {
  }

  /**
   * Runs the background script.
   */
  @Override
  public final void run() {
    name = getClass().getAnnotation(ScriptManifest.class).name();
    methods.bot.getEventManager().addListener(this);
    running = true;
    try {
      while (running) {
        if (activateCondition()) {
          final boolean start = onStart();
          if (start) {
            while (running) {
              final int timeOut = loop();
              if (timeOut == -1)
                break;
              Thread.sleep(timeOut);
            }
            onFinish();
          }
        }
        Thread.sleep(iterationSleep());
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
    methods.bot.getEventManager().removeListener(this);
    running = false;
  }

  /**
   * Removes the script.
   * 
   * @param id The id to deactivate.
   */
  public final void deactivate(final int id) {
    if (id != this.id) {
      throw new IllegalStateException("Invalid id!");
    }
    running = false;
  }

  /**
   * Gives the script an id.
   * 
   * @param id The id.
   */
  public final void setID(final int id) {
    if (this.id != -1) {
      throw new IllegalStateException("Already added to pool!");
    }
    this.id = id;
  }

  /**
   * Gets the id of the script.
   * 
   * @return The ID.
   */
  public final int getID() {
    return id;
  }

  /**
   * Checks if the script is running.
   * 
   * @return <tt>true</tt> if true.
   */
  public final boolean isRunning() {
    return running;
  }
}
