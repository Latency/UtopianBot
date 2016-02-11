package org.rsbot.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Permission;
import java.util.HashSet;
import java.util.logging.Logger;

import org.rsbot.Application;
import org.rsbot.Configuration;
import org.rsbot.Configuration.OperatingSystem;
import org.rsbot.gui.BotGUI;
import org.rsbot.gui.LoadScreen;
import org.rsbot.script.AccountStore;
import org.rsbot.script.Script;
import org.rsbot.script.internal.ScriptHandler;
import org.rsbot.util.io.JavaCompiler;

import sun.font.FontManager;

/**
 * @author Paris
 */
public class RestrictedSecurityManager extends SecurityManager {
  private static Logger          log      = Logger.getLogger("Security");
  private final static int       PORT_UNKNOWN = -1, PORT_HTTP = 80, PORT_HTTPS = 443, PORT_DNS = 53;
  public final static String     DNSA         = "8.8.8.8", DNSB = "8.8.4.4";                        // Google Public DNS (http://code.google.com/speed/public-dns/)
  private static HashSet<String> resolved     = new HashSet<String>();
  public static final String SCRIPTTHREAD = "Script-", SCRIPTCLASS = "org.rsbot.script.Script";
  
  private String getCallingClass() {
    final String prefix = Application.class.getPackage().getName() + ".";
    for (final Class<?> c : getClassContext()) {
      final String name = c.getName();
      if (name.startsWith(prefix) && !name.equals(RestrictedSecurityManager.class.getName()))
        return name;
    }
    return "";
  }

  public boolean isCallerScript() {
    return getThreadGroup().getName().equals(ScriptHandler.THREAD_GROUP_NAME) || Thread.currentThread().getName().startsWith(SCRIPTTHREAD) || getCallingClass().startsWith(SCRIPTCLASS);
  }

  public static void assertNonScript() {
    final SecurityManager sm = System.getSecurityManager();
    if (sm == null || !(sm instanceof RestrictedSecurityManager) || ((RestrictedSecurityManager) sm).isCallerScript())
        throw new SecurityException();
  }
  
