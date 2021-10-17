package venusians.gui.main;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import venusians.data.DiceRoll;
import venusians.data.Game;
import venusians.data.Point;
import venusians.data.board.Board;
import venusians.data.board.tiles.MapSlot;
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
    // roll the dice first
    DiceRoll diceRoll = new DiceRoll(2);

    // add a rolling dice label
    StackPane dicePane = new StackPane();

    Rectangle backdrop = new Rectangle();
    backdrop.setFill(Color.BLACK);
    backdrop.setOpacity(0.75);
    backdrop.widthProperty().bind(mainViewPane.widthProperty());
    backdrop.heightProperty().bind(mainViewPane.heightProperty());

    Rectangle windowGraphic = new Rectangle(300, 200);
    windowGraphic.setArcWidth(20);
    windowGraphic.setArcHeight(20);
    windowGraphic.setFill(Color.GREY);
    
    VBox contentBox = new VBox();
    contentBox.setAlignment(Pos.CENTER);

    Label totalValueLabel = new Label(String.format("Total: %d", diceRoll.totalValue));
    totalValueLabel.setTextFill(Color.WHITE);

    HBox diceBox = new HBox();
    diceBox.setSpacing(20);
    diceBox.setPadding(new Insets(20));
    diceBox.setAlignment(Pos.CENTER);

    for (int i = 0; i < 2; i++) {
      Image dieImage = DiceRoll.getImage(diceRoll.values[i]);

      ImageView dieImageView = new ImageView(dieImage);

      dieImageView.setFitWidth(75);
      dieImageView.setFitHeight(75);

      diceBox.getChildren().add(dieImageView);
    }

    Button okButton = new Button("OK");

    okButton.setOnAction(event -> {
      diceRollButton.setDisable(true);
      mainViewPane.getChildren().remove(dicePane);
    });

    contentBox.getChildren().addAll(totalValueLabel, diceBox, okButton);

    dicePane.getChildren().addAll(backdrop, windowGraphic, contentBox);

    mainViewPane.getChildren().add(dicePane);
  }

  @FXML
  private void initialize() {
    Game.startGame();

    createMap();

    initializePlayers();

    applyChatSnap();
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
}
