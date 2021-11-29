package venusians.gui.main.subControllers;

import java.util.HashSet;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Road;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.main.HexTransform;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.RoadArtist;
import venusians.gui.main.updaters.RoadPaneUpdater;

public class RoadBuildingController {

  private int roadsLeft = 2;
  private Road suggestedRoad;

  private Player currentPlayer;

  private MainGameController parentController;
  private Pane mainViewPane;
  private RoadPaneUpdater roadPaneUpdater;

  private Point pressedPosition;
  private Point guiPressedCorner;
  private IntPoint hexPressedCorner;

  private Pane roadPane;
  private Line currentRoadGraphic;

  private boolean isBuilding;

  public RoadBuildingController(MainGameController parentController) {
    this.parentController = parentController;
    this.mainViewPane = parentController.getMainViewPane();
    this.roadPane = parentController.getRoadPane();
    this.roadPaneUpdater = parentController.getRoadPaneUpdater();
    this.currentPlayer = Players.getCurrentPlayer();
  }

  private void finishTurn() {
    isBuilding = false;
    roadPane.getChildren().remove(currentRoadGraphic);
    currentRoadGraphic = null;

    updatePreview();

    applySuggestions();

    roadsLeft--;
    if (roadsLeft <= 0) {
      returnControlToMain();
    }
  }

  private void applySuggestions() {
    Board.addRoad(suggestedRoad);
    roadPaneUpdater.update(Board.getRoads());
  }

  private void updatePreview() {
    if (suggestedRoad != null) {
      HashSet<Road> previewedRoads = new HashSet<>(Board.getRoads());
      previewedRoads.add(suggestedRoad);
      roadPaneUpdater.update(previewedRoads);
    } else {
      roadPaneUpdater.update(Board.getRoads());
    }
  }

  private void returnControlToMain() {
    unbindMouse();
    parentController.continueHandlingRoadBuildingCard();
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

    if (Board.positionIsNextToTile(hexPressedCorner)) {
      isBuilding = true;
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
    if (event.getButton() != MouseButton.PRIMARY) return;
    if (!isBuilding) return;
    Point mousePosition = new Point(event.getX(), event.getY());
    RoadArtist.reRender(currentRoadGraphic, guiPressedCorner, mousePosition);
  }

  private void onMouseReleased(MouseEvent event) {
    if (event.getButton() != MouseButton.PRIMARY) return;
    if (!isBuilding) return;
    Point mousePosition = new Point(event.getX(), event.getY());

    IntPoint hexMouseCorner = HexTransform.hexEstimateHexCorner(
      hexPressedCorner,
      HexTransform.guiToHexSpace(mousePosition)
    );

    if (Board.canBuildRoadAt(currentPlayer, hexPressedCorner, hexMouseCorner)) {
      suggestedRoad = new Road(currentPlayer, hexPressedCorner, hexMouseCorner);
      finishTurn();
    } else {
      isBuilding = false;
      roadPane.getChildren().remove(currentRoadGraphic);
      currentRoadGraphic = null;
    }
  }
}
