package org.rsbot.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import org.rsbot.bot.Bot;

public class BotHome extends Canvas {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private List<Bot>                bots;
  static final Font                FONT = new Font("Helvetica", 1, 13);
  
  public void setBots(final List<Bot> bots) {
    this.bots = bots; 
  }
  
  @Override
  public void paint(final Graphics g) {
    if (bots == null)
      return;
    
    final int w = super.getWidth();
    final int h = super.getHeight();
    
    g.setColor(Color.black);
    g.fillRect(0, 0, w, h);
    switch (bots.size()) {
      case 1:
        draw(g, bots.get(0).getImage(), new Rectangle(0,           0,        w,      h    ));
        break;
      case 2:
        draw(g, bots.get(1).getImage(), new Rectangle(0,           0,        w,      h / 2));
        draw(g, bots.get(2).getImage(), new Rectangle(0,           h / 2,    w,      h / 2));
        break;
      case 3:
        draw(g, bots.get(1).getImage(), new Rectangle(0,           0,        w / 2,   h / 2));
        draw(g, bots.get(2).getImage(), new Rectangle(w / 2,       0,        w / 2,   h / 2));
        draw(g, bots.get(3).getImage(), new Rectangle(0,           h / 2,    w,       h / 2));
        break;
      case 4:
        draw(g, bots.get(1).getImage(), new Rectangle(0,           0,        w / 2,   h / 2));
        draw(g, bots.get(2).getImage(), new Rectangle(w / 2,       0,        w / 2,   h / 2));
        draw(g, bots.get(3).getImage(), new Rectangle(0,           h / 2,    w / 2,   h / 2));
        draw(g, bots.get(4).getImage(), new Rectangle(w / 2,       h / 2,    w / 2,   h / 2));
        break;
      case 5:
        draw(g, bots.get(1).getImage(), new Rectangle(0,           0,        w / 3,   h / 2));
        draw(g, bots.get(2).getImage(), new Rectangle(w / 3,       0,        w / 3,   h / 2));
        draw(g, bots.get(3).getImage(), new Rectangle(w * 2 / 3,   0,        w / 3,   h / 2));
        draw(g, bots.get(4).getImage(), new Rectangle(0,           h / 2,    w / 2,   h / 2));
        draw(g, bots.get(5).getImage(), new Rectangle(w / 2,       h / 2,    w / 2,   h / 2));
        break;
      case 6:
        draw(g, bots.get(1).getImage(), new Rectangle(0,           0,        w / 3,   h / 2));
        draw(g, bots.get(2).getImage(), new Rectangle(w / 3,       0,        w / 3,   h / 2));
        draw(g, bots.get(3).getImage(), new Rectangle(w * 2 / 3,   0,        w / 3,   h / 2));
        draw(g, bots.get(4).getImage(), new Rectangle(0,           h / 2,    w / 3,   h / 2));
        draw(g, bots.get(5).getImage(), new Rectangle(w / 3,       h / 2,    w / 3,   h / 2));
        draw(g, bots.get(6).getImage(), new Rectangle(w * 2 / 3,   h / 2,    w / 3,   h / 2));
        break;
      default:
        return;
    }
    final FontMetrics metrics = g.getFontMetrics(FONT);
    g.setColor(new Color(0, 0, 0, 170));
    g.fillRect(0, h - 30, w, 30);
    g.setColor(Color.white);
    g.drawString("Spectating " + (bots.size() == 1 ? "1 bot." : bots.size() + " bots."), 5, h + metrics.getDescent() - 14);
  }

  private static void draw(final Graphics g,  final BufferedImage img, final Rectangle rect) {
    if (img != null && img.getWidth() > 0) {
      final int w_img = img.getWidth(), h_img = img.getHeight();
      final float img_ratio = (float) w_img / (float) h_img;
      final float bound_ratio = (float) rect.width / (float) rect.height;
      int w, h;
      if (img_ratio < bound_ratio) {
        h = rect.height;
        w = (int) ((float) w_img / (float) h_img * h);
      } else {
        w = rect.width;
        h = (int) ((float) h_img / (float) w_img * w);
      }
      g.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), rect.x + rect.width / 2 - w / 2, rect.y + rect.height / 2 - h / 2, null);
      g.setColor(Color.gray);
      g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
    }
  }
}