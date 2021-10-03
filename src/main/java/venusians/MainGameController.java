package venusians;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainGameController {

  @FXML
  private void gameEnded() throws IOException {
    App.setRoot("results");
  }
}
