package venusians.data.cards.resource;

import javafx.scene.image.Image;
import venusians.data.board.tiles.TileKind;

public enum ResourceCard implements TileKind {
  WOOD(new Image("file:woodCard.png"), new Image("file:woodTile.png")),
  WOOL(new Image("file:woolCard.png"), new Image("file:woolTile.png")),
  WHEAT(new Image("file:wheatCard.png"), new Image("file:wheatTile.png")),
  ORE(new Image("file:oreCard.png"), new Image("file:oreTile.png")),
  BRICK(new Image("file:brickCard.png"), new Image("file:brickTile.png"));

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
