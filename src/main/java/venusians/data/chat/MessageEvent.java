package venusians.data.chat;

import javafx.event.Event;
import javafx.event.EventType;

public class MessageEvent extends Event {

  private final Message message;

  public MessageEvent(Message message) {
    super(EventType.ROOT);
    this.message = message;
  }

  public Message getMessage() {
    return message;
  }
}
