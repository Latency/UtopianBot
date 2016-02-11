package org.rsbot.gui;

import static org.rsbot.script.methods.Environment.INPUT_KEYBOARD;
import static org.rsbot.script.methods.Environment.INPUT_MOUSE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import org.rsbot.bot.Bot;
import org.rsbot.event.EventManager;
import org.rsbot.script.methods.Mouse;

/**
 */
public class BotPanel extends JPanel {
  private static final long serialVersionUID = 2269767882075468055L;

  private class HomeUpdater implements Runnable {
    private boolean running;

    @Override
    public void run() {
      synchronized (this) {
        if (running)
          throw new IllegalStateException("Already running!");
        running = true;
      }
      while (true) {
        synchronized (this) {
          if (!running)
            break;
        }
        repaint();
        try {
          Thread.sleep(70);
        } catch (final Exception ex) {
          break;
        }
      }
      synchronized (this) {
        running = false;
      }
    }

    public void stop() {
      synchronized (this) {
        running = false;
      }
    }
  }

  private Bot               bot;
  private final BotHome     home;
  private final HomeUpdater updater;
  private int               offX;
  private boolean           present;

  public BotPanel(final BotHome home) {
    this.home = home;
    updater = new HomeUpdater();
    setSize(new Dimension(BotGUI.PANEL_WIDTH, BotGUI.PANEL_HEIGHT));
    setMinimumSize(new Dimension(BotGUI.PANEL_WIDTH, BotGUI.PANEL_HEIGHT));
    setPreferredSize(new Dimension(BotGUI.PANEL_WIDTH, BotGUI.PANEL_HEIGHT));
    setBackground(Color.black);
    home.setSize(getWidth(), getHeight());
    
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(final ComponentEvent evt) {
        BotPanel.this.home.setSize(getWidth(), getHeight());
        if (bot != null) {
          bot.setSize(bot.getImage().getWidth(), bot.getImage().getHeight());
          offset();
        }
        requestFocus();
      }
    });
    
    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        redispatch(e);
        if (!hasFocus()) {
          requestFocus();
        }
      }

      @Override
      public void mouseEntered(final MouseEvent e) {
      }

      @Override
      public void mouseExited(final MouseEvent e) {
        redispatch(e);
      }

      @Override
      public void mousePressed(final MouseEvent e) {
        redispatch(e);
      }

      @Override
      public void mouseReleased(final MouseEvent e) {
        redispatch(e);
      }
    });
    
    addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(final MouseEvent e) {
        redispatch(e);
      }

      @Override
      public void mouseMoved(final MouseEvent e) {
        redispatch(e);
      }
    });
    
    addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(final MouseWheelEvent e) {
        redispatch(e);
      }
    });
    
    addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(final KeyEvent e) {
        redispatch(e);
      }

      @Override
      public void keyReleased(final KeyEvent e) {
        redispatch(e);
      }

      @Override
      public void keyTyped(final KeyEvent e) {
        redispatch(e);
      }
    });
  }

  public void offset() {
    if (bot.getCanvas() != null) // center canvas horizontally if not filling container
      offX = (getWidth() - bot.getCanvas().getWidth()) / 2;
  }

  public void setBot(final Bot bot) {
    if (this.bot != null)
      this.bot.setPanel(null);
    else
      updater.stop();

    this.bot = bot;
    if (bot != null) {
      bot.setPanel(this);
      if (bot.getCanvas() != null)
        offset();
    } else
      new Thread(updater).start();
  }

  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    if (bot == null)
      home.paint(g);
    else
      g.drawImage(bot.getImage(), offX, 0, null);
  }

  private void redispatch(final MouseEvent e) {
    if (bot != null && bot.getLoader() != null && bot.getLoader().getComponentCount() > 0) {
      if (bot.getScriptHandler() != null && bot.getScriptHandler().getMethodContext() != null) {
        final Mouse mouse = bot.getScriptHandler().getMethodContext().mouse;
        if (mouse == null)
          return; // client cannot currently accept events
        final boolean present = mouse.isPresent();
        final Component c = bot.getLoader().getComponent(0);
        // account for horizontal offset
        e.translatePoint(-offX, 0);
        // fire human mouse event for scripts
        dispatchHuman(c, e);
        if (!bot.overrideInput && (bot.inputFlags & INPUT_MOUSE) == 0)
          return;
        if (e.getX() > 0 && e.getX() < c.getWidth() && e.getY() < c.getHeight() && e.getID() != MouseEvent.MOUSE_EXITED) {
          if (present) {
            if (e instanceof MouseWheelEvent) {
              final MouseWheelEvent mwe = (MouseWheelEvent) e;
              c.dispatchEvent(new MouseWheelEvent(c, e.getID(), System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, e.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation()));
            } else
              c.dispatchEvent(new MouseEvent(c, e.getID(), System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, e.isPopupTrigger(), e.getButton()));
          } else
            c.dispatchEvent(new MouseEvent(c, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, false));
        } else if (present)
          c.dispatchEvent(new MouseEvent(c, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, false));
      }
    }
  }

  private void redispatch(final KeyEvent e) {
    if (bot != null) {
      final EventManager m = bot.getEventManager();
      if (m != null)
        m.dispatchEvent(e);
      if ((bot.overrideInput || (bot.inputFlags & INPUT_KEYBOARD) != 0) && bot.getLoader().getComponentCount() > 0) {
        final Component c = bot.getLoader().getComponent(0);
        c.dispatchEvent(e);
      }
    }
  }

  private void dispatchHuman(final Component c, final MouseEvent e) {
    if (e.getX() > 0 && e.getX() < c.getWidth() && e.getY() < c.getHeight() && e.getID() != MouseEvent.MOUSE_EXITED) {
      if (present)
        bot.getEventManager().dispatchEvent(e);
      else {
        present = true;
        bot.getEventManager().dispatchEvent(new MouseEvent(c, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, false));
      }
    } else if (present) {
      present = false;
      bot.getEventManager().dispatchEvent(new MouseEvent(c, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, e.getX(), e.getY(), 0, false));
    }
  }
}