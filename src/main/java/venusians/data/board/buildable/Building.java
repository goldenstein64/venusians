package venusians.data.board.buildable;

import javafx.scene.image.Image;
import venusians.data.cards.resource.ResourceCardMap;

public interface Building extends Buildable {
	public ResourceCardMap getResources();

	public Image getMapGraphic();
}
