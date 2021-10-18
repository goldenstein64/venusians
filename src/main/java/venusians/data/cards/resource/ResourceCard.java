package venusians.data.cards.resource;

import java.net.URISyntaxException;
import javafx.scene.image.Image;
import venusians.data.board.tiles.TileKind;
import venusians.data.cards.HasCardImage;

public enum ResourceCard implements TileKind, HasCardImage {
  WOOD("woodCard.png", "woodTile.png"),
  WOOL("woolCard.png", "woolTile.png"),
  WHEAT("wheatCard.png", "wheatTile.png"),
  ORE("oreCard.png", "oreTile.png"),
  BRICK("brickCard.png", "brickTile.png");

  private static final ResourceCard[] resourceArray = new ResourceCard[] {
    WOOD,
    WOOL,
    WHEAT,
    ORE,
    BRICK,
  };

  private Image cardImage;
  private Image tileImage;

  private ResourceCard(String cardImage, String tileImage) {
    try {
      this.cardImage =
        new Image(getClass().getResource(cardImage).toURI().toString());
      this.tileImage =
        new Image(getClass().getResource(tileImage).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public static ResourceCard valueOf(int value) {
    if (value >= resourceArray.length) {
      throw new IllegalArgumentException("Value too large");
    }
    return resourceArray[value];
  }

  public Image getCardImage() {
    return cardImage;
  }

  public Image getTileImage() {
    return tileImage;
  }
}
