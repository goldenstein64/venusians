package venusians.data.board.tiles;

import venusians.data.Point;

public class MapSlot {

  public Point position;
  public TileKind kind;
  public int rollValue;

  public MapSlot(Point position, TileKind kind, int rollValue) {
    this.position = position;
    this.kind = kind;
    this.rollValue = rollValue;
  }

  public MapSlot clone() {
    return new MapSlot(position, kind, rollValue);
  }
}
