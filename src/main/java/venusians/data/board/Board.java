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
import venusians.data.cards.special.LongestRoadCard;
import venusians.data.lifecycle.GameOptions;
import venusians.data.players.Player;
import venusians.util.Matrix;

public final class Board {

  private Board() {}

  private static SecureRandom rng = new SecureRandom();
  private static Matrix<MapSlot> slotMap;
  private static HashMap<Integer, ArrayList<TileSlot>> rollValueMap;
  private static HashSet<Building> allBuildings = new HashSet<>();
  private static HashSet<Road> allRoads = new HashSet<>();
  private static Matrix<Building> buildingMap;
  private static Matrix<HashSet<Road>> roadMap;
  private static IntPoint robberPosition;
  private static IntPoint mapSize;

  /** Lists all offsets that are 1 unit away in normal space */
  public static final IntPoint[] FIRST_ORDER_OFFSETS = new IntPoint[] {
    new IntPoint(1, -1),
    new IntPoint(1, 0),
    new IntPoint(0, 1),
    new IntPoint(-1, 1),
    new IntPoint(-1, 0),
    new IntPoint(0, -1),
  };

  /** Describes what type of formation is found at a certain point */
  public static enum PositionType {
    TILE,
    EVEN_CORNER,
    ODD_CORNER;

