package venusians.gui.main;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import venusians.data.Game;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Building;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.PortKind;
import venusians.data.board.tiles.PortSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.development.DevelopmentCard;
import venusians.data.cards.development.DevelopmentCardList;
import venusians.data.cards.development.KnightCard;
import venusians.data.cards.development.MonopolyCard;
import venusians.data.cards.development.RoadBuildingCard;
import venusians.data.cards.development.VictoryPointCard;
import venusians.data.cards.development.YearOfPlentyCard;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.cards.special.LargestArmyCard;
import venusians.data.cards.special.LongestRoadCard;
import venusians.data.chat.Chat;
import venusians.data.chat.Message;
import venusians.data.chat.TradeOfferMessage;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;
import venusians.gui.ChangeListenerBuilder;
import venusians.gui.main.artists.PortSlotArtist;
import venusians.gui.main.artists.RobberArtist;
import venusians.gui.main.artists.TileSlotArtist;
import venusians.gui.main.artists.resourceChoice.ResourceChoiceArtist;
import venusians.gui.main.artists.resourceChoice.ResourceChoiceController;
import venusians.gui.main.artists.resourceMultiChoice.ResourceMultiChoiceController;
import venusians.gui.main.artists.tradeRequest.TradeRequestArtist;
import venusians.gui.main.artists.tradeRequest.TradeRequestController;
import venusians.gui.main.subControllers.BuildController;
import venusians.gui.main.subControllers.RoadBuildingController;
import venusians.gui.main.subControllers.RobberPlacementController;
import venusians.gui.main.subControllers.StartBuildController;
import venusians.gui.main.updaters.BuildingPaneUpdater;
import venusians.gui.main.updaters.CardUpdater;
import venusians.gui.main.updaters.ChatUpdater;
import venusians.gui.main.updaters.RoadPaneUpdater;
import venusians.gui.main.windows.DicePaneWindow;
import venusians.gui.main.windows.PortTradeWindow;
import venusians.gui.main.windows.ResourceAdditionWindow;
import venusians.gui.main.windows.ResourceRemovalWindow;
import venusians.gui.main.windows.SelfTradeWindow;
import venusians.gui.main.windows.TradeDraftWindow;

public class MainGameController {

  private SecureRandom rng = new SecureRandom();

  @FXML
  private Rectangle backdrop;

  @FXML
  private AnchorPane mapPane;

  @FXML
  private AnchorPane roadPane;

  @FXML
  private AnchorPane buildingPane;

  @FXML
  private Label currentPlayerLabel;

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
  private Button buildButton;

  @FXML
  private Button cancelBuildButton;

  @FXML
  private Button buyDevelopmentCardButton;

  @FXML
  private Button tradeButton;

  @FXML
  private Button selfTradeButton;

  @FXML
  private Label rollResultLabel;

  @FXML
  private Button endTurnButton;

  @FXML
  private ImageView largestArmyImage;

  @FXML
  private ImageView longestRoadImage;

  private ImageView robberImage;

  public static final double TILE_ASPECT_RATIO = Math.sqrt(3) / 2;

  public static final Color BUILD_MODE_COLOR = new Color(
    191 / 255.0,
    191 / 255.0,
    191 / 255.0,
    1
  );

  public static final Color ROBBER_PLACEMENT_COLOR = new Color(
    255 / 255.0,
    186 / 255.0,
    186 / 255.0,
    1
  );

  public static final double TILE_SIZE = 100;
  public static final double BUILDING_SIZE = 35;

  private BuildController currentBuildController;
  private Node currentTradeWindowRender;
  private BuildingPaneUpdater buildingPaneUpdater;
  private RoadPaneUpdater roadPaneUpdater;
  private CardUpdater<DevelopmentCard> developmentCardUpdater;
  private CardUpdater<ResourceCard> resourceCardUpdater;
  private ChatUpdater chatUpdater;
  private Rectangle frontdrop;

  private ButtonState currentButtonState = ButtonState.BUILD;

  private enum ButtonState {
    STARTING,
    DEFAULT,
    BUILD,
    BUILD_COUNTABLE,
    MODAL,
  }

