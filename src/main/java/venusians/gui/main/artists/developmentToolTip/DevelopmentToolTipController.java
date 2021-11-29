package venusians.gui.main.artists.developmentToolTip;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DevelopmentToolTipController {

  @FXML
  private AnchorPane rootPane;

  @FXML
  private Label nameLabel;

  @FXML
  private Label descriptionLabel;

  public AnchorPane getRootPane() {
    return rootPane;
  }

  public Label getNameLabel() {
    return nameLabel;
  }

  public Label getDescriptionLabel() {
    return descriptionLabel;
  }
}
