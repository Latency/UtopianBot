package org.rsbot.script.methods;

import org.rsbot.constants.Skill;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.util.Timer;

/**
 * This class is for all the skill calculations.
 * <p/>
 * Example usage: skills.getRealLevel(Skills.ATTACK);
 */
public class Skills extends MethodProvider {
  public Skills(final MethodContext ctx) {
    super(ctx);
  }

  /**
   * A table containing the experiences that begin each level.
   */
  public static final int[]    XP_TABLE                = { 0, 0, 83, 174, 276, 388, 512, 650,
                                                       801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
                                                       3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
                                                       13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
                                                       33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
                                                       83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
                                                       184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
                                                       407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
                                                       899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
                                                       1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
                                                       3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
                                                       7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431,
                                                       14391160, 15889109, 17542976, 19368992, 21385073, 23611006,
                                                       26068632, 28782069, 31777943, 35085654, 38737661, 42769801,
                                                       47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
                                                       85539082, 94442737, 104273167 };



  public static final int      INTERFACE_TAB_STATS     = 320;
  public static final int      INTERFACE_ATTACK        = 1;
  public static final int      INTERFACE_DEFENSE       = 22;
  public static final int      INTERFACE_STRENGTH      = 4;
  public static final int      INTERFACE_CONSTITUTION  = 2;
  public static final int      INTERFACE_RANGE         = 46;
  public static final int      INTERFACE_PRAYER        = 70;
  public static final int      INTERFACE_MAGIC         = 87;
  public static final int      INTERFACE_COOKING       = 62;
  public static final int      INTERFACE_WOODCUTTING   = 102;
  public static final int      INTERFACE_FLETCHING     = 95;
  public static final int      INTERFACE_FISHING       = 38;
  public static final int      INTERFACE_FIREMAKING    = 85;
  public static final int      INTERFACE_CRAFTING      = 78;
  public static final int      INTERFACE_SMITHING      = 20;
  public static final int      INTERFACE_MINING        = 3;
  public static final int      INTERFACE_HERBLORE      = 30;
  public static final int      INTERFACE_AGILITY       = 12;
  public static final int      INTERFACE_THIEVING      = 54;
  public static final int      INTERFACE_SLAYER        = 112;
  public static final int      INTERFACE_FARMING       = 120;
  public static final int      INTERFACE_RUNECRAFTING  = 104;
  public static final int      INTERFACE_HUNTER        = 136;
  public static final int      INTERFACE_CONSTRUCTION  = 128;
  public static final int      INTERFACE_SUMMONING     = 144;
  public static final int      INTERFACE_DUNGEONEERING = 152;

  private final int[] startExp = new int[Skill.values().length],
                      startLvl = new int[Skill.values().length];
  private Timer runTimer = null;
  
  public void startTimer(final Timer runTimer) {
    for (final Skill skl : Skill.values()) {
      startExp[skl.ordinal()] = exp(skl);
      startLvl[skl.ordinal()] = level(skl);
    }
    this.runTimer = runTimer != null ? runTimer : new Timer(0);
  }


  public int level(final Skill skl) {
    return methods.skills.getCurrentLevel(skl);
  }

  public int exp(final Skill skl) {
    return methods.skills.getCurrentExp(skl);
  }

  public int levelsGained(final Skill skl) {
    return level(skl) - startLvl[skl.ordinal()];
  }

  public int expGain(final Skill skl) {
    return exp(skl) - startExp[skl.ordinal()];
  }

  public int expToLevel(final Skill skl) {
    return expToLevel(skl, level(skl) + 1);
  }

  public int expToLevel(final Skill skl, final int lvl) {
    if (lvl < 1 || lvl > 99)
      return -1;
    if (lvl == 99)
      return 0;
    return Skills.XP_TABLE[lvl] - exp(skl);
  }

  public double percentToLevel(final Skill skl) {
    return percentToLevel(skl, level(skl) + 1);
  }

  public double percentToLevel(final Skill skl, final int lvl) {
    int curLvl = level(skl);
    if (lvl < 1 || lvl > 99)
      return 0;
    if (curLvl == 99 || curLvl == lvl)
      return 100;
    return ((100 * (exp(skl) - Skills.XP_TABLE[curLvl])) / (Skills.XP_TABLE[lvl] - Skills.XP_TABLE[curLvl]));
  }

  public double hourlyExp(final Skill skl) {
    return expGain(skl) * Timer.MILLISECS_PER_HOUR / runTimer.getElapsed();
  }

  public long timeToLevel(final Skill skl) {
    double hourlyExp = hourlyExp(skl);
    if (hourlyExp == 0)
      return 0;
    return Timer.MILLISECS_PER_SEC * (long) ((expToLevel(skl) * Timer.SECS_PER_HOUR) / hourlyExp);
  }

  public long timeToLevel(final Skill skl, final int level) {
    double hourlyExp = hourlyExp(skl);
    if (hourlyExp == 0) {
      return 0;
    }
    return Timer.MILLISECS_PER_SEC * (long) ((expToLevel(skl, level) * Timer.SECS_PER_HOUR) / hourlyExp);
  }
  
