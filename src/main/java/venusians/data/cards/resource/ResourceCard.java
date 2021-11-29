package venusians.data.cards.resource;

import javafx.scene.image.Image;
import venusians.data.board.tiles.PortKind;
import venusians.data.board.tiles.TileKind;
import venusians.data.cards.HasCard;
import venusians.util.Images;

public enum ResourceCard implements HasCard, TileKind, PortKind {
  WOOD("woodCard.png", "woodTile.png", "woodPort.png"),
  WOOL("woolCard.png", "woolTile.png", "woolPort.png"),
  WHEAT("wheatCard.png", "wheatTile.png", "wheatPort.png"),
  ORE("oreCard.png", "oreTile.png", "orePort.png"),
  BRICK("brickCard.png", "brickTile.png", "brickPort.png");

  private Image cardImage;
  private Image tileImage;
  private Image portImage;

  private int portNecessaryCount = 2;
  private int portRequestedCount = 1;

  private ResourceCard(String cardImage, String tileImage, String portImage) {
    this.cardImage = Images.load(this, cardImage);
    this.tileImage = Images.load(this, tileImage);
    this.portImage = Images.load(this, portImage);
  }

  public static ResourceCard valueOf(int value) {
    switch (value) {
      case 0:
        return WOOD;
      case 1:
        return WOOL;
      case 2:
        return WHEAT;
      case 3:
        return ORE;
      case 4:
        return BRICK;
      default:
        throw new IllegalArgumentException(
          String.format("Value (%d) is invalid", value)
        );
    }
  }

  @Override
  public Image getCardImage() {
    return cardImage;
  }

  @Override
  public Image getTileImage() {
    return tileImage;
  }

  public Image getPortImage() {
    return portImage;
  }

  @Override
  public int getPortNecessaryCount() {
    return portNecessaryCount;
  }

  @Override
  public int getPortRequestedCount() {
    return portRequestedCount;
  }
}
