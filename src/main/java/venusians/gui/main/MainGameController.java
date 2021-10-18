package venusians.gui.main;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import venusians.data.Game;
import venusians.data.board.Board;
import venusians.data.board.Point;
import venusians.data.board.tiles.MapSlot;
import venusians.data.cards.development.DevelopmentCard;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;
import venusians.gui.ChangeListenerBuilder;

public class MainGameController {

  private double tileHeightAspectRatio = Math.sqrt(3) / 2;

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
  private Pane developmentPane;

  @FXML
  private Pane resourcePane;

  @FXML
  private StackPane mainViewPane;

  @FXML
  private ScrollPane chatScrollPane;

  @FXML
  private Button diceRollButton;

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

  @FXML
  private void rollDice() {
    DicePaneComponent.showDiceWindow(mainViewPane);
  }

  @FXML
  private void initialize() {
    Game.startGame();

    createMap();
    initializePlayers();
    applyChatSnap();

    Player currentPlayer = Players.getCurrentPlayer();

    for (int i = 0; i < 5; i++) {
      currentPlayer.pickDevelopmentCard();
    }

    displayCards(currentPlayer.getDevelopmentHand());
    displayCards(currentPlayer.getResourceHand());
  }

  private void createMap() {
    for (MapSlot[] row : Board.getMap()) {
      for (MapSlot tile : row) {
        if (tile == null) continue;

        Point guiPosition = HexTransform.toGuiPosition(tile.position);
        Image guiImage = tile.kind.getTileImage();

        StackPane slotPane = new StackPane();
        slotPane.setLayoutX(guiPosition.x);
        slotPane.setLayoutY(guiPosition.y);
        slotPane.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(guiImage);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100 * tileHeightAspectRatio);

        slotPane.getChildren().add(imageView);

        if (tile.rollValue != -1) {
          Circle rollValueShape = new Circle(15);
          rollValueShape.setFill(Color.WHITE);

          Label rollValueLabel = new Label(String.valueOf(tile.rollValue));

          slotPane.getChildren().addAll(rollValueShape, rollValueLabel);
        }

        mapPane.getChildren().add(slotPane);
      }
    }
  }

  private void initializePlayers() {
    Players.currentPlayerProperty().addListener(playerListener);

    Player currentPlayer = Players.getCurrentPlayer();
    currentPlayer.victoryPointsProperty().addListener(victoryPointsListener);

    int currentVictoryPoints = currentPlayer.getVictoryPoints();
    victoryPointsValueLabel.setText(String.valueOf(currentVictoryPoints));
  }

  private ChangeListener<Number> victoryPointsListener = new ChangeListenerBuilder<Number>()
  .from(
      (Number oldValue, Number newValue) -> {
        victoryPointsValueLabel.setText(newValue.toString());
      }
    );

  private ChangeListener<Player> playerListener = new ChangeListenerBuilder<Player>()
  .from(
      (Player oldValue, Player newValue) -> {
        if (oldValue != null) {
          oldValue
            .victoryPointsProperty()
            .removeListener(victoryPointsListener);
        }
        if (newValue != null) {
          newValue.victoryPointsProperty().addListener(victoryPointsListener);
        }
      }
    );

  private void applyChatSnap() {
    chatScrollPane.vvalueProperty().addListener(snapToBottom);
  }

  private ChangeListener<Number> snapToBottom = new ChangeListenerBuilder<Number>()
  .from(
      (Number oldValue, Number newValue) -> {
        if (shouldSnapVvalue) {
          shouldSnapVvalue = false;
          chatScrollPane.setVvalue(1);
        }
      }
      );
    
  private void displayCards(DevelopmentCard[] cards) {
    for (int i = 0; i < cards.length; i++) {
      DevelopmentCard card = cards[i];
      Image cardImage = card.getCardImage();
      ImageView cardImageView = new ImageView(cardImage);
      cardImageView.setFitWidth(100);
      cardImageView.setFitHeight(100);
      double offset = 20 * (i - (cards.length - 1) / 2.0);
      cardImageView.layoutXProperty().bind(developmentPane.widthProperty().divide(2.0).add(offset));
      cardImageView.setLayoutY(37.5);
      developmentPane.getChildren().add(cardImageView);
    }
  }

  private void displayCards(ResourceCard[] cards) {
    for (int i = 0; i < cards.length; i++) {
      ResourceCard card = cards[i];
      Image cardImage = card.getCardImage();
      ImageView cardImageView = new ImageView(cardImage);
      cardImageView.setFitWidth(100);
      cardImageView.setFitHeight(100);
      double offset = 20 * (i - (cards.length - 1) / 2.0);
      cardImageView.layoutXProperty().bind(resourcePane.widthProperty().divide(2.0).add(offset));
      cardImageView.setLayoutY(37.5);
      resourcePane.getChildren().add(cardImageView);
    }
  }
}