  private void setButtonState(ButtonState newButtonState) {
    currentButtonState = newButtonState;
    switch (currentButtonState) {
      case STARTING:
        unbindMouse();
        endTurnButton.setDisable(true);
        buildButton.setDisable(true);
        cancelBuildButton.setVisible(false);
        buyDevelopmentCardButton.setDisable(true);
        tradeButton.setDisable(true);
        selfTradeButton.setDisable(true);

        diceRollButton.setDisable(false);
        break;
      case DEFAULT:
        bindMouse();
        diceRollButton.setDisable(true);
        cancelBuildButton.setVisible(false);

        endTurnButton.setDisable(false);
        buildButton.setDisable(false);
        buyDevelopmentCardButton.setDisable(false);
        tradeButton.setDisable(false);
        selfTradeButton.setDisable(false);
        break;
      case BUILD:
        unbindMouse();
        endTurnButton.setDisable(true);
        diceRollButton.setDisable(true);
        buyDevelopmentCardButton.setDisable(true);
        tradeButton.setDisable(true);
        selfTradeButton.setDisable(true);

        buildButton.setDisable(false);
        cancelBuildButton.setVisible(true);
        break;
      case BUILD_COUNTABLE:
      case MODAL:
        unbindMouse();
        endTurnButton.setDisable(true);
        diceRollButton.setDisable(true);
        buyDevelopmentCardButton.setDisable(true);
        tradeButton.setDisable(true);
        selfTradeButton.setDisable(true);
        buildButton.setDisable(true);
        cancelBuildButton.setVisible(false);
        break;
      default:
        throw new RuntimeException(
          String.format(
            "Unknown button state (%s)",
            currentButtonState != null ? currentButtonState.name() : "null"
          )
        );
    }
  }

  /**
   * Describes what happens when the controller is set up for the first time.
   */
  @FXML
  private void initialize() {
    buildingPaneUpdater = new BuildingPaneUpdater(buildingPane);
    roadPaneUpdater = new RoadPaneUpdater(roadPane);
    developmentCardUpdater = new CardUpdater<>(developmentPane);
    resourceCardUpdater = new CardUpdater<>(resourcePane);
    chatUpdater = new ChatUpdater(chatHistory, chatScrollPane, chatPrompt);

    Game.startGame();

    renderMap();
    renderRobber();

    initializePlayers();
    chatUpdater.bindSnapToBottom();

    setButtonState(ButtonState.DEFAULT);
    loadInterfaceFor(Players.getCurrentPlayer());
    placeStartingSettlements();
  }

  private void bindMouse() {
    mainViewPane.setOnMouseClicked(this::onMouseClicked);
  }

  private void unbindMouse() {
    mainViewPane.setOnMouseClicked(null);
  }

  private void onMouseClicked(MouseEvent event) {
    Point mousePosition = new Point(event.getX(), event.getY());
    IntPoint slotPosition = HexTransform.guiToHexSlot(mousePosition);

    MapSlot slot = Board.getSlotAt(slotPosition);
    if (slot instanceof PortSlot) {
      PortSlot portSlot = (PortSlot) slot;
      tryTradingWithPort(portSlot);
    }
  }

  public void addDebugResources() {
    for (Player player : Players.getPlayers()) {
      ResourceCardMap resourceHand = player.getResourceHand();
      for (ResourceCard card : ResourceCard.values()) {
        resourceHand.add(card, 5);
      }

      for (int i = 0; i < 5; i++) {
        resourceHand.add(DevelopmentCard.BLUEPRINT);
        player.pickDevelopmentCard();
      }
    }
  }

  private void placeStartingSettlements() {
    setButtonState(ButtonState.BUILD_COUNTABLE);
    backdrop.setFill(BUILD_MODE_COLOR);

    StartBuildController controller = new StartBuildController(this);
    controller.bindMouse();
  }

  public void finishPlacingStartingSettlements() {
    backdrop.setFill(Color.WHITE);

    for (Building building : Board.getBuildings()) {
      ResourceCardMap resources = building.getResources();
      building.getOwner().getResourceHand().add(resources);
    }

    loadCardsFor(Players.getCurrentPlayer());
    setButtonState(ButtonState.STARTING);
  }

  private void renderMap() {
    for (MapSlot mapSlot : Board.getMap()) {
      if (mapSlot == null) continue;

      StackPane slotPane;
      if (mapSlot instanceof TileSlot) {
        slotPane = TileSlotArtist.render((TileSlot) mapSlot);
      } else if (mapSlot instanceof PortSlot) {
        slotPane = PortSlotArtist.render((PortSlot) mapSlot);
      } else {
        throw new RuntimeException("MapSlot kind not found");
      }

      mapPane.getChildren().add(slotPane);
    }
  }

