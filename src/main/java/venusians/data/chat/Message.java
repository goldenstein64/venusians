package venusians.data.chat;

import venusians.data.players.Player;

public class Message {

  public Player author;
  public String text;

  public Message(Player author, String text) {
    this.author = author;
    this.text = text;
  }
}
