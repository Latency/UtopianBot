package org.rsbot.script.randoms;

import java.awt.Point;
import java.util.ArrayList;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Objects;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

/**
 * Updated by Iscream Apr 07,11
 */
@ScriptManifest(authors = { "Zenzie", "Iscream" }, name = "Maze", version = 1.6)
public class Maze extends Random {
  public Maze(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om         = methods.locale;

  ArrayList<Door>             paths      = new ArrayList<Door>();

  String                      usePath    = "None";

  public char                 doorDir    = 'a';
  public RSTile               walkToTile = null;
  public int                  doorIndex  = 0;

  public int                  tryCount   = 0;
  public long                 lastTry    = 0;
  public RSTile               lastDoor   = null;

  // working as of 4/07/2011
  public void loadGreenPath() {

    paths.add(new Door(new RSTile(2903, 4555), 's', 0));
    paths.add(new Door(new RSTile(2890, 4566), 'e', 1));
    paths.add(new Door(new RSTile(2892, 4578), 'e', 2));
    paths.add(new Door(new RSTile(2894, 4567), 'e', 3));
    paths.add(new Door(new RSTile(2896, 4562), 'e', 4));
    paths.add(new Door(new RSTile(2905, 4561), 's', 5));
    paths.add(new Door(new RSTile(2915, 4560), 'n', 6));
    paths.add(new Door(new RSTile(2909, 4562), 'n', 7));
    paths.add(new Door(new RSTile(2924, 4566), 'w', 8));
    paths.add(new Door(new RSTile(2922, 4575), 'w', 9));
    paths.add(new Door(new RSTile(2916, 4568), 'n', 10));
    paths.add(new Door(new RSTile(2905, 4573), 'w', 11));
    paths.add(new Door(new RSTile(2906, 4585), 'n', 12));
    paths.add(new Door(new RSTile(2916, 4586), 's', 13));
    paths.add(new Door(new RSTile(2920, 4582), 'w', 14));
    paths.add(new Door(new RSTile(2910, 4582), 's', 15));
    paths.add(new Door(new RSTile(2910, 4572), 'n', 16));
    paths.add(new Door(new RSTile(2910, 4576), 'e', 17));
    log(om.OS.toString("mazeGreen"));
  }

  // working as of 1/31/2010
  public void loadBluePath() {

    paths.add(new Door(new RSTile(2891, 4588), 'w', 0));
    paths.add(new Door(new RSTile(2889, 4596), 'w', 1));
    paths.add(new Door(new RSTile(2893, 4600), 's', 2));
    paths.add(new Door(new RSTile(2901, 4598), 's', 3));
    paths.add(new Door(new RSTile(2897, 4596), 's', 4));
    paths.add(new Door(new RSTile(2894, 4587), 'e', 5));
    paths.add(new Door(new RSTile(2896, 4582), 'e', 6));
    paths.add(new Door(new RSTile(2898, 4570), 'e', 7));
    paths.add(new Door(new RSTile(2900, 4567), 'e', 8));
    paths.add(new Door(new RSTile(2911, 4566), 'n', 9));
    paths.add(new Door(new RSTile(2906, 4585), 'n', 10));
    paths.add(new Door(new RSTile(2916, 4586), 's', 11));
    paths.add(new Door(new RSTile(2920, 4582), 'w', 12));
    paths.add(new Door(new RSTile(2910, 4582), 's', 13));
    paths.add(new Door(new RSTile(2910, 4572), 'n', 14));
    paths.add(new Door(new RSTile(2910, 4576), 'e', 15));
    log(om.OS.toString("mazeBlue"));
  }

  // working as of 4/07/2011
  public void loadCyanPath() {

    paths.add(new Door(new RSTile(2930, 4555), 's', 0));
    paths.add(new Door(new RSTile(2912, 4553), 's', 1));
    paths.add(new Door(new RSTile(2936, 4556), 'w', 2));
    paths.add(new Door(new RSTile(2934, 4568), 'w', 3));
    paths.add(new Door(new RSTile(2932, 4575), 'w', 4));
    paths.add(new Door(new RSTile(2930, 4561), 'w', 5));
    paths.add(new Door(new RSTile(2929, 4581), 'e', 6));
    paths.add(new Door(new RSTile(2930, 4590), 'w', 7));
    paths.add(new Door(new RSTile(2924, 4592), 's', 8));
    paths.add(new Door(new RSTile(2926, 4575), 'w', 9));
    paths.add(new Door(new RSTile(2924, 4583), 'w', 10));
    paths.add(new Door(new RSTile(2916, 4586), 's', 11));
    paths.add(new Door(new RSTile(2920, 4582), 'w', 12));
    paths.add(new Door(new RSTile(2910, 4582), 's', 13));
    paths.add(new Door(new RSTile(2910, 4572), 'n', 14));
    paths.add(new Door(new RSTile(2910, 4576), 'e', 15));
    log(om.OS.toString("mazeCyan"));
  }

  // working as of 2/05/2010
  public void loadPurplePath() {

    paths.add(new Door(new RSTile(2932, 4597), 'n', 0));
    paths.add(new Door(new RSTile(2921, 4599), 'n', 1));
    paths.add(new Door(new RSTile(2909, 4600), 's', 3));
    paths.add(new Door(new RSTile(2913, 4598), 's', 4));
    paths.add(new Door(new RSTile(2908, 4596), 's', 5));
    paths.add(new Door(new RSTile(2919, 4594), 's', 6));
    paths.add(new Door(new RSTile(2908, 4592), 's', 7));
    paths.add(new Door(new RSTile(2898, 4585), 'e', 8));
    paths.add(new Door(new RSTile(2903, 4588), 's', 9));
    paths.add(new Door(new RSTile(2902, 4575), 'e', 10));
    paths.add(new Door(new RSTile(2906, 4585), 'n', 11));
    paths.add(new Door(new RSTile(2916, 4586), 's', 12));
    paths.add(new Door(new RSTile(2920, 4582), 'w', 13));
    paths.add(new Door(new RSTile(2910, 4582), 's', 14));
    paths.add(new Door(new RSTile(2910, 4572), 'n', 15));
    paths.add(new Door(new RSTile(2910, 4576), 'e', 16));
    log(om.OS.toString("mazePurple"));
  }

  public class Door {

    public final RSTile doorTile;
    public final char   doorDir;
    public int          doorID;

    public Door(final RSTile doorTile, final char doorDir, final int doorID) {
      this.doorTile = doorTile;
      this.doorDir = doorDir;
      this.doorID = doorID;
    }
  }

  @Override
  public boolean activateCondition() {
    if (methods.game.isLoggedIn() && methods.objects.getNearest(3626, 3649) != null) {
      methods.camera.setPitch(true);
      return true;
    }
    return false;
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      return -1;
    }

    if (methods.players.getMyPlayer().isMoving()) {
      return random(150, 200);
    }

    if (methods.players.getMyPlayer().getLocation().equals(new RSTile(2911, 4576))) {
      if (methods.players.getMyPlayer().getAnimation() == -1) {
        methods.tiles.interact(new RSTile(2912, 4576), om.Server.toString("mazeTouch"));
        return random(5000, 6000);
      }
    }
    if (usePath.equals(om.Server.toString("mazeNone"))) {
      getPath();
      return random(900, 1100);
    }

    if (methods.players.getMyPlayer().getLocation().equals(tileAfterDoor())) {
      doorIndex += 1;
      log(om.OS.toString("mazeNewWall"));
      getNewWall();
      return random(200, 300);
    }

    if (walkToTile != null && methods.calc.distanceTo(walkToTile) >= 3) {
      if (!methods.walking.getPath(walkToTile).traverse()) {
        methods.walking.walkTileMM(walkToTile.randomize(2, 2));
      }

      return random(500, 600);
    }

    if (walkToTile != null && methods.calc.distanceTo(walkToTile) <= 3) {
      if (doorDir != 'a' && !methods.players.getMyPlayer().isMoving()) {
        if (methods.camera.getAngle() - turnCameraTo() < 30 || methods.camera.getAngle() - turnCameraTo() > 30) {
          methods.camera.setAngle(turnCameraTo());
        }
        final RSObject obj = methods.objects.getTopAt(walkToTile, Objects.TYPE_BOUNDARY);
        if (obj != null && obj.interact(om.Server.toString("examOpen")) || atDoor(walkToTile, doorDir)) {
          return random(2750, 3250);
        }
      }
    }

    return random(300, 350);
  }

