package venusians.gui.main.artists.tradeRequest;

import java.util.HashMap;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.resource.ResourceCard;

public class TradeRequestController {

  @FXML
  private Label forBrickLabel;

  @FXML
  private Label forOreLabel;

  @FXML
  private Label forWheatLabel;

  @FXML
  private Label forWoodLabel;

  @FXML
  private Label forWoolLabel;

  @FXML
  private Label giveBrickLabel;

  @FXML
  private Label giveOreLabel;

  @FXML
  private Label giveWheatLabel;

  @FXML
  private Label giveWoodLabel;

  @FXML
  private Label giveWoolLabel;

  @FXML
  private Button acceptButton;

  @FXML
  private Button modifyButton;

  @FXML
  private Button cancelButton;

  @FXML
  private Label authorLabel;

  @FXML
  private StackPane rootPane;

  public HashMap<ResourceCard, Label> forLabelMap = new HashMap<>();
  public HashMap<ResourceCard, Label> giveLabelMap = new HashMap<>();

  private TradeRequest tradeRequest;

  private Consumer<TradeRequest> onAcceptOffer;
  private Consumer<TradeRequest> onModifyOffer;
  private Consumer<TradeRequest> onCancelOffer;

  @FXML
  private void initialize() {
    forLabelMap.put(ResourceCard.BRICK, forBrickLabel);
    forLabelMap.put(ResourceCard.ORE, forOreLabel);
    forLabelMap.put(ResourceCard.WHEAT, forWheatLabel);
    forLabelMap.put(ResourceCard.WOOD, forWoodLabel);
    forLabelMap.put(ResourceCard.WOOL, forWoolLabel);

    giveLabelMap.put(ResourceCard.BRICK, giveBrickLabel);
    giveLabelMap.put(ResourceCard.ORE, giveOreLabel);
    giveLabelMap.put(ResourceCard.WHEAT, giveWheatLabel);
    giveLabelMap.put(ResourceCard.WOOD, giveWoodLabel);
    giveLabelMap.put(ResourceCard.WOOL, giveWoolLabel);
  }

  @FXML
  private void acceptOffer() {
    if (onAcceptOffer != null) {
      onAcceptOffer.accept(tradeRequest);
    }
  }

  @FXML
  private void modifyOffer() {
    if (onModifyOffer != null) {
      onModifyOffer.accept(tradeRequest);
    }
  }

  @FXML
  private void cancelOffer() {
    if (onCancelOffer != null) {
      onCancelOffer.accept(tradeRequest);
    }
  }

  public StackPane getRootPane() {
    return rootPane;
  }

  public Label getAuthorLabel() {
    return authorLabel;
  }

  public void setTradeRequest(TradeRequest tradeRequest) {
    this.tradeRequest = tradeRequest;
  }

  public void setOnAcceptOffer(Consumer<TradeRequest> onAcceptOffer) {
    this.onAcceptOffer = onAcceptOffer;
  }

  public void setOnModifyOffer(Consumer<TradeRequest> onModifyOffer) {
    this.onModifyOffer = onModifyOffer;
  }

  public void setOnCancelOffer(Consumer<TradeRequest> onCancelOffer) {
    this.onCancelOffer = onCancelOffer;
  }
}
