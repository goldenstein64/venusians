package venusians.gui.main.artists.tradeDraft;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class TradeDraftArtist {

  public static TradeDraftController render() {
    FXMLLoader fxmlLoader = new FXMLLoader(
      TradeDraftArtist.class.getResource("tradeDraft.fxml")
    );

    try {
      fxmlLoader.<StackPane>load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    TradeDraftController controller = fxmlLoader.<TradeDraftController>getController();

    return controller;
  }
}
