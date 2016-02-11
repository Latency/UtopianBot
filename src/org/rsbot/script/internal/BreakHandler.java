package org.rsbot.script.internal;

import java.util.TimerTask;

import org.rsbot.script.Script;
import org.rsbot.script.internal.containers.ScriptContainer;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.util.Timer;

public class BreakHandler extends MethodProvider {
  public BreakHandler(MethodContext ctx) {
    super(ctx);
  }

  private final Timer       timer = new Timer();
  private long              period;

  protected ScriptContainer sc;
  private   volatile boolean      enabled   = false;
  protected volatile boolean      running   = false;
  protected volatile boolean      paused    = false;
  protected int                   id        = -1;
  
  protected void initialize(final MethodContext ctx, ScriptContainer sc) {
    this.methods = ctx;
    this.sc = sc;
  }

  protected void initialize(final Script script) {
    this.initialize(this.methods, script.getContainerInfo());
  }

  // ///////////////////////////////////////////////////////////////

  class Tick extends TimerTask {
    @Override
    public void run() {
      enabled = false;
    }
  }

  /**
   * 
   * @return
   */
  public boolean isRunning() {
    return timer.isRunning() && isEnabled();
  }

  /**
   * 
   * @return
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Set timer to the number of milliseconds until tick.
   * 
   * @param seconds
   */
  public void setTimer(final long milliseconds) {
    period = milliseconds;
    timer.setEndIn(period);
  }

  /**
   * 
   */
  public void stop() {
    timer.cancel();
    Timer.Seconds(timer.getElapsed());
    enabled = false;
  }
  
  /**
   * 
   */
  public void start() {
    enabled = true;
    log.info("Going idle for " + Timer.format(timer.getRemaining()));
    timer.schedule(new Tick(), timer.getRemaining());
  }

  /**
   * 
   */
  public void reset() {
    timer.reset();
  }

  /**
   * For internal use only. Deactivates this script if the appropriate id is provided.
   * 
   * @param id The id from ScriptHandler.
   */
  protected final void deactivate(final int id) {
    if (id != this.id)
      throw new IllegalStateException("Invalid id!");
    running = false;
  }

  /**
   * For internal use only. Sets the pool id of this script.
   * 
   * @param id The id from ScriptHandler.
   */
  protected final void setID(final int id) {
    if (this.id != -1)
      throw new IllegalStateException("Already added to pool!");
    this.id = id;
  }

}