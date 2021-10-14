package venusians.gui.main;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.Board;
import venusians.data.board.tiles.MapSlot;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;

public class MainGameController {

  private final double tileOffset = 0.104;
  private final int HISTORY_RANGE = 20;

  private boolean shouldSnapVvalue = false;

  @FXML
  private AnchorPane mapPane;

  @FXML
  private Label victoryPointsValueLabel;

  @FXML
  private TextField chatPrompt;

  @FXML
  private VBox chatHistory;

  @FXML
  private ScrollPane chatScrollPane;

  @FXML
  private void endGame(ActionEvent event) throws IOException {
    App.setRoot("results");
  }

  @FXML
  private void incrementVictoryPoints() {
    Player currentPlayer = Players.getCurrentPlayer();
    int oldValue = currentPlayer.getVictoryPoints();
    currentPlayer.setVictoryPoints(oldValue + 1);
  }

  @FXML
  private void sayMessage() {
    String message = chatPrompt.getText();
    if (message.isEmpty()) return;

    String authorName = Players.getCurrentPlayer().getName();
    String messageContent = String.format("[%s]: %s", authorName, message);

    ObservableList<Node> chatChildren = chatHistory.getChildren();

    Label messageLabel = new Label(messageContent);
    chatChildren.add(messageLabel);
    if (chatChildren.size() > HISTORY_RANGE) chatChildren.remove(0);

    chatPrompt.setText("");
    if (chatScrollPane.getVvalue() == 1.0) {
      shouldSnapVvalue = true;
    }
  }

  private ChangeListener<Number> victoryPointsListener = new ChangeListener<Number>() {
    public void changed(
      ObservableValue<? extends Number> value,
      Number oldValue,
      Number newValue
    ) {
      System.out.println("fired!");
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
        .removeListener(victoryPointsListener);
      if (newValue != null) newValue
        .victoryPointsProperty()
        .addListener(victoryPointsListener);
    }
  };

  private ChangeListener<Number> setToMax = new ChangeListener<Number>() {
    public void changed(
      ObservableValue<? extends Number> value,
      Number oldValue,
      Number newValue
    ) {
      if (shouldSnapVvalue) {
        shouldSnapVvalue = false;
        chatScrollPane.setVvalue(1);
      }
    }
  };

  @FXML
  private void initialize() {
    Game.startGame();

    createMap();

    initializePlayers();

    chatScrollPane.vvalueProperty().addListener(setToMax);
  }

  private void initializePlayers() {
    Players.currentPlayerProperty().addListener(playerListener);

    Player currentPlayer = Players.getCurrentPlayer();
    currentPlayer.victoryPointsProperty().addListener(victoryPointsListener);

    int currentVictoryPoints = currentPlayer.getVictoryPoints();
    victoryPointsValueLabel.setText(String.valueOf(currentVictoryPoints));
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