    public static PositionType valueOf(IntPoint position) {
      switch ((position.x - position.y) % 3) {
        case 0: // case 0:
          return TILE;
        case -2: // case 1:
        case 1:
          return ODD_CORNER;
        case -1:
        case 2: // case 2:
          return EVEN_CORNER;
        default:
          throw new RuntimeException("Mod 3 gave a number out of range");
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

  private static void setUpMaps(GameOptions gameOptions) {
    MapPreset mapPreset = MapPreset.CLASSIC;
    TileSlot[] tileSlots = deepCloneSlots(mapPreset.getTileSlots());
    PortSlot[] portSlots = deepCloneSlots(mapPreset.getPortSlots());

    mapSize = mapPreset.size;
    slotMap = new Matrix<MapSlot>(mapSize);
    buildingMap = new Matrix<Building>(mapSize);
    roadMap = new Matrix<HashSet<Road>>(mapSize);
    rollValueMap = new HashMap<>();
    setRobberPosition(mapPreset.robberPosition);

    if (gameOptions.areTilePositionsRandomized) randomizeTilePositions(
      tileSlots
    );

    if (gameOptions.areRollValuesRandomized) randomizeRollValues(tileSlots);

    putSlots(tileSlots);
    putSlots(portSlots);

    for (TileSlot tile : tileSlots) {
      if (tile.rollValue == -1) continue;

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
      slotMap.put(slot.position, slot);
    }
  }

  public static boolean hasBuilding(Building building) {
    return allBuildings.contains(building);
  }

  public static boolean hasRoad(Road road) {
    return allRoads.contains(road);
  }

  public static void addBuilding(Building building) {
    allBuildings.add(building);

    IntPoint position = building.getPosition();
    if (positionIsOutOfBounds(position)) {
      throw new IllegalArgumentException(
        "This building is at an invalid position"
      );
    }

    buildingMap.put(position, building);
  }

  public static void addBuildings(Collection<? extends Building> buildings) {
    for (Building building : buildings) {
      addBuilding(building);
    }
  }

  public static void addRoad(Road road) {
    HashSet<Road> roadSet1 = getRoadsAt(road.getPosition1());
    HashSet<Road> roadSet2 = getRoadsAt(road.getPosition2());

    if (roadSet1 == null || roadSet2 == null) {
      throw new IllegalArgumentException("This road is at an invalid position");
    }

    allRoads.add(road);
    roadSet1.add(road);
    roadSet2.add(road);

    Player roadOwner = road.getOwner();
    int newRoadLength = RoadRuler.getLongestRoadLengthFor(roadOwner);
    if (
      newRoadLength >= 5 &&
      newRoadLength > LongestRoadCard.getLongestRoadLength()
    ) {
      LongestRoadCard.setCardOwner(roadOwner);
    }
  }

  public static void addRoads(Collection<Road> roads) {
    for (Road road : roads) {
      addRoad(road);
    }
  }

  public static void removeBuilding(Building building) {
    allBuildings.add(building);

    IntPoint position = building.getPosition();
    buildingMap.put(position, null);
  }

  public static void removeRoad(Road road) {
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

  public static void removeBuildable(Buildable buildable) {
    if (buildable instanceof Road) {
      Road road = (Road) buildable;
      removeRoad(road);
    } else if (buildable instanceof Building) {
      Building building = (Building) buildable;
      removeBuilding(building);
    }
  }

  public static void removeBuildables(
    Collection<? extends Buildable> buildables
  ) {
    for (Buildable buildable : buildables) {
      removeBuildable(buildable);
    }
  }

  public static void upgradeBuilding(Building from, Building to) {
    allBuildings.remove(from);
    allBuildings.add(to);
  }

  public static IntPoint getRobberPosition() {
    return robberPosition;
  }

  public static void setRobberPosition(IntPoint newPosition) {
    robberPosition = newPosition;
  }

  public static ArrayList<TileSlot> getTilesForRollValue(int rollValue) {
    ArrayList<TileSlot> tileList = rollValueMap.get(rollValue);
    if (tileList == null) {
      tileList = new ArrayList<>();
      rollValueMap.put(rollValue, tileList);
    }

    return tileList;
  }

  public static ArrayList<Building> getBuildingsAroundTile(TileSlot slot) {
    IntPoint tilePosition = slot.position;
    ArrayList<Building> buildingList = new ArrayList<Building>();
    for (IntPoint offset : FIRST_ORDER_OFFSETS) {
      IntPoint buildingPosition = tilePosition.plus(offset);
      Building building = buildingMap.get(buildingPosition);
      if (building != null) {
        buildingList.add(building);
      }
    }

    return buildingList;
  }

  public static Building getBuildingAt(IntPoint position) {
    if (positionIsOutOfBounds(position)) {
      return null;
    } else {
      return buildingMap.get(position);
    }
  }

  public static HashSet<Road> getRoadsAt(IntPoint position) {
    if (positionIsOutOfBounds(position)) {
      return null;
    }

    HashSet<Road> result = roadMap.get(position);
    if (result == null) {
      result = new HashSet<Road>();
      roadMap.put(position, result);
    }

    return result;
  }

  public static MapSlot getSlotAt(IntPoint position) {
    if (positionIsOutOfBounds(position)) {
      return null;
    } else {
      return slotMap.get(position);
    }
  }

  public static boolean positionIsNextToTile(IntPoint position) {
    PositionType positionType = PositionType.valueOf(position);

    int start;
    switch (positionType) {
      case EVEN_CORNER:
        start = 1;
        break;
      case ODD_CORNER:
        start = 0;
        break;
      default:
        return false;
    }

    for (int i = start; i < FIRST_ORDER_OFFSETS.length; i += 2) {
      IntPoint offset = FIRST_ORDER_OFFSETS[i];
      IntPoint tilePosition = position.plus(offset);

      if (positionIsOutOfBounds(tilePosition)) continue;

      MapSlot slot = slotMap.get(tilePosition);
      if (slot != null && slot instanceof TileSlot) {
        return true;
      }
    }

    return false;
  }

  public static boolean positionIsNextToBuilding(IntPoint position) {
    PositionType positionType = PositionType.valueOf(position);

    int start;
    switch (positionType) {
      case EVEN_CORNER:
        start = 0;
        break;
      case ODD_CORNER:
        start = 1;
        break;
      default:
        return false;
    }

    for (int i = start; i < FIRST_ORDER_OFFSETS.length; i += 2) {
      IntPoint offset = FIRST_ORDER_OFFSETS[i];
      IntPoint buildingPosition = position.plus(offset);

      if (positionIsOutOfBounds(buildingPosition)) continue;

      Building building = buildingMap.get(buildingPosition);
      if (building != null) {
        return true;
      }
    }

    return false;
  }

  public static boolean positionHasRoad(Player owner, IntPoint position) {
    HashSet<Road> roads = getRoadsAt(position);
    if (roads == null) return false;

    for (Road road : roads) {
      if (road.getOwner() == owner) {
        return true;
      }
    }

    return false;
  }

  public static boolean canBuildBuildingAt(Player owner, IntPoint position) {
    return (
      positionIsNextToTile(position) &&
      !positionIsNextToBuilding(position) &&
      positionHasRoad(owner, position)
    );
  }

  public static boolean canBuildRoadAt(
    Player owner,
    IntPoint position1,
    IntPoint position2
  ) {
    return (
      positionIsNextToTile(position1) &&
      positionIsNextToTile(position2) &&
      (positionHasRoad(owner, position1) || positionHasRoad(owner, position2))
    );
  }

  public static ArrayList<Building> getBuildingsNextTo(IntPoint position) {
    ArrayList<Building> result = new ArrayList<>();

    for (IntPoint offset : FIRST_ORDER_OFFSETS) {
      IntPoint buildingPosition = position.plus(offset);
      if (positionIsOutOfBounds(buildingPosition)) continue;

      Building building = buildingMap.get(buildingPosition);
      if (building != null) {
        result.add(building);
      }
    }

    return result;
  }

  public static ArrayList<Building> getBuildingsNextToRobberPosition() {
    return getBuildingsNextTo(robberPosition);
  }

  public static ArrayList<Building> getBuildingsAtPort(PortSlot portSlot) {
    ArrayList<Building> result = new ArrayList<>();

    int positionIndex1 = portSlot.portDirection;

    // java's modulus is so weird...
    int positionIndex2 = (portSlot.portDirection - 1) % 6;
    if (positionIndex2 < 0) {
      positionIndex2 += 6;
    }

    IntPoint positionOffset1 = FIRST_ORDER_OFFSETS[positionIndex1];
    IntPoint positionOffset2 = FIRST_ORDER_OFFSETS[positionIndex2];

    IntPoint position1 = portSlot.position.plus(positionOffset1);
    IntPoint position2 = portSlot.position.plus(positionOffset2);

    Building building1 = buildingMap.get(position1);
    Building building2 = buildingMap.get(position2);

    if (building1 != null) {
      result.add(building1);
    }
    if (building2 != null) {
      result.add(building2);
    }

    return result;
  }

  public static boolean positionIsOutOfBounds(IntPoint position) {
    return slotMap == null || slotMap.positionIsOutOfBounds(position);
  }

  public static Matrix<MapSlot> getMap() {
    return slotMap;
  }

  public static HashSet<Building> getBuildings() {
    return allBuildings;
  }

  public static HashSet<Road> getRoads() {
    return allRoads;
  }

  public static HashSet<Buildable> getBuildables() {
    HashSet<Buildable> result = new HashSet<>();
    result.addAll(allBuildings);
    result.addAll(allRoads);
    return result;
  }

  public static IntPoint getMapSize() {
    return mapSize;
  }
}