  /**
   * Gets the level at the given experience.
   * 
   * @param exp The experience.
   * @return The level based on the experience given.
   * @see #XP_TABLE
   */
  public static int getLevelAt(final int exp) {
    for (int i = Skills.XP_TABLE.length - 1; i > 0; i--) {
      if (exp > Skills.XP_TABLE[i])
        return i;
    }
    return 1;
  }

  /**
   * Gets the experience at the given level.
   * 
   * @param lvl The level.
   * @return The level based on the experience given.
   */
  public static int getExpAt(final int lvl) {
    if (lvl > 120)
      return 1;
    return Skills.XP_TABLE[lvl - 1];
  }

  /**
   * Gets the experience required for the given level.
   * 
   * @param lvl The level.
   * @return The level based on the experience given.
   */
  public static int getExpRequired(final int lvl) {
    if (lvl > 120)
      return 1;
    return Skills.XP_TABLE[lvl];
  }

  /**
   * Gets the current experience for the given skill.
   * 
   * @param index The index of the skill.
   * @return -1 if the skill is unavailable
   */
  public int getCurrentExp(final Skill skl) {
    if (skl == null)
      return -1;
    return methods.client.getSkillExperiences()[skl.ordinal()];
  }

  /**
   * Gets the effective level of the given skill (accounting for temporary boosts and reductions).
   * 
   * @param index The index of the skill.
   * @return The current level of the given Skill.
   */
  public int getCurrentLevel(final Skill skl) {
    if (skl == null)
      return -1;
    return methods.client.getSkillLevels()[skl.ordinal()];
  }

  /**
   * Gets the player's current level in a skill based on their experience in that skill.
   * 
   * @param index The index of the skill.
   * @return The real level of the skill.
   * @see #getRealLevel(int)
   */
  public int getRealLevel(final Skill skl) {
    if (skl == null)
      return -1;
    return Skills.getLevelAt(getCurrentExp(skl));
  }

  /**
   * Gets the percentage to the next level in a given skill.
   * 
   * @param index The index of the skill.
   * @return The percent to the next level of the provided skill or 0 if level of skill is 99.
   */
  public int getPercentToNextLevel(final Skill skl) {
    if (skl == null)
      return -1;
    final int lvl = getRealLevel(skl);
    return getPercentToLevel(skl, lvl + 1);
  }

  /**
   * Gets the percentage to the a level in a given skill.
   * 
   * @param index The index of the skill.
   * @param endLvl The level for the percent.
   * @return The percent to the level provided of the provided skill or 0 if level of skill is 99.
   */
  public int getPercentToLevel(final Skill skl, final int endLvl) {
    if (skl == null)
      return -1;
    final int lvl = getRealLevel(skl);
    if (skl == Skill.DUNGEONEERING && (lvl == 120 || endLvl > 120)) {
      return 0;
    } else if (lvl == 99 || endLvl > 99) {
      return 0;
    }
    final int xpTotal = Skills.XP_TABLE[endLvl] - Skills.XP_TABLE[lvl];
    if (xpTotal == 0)
      return 0;
    final int xpDone = getCurrentExp(skl) - Skills.XP_TABLE[lvl];
    return 100 * xpDone / xpTotal;
  }

  /**
   * Gets the maximum level of a given skill.
   * 
   * @param index The index of the skill.
   * @return The max level of the skill.
   */
  public int getMaxLevel(final Skill skl) {
    if (skl == null)
      return -1;
    return methods.client.getSkillLevelMaxes()[skl.ordinal()];
  }

  /**
   * Gets the maximum experience of a given skill.
   * 
   * @param index The index of the skill.
   * @return The max experience of the skill.
   */
  public int getMaxExp(final Skill skl) {
    if (skl == null)
      return -1;
    return methods.client.getSkillExperiencesMax()[skl.ordinal()];
  }

  /**
   * Gets the experience remaining until reaching the next level in a given skill.
   * 
   * @param index The index of the skill.
   * @return The experience to the next level of the skill.
   */
  public int getExpToNextLevel(final Skill skl) {
    if (skl == null)
      return -1;
    final int lvl = getRealLevel(skl);
    return getExpToLevel(skl, lvl + 1);
  }

  /**
   * Gets the experience remaining until reaching the a level in a given skill.
   * 
   * @param index The index of the skill.
   * @param endLvl The level for the experience remaining.
   * @return The experience to the level provided of the skill.
   */
  public int getExpToLevel(final Skill skl, final int endLvl) {
    if (skl == null)
      return -1;
    final int lvl = getRealLevel(skl);
    if ((skl == Skill.DUNGEONEERING && (lvl == 120 || endLvl > 120)) || (lvl == 99 || endLvl > 99))
      return 0;
    return Skills.XP_TABLE[endLvl] - getCurrentExp(skl);
  }

