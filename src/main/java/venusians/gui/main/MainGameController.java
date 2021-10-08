package venusians.gui.main;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.Board;
import venusians.data.board.tiles.MapSlot;
import venusians.gui.App;

public class MainGameController {

  private double tileOffset = 0.104;

  @FXML
  private AnchorPane mapPane;

  @FXML
  private void endGame() throws IOException {
    App.setRoot("results");
  }

  @FXML
  private void initialize() {
    Game.startGame();

    createMap();
  }

  private void createMap() {
    for (MapSlot[] row : Board.getMap()) {
      for (MapSlot tile : row) {
        if (tile == null) {
          continue;
        }
        Point guiPosition = HexTransform.toGuiPosition(tile.position);
        Image guiImage = tile.kind.getTileImage();
        ImageView imageView = new ImageView(guiImage);
        imageView.setScaleX(tileOffset);
        imageView.setScaleY(tileOffset);
        imageView.setLayoutX(guiPosition.x);
        imageView.setLayoutY(guiPosition.y);
        mapPane.getChildren().add(imageView);
      }
    }
  }
}
