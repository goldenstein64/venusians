package venusians.gui.main.artists;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import venusians.data.cards.HasCard;
import venusians.data.cards.development.DevelopmentCard;
import venusians.gui.main.artists.developmentToolTip.DevelopmentToolTipArtist;

public class CardArtist {

  public static final double CARD_SIZE = 100;
  public static final double HALF_CARD_SIZE = CARD_SIZE / 2;

  public static <C extends HasCard> StackPane render(C subject) {
    Image cardImage = subject.getCardImage();

    StackPane result = new StackPane();

    ImageView image = new ImageView(cardImage);
    image.setFitWidth(CARD_SIZE);
    image.setFitHeight(CARD_SIZE);
    image.setLayoutY(0);
    result.getChildren().add(image);

    if (subject instanceof DevelopmentCard) {
      DevelopmentCard developmentCard = (DevelopmentCard) subject;
      bindDevelopmentToolTip(developmentCard, result);
    }

    return result;
  }

  private static void bindDevelopmentToolTip(
    DevelopmentCard subject,
    StackPane result
  ) {
    Node toolTip = DevelopmentToolTipArtist.render(subject);
    result.setOnMouseEntered(event -> result.getChildren().add(toolTip));
    result.setOnMouseExited(event -> result.getChildren().remove(toolTip));
  }
}