  @FXML
  private void tradeWithSelf() {
    setUpFrontdrop();
    setButtonState(ButtonState.MODAL);
    new SelfTradeWindow(this);
  }

  public void continueHandlingTradingWithSelf(
    ResourceCard necessaryResource,
    ResourceCard requestedResource
  ) {
    Player currentPlayer = Players.getCurrentPlayer();

    int necessaryCount = 4;
    int requestedCount = 1;

    ResourceCardMap resourceHand = currentPlayer.getResourceHand();
    resourceHand.remove(necessaryResource, necessaryCount);
    resourceHand.add(requestedResource, requestedCount);

    tearDownFrontdrop();
    loadCardsFor(currentPlayer);
    setButtonState(ButtonState.DEFAULT);
  }

  public void continueHandlingTradingWithSelf() {
    tearDownFrontdrop();
    setButtonState(ButtonState.DEFAULT);
  }

  private void tryTradingWithPort(PortSlot portSlot) {
    Player currentPlayer = Players.getCurrentPlayer();
    boolean canTrade = false;
    for (Building building : Board.getBuildingsAtPort(portSlot)) {
      if (building.getOwner() == currentPlayer) {
        canTrade = true;
        break;
      }
    }

    if (canTrade) {
      tradeWithPort(portSlot);
    }
  }

  private void tradeWithPort(PortSlot portSlot) {
    setUpFrontdrop();
    setButtonState(ButtonState.MODAL);
    new PortTradeWindow(this, portSlot.kind);
  }

  public void continueHandlingTradingWithPort(
    PortKind portKind,
    ResourceCard necessaryResource,
    ResourceCard requestedResource
  ) {
    Player currentPlayer = Players.getCurrentPlayer();

    int necessaryCount = portKind.getPortNecessaryCount();
    int requestedCount = portKind.getPortRequestedCount();

    ResourceCardMap resourceHand = currentPlayer.getResourceHand();
    resourceHand.remove(necessaryResource, necessaryCount);
    resourceHand.add(requestedResource, requestedCount);

    tearDownFrontdrop();
    loadCardsFor(currentPlayer);
    setButtonState(ButtonState.DEFAULT);
  }

  public void continueHandlingTradingWithPort() {
    tearDownFrontdrop();
    setButtonState(ButtonState.DEFAULT);
  }

  private void renderRobber() {
    if (robberImage == null) {
      robberImage = RobberArtist.render(Board.getRobberPosition());
      mapPane.getChildren().add(robberImage);
    } else {
      RobberArtist.reRender(robberImage, Board.getRobberPosition());
    }
  }

  private void initializePlayers() {
    Players.currentPlayerProperty().addListener(playerChangedListener);

    Player currentPlayer = Players.getCurrentPlayer();
    victoryPointsValueLabel
      .textProperty()
      .bind(currentPlayer.victoryPointsProperty().asString());
  }

  private ChangeListener<Player> playerChangedListener = ChangeListenerBuilder.from(
    this::onPlayerChanged
  );

  private void loadInterfaceFor(Player player) {
    currentPlayerLabel.setText(String.format("%s's Turn", player.getName()));
    largestArmyImage.setOpacity(
      LargestArmyCard.getCardOwner() == player ? 1 : 0.3
    );
    longestRoadImage.setOpacity(
      LongestRoadCard.getCardOwner() == player ? 1 : 0.3
    );

    loadCardsFor(player);
  }

  private void loadCardsFor(Player player) {
    DevelopmentCardList developmentHand = player.getDevelopmentHand();
    developmentCardUpdater.update(developmentHand);
    resourceCardUpdater.update(player.getResourceHand().toList());

    for (DevelopmentCard card : developmentHand) {
      Node render = developmentCardUpdater.getRenderForCard(card);
      render.setOnMouseClicked(event -> onDevelopmentCardClicked(card));
    }
  }

