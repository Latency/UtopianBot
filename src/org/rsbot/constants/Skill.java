package org.rsbot.constants;

public enum Skill {
  ATTACK,
  DEFENSE,
  STRENGTH,
  CONSTITUTION,
  RANGE,
  PRAYER,
  MAGIC,
  COOKING,
  WOODCUTTING,
  FLETCHING,
  FISHING,
  FIREMAKING,
  CRAFTING,
  SMITHING,
  MINING,
  HERBLORE,
  AGILITY,
  THIEVING,
  SLAYER,
  FARMING,
  RUNECRAFTING,
  HUNTER,
  CONSTRUCTION,
  SUMMONING,
  DUNGEONEERING;
  
  /**
   * Gets the index of the skill provided a given name. This is not case sensitive.
   * 
   * @param name The skill's name.
   * @return The index of the specified skill; otherwise -1.
   */
  public final static int getIndex(final String name) {
    final Skill skl = getValue(name);
    if (skl == null)
      return -1;
    return skl.ordinal();
  }
  
  /**
   * Gets the enumerated value of the skill provided a given name. This is not case sensitive.
   * 
   * @param name The skill's name.
   * @return The index of the specified skill; otherwise -1.
   */
  public final static Skill getValue(final String name) {
    for (final Skill skl : values()) 
      if (skl.toString().equalsIgnoreCase(name))
        return skl;
    return null;
  }

  /**
   * Gets the skill name of a provided index.
   * 
   * @param index The index.
   * @return The name of the skill for that index.
   */
  public final static String getSkillName(final int index) {
    for (final Skill skl : values()) {
      if (index == skl.ordinal())
        return skl.toString();
    }
    return null;
  }
}