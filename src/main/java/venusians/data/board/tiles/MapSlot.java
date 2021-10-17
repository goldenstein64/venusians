package venusians.data.board.tiles;

import venusians.data.board.IntPoint;

public class MapSlot {

  public IntPoint position;
  public TileKind kind;
  public int rollValue;

  public MapSlot(IntPoint position, TileKind kind, int rollValue) {
    this.position = position;
    this.kind = kind;
    this.rollValue = rollValue;
  }

  public MapSlot clone() {
    return new MapSlot(position, kind, rollValue);
  }
}