  private void onDevelopmentCardClicked(DevelopmentCard card) {
    Player cardOwner = card.getOwner();

    if (card instanceof KnightCard) {
      moveRobber();
    } else if (card instanceof MonopolyCard) {
      ResourceChoiceController choiceController = ResourceChoiceArtist.render();
      choiceController
        .getCaptionLabel()
        .setText("Please choose a resource to monopolize.");
      for (Entry<ResourceCard, ImageView> entry : choiceController
        .getButtonMap()
        .entrySet()) {
        ResourceCard chosenCard = entry.getKey();
        ImageView button = entry.getValue();

        button.setOnMouseClicked(
          event -> {
            mainViewPane.getChildren().remove(choiceController.getRootPane());
            tearDownFrontdrop();
            continueHandlingMonopolyCard(cardOwner, chosenCard);
          }
        );
      }

      setButtonState(ButtonState.MODAL);
      setUpFrontdrop();
      mainViewPane.getChildren().add(choiceController.getRootPane());
    } else if (card instanceof RoadBuildingCard) {
      backdrop.setFill(BUILD_MODE_COLOR);
      RoadBuildingController controller = new RoadBuildingController(this);
      setButtonState(ButtonState.BUILD_COUNTABLE);
      controller.bindMouse();
    } else if (card instanceof YearOfPlentyCard) {
      setButtonState(ButtonState.MODAL);
      setUpFrontdrop();
      ResourceAdditionWindow window = new ResourceAdditionWindow(this, 2);
      mainViewPane.getChildren().add(window.getController().getRootPane());
    } else if (card instanceof VictoryPointCard) {
      return;
    }

    cardOwner.useDevelopmentCard(card);
    if (Players.getCurrentPlayer() == cardOwner) {
      loadInterfaceFor(cardOwner);
    }
  }

  private void continueHandlingMonopolyCard(
    Player owner,
    ResourceCard chosenCard
  ) {
    ArrayList<Player> otherPlayers = new ArrayList<>(
      Arrays.asList(Players.getPlayers())
    );
    otherPlayers.remove(owner);

    ResourceCardMap ownerResourceHand = owner.getResourceHand();
    for (Player player : otherPlayers) {
      ResourceCardMap resourceHand = player.getResourceHand();
      int cardsTaken = Math.min(resourceHand.get(chosenCard), 2);

      resourceHand.remove(chosenCard, cardsTaken);
      ownerResourceHand.add(chosenCard, cardsTaken);
    }

    loadCardsFor(Players.getCurrentPlayer());
    setButtonState(ButtonState.DEFAULT);
  }

  public void continueHandlingRoadBuildingCard() {
    backdrop.setFill(Color.WHITE);
    setButtonState(ButtonState.DEFAULT);
  }

  public void continueHandlingYearOfPlentyCard(ResourceAdditionWindow window) {
    mainViewPane.getChildren().remove(window.getController().getRootPane());
    tearDownFrontdrop();
    loadCardsFor(Players.getCurrentPlayer());
    setButtonState(ButtonState.DEFAULT);
  }

  @FXML
  private void endGame() throws IOException {
    App.setRoot("results");
  }

  @FXML
  private void endTurn() {
    Players.nextTurn();
    setButtonState(ButtonState.STARTING);
  }

  @FXML
  private void toggleBuildMode() {
    setButtonState(
      currentButtonState == ButtonState.BUILD
        ? ButtonState.DEFAULT
        : ButtonState.BUILD
    );
    switch (currentButtonState) {
      case BUILD:
        enterBuildMode();
        break;
      default:
        exitBuildModeAndApplySuggestions();
        break;
    }
  }

  private void enterBuildMode() {
    currentBuildController = new BuildController(this);
    buildButton.setText("Finish Building");

    backdrop.setFill(BUILD_MODE_COLOR);

    currentBuildController.bindMouse();
  }

  private void exitBuildModeAndApplySuggestions() {
    exitBuildModeWithoutCleanup();
    currentBuildController.applySuggestions();
    currentBuildController = null;
    loadInterfaceFor(Players.getCurrentPlayer());
  }

  @FXML
  private void cancelBuildMode() {
    exitBuildModeWithoutCleanup();
    setButtonState(ButtonState.DEFAULT);
    currentBuildController.discardSuggestions();
    currentBuildController = null;
  }

  private void exitBuildModeWithoutCleanup() {
    buildButton.setText("Build");

    backdrop.setFill(Color.WHITE);

    currentBuildController.unbindMouse();
  }

  @FXML
  private void createTradeDraft() {
    TradeDraftWindow tradeWindow = new TradeDraftWindow();
    setUpTradeWindow(tradeWindow);
  }

  private void createTradeDraft(TradeRequest offer) {
    TradeDraftWindow tradeWindow = new TradeDraftWindow(offer);
    setUpTradeWindow(tradeWindow);
  }

  private void setUpTradeWindow(TradeDraftWindow tradeWindow) {
    setButtonState(ButtonState.MODAL);

    tradeWindow.setOnOfferCreated(this::onOfferCreated);
    tradeWindow.setOnOfferDiscarded(this::tearDownTradeWindow);

    setUpFrontdrop();

    currentTradeWindowRender = tradeWindow.getRender();
    mainViewPane.getChildren().add(currentTradeWindowRender);
  }

