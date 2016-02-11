package org.rsbot.gui;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.rsbot.Configuration;
import org.rsbot.Configuration.OperatingSystem;
import org.rsbot.bot.Bot;
import org.rsbot.locale.OutputManager;
import org.rsbot.log.TextAreaLogHandler;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.internal.ScriptHandler;
import org.rsbot.script.internal.ScriptListener;
import org.rsbot.script.methods.Environment;
import org.rsbot.script.methods.ScreenShot;
import org.rsbot.script.provider.ScriptDownloader;
import org.rsbot.script.util.WindowUtil;
import org.rsbot.service.Preferences;
import org.rsbot.util.Timer;
import org.rsbot.util.VersionChecker;
import org.rsbot.util.io.IOHelper;

/**
 * @author Paris
 * @author Jacmob
 */
public class BotGUI extends JFrame implements ActionListener, ScriptListener {
  public static final int                   PANEL_WIDTH      = 765,
                                            PANEL_HEIGHT     = 503,
                                            LOG_HEIGHT       = 120;
  public static final int                   MAX_BOTS         = 6;
  private static final long                 serialVersionUID = -5411033752001988794L;
  private static final Logger               log              = Logger.getLogger(BotGUI.class.getName());
  private final SettingsManager             settings;
  private final Preferences                 preferences;
  private OutputManager                     om;
  private BotPanel                          panel;
  private BotToolBar                        toolBar;
  private BotMenuBar                        menuBar;
  private JScrollPane                       textScroll;
  private BotHome                           home;
  private final List<Bot>                   bots             = new ArrayList<Bot>();
  private TrayIcon                          tray             = null;
  private java.util.Timer                   shutdown         = null;

