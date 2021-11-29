package venusians.gui.main.updaters;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import venusians.data.chat.Chat;
import venusians.data.chat.Message;
import venusians.data.chat.MessageEvent;
import venusians.gui.ChangeListenerBuilder;
import venusians.gui.main.artists.ChatMessageArtist;

public class ChatUpdater {

  public static final int HISTORY_RANGE = 20;

  private Pane chatHistory;
  private ScrollPane chatScrollPane;
  private TextField chatPrompt;
  private boolean shouldSnapVvalue = false;

  public ChatUpdater(
    Pane chatHistory,
    ScrollPane chatScrollPane,
    TextField chatPrompt
  ) {
    this.chatHistory = chatHistory;
    this.chatScrollPane = chatScrollPane;
    this.chatPrompt = chatPrompt;

    Chat.setOnChatted(this::onChatted);
  }

  private void onChatted(MessageEvent messageEvent) {
    Message message = messageEvent.getMessage();

    Label render = ChatMessageArtist.render(message);

    List<Node> chatChildren = chatHistory.getChildren();

    chatChildren.add(render);
    if (chatChildren.size() > HISTORY_RANGE) chatChildren.remove(0);

    chatPrompt.setText("");
    if (chatScrollPane.getVvalue() == 1.0) {
      shouldSnapVvalue = true;
    }
  }

  public Node getLastMessageRendered() {
    List<Node> chatHistoryChildren = chatHistory.getChildren();
    int lastIndex = chatHistoryChildren.size() - 1;
    return chatHistoryChildren.get(lastIndex);
  }

  public void bindSnapToBottom() {
    chatScrollPane.vvalueProperty().addListener(snapToBottom);
  }

  private ChangeListener<Number> snapToBottom = ChangeListenerBuilder.from(
    this::onChatLogSizeChanged
  );

  private void onChatLogSizeChanged() {
    if (shouldSnapVvalue) {
      shouldSnapVvalue = false;
      chatScrollPane.setVvalue(1);
    }
  }
}
