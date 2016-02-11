package org.rsbot.script.methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.rsbot.constants.Skill;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.util.StringUtil;

/**
 * This class is used to fetch the stats of another player.
 * <p/>
 * Example: Hiscores.Stats stats = hiscores.lookup("username"); int attack = stats.getCurrentLevel(Skills.ATTACK);
 * 
 * @author Aion
 * @version 0.2
 */
public class HiScores extends MethodProvider {
  private final OutputManager om = methods.locale;
  private static final String HOST = "http://hiscore.runescape.com";
  private static final String GET  = "/index_lite.ws?player=";

  public HiScores(final MethodContext ctx) {
    super(ctx);
  }
  
  /**
   * Collects data for a given player from the hiscore website.
   * 
   * @param username The username
   * @return An instance of Hiscores.Stats; <code>null</code> if unable to fetch data.
   */
  public Stats lookup(final String username) {
    if (username != null && !username.isEmpty()) {
      try {
        final URL url = new URL(HiScores.HOST + HiScores.GET + username);
        final BufferedReader br = new BufferedReader(new InputStreamReader(
          url.openStream()));
        String[] html;
        final int[] exps = new int[26];
        final int[] lvls = new int[26];
        final int[] ranks = new int[26];
        for (int i = 0; i < 26; i++) {
          html = br.readLine().split(",");
          exps[i] = Integer.parseInt(html[2]);
          lvls[i] = Integer.parseInt(html[1]);
          ranks[i] = Integer.parseInt(html[0]);
        }
        br.close();
        return new Stats(username, exps, lvls, ranks);
      } catch (final IOException ignored) {}
    }
    return null;
  }

  /**
   * Provides access to High Scores Information.
   * 
   * @author Jacmob, Aut0r
   */
  public class Stats {

    private final String username;
    
    final Skills sk = new Skills(methods);

    private final int[]  exps, lvls, ranks;

    Stats(final String username, final int[] exps, final int[] lvls, final int[] ranks) {
      this.username = username;
      this.exps = exps;
      this.lvls = lvls;
      this.ranks = ranks;
    }

    /**
     * Gets the experience of a given skill
     * 
     * @param index The index of the skill
     * @return The experience or -1
     */
    public int getExperience(final int index) {
      if (index < 0 || index >= exps.length - 1)
        throw new IllegalArgumentException(om.OS.toString("highIllegalSkillIndex" + ": ") + index);
      return exps[index + 1];
    }

    /**
     * Gets the level of a given skill
     * 
     * @param index The index of the skill
     * @return The level or -1
     */
    public int getLevel(final int index) {
      if (index < 0 || index >= exps.length - 1)
        throw new IllegalArgumentException(om.OS.toString("highIllegalSkillIndex" + ": ") + index);
      return lvls[index + 1];
    }

    /**
     * Gets the rank of a given skill
     * 
     * @param index The index of the skill
     * @return The rank or -1
     */
    public int getRank(final int index) {
      if (index < 0 || index >= exps.length - 1)
        throw new IllegalArgumentException(om.OS.toString("highIllegalSkillIndex" + ": ") + index);
      return ranks[index + 1];
    }

    /**
     * Gets the overall experience
     * 
     * @return The overall experience or -1
     */
    public int getOverallExp() {
      return exps[0];
    }

    /**
     * Gets the overall level (also known as total level)
     * 
     * @return The overall level or -1
     */
    public int getOverallLevel() {
      return lvls[0];
    }

    /**
     * Gets the overall rank
     * 
     * @return The overall rank or -1
     */
    public int getOverallRank() {
      return ranks[0];
    }

    /**
     * Gets the username of this instance
     * 
     * @return The username
     */
    public String getUsername() {
      return username;
    }

    @Override
    public String toString() {
      String sb = username;
      sb += "[";
      sb += om.Server.toString("hiScores.overall");
      sb += ":";
      sb += lvls[0];
      sb += ",";
      sb += ranks[0];
      sb += ",";
      sb += exps[0];
      sb += " ";
      for (final Skill skl : Skill.values()) {
        sb += StringUtil.CapitalizeString(skl.toString());
        sb += ":";
        sb += lvls[skl.ordinal()];
        sb += ",";
        sb += ranks[skl.ordinal()];
        sb += ",";
        sb +=exps[skl.ordinal()];
        sb += " ";
      }
      return sb.replaceFirst("\\s+$", "]");
    }

  }

}