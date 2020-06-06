/*
Message.java
Sat Arora
Message class for the creator to put optional messages in the game wherever they wish.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Message extends Sprite implements Serializable {
    private String title; //title of message
    private String content; //content of message
    //constructor, all content starts out empty
    public Message(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.title = "";
        this.content = "";
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);} //linking Sprite object to Message object
    //getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