  public BotGUI() {
    init();
    pack();
    setTitle(null);
    setLocationRelativeTo(getOwner());
    setMinimumSize(new Dimension((int) (getSize().width * .8), (int) (getSize().height * .8)));
    setResizable(true);
    settings = new SettingsManager(this);
    preferences = settings.getPreferences();
    preferences.load();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        if (!preferences.hideAds)
          new Splash(BotGUI.this).display();
        addBot(settings.getPreferences().language.getName());
        updateScriptControls();
        setShutdownTimer(preferences.shutdown);
        System.gc();
      }
    });
    new java.util.Timer(true).schedule(new TimerTask() {
      @Override
      public void run() {
        System.gc();
      }
    }, Timer.Minutes(1), Timer.Minutes(10));
  }

  @Override
  public void setTitle(final String title) {
    String t = Configuration.NAME + " v" + VersionChecker.formatVersion(VersionChecker.getVersion());
    super.setTitle(t);
  }

  @Override
  public void actionPerformed(final ActionEvent evt) {
    final String action = evt.getActionCommand();
    final String menu, option;
    final int z = action.indexOf('.');
    if (z == -1) {
      menu = action;
      option = "";
    } else {
      menu = action.substring(0, z);
      option = action.substring(z + 1);
    }
    if (menu.equals(om.OS.toString("CLOSEBOT"))) {
      if (confirmRemoveBot()) {
        final int idx = Integer.parseInt(option);
        removeBot(bots.get(idx-1));
      }
    } else if (menu.equals(om.OS.toString("FILE"))) {
      if (option.equals(om.OS.toString("NEWBOT"))) {
        addBot(settings.getPreferences().language.getName());
      } else if (option.equals(om.OS.toString("CLOSEBOT"))) {
        if (confirmRemoveBot())
          removeBot(getCurrentBot());
      } else if (option.equals(om.OS.toString("ADDSCRIPT"))) {
        final String pretext = "";
        final String key = (String) JOptionPane.showInputDialog(this, om.OS.toString("showInputDialog"), option, JOptionPane.QUESTION_MESSAGE, null, null, pretext);
        if (!(key == null || key.trim().isEmpty()))
          ScriptDownloader.save(key);
      } else if (option.equals(om.OS.toString("RUNSCRIPT"))) {
        final Bot current = getCurrentBot();
        if (current != null)
          showScriptSelector(current);
      } else if (option.equals(om.OS.toString("STOPSCRIPT"))) {
        final Bot current = getCurrentBot();
        if (current != null)
          showStopScript(current);
      } else if (option.equals(om.OS.toString("PAUSESCRIPT"))) {
        final Bot current = getCurrentBot();
        if (current != null)
          pauseScript(current);
      } else if (option.equals(om.OS.toString("SAVESCREENSHOT"))) {
        final Bot current = getCurrentBot();
        if (current != null)
          ScreenShot.saveScreenshot(current, current.getMethodContext().game.isLoggedIn());
      } else if (option.equals(om.OS.toString("HIDEBOT"))) {
        setTray();
      } else if (option.equals(om.OS.toString("EXIT"))) {
        cleanExit(false);
      }
    } else if (menu.equals(om.OS.toString("EDIT"))) {
      if (option.equals(om.OS.toString("ACCOUNTS"))) {
        AccountManager.getInstance().showGUI();
      } else {
        final Bot current = getCurrentBot();
        if (current != null) {
          if (option.equals(om.OS.toString("FORCEINPUT"))) {
            current.overrideInput = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
            updateScriptControls();
          } else if (option.equals(om.OS.toString("LESSCPU"))) {
            lessCpu(((JCheckBoxMenuItem) evt.getSource()).isSelected());
          } else if (option.equals(om.OS.toString("EXTDVIEWS"))) {
            menuBar.setExtendedView(((JCheckBoxMenuItem) evt.getSource()).isSelected());
          } else if (option.equals(om.OS.toString("DISABLERENDERING"))) {
            current.disableRendering = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
          } else if (option.equals(om.OS.toString("DISABLEANTIRANDOMS"))) {
            current.disableRandoms = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
          } else if (option.equals(om.OS.toString("DISABLEAUTOLOGIN"))) {
            current.disableAutoLogin = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
          }
        }
      }
    } else if (menu.equals(om.OS.toString("VIEW"))) {
      final Bot current = getCurrentBot();
      final boolean selected = ((JCheckBoxMenuItem) evt.getSource()).isSelected();
      if (option.equals(om.OS.toString("HIDETOOLBAR"))) {
        toggleViewState(toolBar, selected);
      } else if (option.equals(om.OS.toString("HIDELOGPANE"))) {
        toggleViewState(textScroll, selected);
      } else if (current != null) {
        if (option.equals(om.OS.toString("ALLDEBUGGING"))) {
          for (final Object key : BotMenuBar.DEBUG_MAP.keySet()) {
            final Class<?> el = BotMenuBar.DEBUG_MAP.get(key);
            if (menuBar.getCheckBox((String) key).isVisible()) {
              final boolean wasSelected = menuBar.getCheckBox((String) key).isSelected();
              menuBar.getCheckBox((String) key).setSelected(selected);
              if (selected) {
                if (!wasSelected)
                  current.addListener(el);
              } else {
                if (wasSelected)
                  current.removeListener(el);
              }
            }
          }
        } else {
          final Class<?> el = BotMenuBar.DEBUG_MAP.get(option);
          menuBar.getCheckBox(option).setSelected(selected);
          if (selected)
            current.addListener(el);
          else {
            menuBar.getCheckBox(om.OS.toString("ALLDEBUGGING")).setSelected(false);
            current.removeListener(el);
          }
        }
      }
    } else if (menu.equals(om.OS.toString("TOOLS"))) {
      if (option.equals(om.OS.toString("CLEARCACHE"))) {
        final int result = JOptionPane.showConfirmDialog(this, om.OS.toString("showConfirmDialog.0"), option, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
          IOHelper.recursiveDelete(new File(Configuration.Paths.getCacheDirectory()), false);
          IOHelper.recursiveDelete(new File(Configuration.Paths.getSettingsDirectory()), false);
          log.info(om.OS.toString("showConfirmDialog.1"));
        }
      } else if (option.equals(om.OS.toString("OPTIONS"))) {
        settings.display();
      }
    } else if (menu.equals(om.OS.toString("HELP"))) {
      if (option.equals(om.OS.toString("SITE")))
        openURL(Configuration.Paths.URLs.BASE1);
      else if (option.equals(om.OS.toString("ABOUT"))) {
        JOptionPane.showMessageDialog(this, new String[] { om.OS.toString("showMessageDialog.0"), om.OS.toString("showMessageDialog.1") }, option, JOptionPane.INFORMATION_MESSAGE);
      }
    } else if (menu.equals(om.OS.toString("SERVERS"))) {
      removeBot(getCurrentBot());
      addBot(option);
    } else if (menu.equals(om.OS.toString("TAB"))) {
      final Bot curr = getCurrentBot();
      menuBar.setBot(curr);
      panel.setBot(curr);
      panel.repaint();
      setTitle(curr == null ? null : curr.getAccount());
      updateScriptControls();
    }
  }

  public void updateScriptControls() {
    boolean idle = true, paused = false;
    final Bot bot = getCurrentBot();

    if (bot != null && bot.getScriptHandler() != null) {
      final Map<Integer, Script> scriptMap = bot.getScriptHandler().getRunningScripts();
      if (scriptMap != null && !scriptMap.isEmpty()) {
        idle = false;
        paused = scriptMap.values().iterator().next().isPaused();
      } else
        idle = true;
    }

    BotMenuBar.getMenuItem(om.OS.toString("RUNSCRIPT")).setVisible(idle);
    BotMenuBar.getMenuItem(om.OS.toString("STOPSCRIPT")).setVisible(!idle);
    BotMenuBar.getMenuItem(om.OS.toString("PAUSESCRIPT")).setEnabled(!idle);
    menuBar.setPauseScript(paused);
    toolBar.setInputButtonVisible(!idle);
    menuBar.setEnabled(om.OS.toString("FORCEINPUT"), !idle);

    if (idle) {
      toolBar.setOverrideInput(false);
      menuBar.setOverrideInput(false);
      toolBar.setInputState(Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE);
      toolBar.setScriptButton(BotToolBar.ScriptStatus.RUN);
    } else {
      toolBar.setOverrideInput(bot.overrideInput);
      toolBar.setOverrideInput(bot.overrideInput);
      toolBar.setInputState(bot.inputFlags);
      toolBar.setScriptButton(paused ? BotToolBar.ScriptStatus.RESUME : BotToolBar.ScriptStatus.PAUSE);
    }

    toolBar.updateInputButton();
  }
  
  public final BotPanel getPanel() {
    return panel;
  }
  
  public final Bot getBot(final Object o) {
    final ClassLoader cl = o.getClass().getClassLoader();
    for (final Bot bot : bots) {
      if (cl == bot.getLoader().getClient().getClass().getClassLoader()) {
        panel.offset();
        return bot;
      }
    }
    return null;
  }

  public void addBot(final String language) {
    if (bots.size() > MAX_BOTS)
      return;
    if (!Configuration.RUNNING_FROM_JAR)
      log.info(om.OS.toString("LANGUAGE") + ": " + language);
    final Bot bot = new Bot(this);
    bots.add(bot);
    toolBar.addTab();
    toolBar.setAddTabVisible(bots.size() < MAX_BOTS);
    new Thread(new Runnable() {
      @Override
      public void run() {
        bot.start(language);
        home.setBots(bots);
      }
    }).start();
  }

  public void removeBot(final Bot bot) {
    final int idx = bots.indexOf(bot);
    bot.getScriptHandler().stopAllScripts();
    bot.getScriptHandler().removeScriptListener(this);
    toolBar.removeTab(idx);
    bots.remove(idx);
    home.setBots(bots); // 0 = home
    toolBar.setAddTabVisible(bots.size() < MAX_BOTS);
    repaint();
    new Thread(new Runnable() {
      @Override
      public void run() {
        bot.stop();
        System.gc();
        toolBar.setHome(bots.isEmpty());
      }
    }).start();
  }
  
  static void pauseScript(final Bot bot) {
    final ScriptHandler sh = bot.getScriptHandler();
    final Map<Integer, Script> running = sh.getRunningScripts();
    if (running.size() > 0) {
      final int id = running.keySet().iterator().next();
      sh.pauseScript(id);
    }
  }
  
  public final Bot getCurrentBot() {
    final int idx = toolBar.getCurrentTab();
    return (idx > 0 ? bots.get(idx - 1) : null);
  }

  private void showScriptSelector(final Bot bot) {
    if (AccountManager.getAccountNames() == null || AccountManager.getAccountNames().length == 0) {
      JOptionPane.showMessageDialog(this, om.OS.toString("showMessageDialog"));
      AccountManager.getInstance().showGUI();
    } else if (bot.getMethodContext() == null)
      log.warning(om.OS.toString("log.Loading"));
    else
      new ScriptSelector(this, bot).showGUI();
  }

  private void showStopScript(final Bot bot) {
    final ScriptHandler sh = bot.getScriptHandler();
    final Map<Integer, Script> running = sh.getRunningScripts();
    if (!running.isEmpty()) {
      final int id = running.keySet().iterator().next();
      final Script s = running.get(id);
      final ScriptManifest prop = s.getClass().getAnnotation(ScriptManifest.class);
      final int result = JOptionPane.showConfirmDialog(this, om.OS.toString("showConfirmDialog.2") + " '" + prop.name() + "'?", om.OS.toString("SCRIPT"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (result == JOptionPane.OK_OPTION) {
        sh.stopScript(id);
        updateScriptControls();
      }
    }
  }

  private void toggleViewState(final Component component, final boolean visible) {
    final Dimension size = getSize();
    size.height += component.getSize().height * (visible ? -1 : 1);
    component.setVisible(!visible);
    setMinimumSize(size);
    if ((getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH)
      pack();
  }
  
  private void lessCpu(boolean on) {
    if (getCurrentBot() != null) {
      disableRendering(on || menuBar.isTicked(om.OS.toString("LESSCPU")));
      disableGraphics(on || menuBar.isTicked(om.OS.toString("DISABLECANVAS")));
    }
  }

  public void disableRendering(final boolean mode) {
    for (final Bot bot : bots)
      bot.disableRendering = mode;
  }

  public void disableGraphics(final boolean mode) {
    for (final Bot bot : bots)
      bot.disableGraphics = mode;
  }
  
  public final OutputManager getOutputManager() {
    return om;
  }

  public final BotToolBar getToolbar() {
    return this.toolBar;
  }

  private void init() {
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        if (cleanExit(false))
          dispose();
      }
    });
    addWindowStateListener(new WindowStateListener() {
      @Override
      public void windowStateChanged(final WindowEvent arg0) {
        switch (arg0.getID()) {
          case WindowEvent.WINDOW_ICONIFIED:
            lessCpu(true);
            break;
          case WindowEvent.WINDOW_DEICONIFIED:
            lessCpu(false);
            break;
        }
      }
    });
    setIconImage(Configuration.getImage(Configuration.Paths.Resources.ICON));
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    WindowUtil.setFrame(this);
    home = new BotHome();
    panel = new BotPanel(home);
    om = new OutputManager(getLocale());
    menuBar = new BotMenuBar(this);
    toolBar = new BotToolBar(this, menuBar);
    panel.setFocusTraversalKeys(0, new HashSet<AWTKeyStroke>());
    menuBar.setBot(null);
    setJMenuBar(menuBar);
    textScroll = new JScrollPane(TextAreaLogHandler.TEXT_AREA, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    textScroll.setBorder(null);
    textScroll.setPreferredSize(new Dimension(PANEL_WIDTH, LOG_HEIGHT));
    textScroll.setVisible(true);
    JScrollPane scrollableBotPanel = new JScrollPane(panel);
    add(toolBar, BorderLayout.NORTH);
    add(scrollableBotPanel, BorderLayout.CENTER);
    add(textScroll, BorderLayout.SOUTH);
  }

  @Override
  public void scriptStarted(final ScriptHandler handler, final Script script) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        final Bot bot = handler.getMethodContext().bot;
        if (bot == getCurrentBot()) {
          bot.inputFlags = Environment.INPUT_KEYBOARD;
          bot.overrideInput = false;
          updateScriptControls();
          final String acct = bot.getAccount();
          toolBar.setTabLabel(bots.indexOf(bot), acct == null ? om.OS.toString("TABDEFAULTTEXT") : acct);
          setTitle(acct);
        }
      }
    });
  }

  @Override
  public void scriptStopped(final ScriptHandler handler, final Script script) {
    final Bot bot = handler.getMethodContext().bot;
    if (bot == getCurrentBot()) {
      bot.inputFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;
      bot.overrideInput = false;
      updateScriptControls();
      toolBar.setTabLabel(bots.indexOf(bot), om.OS.toString("TABDEFAULTTEXT"));
      setTitle(null);
    }
  }

  @Override
  public void scriptResumed(final ScriptHandler handler, final Script script) {
    if (handler.getMethodContext().bot == getCurrentBot())
      updateScriptControls();
  }

  @Override
  public void scriptPaused(final ScriptHandler handler, final Script script) {
    if (handler.getMethodContext().bot == getCurrentBot())
      updateScriptControls();
  }

  @Override
  public void inputChanged(final Bot bot, final int mask) {
    bot.inputFlags = mask;
    toolBar.setInputState(mask);
    updateScriptControls();
  }

  public void openURL(final String url) {
    final Configuration.OperatingSystem os = Configuration.getCurrentOperatingSystem();
    try {
      if (os == Configuration.OperatingSystem.MAC) {
        final Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
        final Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
        openURL.invoke(null, url);
      } else if (os == Configuration.OperatingSystem.WINDOWS) {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
      } else {
        final String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "google-chrome", "chromium-browser" };
        String browser = null;
        for (int count = 0; count < browsers.length && browser == null; count++) {
          if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
            browser = browsers[count];
        }
        if (browser == null)
          throw new Exception(om.OS.toString("Exception.Web"));
        Runtime.getRuntime().exec(new String[] { browser, url });
      }
    } catch (final Exception e) {
      log.warning(om.OS.toString("Exception.Open") + " '" + url + "'");
    }
  }

  private boolean confirmRemoveBot() {
    if (!preferences.confirmations) {
      final int result = JOptionPane.showConfirmDialog(this, om.OS.toString("showConfirmDialog.3"), om.OS.toString("CLOSEBOT"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      return result == JOptionPane.OK_OPTION;
    }
    return true;
  }

  public void setShutdownTimer(final boolean enabled) {
    if (!enabled) {
      if (shutdown != null) {
        shutdown.cancel();
        shutdown.purge();
      }
      shutdown = null;
    } else {
      final long interval = preferences.shutdownTime * Timer.Minutes(1);
      shutdown = new java.util.Timer(true);
      shutdown.schedule(new TimerTask() {
        @Override
        public void run() {
          for (final Bot bot : bots) {
            if (bot.getScriptHandler().getRunningScripts().size() != 0)
              return;
          }
          final int delay = 3;
          log.info(om.OS.toString("log.ShutdownPending") + " " + delay + " " + om.OS.toString("Minutes") + "...");
          final Point[] mouse = new Point[] { MouseInfo.getPointerInfo().getLocation(), null };
          try {
            Thread.sleep(delay * Timer.Minutes(1));
          } catch (InterruptedException ignored) {
          }
          mouse[1] = MouseInfo.getPointerInfo().getLocation();
          if (mouse[0].x != mouse[1].x || mouse[0].y != mouse[1].y) {
            log.info(om.OS.toString("log.MouseInfo"));
          } else if (!preferences.shutdown) {
            log.info(om.OS.toString("log.MouseShutdownCancel"));
          } else if (Configuration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS) {
            try {
              Runtime.getRuntime().exec("shutdown.exe", new String[] { "-s" });
              cleanExit(true);
            } catch (IOException ignored) {
              log.severe(om.OS.toString("log.ShutdownFatal"));
            }
          }
        }
      }, interval, interval);
    }
  }

  public boolean cleanExit(final boolean silent) {
    if (silent)
      preferences.confirmations = true;
    if (!preferences.confirmations) {
      preferences.confirmations = true;
      for (final Bot bot : bots) {
        if (bot.getAccount() != null) {
          preferences.confirmations = true;
          break;
        }
      }
    }
    boolean doExit = true;
    if (!preferences.confirmations) {
      final int result = JOptionPane.showConfirmDialog(this, om.OS.toString("showConfirmDialog.4"), om.OS.toString("EXIT"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (result != JOptionPane.OK_OPTION)
        doExit = false;
    }
    if (doExit) {
      setVisible(false);
      preferences.save();
      System.exit(0);
    }
    return doExit;
  }

  public void setTray() {
    if (tray == null) {
      final Image image = Configuration.getImage(Configuration.Paths.Resources.ICON);
      tray = new TrayIcon(image, Configuration.NAME, null);
      tray.setImageAutoSize(true);
      tray.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
          SystemTray.getSystemTray().remove(tray);
          setVisible(true);
          lessCpu(false);
        }
      });
    }
    try {
      SystemTray.getSystemTray().add(tray);
      tray.displayMessage(Configuration.NAME + " Hidden", om.OS.toString("displayMessage.tray"), MessageType.INFO);
    } catch (Exception ignored) {
      log.warning(om.OS.toString("Exception.Tray"));
    }
    setVisible(false);
    lessCpu(true);
  }
}
