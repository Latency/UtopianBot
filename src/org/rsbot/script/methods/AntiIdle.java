package org.rsbot.script.methods;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Vector;

import org.rsbot.script.internal.InputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

public class AntiIdle extends MethodProvider {

  public static final char   CAMERA_UP    = KeyEvent.VK_UP;
  public static final char   CAMERA_DOWN  = KeyEvent.VK_DOWN;
  public static final char   CAMERA_LEFT  = KeyEvent.VK_LEFT;
  public static final char   CAMERA_RIGHT = KeyEvent.VK_RIGHT;

  public final CameraHandler cameraHandler;
  public final MouseHandler  mouseHandler;

  public AntiIdle(final MethodContext ctx) {
    super(ctx);
    cameraHandler = new CameraHandler();
    mouseHandler = new MouseHandler(methods.inputManager);
  }

  public RSObject getRandomRSObject(final int range) {
    try {
      RSObject[] allPossibleObjects = methods.objects.getAll(new Filter<RSObject>() {
        @Override
        public boolean accept(final RSObject o) {
          if (o != null && methods.calc.distanceTo(o) <= range) {
            RSObject.Type t = o.getType();
            return t.equals(RSObject.Type.INTERACTABLE);
          }
          return false;
        }
      });
      return allPossibleObjects[rand(0, allPossibleObjects.length)];
    } catch (final Exception ignored) {
    }
    return null;
  }

  public RSNPC getRandomNPC(final int range) {
    try {
      RSNPC[] allPossibleNPCs = methods.npcs.getAll(new Filter<RSNPC>() {
        @Override
        public boolean accept(final RSNPC n) {
          return n != null && methods.calc.distanceTo(n) <= range;
        }
      });
      return allPossibleNPCs[rand(0, allPossibleNPCs.length)];
    } catch (final Exception ignored) {
    }
    return null;
  }

  private int rand(int min, int max) {
    return random(min, max);
  }

  public class CameraHandler {

    public void moveLeft(final long timeout) {
      new Thread(new Charpresser(timeout, CAMERA_LEFT)).start();
    }

    public void moveRight(final long timeout) {
      new Thread(new Charpresser(timeout, CAMERA_RIGHT)).start();
    }

    public void moveUp(final long timeout) {
      new Thread(new Charpresser(timeout, CAMERA_UP)).start();
    }

    public void moveDown(final long timeout) {
      new Thread(new Charpresser(timeout, CAMERA_DOWN)).start();
    }

    public void pitchRandomly(final long averageTimeout, final boolean multiDir) {
      try {
        long sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
        final int r = rand(1, 3);
        switch (r) {
          case 1: {
            moveUp(sleep);
            break;
          }
          case 2: {
            moveDown(sleep);
            break;
          }
          default: {
            moveUp(sleep);
          }
        }
        if (multiDir) {
          sleep(Math.abs(rand(5, (int) sleep / 2) + rand((int) -(sleep / 4), (int) (sleep / 4))));
          sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
          final int r2 = rand(1, 3);
          switch (r2) {
            case 1: {
              moveUp(sleep);
              break;
            }
            case 2: {
              moveDown(sleep);
              break;
            }
            default: {
              moveUp(sleep);
            }
          }
        }
      } catch (final Exception ignored) {
      }
    }

    public void turnRandomly(final long averageTimeout, final boolean multiDir) {
      try {
        long sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
        final int r = rand(1, 3);
        switch (r) {
          case 1: {
            moveLeft(sleep);
            break;
          }
          case 2: {
            moveRight(sleep);
            break;
          }
          default: {
            moveUp(sleep);
            break;
          }
        }
        if (multiDir) {
          sleep(Math.abs(rand(5, (int) sleep / 2) + rand((int) -(sleep / 4), (int) (sleep / 4))));
          sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
          final int r2 = random(1, 3);
          switch (r2) {
            case 1: {
              moveLeft(sleep);
              break;
            }
            case 2: {
              moveRight(sleep);
              break;
            }
            default: {
              moveUp(sleep);
              break;
            }
          }
        }
      } catch (final Exception ignored) {
      }
    }

