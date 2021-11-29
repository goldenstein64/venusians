package venusians.gui.main.artists;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import venusians.data.board.Point;
import venusians.data.board.tiles.TileSlot;
import venusians.gui.main.HexTransform;

public class TileSlotArtist {

  public static final double TILE_SIZE = 100;
  public static final double TILE_ASPECT_RATIO = Math.sqrt(3) / 2;

  public static StackPane render(TileSlot subject) {
    Point guiPosition = HexTransform.hexToGuiSpace(subject.position);

    StackPane result = new StackPane();

    attachTileImageView(subject, result);

    if (subject.rollValue != -1) {
      attachRollValueLabel(subject, result);
    }

    result.setLayoutX(guiPosition.x - TILE_SIZE / 2);
    result.setLayoutY(guiPosition.y - TILE_SIZE / 2 + 7);
    result.setAlignment(Pos.CENTER);

    return result;
  }

  private static void attachTileImageView(TileSlot tile, StackPane parentPane) {
    Image tileImage = tile.kind.getTileImage();
    ImageView tileImageView = new ImageView(tileImage);
    fitImageViewToTile(tileImageView);
    parentPane.getChildren().add(tileImageView);
  }

  private static void fitImageViewToTile(ImageView imageView) {
    imageView.setFitWidth(TILE_SIZE);
    imageView.setFitHeight(TILE_SIZE * TILE_ASPECT_RATIO);
  }

  private static void attachRollValueLabel(
    TileSlot tile,
    StackPane parentPane
  ) {
    Circle rollValueShape = new Circle(15);
    rollValueShape.setFill(Color.WHITE);

    Label rollValueLabel = new Label(String.valueOf(tile.rollValue));

    parentPane.getChildren().addAll(rollValueShape, rollValueLabel);
  }
}
