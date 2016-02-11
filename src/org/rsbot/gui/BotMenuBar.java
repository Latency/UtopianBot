package org.rsbot.gui;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.rsbot.Configuration;
import org.rsbot.bot.Bot;
import org.rsbot.bot.Languages.Language;
import org.rsbot.event.impl.DrawBoundaries;
import org.rsbot.event.impl.DrawGround;
import org.rsbot.event.impl.DrawInventory;
import org.rsbot.event.impl.DrawItems;
import org.rsbot.event.impl.DrawModel;
import org.rsbot.event.impl.DrawMouse;
import org.rsbot.event.impl.DrawNPCs;
import org.rsbot.event.impl.DrawObjects;
import org.rsbot.event.impl.DrawPlayers;
import org.rsbot.event.impl.DrawSettings;
import org.rsbot.event.impl.MessageLogger;
import org.rsbot.event.impl.TAnimation;
import org.rsbot.event.impl.TCamera;
import org.rsbot.event.impl.TFPS;
import org.rsbot.event.impl.TFloorHeight;
import org.rsbot.event.impl.TLoginIndex;
import org.rsbot.event.impl.TMenu;
import org.rsbot.event.impl.TMenuActions;
import org.rsbot.event.impl.TMousePosition;
import org.rsbot.event.impl.TPlayerPosition;
import org.rsbot.event.impl.TTab;
import org.rsbot.event.impl.TUserInputAllowed;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.event.listeners.TextPaintListener;
import org.rsbot.locale.OutputManager;

public class BotMenuBar extends JMenuBar {
  private static final long                    serialVersionUID  = 971579975301998332L;
  public static final Map<String, Class<?>>    DEBUG_MAP         = new LinkedHashMap<String, Class<?>>();
  public static final ArrayList<String>        TITLES            = new ArrayList<String>();
  public static final Map<String, String[]>    ELEMENTS          = new HashMap<String, String[]>();
  private static final Logger                  log               = Logger.getLogger(BotMenuBar.class.getName());
  private static final boolean                 EXTD_VIEW_INITIAL = !Configuration.RUNNING_FROM_JAR;
  private final OutputManager                  om;
  private final Map<String, JCheckBoxMenuItem> eventCheckMap     = new HashMap<String, JCheckBoxMenuItem>(),
                                               commandCheckMap   = new HashMap<String, JCheckBoxMenuItem>();
  private final static Map<String, JMenuItem>  commandMenuItem   = new HashMap<String, JMenuItem>();
  private final ActionListener                 listener;