    public void moveRandomly(final long averageTimeout, final boolean multiDir) {
      try {
        long sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
        final int r = rand(1, 5);
        switch (r) {
          case 1: {
            moveLeft(sleep);
            break;
          }
          case 2: {
            moveRight(sleep);
            break;
          }
          case 3: {
            moveUp(sleep);
            break;
          }
          case 4: {
            moveDown(sleep);
            break;
          }
          default: {
            moveUp(sleep);
            break;
          }
        }
        if (multiDir) {
          sleep(Math.abs(rand(5, (int) sleep / 2) + rand((int) -(sleep / 4), (int) (sleep / 4))));
          sleep = Math.abs((long) (random(averageTimeout - 200, averageTimeout + 200)));
          final int r2 = rand(1, 5);
          switch (r2) {
            case 1: {
              moveLeft(sleep);
              break;
            }
            case 2: {
              moveRight(sleep);
              break;
            }
            case 3: {
              moveUp(sleep);
              break;
            }
            case 4: {
              moveDown(sleep);
              break;
            }
            default: {
              moveUp(sleep);
              break;
            }
          }
        }
      } catch (final Exception ignored) {
      }
    }

    public void lookAtRandomThing() {
      lookAtRandomThing(10);
    }

    public void lookAtRandomThing(final int range) {
      try {
        RSObject object = getRandomRSObject(range);
        RSNPC npc = getRandomNPC(range);
        boolean useObject = object != null;
        if ((!useObject && npc != null) || (npc != null && methods.calc.distanceTo(npc) < methods.calc.distanceTo(object) && rand(0, 5) >= 1)) {
          useObject = false;
        }
        RSTile loc = null;
        if (useObject) {
          loc = object.getLocation();
        } else if (npc != null) {
          loc = npc.getLocation();
        }
        if (loc != null) {
          turnTowards(loc);
          if (rand(0, 2) == 0) {
            sleep(rand(1, 200));
            final int r = rand(0, 3);
            if (r == 0) {
              moveUp(rand(300, 1500));
            } else if (r == 1) {
              moveDown(rand(300, 1500));
            }
          }
        }
      } catch (final Exception ignored) {
      }
    }

    public void turnTowards(final RSTile tile) {
      setAngle(methods.camera.getTileAngle(tile));
    }

    public void setAngle(final int angle) {
      Charpresser presser = null;
      Thread pthread = null;
      try {
        if (methods.camera.getAngleTo(angle) > 5) {
          presser = new Charpresser(CAMERA_LEFT);
          pthread = new Thread(presser);
          pthread.start();
          while (methods.camera.getAngleTo(angle) > 5) {
            sleep(10);
          }
          presser.stopThread();
        } else if (methods.camera.getAngleTo(angle) <= -5) {
          presser = new Charpresser(CAMERA_RIGHT);
          pthread = new Thread(presser);
          pthread.start();
          while (methods.camera.getAngleTo(angle) <= -5) {
            sleep(10);
          }
          presser.stopThread();
        }
      } catch (final Exception ignored) {
      } finally {
        if (presser != null && !presser.stop) {
          presser.stopThread();
        }
        if (pthread != null && pthread.isAlive()) {
          pthread.interrupt();
        }
      }
    }

    private class Charpresser implements Runnable {

      private final char      topress;
      private long            tohold = -1;
      public volatile boolean stop   = false;

      public Charpresser(char press) {
        topress = press;
      }

      public Charpresser(long timeout, char press) {
        topress = press;
        tohold = timeout;
      }

      @Override
      public void run() {
        try {
          Thread.sleep(rand(0, 71));
          if (tohold != -1) {
            methods.keyboard.pressKey(topress);
            Thread.sleep(tohold);
            methods.keyboard.releaseKey(topress);
          } else {
            methods.keyboard.pressKey(topress);
            while (!stop) {
              Thread.sleep(25);
            }
            methods.keyboard.releaseKey(topress);
          }
        } catch (InterruptedException ignored) {
        }
      }

      public void stopThread() {
        stop = true;
      }
    }
  }

  public class MouseHandler {

    private boolean                                      generateSplines = true;
    private final org.rsbot.script.internal.MouseHandler mh;

    public MouseHandler(final InputManager im) {
      mh = new org.rsbot.script.internal.MouseHandler(im);
    }

