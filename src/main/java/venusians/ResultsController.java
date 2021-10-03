package venusians;

import java.io.IOException;
import javafx.fxml.FXML;

public class ResultsController {

  @FXML
  private void restart() throws IOException {
    App.setRoot("setup");
  }
}
