package venusians.data.cards.resource;

import javafx.scene.image.Image;
import venusians.data.board.tiles.TileKind;

public enum ResourceCard implements TileKind {
  WOOD(new Image(""), new Image("")),
  WOOL(new Image(""), new Image("")),
  WHEAT(new Image(""), new Image("")),
  ORE(new Image(""), new Image("")),
  BRICK(new Image(""), new Image(""));

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
