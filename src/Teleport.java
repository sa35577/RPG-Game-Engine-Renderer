/*
Teleport.java
Sat Arora
Teleport class holds data that allows for the player to move to a different location if they
come in contact with the entry teleporter.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Teleport extends Sprite implements Serializable {
    private Teleport exitTeleport; //holding the partner teleporter, which could be the exit if they put in an entry
    private Teleport entryTeleport; //holding the partner teleporter, which could be the entry if they put in an entry
    //constructor
    public Teleport(String id, int x, int y, ImageIcon image) {
         super(id,x,y,image);
         this.exitTeleport = null;
         this.entryTeleport = null;
         super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);} //linking Sprite object to Teleport object

    public void setExit(Teleport t) {
        this.exitTeleport = t;
    }
    public void setEntry(Teleport t) {
        this.entryTeleport = t;
    }
    public int getPartnerX() {
        if (this.entryTeleport != null) {
            return this.entryTeleport.getX(); //returns the entry x if the object accessed from is the exit
        }
        else {
            return this.exitTeleport.getX(); //returns the exit x if the object accessed from is the entry
        }
    }
    public int getPartnerY() {
        if (this.entryTeleport != null) {
            return this.entryTeleport.getY(); //returns the entry y if the object accessed from is the exit
        }
        else {
            return this.exitTeleport.getY(); //returns the exit x if the object accessed from is the entry
        }
    }
    //checks if the accessed Teleport is an entry teleport
    public boolean isEntry() {
        return exitTeleport == null;
    }






}
