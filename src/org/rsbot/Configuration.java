package org.rsbot;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

import javax.swing.filechooser.FileSystemView;

import org.rsbot.log.LogFormatter;
import org.rsbot.log.SystemConsoleHandler;
import org.rsbot.log.TextAreaLogHandler;

public class Configuration {
  // Constructor
  public Configuration() {
    RUNNING_FROM_JAR = isRunningFromJar();  
  }
  
  public enum OperatingSystem {
    MAC, WINDOWS, LINUX, UNKNOWN
  }

  public static final String           NAME             = "Utopian Bot",
                                       VERSION          = "100";
  public static final OperatingSystem  CURRENT_OS;
  public static boolean                RUNNING_FROM_JAR,
                                       PRINT_OP_CODES   = true;
  
  public static class Paths {
    public static class Resources {
      public static final String ROOT             = "resources";
      public static final String SCRIPTS          = Paths.SCRIPTS_NAME_SRC + "/";
      public static final String ROOT_IMG         = ROOT     + "/images";
      public static final String SPLASH_IMG       = ROOT_IMG + "/splash.png";
      public static final String ROOT_IMG_FLAG    = ROOT_IMG + "/flags";
      public static final String ICON             = ROOT_IMG + "/icon.png";
      public static final String ICON_APPADD      = ROOT_IMG + "/application_add.png";
      public static final String ICON_APPDELETE   = ROOT_IMG + "/application_delete.png";
      public static final String ICON_ARROWIN     = ROOT_IMG + "/arrow_in.png";
      public static final String ICON_REFRESH     = ROOT_IMG + "/arrow_refresh.png";
      public static final String ICON_DELETE      = ROOT_IMG + "/delete.png";
      public static final String ICON_GITHUB      = ROOT_IMG + "/github.png";
      public static final String ICON_PLAY        = ROOT_IMG + "/control_play_blue.png";
      public static final String ICON_PAUSE       = ROOT_IMG + "/control_pause.png";
      public static final String DATABASE_ERROR   = ROOT_IMG + "/database_error.png";
      public static final String ICON_ADD         = ROOT_IMG + "/add.png";
      public static final String ICON_HOME        = ROOT_IMG + "/home.png";
      public static final String ICON_BOT         = ROOT_IMG + "/bot.png";
      public static final String ICON_CLOSE       = ROOT_IMG + "/close.png";
      public static final String ICON_TICK        = ROOT_IMG + "/tick.png";
      public static final String ICON_MOUSE       = ROOT_IMG + "/mouse.png";
      public static final String ICON_PHOTO       = ROOT_IMG + "/photo.png";
      public static final String ICON_REPORTKEY   = ROOT_IMG + "/report_key.png";
      public static final String ICON_REPORT_DISK = ROOT_IMG + "/report_disk.png";
      public static final String ICON_INFO        = ROOT_IMG + "/information.png";
      public static final String ICON_KEY         = ROOT_IMG + "/key.png";
      public static final String ICON_KEYBOARD    = ROOT_IMG + "/keyboard.png";
      public static final String ICON_CONNECT     = ROOT_IMG + "/connect.png";
      public static final String ICON_DISCONNECT  = ROOT_IMG + "/disconnect.png";
      public static final String ICON_START       = ROOT_IMG + "/control_play.png";
      public static final String ICON_SCRIPT      = ROOT_IMG + "/script.png";
      public static final String ICON_SCRIPT_ADD  = ROOT_IMG + "/script_add.png";
      public static final String ICON_SCRIPT_LIVE = ROOT_IMG + "/script_lightning.png";
      public static final String ICON_SCRIPT_GEAR = ROOT_IMG + "/script_gear.png";
      public static final String ICON_SCRIPT_CODE = ROOT_IMG + "/script_code.png";
      public static final String ICON_WEBLINK     = ROOT_IMG + "/world_link.png";
      public static final String ICON_WRENCH      = ROOT_IMG + "/wrench.png";
    }

