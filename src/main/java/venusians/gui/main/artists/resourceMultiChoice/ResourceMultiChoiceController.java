package venusians.gui.main.artists.resourceMultiChoice;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import venusians.data.cards.resource.ResourceCard;

public class ResourceMultiChoiceController {

  @FXML
  private StackPane rootPane;

  @FXML
  private Label captionLabel;

  @FXML
  private Button submitButton;

  @FXML
  private TextField brickField;

  @FXML
  private TextField oreField;

  @FXML
  private TextField wheatField;

  @FXML
  private TextField woodField;

  @FXML
  private TextField woolField;

  private HashMap<ResourceCard, TextField> fieldMap = new HashMap<>();

  @FXML
  private void initialize() {
    fieldMap.put(ResourceCard.BRICK, brickField);
    fieldMap.put(ResourceCard.ORE, oreField);
    fieldMap.put(ResourceCard.WHEAT, wheatField);
    fieldMap.put(ResourceCard.WOOD, woodField);
    fieldMap.put(ResourceCard.WOOL, woolField);
  }

  public StackPane getRootPane() {
    return rootPane;
  }

  public Label getCaptionLabel() {
    return captionLabel;
  }

  public Button getSubmitButton() {
    return submitButton;
  }

  public HashMap<ResourceCard, TextField> getFieldMap() {
    return fieldMap;
  }
}