  public void getNewWall() {
    for (final Door door : paths) {
      if (door.doorID == doorIndex) {
        walkToTile = new RSTile(door.doorTile.getX(), door.doorTile.getY());
        doorDir = door.doorDir;
        door.doorID = doorIndex;
        log(om.OS.toString("walking.walkTo") + ": " + walkToTile.getX() + ", " + walkToTile.getY());
        log(om.OS.toString("mazeDoorIndex") + ": " + doorIndex + " | " + om.OS.toString("mazeDoorDirection") + ": " + doorDir);
      }
    }
  }

  public int turnCameraTo() {
    final int doorD = doorDir;
    if (doorD == 'a') {
      // log("TURNCAMERATO: WALL DIRECTION IS 'A");
      return random(330, 380);
    }
    switch (doorD) {
      case 'n':
        return random(330, 380);
      case 's':
        return random(155, 190);
      case 'e':
        return random(245, 290);
      case 'w':
        return random(65, 110);
    }
    return random(330, 380);
  }

  public RSTile tileAfterDoor() {
    final int doorD = doorDir;
    if (doorD == 'a') {
      // log("TILEAFTERDOOR: doorD = A");
      return new RSTile(1, 1);
    }
    if (walkToTile == null) {
      // log("TILEAFTERDOOR: walkToTile = NULL");
      return new RSTile(1, 1);
    }
    switch (doorD) {
      case 'n':
        return new RSTile(walkToTile.getX(), walkToTile.getY() + 1);
      case 'w':
        return new RSTile(walkToTile.getX() - 1, walkToTile.getY());
      case 'e':
        return new RSTile(walkToTile.getX() + 1, walkToTile.getY());
      case 's':
        return new RSTile(walkToTile.getX(), walkToTile.getY() - 1);
    }
    return new RSTile(1, 1);
  }

