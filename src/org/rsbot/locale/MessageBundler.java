package org.rsbot.locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.rsbot.bot.Languages.Language;

public class MessageBundler {
  private Language language;
  private static final Logger               log              = Logger.getLogger(MessageBundler.class.getName());
  
  protected MessageBundler(final Language language) {
    setLocale(language);
  }
  
  public void setLocale(final Language language) {
    this.language = language;
  }

  private static String parseTag(final String key) {
    int sz = key.length(), multi_dot = 0;
    Character c;
    for (int i = 0; i < sz; i++) {
      c = key.charAt(i);
      if (c == '.')
        multi_dot++;
      if (!(Character.isLetterOrDigit(c) == true || (c == '.' && multi_dot <= 1)) || (i == 0 && c == '.'))
        throw new IllegalArgumentException("Invalid string format detected for macro in file " + MessageBundler.class.getName());
    }
    return key;
  }
  
  protected final String toString(final String bundle, final String key) {
    if (key == null || key.isEmpty())
      throw new NullPointerException("Empty string detected for macro in file " + MessageBundler.class.getName());
    ResourceBundle labels;
    try {
      labels = ResourceBundle.getBundle(bundle, new Locale(language.getFlag()));
    } catch (final NullPointerException e) {
      throw new NullPointerException("Null pointer for messages detected.");
    } catch (final MissingResourceException e) {
      return '!' + key + '!';
    }
    try {
      return labels.getString(parseTag(key));
    } catch (final MissingResourceException e) {
      log.warning("Tag not found for !" + key + "!");
      return '!' + key + '!';
    }
  }
}
