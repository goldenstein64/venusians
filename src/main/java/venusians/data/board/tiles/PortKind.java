package venusians.data.board.tiles;

import javafx.scene.image.Image;

public interface PortKind {
  public Image getPortImage();

  public int getPortRequestedCount();

  public int getPortNecessaryCount();
}
