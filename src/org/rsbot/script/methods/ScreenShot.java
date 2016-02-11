package org.rsbot.script.methods;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.rsbot.Configuration;
import org.rsbot.bot.Bot;

public class ScreenShot {

  private static final Logger           log        = Logger.getLogger(ScreenShot.class.getName());
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

  public static void saveScreenshot(final Bot bot, final boolean hideUsername) {
    final String name = ScreenShot.dateFormat.format(new Date()) + ".png";
    final File dir = new File(Configuration.Paths.getScreenshotsDirectory());
    if (dir.isDirectory() || dir.mkdirs())
      ScreenShot.saveScreenshot(bot, new File(dir, name), "png", hideUsername);
  }

  public static void saveScreenshot(final Bot bot, final boolean hideUsername, String filename) {
    if (!filename.endsWith(".png")) {
      filename = filename.concat(".png");
    }

    final File dir = new File(Configuration.Paths.getScreenshotsDirectory());
    if (dir.isDirectory() || dir.mkdirs())
      ScreenShot.saveScreenshot(bot, new File(dir, filename), "png", hideUsername);
  }

  private static void saveScreenshot(final Bot bot, final File file, final String type, final boolean hideUsername) {
    try {
      final BufferedImage image = takeScreenshot(bot, hideUsername);
      ImageIO.write(image, type, file);
      ScreenShot.log.info("Screenshot saved to: " + file.getPath());
    } catch (final Exception e) {
      ScreenShot.log.log(Level.SEVERE, "Could not take screenshot.", e);
    }
  }

  public static BufferedImage takeScreenshot(final Bot bot, final boolean hideUsername) {
    if (bot == null)
      return null;
    
    final BufferedImage source = bot.getImage();
    final WritableRaster raster = source.copyData(null);

    final BufferedImage bufferedImage = new BufferedImage(source.getColorModel(), raster,
        source.isAlphaPremultiplied(), null);
    final Graphics2D graphics = bufferedImage.createGraphics();

    if (hideUsername) {
      if (bot != null && bot.getScriptHandler() != null && bot.getScriptHandler().getMethodContext() != null && bot.getScriptHandler().getMethodContext().game.isFixed()) {
        graphics.setColor(Color.black);
        graphics.fill(new Rectangle(9, 459, 100, 15));
        graphics.dispose();
      } else {
        graphics.setColor(Color.black);
        graphics.drawRect(8, 555, 100, 15);
        graphics.dispose();
      }
    }
    return source;
  }
}