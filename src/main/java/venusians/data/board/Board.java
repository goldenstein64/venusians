package venusians.data.board;

import java.security.SecureRandom;
import java.util.HashSet;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.buildable.Buildable;
import venusians.data.board.tiles.MapPreset;
import venusians.data.board.tiles.MapSlot;
import venusians.data.lifecycle.GameOptions;
import venusians.util.Event;

public class Board {

  private static SecureRandom rng = new SecureRandom();
  private static MapSlot[][] map;
  private static HashSet<Buildable> allBuildables = new HashSet<Buildable>();
  private static Point robberPosition;

  /** Lists all offsets that are 2 units away */
  public static final Point[] secondOrderOffsets = new Point[] {
    new Point(1, 2),
    new Point(2, 1),
    new Point(1, -1),
    new Point(-1, -2),
    new Point(-2, -1),
    new Point(-1, 1),
  };

  /** Lists all offsets that are 1 unit away */
  public static final Point[] firstOrderOffsets = new Point[] {
    new Point(1, 1),
    new Point(1, 0),
    new Point(0, -1),
    new Point(-1, -1),
    new Point(-1, 0),
    new Point(0, 1),
  };

  public static enum PositionType {
    TILE,
    EVEN_CORNER,
    ODD_CORNER;

    public static PositionType valueOf(Point position) {
      switch ((position.x - position.y) % 3) {
        case 0:
          return TILE;
        case 1:
          return ODD_CORNER;
        default:
          return EVEN_CORNER;
      }
    }
  }

  public static Event<MapChangedArgs> mapChanged = new Event<MapChangedArgs>();

  public static class MapChangedArgs {

    public static enum ActionType {
      ADDED,
      UPGRADED,
    }

    public Buildable from;
    public Buildable to;
    public ActionType actionType;

    public MapChangedArgs(Buildable buildable) {
      this.to = buildable;
      this.actionType = ActionType.ADDED;
    }

    public MapChangedArgs(Buildable from, Buildable to) {
      this.from = from;
      this.to = to;
      this.actionType = ActionType.UPGRADED;
    }
  }

  public static void startGame() {
    GameOptions gameOptions = Game.getGameOptions();

    setUpMap(gameOptions);
  }

  private static void setUpMap(GameOptions gameOptions) {
    MapPreset mapPreset = MapPreset.CLASSIC;
    MapSlot[] slots = deepCloneSlots(mapPreset.getSlots());

    map = new MapSlot[13][13];

    if (gameOptions.randomizeTilePositions) {
      randomizeSlotPositions(slots);
    }

    if (gameOptions.randomizeRollValues) {
      randomizeRollValues(slots);
    }

    setMapToSlots(slots);
  }

  private static MapSlot[] deepCloneSlots(MapSlot[] slots) {
    MapSlot[] newSlots = slots.clone();
    for (int i = 0; i < newSlots.length; i++) {
      slots[i] = slots[i].clone();
    }
    return newSlots;
  }

  private static void randomizeSlotPositions(MapSlot[] slots) {
    for (int i = 0; i < slots.length; i++) {
      int choice = rng.nextInt(slots.length);
      MapSlot slot1 = slots[i];
      MapSlot slot2 = slots[choice];

      // swap the position on each slot
      Point tempPosition = slot1.position;
      slot1.position = slot2.position;
      slot2.position = tempPosition;
    }
  }

  private static void randomizeRollValues(MapSlot[] slots) {
    for (int i = 0; i < slots.length; i++) {
      int choice = rng.nextInt(slots.length);
      MapSlot slot1 = slots[i];
      MapSlot slot2 = slots[choice];

      if (slot1.rollValue != -1 && slot2.rollValue != -1) {
        int tempRollValue = slot1.rollValue;
        slot1.rollValue = slot2.rollValue;
        slot2.rollValue = tempRollValue;
      }
    }
  }

  private static void setMapToSlots(MapSlot[] slots) {
    for (MapSlot slot : slots) {
      map[slot.position.x][slot.position.y] = slot;
    }
  }

  public static MapSlot[][] getMap() {
    return map;
  }

  public static HashSet<Buildable> getBuildables() {
    return allBuildables;
  }

  public static boolean hasBuildable(Buildable buildable) {
    return allBuildables.contains(buildable);
  }

  public static void addBuildable(Buildable buildable) {
    allBuildables.add(buildable);
    mapChanged.fire(new MapChangedArgs(buildable));
  }

  public static void upgradeBuildable(Buildable from, Buildable to) {
    allBuildables.remove(from);
    allBuildables.add(to);
    mapChanged.fire(new MapChangedArgs(from, to));
  }

  public static Point getRobberPosition() {
    return robberPosition;
  }
}