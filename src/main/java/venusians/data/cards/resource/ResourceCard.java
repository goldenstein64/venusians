package venusians.data.cards.resource;

import javafx.scene.image.Image;
import venusians.data.board.tiles.TileKind;

public enum ResourceCard implements TileKind {
  WOOD(new Image("woodCard.png"), new Image("woodTile.png")),
  WOOL(new Image("woolCard.png"), new Image("woolTile.png")),
  WHEAT(new Image("wheatCard.png"), new Image("wheatTile.png")),
  ORE(new Image("oreCard.png"), new Image("oreTile.png")),
  BRICK(new Image("brickCard.png"), new Image("brickTile.png"));

  private static final ResourceCard[] resourceArray = new ResourceCard[] {
    WOOD,
    WOOL,
    WHEAT,
    ORE,
    BRICK,
  };

  private Image cardImage;
  private Image tileImage;

  private ResourceCard(Image cardImage, Image tileImage) {
    this.cardImage = cardImage;
    this.tileImage = tileImage;
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
