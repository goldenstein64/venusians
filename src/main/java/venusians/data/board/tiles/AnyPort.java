package venusians.data.board.tiles;

import javafx.scene.image.Image;
import venusians.util.Images;

public enum AnyPort implements PortKind {
  INSTANCE;

  private Image portImage = Images.load(AnyPort.class, "anyPort.png");
  private int portNecessaryCount = 3;
  private int portRequestedCount = 1;

  public static AnyPort getInstance() {
    return INSTANCE;
  }

  @Override
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