  public BotMenuBar(final BotGUI gui) {
    this.om = gui.getOutputManager();
    this.listener = gui;

    TITLES.add(om.OS.toString("FILE"));
    TITLES.add(om.OS.toString("EDIT"));
    TITLES.add(om.OS.toString("VIEW"));
    TITLES.add(om.OS.toString("TOOLS"));
    TITLES.add(om.OS.toString("SERVERS"));
    TITLES.add(om.OS.toString("HELP"));

    ELEMENTS.put(TITLES.get(0), new String[] {
        om.OS.toString("NEWBOT"),
        om.OS.toString("CLOSEBOT"),
        om.OS.toString("MENUSEPERATOR"),
        om.OS.toString("ADDSCRIPT"),
        om.OS.toString("RUNSCRIPT"),
        om.OS.toString("STOPSCRIPT"),
        om.OS.toString("PAUSESCRIPT"),
        om.OS.toString("MENUSEPERATOR"),
        om.OS.toString("SAVESCREENSHOT"),
        om.OS.toString("MENUSEPERATOR"),
        om.OS.toString("HIDEBOT"),
        om.OS.toString("EXIT")
    });
    ELEMENTS.put(TITLES.get(1), new String[] {
        om.OS.toString("ACCOUNTS"),
        om.OS.toString("MENUSEPERATOR"),
        om.OS.toString("TOGGLEFALSE") + om.OS.toString("FORCEINPUT"),
        om.OS.toString("TOGGLEFALSE") + om.OS.toString("LESSCPU"),
        (EXTD_VIEW_INITIAL ? om.OS.toString("TOGGLETRUE") : om.OS.toString("TOGGLEFALSE")) + om.OS.toString("EXTDVIEWS"),
        om.OS.toString("MENUSEPERATOR"),
        om.OS.toString("TOGGLEFALSE") + om.OS.toString("DISABLECANVAS"),
        om.OS.toString("TOGGLEFALSE") + om.OS.toString("DISABLEANTIRANDOMS"),
        om.OS.toString("TOGGLEFALSE") + om.OS.toString("DISABLEAUTOLOGIN")
    });
    ELEMENTS.put(TITLES.get(2), constructDebugs());
    ELEMENTS.put(TITLES.get(3), new String[] {
        om.OS.toString("CLEARCACHE"),
        om.OS.toString("OPTIONS")
    });
    ELEMENTS.put(TITLES.get(4), Language.toArrayFormal());
    ELEMENTS.put(TITLES.get(5), new String[] {
        om.OS.toString("SITE"),
        om.OS.toString("ABOUT")
    });

    for (final String title : TITLES.toArray(new String[TITLES.size()]))
      add(constructMenu(title, ELEMENTS.get(title)));

    // Construct Icons
    final Map<String, String> map = new HashMap<String, String>();
    map.put(om.OS.toString("NEWBOT"), Configuration.Paths.Resources.ICON_APPADD);
    map.put(om.OS.toString("CLOSEBOT"), Configuration.Paths.Resources.ICON_APPDELETE);
    map.put(om.OS.toString("ADDSCRIPT"), Configuration.Paths.Resources.ICON_SCRIPT_ADD);
    map.put(om.OS.toString("RUNSCRIPT"), Configuration.Paths.Resources.ICON_PLAY);
    map.put(om.OS.toString("STOPSCRIPT"), Configuration.Paths.Resources.ICON_DELETE);
    map.put(om.OS.toString("PAUSESCRIPT"), Configuration.Paths.Resources.ICON_PAUSE);
    map.put(om.OS.toString("SAVESCREENSHOT"), Configuration.Paths.Resources.ICON_PHOTO);
    map.put(om.OS.toString("HIDEBOT"), Configuration.Paths.Resources.ICON_ARROWIN);
    map.put(om.OS.toString("EXIT"), Configuration.Paths.Resources.ICON_CLOSE);
    map.put(om.OS.toString("CLEARCACHE"), Configuration.Paths.Resources.DATABASE_ERROR);
    map.put(om.OS.toString("OPTIONS"), Configuration.Paths.Resources.ICON_WRENCH);
    map.put(om.OS.toString("SITE"), Configuration.Paths.Resources.ICON_WEBLINK);
    map.put(om.OS.toString("ABOUT"), Configuration.Paths.Resources.ICON_INFO);

    for (final Entry<String, String> item : map.entrySet()) {
      final JMenuItem menu = commandMenuItem.get(item.getKey());
      menu.setIcon(new ImageIcon(Configuration.getImage(item.getValue())));
    }

    for (final Language item : Language.values()) {
      final JMenuItem menu = commandMenuItem.get(item.getName());
      menu.setIcon(new ImageIcon(Configuration.getFlag(item.getFlag())));
    }

    // Misc.
    commandMenuItem.get(om.OS.toString("HIDEBOT")).setVisible(SystemTray.isSupported());
    setExtendedView(EXTD_VIEW_INITIAL);
  }

  public void setExtendedView(final boolean show) {
    for (String disableFeature : DEBUG_MAP.keySet()) {
      if (commandCheckMap.containsKey(disableFeature))
        commandCheckMap.get(disableFeature).setVisible(show);
    }
  }
  
  public final OutputManager getOutputManager() {
    return om;
  }

