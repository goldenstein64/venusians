package venusians.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

  private static Scene scene;
  private static Stage stage;

  /** Describes what happens when the window is first initialized. */
  @Override
  public void start(Stage stage) throws IOException {
    App.stage = stage;
    scene = new Scene(loadFXML("setup"));
    stage.setScene(scene);
    stage.setTitle("The Venusians");
    stage.show();
  }

  /** Sets a new scene with the given file name */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
      App.class.getResource(fxml + ".fxml")
    );
    return fxmlLoader.<Parent>load();
  }

  /** Closes the window and exits the program */
  public static void close() {
    stage.close();
  }

  /**
   * Provides the main entry point into the program.
   * @param args Arguments passed into the program's invocation.
   */
  public static void main(String[] args) {
    launch();
  }
}
