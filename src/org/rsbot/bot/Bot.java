package org.rsbot.bot;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.EventListener;
import java.util.Map;
import java.util.TreeMap;

import org.rsbot.bot.Languages.Language;
import org.rsbot.client.Client;
import org.rsbot.client.input.Canvas;
import org.rsbot.event.EventManager;
import org.rsbot.event.events.PaintEvent;
import org.rsbot.event.events.TextPaintEvent;
import org.rsbot.gui.AccountManager;
import org.rsbot.gui.BotGUI;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.internal.InputManager;
import org.rsbot.script.internal.ScriptHandler;
import org.rsbot.script.methods.Environment;
import org.rsbot.script.provider.MethodContext;

public class Bot {
  private String                           account;
  private BotStub                          botStub;
  private final BotGUI                     gui;
  private Client                           client;
  private MethodContext                    methods;
  private Component                        panel;
  private final PaintEvent                 paintEvent;
  private final TextPaintEvent             textPaintEvent;
  private final EventManager               eventManager;
  private BufferedImage                    image, backBuffer;
  private final InputManager               im;
  private RSLoader                         loader;
  private final ScriptHandler              sh;
  private final Map<String, EventListener> listeners;
  public static final String THREADGROUPID = "RSClient-";

  /**
   * Whether or not user input is allowed despite a script's preference.
   */
  public volatile boolean                  overrideInput    = false;

  /**
   * Whether or not all anti-randoms are enabled.
   */
  public volatile boolean                  disableRandoms   = false;

  /**
   * Whether or not the login screen anti-random is enabled.
   */
  public volatile boolean                  disableAutoLogin = false;

  /**
   * Whether or not rendering is enabled.
   */
  public volatile boolean                  disableRendering = false;

  /**
   * Whether or not graphics are enabled.
   */
  public volatile boolean                  disableGraphics  = false;

  /**
   * Defines what types of input are enabled when overrideInput is false. Defaults to 'keyboard only' whenever a script is started.
   */
  public volatile int                      inputFlags       = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;

  public Bot(final BotGUI gui) {
    final Dimension d = gui.getPanel().getSize();
    this.gui = gui;
    im = new InputManager(this);
    loader = new RSLoader(d);
    loader.setCallback(new Runnable() {
      @Override
      public void run() {
        try {
          setClient((Client) loader.getClient());
          setSize(d);
          methods.menu.setupListener();
        } catch (final Exception ignored) {
        }
      }
    });
    sh = new ScriptHandler(methods);
    backBuffer = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    paintEvent = new PaintEvent();
    textPaintEvent = new TextPaintEvent();
    eventManager = new EventManager();
    listeners = new TreeMap<String, EventListener>();
  }

  public void start(final String server) {
    try {
      loader.paint(image.getGraphics());
      loader.load();
      if (loader.getTargetName() == null)
        return;
      Language enm = Language.toEnum(server);
      gui.getOutputManager().Server.setLocale(enm);
      botStub = new BotStub(loader, (enm == null ? Language.ENGLISH : enm));
      loader.setStub(botStub);
      eventManager.start();
      botStub.setActive(true);
      final ThreadGroup tg = new ThreadGroup(THREADGROUPID + hashCode());
      final Thread thread = new Thread(tg, loader, "Loader");
      thread.start();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    eventManager.killThread(false);
    sh.stopScript();
    loader.stop();
    loader.destroy();
    loader = null;
  }

  public final Dimension getSize() {
    return getLoader().getSize();
  }

  public void setSize(final int width, final int height) {
    final Dimension d = new Dimension(width, height);
    this.setSize(d);
  }

  public void setSize(final Dimension d) {
    backBuffer = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    // client reads size of loader applet for drawing
    loader.setSize(d);
    // simulate loader repaint awt event dispatch
    final Graphics g = backBuffer.getGraphics();
    loader.update(g);
    loader.paint(g);
  }

  public boolean setAccount(final String name) {
    account = null;
    for (final String s : AccountManager.getAccountNames()) {
      if (s.toLowerCase().equals(name.toLowerCase())) {
        account = name;
        break;
      }
    }
    return (account != null);
  }

  public final String getAccount() {
    return account;
  }

  public void setPanel(final Component c) {
    panel = c;
  }

  public void addListener(final Class<?> clazz) {
    final EventListener el = instantiateListener(clazz);
    listeners.put(clazz.getName(), el);
    eventManager.addListener(el);
  }

  public void removeListener(final Class<?> clazz) {
    final EventListener el = listeners.get(clazz.getName());
    listeners.remove(clazz.getName());
    eventManager.removeListener(el);
  }

  public final boolean hasListener(final Class<?> clazz) {
    return clazz != null && listeners.get(clazz.getName()) != null;
  }

  public final Client getClient() {
    return client;
  }

  public final Canvas getCanvas() {
    if (client == null)
      return null;
    return (Canvas) client.getCanvas();
  }

  public final Graphics getGraphics() {
    final Graphics back = backBuffer.getGraphics();
    if (disableGraphics) {
      paintEvent.graphics = null;
      textPaintEvent.graphics = null;
      eventManager.processEvent(paintEvent);
      eventManager.processEvent(textPaintEvent);
      return backBuffer.getGraphics();
    }
    paintEvent.graphics = back;
    textPaintEvent.graphics = back;
    textPaintEvent.idx = 0;
    eventManager.processEvent(paintEvent);
    eventManager.processEvent(textPaintEvent);
    back.dispose();
    image.getGraphics().drawImage(backBuffer, 0, 0, null);
    if (panel != null)
      panel.repaint();
    return backBuffer.getGraphics();
  }

  public final BufferedImage getImage() {
    return image;
  }

  public final BotStub getBotStub() {
    return botStub;
  }

  public final OutputManager getOutputManager() {
    return gui.getOutputManager();
  }

  public final RSLoader getLoader() {
    return loader;
  }

  public MethodContext getMethodContext() {
    return methods;
  }

  public final EventManager getEventManager() {
    return eventManager;
  }

  public final InputManager getInputManager() {
    return im;
  }

  public final ScriptHandler getScriptHandler() {
    return sh;
  }

  private void setClient(final Client cl) {
    client = cl;
    client.setCallback(new CallbackImpl(this));
    methods = new MethodContext(this);
    sh.init(methods);
  }

  private final EventListener instantiateListener(final Class<?> clazz) {
    try {
      EventListener listener;
      try {
        final Constructor<?> constructor = clazz.getConstructor(Bot.class);
        listener = (EventListener) constructor.newInstance(this);
      } catch (final Exception e) {
        listener = clazz.asSubclass(EventListener.class).newInstance();
      }
      return listener;
    } catch (final Exception ignored) {
    }
    return null;
  }
}