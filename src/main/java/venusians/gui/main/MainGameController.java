package venusians.gui.main;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.Board;
import venusians.data.board.tiles.MapSlot;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;

public class MainGameController {

  private double tileOffset = 0.104;

  @FXML
  private AnchorPane mapPane;

  @FXML
  private Label victoryPointsValueLabel;

  @FXML
  private void endGame() throws IOException {
    App.setRoot("results");
  }

  private ChangeListener<Number> victoryPointListener = new ChangeListener<Number>() {
    public void changed(
      ObservableValue<? extends Number> value,
      Number oldValue,
      Number newValue
    ) {
      victoryPointsValueLabel.setText(newValue.toString());
    }
  };

  private ChangeListener<Player> playerListener = new ChangeListener<Player>() {
    public void changed(
      ObservableValue<? extends Player> value,
      Player oldValue,
      Player newValue
    ) {
      if (oldValue != null) oldValue
        .victoryPointsProperty()
        .removeListener(victoryPointListener);
      if (newValue != null) newValue
        .victoryPointsProperty()
        .addListener(victoryPointListener);
    }
  };

  @FXML
  private void initialize() {
    Game.startGame();

    createMap();

    Players.currentPlayerProperty().addListener(playerListener);
  }

  private void createMap() {
    for (MapSlot[] row : Board.getMap()) {
      for (MapSlot tile : row) {
        if (tile == null) continue;

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