  private void setUpFrontdrop() {
    frontdrop = new Rectangle();
    frontdrop.setFill(Color.BLACK);
    frontdrop.setOpacity(0.75);
    frontdrop.widthProperty().bind(mainViewPane.widthProperty());
    frontdrop.heightProperty().bind(mainViewPane.heightProperty());
    mainViewPane.getChildren().add(frontdrop);
  }

  private void tearDownTradeWindow() {
    List<Node> mainViewChildren = mainViewPane.getChildren();

    tearDownFrontdrop();

    if (currentTradeWindowRender != null) {
      mainViewChildren.remove(currentTradeWindowRender);
      currentTradeWindowRender = null;
    }
    setButtonState(ButtonState.DEFAULT);
  }

  private void tearDownFrontdrop() {
    if (frontdrop != null) {
      mainViewPane.getChildren().remove(frontdrop);
      frontdrop = null;
    }
  }

  private void onOfferCreated(TradeRequest offer) {
    tearDownTradeWindow();

    TradeOfferMessage message = new TradeOfferMessage(
      Players.getCurrentPlayer()
    );

    Chat.add(message);
    Node messageRender = chatUpdater.getLastMessageRendered();
    messageRender.setOnMouseClicked(
      event -> {
        setButtonState(ButtonState.MODAL);
        TradeRequestController offerController = TradeRequestArtist.render(
          offer
        );
        setUpFrontdrop();
        StackPane offerPane = offerController.getRootPane();
        mainViewPane.getChildren().add(offerPane);
        offerController.setOnAcceptOffer(
          (TradeRequest eventOffer) -> {
            mainViewPane.getChildren().remove(offerPane);
            tearDownFrontdrop();
            setButtonState(ButtonState.DEFAULT);
            acceptTradeRequest(eventOffer);
            messageRender.setDisable(true);
            messageRender.setOnMouseClicked(null);
          }
        );
        offerController.setOnModifyOffer(
          (TradeRequest eventOffer) -> {
            mainViewPane.getChildren().remove(offerPane);
            tearDownFrontdrop();
            setButtonState(ButtonState.DEFAULT);
            createTradeDraft(eventOffer);
          }
        );
        offerController.setOnCancelOffer(
          (TradeRequest eventOffer) -> {
            mainViewPane.getChildren().remove(offerPane);
            tearDownFrontdrop();
            setButtonState(ButtonState.DEFAULT);
          }
        );
      }
    );
  }

  private void acceptTradeRequest(TradeRequest offer) {
    Player customer = Players.getCurrentPlayer();
    Player merchant = offer.merchant;

    merchant.tradeWith(customer, offer);
    resourceCardUpdater.update();
  }

  @FXML
  private void sayMessage() {
    String messageContent = chatPrompt.getText();
    if (messageContent.isEmpty()) return;

    Message message = new Message(Players.getCurrentPlayer(), messageContent);
    Chat.add(message);
  }

  @FXML
  private void rollDice() {
    setButtonState(ButtonState.MODAL);
    DicePaneWindow.rollDice(mainViewPane, this::onDicePaneExited);
  }

  private void onDicePaneExited(int rollValue) {
    rollResultLabel.setText(String.valueOf(rollValue));

    if (rollValue == 7) {
      handleSevenRollBeforeRemovingResources();
    } else {
      gatherResourcesForRollValue(rollValue);
      setButtonState(ButtonState.DEFAULT);
    }
  }

  private void handleSevenRollBeforeRemovingResources() {
    ArrayList<Player> victims = new ArrayList<>();
    for (Player player : Players.getPlayers()) {
      if (player.getResourceHand().size() > 7) {
        victims.add(player);
      }
    }

    if (!victims.isEmpty()) {
      ResourceRemovalWindow window = new ResourceRemovalWindow(this, victims);

      ResourceMultiChoiceController controller = window.getController();

      setButtonState(ButtonState.MODAL);
      setUpFrontdrop();
      mainViewPane.getChildren().add(controller.getRootPane());
    } else {
      moveRobber();
    }
  }

  public void handleSevenRollAfterRemovingResources(
    ResourceRemovalWindow window
  ) {
    ResourceMultiChoiceController controller = window.getController();
    mainViewPane.getChildren().remove(controller.getRootPane());
    tearDownFrontdrop();
    loadInterfaceFor(Players.getCurrentPlayer());
    moveRobber();
  }

