package venusians.gui.main.artists.resourceChoice;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import venusians.data.cards.resource.ResourceCard;

public class ResourceChoiceController {

  @FXML
  private Label captionLabel;

  @FXML
  private Button cancelButton;

  @FXML
  private ImageView brickButton;

  @FXML
  private ImageView oreButton;

  @FXML
  private StackPane rootPane;

  @FXML
  private ImageView wheatButton;

  @FXML
  private ImageView woodButton;

  @FXML
  private ImageView woolButton;

  private HashMap<ResourceCard, ImageView> buttonMap = new HashMap<>();

  @FXML
  private void initialize() {
    buttonMap.put(ResourceCard.BRICK, brickButton);
    buttonMap.put(ResourceCard.ORE, oreButton);
    buttonMap.put(ResourceCard.WHEAT, wheatButton);
    buttonMap.put(ResourceCard.WOOD, woodButton);
    buttonMap.put(ResourceCard.WOOL, woolButton);
  }

  public HashMap<ResourceCard, ImageView> getButtonMap() {
    return buttonMap;
  }

  public StackPane getRootPane() {
    return rootPane;
  }

  public Label getCaptionLabel() {
    return captionLabel;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public ImageView getBrickButton() {
    return brickButton;
  }

  public ImageView getOreButton() {
    return oreButton;
  }

  public ImageView getWheatButton() {
    return wheatButton;
  }

  public ImageView getWoodButton() {
    return woodButton;
  }

  public ImageView getWoolButton() {
    return woolButton;
  }
}
