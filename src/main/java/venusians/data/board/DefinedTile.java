package venusians.data.board;

import venusians.data.Point;

public class DefinedTile {

  public Point position;
  public TileKind kind;

  public DefinedTile(Point position, TileKind kind) {
    this.position = position;
    this.kind = kind;
  }

  public DefinedTile clone() {
    return new DefinedTile(position, kind);
  }
}
