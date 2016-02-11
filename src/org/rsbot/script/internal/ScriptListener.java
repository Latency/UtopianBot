package org.rsbot.script.internal;

import org.rsbot.bot.Bot;
import org.rsbot.script.Script;

/**
 */
public interface ScriptListener {
  public void scriptStarted(ScriptHandler handler, Script script);

  public void scriptStopped(ScriptHandler handler, Script script);

  public void scriptResumed(ScriptHandler handler, Script script);

  public void scriptPaused(ScriptHandler handler, Script script);

  public void inputChanged(Bot bot, int mask);
}