  public static boolean isHostAllowed(final String host) {
    // NOTE: if whitelist item starts with a dot "." then it is checked at the end of the host
    final String[] ALLOWED_HOSTS  = {
        ".runescape.com",
        ".powerbot.org",
        ".imageshack.us",
        ".tinypic.com",
        ".photobucket.com",
        ".imgur.com",
        ".deviantart.com",
        ".ipcounter.de",
        ".wikia.com",
        ".wikia.nocookie.net",

        ".glorb.nl", // SXForce - Swamp Lizzy Paid, Snake Killah
        "scripts.johnkeech.com", // MrSneaky - SneakyFarmerPro
        "jtryba.com", // jtryba - autoCook, monkR8per
        "tehgamer.info", // TehGamer - iMiner
        "www.universalscripts.org", // Fletch To 99 - UFletch
        "www.dunkscripts.freeiz.com", // Dunnkers
        "www.dlolpics.com", // DlolPics
        ".logikmedia.co", // countvidal
        "letthesmokeout.com", // MrByte
        "zaszmedia.com", // zasz - Frost Dragons Pro, Enchanter Pro, Jars Pro
        "massacrescripting.net", // ShizZznit - Aviansie Massacre
        ".ownagebots.com", // Ownageful - OwnageGDK, OwnageBDK, OwnageFDK
        "tablocks.com", // xCoder99 - xRedChin, xLeather, xWerewolf
        ".solarbots.org", // Wei Su
    };

    for (final String check : ALLOWED_HOSTS ) {
      if (check.startsWith(".")) {
        if (host.endsWith(check) || check.equals("." + host)) {
          return true;
        }
      } else if (host.equals(check)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void checkAccept(final String host, final int port) {
    if (port == PORT_DNS) {
      checkConnectDNS(host);
    } else {
      throw new SecurityException();
    }
  }

  private static void checkConnectDNS(final String host) {
    if (!(host.equals(DNSA) || host.equals(DNSB))) {
      log.warning("DNS lookup denied: " + host);
      throw new SecurityException();
    }
  }

  @Override
  public void checkAccess(final Thread t) {
      checkAccess(t.getThreadGroup());
  }

  @Override
  public void checkAccess(final ThreadGroup g) {
      if (g.getName().equals(ScriptHandler.THREAD_GROUP_NAME) && !(getCallingClass().equals(ScriptHandler.class.getName()) || getCallingClass().equals(Script.class.getName()))) {
          throw new SecurityException();
      }
  }
  
  @Override
  public void checkConnect(final String host, final int port) {
    if (host.equalsIgnoreCase("localhost") || host.startsWith("127.") || host.startsWith("192.168.") || host.startsWith("10.") || host.endsWith("::1")) {
      throw new SecurityException();
    }

    switch (port) {
      case PORT_UNKNOWN:
        break;
      case PORT_DNS:
        checkConnectDNS(host);
        break;
      case PORT_HTTP:
      case PORT_HTTPS:
        boolean allowed = false;
        if (isIpAddress(host)) {
          allowed = resolved.contains(host) || (getClassContext()[1].getName().equals(Socket.class.getName()) && !isCallerScript());
        } else {
          allowed = isHostAllowed(host);
        }
        if (!allowed) {
          log.warning("Connection denied: " + host);
          throw new SecurityException();
        }
        try {
          for (final InetAddress a : InetAddress.getAllByName(host)) {
            resolved.add(a.getHostAddress());
          }
        } catch (final UnknownHostException ignored) {
        }
        break;
      default:
        throw new SecurityException("Connection denied: " + host + ":" + port);
    }

    super.checkConnect(host, port);
  }

  private static boolean isIpAddress(final String check) {
    if (check.contains(":")) {
      return true; // IPv6
    }
    final int l = check.length();
    if (l < 7 || l > 15) {
      return false;
    }
    final String[] parts = check.split("\\.", 4);
    if (parts.length != 4) {
      return false;
    }
    for (int i = 0; i < 4; i++) {
      final int n = Integer.parseInt(parts[i]);
      if (n < 0 || n > 255) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void checkConnect(final String host, final int port, final Object context) {
    checkConnect(host, port);
  }

  @Override
  public void checkCreateClassLoader() {
    super.checkCreateClassLoader();
  }

  @Override
  public void checkDelete(final String file) {
    checkFilePath(file, false);
    super.checkDelete(file);
  }

  @Override
  public void checkExec(final String cmd) {
    final String calling = getCallingClass();
    for (final Class<?> c : new Class<?>[] { BotGUI.class, JavaCompiler.class }) {
      if (calling.startsWith(c.getName())) {
        super.checkExec(cmd);
        return;
      }
    }
    throw new SecurityException();
  }

  @Override
  public void checkExit(final int status) {
    final String calling = getCallingClass();
    if (calling.equals(BotGUI.class.getName()) || calling.equals(Application.class.getName()) || calling.startsWith(LoadScreen.class.getName())) {
      super.checkExit(status);
    } else {
      throw new SecurityException();
    }
  }

  @Override
  public void checkLink(final String lib) {
    super.checkLink(lib);
  }

  @Override
  public void checkListen(final int port) {
    if (port != 0) {
      throw new SecurityException();
    }
  }

  @Override
  public void checkMemberAccess(final Class<?> clazz, final int which) {
    super.checkMemberAccess(clazz, which);
  }

  @Override
  public void checkMulticast(final InetAddress maddr) {
    throw new SecurityException();
  }

  @Override
  public void checkMulticast(final InetAddress maddr, final byte ttl) {
    throw new SecurityException();
  }

  @Override
  public void checkPackageAccess(final String pkg) {
    super.checkPackageAccess(pkg);
  }

  @Override
  public void checkPackageDefinition(final String pkg) {
    super.checkPackageDefinition(pkg);
  }

  @Override
  public void checkPermission(final Permission perm) {
    if (perm instanceof RuntimePermission) {
      if (perm.getName().equals("setSecurityManager")) {
        throw new SecurityException();
      }
    }
    // super.checkPermission(perm);
  }

  @Override
  public void checkPermission(final Permission perm, final Object context) {
    checkPermission(perm);
  }

  @Override
  public void checkPrintJobAccess() {
    throw new SecurityException();
  }

  @Override
  public void checkPropertiesAccess() {
    super.checkPropertiesAccess();
  }

  @Override
  public void checkPropertyAccess(final String key) {
    super.checkPropertyAccess(key);
  }

  @Override
  public void checkRead(final FileDescriptor fd) {
    super.checkRead(fd);
  }

  @Override
  public void checkRead(final String file) {
    checkFilePath(file, true);
    super.checkRead(file);
  }

  @Override
  public void checkRead(final String file, final Object context) {
    checkRead(file);
  }

  @Override
  public void checkSecurityAccess(final String target) {
    super.checkSecurityAccess(target);
  }

  @Override
  public void checkSetFactory() {
    super.checkSetFactory();
  }

  @Override
  public void checkSystemClipboardAccess() {
    if (isCallerScript()) {
      throw new SecurityException();
    }
  }

  @Override
  public boolean checkTopLevelWindow(final Object window) {
    return super.checkTopLevelWindow(window);
  }

  @Override
  public void checkWrite(final FileDescriptor fd) {
    super.checkWrite(fd);
  }

  @Override
  public void checkWrite(final String file) {
    checkFilePath(file, false);
    super.checkWrite(file);
  }

  private void checkFilePath(final String pathRaw, final boolean readOnly) {
    final String path = new File(pathRaw).getAbsolutePath();
    if (isCallerScript()) {
      if (!path.startsWith(Configuration.Paths.getScriptCacheDirectory())) {
        boolean fail = true;
        if (!Configuration.RUNNING_FROM_JAR) {
          // allow project resource directory if not running from JAR (i.e. in eclipse)
          String check = new File(Configuration.Paths.ROOT).getAbsolutePath();
          try {
            check = new File(check).getCanonicalPath();
          } catch (final IOException ignored) {
          }
          fail = !path.startsWith(check);
        } else {
          if (readOnly && path.equals(Configuration.Paths.getRunningJarPath())) {
            fail = false;
          }
        }
        for (final String prefix : new String[] { Configuration.Paths.getScreenshotsDirectory(),
            Configuration.Paths.getScriptsDirectory(), Configuration.Paths.getWebDatabase() }) {
          if (path.startsWith(prefix)) {
            fail = false;
            break;
          }
        }
        final String jre = System.getProperty("java.home");
        if (readOnly && jre != null && !jre.isEmpty() && path.startsWith(jre)) {
          fail = false;
        }
        if (fail && readOnly) {
          for (final String font : FontManager.getFontPath(true).split("\\Q" + File.pathSeparator + "\\E")) {
            if (path.startsWith(font)) {
              fail = false;
              break;
            }
          }
        }
        if (Configuration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS) {
          final String sysroot = System.getenv("SystemRoot");
          if (sysroot != null && sysroot.length() > 0 && path.startsWith(sysroot)) {
            fail = !readOnly;
          }
          if (path.endsWith(".ttf") && readOnly) {
            fail = false;
          }
        }
        if (fail) {
          log.warning((readOnly ? "Read" : "Write") + " access denied: " + path);
          throw new SecurityException();
        }
      }
    }
    if (path.equalsIgnoreCase(new File(Configuration.Paths.getAccountsFile()).getAbsolutePath())) {
      for (final StackTraceElement s : Thread.currentThread().getStackTrace()) {
        final String name = s.getClassName();
        if (name.equals(AccountStore.class.getName())) {
          return;
        }
      }
      throw new SecurityException();
    }
  }

  public static void fixHosts() {
    if (Configuration.getCurrentOperatingSystem() != OperatingSystem.WINDOWS) {
      return;
    }
    final File hosts = new File(System.getenv("SystemRoot") + "\\System32\\drivers\\etc\\hosts");
    if (!hosts.exists()) {
      return;
    }
    try {
      final StringBuilder modified = new StringBuilder((int) hosts.length());
      final BufferedReader reader = new BufferedReader(new FileReader(hosts));
      boolean infected = false;
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(Configuration.Paths.URLs.HOST)) {
          infected = true;
          continue;
        }
        modified.append(line);
        modified.append("\r\n");
      }
      reader.close();
      if (infected) {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(hosts));
        writer.append(modified.toString());
        writer.close();
      }
    } catch (final IOException ignored) {
    }
  }
}