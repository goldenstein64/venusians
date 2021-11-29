package venusians.gui.main.windows;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.resourceMultiChoice.ResourceMultiChoiceArtist;
import venusians.gui.main.artists.resourceMultiChoice.ResourceMultiChoiceController;

public class ResourceAdditionWindow {

  private class TextFieldListener implements ChangeListener<String> {

    private ResourceCard card;
    private TextField field;

    public TextFieldListener(ResourceCard card, TextField field) {
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

      ResourceCardMap resourceHand = currentPlayer.getResourceHand();
      if (newIntValue < 0) {
        newIntValue = 0;
      } else if (!resourceHand.contains(card, newIntValue)) {
        newIntValue = resourceHand.get(card);
      }

      int actualSize =
        selectedCards.size() + newIntValue - selectedCards.get(card);
      int difference = cardsLeft - actualSize;

      if (difference < 0) {
        newIntValue += difference;
      }

      windowController
        .getCaptionLabel()
        .setText(
          String.format("Please choose %d more cards to add.", difference)
        );

      if (newIntValue == 0) {
        field.setText("");
      } else {
        field.setText(String.valueOf(newIntValue));
      }

      selectedCards.put(card, newIntValue);
    }
  }

  private Player currentPlayer;
  private ResourceCardMap selectedCards = new ResourceCardMap();
  private int cardsLeft;

  private MainGameController parentController;
  private ResourceMultiChoiceController windowController;

  public ResourceAdditionWindow(
    MainGameController parentController,
    int cardsToAdd
  ) {
    this.parentController = parentController;
    this.windowController = ResourceMultiChoiceArtist.render();
    this.currentPlayer = Players.getCurrentPlayer();
    this.cardsLeft = cardsToAdd;

    updatePlayer();

    HashMap<ResourceCard, TextField> fieldMap = windowController.getFieldMap();
    for (Entry<ResourceCard, TextField> entry : fieldMap.entrySet()) {
      ResourceCard card = entry.getKey();
      TextField field = entry.getValue();

      field.textProperty().addListener(new TextFieldListener(card, field));
    }

    windowController.getSubmitButton().setOnAction(this::onSubmit);
  }

  private void updatePlayer() {
    windowController
      .getCaptionLabel()
      .setText(String.format("Please choose %d more cards to add.", cardsLeft));

    HashMap<ResourceCard, TextField> fieldMap = windowController.getFieldMap();
    for (TextField field : fieldMap.values()) {
      field.setPromptText(String.format("0-%d", cardsLeft));
    }
  }

  private void onSubmit(ActionEvent event) {
    ResourceCardMap currentResourceHand = currentPlayer.getResourceHand();
    currentResourceHand.add(selectedCards);
    returnControlToParentController();
  }

  private void returnControlToParentController() {
    parentController.continueHandlingYearOfPlentyCard(this);
  }

  public ResourceMultiChoiceController getController() {
    return windowController;
  }
}
