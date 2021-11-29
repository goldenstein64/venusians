package venusians.data.chat;

public class Message {

  public HasName author;
  public String content;

  public Message(HasName author, String content) {
    this.author = author;
    this.content = content;
  }
}
