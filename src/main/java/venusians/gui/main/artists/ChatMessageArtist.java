package venusians.gui.main.artists;

import javafx.scene.control.Label;
import venusians.data.chat.Message;

public class ChatMessageArtist {

  public static Label render(Message subject) {
    String resultText = String.format(
      "[%s]: %s",
      subject.author.getName(),
      subject.content
    );

    Label result = new Label(resultText);
    result.setWrapText(true);

    return result;
  }
}
