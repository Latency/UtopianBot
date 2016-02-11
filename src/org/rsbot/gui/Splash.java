package org.rsbot.gui;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.rsbot.Configuration;

/**
 * @author Paris
 * @author Timer
 * @author Nader
 */
public class Splash extends JDialog {
  private static final long serialVersionUID = 1L;

  private final int         display          = 5000;

  public Splash(final JFrame owner) {
    super(owner);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setUndecorated(true);
    setTitle(Configuration.NAME);

    final Image image = Configuration.getImage(Configuration.Paths.Resources.SPLASH_IMG);

    try {
      final Icon icon = new ImageIcon(image);
      setSize(icon.getIconWidth(), icon.getIconHeight());
      final JLabel label = new JLabel(icon);
      label.setOpaque(true);
      label.setBackground(Color.BLACK);
      add(label);
    } catch (final Exception ignored) {
      dispose();
      return;
    }
  }

  public void display() {
    setLocationRelativeTo(getOwner());
    setVisible(true);
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        dispose();
      }
    }, display);
  }
}
