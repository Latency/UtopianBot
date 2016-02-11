package org.rsbot.bot;

import java.util.ArrayList;

public class Languages {
  public static enum Language {
    ENGLISH(0, "gb", "English"),
    GERMAN(1, "de", "Deutsch"),
    FRENCH(2, "fr", "Français"),
    PORTUGUES(3, "br", "Português");

    private final int    id;
    private final String flag;
    private final String name;

    Language(final int id, final String flag, final String name) {
      this.id = id;
      this.flag = flag;
      this.name = name;
    }

    @Override
    public final String toString() {
      // only capitalize the first letter
      String s = super.toString();
      return s.substring(0, 1) + s.substring(1).toLowerCase();
    }

    /**
     * Will print the array as defined by enumerated type.
     * 
     * @return
     */
    public static final String[] toArray() {
      ArrayList<String> ary = new ArrayList<String>();
      for (final Language enm : Language.values())
        ary.add(enm.toString());
      return ary.toArray(new String[ary.size()]);
    }

    /**
     * Will print the array as defined by name type.
     * 
     * @return
     */
    public static final String[] toArrayFormal() {
      ArrayList<String> ary = new ArrayList<String>();
      for (final Language enm : Language.values())
        ary.add(enm.name); // + " (" + enm.flag.toUpperCase() + ")");
      return ary.toArray(new String[ary.size()]);
    }

    public static final Language toEnum(final String string) {
      if (string != null) {
        try {
          // Map this.name -> this.toString()
          for (final Language l : Language.values()) {
            if (l.name.equals(string) || l.name().equals(string))
              return l;
          }
        } catch (IllegalArgumentException ex) {
          ex.printStackTrace();
        }
      }
      throw new NullPointerException("toEnum(string) does not match enumerated type in '" + Languages.class.getName() + "'");
    }

    public final String getFlag() {
      return flag;
    }

    public final int getID() {
      return id;
    }

    public final String getName() {
      return name;
    }
  }
}