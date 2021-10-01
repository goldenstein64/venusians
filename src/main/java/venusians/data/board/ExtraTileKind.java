package venusians.data.board;

import javafx.scene.image.Image;

public enum ExtraTileKind implements TileKind {
  OCEAN(new Image("")),
  DESERT(new Image(""));

  private Image tileImage;

  private ExtraTileKind(Image tileImage) {
    this.tileImage = tileImage;
  }

  public Image getTileImage() {
    return tileImage;
  }
}
