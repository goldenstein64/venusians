package venusians.gui.results;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.App;

public class ResultsController {

  @FXML
  private Label resultsLabel;

  @FXML
  private void initialize() {
    Player winner = Players.getCurrentPlayer();

    resultsLabel.setText(
      String.format("%s has won the game!", winner.getName())
    );
  }

  @FXML
  private void restart() throws IOException {
    App.setRoot("setup");
  }

  @FXML
  private void exit() {
    App.close();
  }
}
