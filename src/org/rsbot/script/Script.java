package org.rsbot.script;

import java.io.File;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rsbot.Configuration;
import org.rsbot.event.EventMulticaster;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.gui.AccountManager;
import org.rsbot.script.internal.BreakHandler;
import org.rsbot.script.internal.containers.ScriptContainer;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.randoms.LoginBot;
import org.rsbot.util.Timer;

public abstract class Script extends BreakHandler implements EventListener, Runnable {
  public Script(MethodContext ctx) {
    super(ctx);
  }

  Set<Script>                   delegates = new HashSet<Script>();
  public static final Logger    log       = Logger.getLogger(Script.class.getName());
  private volatile boolean      random    = false;
  /**
   * Called before loop() is first called, after this script has been initialized with all method providers. Override to perform any initialization or prevent script start.
   * 
   * @return <tt>true</tt> if the script can start.
   */
  public static boolean onStart() {
    return true;
  }

  /**
   * The main loop. Called if you return true from onStart, then continuously until a negative integer is returned or the script stopped externally. When this script is paused this method will not be called until the script is resumed. Avoid causing execution to pause using sleep() within this method in favor of returning the number of milliseconds to sleep. This ensures that pausing and anti-randoms perform normally.
   * 
   * @return The number of milliseconds that the manager should sleep before calling it again. Returning a negative number will deactivate the script.
   */
  public abstract int loop();

  /**
   * Override to perform any clean up on script stopScript.
   */
  public void onFinish() { }
  
  /**
   * 
   */
  public final ScriptContainer getContainerInfo() {
    return sc;
  }
  
  /**
   * Initializes the provided script with this script's method context and adds the delegate as a listener to the event manager, allowing it to receive client events. The script will be stored as a delegate of this script and removed from the event manager when this script is stopped. The onStart(), loop() and onFinish() methods are not automatically called on the delegate.
   * 
   * @param script The script to delegate to.
   */
  public final void delegateTo(final Script script) {
    initialize(script);
    methods.bot.getEventManager().addListener(script);
    delegates.add(script);
  }

  /**
   * Pauses/resumes this script.
   * 
   * @param paused <tt>true</tt> to pause; <tt>false</tt> to resume.
   */
  public final void setPaused(final boolean paused) {
    if (running && !random) {
      if (paused)
        blockEvents(true);
      else
        unblockEvents();
    }
    this.paused = paused;
  }

  /**
   * Returns whether or not this script is paused.
   * 
   * @return <tt>true</tt> if paused; otherwise <tt>false</tt>.
   */
  public final boolean isPaused() {
    return paused;
  }

  /**
   * Returns whether or not the loop of this script is able to receive control (i.e. not paused, stopped or in random).
   * 
   * @return <tt>true</tt> if active; otherwise <tt>false</tt>.
   */
  public boolean isActive() {
    return running && !paused && !random;
  }

  /**
   * Stops the current script without logging out.
   */
  public void stopScript() {
    stopScript(false);
  }

  /**
   * Stops the current script; player can be logged out before the script is stopped.
   * 
   * @param logout <tt>true</tt> if the player should be logged out before the script is stopped.
   */
  public void stopScript(final boolean logout) {
    log.info("Script stopping...");
    super.stop();
    if (logout) {
      if (methods.bank.isOpen())
        methods.bank.close();
      if (methods.game.isLoggedIn())
        methods.game.logout(false);
    }
    running = false;
  }

  @Override
  public void run() {
    boolean start = false;
    try {
      start = onStart();
    } catch (final ThreadDeath ignored) {
    } catch (final Throwable ex) {
      log.log(Level.SEVERE, "Error starting script: ", ex);
    }
    if (start) {
      running = true;
      methods.bot.getEventManager().addListener(this);
      log.info("Script started.");
      
      try {
        while (running) {
          if (!paused) {
            if (AccountManager.isTakingBreaks(methods.account.getName()) && methods.game.isLoggedIn() && isEnabled())
                continue;
            if (checkForRandoms())
              continue;
            int timeOut = -1;
            try {
              timeOut = loop();
            } catch (final ThreadDeath td) {
              break;
            } catch (final Exception ex) {
              log.log(Level.WARNING, "Uncaught exception from script: ", ex);
            }
            if (timeOut == -1)
              break;
            try {
              sleep(timeOut);
            } catch (final ThreadDeath td) {
              break;
            }
          } else {
            try {
              sleep(Timer.Seconds(1));
            } catch (final ThreadDeath td) {
              break;
            }
          }
        }
        try {
          onFinish();
        } catch (final ThreadDeath ignored) {
        } catch (final RuntimeException e) {
          e.printStackTrace();
        }
      } catch (final Throwable t) {
        onFinish();
      }
      running = false;
      log.info("Script stopped.");
    } else {
      log.severe("Failed to start up.");
    }
    methods.mouse.moveOffScreen();
    for (final Script s : delegates)
      methods.bot.getEventManager().removeListener(s);
    delegates.clear();
    methods.bot.getEventManager().removeListener(this);
    methods.bot.getScriptHandler().stopScript(id);
    id = -1;
  }

  private boolean checkForRandoms() {
    if (methods.bot.disableRandoms)
      return false;
    for (final Random random : methods.bot.getScriptHandler().getRandoms()) {
      if (random.isEnabled() && !(methods.bot.disableAutoLogin && random instanceof LoginBot)) {
        if (random.activateCondition()) {
          this.random = true;
          blockEvents(false);
          random.run(this);
          unblockEvents();
          this.random = false;
          return true;
        }
      }
    }
    return false;
  }

  private void blockEvents(final boolean paint) {
    for (final Script s : delegates) {
      methods.bot.getEventManager().removeListener(s);
      if (paint && s instanceof PaintListener)
        methods.bot.getEventManager().addListener(s, EventMulticaster.PAINT_EVENT);
    }
    methods.bot.getEventManager().removeListener(this);
    if (paint && this instanceof PaintListener)
      methods.bot.getEventManager().addListener(this, EventMulticaster.PAINT_EVENT);
  }

  private void unblockEvents() {
    for (final Script s : delegates) {
      methods.bot.getEventManager().removeListener(s);
      methods.bot.getEventManager().addListener(s);
    }
    methods.bot.getEventManager().removeListener(this);
    methods.bot.getEventManager().addListener(this);
  }

  /**
   * Get an accessible and isolated directory for reading and writing files.
   * 
   * @return A unique per-script directory path with file IO permissions.
   */
  public File getCacheDirectory() {
    final File dir = new File(Configuration.Paths.getScriptCacheDirectory(), getClass().getName());
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir;
  }
}