  /**
   * Gets the time remaining until the next level.
   * 
   * @param index The index of the skill.
   * @param exp The start Exp.
   * @param time The time the script has been running.
   * @return The time till the next level of the skill.
   */
  public long getTimeTillNextLevel(final Skill skl, final int exp, final long time) {
    if (skl == null)
      return -1;
    final int level = getRealLevel(skl);
    return getTimeTillLevel(skl, exp, level + 1, time);
  }

  /**
   * Gets the time remaining until the level provided.
   * 
   * @param index The index of the skill.
   * @param exp The start Exp.
   * @param endLvl The level to get the time till for.
   * @param time The time the script has been running.
   * @return The time till the level provided of the skill.
   */
  public long getTimeTillLevel(final Skill skl, final int exp, final int endLvl, final long time) {
    if (skl == null)
      return -1;
    final int level = getRealLevel(skl);
    final int currentExp = getCurrentExp(skl);
    if ((skl == Skill.DUNGEONEERING && level == 120) || level == 99)
      return 0;
    try {
      return time * getExpToLevel(skl, endLvl) / (currentExp - exp);
    } catch (final Exception e) {
      return -1;
    }
  }

  /**
   * Gets the number of actions needed until the next level.
   * 
   * @param index The index.
   * @param exp The exp each action gives.
   * @return How many you need to do until the next level.
   */
  public int ammountTillNextLevel(final Skill skl, final double exp) {
    if (skl == null)
      return -1;
    final int level = getRealLevel(skl);
    return ammountTillLevel(skl, exp, level + 1);
  }

  /**
   * Gets the number of actions needed until the level provided.
   * 
   * @param index The index.
   * @param exp The exp each action gives.
   * @param lvl The level to get the ammount till for.
   * @return How many you need to do until leveling to the level provided.
   */
  public int ammountTillLevel(final Skill skl, final double exp, final int lvl) {
    if (skl == null)
      return -1;
    return getExpToLevel(skl, lvl) != -1 ? (int) (getExpToLevel(skl, lvl) / exp) : 0;
  }

  /**
   * Gets the total/overall level.
   * 
   * @return The total/overall level.
   */
  public int getTotalLevel() {
    int total = 0;
    for (final Skill skl : Skill.values())
      total += getRealLevel(skl);
    return total;
  }

  /**
   * Gets the percent to max level in the specified skill index
   * 
   * @param index Skill index.
   * @return Percent to level max level.
   */
  public int getPercentToMaxLevel(final Skill skl) {
    if (skl == null)
      return -1;
    int lvl = 99;
    if (skl == Skill.DUNGEONEERING)
      lvl = 120;
    return getPercentToLevel(skl, lvl);
  }

  /**
   * Gets the experience needed to the max level in the specified skill index
   * 
   * @param index Skill index.
   * @return Experience to level max level.
   */
  public int getExpToMaxLevel(final Skill skl) {
    if (skl == null)
      return -1;
    int lvl = 99;
    if (skl == Skill.DUNGEONEERING)
      lvl = 120;
    return getExpToLevel(skl, lvl);
  }

  /**
   * Gets the time remaining until the max level.
   * 
   * @param index The index of t he skill.
   * @param exp The start Exp.
   * @param time The time the script has been running.
   * @return The time till the max level of the skill.
   */
  public long getTimeTillMaxLevel(final Skill skl, final int exp, final long time) {
    if (skl == null)
      return -1;
    int lvl = 99;
    if (skl == Skill.DUNGEONEERING)
      lvl = 120;
    return getTimeTillLevel(skl, exp, lvl, time);
  }

  /**
   * Gets the number of actions needed until the max level.
   * 
   * @param index The index.
   * @param exp The exp each action gives.
   * @return How many you need to do until the max level.
   */
  public int ammountTillMaxLevel(final Skill skl, final double exp) {
    if (skl == null)
      return -1;
    int lvl = 99;
    if (skl == Skill.DUNGEONEERING)
      lvl = 120;
    return ammountTillLevel(skl, exp, lvl);
  }

  /**
   * Moves the mouse over a given component in the stats tab.
   * 
   * @param component The component index.
   * @return <tt>true</tt> if the mouse was moved over the given component index.
   */
  public boolean doHover(final int component) {
    methods.game.openTab(Game.Tab.STATS);
    sleep(random(10, 100));
    return methods.interfaces.getComponent(INTERFACE_TAB_STATS, component).doHover();
  }

  /**
   * Checks if one of the given skills is boosted.
   * 
   * @param index The index of the skill.
   * @return <tt>true</tt> if one the given skills is boosted.
   */
  public boolean isSkillBoosted(final Skill... skls) {
    for (final Skill skl : skls) {
      if (skl == null)
        continue;
      int realLevel = getRealLevel(skl);
      if (realLevel > getMaxLevel(skl)) {
        switch (skl) {
          case DUNGEONEERING:
            realLevel = 120;
            break;
          default:
            realLevel = 99;
            break;
        }
      }
      if (realLevel == getCurrentLevel(skl))
        return false;
    }
    return true;
  }

}