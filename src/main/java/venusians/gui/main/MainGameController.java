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
import javafx.scene.text.Font;
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

  private Image portConnectorImage = Images.load(MainGameController.class, "portConnector.png");

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

    attachTileImageView(tile, result);

    if (tile.rollValue != -1) {
      attachRollValueLabel(tile, result);
    }

    return result;
  }

  private void attachTileImageView(TileSlot tile, Pane parentPane) {
    Image tileImage = tile.kind.getTileImage();
    ImageView tileImageView = new ImageView(tileImage);
    fitImageViewToTile(tileImageView);
    parentPane.getChildren().add(tileImageView);
  }

  private void attachRollValueLabel(TileSlot tile, Pane parentPane) {
    Circle rollValueShape = new Circle(15);
    rollValueShape.setFill(Color.WHITE);

    Label rollValueLabel = new Label(String.valueOf(tile.rollValue));

    parentPane.getChildren().addAll(rollValueShape, rollValueLabel);
  }
  
  private void fitImageViewToTile(ImageView imageView) {
    imageView.setFitWidth(TILE_SIZE);
    imageView.setFitHeight(TILE_SIZE * TILE_ASPECT_RATIO);
  }

  private StackPane generatePort(PortSlot port) {
    StackPane result = new StackPane();
    fitPaneToTile(result);

    attachPortConnectorPane(port, result);
    
    attachPortImage(port, result);

    return result;
  }
  
  private void fitPaneToTile(Pane pane) {
    pane.setPrefWidth(TILE_SIZE);
    pane.setPrefHeight(TILE_SIZE * TILE_ASPECT_RATIO);
  }

  private void attachPortConnectorPane(PortSlot port, Pane parentPane) {
    StackPane result = new StackPane();
    result.setAlignment(Pos.TOP_CENTER);
    fitPaneToTile(result);

    ImageView portConnectorImageView = new ImageView(portConnectorImage);
    fitImageViewToTile(portConnectorImageView);

    Label tradeRatioLabel = new Label(
        String.format("%d:%d", port.kind.getPortNecessaryCount(), port.kind.getPortRequestedCount()));
    tradeRatioLabel.setLayoutY(TILE_SIZE / 2);
    tradeRatioLabel.setFont(new Font(8));
    tradeRatioLabel.setRotate(180);

    result.setRotate(60 * port.portDirection);
    result.getChildren().addAll(portConnectorImageView, tradeRatioLabel);

    parentPane.getChildren().add(result);
  }
  
  private void attachPortImage(PortSlot port, Pane parentPane) {
    Image portImage = port.kind.getPortImage();
    ImageView result = new ImageView(portImage);
    result.setFitWidth(TILE_SIZE * 0.6);
    result.setFitHeight(TILE_SIZE * 0.6);

    parentPane.getChildren().add(result);
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