  public void moveRobber() {
    RobberPlacementController robberController = new RobberPlacementController(
      this
    );
    backdrop.setFill(ROBBER_PLACEMENT_COLOR);

    setButtonState(ButtonState.BUILD_COUNTABLE);
    robberController.bindMouse();
  }

  public void finishMovingRobber() {
    backdrop.setFill(Color.WHITE);
    renderRobber();

    Player currentPlayer = Players.getCurrentPlayer();

    HashSet<Player> attackedPlayers = new HashSet<>();
    for (Building building : Board.getBuildingsNextToRobberPosition()) {
      Player buildingOwner = building.getOwner();
      if (buildingOwner != currentPlayer) {
        attackedPlayers.add(buildingOwner);
      }
    }

    ResourceCardMap currentResourceHand = currentPlayer.getResourceHand();
    for (Player player : attackedPlayers) {
      ResourceCardMap resourceHand = player.getResourceHand();
      int chosen = rng.nextInt(resourceHand.size());
      ResourceCard chosenCard = null;
      for (ResourceCard card : ResourceCard.values()) {
        chosen -= resourceHand.get(card);
        if (chosen < 0) {
          chosenCard = card;
          break;
        }
      }

      if (chosenCard == null) {
        throw new RuntimeException("chosenCard is null");
      }

      resourceHand.remove(chosenCard, 1);
      currentResourceHand.add(chosenCard, 1);
    }

    loadInterfaceFor(currentPlayer);
    setButtonState(ButtonState.DEFAULT);
  }

  private void gatherResourcesForRollValue(int rollValue) {
    for (TileSlot tile : Board.getTilesForRollValue(rollValue)) {
      if (
        !(tile.kind instanceof ResourceCard) ||
        Board.getRobberPosition().equals(tile.position)
      ) continue;

      ResourceCard resourceCard = (ResourceCard) tile.kind;

      for (Building building : Board.getBuildingsAroundTile(tile)) {
        building.getOwner().getResourceHand().add(resourceCard);
      }
    }
    loadCardsFor(Players.getCurrentPlayer());
  }

  @FXML
  private void buyDevelopmentCard() {
    Player currentPlayer = Players.getCurrentPlayer();
    if (currentPlayer.hasResourcesForDevelopmentCard()) {
      DevelopmentCard newCard = currentPlayer.pickDevelopmentCard();

      developmentCardUpdater.update(currentPlayer.getDevelopmentHand());
      resourceCardUpdater.update(currentPlayer.getResourceHand().toList());

      Node render = developmentCardUpdater.getRenderForCard(newCard);
      render.setOnMouseClicked(event -> onDevelopmentCardClicked(newCard));
    }
  }

  private void onPlayerChanged(Player oldPlayer, Player newPlayer) {
    victoryPointsValueLabel.textProperty().unbind();
    if (oldPlayer != null) {
      oldPlayer.victoryPointsProperty().removeListener(victoryPointsListener);
    }
    if (newPlayer != null) {
      victoryPointsValueLabel
        .textProperty()
        .bind(newPlayer.victoryPointsProperty().asString("Victory Points: %d"));
      newPlayer.victoryPointsProperty().addListener(victoryPointsListener);

      loadInterfaceFor(newPlayer);
    }
  }

  private ChangeListener<Number> victoryPointsListener = ChangeListenerBuilder.from(
    this::onVictoryPointsChanged
  );

  private void onVictoryPointsChanged(Number newVictoryPoints) {
    if (!(newVictoryPoints instanceof Integer)) {
      throw new RuntimeException("This value is not of type Integer");
    }

    int intVictoryPoints = (int) newVictoryPoints;

    if (intVictoryPoints >= 10) {
      try {
        endGame();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public Pane getRoadPane() {
    return roadPane;
  }

  public RoadPaneUpdater getRoadPaneUpdater() {
    return roadPaneUpdater;
  }

  public BuildingPaneUpdater getBuildingPaneUpdater() {
    return buildingPaneUpdater;
  }

  public CardUpdater<ResourceCard> getResourceCardUpdater() {
    return resourceCardUpdater;
  }

  public CardUpdater<DevelopmentCard> getDevelopmentCardUpdater() {
    return developmentCardUpdater;
  }

  public StackPane getMainViewPane() {
    return mainViewPane;
  }

  public Pane getResourcePane() {
    return resourcePane;
  }
}