    public void wiggleMouse() {
      final Point loc = methods.mouse.getLocation();
      int x = random(0, methods.game.getWidth());
      int y = random(0, methods.game.getHeight());
      moveMouse(new Point(x, y));
      int randm = random(1, 3);
      sleep(random(38, 642));
      if (randm == 1) {
        x = Math.abs(x - random(75, 175));
        int random = random(1, 3);
        if (random == 1) {
          y = Math.abs(y - random(75, 175));
        } else {
          y = Math.abs(y + random(75, 175));
        }
      } else {
        x = Math.abs(x + random(75, 175));
        int random = random(1, 3);
        if (random == 3) {
          y = Math.abs(y + random(75, 175));
        } else {
          y = Math.abs(y - random(75, 175));
        }
      }
      moveMouse(new Point(x, y));
      sleep(random(0, 320));
      x = loc.x;
      y = loc.y;
      int rand2 = random(1, 3);
      if (rand2 == 1) {
        x = Math.abs(x - random(50, 200));
        int random = random(1, 3);
        if (random == 1) {
          y = Math.abs(y - random(50, 200));
        } else {
          y = Math.abs(y + random(50, 200));
        }
      } else {
        x = Math.abs(x + random(50, 200));
        int random = random(1, 3);
        if (random == 1) {
          y = Math.abs(y + random(50, 200));
        } else {
          y = Math.abs(y - random(50, 200));
        }
      }
      moveMouse(new Point(x, y));
      sleep(random(0, 200));
    }

    public void moveMouse(final Point destination) {
      final int r = random(0, 16);
      if (r < 6 && generateSplines) {
        moveMouseAlternateSpline(destination);
      } else {
        moveMouseNormally(destination);
      }
    }

    public void moveMouseNormally(final Point destination) {
      mh.moveMouse(methods.mouse.getSpeed(), methods.mouse.getLocation().x, methods.mouse.getLocation().y, destination.x, destination.y, 0, 0);
    }

    public void moveMouseAlternateSpline(final Point destination) {
      mh.moveMouse(methods.mouse.getSpeed(), methods.mouse.getLocation().x, methods.mouse.getLocation().y, destination.x, destination.y, generateSplineTo(destination));
    }

    public void setGenerateSplines(boolean gen) {
      generateSplines = gen;
    }

    public Point[] generateSplineTo(final Point destination) {
      try {
        Point[] spline = genBezier(getControls(destination));
        return spline;
      } catch (final Exception ignored) {
      }
      return null;
    }

    private Point[] genBezier(final Point[] coordlist) {
      if (coordlist.length != 3) {
        return null;
      }
      int x1, x2, y1, y2;
      x1 = coordlist[0].x;
      y1 = coordlist[0].y;
      Vector<Point> points = new Vector<Point>();
      points.add(new Point(x1, y1));
      for (double t = 0; t <= 1; t += 0.01) {
        x2 = (int) ((int) (coordlist[0].x + t * (-coordlist[0].x * 3 + t * (3 * coordlist[0].x - coordlist[0].x * t))) + t
            * (3 * coordlist[1].x + t * (-6 * coordlist[1].x + coordlist[1].x * 3 * t)) + t * t * (coordlist[2].x * 3 - coordlist[2].x * 3 * t) + coordlist[3].x * t * t * t);
        y2 = (int) ((int) (coordlist[0].y + t * (-coordlist[0].y * 3 + t * (3 * coordlist[0].y - coordlist[0].y * t))) + t
            * (3 * coordlist[1].y + t * (-6 * coordlist[1].y + coordlist[1].y * 3 * t)) + t * t * (coordlist[2].y * 3 - coordlist[2].y * 3 * t) + coordlist[3].y * t * t * t);
        points.add(new Point(x2, y2));
      }
      if (!points.get(points.size() - 1).equals(new Point(coordlist[3].x, coordlist[3].y))) {
        points.add(new Point(coordlist[3].x, coordlist[3].y));
      }
      org.rsbot.script.internal.MouseHandler.adaptiveMidpoints(points);
      return points.toArray(new Point[points.size()]);
    }

    private Point[] getControls(final Point destination) {
      Point[] points = new Point[3];
      points[0] = methods.mouse.getLocation();
      points[1] = new Point(random(0, methods.game.getWidth()), random(0, methods.game.getHeight()));
      points[2] = destination;
      return points;
    }

  }
}
