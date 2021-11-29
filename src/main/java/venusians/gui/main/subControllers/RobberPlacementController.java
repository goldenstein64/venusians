package venusians.gui.main.subControllers;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.gui.main.HexTransform;
import venusians.gui.main.MainGameController;

public class RobberPlacementController {

  private MainGameController parentController;
  private StackPane mainViewPane;

  public RobberPlacementController(MainGameController parentController) {
    this.parentController = parentController;
    this.mainViewPane = parentController.getMainViewPane();
  }

  public void bindMouse() {
    mainViewPane.setOnMouseClicked(this::onMouseClicked);
  }

  private void unbindMouse() {
    mainViewPane.setOnMouseClicked(null);
  }

  private void returnControlToMain() {
    unbindMouse();
    parentController.finishMovingRobber();
  }

  private void onMouseClicked(MouseEvent event) {
    Point position = new Point(event.getX(), event.getY());
    IntPoint hexPosition = HexTransform.guiToHexSlot(position);

    MapSlot mapSlot = Board.getSlotAt(hexPosition);
    if (mapSlot != null && mapSlot instanceof TileSlot) {
      Board.setRobberPosition(hexPosition);
      returnControlToMain();
    }
  }
}
