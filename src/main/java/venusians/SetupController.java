package venusians;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import venusians.data.Game;
import venusians.data.lifecycle.GameOptions;
import venusians.data.lifecycle.PlayerProfile;
import venusians.gui.ColorTableCell;

public class SetupController {

  @FXML
  private TableView<PlayerProfile> playerProfileTable;

  @FXML
  private TableColumn<PlayerProfile, String> nameColumn;

  @FXML
  private TableColumn<PlayerProfile, Color> colorColumn;

  @FXML
  private CheckBox randomizeTilePositionsBox;

  @FXML
  private CheckBox randomizeRollValuesBox;

  private ObservableList<PlayerProfile> profileList;

  @FXML
  private void initialize() {
    nameColumn.setCellValueFactory(
      new PropertyValueFactory<PlayerProfile, String>("name")
    );
    nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    nameColumn.setOnEditCommit(
      event -> {
        PlayerProfile profile = event.getRowValue();
        profile.setName(event.getNewValue());
      }
    );

    colorColumn.setCellValueFactory(
      new PropertyValueFactory<PlayerProfile, Color>("color")
    );
    colorColumn.setCellFactory(
      column -> {
        return new ColorTableCell<PlayerProfile>(column);
      }
    );
    colorColumn.setOnEditCommit(
      event -> {
        PlayerProfile profile = event.getRowValue();
        profile.setColor(event.getNewValue());
      }
    );

    profileList =
      FXCollections.observableArrayList(
        new PlayerProfile("James", Color.WHITE)
      );
    playerProfileTable.setItems(profileList);
  }

  @FXML
  private void sayHello() throws IOException {
    System.out.println("Hello");
  }

  @FXML
  private void ready() throws IOException {
    GameOptions gameOptions = Game.getGameOptions();
    gameOptions.profiles = profileList;
    gameOptions.randomizeTilePositions = randomizeTilePositionsBox.isSelected();
    gameOptions.randomizeRollValues = randomizeRollValuesBox.isSelected();

    App.setRoot("mainGame");
  }
}
