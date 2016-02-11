package org.rsbot.script.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.internal.containers.ScriptContainer;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.randoms.BankPins;
import org.rsbot.script.randoms.BeehiveSolver;
import org.rsbot.script.randoms.CapnArnav;
import org.rsbot.script.randoms.Certer;
import org.rsbot.script.randoms.CloseAllInterface;
import org.rsbot.script.randoms.DrillDemon;
import org.rsbot.script.randoms.Exam;
import org.rsbot.script.randoms.FirstTimeDeath;
import org.rsbot.script.randoms.FreakyForester;
import org.rsbot.script.randoms.FrogCave;
import org.rsbot.script.randoms.GraveDigger;
import org.rsbot.script.randoms.ImprovedRewardsBox;
import org.rsbot.script.randoms.LeaveSafeArea;
import org.rsbot.script.randoms.LoginBot;
import org.rsbot.script.randoms.LostAndFound;
import org.rsbot.script.randoms.Maze;
import org.rsbot.script.randoms.Mime;
import org.rsbot.script.randoms.Molly;
import org.rsbot.script.randoms.Pillory;
import org.rsbot.script.randoms.Pinball;
import org.rsbot.script.randoms.Prison;
import org.rsbot.script.randoms.QuizSolver;
import org.rsbot.script.randoms.SandwhichLady;
import org.rsbot.script.randoms.ScapeRuneIsland;
import org.rsbot.script.randoms.SystemUpdate;
import org.rsbot.script.randoms.TeleotherCloser;

public class ScriptHandler extends MethodProvider {
  public final static String                       THREAD_GROUP_NAME = "Scripts";
  private final ThreadGroup                        scriptThreadGroup = new ThreadGroup(THREAD_GROUP_NAME);
  private final ArrayList<org.rsbot.script.Random> randoms           = new ArrayList<org.rsbot.script.Random>();
  private final ScriptContainer                    sc                = new ScriptContainer();

  public ScriptHandler(final MethodContext ctx) {
    super(ctx);
  }

  public void init(final MethodContext methods) {
    this.methods = methods;
    try {
      randoms.add(new LoginBot(methods));
      randoms.add(new BankPins(methods));
      randoms.add(new BeehiveSolver(methods));
      randoms.add(new CapnArnav(methods));
      randoms.add(new Certer(methods));
      randoms.add(new CloseAllInterface(methods));
      randoms.add(new DrillDemon(methods));
      randoms.add(new FreakyForester(methods));
      randoms.add(new FrogCave(methods));
      randoms.add(new GraveDigger(methods));
      randoms.add(new ImprovedRewardsBox(methods));
      randoms.add(new LostAndFound(methods));
      randoms.add(new Maze(methods));
      randoms.add(new Mime(methods));
      randoms.add(new Molly(methods));
      randoms.add(new Exam(methods));
      randoms.add(new Pillory(methods));
      randoms.add(new Pinball(methods));
      randoms.add(new Prison(methods));
      randoms.add(new QuizSolver(methods));
      randoms.add(new SandwhichLady(methods));
      randoms.add(new ScapeRuneIsland(methods));
      randoms.add(new TeleotherCloser(methods));
      randoms.add(new FirstTimeDeath(methods));
      randoms.add(new LeaveSafeArea(methods));
      randoms.add(new SystemUpdate(methods));
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void addScriptListener(final ScriptListener l) {
    sc.listeners.add(l);
  }

  public void removeScriptListener(final ScriptListener l) {
    sc.listeners.remove(l);
  }
  
  private void addScriptToPool(final Script ss, final Thread t) {
    for (int off = 0; off < sc.scripts.size(); ++off) {
      if (!sc.scripts.containsKey(off)) {
        sc.scripts.put(off, ss);
        ss.setID(off);
        sc.scriptThreads.put(off, t);
        return;
      }
    }
    ss.setID(sc.scripts.size());
    sc.scripts.put(sc.scripts.size(), ss);
    sc.scriptThreads.put(sc.scriptThreads.size(), t);
  }

  public final Collection<org.rsbot.script.Random> getRandoms() {
    return randoms;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> castMap(Map<?, ?> map, Class<K> kClass, Class<V> vClass) {
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      kClass.cast(entry.getKey());
      vClass.cast(entry.getValue());
    }
    return (Map<K, V>) map;
  }

  public final Map<Integer, Script> getRunningScripts() {
    return Collections.unmodifiableMap(sc.scripts);
  }

  public void pauseScript(final int id) {
    final Script s = sc.scripts.get(id);
    s.setPaused(!s.isPaused());
    if (s.isPaused())
      for (final ScriptListener l : sc.listeners)
        l.scriptPaused(this, s);
    else
      for (final ScriptListener l : sc.listeners)
        l.scriptResumed(this, s);
  }

  public void runScript(final Script script) {
    script.initialize(methods, sc);
    for (final ScriptListener l : sc.listeners)
      l.scriptStarted(this, script);
    final ScriptManifest prop = script.getClass().getAnnotation(ScriptManifest.class);
    final Thread t = new Thread(scriptThreadGroup, script, "Script-" + prop.name());
    addScriptToPool(script, t);
    t.start();
  }

  public void stopAllScripts() {
    final Set<Integer> theSet = sc.scripts.keySet();
    final int[] arr = new int[theSet.size()];
    int c = 0;
    for (final int i : theSet)
      arr[c++] = i;
    for (final int id : arr)
      stopScript(id);
  }

  public void stopScript(final int id) {
    final Script script = sc.scripts.get(id);
    if (script != null) {
      script.deactivate(id);
      sc.scripts.remove(id);
      sc.scriptThreads.remove(id);
      for (final ScriptListener l : sc.listeners)
        l.scriptStopped(this, script);
    }
  }

  public void stopScript() {
    final Thread curThread = Thread.currentThread();
    for (int i = 0; i < sc.scripts.size(); i++) {
      final Script script = sc.scripts.get(i);
      if (script != null && script.isRunning()) {
        if (sc.scriptThreads.get(i) == curThread)
          stopScript(i);
      }
    }
    if (curThread == null)
      throw new ThreadDeath();
  }

  protected void updateInput(final int mask) {
    for (final ScriptListener l : sc.listeners)
      l.inputChanged(methods.bot, mask);
  }

}
