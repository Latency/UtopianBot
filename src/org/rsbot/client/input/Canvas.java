package org.rsbot.client.input;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

import javax.swing.SwingUtilities;

import org.rsbot.Application;
import org.rsbot.bot.Bot;

public class Canvas extends java.awt.Canvas {
  public static final int   GRAPHICS_DELAY      = 6;
  public static final int   SLOW_GRAPHICS_DELAY = 50;

  private static final long serialVersionUID    = -2276037172265300477L;

  private Bot               bot;
  private boolean           toshi;

  private boolean           visible;
  private boolean           focused;

  @Override
  public final Graphics getGraphics() {
    if (bot == null) {
      if (toshi)
        return super.getGraphics();
      bot = Application.gui.getBot(this);
      toshi = true;
    }
    try {
      Thread.sleep(bot.disableRendering || bot.disableGraphics ? SLOW_GRAPHICS_DELAY : GRAPHICS_DELAY);
    } catch (final InterruptedException ignored) {
    }
    return bot.getGraphics();
  }

  @Override
  public final boolean hasFocus() {
    return focused;
  }

  @Override
  public final boolean isValid() {
    return visible;
  }

  @Override
  public final boolean isVisible() {
    return visible;
  }

  @Override
  public final boolean isDisplayable() {
    return true;
  }

  @Override
  public final void setVisible(final boolean visible) {
    super.setVisible(visible);
    this.visible = visible;
  }

  public final void setFocused(final boolean focused) {
    if (focused && !this.focused) {
      // null opposite; permanent gain, as expected when entire Applet
      // regains focus
      super.processEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED, false, null));
    } else if (this.focused) {
      // null opposite; temporary loss, as expected when entire Applet
      // loses focus
      super.processEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST, true, null));
    }
    this.focused = focused;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Image createImage(final int width, final int height) {
    // Prevents NullPointerException when opening world map.
    // This is caused by the character loader, which creates
    // character sprites using this method (which will return
    // null as long as this canvas is not really displayed).
    final int[] pixels = new int[height * width];
    final DataBufferInt databufferint = new DataBufferInt(pixels, pixels.length);
    final DirectColorModel directcolormodel = new DirectColorModel(32, 0xff0000, 0xff00, 255);
    final WritableRaster writableraster = Raster.createWritableRaster(directcolormodel.createCompatibleSampleModel(width, height), databufferint, null);
    return new BufferedImage(directcolormodel, writableraster, false, new Hashtable());
  }

  public BufferedImage canvasToImage(final Dimension d) {
    BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();
    super.paint(g2);
    g2.dispose();
    return image;
  }

  @Override
  protected final void processEvent(final AWTEvent e) {
    if (!(e instanceof FocusEvent)) {
      super.processEvent(e);
    }
  }

  @SuppressWarnings("unused")
  private void init() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        setFocused(true);
      }
    });
  }
}