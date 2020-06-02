import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Message extends Sprite implements Serializable {
    private String title;
    private String content;
    public Message(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.title = "";
        this.content = "";
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
