package venusians.data.chat;

import venusians.data.players.Player;

public class TradeOfferMessage extends Message {

  public Player merchant;

  public TradeOfferMessage(Player merchant) {
    super(
      GameBroadcaster.INSTANCE,
      String.format(
        "%s has made an trade offer! Click here to view.",
        merchant.getName()
      )
    );
    this.merchant = merchant;
  }
}
