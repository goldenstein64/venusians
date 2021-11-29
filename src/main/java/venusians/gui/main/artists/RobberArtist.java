package venusians.gui.main.artists;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.gui.main.HexTransform;
import venusians.util.Images;

public class RobberArtist {

  public static final int ROBBER_SIZE = 50;

  public static final Image ROBBER_IMAGE = Images.load(
    RobberArtist.class,
    "robber.png"
  );

  private static final ColorAdjust MONOCHROME = new ColorAdjust(0, -1, 0, 0);

  public static ImageView render(IntPoint position) {
    ImageView result = new ImageView(ROBBER_IMAGE);
    result.setFitWidth(ROBBER_SIZE);
    result.setFitHeight(ROBBER_SIZE);

    ImageView clipView = new ImageView(ROBBER_IMAGE);
    clipView.setFitWidth(ROBBER_SIZE);
    clipView.setFitHeight(ROBBER_SIZE);
    result.setClip(clipView);

    result.setEffect(
      new Blend(
        BlendMode.MULTIPLY,
        MONOCHROME,
        new ColorInput(
          0,
          0, // x, y
          ROBBER_SIZE,
          ROBBER_SIZE,
          new Color(127 / 255.0, 127 / 255.0, 127 / 255.0, 1)
        )
      )
    );

    reRender(result, position);

    return result;
  }

  public static void reRender(ImageView render, IntPoint position) {
    Point guiPosition = HexTransform.hexToGuiSpace(position);
    render.setLayoutX(guiPosition.x - ROBBER_SIZE / 2);
    render.setLayoutY(guiPosition.y - ROBBER_SIZE / 2);
  }
}
