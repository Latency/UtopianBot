package org.rsbot.script.randoms;

import java.awt.Point;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSCharacter;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;

/*
 * Updated by Iscream Feb 04,10
 * Updated by Iscream Feb 20,10
 * Updated by Iscream Mar 01,10
 * Updated by Arbiter Sep 20,10
 * Updated by Arbiter Sep 23,10
 * Updated by icnhzabot June 08,11
 * -------------------------------->Need to complete @ Ministry
 */
@ScriptManifest(authors = { "Arbiter", "PwnZ", "Megaalgos", "Taha", "Fred", "Poxer", "Iscream" }, name = "Exam", version = 2.2)
public class Exam extends Random {
  public Exam(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;

  public class NextObjectQuestion {
    int One   = -1;
    int Two   = -1;
    int Three = -1;

    public NextObjectQuestion() {
    }

    public boolean arrayContains(final int[] arr, final int i) {
      boolean returnt = false;
      for (final int num : arr) {
        if (num == i) {
          returnt = true;
        }
      }

      return returnt;
    }

    public boolean clickAnswer() {
      int[] Answers;
      if ((Answers = returnAnswer()) == null) {
        return false;
      }

      for (int i = 10; i <= 13; i++) {
        if (arrayContains(Answers, methods.interfaces.get(nextObjectInterface).
            getComponent(i).getComponentID())) {
          return methods.interfaces.get(nextObjectInterface).getComponent(i).doClick();
        }
      }

      return false;
    }

    public boolean getObjects() {
      One = -1;
      Two = -1;
      Three = -1;
      One = methods.interfaces.get(nextObjectInterface).getComponent(6).getComponentID();
      Two = methods.interfaces.get(nextObjectInterface).getComponent(7).getComponentID();
      Three = methods.interfaces.get(nextObjectInterface).getComponent(8).getComponentID();

      return One != -1 && Two != -1 && Three != -1;
    }

    public void guess() {
      methods.interfaces.get(nextObjectInterface).getComponent(random(10, 14)).doClick();
    }

    public int[] returnAnswer() {
      final int[] count = new int[items.length];
      int firstcard = 0;
      int secondcard = 0;
      int thirdcard = 0;

      for (int i = 0; i < count.length; i++) {
        count[i] = 0;
      }
      // Will verify that all IDs are IDs which we currently have
      for (final int[] item : items) {
        for (final int anItem : item) {
          if (anItem == One) {
            firstcard = 1;
          }
          if (anItem == Two) {
            secondcard = 1;
          }
          if (anItem == Three) {
            thirdcard = 1;
          }
        }
      }
      if (firstcard == 0) {
        log.severe(om.OS.toString("examFirstObject"));
        log.severe(om.OS.toString("examObjectID") + " :" + Integer.toString(One));
      }
      if (secondcard == 0) {
        log.severe(om.OS.toString("examSecondObject"));
        log.severe(om.OS.toString("examObjectID") + " :" + Integer.toString(Two));
      }
      if (thirdcard == 0) {
        log.severe(om.OS.toString("examThirdObject"));
        log.severe(om.OS.toString("examObjectID") + " :" + Integer.toString(Three));
      }

      for (int i = 0; i < items.length; i++) {
        for (int j = 0; j < items[i].length; j++) {
          if (items[i][j] == One) {
            count[i]++;
          }
          if (items[i][j] == Two) {
            count[i]++;
          }
          if (items[i][j] == Three) {
            count[i]++;
          }
          if (count[i] >= 2) {
            log.info(om.OS.toString("examAnswer"));
            return items[i];
          }
        }
      }

      return null;
    }
  }

  public class SimilarObjectQuestion {
    String      question;
    final int[] Answers;

    public SimilarObjectQuestion(final String q, final int[] Answers) {
      this.question = methods.locale.Server.toString(q).toLowerCase();
      this.Answers = Answers;
    }

    public void accept() {
      RSInterface inter = methods.interfaces.get(relatedCardsInterface);
      if (inter.isValid()) {
        RSComponent OK = inter.getComponent(26);
        for (int i = 0; i < 5 && inter.isValid() && OK.doClick(true); i++) {
          sleep(random(100, 300));
          if (inter.isValid()) {
            sleep(random(100, 150));
            if (inter.isValid()) {
              methods.mouse.moveSlightly();
            }
          }
        }
      }
    }

    public boolean activateCondition() {
      if (!methods.interfaces.get(relatedCardsInterface).isValid()) {
        return false;
      }

      if (methods.interfaces.getComponent(relatedCardsInterface, 25).getText().toLowerCase().contains(question)) {
        log.info(om.OS.toString("examQuestion") + ": " + question);
        return true;
      }

      return false;
    }

    public boolean clickObjects() {
      int count = 0;
      for (int i = 42; i <= 56; i++) {
        for (final int answer : Answers) {
          if (methods.interfaces.getComponent(relatedCardsInterface, i).getComponentID() == answer) {
            if (methods.interfaces.getComponent(relatedCardsInterface, i).doClick()) {
              sleep(random(600, 1000));
            }
            count++;
            if (count >= 3) {
              return true;
            }
          }
        }
      }
      log.info(om.OS.toString("examErrorClicking"));
      return false;
    }
  }

  private static final int              nextObjectInterface   = 103;
  private static final int              relatedCardsInterface = 559;

  private static final int[]            Ranged                = { 11539, 11540, 11541, 11614, 11615, 11633 };

  private static final int[]            Cooking               = { 11526, 11529, 11545, 11549, 11550, 11555, 11560, 11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
                                                              11628,

                                                              11629, 11634, 11639, 11641, 11649, 11624 };

  private static final int[]            Fishing               = { 11527, 11574, 11578, 11580, 11599, 11600, 11601, 11602, 11603,

                                                              11604, 11605, 11606, 11625 };

  private static final int[]            Combat                = { 11528, 11531, 11536, 11537, 11579, 11591, 11592, 11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648,
                                                              11617 };

  private static final int[]            Farming               = { 11530, 11532, 11547, 11548, 11554, 11556, 11571, 11581, 11586, 11610, 11645 };

  private static final int[]            Magic                 = { 11533, 11534, 11538, 11562, 11567, 11582 };

  private static final int[]            Firemaking            = { 11535, 11551, 11552, 11559, 11646 };

  private static final int[]            Hats                  = { 11540, 11557, 11558, 11560, 11570, 11619, 11626, 11630, 11632, 11637, 11654 };
  private static final int[]            Pirate                = { 11570, 11626, 11558 };

  private static final int[]            Jewellery             = { 11572, 11576, 11652 };

  private static final int[]            Jewellery2            = { 11572, 11576, 11652 };

  private static final int[]            Drinks                = { 11542, 11543, 11544, 11644, 11647 };

  private static final int[]            Woodcutting           = { 11573, 11595 };

  private static final int[]            Boots                 = { 11561, 11618, 11650, 11651 };

  private static final int[]            Crafting              = { 11546, 11553, 11565, 11566, 11568, 11569, 11572, 11575, 11576, 11577, 11581, 11583, 11584, 11585, 11643, 11652,
                                                              11653 };

  private static final int[]            Mining                = { 11587, 11588, 11594, 11596, 11598, 11609, 11610 };

  private static final int[]            Smithing              = { 11611, 11612, 11613 };
  private static final int[][]          items                 = { Ranged, Cooking, Fishing, Combat, Farming, Magic, Firemaking, Hats, Drinks, Woodcutting, Boots, Crafting, Mining,
                                                              Smithing };

  private final SimilarObjectQuestion[] simObjects            = {
                                                              new SimilarObjectQuestion("examJewellery.1", Jewellery),
                                                              new SimilarObjectQuestion("examJewellery.2", Jewellery2),
                                                              new SimilarObjectQuestion("examDrinks.1", Drinks),
                                                              new SimilarObjectQuestion("examDrinks.2", Drinks),
                                                              new SimilarObjectQuestion("examDrinks.3", Drinks),
                                                              new SimilarObjectQuestion("examDrinks.4", Drinks),
                                                              new SimilarObjectQuestion("examFishy", Fishing),
                                                              new SimilarObjectQuestion("examFishing", Fishing),
                                                              new SimilarObjectQuestion("examHats.1", Hats),
                                                              new SimilarObjectQuestion("examHats.2", Hats),
                                                              new SimilarObjectQuestion("examHats.3", Hats),
                                                              new SimilarObjectQuestion("examHats.4", Hats),
                                                              new SimilarObjectQuestion("examHats.5", Hats),
                                                              new SimilarObjectQuestion("examHats.6", Hats),
                                                              new SimilarObjectQuestion("examMagic.1", Magic),
                                                              new SimilarObjectQuestion("examMagic.2", Magic),
                                                              new SimilarObjectQuestion("examMagic.3", Magic),
                                                              new SimilarObjectQuestion("examCombat.1", Combat),
                                                              new SimilarObjectQuestion("examCombat.2", Combat),
                                                              new SimilarObjectQuestion("examCombat.3", Combat),
                                                              new SimilarObjectQuestion("examPirate.1", Pirate),
                                                              new SimilarObjectQuestion("examPirate.2", Pirate),
                                                              new SimilarObjectQuestion("examPirate.3", Pirate),
                                                              new SimilarObjectQuestion("examRanged.1", Ranged),
                                                              new SimilarObjectQuestion("examRanged.2", Ranged),
                                                              new SimilarObjectQuestion("examRanged.3", Ranged),
                                                              new SimilarObjectQuestion("examCrafting", Crafting),
                                                              new SimilarObjectQuestion("examFiremaking.1", Firemaking),
                                                              new SimilarObjectQuestion("examFiremaking.2", Firemaking),
                                                              new SimilarObjectQuestion("examFiremaking.3", Firemaking),
                                                              new SimilarObjectQuestion("examFiremaking.4", Firemaking),
                                                              // added diguised Feb 04,2010

      // Default questions just incase the bot gets stuck
      new SimilarObjectQuestion("range", Ranged),
      new SimilarObjectQuestion("arrow", Ranged),
      new SimilarObjectQuestion("drink", Drinks),
      new SimilarObjectQuestion("logs", Firemaking),
      new SimilarObjectQuestion("light", Firemaking),
      new SimilarObjectQuestion("headgear", Hats),
      new SimilarObjectQuestion("hat", Hats),
      new SimilarObjectQuestion("cap", Hats),
      new SimilarObjectQuestion("mine", Mining),
      new SimilarObjectQuestion("mining", Mining),
      new SimilarObjectQuestion("ore", Mining),
      new SimilarObjectQuestion("fish", Fishing),
      new SimilarObjectQuestion("fishing", Fishing),
      new SimilarObjectQuestion("thinkingCap", Hats),
      new SimilarObjectQuestion("cooking", Cooking),
      new SimilarObjectQuestion("cook", Cooking),
      new SimilarObjectQuestion("bake", Cooking),
      new SimilarObjectQuestion("farm", Farming),
      new SimilarObjectQuestion("farming", Farming),
      new SimilarObjectQuestion("cast", Magic),
      new SimilarObjectQuestion("magic", Magic),
      new SimilarObjectQuestion("craft", Crafting),
      new SimilarObjectQuestion("boot", Boots),
      new SimilarObjectQuestion("chop", Woodcutting),
      new SimilarObjectQuestion("cut", Woodcutting),
      new SimilarObjectQuestion("tree", Woodcutting),

                                                              };

  private RSObject                      door                  = null;

  @Override
  public boolean activateCondition() {
    door = null;
    return methods.npcs.getNearest(om.Server.toString("examMrMordaut")) != null;
  }

  /*
   * Don't use this with any other monster.I edited for this script only cause Mr. Mordaunt doesn't move
   */
  void clickCharacter(final RSCharacter c, final String action) {
    try {
      Point screenLoc;
      screenLoc = c.getScreenLocation();

      if (!c.isValid() || !methods.calc.pointOnScreen(screenLoc)) {
        System.out.println(om.OS.toString("examNotOnScreen") + " " + action);
        return;
      }

      methods.mouse.move(screenLoc);

      screenLoc = c.getScreenLocation();

      final String[] items = methods.menu.getItems();

      if (items[0].toLowerCase().contains(action.toLowerCase())) {
        methods.mouse.click(screenLoc, true);
      } else {
        methods.mouse.click(screenLoc, false);
        for (int i = 0; i < 25 && !methods.menu.isOpen(); i++) {
          sleep(random(70, 80));
        }
        methods.menu.doAction(action);
      }

    } catch (final NullPointerException ignored) {
    }
  }

  // My clickObject, like clickCharacter, and faster than atObject.
  void clickObject(final RSObject c, final String action) {
    try {
      Point screenLoc;
      int X = (int) methods.calc.tileToScreen(c.getLocation()).getX();
      int Y = (int) methods.calc.tileToScreen(c.getLocation()).getY();

      screenLoc = new Point(X, Y);

      if (c == null || !methods.calc.pointOnScreen(screenLoc)) {
        log(om.OS.toString("examNotOnScreen") + " " + action);
        return;
      }

      methods.mouse.move(screenLoc);

      X = (int) methods.calc.tileToScreen(c.getLocation()).getX();
      Y = (int) methods.calc.tileToScreen(c.getLocation()).getY();
      screenLoc = new Point(X, Y);
      if (!methods.mouse.getLocation().equals(screenLoc)) {
        return;
      }

      final String[] items = methods.menu.getItems();
      if (items.length <= 1) {
        return;
      }
      if (items[0].toLowerCase().contains(action.toLowerCase())) {
        methods.mouse.click(screenLoc, true);
      } else {
        methods.mouse.click(screenLoc, false);
        for (int i = 0; i < 25 && !methods.menu.isOpen(); i++) {
          sleep(random(70, 80));
        }
        methods.menu.doAction(action);
      }
    } catch (final NullPointerException ignored) {
    }
  }

  @Override
  public int loop() {
    final RSNPC mordaut = methods.npcs.getNearest(om.Server.toString("examMrMordaut"));
    if (mordaut == null) {
      return -1;
    }

    if (methods.players.getMyPlayer().isMoving() || methods.players.getMyPlayer().getAnimation() != -1) {
      return random(800, 1200);
    }

    if (door != null) {
      if (methods.calc.distanceTo(door) > 3) {
        methods.walking.getPath(door.getLocation()).traverse();
        sleep(random(1400, 2500));
      }
      if (!methods.calc.tileOnScreen(door.getLocation())) {
        methods.walking.walkTileMM(door.getLocation());
        sleep(random(1400, 2500));
      }
      if (door.getID() == 2188) {
        methods.camera.setCompass('w');
      }
      if (door.getID() == 2193) {
        methods.camera.setCompass('e');
      }
      if (door.getID() == 2189) {
        methods.camera.setCompass('w');
      }
      if (door.getID() == 2192) {
        methods.camera.setCompass('n');
      }
      clickObject(door, om.Server.toString("examOpen"));
      return random(500, 1000);
    }
    final RSComponent inter = searchInterfacesText(om.Server.toString("examToExit"));
    if (inter != null) {
      if (inter.getText().toLowerCase().contains(om.Server.toString("examRed"))) {
        door = methods.objects.getNearest(2188);
      }
      if (inter.getText().toLowerCase().contains(om.Server.toString("examGreen"))) {
        door = methods.objects.getNearest(2193);
      }
      if (inter.getText().toLowerCase().contains(om.Server.toString("examBlue"))) {
        door = methods.objects.getNearest(2189);
      }
      if (inter.getText().toLowerCase().contains(om.Server.toString("examPurple"))) {
        door = methods.objects.getNearest(2192);
      }
      return random(500, 1000);
    }
    if (!methods.interfaces.get(nextObjectInterface).isValid() && !methods.players.getMyPlayer().isMoving() && !methods.interfaces.get(relatedCardsInterface).isValid()
        && !methods.interfaces.canContinue()
        && door == null) {
      if (methods.calc.distanceTo(mordaut) > 4) {
        methods.walking.getPath(mordaut.getLocation()).traverse();
      }
      if (!methods.calc.tileOnScreen(mordaut.getLocation())) {
        methods.walking.walkTileMM(mordaut.getLocation());
      }
      clickCharacter(mordaut, om.Server.toString("srTalkTo"));
      return random(1500, 1700);
    }
    if (methods.interfaces.get(nextObjectInterface).isValid()) {
      log.info(om.OS.toString("examQuestionType"));
      final NextObjectQuestion noq = new NextObjectQuestion();
      if (noq.getObjects()) {
        if (noq.clickAnswer()) {
          return random(800, 1200);
        }
        noq.guess();
        return random(800, 1200);
      }
      log.info(om.OS.toString("examGuess"));
      noq.guess();
      return random(800, 1200);
    }

    if (methods.interfaces.get(relatedCardsInterface).isValid()) {
      log.info(om.OS.toString("examSimilarObject"));
      int z = 0;
      for (final SimilarObjectQuestion obj : simObjects) {
        if (obj.activateCondition()) {
          z = 1;
          if (obj.clickObjects()) {
            obj.accept();
          }
        }
      }
      if (z == 0) {
        log.severe(om.OS.toString("examNewQuestion"));
        log.severe(om.OS.toString("examPost"));
        log.severe(om.OS.toString("examMissingQuestion"));
        log(methods.interfaces.get(nextObjectInterface).getComponent(25).getText().toLowerCase());
      }
      return random(800, 1200);
    }

    if (methods.interfaces.clickContinue()) {
      return random(800, 3500);
    }

    return random(800, 1200);
  }

  RSComponent searchInterfacesText(final String string) {
    final RSInterface[] inters = methods.interfaces.getAll();
    for (final RSInterface inter : inters) {
      for (final RSComponent interfaceChild : inter.getComponents()) {
        if (interfaceChild.getText().toLowerCase().contains(string.toLowerCase())) {
          return interfaceChild;
        }
      }
    }

    return null;
  }
}