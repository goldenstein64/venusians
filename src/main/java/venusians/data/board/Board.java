package venusians.data.board;

import java.security.SecureRandom;
import java.util.HashSet;
import venusians.data.Game;
import venusians.data.IntPoint;
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
  public static final IntPoint[] secondOrderOffsets = new IntPoint[] {
    new IntPoint(1, 2),
    new IntPoint(2, 1),
    new IntPoint(1, -1),
    new IntPoint(-1, -2),
    new IntPoint(-2, -1),
    new IntPoint(-1, 1),
  };

  /** Lists all offsets that are 1 unit away */
  public static final IntPoint[] firstOrderOffsets = new IntPoint[] {
    new IntPoint(1, 1),
    new IntPoint(1, 0),
    new IntPoint(0, -1),
    new IntPoint(-1, -1),
    new IntPoint(-1, 0),
    new IntPoint(0, 1),
  };

  /** Describes what type of formation is found at a certain point */
  public static enum PositionType {
    TILE,
    EVEN_CORNER,
    ODD_CORNER;

    public static PositionType valueOf(IntPoint position) {
      switch ((position.x - position.y) % 3) {
        case 0:
          return TILE;
        case 1:
          return ODD_CORNER;
        default: // case 2:
          return EVEN_CORNER;
      }
    }
  }

  public static Event<Buildable> onBuildableAdded = new Event<Buildable>();

  public static Event<BuildableUpgradedArgs> onBuildableUpgraded = new Event<BuildableUpgradedArgs>();

  public static class BuildableUpgradedArgs {

    public final Buildable from;
    public final Buildable to;

    public BuildableUpgradedArgs(Buildable from, Buildable to) {
      this.from = from;
      this.to = to;
    }
  }

  /**
   * Assuming the game's GameOptions has been finalized, it initializes the map
   * contained in the Board object with the specified map setup.
   */
  public static void startGame() {
    GameOptions gameOptions = Game.getGameOptions();

    setUpMap(gameOptions);
  }

  private static void setUpMap(GameOptions gameOptions) {
    MapPreset mapPreset = MapPreset.CLASSIC;
    MapSlot[] slots = deepCloneSlots(mapPreset.getSlots());

    map = new MapSlot[13][13];

    if (gameOptions.areTilePositionsRandomized) randomizeTilePositions(slots);

    if (gameOptions.areRollValuesRandomized) randomizeRollValues(slots);

    setMapToSlots(slots);
  }

  private static MapSlot[] deepCloneSlots(MapSlot[] slots) {
    MapSlot[] newSlots = slots.clone();
    for (int i = 0; i < newSlots.length; i++) {
      slots[i] = slots[i].clone();
    }
    return newSlots;
  }

  private static void randomizeTilePositions(MapSlot[] slots) {
    for (int i = 0; i < slots.length; i++) {
      int choice = rng.nextInt(slots.length);
      MapSlot slot1 = slots[i];
      MapSlot slot2 = slots[choice];

      // swap the position on each slot
      IntPoint tempPosition = slot1.position;
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
    onBuildableAdded.fire(buildable);
  }

  public static void upgradeBuildable(Buildable from, Buildable to) {
    allBuildables.remove(from);
    allBuildables.add(to);
    onBuildableUpgraded.fire(new BuildableUpgradedArgs(from, to));
  }

  public static Point getRobberPosition() {
    return robberPosition;
  }
}