    public static class URLs {
      public static final String HOST             = "powerbot.org";
      public static final String HOST1            = "uscripts.info";
      public static final String BASE             = "http://links." + HOST + "/";
      public static final String BASE1            = "http://" + HOST1 + "/";
      public static final String VERSION          = BASE1 + "version/version.txt";
      public static final String CLIENTPATCH      = BASE + "modscript";
      public static final String SERVICELOGIN     = BASE + "servicelogin";
      public static final String DOWNLOAD         = BASE + "download/Utopian_Bot.jar";
    }

    public static final String ROOT                = new File(".").getAbsolutePath();

    public static final String COMPILE_SCRIPTS_BAT = "Compile-Scripts.bat";
    public static final String COMPILE_SCRIPTS_SH  = "compile-scripts.sh";
    public static final String COMPILE_FIND_JDK    = "FindJDK.bat";

    public static final String SCRIPTS_NAME_SRC    = "scripts";
    public static final String SCRIPTS_NAME_OUT    = "Scripts";

    public static String getAccountsFile() {
      final String path;
      if (Configuration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS)
        path = System.getenv("APPDATA") + File.separator + Configuration.NAME + "_Accounts.ini";
      else
        path = Paths.getUnixHome() + File.separator + "." + Configuration.NAME.toLowerCase() + "acct";
      return path;
    }

    public static String getHomeDirectory() {
      final String env = System.getenv(Configuration.NAME.toUpperCase() + "_HOME");
      if (env == null || env.isEmpty()) {
        return (Configuration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS ? FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() : Paths
            .getUnixHome()) + File.separator + Configuration.NAME;
      }
      return env;
    }

    public static String getLogsDirectory() {
      return Paths.getHomeDirectory() + File.separator + "Logs";
    }

    public static String getPathCache() {
      return Paths.getSettingsDirectory() + File.separator + "path.txt";
    }

    public static String getUIDsFile() {
      return Paths.getSettingsDirectory() + File.separator + "uid.txt";
    }

    public static String getScreenshotsDirectory() {
      return Paths.getHomeDirectory() + File.separator + "Screenshots";
    }

    public static String getScriptsDirectory() {
      return Paths.getHomeDirectory() + File.separator + Paths.SCRIPTS_NAME_OUT;
    }

    public static String getScriptsSourcesDirectory() {
      return Paths.getScriptsDirectory() + File.separator + "Sources";
    }

    public static String getScriptsPrecompiledDirectory() {
      return Paths.getScriptsDirectory() + File.separator + "Precompiled";
    }

    public static String getScriptsNetworkDirectory() {
      return Paths.getScriptsDirectory() + File.separator + "Network";
    }

    public static String getCacheDirectory() {
      return Paths.getHomeDirectory() + File.separator + "Cache";
    }

    public static String getScriptCacheDirectory() {
      return getCacheDirectory() + File.separator + "Scripts";
    }

    public static String getVersionCache() {
      return Paths.getCacheDirectory() + File.separator + "info.dat";
    }

    public static String getWebDatabase() {
      return Paths.getSettingsDirectory() + File.separator + "Web.store";
    }

    public static String getServiceKey() {
      return Paths.getSettingsDirectory() + File.separator + "service.key";
    }

    public static String getSettingsDirectory() {
      return Paths.getHomeDirectory() + File.separator + "Settings";
    }

    public static String getGarbageDirectory() {
      final File dir = new File(Configuration.Paths.getScriptCacheDirectory(), ".java");
      if (!dir.exists()) {
        dir.mkdirs();
      }
      String path = dir.getAbsolutePath();
      try {
        path = URLDecoder.decode(path, "UTF-8");
      } catch (final UnsupportedEncodingException ignored) {
      }
      return path;
    }

    public static String getRunningJarPath() {
      if (!RUNNING_FROM_JAR)
        return null;
      String path = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
      try {
        path = URLDecoder.decode(path, "UTF-8");
      } catch (UnsupportedEncodingException ignored) {
      }
      return path;
    }

    public static String getUnixHome() {
      final String home = System.getProperty("user.home");
      return home == null ? "~" : home;
    }

    private static Map<String, File> cachableResources;

