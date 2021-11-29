package venusians.gui.main.artists;

import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import venusians.data.board.Point;
import venusians.data.board.buildable.Building;
import venusians.gui.main.HexTransform;

public class BuildingArtist {

  public static final double BUILDING_SIZE = 35;
  private static final double HALF_BUILDING_SIZE = BUILDING_SIZE / 2;
  private static final ColorAdjust MONOCHROME = new ColorAdjust(0, -1, 0, 0);

  public static Node render(Building subject) {
    Image mapGraphic = subject.getImage();
    ImageView result = new ImageView(mapGraphic);
    result.setFitWidth(BUILDING_SIZE);
    result.setFitHeight(BUILDING_SIZE);

    ImageView clipView = new ImageView(mapGraphic);
    clipView.setFitWidth(BUILDING_SIZE);
    clipView.setFitHeight(BUILDING_SIZE);
    result.setClip(clipView);

    Point guiPosition = HexTransform.hexToGuiSpace(subject.getPosition());
    result.setLayoutX(guiPosition.x - HALF_BUILDING_SIZE);
    result.setLayoutY(guiPosition.y - HALF_BUILDING_SIZE);

    result.setEffect(
      new Blend(
        BlendMode.MULTIPLY,
        MONOCHROME,
        new ColorInput(
          0,
          0, // x, y
          mapGraphic.getWidth(),
          mapGraphic.getHeight(),
          subject.getOwner().getColor()
        )
      )
    );

    return result;
  }
}
