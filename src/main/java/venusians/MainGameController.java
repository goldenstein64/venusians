package venusians;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.Board;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.TileKind;
import venusians.gui.HexTransform;

public class MainGameController {

  // @FXML
  // private AnchorPane mapPane;

  @FXML
  private void gameEnded() throws IOException {
    App.setRoot("results");
  }

  @FXML
  private void initialize() {
    Game.startGame();

    // create a map
    for (MapSlot[] row : Board.getMap()) {
      for (MapSlot tile : row) {
        if (tile == null) {
          continue;
        }
        Point guiPosition = HexTransform.toGuiPosition(tile.position);
        Image guiImage = tile.kind.getTileImage();
        ImageView imageView = new ImageView(guiImage);
        imageView.setLayoutX(guiPosition.x);
        imageView.setLayoutY(guiPosition.y);
        //mapPane.getChildren().add(imageView);
      }
    }
  }
}
