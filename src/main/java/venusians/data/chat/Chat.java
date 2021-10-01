package venusians.data.chat;

import java.util.Queue;
import venusians.util.Event;

public class Chat {

  private static final int MESSAGE_LIMIT = 20;

  private static Queue<Message> messages;

  public static final Event<Message> chatted = new Event<Message>();

  public static void add(Message message) {
    messages.add(message);
    if (messages.size() > MESSAGE_LIMIT) {
      messages.remove();
    }
    chatted.fire(message);
  }
}
