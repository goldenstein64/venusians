package venusians.data.board;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import venusians.data.Point;
import venusians.data.board.buildable.Buildable;
import venusians.util.Event;

public class Board {

  private static SecureRandom rng = new SecureRandom();
  private static TileKind[][] map;
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

    public Buildable fromBuildable;
    public Buildable toBuildable;
    public ActionType actionType;

    public MapChangedArgs(Buildable buildable) {
      this.toBuildable = buildable;
      this.actionType = ActionType.ADDED;
    }

    public MapChangedArgs(Buildable from, Buildable to) {
      this.fromBuildable = from;
      this.toBuildable = to;
      this.actionType = ActionType.UPGRADED;
    }
  }

  public static void setUp() {
    // create the map of tiles

    // from a preset
    MapPreset mapPreset = MapPreset.CLASSIC;

    map = new TileKind[13][13];
    if (mapPreset.isRandomized()) {
      DefinedTile[] tiles = mapPreset.getTiles();
      DefinedTile[] newTiles = tiles.clone();
      for (int i = 0; i < newTiles.length; i++) {
        tiles[i] = tiles[i].clone();
      }
      for (int i = 0; i < newTiles.length; i++) {
        DefinedTile tile1 = newTiles[i];
        int choice = rng.nextInt(newTiles.length);
        DefinedTile tile2 = newTiles[choice];
        Point temp = tile1.position;
        tile1.position = tile2.position;
        tile2.position = temp;
      }
    } else {
      for (DefinedTile tile : mapPreset.getTiles()) {
        map[tile.position.x][tile.position.y] = tile.kind;
      }
    }
  }

  public static TileKind[][] getMap() {
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
