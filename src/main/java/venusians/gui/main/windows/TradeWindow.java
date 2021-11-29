package venusians.gui.main.windows;

import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.main.artists.tradeDraft.TradeDraftArtist;
import venusians.gui.main.artists.tradeDraft.TradeDraftController;

public class TradeWindow {

  private class GiveTextFieldListener implements ChangeListener<String> {

    private ResourceCard card;
    private TextField field;

    public GiveTextFieldListener(ResourceCard card, TextField field) {
      this.card = card;
      this.field = field;
    }

    public void changed(
      ObservableValue<? extends String> value,
      String oldValue,
      String newValue
    ) {
      int newIntValue;
      if (newValue.isEmpty()) {
        newIntValue = 0;
      } else {
        try {
          newIntValue = Integer.valueOf(newValue);
        } catch (NumberFormatException e) {
          field.setText(oldValue);
          return;
        }
      }

      tradeRequest.requestedResources.put(card, newIntValue);
    }
  }

  private class ForTextFieldListener implements ChangeListener<String> {

    private ResourceCard card;
    private TextField field;

    public ForTextFieldListener(ResourceCard card, TextField field) {
      this.card = card;
      this.field = field;
    }

    public void changed(
      ObservableValue<? extends String> value,
      String oldValue,
      String newValue
    ) {
      int newIntValue;
      if (newValue.isEmpty()) {
        newIntValue = 0;
      } else {
        try {
          newIntValue = Integer.valueOf(newValue);
        } catch (NumberFormatException e) {
          field.setText(oldValue);
          return;
        }

        ResourceCardMap resourceHand = currentPlayer.getResourceHand();
        if (!resourceHand.contains(card, newIntValue)) {
          field.setText(oldValue);
          return;
        }
      }

      tradeRequest.necessaryResources.put(card, newIntValue);
    }
  }

  private Button cancelButton;
  private Player currentPlayer;
  private TradeRequest tradeRequest;

  private Consumer<TradeRequest> onOfferCreated;
  private Runnable onOfferDiscarded;
  private StackPane tradePane;

  public TradeWindow() {
    this.currentPlayer = Players.getCurrentPlayer();
    this.tradeRequest = new TradeRequest(currentPlayer);

    TradeDraftController controller = TradeDraftArtist.render();

    fillListeners(controller);

    this.tradePane = controller.getRender();

    controller.getCancelButton().setOnAction(this::cancelOffer);
    controller.getCreateOfferButton().setOnAction(this::createOffer);
  }

  public TradeWindow(TradeRequest oldOffer) {
    this();
    tradeRequest.setNecessaryResources(oldOffer.getRequestedResources());
    tradeRequest.setRequestedResources(oldOffer.getNecessaryResources());
  }

  private void fillListeners(TradeDraftController controller) {
    for (ResourceCard card : ResourceCard.values()) {
      TextField giveField = controller.giveFieldMap.get(card);
      ChangeListener<String> giveListener = new GiveTextFieldListener(
        card,
        giveField
      );
      giveField.textProperty().addListener(giveListener);

      TextField forField = controller.forFieldMap.get(card);
      ChangeListener<String> forListener = new ForTextFieldListener(
        card,
        forField
      );
      forField.textProperty().addListener(forListener);
    }
  }

  private void createOffer(ActionEvent event) {
    if (this.onOfferCreated != null) {
      this.onOfferCreated.accept(tradeRequest);
    }
  }

  private void cancelOffer(ActionEvent event) {
    if (this.onOfferDiscarded != null) {
      this.onOfferDiscarded.run();
    }
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public StackPane getRender() {
    return tradePane;
  }

  public void setOnOfferCreated(Consumer<TradeRequest> onOfferCreated) {
    this.onOfferCreated = onOfferCreated;
  }

  public void setOnOfferDiscarded(Runnable onOfferDiscarded) {
    this.onOfferDiscarded = onOfferDiscarded;
  }
}