  private final String[] constructDebugs() {
    // Text
    DEBUG_MAP.put(om.OS.toString("viewMenu.TLoginIndex"), TLoginIndex.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TTab"), TTab.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TCamera"), TCamera.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TAnimation"), TAnimation.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TFloorHeight"), TFloorHeight.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TPlayerPosition"), TPlayerPosition.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TMousePosition"), TMousePosition.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TUserInputAllowed"), TUserInputAllowed.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TMenuActions"), TMenuActions.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TMenu"), TMenu.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.TFPS"), TFPS.class);

    // Paint
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawPlayers"), DrawPlayers.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawNPCs"), DrawNPCs.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawObjects"), DrawObjects.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawModel"), DrawModel.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawMouse"), DrawMouse.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawInventory"), DrawInventory.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawGround"), DrawGround.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawItems"), DrawItems.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawBoundaries"), DrawBoundaries.class);
    DEBUG_MAP.put(om.OS.toString("viewMenu.DrawSettings"), DrawSettings.class);

    // Other
    DEBUG_MAP.put(om.OS.toString("viewMenu.MessageLogger"), MessageLogger.class);

    final List<String> debugItems = new ArrayList<String>();
    debugItems.add(om.OS.toString("HIDETOOLBAR"));
    debugItems.add(om.OS.toString("HIDELOGPANE"));
    debugItems.add(om.OS.toString("ALLDEBUGGING"));
    debugItems.add(om.OS.toString("MENUSEPERATOR"));
    for (final String key : DEBUG_MAP.keySet()) {
      final Class<?> el = DEBUG_MAP.get(key);
      if (PaintListener.class.isAssignableFrom(el))
        debugItems.add(key);
    }
    debugItems.add(om.OS.toString("MENUSEPERATOR"));
    for (final String key : DEBUG_MAP.keySet()) {
      final Class<?> el = DEBUG_MAP.get(key);
      if (TextPaintListener.class.isAssignableFrom(el))
        debugItems.add(key);
    }
    debugItems.add(om.OS.toString("MENUSEPERATOR"));
    for (final String key : DEBUG_MAP.keySet()) {
      final Class<?> el = DEBUG_MAP.get(key);
      if (!TextPaintListener.class.isAssignableFrom(el) && !PaintListener.class.isAssignableFrom(el))
        debugItems.add(key);
    }
    for (final ListIterator<String> it = debugItems.listIterator(); it.hasNext();) {
      final String s = it.next();
      if (!s.equals(om.OS.toString("MENUSEPERATOR")))
        it.set(om.OS.toString("TOGGLEFALSE") + s);
    }
    return debugItems.toArray(new String[debugItems.size()]);
  }

  public void setOverrideInput(final boolean force) {
    commandCheckMap.get(om.OS.toString("FORCEINPUT")).setSelected(force);
  }

  public void setPauseScript(final boolean pause) {
    final JMenuItem item = commandMenuItem.get(om.OS.toString("PAUSESCRIPT"));
    item.setText(pause ? om.OS.toString("RESUMESCRIPT") : om.OS.toString("PAUSESCRIPT"));
    final Image image = Configuration.getImage(pause ? Configuration.Paths.Resources.ICON_START : Configuration.Paths.Resources.ICON_PAUSE);
    if (image != null)
      item.setIcon(new ImageIcon(image));
  }

  public static JMenuItem getMenuItem(final String name) {
    return commandMenuItem.get(name);
  }

  public void setBot(final Bot bot) {
    if (bot == null) {
      commandMenuItem.get(om.OS.toString("CLOSEBOT")).setEnabled(false);
      commandMenuItem.get(om.OS.toString("RUNSCRIPT")).setEnabled(false);
      commandMenuItem.get(om.OS.toString("STOPSCRIPT")).setEnabled(false);
      commandMenuItem.get(om.OS.toString("PAUSESCRIPT")).setEnabled(false);
      commandMenuItem.get(om.OS.toString("SAVESCREENSHOT")).setEnabled(false);
      for (final JCheckBoxMenuItem item : eventCheckMap.values()) {
        item.setSelected(false);
        item.setEnabled(false);
      }
      disable(om.OS.toString("ALLDEBUGGING"), om.OS.toString("FORCEINPUT"), om.OS.toString("LESSCPU"), om.OS.toString("DISABLEANTIRANDOMS"), om.OS.toString("DISABLEAUTOLOGIN"));
    } else {
      commandMenuItem.get(om.OS.toString("CLOSEBOT")).setEnabled(true);
      commandMenuItem.get(om.OS.toString("RUNSCRIPT")).setEnabled(true);
      commandMenuItem.get(om.OS.toString("STOPSCRIPT")).setEnabled(true);
      commandMenuItem.get(om.OS.toString("PAUSESCRIPT")).setEnabled(true);
      commandMenuItem.get(om.OS.toString("SAVESCREENSHOT")).setEnabled(true);
      int selections = 0;
      for (final Map.Entry<String, JCheckBoxMenuItem> entry : eventCheckMap.entrySet()) {
        entry.getValue().setEnabled(true);
        final boolean selected = bot.hasListener(DEBUG_MAP.get(entry.getKey()));
        entry.getValue().setSelected(selected);
        if (selected)
          ++selections;
      }
      enable(om.OS.toString("ALLDEBUGGING"), selections == eventCheckMap.size());
      enable(om.OS.toString("FORCEINPUT"), bot.overrideInput);
      enable(om.OS.toString("LESSCPU"), bot.disableRendering);
      enable(om.OS.toString("DISABLECANVAS"), bot.disableGraphics);
      enable(om.OS.toString("DISABLEANTIRANDOMS"), bot.disableRandoms);
      enable(om.OS.toString("DISABLEAUTOLOGIN"), bot.disableAutoLogin);
    }
  }

  public JCheckBoxMenuItem getCheckBox(final String key) {
    return commandCheckMap.get(key);
  }

  private void disable(final String... items) {
    for (final String item : items) {
      try {
        commandCheckMap.get(item).setSelected(false);
        commandCheckMap.get(item).setEnabled(false);
      } catch (Exception e) {
        e.printStackTrace();
        log.severe("Unable to get item '" + item + "' from commandCheckMap.");
      }
    }
  }

  public void enable(final String item, final boolean selected) {
    commandCheckMap.get(item).setSelected(selected);
    commandCheckMap.get(item).setEnabled(true);
  }

  public void setEnabled(final String item, final boolean mode) {
    commandCheckMap.get(item).setEnabled(mode);
  }

  public static void doClick(final String item) {
    commandMenuItem.get(item).doClick();
  }

  public void doTick(final String item) {
    commandCheckMap.get(item).doClick();
  }

  public boolean isTicked(final String item) {
    return commandCheckMap.get(item).isSelected();
  }

  private JMenu constructMenu(final String title, final String[] elems) {
    final JMenu menu = new JMenu(title);
    for (String e : elems) {
      if (e.equals(om.OS.toString("MENUSEPERATOR"))) {
        menu.add(new JSeparator());
      } else {
        JMenuItem jmi;
        if (e.startsWith(om.OS.toString("TOGGLE"))) {
          e = e.substring(om.OS.toString("TOGGLE").length());
          final char state = e.charAt(1);
          e = e.substring(2);
          jmi = new JCheckBoxMenuItem(e);
          if (state == 't' || state == 'T')
            jmi.setSelected(true);
          if (DEBUG_MAP.containsKey(e)) {
            final JCheckBoxMenuItem ji = (JCheckBoxMenuItem) jmi;
            eventCheckMap.put(e, ji);
          }
          final JCheckBoxMenuItem ji = (JCheckBoxMenuItem) jmi;
          commandCheckMap.put(e, ji);
        } else {
          jmi = new JMenuItem(e);
          commandMenuItem.put(e, jmi);
        }
        jmi.addActionListener(listener);
        jmi.setActionCommand(title + "." + e);
        menu.add(jmi);
      }
    }
    return menu;
  }
}