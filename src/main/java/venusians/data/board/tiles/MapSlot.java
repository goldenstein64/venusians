package venusians.data.board.tiles;

import venusians.data.board.IntPoint;

public abstract class MapSlot {

  public IntPoint position;

  public MapSlot(IntPoint position) {
    this.position = position;
  }

  @Override
  public abstract MapSlot clone();
}
