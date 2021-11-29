package venusians.gui.main.artists.resourceChoice;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class ResourceChoiceArtist {

  public static ResourceChoiceController render() {
    return render("");
  }

  public static ResourceChoiceController render(String caption) {
    FXMLLoader loader = new FXMLLoader(
      ResourceChoiceArtist.class.getResource("resourceChoice.fxml")
    );

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ResourceChoiceController result = loader.<ResourceChoiceController>getController();

    result.getCaptionLabel().setText(caption);

    return result;
  }
}
