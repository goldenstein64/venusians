package venusians.gui.main.artists.resourceMultiChoice;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class ResourceMultiChoiceArtist {

  public static ResourceMultiChoiceController render() {
    FXMLLoader loader = new FXMLLoader(
      ResourceMultiChoiceArtist.class.getResource("resourceMultiChoice.fxml")
    );

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ResourceMultiChoiceController result = loader.<ResourceMultiChoiceController>getController();

    return result;
  }
}