  public void getPath() {
    final int x = methods.players.getMyPlayer().getLocation().getX();
    final int y = methods.players.getMyPlayer().getLocation().getY();
    if (x >= 2920 && x <= 2940 && y >= 4572 && y <= 4600) {
      loadPurplePath();
      usePath = "purple";
      walkToTile = new RSTile(2932, 4597);
      doorDir = 'n';
      doorIndex = 0;
      log(om.OS.toString("mazeUsingPurple"));
    }
    if (x >= 2891 && x <= 2894 && y >= 4586 && y <= 4599) {
      loadBluePath();
      usePath = "blue";
      walkToTile = new RSTile(2891, 4588);
      doorDir = 'w';
      doorIndex = 0;
      log(om.OS.toString("mazeUsingBlue"));
    }
    if (x >= 2915 && x <= 2933 && y >= 4555 && y <= 4560) {
      loadCyanPath();
      usePath = "cyan";
      walkToTile = new RSTile(2930, 4555);
      doorDir = 's';
      doorIndex = 0;
      log(om.OS.toString("mazeUsingCyan"));
    }
    if (x >= 2891 && x <= 2914 && y >= 4555 && y <= 4561) {
      loadGreenPath();
      usePath = "green";
      walkToTile = new RSTile(2903, 4555);
      doorDir = 's';
      doorIndex = 0;
      log(om.OS.toString("mazeUsingGreen"));
    }
  }

