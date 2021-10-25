package venusians.data.board;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import venusians.data.Game;
import venusians.data.board.buildable.Buildable;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.Road;
import venusians.data.board.tiles.MapPreset;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.PortSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.data.lifecycle.GameOptions;

public class Board {

  private static SecureRandom rng = new SecureRandom();
  private static MapSlot[][] slotMap;
  private static HashMap<Integer, ArrayList<TileSlot>> rollValueMap;
  private static HashSet<Buildable> allBuildables = new HashSet<Buildable>();
  private static Building[][] buildingMap;
  private static HashSet<Road>[][] roadMap;
  private static IntPoint robberPosition;

  /** Lists all offsets that are 2 units away */
  public static final IntPoint[] SECOND_ORDER_OFFSETS = new IntPoint[] {
    new IntPoint(1, 2),
    new IntPoint(2, 1),
    new IntPoint(1, -1),
    new IntPoint(-1, -2),
    new IntPoint(-2, -1),
    new IntPoint(-1, 1),
  };

  /** Lists all offsets that are 1 unit away */
  public static final IntPoint[] FIRST_ORDER_OFFSETS = new IntPoint[] {
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
          return ODD_CORNER;
        case 1:
          return EVEN_CORNER;
        default: // case 2:
          return TILE;
      }
    }
  }

  /**
   * Assuming the game's GameOptions has been finalized, it initializes the map
   * contained in the Board object with the specified map setup.
   */
  public static void startGame() {
    GameOptions gameOptions = Game.getGameOptions();

    setUpMaps(gameOptions);
  }

  @SuppressWarnings("unchecked")
  private static void setUpMaps(GameOptions gameOptions) {
    MapPreset mapPreset = MapPreset.CLASSIC;
    TileSlot[] tileSlots = deepCloneSlots(mapPreset.getTileSlots());
    PortSlot[] portSlots = deepCloneSlots(mapPreset.getPortSlots());

    slotMap = new MapSlot[mapPreset.length][mapPreset.width];
    buildingMap = new Building[mapPreset.length][mapPreset.width];
    roadMap = (HashSet<Road>[][]) new HashSet[mapPreset.length][mapPreset.width];
    rollValueMap = new HashMap<>();

    if (gameOptions.areTilePositionsRandomized)
      randomizeTilePositions(tileSlots);

    if (gameOptions.areRollValuesRandomized)
      randomizeRollValues(tileSlots);

    putSlots(tileSlots);
    putSlots(portSlots);

    for (TileSlot tile : tileSlots) {
      if (tile.rollValue == -1)
        continue;

      ArrayList<TileSlot> tileArray = rollValueMap.get(tile.rollValue);
      if (tileArray == null) {
        tileArray = new ArrayList<>();
        rollValueMap.put(tile.rollValue, tileArray);
      }

      tileArray.add(tile);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends MapSlot> T[] deepCloneSlots(T[] slots) {
    T[] newSlots = slots.clone();
    for (int i = 0; i < newSlots.length; i++) {
      newSlots[i] = (T) slots[i].clone();
    }
    return newSlots;
  }

  private static void randomizeTilePositions(TileSlot[] slots) {
    for (int i = 0; i < slots.length; i++) {
      int choice = rng.nextInt(slots.length);
      TileSlot slot1 = slots[i];
      TileSlot slot2 = slots[choice];

      // swap the position on each slot
      IntPoint tempPosition = slot1.position;
      slot1.position = slot2.position;
      slot2.position = tempPosition;
    }
  }

  private static void randomizeRollValues(TileSlot[] slots) {
    for (int i = 0; i < slots.length; i++) {
      int choice = rng.nextInt(slots.length);
      TileSlot slot1 = slots[i];
      TileSlot slot2 = slots[choice];

      if (slot1.rollValue != -1 && slot2.rollValue != -1) {
        int tempRollValue = slot1.rollValue;
        slot1.rollValue = slot2.rollValue;
        slot2.rollValue = tempRollValue;
      }
    }
  }

  private static void putSlots(MapSlot[] slots) {
    for (MapSlot slot : slots) {
      slotMap[slot.position.x][slot.position.y] = slot;
      
    }
  }

  public static MapSlot[][] getMap() {
    return slotMap;
  }

  public static HashSet<Buildable> getBuildables() {
    return allBuildables;
  }

  public static boolean hasBuildable(Buildable buildable) {
    return allBuildables.contains(buildable);
  }

  public static void addBuildable(Buildable buildable) {
    allBuildables.add(buildable);
    if (buildable instanceof Road) {
      Road road = (Road) buildable;

      HashSet<Road> roadSet1 = getRoadsAt(road.getPosition1());
      HashSet<Road> roadSet2 = getRoadsAt(road.getPosition2());

      roadSet1.add(road);
      roadSet2.add(road);
    } else if (buildable instanceof Building) {
      Building building = (Building) buildable;

      IntPoint position = building.getPosition();

      buildingMap[position.x][position.y] = building;
    }
  }

  public static void addBuildables(Collection<Buildable> buildables) {
    for (Buildable buildable : buildables) {
      addBuildable(buildable);
    }
  }

  public static void removeBuildable(Buildable buildable) {
    allBuildables.remove(buildable);
    if (buildable instanceof Road) {
      Road road = (Road) buildable;

      HashSet<Road> roadSet1 = getRoadsAt(road.getPosition1());
      HashSet<Road> roadSet2 = getRoadsAt(road.getPosition2());

      HashSet<Road> intersection = new HashSet<>(roadSet1);
      intersection.retainAll(roadSet2);

      if (intersection.size() > 1) {
        throw new RuntimeException("Two roads overlap the same position pair");
      } else if (intersection.size() < 1) {
        return;
      }
  
      Road selectedRoad = intersection.iterator().next();
      roadSet1.remove(selectedRoad);
      roadSet2.remove(selectedRoad);
    }
  }

  public static void upgradeBuildable(Buildable from, Buildable to) {
    allBuildables.remove(from);
    allBuildables.add(to);
  }

  public static IntPoint getRobberPosition() {
    return robberPosition;
  }

  public static void setRobberPosition(IntPoint newPosition) {
    robberPosition = newPosition;
  }

  public static TileSlot[] getTileSlotsForRollValue(int rollValue) {
    ArrayList<TileSlot> tileList = rollValueMap.get(rollValue);
    TileSlot[] result = new TileSlot[tileList.size()];
    tileList.toArray(result);
    return result;
  }

  public static Building[] getBuildingsForTileSlot(TileSlot slot) {
    IntPoint tilePosition = slot.position;
    ArrayList<Building> buildingList = new ArrayList<Building>();
    for (IntPoint offset : FIRST_ORDER_OFFSETS) {
      IntPoint buildingPosition = tilePosition.plus(offset);
      Building building = buildingMap[buildingPosition.x][buildingPosition.y];
      if (building != null) {
        buildingList.add(building);
      }
    }

    return buildingList.toArray(new Building[buildingList.size()]);
  }

  public static Building getBuildingAt(IntPoint position) {
    return buildingMap[position.x][position.y];
  }

  public static HashSet<Road> getRoadsAt(IntPoint position) {
    HashSet<Road> result = roadMap[position.x][position.y];
    if (result == null) {
      result = new HashSet<Road>();
      roadMap[position.x][position.y] = result;
    }

    return result;
  }
}
