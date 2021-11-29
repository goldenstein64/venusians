package venusians.gui.main.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.resourceMultiChoice.ResourceMultiChoiceArtist;
import venusians.gui.main.artists.resourceMultiChoice.ResourceMultiChoiceController;

public class ResourceRemovalWindow {

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

      int expectedSize = resourceHand.size() / 2;
      int actualSize =
        selectedCards.size() + newIntValue - selectedCards.get(card);
      int difference = expectedSize - actualSize;

      if (difference < 0) {
        newIntValue += difference;
      }

      windowController
        .getCaptionLabel()
        .setText(
          String.format(
            "%s, please choose %d more cards to remove.",
            currentPlayer.getName(),
            difference
          )
        );

      if (newIntValue == 0) {
        field.setText("");
      } else {
        field.setText(String.valueOf(newIntValue));
      }

      selectedCards.put(card, newIntValue);
    }
  }

  private ArrayList<Player> players;
  private Player currentPlayer;
  private int playerCounter = 0;
  private ResourceCardMap selectedCards;

  private MainGameController parentController;
  private ResourceMultiChoiceController windowController;

  public ResourceRemovalWindow(
    MainGameController parentController,
    ArrayList<Player> players
  ) {
    this.parentController = parentController;
    this.windowController = ResourceMultiChoiceArtist.render();
    this.players = players;

    updatePlayer();

    HashMap<ResourceCard, TextField> fieldMap = windowController.getFieldMap();
    for (Entry<ResourceCard, TextField> entry : fieldMap.entrySet()) {
      ResourceCard card = entry.getKey();
      TextField field = entry.getValue();

      field.textProperty().addListener(new TextFieldListener(card, field));
    }

    windowController.getSubmitButton().setOnAction(this::onSubmit);
  }

  private void onSubmit(ActionEvent event) {
    ResourceCardMap currentResourceHand = currentPlayer.getResourceHand();
    currentResourceHand.remove(selectedCards);

    playerCounter++;
    if (playerCounter < players.size()) {
      updatePlayer();
    } else {
      returnControlToParentController();
    }
  }

  private void returnControlToParentController() {
    parentController.handleSevenRollAfterRemovingResources(this);
  }

  private void updatePlayer() {
    this.currentPlayer = players.get(playerCounter);
    this.selectedCards = new ResourceCardMap();

    ResourceCardMap currentResourceHand = currentPlayer.getResourceHand();

    windowController
      .getCaptionLabel()
      .setText(
        String.format(
          "%s, please choose %d more cards to remove.",
          currentPlayer.getName(),
          currentResourceHand.size() / 2
        )
      );

    HashMap<ResourceCard, TextField> fieldMap = windowController.getFieldMap();
    for (Entry<ResourceCard, TextField> entry : fieldMap.entrySet()) {
      ResourceCard card = entry.getKey();
      TextField field = entry.getValue();

      field.setText("");
      field.setPromptText(String.format("0-%d", currentResourceHand.get(card)));
    }
  }

  public ResourceMultiChoiceController getController() {
    return windowController;
  }
}
