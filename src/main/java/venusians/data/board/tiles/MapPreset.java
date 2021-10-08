package venusians.data.board.tiles;

import venusians.data.IntPoint;
import venusians.data.cards.resource.ResourceCard;

public enum MapPreset {
  CLASSIC(
    new MapSlot[] {
      new MapSlot(new IntPoint(1, 7), ResourceCard.WHEAT, 9),
      new MapSlot(new IntPoint(2, 8), ResourceCard.WOOD, 8),
      new MapSlot(new IntPoint(3, 9), ResourceCard.BRICK, 5),
      new MapSlot(new IntPoint(2, 5), ResourceCard.WHEAT, 12),
      new MapSlot(new IntPoint(3, 6), ResourceCard.WOOD, 11),
      new MapSlot(new IntPoint(4, 7), ResourceCard.ORE, 3),
      new MapSlot(new IntPoint(5, 8), ResourceCard.WHEAT, 6),
      new MapSlot(new IntPoint(3, 3), ResourceCard.ORE, 10),
      new MapSlot(new IntPoint(4, 4), ResourceCard.BRICK, 6),
      new MapSlot(new IntPoint(5, 5), ExtraTileKind.DESERT, -1),
      new MapSlot(new IntPoint(6, 6), ResourceCard.WHEAT, 4),
      new MapSlot(new IntPoint(7, 7), ResourceCard.WOOL, 11),
      new MapSlot(new IntPoint(5, 2), ResourceCard.WOOL, 2),
      new MapSlot(new IntPoint(6, 3), ResourceCard.WOOL, 4),
      new MapSlot(new IntPoint(7, 4), ResourceCard.WOOD, 3),
      new MapSlot(new IntPoint(8, 5), ResourceCard.WOOL, 5),
      new MapSlot(new IntPoint(7, 1), ResourceCard.WOOD, 9),
      new MapSlot(new IntPoint(8, 2), ResourceCard.BRICK, 10),
      new MapSlot(new IntPoint(9, 3), ResourceCard.ORE, 8),
    }
  ), // a hexagon made up of 3 layers

  SEAFARERS_1, // different island configurations frOm the Seafarers extension
  SEAFARERS_2,
  SEAFARERS_3,
  SEAFARERS_4, // etc

  PLAYER_EXTENSION; // a rhombusy hexagon?

  private final MapSlot[] tiles;

  private MapPreset() {
    this(new MapSlot[0]);
  }

  private MapPreset(MapSlot[] tiles) {
    this.tiles = tiles;
  }

  public MapSlot[] getSlots() {
    return tiles;
  }
}
