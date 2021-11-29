package venusians.gui.main.artists.tradeRequest;

import java.io.IOException;
import java.util.Map.Entry;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.resource.ResourceCard;

public class TradeRequestArtist {

  public static TradeRequestController render(TradeRequest subject) {
    FXMLLoader fxmlLoader = new FXMLLoader();

    fxmlLoader.setLocation(
      TradeRequestArtist.class.getResource("tradeRequest.fxml")
    );

    try {
      fxmlLoader.<StackPane>load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    TradeRequestController controller = fxmlLoader.<TradeRequestController>getController();

    controller.setTradeRequest(subject);

    for (Entry<ResourceCard, Label> entry : controller.giveLabelMap.entrySet()) {
      ResourceCard card = entry.getKey();
      Label label = entry.getValue();

      label.setText(String.valueOf(subject.necessaryResources.get(card)));
    }

    for (Entry<ResourceCard, Label> entry : controller.forLabelMap.entrySet()) {
      ResourceCard card = entry.getKey();
      Label label = entry.getValue();

      label.setText(String.valueOf(subject.requestedResources.get(card)));
    }

    return controller;
  }
}
