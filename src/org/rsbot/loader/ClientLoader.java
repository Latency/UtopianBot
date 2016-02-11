package org.rsbot.loader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.rsbot.Configuration;
import org.rsbot.loader.asm.ClassReader;
import org.rsbot.loader.script.ModScript;
import org.rsbot.util.io.HttpClient;

/**
 */
public class ClientLoader {

  private final Logger        log   = Logger.getLogger(ClientLoader.class.getName());

  private ModScript           script = null;
  private Map<String, byte[]> classes;
  private int                 world = nextWorld();

  public void init(final URL script, final File cache) throws IOException, ParseException {
    byte[] data = null;
    FileInputStream fis = null;
    
    try {
      HttpClient.download(script, cache);
    } catch (final IOException ioe) {
      if (cache.exists())
        log.warning("Unable to download client patch, attempting to use cached copy.");
    }
    
    try {
      fis = new FileInputStream(cache);
      data = load(fis);
    } catch (final IOException ioe) {
      if (cache.exists())
        log.severe("Could not load client patch.");
    } finally {
      try {
        if (fis != null)
          fis.close();
      } catch (final IOException ignored) {
      }
    }

    this.script = new ModScript(data);
  }

  public void load(final File cache, final File version_file) throws IOException  {
    classes = new HashMap<String, byte[]>();
    final int version = script.getVersion(); // v652
    int cached_version = 0;

    // /////////////////////////////////////////////////////
    // Read localhost version.txt => cached_version
    // /////////////////////////////////////////////////////

    // Check if the client.dat (cache_file) && info.dat (version_file) exists
    if (cache.exists() && version_file.exists()) {
      // Open the file stream for version.text and read the 1st line and store it to cached_version.
      BufferedReader reader = new BufferedReader(new FileReader(version_file)); // Read info.dat
      cached_version = Integer.parseInt(reader.readLine()); // v652
      reader.close();
    }

    // (Mod)script versioning
    if (version <= cached_version) { // v652 == v652
      JarFile client = new JarFile(cache);
      if (client != null) {
        log.info("Processing client");
        Enumeration<JarEntry> entries = client.entries();
        while (entries.hasMoreElements()) {
          final JarEntry entry = entries.nextElement();
          String name = entry.getName();
          if (name.endsWith(".class")) {
            name = name.substring(0, name.length() - 6).replace('/', '.');
            classes.put(name, script.process(name, client.getInputStream(entry)));
          }
        }
      }
    } else {
      final String target = script.getAttribute("target");
      log.info("Downloading client: " + target);
      final JarFile loader = getJar(target, true);
      final JarFile client = getJar(target, false);

      final List<String> replace = Arrays.asList(script.getAttribute("replace").split(" "));

      Enumeration<JarEntry> entries = client.entries();
      while (entries.hasMoreElements()) {
        final JarEntry entry = entries.nextElement();
        String name = entry.getName();
        if (name.endsWith(".class")) {
          name = name.substring(0, name.length() - 6).replace('/', '.');
          classes.put(name, load(client.getInputStream(entry)));
        }
      }

      entries = loader.entries();
      while (entries.hasMoreElements()) {
        final JarEntry entry = entries.nextElement();
        String name = entry.getName();
        if (name.endsWith(".class")) {
          name = name.substring(0, name.length() - 6).replace('/', '.');
          if (replace.contains(name)) {
            classes.put(name, load(loader.getInputStream(entry)));
          }
        }
      }

      FileOutputStream stream = new FileOutputStream(cache);
      JarOutputStream out = new JarOutputStream(stream);

      for (final Map.Entry<String, byte[]> entry : classes.entrySet()) {
        out.putNextEntry(new JarEntry(entry.getKey() + ".class"));
        out.write(entry.getValue());
      }

      out.close();
      stream.close();

      int client_version = checkVersion(new ByteArrayInputStream(classes.get("client")));
      if (client_version != 0) {
        FileWriter writer = new FileWriter(Configuration.Paths.getVersionCache());
        writer.write(Integer.toString(client_version));
        writer.close();
      }

      log.info("Processing client");
      for (final Map.Entry<String, byte[]> entry : classes.entrySet())
        entry.setValue(script.process(entry.getKey(), entry.getValue()));
    }
  }

  public Map<String, byte[]> getClasses() {
    return classes;
  }

  public String getTargetName() {
    return script.getAttribute("target");
  }

  private int checkVersion(final InputStream in) throws IOException {
    final ClassReader reader = new ClassReader(in);
    final VersionVisitor vv = new VersionVisitor();
    reader.accept(vv, ClassReader.SKIP_FRAMES);
    if (vv.getVersion() != script.getVersion()) { // 652 != 652
      JOptionPane.showMessageDialog(
          null,
          "The bot is currently oudated, please wait patiently for a new version.",
          "Outdated",
          JOptionPane.INFORMATION_MESSAGE);
      throw new IOException("ModScript #" + script.getVersion() + " != #" + vv.getVersion());
    }
    return vv.getVersion();
  }

  private JarFile getJar(final String target, final boolean loader) {
    while (true) {
      try {
        String s = "jar:http://world" + world + "." + target + ".com/";
        s += (loader ? "loader.jar!/" : target + ".jar!/");
        final URL url = new URL(s);
        final JarURLConnection juc = (JarURLConnection) url.openConnection();
        juc.setConnectTimeout(5000);
        return juc.getJarFile();
      } catch (final Exception ignored) {
        world = nextWorld();
      }
    }
  }

  private static int nextWorld() {
    return 1 + new Random().nextInt(169);
  }

  private static byte[] load(final InputStream is) throws IOException {
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    final byte[] buffer = new byte[4096];
    int n;
    while ((n = is.read(buffer)) != -1) {
      os.write(buffer, 0, n);
    }
    return os.toByteArray();
  }
}