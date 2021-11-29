package venusians.data.chat;

import java.util.ArrayDeque;
import javafx.event.EventHandler;

public final class Chat {

  private Chat() {}

  private static final int MESSAGE_LIMIT = 20;

  private static ArrayDeque<Message> messages = new ArrayDeque<>();

  private static EventHandler<MessageEvent> onChatted;

  public static void add(Message message) {
    messages.add(message);
    if (messages.size() > MESSAGE_LIMIT) {
      messages.remove();
    }

    MessageEvent messageEvent = new MessageEvent(message);
    if (onChatted != null) {
      onChatted.handle(messageEvent);
    }
  }

  public static void setOnChatted(EventHandler<MessageEvent> onChatted) {
    Chat.onChatted = onChatted;
  }

  public static EventHandler<MessageEvent> getOnChatted() {
    return onChatted;
  }
}