    public static Map<String, File> getCachableResources() {
      if (cachableResources == null) {
        cachableResources = new HashMap<String, File>(1);
        cachableResources.put(URLs.CLIENTPATCH, new File(getCacheDirectory(), "ms.dat"));  // http://links.powerbot.org/modscript 

      }
      return cachableResources;
    }
  }

  public final boolean isRunningFromJar() {
    String className = this.getClass().getName().replace('.', '/');
    String classJar =  this.getClass().getResource("/" + className + ".class").toString();
    return (RUNNING_FROM_JAR = classJar.startsWith("jar:"));
  }
  
  static {
    final String os = System.getProperty("os.name");
    if (os.contains("Mac")) {
      CURRENT_OS = OperatingSystem.MAC;
    } else if (os.contains("Windows")) {
      CURRENT_OS = OperatingSystem.WINDOWS;
    } else if (os.contains("Linux")) {
      CURRENT_OS = OperatingSystem.LINUX;
    } else {
      CURRENT_OS = OperatingSystem.UNKNOWN;
    }
  }

  public static void createDirectories() {
    final ArrayList<String> dirs = new ArrayList<String>();
    dirs.add(Paths.getHomeDirectory());
    dirs.add(Paths.getLogsDirectory());
    dirs.add(Paths.getCacheDirectory());
    dirs.add(Paths.getSettingsDirectory());
    dirs.add(Paths.getScriptsDirectory());
    dirs.add(Paths.getScriptsSourcesDirectory());
    dirs.add(Paths.getScriptsPrecompiledDirectory());
    for (final String name : dirs) {
      final File dir = new File(name);
      if (!dir.exists()) {
        dir.mkdirs();
      }
    }
  }

  public void registerLogging() {
    final Properties logging = new Properties();
    final String logFormatter = LogFormatter.class.getCanonicalName();
    final String fileHandler = FileHandler.class.getCanonicalName();
    logging.setProperty("handlers", TextAreaLogHandler.class.getCanonicalName() + "," + fileHandler);
    logging.setProperty(".level", "INFO");
    logging.setProperty(SystemConsoleHandler.class.getCanonicalName() + ".formatter", logFormatter);
    logging.setProperty(fileHandler + ".formatter", logFormatter);
    logging.setProperty(TextAreaLogHandler.class.getCanonicalName() + ".formatter", logFormatter);
    logging.setProperty(fileHandler + ".pattern", Paths.getLogsDirectory() + File.separator + "%u.%g.log");
    logging.setProperty(fileHandler + ".count", "10");
    final ByteArrayOutputStream logout = new ByteArrayOutputStream();
    try {
      logging.store(logout, "");
      LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(logout.toByteArray()));
    } catch (final Exception ignored) {
    }
    if (isRunningFromJar()) {
      String path = null;
      try {
        path = URLDecoder.decode(Paths.getRunningJarPath(), "UTF-8");
      } catch (final UnsupportedEncodingException ignored) {
      }
      final String prefix = "jar:file:/";
      if (path.startsWith(prefix)) {
        path = path.substring(prefix.length());
        path = path.substring(0, path.indexOf('!'));
        if (File.separatorChar != '/')
          path = path.replace('/', File.separatorChar);
        try {
          final File pathfile = new File(Paths.getPathCache());
          if (pathfile.exists())
            pathfile.delete();
          pathfile.createNewFile();
          final Writer out = new BufferedWriter(new FileWriter(Paths.getPathCache()));
          out.write(path);
          out.close();
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static URL getResourceURL(final String path) throws MalformedURLException {
    return RUNNING_FROM_JAR ? Configuration.class.getResource("/" + path) : new File(path).toURI().toURL();
  }

  public static Image getImage(final String resource) {
    if (resource != null) {
      try {
        return Toolkit.getDefaultToolkit().getImage(getResourceURL(resource));
      } catch (final Exception e) {
      }
    }
    return null;
  }

  public static Image getFlag(final String flag) {
    try {
      return Toolkit.getDefaultToolkit().getImage(getResourceURL(Paths.Resources.ROOT_IMG_FLAG + "/" + flag + ".png"));
    } catch (final Exception e) {
    }
    return null;
  }

  public static OperatingSystem getCurrentOperatingSystem() {
    return Configuration.CURRENT_OS;
  }

}