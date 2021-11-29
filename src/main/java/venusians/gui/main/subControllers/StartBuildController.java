package venusians.gui.main.subControllers;

import java.util.HashSet;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.Settlement;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.main.HexTransform;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.RoadArtist;
import venusians.gui.main.updaters.BuildingPaneUpdater;
import venusians.gui.main.updaters.RoadPaneUpdater;

/**
 * Controls how the player interacts with the Board when building starting Settlements.
 */
public class StartBuildController {

  private MainGameController parentController;
  private Player[] playerTurns;
  private Player currentPlayer;
  private Player oldPlayer;

  private Line currentRoadGraphic;

  private Settlement suggestedSettlement;
  private Road suggestedRoad;

  private Point pressedPosition;
  private IntPoint hexPressedCorner;
  private Point guiPressedCorner;

  private Pane mainViewPane;
  private Pane roadPane;

  private BuildingPaneUpdater buildingPaneUpdater;
  private RoadPaneUpdater roadPaneUpdater;

  private int turnCounter = 0;

  private boolean isBuilding = false;

  public StartBuildController(MainGameController parentController) {
    this.parentController = parentController;
    this.mainViewPane = parentController.getMainViewPane();
    this.roadPaneUpdater = parentController.getRoadPaneUpdater();
    this.buildingPaneUpdater = parentController.getBuildingPaneUpdater();
    this.roadPane = parentController.getRoadPane();

    playerTurns = new Player[2 * Players.getPlayerCount()];
    int i = 0;
    for (Player player : Players.getPlayers()) {
      playerTurns[i] = player;
      playerTurns[playerTurns.length - i - 1] = player;
      i++;
    }

    currentPlayer = playerTurns[0];
    oldPlayer = Players.getCurrentPlayer();
    Players.setCurrentPlayer(currentPlayer);
  }

  private void finishTurn() {
    isBuilding = false;
    roadPane.getChildren().remove(currentRoadGraphic);
    currentRoadGraphic = null;

    updatePreview();

    applySuggestions();

    turnCounter++;
    if (turnCounter < playerTurns.length) {
      currentPlayer = playerTurns[turnCounter];
      Players.setCurrentPlayer(currentPlayer);
    } else {
      returnControlToMain();
    }
  }

  private void applySuggestions() {
    Board.addBuilding(suggestedSettlement);
    Board.addRoad(suggestedRoad);

    buildingPaneUpdater.update(Board.getBuildings());
    roadPaneUpdater.update(Board.getRoads());
  }

  private void updatePreview() {
    if (suggestedSettlement != null) {
      HashSet<Building> previewedBuildings = new HashSet<>(
        Board.getBuildings()
      );
      previewedBuildings.add(suggestedSettlement);
      buildingPaneUpdater.update(previewedBuildings);
    } else {
      buildingPaneUpdater.update(Board.getBuildings());
    }

    if (suggestedRoad != null) {
      HashSet<Road> previewedRoads = new HashSet<>(Board.getRoads());
      previewedRoads.add(suggestedRoad);
      roadPaneUpdater.update(previewedRoads);
    } else {
      roadPaneUpdater.update(Board.getRoads());
    }
  }

  private void returnControlToMain() {
    Players.setCurrentPlayer(oldPlayer);
    unbindMouse();
    parentController.finishPlacingStartingSettlements();
  }

  public void bindMouse() {
    mainViewPane.setOnMousePressed(this::onMousePressed);
    mainViewPane.setOnMouseDragged(this::onMouseDragged);
    mainViewPane.setOnMouseReleased(this::onMouseReleased);
  }

  private void unbindMouse() {
    mainViewPane.setOnMousePressed(null);
    mainViewPane.setOnMouseDragged(null);
    mainViewPane.setOnMouseReleased(null);
  }

  private void onMousePressed(MouseEvent event) {
    if (event.getButton() != MouseButton.PRIMARY) return;
    pressedPosition = new Point(event.getX(), event.getY());
    guiPressedCorner = HexTransform.guiToGuiCorner(pressedPosition);
    hexPressedCorner = HexTransform.guiToHexCorner(pressedPosition);

    if (
      Board.positionIsNextToTile(hexPressedCorner) &&
      !Board.positionIsNextToBuilding(hexPressedCorner)
    ) {
      isBuilding = true;
      suggestedSettlement = new Settlement(currentPlayer, hexPressedCorner);
      currentRoadGraphic =
        RoadArtist.render(
          guiPressedCorner,
          pressedPosition,
          currentPlayer.getColor()
        );
      roadPane.getChildren().add(currentRoadGraphic);
      updatePreview();
    }
  }

  private void onMouseDragged(MouseEvent event) {
    if (event.getButton() != MouseButton.PRIMARY || !isBuilding) return;
    Point mousePosition = new Point(event.getX(), event.getY());
    RoadArtist.reRender(currentRoadGraphic, guiPressedCorner, mousePosition);
  }

  private void onMouseReleased(MouseEvent event) {
    if (event.getButton() != MouseButton.PRIMARY || !isBuilding) return;
    Point mousePosition = new Point(event.getX(), event.getY());
    IntPoint hexMouseCorner = HexTransform.hexEstimateHexCorner(
      hexPressedCorner,
      HexTransform.guiToHexSpace(mousePosition)
    );

    if (
      Board.positionIsNextToTile(hexPressedCorner) &&
      Board.positionIsNextToTile(hexMouseCorner)
    ) {
      suggestedRoad = new Road(currentPlayer, hexPressedCorner, hexMouseCorner);
      currentPlayer.addVictoryPoints(1);
      finishTurn();
    } else {
      isBuilding = false;

      roadPane.getChildren().remove(currentRoadGraphic);
      currentRoadGraphic = null;

      suggestedSettlement = null;
      updatePreview();
    }
  }
}
