package venusians.data.board.tiles;

import javafx.scene.image.Image;

public enum ExtraTileKind implements TileKind {
  OCEAN(new Image("oceanTile.png")),
  DESERT(new Image("desertTile.png"));

  private Image tileImage;

  private ExtraTileKind(Image tileImage) {
    this.tileImage = tileImage;
  }

  public Image getTileImage() {
    return tileImage;
  }
}
