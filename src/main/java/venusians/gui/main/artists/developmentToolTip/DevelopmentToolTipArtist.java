package venusians.gui.main.artists.developmentToolTip;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import venusians.data.cards.development.DevelopmentCard;

public class DevelopmentToolTipArtist {

  public static AnchorPane render(DevelopmentCard subject) {
    FXMLLoader toolTipLoader = new FXMLLoader(
      DevelopmentToolTipArtist.class.getResource("developmentToolTip.fxml")
    );

    DevelopmentToolTipController controller;
    try {
      toolTipLoader.load();
      controller = toolTipLoader.getController();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    controller.getNameLabel().setText(subject.getName());
    controller.getDescriptionLabel().setText(subject.getDescription());

    return controller.getRootPane();
  }
}
