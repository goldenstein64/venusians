package venusians.gui.main.artists;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import venusians.data.board.Point;
import venusians.data.board.tiles.PortSlot;
import venusians.gui.main.HexTransform;
import venusians.util.Images;

public class PortSlotArtist {

  public static final double TILE_SIZE = TileSlotArtist.TILE_SIZE;
  public static final double TILE_ASPECT_RATIO =
    TileSlotArtist.TILE_ASPECT_RATIO;

  private static final Image portConnectorImage = Images.load(
    PortSlotArtist.class,
    "portConnector.png"
  );

  public static StackPane render(PortSlot subject) {
    Point guiPosition = HexTransform.hexToGuiSpace(subject.position);

    StackPane rootPane = new StackPane();
    fitPaneToTile(rootPane);

    StackPane portConnector = renderPortConnector(subject);
    ImageView portImage = renderPortImage(subject);

    rootPane.setLayoutX(guiPosition.x - TILE_SIZE / 2);
    rootPane.setLayoutY(guiPosition.y - TILE_SIZE / 2 + 7);
    rootPane.setAlignment(Pos.CENTER);

    rootPane.getChildren().addAll(portConnector, portImage);

    return rootPane;
  }

  private static void fitPaneToTile(Pane pane) {
    pane.setPrefWidth(TILE_SIZE);
    pane.setPrefHeight(TILE_SIZE * TILE_ASPECT_RATIO);
  }

  private static StackPane renderPortConnector(PortSlot subject) {
    StackPane result = new StackPane();
    result.setAlignment(Pos.TOP_CENTER);
    fitPaneToTile(result);

    ImageView portConnectorImageView = new ImageView(portConnectorImage);
    fitImageViewToTile(portConnectorImageView);

    Label tradeRatioLabel = new Label(
      String.format(
        "%d:%d",
        subject.kind.getPortNecessaryCount(),
        subject.kind.getPortRequestedCount()
      )
    );
    tradeRatioLabel.setLayoutY(TILE_SIZE / 2);
    tradeRatioLabel.setFont(new Font("System Bold", 8));
    tradeRatioLabel.setTextFill(Color.WHITE);
    tradeRatioLabel.setRotate(180);

    result.setRotate(60 * subject.portDirection);
    result.getChildren().addAll(portConnectorImageView, tradeRatioLabel);

    return result;
  }

  private static ImageView renderPortImage(PortSlot subject) {
    Image portImage = subject.kind.getPortImage();
    ImageView result = new ImageView(portImage);
    result.setFitWidth(TILE_SIZE * 0.6);
    result.setFitHeight(TILE_SIZE * 0.6);

    return result;
  }

  private static void fitImageViewToTile(ImageView imageView) {
    imageView.setFitWidth(TILE_SIZE);
    imageView.setFitHeight(TILE_SIZE * TILE_ASPECT_RATIO);
  }
}
