package venusians.gui.main.windows;

import java.util.Map.Entry;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import venusians.data.board.tiles.AnyPort;
import venusians.data.board.tiles.PortKind;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Players;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.resourceChoice.ResourceChoiceArtist;
import venusians.gui.main.artists.resourceChoice.ResourceChoiceController;

public class PortTradeWindow {

  private Pane mainViewPane;
  private PortKind portKind;

  private ResourceChoiceController necessaryController;
  private ResourceChoiceController requestedController;

  private ResourceCard necessaryResource;
  private ResourceCard requestedResource;

  private MainGameController parentController;

  public PortTradeWindow(
    MainGameController parentController,
    PortKind portKind
  ) {
    this.parentController = parentController;
    this.mainViewPane = parentController.getMainViewPane();
    this.portKind = portKind;
    this.necessaryController = ResourceChoiceArtist.render();
    necessaryController
      .getCaptionLabel()
      .setText("Please choose a resource to give.");

    ResourceCardMap resourceHand = Players.getCurrentPlayer().getResourceHand();
    if (portKind instanceof AnyPort) {
      requestedController = ResourceChoiceArtist.render();
      requestedController
        .getCaptionLabel()
        .setText("Please choose a resource to take.");
    }

    for (Entry<ResourceCard, ImageView> entry : necessaryController
      .getButtonMap()
      .entrySet()) {
      ResourceCard card = entry.getKey();
      ImageView button = entry.getValue();

      if (resourceHand.contains(card, portKind.getPortNecessaryCount())) {
        button.setOnMouseClicked(event -> onNecessaryResourceChosen(card));
      } else {
        button.setOpacity(0.3);
      }
    }

    necessaryController
      .getCancelButton()
      .setOnAction(
        event -> {
          mainViewPane.getChildren().remove(necessaryController.getRootPane());
          returnControlWithoutSuggestions();
        }
      );

    mainViewPane.getChildren().add(necessaryController.getRootPane());
  }

  private void onNecessaryResourceChosen(ResourceCard chosenResource) {
    mainViewPane.getChildren().remove(necessaryController.getRootPane());
    this.necessaryResource = chosenResource;

    if (portKind instanceof AnyPort) {
      for (Entry<ResourceCard, ImageView> entry : requestedController
        .getButtonMap()
        .entrySet()) {
        ResourceCard card = entry.getKey();
        ImageView button = entry.getValue();

        button.setOnMouseClicked(event -> onRequestedResourceChosen(card));
      }

      requestedController
        .getCancelButton()
        .setOnAction(
          event -> {
            mainViewPane
              .getChildren()
              .remove(requestedController.getRootPane());
            returnControlWithoutSuggestions();
          }
        );

      mainViewPane.getChildren().add(requestedController.getRootPane());
    } else if (portKind instanceof ResourceCard) {
      onRequestedResourceChosen((ResourceCard) portKind);
    }
  }

  private void onRequestedResourceChosen(ResourceCard chosenResource) {
    this.requestedResource = chosenResource;
    mainViewPane.getChildren().remove(requestedController.getRootPane());
    returnControlWithSuggestions();
  }

  private void returnControlWithSuggestions() {
    parentController.continueHandlingTradingWithPort(
      portKind,
      necessaryResource,
      requestedResource
    );
  }

  private void returnControlWithoutSuggestions() {
    parentController.continueHandlingTradingWithPort();
  }
}
