package org.rsbot.script;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.logging.Level;

import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.util.Timer;

public abstract class Random extends MethodProvider implements PaintListener {

  protected String         name;
  private volatile boolean enabled = true;
  public int               i       = 50;
  public boolean           up      = false;
  private Script           script;
  private final long       timeout = random(240, 300);

  public Random(final MethodContext ctx) {
    super(ctx);
  }

  /**
   * Called after the method providers for this Random become available for use in initialization.
   */
  public void onStart() {

  }

  public void onFinish() {

  }

  /**
   * Override to provide a time limit in seconds for this anti-random to complete.
   * 
   * @return The number of seconds after activateCondition returns <tt>true</tt> before the anti-random should be detected as having failed. If this time is reached the random and running script will be stopped.
   */
  public long getTimeout() {
    return timeout;
  }

  /**
   * Detects whether or not this anti-random should activate.
   * 
   * @return <tt>true</tt> if the current script should be paused and control passed to this anti-random's loop.
   */
  public abstract boolean activateCondition();

  public abstract int loop();

  public final boolean isActive() {
    return methods != null;
  }

  public final boolean isEnabled() {
    return enabled;
  }

  public final void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Stops the current script; player can be logged out before the script is stopped.
   * 
   * @param logout <tt>true</tt> if the player should be logged out before the script is stopped.
   */
  protected void stopScript(final boolean logout) {
    script.stopScript(logout);
  }

  public final void run(final Script ctx) {
    script = ctx;
    name = getClass().getAnnotation(ScriptManifest.class).name();
    ctx.getMethodContext().bot.getEventManager().removeListener(ctx);
    for (final Script s : ctx.delegates)
      ctx.getMethodContext().bot.getEventManager().removeListener(s);
    ctx.getMethodContext().bot.getEventManager().addListener(this);
    log("Random event started: " + name);
    onStart();
    long timeout = getTimeout();
    if (timeout > 0) {
      timeout *= Timer.Seconds(1);
      timeout += System.currentTimeMillis();
    }
    while (ctx.isRunning()) {
      try {
        final int wait = loop();
        if (wait == -1) {
          break;
        } else if (timeout > 0 && System.currentTimeMillis() >= timeout) {
          log.warning("Time limit reached for " + name + ".");
          ctx.stopScript();
        } else {
          sleep(wait);
        }
      } catch (final Exception ex) {
        log.log(Level.SEVERE, "Uncaught exception: ", ex);
        break;
      }
    }
    script = null;
    onFinish();
    log("Random event finished: " + name);
    ctx.getMethodContext().bot.getEventManager().removeListener(this);
    sleep(1000);
    ctx.getMethodContext().bot.getEventManager().addListener(ctx);
    for (final Script s : ctx.delegates)
      ctx.getMethodContext().bot.getEventManager().addListener(s);
  }

  @Override
  public final void onRepaint(final Graphics g) {
    final Point p = methods.mouse.getLocation();
    final int w = methods.game.getWidth(), h = methods.game.getHeight();
    if (i >= 70 && !up)
      i--;
    else {
      i++;
      up = i < 130;
    }
    g.setColor(new Color(0, 255, 0, i));
    g.fillRect(0, 0, p.x - 1, p.y - 1);
    g.fillRect(p.x + 1, 0, w - (p.x + 1), p.y - 1);
    g.fillRect(0, p.y + 1, p.x - 1, h - (p.y - 1));
    g.fillRect(p.x + 1, p.y + 1, w - (p.x + 1), h - (p.y - 1));
    g.setColor(Color.RED);
    g.drawString("Random Active: " + name, 540, 20);
  }
}