  @Override
  public void onFinish() {
    paths = new ArrayList<Door>();
    usePath = "None";
    doorDir = 'a';
    walkToTile = null;
    doorIndex = 0;
    tryCount = 0;
    lastTry = 0;
    lastDoor = null;
    log(om.OS.toString("mazeFinished"));
  }

  public boolean atDoor(final RSTile location, final char direction) {
    if (location == null) {
      return false;
    }
    int x = location.getX(), y = location.getY();
    boolean fail = false;
    switch (direction) {
      case 'N':
      case 'n':
        y++;
        break;
      case 'W':
      case 'w':
        x--;
        break;
      case 'E':
      case 'e':
        x++;
        break;
      case 'S':
      case 's':
        y--;
        break;
      default:
        fail = true;
    }
    if (fail) {
      throw new IllegalArgumentException();
    }
    return atDoorTiles(location, new RSTile(x, y));
  }

  public boolean atDoorTiles(final RSTile a, final RSTile b) {
    if (a != lastDoor) {
      lastTry = 0;
      tryCount = 0;
      lastDoor = a;
    }
    tryCount++;
    if (System.currentTimeMillis() - lastTry > random(20000, 40000)) {
      tryCount = 1;
    }
    lastTry = System.currentTimeMillis();
    if (tryCount > 4) {
      if (random(0, 10) < random(2, 4)) {
        methods.camera.setAngle(
            methods.camera.getAngle() + (random(0, 9) < random(6, 8) ? random(-20, 20) : random(-360, 360)));
      }
      if (random(0, 14) < random(0, 2)) {
        methods.camera.setPitch(random(0, 100));
      }
    }
    if (tryCount > 100) {
      log(om.OS.toString("mazeProblems"));
      stopScript(false);
    }
    if (!methods.calc.tileOnScreen(a) || !methods.calc.tileOnScreen(b) || methods.calc.distanceTo(a) > random(4, 7)) {
      if (methods.calc.tileOnMap(a)) {
        methods.walking.getPath(a.randomize(3, 3)).traverse();
        sleep(random(750, 1250));
      } else {
        log(om.OS.toString("mazeCannot"));
        return false;
      }
    } else {
      final ArrayList<RSTile> theObjs = new ArrayList<RSTile>();
      theObjs.add(a);
      theObjs.add(b);
      try {
        final Point[] thePoints = new Point[theObjs.size()];
        for (int c = 0; c < theObjs.size(); c++) {
          thePoints[c] = methods.calc.tileToScreen(theObjs.get(c));
        }
        float xTotal = 0;
        float yTotal = 0;
        for (final Point thePoint : thePoints) {
          xTotal += thePoint.getX();
          yTotal += thePoint.getY();
        }
        final Point location = new Point((int) (xTotal / thePoints.length),
            (int) (yTotal / thePoints.length) - random(0, 40));
        if (location.x == -1 || location.y == -1) {
          return false;
        }
        if (Math.sqrt(Math.pow((methods.mouse.getLocation().getX() - location.getX()), 2) + Math.pow(
            (methods.mouse.getLocation().getY() - location.getY()), 2)) < random(20, 30)) {
          for (final String command : methods.menu.getItems()) {
            if (command.contains(om.Server.toString("examOpen"))) {
              if (methods.menu.doAction(om.Server.toString("examOpen"))) {
                lastTry = 0;
                tryCount = 0;
                return true;
              }
            }
          }
        }
        methods.mouse.move(location, 7, 7);
        if (methods.menu.doAction(om.Server.toString("examOpen"))) {
          lastTry = 0;
          tryCount = 0;
          return true;
        }
      } catch (final Exception e) {
        return false;
      }
    }
    return false;
  }
}