package venusians.data.board.tiles;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;

/**
 * An enum that stores every map preset used in this game.
 */
public enum MapPreset {
  CLASSIC(
    new TileSlot[] {
      new TileSlot(new IntPoint(2, 8), ResourceCard.WHEAT, 9),
      new TileSlot(new IntPoint(3, 9), ResourceCard.WOOD, 8),
      new TileSlot(new IntPoint(4, 10), ResourceCard.BRICK, 5),
      new TileSlot(new IntPoint(3, 6), ResourceCard.WHEAT, 12),
      new TileSlot(new IntPoint(4, 7), ResourceCard.WOOD, 11),
      new TileSlot(new IntPoint(5, 8), ResourceCard.ORE, 3),
      new TileSlot(new IntPoint(6, 9), ResourceCard.WHEAT, 6),
      new TileSlot(new IntPoint(4, 4), ResourceCard.ORE, 10),
      new TileSlot(new IntPoint(5, 5), ResourceCard.BRICK, 6),
      new TileSlot(new IntPoint(6, 6), ExtraTileKind.DESERT, -1),
      new TileSlot(new IntPoint(7, 7), ResourceCard.WHEAT, 4),
      new TileSlot(new IntPoint(8, 8), ResourceCard.WOOL, 11),
      new TileSlot(new IntPoint(6, 3), ResourceCard.WOOL, 2),
      new TileSlot(new IntPoint(7, 4), ResourceCard.WOOL, 4),
      new TileSlot(new IntPoint(8, 5), ResourceCard.WOOD, 3),
      new TileSlot(new IntPoint(9, 6), ResourceCard.WOOL, 5),
      new TileSlot(new IntPoint(8, 2), ResourceCard.WOOD, 9),
      new TileSlot(new IntPoint(9, 3), ResourceCard.BRICK, 10),
      new TileSlot(new IntPoint(10, 4), ResourceCard.ORE, 8),
    },
    new PortSlot[] {
      new PortSlot(new IntPoint(3, 3), AnyPort.INSTANCE, 2),
      new PortSlot(new IntPoint(1, 7), ResourceCard.WOOD, 1),
      new PortSlot(new IntPoint(3, 12), AnyPort.INSTANCE, 0),
      new PortSlot(new IntPoint(1, 10), ResourceCard.BRICK, 1),
      new PortSlot(new IntPoint(7, 10), AnyPort.INSTANCE, 5),
      new PortSlot(new IntPoint(10, 7), ResourceCard.WOOL, 5),
      new PortSlot(new IntPoint(7, 1), ResourceCard.WHEAT, 3),
      new PortSlot(new IntPoint(10, 1), ResourceCard.ORE, 3),
      new PortSlot(new IntPoint(12, 3), AnyPort.INSTANCE, 4),
    },
    new IntPoint(13, 13), // size
    new IntPoint(6, 6) // robberPosition
  ),

  DEBUG_PORT(
    new TileSlot[] {
      new TileSlot(new IntPoint(7, 1), ResourceCard.WOOD, 6),
      new TileSlot(new IntPoint(5, 5), ExtraTileKind.DESERT, -1),
    },
    new PortSlot[] { new PortSlot(new IntPoint(6, 3), ResourceCard.WOOD, 0) },
    new IntPoint(8, 6),
    new IntPoint(5, 5)
  ),

  SEAFARERS_1, // different island configurations frOm the Seafarers extension
  SEAFARERS_2,
  SEAFARERS_3,
  SEAFARERS_4, // etc

  PLAYER_EXTENSION; // a rhombusy hexagon?

  private final TileSlot[] tiles;
  private final PortSlot[] ports;

  public final IntPoint size;

  public final IntPoint robberPosition;

  private MapPreset() {
    this(
      new TileSlot[0],
      new PortSlot[0],
      new IntPoint(0, 0),
      new IntPoint(0, 0)
    );
  }

  private MapPreset(
    TileSlot[] tiles,
    PortSlot[] ports,
    IntPoint size,
    IntPoint robberPosition
  ) {
    this.tiles = tiles;
    this.ports = ports;
    this.size = size;
    this.robberPosition = robberPosition;
  }

  public TileSlot[] getTileSlots() {
    return tiles;
  }

  public PortSlot[] getPortSlots() {
    return ports;
  }
}
