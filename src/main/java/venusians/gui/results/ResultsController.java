package venusians.gui.results;

import java.io.IOException;
import javafx.fxml.FXML;
import venusians.gui.App;

public class ResultsController {

  @FXML
  private void restart() throws IOException {
    App.setRoot("setup");
  }
}
