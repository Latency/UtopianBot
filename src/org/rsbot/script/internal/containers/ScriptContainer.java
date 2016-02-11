package org.rsbot.script.internal.containers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.rsbot.script.Script;
import org.rsbot.script.internal.ScriptListener;

public class ScriptContainer {
  public final HashMap<Integer, Thread> scriptThreads = new HashMap<Integer, Thread>();
  public final HashMap<Integer, Script> scripts       = new HashMap<Integer, Script>();
  public final Set<ScriptListener>      listeners     = Collections.synchronizedSet(new HashSet<ScriptListener>());
}