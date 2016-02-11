package org.rsbot.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.rsbot.Configuration;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.methods.Environment;
import org.rsbot.util.StringUtil;

/**
 * @author Paris
 */
public class BotToolBar extends JToolBar {
  public static int         shutdown_timer   = 0;
  private static final long serialVersionUID = -1861866523519184211L;

  public enum ScriptStatus {
    RUN, PAUSE, RESUME
  }

  private static final ImageIcon        ICON_HOME     = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_HOME)),
                                        ICON_BOT      = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_BOT)),
                                        ICON_PLAY     = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_PLAY)),
                                        ICON_PAUSE    = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_PAUSE)),
                                        ICON_INFO     = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_INFO)),
                                        ICON_START    = new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_START));

  private static Image                  IMAGE_CLOSE,
                                        IMAGE_CLOSE_OVER = Configuration.getImage(Configuration.Paths.Resources.ICON_CLOSE);

  private final AddButton               addTabButton;
  private final JButton                 screenshotButton, userInputButton, runScriptButton, stopScriptButton, shutdownLabel;
  private final HomeButton              homeButton;

  private final Map<Integer, BotButton> tabMap        = new HashMap<Integer, BotButton>(BotGUI.MAX_BOTS);
  private final ActionListener          listener;                                                                                                  // From BotGUI.java
  private int                           tab_index     = 0;
  private int                           inputState    = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;
  private boolean                       inputOverride = true;
  
  private final OutputManager           om;

  public BotToolBar(final ActionListener listener, final BotMenuBar menu) {
    if (listener == null)
      throw new NullPointerException("ActionListener not initialized in " + BotToolBar.class.getName() + ".");

    try {
      IMAGE_CLOSE = getTransparentImage(Configuration.getResourceURL(Configuration.Paths.Resources.ICON_CLOSE), 0.5f);
    } catch (final MalformedURLException e) {
    }

    this.om = menu.getOutputManager();
    this.listener = listener;

    // ///////////////////////////////////////////////////
    // SHUTDOWN LABEL TIMER
    // ///////////////////////////////////////////////////
    shutdownLabel = new JButton(ICON_INFO);
    shutdownLabel.setName("ShutdownLabel");
    shutdownLabel.setFocusable(false);
    shutdownLabel.setBorderPainted(false);
    shutdownLabel.setEnabled(false);
    shutdownLabel.setVisible(false);

    /*
    class ShutdownLoop extends Thread {
      private final long timeout;

      ShutdownLoop(final long timeout) {
        this.timeout = timeout;
      }

      @Override
      public void run() {
        final Timer timer = new Timer(timeout);
        while (timer.isRunning()) {
          shutdownLabel.setText("Shutdown in " + Timer.format(timer.getRemaining()));
          try {
            Thread.sleep(Timer.Seconds(1));
          } catch (InterruptedException e) {
            break;
          }
        }
        shutdownLabel.setVisible(false);
      }
    }

    shutdownLabel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        shutdownLabel.setVisible(true);
        final ShutdownLoop sl = new ShutdownLoop(BotToolBar.shutdown_timer);
        sl.start();
      }
    });
*/
    
    // ///////////////////////////////////////////////////
    // SCREENSHOT BUTTON
    // ///////////////////////////////////////////////////
    screenshotButton = new JButton("Screenshot", new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_PHOTO)));
    screenshotButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        BotMenuBar.doClick(om.OS.toString("SAVESCREENSHOT"));
      }
    });
    screenshotButton.setFocusable(false);
    screenshotButton.setToolTipText(screenshotButton.getText());
    screenshotButton.setText("");

    // ///////////////////////////////////////////////////
    // STOPSCRIPT BUTTON
    // ///////////////////////////////////////////////////
    stopScriptButton = new JButton("Stop", new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_DELETE)));
    stopScriptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        BotMenuBar.doClick(om.OS.toString("STOPSCRIPT"));
      }
    });
    stopScriptButton.setFocusable(false);
    stopScriptButton.setToolTipText(stopScriptButton.getText());
    stopScriptButton.setText("");

    // ///////////////////////////////////////////////////
    // RUNSCRIPT BUTTON
    // ///////////////////////////////////////////////////
    runScriptButton = new JButton("Run", new ImageIcon(Configuration.getImage(Configuration.Paths.Resources.ICON_PLAY)));
    runScriptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        switch (getScriptButton()) {
          case RUN:
            BotMenuBar.doClick(om.OS.toString("RUNSCRIPT"));
            break;
          case RESUME:
          case PAUSE:
            BotMenuBar.doClick(om.OS.toString("PAUSESCRIPT"));
            break;
        }
      }
    });
    runScriptButton.setFocusable(false);
    runScriptButton.setToolTipText(runScriptButton.getText());
    runScriptButton.setText("");

    // ///////////////////////////////////////////////////
    // USERINPUT BUTTON
    // ///////////////////////////////////////////////////
    userInputButton = new JButton("Input", new ImageIcon(getInputImage(inputOverride, inputState)));
    userInputButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        menu.doTick(om.OS.toString("FORCEINPUT"));
      }
    });
    userInputButton.setFocusable(false);
    userInputButton.setToolTipText(userInputButton.getText());
    userInputButton.setText("");

    homeButton = new HomeButton(ICON_HOME);

    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setFloatable(false);
    add(addTabButton = new AddButton(listener, om));
    add(Box.createHorizontalGlue());
    add(homeButton);
    add(screenshotButton);
    add(runScriptButton);
    add(stopScriptButton);
    add(userInputButton);
    updateSelection(false);
  }

  public void setAddTabVisible(final boolean visible) {
    addTabButton.setVisible(visible);
  }

  public void setInputButtonVisible(final boolean visible) {
    userInputButton.setVisible(visible);
  }

  public final int addTab() {
    if (tabMap.size() >= BotGUI.MAX_BOTS)
      throw new ArrayIndexOutOfBoundsException("Index value out of range >= for removeTab() in '" + BotToolBar.class.getName() + "'");

    int idx = 1;  // 0 = Add Button
    while (tabMap.get(idx-1) != null)
      idx++;

    final BotButton button = new BotButton(om.OS.toString("TABDEFAULTTEXT") + " #" + idx, ICON_BOT);
    tabMap.put(idx-1, button);
    add(button, idx);
    validate();
    return setSelection(idx);
  }

  public final int removeTab(final int idx) {
    if (tabMap.isEmpty() || idx < 0 || idx >= tabMap.size())
      throw new ArrayIndexOutOfBoundsException("Index value out of range for removeTab() in '" + BotToolBar.class.getName() + "'");

    try {
      tabMap.remove(idx);
    } catch (UnsupportedOperationException u) {
      throw new UnsupportedOperationException("Unsupported remove operation in tabMap @ '" + BotToolBar.class.getName() + "'");
    } catch (ClassCastException c) {
      throw new ClassCastException("Inappropriate type for key in tabMap @ '" + BotToolBar.class.getName() + "'");
    } catch (NullPointerException n) {
      throw new NullPointerException("Null value detected in tabMap @ '" + BotToolBar.class.getName() + "'");
    }
    try {
      remove(idx+1); // 0 -> getComponentRange()-1
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new ArrayIndexOutOfBoundsException("Index value out of range for BotToolBar container.");
    }
    revalidate();
    repaint();

    if (!tabMap.isEmpty())
      return setSelection(Math.max(1, idx - 1));

    setHome(true);
    return 0;
  }

  public void setTabLabel(final int idx, final String label) {
    tabMap.get(idx).setName(label);
  }

  public final int getCurrentTab() {
    return tab_index;
  }

  public final ScriptStatus getScriptButton() {
    final String label = runScriptButton.getToolTipText();
    for (final ScriptStatus s : ScriptStatus.values())
      if (StringUtil.CapitalizeString(s.name()).equals(label))
        return s;
    throw new IllegalStateException("Illegal script button state!");
  }

  public void setHome(final boolean home) {
    for (final JButton button : new JButton[] { screenshotButton, stopScriptButton, userInputButton, runScriptButton }) {
      button.setEnabled(!home);
      button.setVisible(!home);
    }
  }

  public void setInputState(final int state) {
    inputState = state;
  }

  public void setOverrideInput(final boolean selected) {
    inputOverride = selected;
  }

  public void updateInputButton() {
    userInputButton.setIcon(new ImageIcon(getInputImage(inputOverride, inputState)));
  }

  public void setScriptButton(final ScriptStatus state) {
    boolean running = true;
    switch (state) {
      case RUN:
        runScriptButton.setIcon(ICON_PLAY);
        running = false;
        break;
      case PAUSE:
        runScriptButton.setIcon(ICON_PAUSE);
        break;
      case RESUME:
        runScriptButton.setIcon(ICON_START);
        break;
    }
    stopScriptButton.setVisible(running);
    runScriptButton.setToolTipText(StringUtil.CapitalizeString(state.name()));
    runScriptButton.repaint();
    revalidate();
  }

  private final int setSelection(final int idx) {
    updateSelection(false);
    this.tab_index = idx;
    updateSelection(true);
    listener.actionPerformed(new ActionEvent(this, 0, "Tab"));
    return idx;
  }

  private void updateSelection(final boolean enabled) {
    if (!tabMap.isEmpty() && tabMap.get(this.tab_index) != null) {
      getComponent(this.tab_index).setEnabled(enabled);
      getComponent(this.tab_index).repaint();
    }
  }

  private static Image getInputImage(final boolean override, final int state) {
    if (override || state == (Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE))
      return Configuration.getImage(Configuration.Paths.Resources.ICON_TICK);
    else if (state == Environment.INPUT_KEYBOARD)
      return Configuration.getImage(Configuration.Paths.Resources.ICON_KEYBOARD);
    else if (state == Environment.INPUT_MOUSE)
      return Configuration.getImage(Configuration.Paths.Resources.ICON_MOUSE);
    else
      return Configuration.getImage(Configuration.Paths.Resources.ICON_DELETE);
  }

  private static Image getTransparentImage(final URL url, final float transparency) {
    BufferedImage loaded = null;
    try {
      loaded = ImageIO.read(url);
    } catch (final IOException e) {
      e.printStackTrace();
    }
    final BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), Transparency.TRANSLUCENT);
    final Graphics2D g = aimg.createGraphics();
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
    g.drawImage(loaded, null, 0, 0);
    g.dispose();
    return aimg;
  }

  private class HomeButton extends JPanel {

    private static final long serialVersionUID = 938456324328L;

    private final Image       image;
    private boolean           hovered;

    public HomeButton(final ImageIcon icon) {
      super(new BorderLayout());

      setToolTipText(om.OS.toString("HOME"));
      image = icon.getImage();
      setBorder(new EmptyBorder(3, 6, 2, 3));
      setPreferredSize(new Dimension(24, 22));
      setMaximumSize(new Dimension(24, 22));
      setFocusable(false);
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(final MouseEvent e) {
          setSelection(0);
          setVisible(false);
          screenshotButton.setVisible(false);
          runScriptButton.setVisible(false);
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
          hovered = true;
          repaint();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
          hovered = false;
          repaint();
        }
      });
    }

    @Override
    public void paintComponent(final Graphics g) {
      super.paintComponent(g);
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (getComponentIndex(this) == tab_index) {
        g.setColor(new Color(255, 255, 255, 200));
        g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
        g.setColor(new Color(180, 180, 180, 200));
        g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
      } else if (hovered) {
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
        g.setColor(new Color(180, 180, 180, 150));
        g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
      }
      g.drawImage(image, 3, 3, null);
    }

  }

  private class BotButton extends JPanel {

    private static final long serialVersionUID = 329845763420L;

    private final JLabel      nameLabel;
    private boolean           hovered;
    private boolean           close;

    public BotButton(final String text, final Icon icon) {
      super(new BorderLayout());
      setBorder(new EmptyBorder(3, 6, 2, 3));
      nameLabel = new JLabel(text);
      nameLabel.setIcon(icon);
      nameLabel.setPreferredSize(new Dimension(85, 22));
      nameLabel.setMaximumSize(new Dimension(85, 22));
      add(nameLabel, BorderLayout.WEST);

      setPreferredSize(new Dimension(110, 22));
      setMaximumSize(new Dimension(110, 22));
      setFocusable(false);
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(final MouseEvent e) {
          if (hovered && close)
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, om.OS.toString("CLOSEBOT") + "." + nameLabel.getText().substring(om.OS.toString("TABDEFAULTTEXT").length() + " #".length())));
          else
            setSelection(getComponentIndex(BotButton.this));
          homeButton.setVisible(true);
          screenshotButton.setVisible(true);
          runScriptButton.setVisible(true);
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
          hovered = true;
          repaint();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
          hovered = false;
          repaint();
        }
      });
      addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(final MouseEvent e) {
          close = e.getX() > 95;
          repaint();
        }
      });
    }
    
    @Override
    public void paintComponent(final Graphics g) {
      super.paintComponent(g);
      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      final int RGB = getComponentIndex(this) == tab_index ? 255 : hovered ? 230 : 215;
      g.setColor(new Color(RGB, RGB, RGB, 200));
      g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
      g.setColor(new Color(180, 180, 180, 200));
      g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
      g.drawImage(hovered && close ? IMAGE_CLOSE_OVER : IMAGE_CLOSE, 90, 3, null);
    }
  }

  private static class AddButton extends JComponent {

    private static final long serialVersionUID = 1L;

    private static Image      ICON,
                              ICON_OVER,
                              ICON_DOWN;
    private boolean           hovered          = false,
                                               pressed = false;

    static {
      ICON_DOWN = Configuration.getImage(Configuration.Paths.Resources.ICON_ADD);
    }

    public AddButton(final ActionListener listener, final OutputManager om) {
      try {
        final URL src = Configuration.getResourceURL(Configuration.Paths.Resources.ICON_ADD);
        ICON = getTransparentImage(src, 0.3f);
        ICON_OVER = getTransparentImage(src, 0.7f);
      } catch (final MalformedURLException e) {
      }

      setToolTipText(om.OS.toString("NEWBOT"));
      setPreferredSize(new Dimension(20, 20));
      setMaximumSize(new Dimension(20, 20));
      setFocusable(false);
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(final MouseEvent e) {
          hovered = true;
          repaint();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
          hovered = false;
          repaint();
        }

        @Override
        public void mousePressed(final MouseEvent e) {
          pressed = true;
          repaint();
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
          pressed = false;
          repaint();
          listener.actionPerformed(new ActionEvent(this, e.getID(), "File." + om.OS.toString("NEWBOT")));
        }
      });
    }

    @Override
    public void paintComponent(final Graphics g) {
      super.paintComponent(g);
      if (pressed)
        g.drawImage(ICON_DOWN, 2, 2, null);
      else if (hovered)
        g.drawImage(ICON_OVER, 2, 2, null);
      else
        g.drawImage(ICON, 2, 2, null);
    }

  }

}