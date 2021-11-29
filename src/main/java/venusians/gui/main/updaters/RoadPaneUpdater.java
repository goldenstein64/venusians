package venusians.gui.main.updaters;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import venusians.data.board.buildable.Road;
import venusians.gui.main.artists.RoadArtist;
import venusians.util.DynamicUpdater;

public class RoadPaneUpdater extends DynamicUpdater<Road> {

  private HashMap<Road, Node> roadNodeMap = new HashMap<>();
  private Pane parentPane;

  public RoadPaneUpdater(Pane parentPane) {
    this.parentPane = parentPane;
  }

  protected void onMissing(Road road) {
    Node render = RoadArtist.render(road);

    roadNodeMap.put(road, render);
    parentPane.getChildren().add(render);
  }

  protected void onExtra(Road road) {
    Node render = roadNodeMap.get(road);
    if (render == null) return;

    roadNodeMap.remove(road);
    parentPane.getChildren().remove(render);
  }
}
