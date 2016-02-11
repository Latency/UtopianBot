package org.rsbot.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.rsbot.Boot;
import org.rsbot.Configuration;
import org.rsbot.gui.BotGUI;
import org.rsbot.util.io.HttpClient;
import org.rsbot.util.io.IOHelper;

public abstract class VersionChecker {
  private static int    latest = -1;
  private static boolean error  = false;
  private static final Logger log = Logger.getLogger(VersionChecker.class.getName());
  
  public static boolean hasError() {
    getCachedVersion();
    return error;
  }

  public static int getCachedVersion() {
    if (latest != -1) {
      return latest;
    }
    try {
      final File cache = Configuration.Paths.getCachableResources().get(Configuration.Paths.URLs.VERSION);
      latest = Integer.parseInt(IOHelper.readString(cache).trim());
    } catch (final Exception ignored) {
      error = true;
    }
    return latest;
  }
  
  public static final int getURLVersion(final String path) {
    // /////////////////////////////////////////////////////
    // Check remote version.txt from ubot.info
    // /////////////////////////////////////////////////////
    URL url = null;
    try {
      url = new URL(path);
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
    }
    String version = null;
    try {
      version = HttpClient.downloadAsString(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Integer.parseInt(version.trim());
  }
  
  public static int getVersion() {
    return Integer.parseInt(Configuration.VERSION);
  }
  
  public static String formatVersion(final int version) {
    String ver = String.valueOf(version);
    return (ver.length() >= 3 ? ver.substring(0, 1) + "." + ver.substring(1) : ver);
  }

  public static boolean isBetaBuild() {
    String location = Boot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    Pattern pattern = Pattern.compile("RSBot-([0-9]+)-beta([0-9]+).jar");
    return pattern.matcher(location).find();
  }
  
  public static void Update(final BotGUI instance) throws IOException {
    log.info("Downloading update...");
    final File jarNew = new File(Configuration.NAME + "-" + getCachedVersion() + ".jar");
    HttpClient.download(new URL(Configuration.Paths.URLs.DOWNLOAD), jarNew);
    final String jarOld = Configuration.Paths.getRunningJarPath();
    Runtime.getRuntime().exec("java -jar \"" + jarNew + "\" --delete \"" + jarOld + "\"");
    instance.cleanExit(true);
  }
}