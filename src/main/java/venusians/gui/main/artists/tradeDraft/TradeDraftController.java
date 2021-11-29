package venusians.gui.main.artists.tradeDraft;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import venusians.data.cards.resource.ResourceCard;

public class TradeDraftController {

  @FXML
  private Button cancelButton;

  @FXML
  private Button createOfferButton;

  @FXML
  private TextField forBrickField;

  @FXML
  private TextField forOreField;

  @FXML
  private TextField forWheatField;

  @FXML
  private TextField forWoodField;

  @FXML
  private TextField forWoolField;

  @FXML
  private TextField giveBrickField;

  @FXML
  private TextField giveOreField;

  @FXML
  private TextField giveWheatField;

  @FXML
  private TextField giveWoodField;

  @FXML
  private TextField giveWoolField;

  @FXML
  private StackPane render;

  public HashMap<ResourceCard, TextField> giveFieldMap = new HashMap<>();
  public HashMap<ResourceCard, TextField> forFieldMap = new HashMap<>();

  @FXML
  private void initialize() {
    giveFieldMap.put(ResourceCard.BRICK, giveBrickField);
    giveFieldMap.put(ResourceCard.ORE, giveOreField);
    giveFieldMap.put(ResourceCard.WHEAT, giveWheatField);
    giveFieldMap.put(ResourceCard.WOOD, giveWoodField);
    giveFieldMap.put(ResourceCard.WOOL, giveWoolField);

    forFieldMap.put(ResourceCard.BRICK, forBrickField);
    forFieldMap.put(ResourceCard.ORE, forOreField);
    forFieldMap.put(ResourceCard.WHEAT, forWheatField);
    forFieldMap.put(ResourceCard.WOOD, forWoodField);
    forFieldMap.put(ResourceCard.WOOL, forWoolField);
  }

  public StackPane getRender() {
    return render;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public Button getCreateOfferButton() {
    return createOfferButton;
  }
}
