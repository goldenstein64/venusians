package venusians.gui.main;

import java.io.IOException;
import java.util.ArrayList;

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
import venusians.data.board.buildable.Building;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.PortSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.data.cards.HasCard;
import venusians.data.cards.development.DevelopmentCardList;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardList;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;
import venusians.gui.ChangeListenerBuilder;
import venusians.util.Images;

public class MainGameController {

  private double TILE_ASPECT_RATIO = Math.sqrt(3) / 2;

  private final int HISTORY_RANGE = 20;
  private final double TILE_SIZE = 100;

  private boolean shouldSnapVvalue = false;

  private Image portConnector = Images.load(MainGameController.class, "portConnector.png");

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
  private Label rollResultLabel;

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
    DicePaneComponent.rollDice(mainViewPane, this::onDicePaneExited);
  }
  
  public void onDicePaneExited(int rollValue) {
    // display number
    rollResultLabel.setText(String.valueOf(rollValue));

    TileSlot[] tileSlots = Board.getTileSlotsForRollValue(rollValue);

    for (TileSlot slot : tileSlots) {
      if (!(slot.kind instanceof ResourceCard))
        continue;

      ResourceCard resourceCard = (ResourceCard) slot.kind;

      for (Building building : Board.getBuildingsForTileSlot(slot)) {
        building.getOwner().receiveResource(resourceCard);
      }
    }
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

    // let the player build some settlements
    currentPlayer.receiveResource(ResourceCard.BRICK);
    currentPlayer.receiveResource(ResourceCard.WOOD);

    displayCards(currentPlayer.getDevelopmentHand());

    displayCards(currentPlayer.getResourceHand());
  }

  private void createMap() {
    for (MapSlot[] row : Board.getMap()) {
      for (MapSlot mapSlot : row) {
        if (mapSlot == null)
          continue;

        Point guiPosition = HexTransform.toGuiPosition(mapSlot.position);

        StackPane slotPane;
        if (mapSlot instanceof TileSlot) {
          slotPane = generateTile((TileSlot) mapSlot);
        } else if (mapSlot instanceof PortSlot) {
          slotPane = generatePort((PortSlot) mapSlot);
        } else {
          throw new RuntimeException("MapSlot kind not found");
        }

        slotPane.setLayoutX(guiPosition.x);
        slotPane.setLayoutY(guiPosition.y);
        slotPane.setAlignment(Pos.CENTER);
        
        mapPane.getChildren().add(slotPane);
      }
    }
  }

  private StackPane generateTile(TileSlot tile) {
    StackPane result = new StackPane();

    Image tileImage = tile.kind.getTileImage();
    ImageView imageView = new ImageView(tileImage);
    imageView.setFitWidth(TILE_SIZE);
    imageView.setFitHeight(TILE_SIZE * TILE_ASPECT_RATIO);

    result.getChildren().add(imageView);

    if (tile.rollValue != -1) {
      Circle rollValueShape = new Circle(15);
      rollValueShape.setFill(Color.WHITE);

      Label rollValueLabel = new Label(String.valueOf(tile.rollValue));

      result.getChildren().addAll(rollValueShape, rollValueLabel);
    }

    return result;
  }

  private StackPane generatePort(PortSlot port) {
    StackPane result = new StackPane();
    result.setPrefWidth(TILE_SIZE);
    result.setPrefHeight(TILE_SIZE * TILE_ASPECT_RATIO);

    StackPane connectorPane = new StackPane();
    connectorPane.setPrefWidth(TILE_SIZE);
    connectorPane.setPrefHeight(TILE_SIZE * TILE_ASPECT_RATIO);

    Image portImage = port.kind.getPortImage();
    ImageView portImageView = new ImageView(portImage);
    portImageView.setFitWidth(TILE_SIZE * 0.6);
    portImageView.setFitHeight(TILE_SIZE * 0.6);

    ImageView portConnectorImageView = new ImageView(portConnector);
    portConnectorImageView.setFitWidth(TILE_SIZE);
    portConnectorImageView.setFitHeight(TILE_SIZE * TILE_ASPECT_RATIO);
    Label tradeRatioLabel = new Label(String.format(
      "%d:%d", 
      port.kind.getPortNecessaryCount(), 
      port.kind.getPortRequestedCount()
    ));

    connectorPane.setRotate(60 * port.portDirection);

    connectorPane.getChildren().addAll(portConnectorImageView, tradeRatioLabel);

    result.getChildren().addAll(connectorPane, portImageView);

    return result;
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
    

  private void displayCards(ResourceCardList cards) {
    displayCardsOnPane(cards, resourcePane);
  }

  private void displayCards(DevelopmentCardList cards) {
    displayCardsOnPane(cards, developmentPane);
  }

  private <T extends HasCard> void displayCardsOnPane(ArrayList<T> cards, Pane cardPane) {
    for (int i = 0; i < cards.size(); i++) {
      T card = cards.get(i);
      Image cardImage = card.getCardImage();
      ImageView cardImageView = new ImageView(cardImage);
      cardImageView.setFitWidth(100);
      cardImageView.setFitHeight(100);
      double offset = 80 * (i - (cards.size() - 1) / 2.0) - 50;
      cardImageView.layoutXProperty().bind(cardPane.widthProperty().divide(2.0).add(offset));
      cardImageView.setLayoutY(37.5);
      cardPane.getChildren().add(cardImageView);
    }
  }
}
