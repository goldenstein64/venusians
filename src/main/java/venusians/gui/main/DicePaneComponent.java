package venusians.gui.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import venusians.data.DiceRoll;

public class DicePaneComponent {
  public static void rollDice(Pane parentPane, ExitCallback exitCallback) {
    // roll the dice first
    DiceRoll diceRoll = new DiceRoll(2);

    // add a rolling dice label
    StackPane dicePane = new StackPane();

    Rectangle backdrop = new Rectangle();
    backdrop.setFill(Color.BLACK);
    backdrop.setOpacity(0.75);
    backdrop.widthProperty().bind(parentPane.widthProperty());
    backdrop.heightProperty().bind(parentPane.heightProperty());

    Rectangle windowGraphic = new Rectangle(300, 200);
    windowGraphic.setArcWidth(20);
    windowGraphic.setArcHeight(20);
    windowGraphic.setFill(Color.GREY);

    VBox contentBox = new VBox();
    contentBox.setAlignment(Pos.CENTER);

    Label totalValueLabel = new Label(String.format("Total: %d", diceRoll.totalValue));
    totalValueLabel.setTextFill(Color.WHITE);

    HBox diceBox = new HBox();
    diceBox.setSpacing(20);
    diceBox.setPadding(new Insets(20));
    diceBox.setAlignment(Pos.CENTER);

    for (int i = 0; i < 2; i++) {
      Image dieImage = DiceRoll.getImage(diceRoll.values[i]);

      ImageView dieImageView = new ImageView(dieImage);

      dieImageView.setFitWidth(75);
      dieImageView.setFitHeight(75);

      diceBox.getChildren().add(dieImageView);
    }

    Button okButton = new Button("OK");

    okButton.setOnAction(event -> {
      parentPane.getChildren().remove(dicePane);
      exitCallback.execute(diceRoll.totalValue);
    });

    contentBox.getChildren().addAll(totalValueLabel, diceBox, okButton);

    dicePane.getChildren().addAll(backdrop, windowGraphic, contentBox);

    parentPane.getChildren().add(dicePane);
  }

  public interface ExitCallback {
    public void execute(int rollValue);
  }
}
