package venusians.gui.main.updaters;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import venusians.data.cards.HasCard;
import venusians.gui.ChangeListenerBuilder;
import venusians.gui.main.artists.CardArtist;

public class CardUpdater<C extends HasCard> {

  private Collection<C> currentCards;
  private HashMap<C, StackPane> cardRenderMap = new HashMap<>();
  private Pane parentPane;

  public CardUpdater(Pane parentPane) {
    this.parentPane = parentPane;
    bindAlignment();
  }

  public void update(Collection<C> cards) {
    this.currentCards = cards;
    update();
  }

  public void update() {
    List<Node> paneChildren = parentPane.getChildren();
    paneChildren.clear();
    cardRenderMap.clear();
    for (C card : currentCards) {
      StackPane render = CardArtist.render(card);
      cardRenderMap.put(card, render);
      paneChildren.add(render);
    }
    setAlignment();
  }

  public void addCard(C card) {
    currentCards.add(card);
  }

  public void addAllCards(Collection<? extends C> cards) {
    currentCards.addAll(cards);
  }

  public void bindAlignment() {
    parentPane
      .widthProperty()
      .addListener(ChangeListenerBuilder.from(this::setAlignment));
  }

  public void setAlignment() {
    List<Node> children = parentPane.getChildren();

    // if there are enough cards to overflow the pane, make the offset per card
    // fit within that width.
    double offsetPerCard = Math.min(
      80,
      (parentPane.getWidth() - CardArtist.CARD_SIZE) / children.size()
    );

    double offsetHalf = children.size() / 2.0;
    double middleOffset = parentPane.getWidth() / 2.0;
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      double offset = offsetPerCard * (i - offsetHalf) + middleOffset;
      if (child instanceof StackPane) {
        StackPane stackPane = (StackPane) child;
        stackPane
          .layoutXProperty()
          .bind(stackPane.widthProperty().negate().divide(2).add(offset));
      } else {
        child.setLayoutX(offset);
      }
    }
  }

  public StackPane getRenderForCard(C card) {
    return cardRenderMap.get(card);
  }
}
