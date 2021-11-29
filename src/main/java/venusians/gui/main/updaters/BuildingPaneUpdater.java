package venusians.gui.main.updaters;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import venusians.data.board.buildable.Building;
import venusians.gui.main.artists.BuildingArtist;
import venusians.util.DynamicUpdater;

public class BuildingPaneUpdater extends DynamicUpdater<Building> {

  private HashMap<Building, Node> buildingNodeMap = new HashMap<>();
  private Pane parentPane;

  public BuildingPaneUpdater(Pane parentPane) {
    this.parentPane = parentPane;
  }

  protected void onMissing(Building building) {
    Node render = BuildingArtist.render(building);

    buildingNodeMap.put(building, render);
    parentPane.getChildren().add(render);
  }

  protected void onExtra(Building building) {
    Node render = buildingNodeMap.get(building);
    if (render == null) return;

    buildingNodeMap.remove(building);
    parentPane.getChildren().remove(render);
  }
}
