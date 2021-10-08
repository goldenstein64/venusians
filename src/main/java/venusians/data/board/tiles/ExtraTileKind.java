package venusians.data.board.tiles;

import java.net.URISyntaxException;
import javafx.scene.image.Image;

public enum ExtraTileKind implements TileKind {
  OCEAN("oceanTile.png"),
  DESERT("desertTile.png");

  private Image tileImage;

  private ExtraTileKind(String tileImage) {
    try {
      this.tileImage =
        new Image(getClass().getResource(tileImage).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public Image getTileImage() {
    return tileImage;
  }
}
