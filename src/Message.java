import java.awt.*;
public class Message extends Sprite {
    private String title;
    private String content;
    public Message(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.title = "";
        this.content = "";
    }
    public void init() {super.setInstance(this);}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
