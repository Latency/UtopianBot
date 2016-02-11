package org.rsbot.locale;

import java.util.Locale;

import org.rsbot.bot.Languages.Language;

public class OutputManager {
  private static Language     language;
  public ServerBundles        Server;
  public OSBundles            OS;
  
  // Default
  public OutputManager(final Locale locale) {
    language = Language.toEnum(locale.getDisplayLanguage(Locale.ENGLISH));  // Bot is written in English SO DO NOT CHANGE EVER.
    this.Server = new ServerBundles();
    this.OS = new OSBundles();
  }
  
  ////////////////////////////////////////////////////
  // Server
  ////////////////////////////////////////////////////
  public static class ServerBundles extends MessageBundler {
    public ServerBundles() {
      super(language);
    }
    
    public final String[] toString(final String[] array) {
      final String[] output = new String[array.length];
      for (int x = 0; x < array.length; x++)
        output[x] = this.toString(array[x]);
      return output;
    }

    public final String toString(final String key) {
      return toString(this.getClass().getPackage().getName() + ".server", key);
    }
  }

  ////////////////////////////////////////////////////
  // Locale
  ////////////////////////////////////////////////////
  public static class OSBundles extends MessageBundler {
    public OSBundles() {
      super(language);
    }
    
    public final String[] toString(final String[] array) {
      final String[] output = new String[array.length];
      for (int x = 0; x < array.length; x++)
        output[x] = this.toString(array[x]);
      return output;
    }
    
    public final String toString(final String key) {
      return toString(this.getClass().getPackage().getName() + ".locale", key);      
    }
  }
  
}