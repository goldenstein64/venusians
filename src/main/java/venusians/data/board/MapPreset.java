package venusians.data.board;

import venusians.data.Point;

public enum MapPreset {
  CLASSIC, // a hexagon made up of 3 layers

  SEAFARERS_1, // different island configurations form the Seafarers extension
  SEAFARERS_2,
  SEAFARERS_3,
  SEAFARERS_4, // etc

  PLAYER_EXTENSION; // a rhombusy hexagon?

  private final DefinedTile[] tiles;
  private final boolean randomized;

  private MapPreset() {
    this.tiles = new DefinedTile[0];
    this.randomized = true;
  }

  private MapPreset(DefinedTile[] tiles) {
    this(tiles, true);
  }

  private MapPreset(DefinedTile[] tiles, boolean randomized) {
    this.tiles = tiles;
    this.randomized = randomized;
  }

  public DefinedTile[] getTiles() {
    return tiles;
  }

  public boolean isRandomized() {
    return randomized;
  }
}
