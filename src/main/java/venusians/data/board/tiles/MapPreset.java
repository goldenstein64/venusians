package venusians.data.board.tiles;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;

public enum MapPreset {
  CLASSIC(
    new TileSlot[] {
      new TileSlot(new IntPoint(1, 7), ResourceCard.WHEAT, 9),
      new TileSlot(new IntPoint(2, 8), ResourceCard.WOOD, 8),
      new TileSlot(new IntPoint(3, 9), ResourceCard.BRICK, 5),
      new TileSlot(new IntPoint(2, 5), ResourceCard.WHEAT, 12),
      new TileSlot(new IntPoint(3, 6), ResourceCard.WOOD, 11),
      new TileSlot(new IntPoint(4, 7), ResourceCard.ORE, 3),
      new TileSlot(new IntPoint(5, 8), ResourceCard.WHEAT, 6),
      new TileSlot(new IntPoint(3, 3), ResourceCard.ORE, 10),
      new TileSlot(new IntPoint(4, 4), ResourceCard.BRICK, 6),
      new TileSlot(new IntPoint(5, 5), ExtraTileKind.DESERT, -1),
      new TileSlot(new IntPoint(6, 6), ResourceCard.WHEAT, 4),
      new TileSlot(new IntPoint(7, 7), ResourceCard.WOOL, 11),
      new TileSlot(new IntPoint(5, 2), ResourceCard.WOOL, 2),
      new TileSlot(new IntPoint(6, 3), ResourceCard.WOOL, 4),
      new TileSlot(new IntPoint(7, 4), ResourceCard.WOOD, 3),
      new TileSlot(new IntPoint(8, 5), ResourceCard.WOOL, 5),
      new TileSlot(new IntPoint(7, 1), ResourceCard.WOOD, 9),
      new TileSlot(new IntPoint(8, 2), ResourceCard.BRICK, 10),
      new TileSlot(new IntPoint(9, 3), ResourceCard.ORE, 8),
    },
      new PortSlot[] {
        new PortSlot(new IntPoint(1, 4), ResourceCard.WOOL, 2)
      }, 13, 13
  ), // a hexagon made up of 3 layers

  SEAFARERS_1, // different island configurations frOm the Seafarers extension
  SEAFARERS_2,
  SEAFARERS_3,
  SEAFARERS_4, // etc

  PLAYER_EXTENSION; // a rhombusy hexagon?

  private final TileSlot[] tiles;
  private final PortSlot[] ports;

  public final int length;
  public final int width;

  private MapPreset() {
    this(new TileSlot[0], new PortSlot[0], 0, 0);
  }

  private MapPreset(TileSlot[] tiles, PortSlot[] ports, int length, int width) {
    this.tiles = tiles;
    this.ports = ports;
    this.length = length;
    this.width = width;
  }

  public TileSlot[] getTileSlots() {
    return tiles;
  }

  public PortSlot[] getPortSlots() {
    return ports;
  }
}
