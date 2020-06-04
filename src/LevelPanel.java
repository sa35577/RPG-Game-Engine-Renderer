import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;

public class LevelPanel extends JPanel implements KeyListener,MouseListener {
    private Level mainFrame;
    private boolean[] keys;
    private ObjectInputStream inputStream;
    private FileInputStream file;
    private boolean topDown;
    private Color backgroundColor;
    private Image background;
    private String name,description;
    private Countdown countdown; //object that holds the countdown data
    private Health health;
    private PointTotal pointTotal;
    private Font font30, font45, font60; //different font sizes
    private int timeVar; //controlling the time (100 increments per second)

    private Image countdownImage,healthImage;
    private int maxX, maxY;
    private int offX,offY;
    private Rectangle countdownRect,healthRect,pointTotalRect;
    private int direction;
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;

    private Sprite avatar;
    private boolean inMotion;
    private double step;
    private int immunity;
    private boolean gameOver;
    private Sprite curMessage;

    private ArrayList<Sprite> blocks = new ArrayList<>(),
            coins = new ArrayList<>(),
            enemies = new ArrayList<>(),
            goals = new ArrayList<>(),
            healthBonuses = new ArrayList<>(),
            keyInserts = new ArrayList<>(),
            keyHoles = new ArrayList<>(),
            messages = new ArrayList<>(),
            spikes = new ArrayList<>(),
            teleports = new ArrayList<>(),
            timeBonuses = new ArrayList<>(),
            allSprites = new ArrayList<>();
    private int redKeys, greenKeys;

    public LevelPanel(Level lvl) throws IOException, ClassNotFoundException {
        mainFrame = lvl;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        addMouseListener(this);
        font30 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30);
        font45 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,45);
        font60 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,60);
        redKeys = 0;
        greenKeys = 0;
        timeVar = 0;
        maxX = 0; maxY = 0;
        offX = 0; offY = 0;
        countdownImage = new ImageIcon("System/clock.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        healthImage = new ImageIcon("System/health.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        countdown = null;
        health = null;
        pointTotal = null;
        immunity = 0;
        
        direction = RIGHT;
        
        file = new FileInputStream("Allah.txt");
        inputStream = new ObjectInputStream(file);
        topDown = (boolean)inputStream.readObject();


        Object obj = inputStream.readObject();
        if (obj instanceof Color) {
            backgroundColor = (Color)obj;
            background = null;
        }
        else if (obj instanceof ImageIcon) {
            background = ((ImageIcon) obj).getImage();
            backgroundColor = null;
        }
        name = (String)inputStream.readObject();
        description = (String)inputStream.readObject();
        obj = inputStream.readObject();
        if (obj instanceof Countdown) {
            countdown = (Countdown) obj;
            obj = inputStream.readObject();
        }
        if (obj instanceof Health) {
            health = (Health) obj;
            obj = inputStream.readObject();
        }
        if (obj instanceof PointTotal) {
            pointTotal = (PointTotal) obj;
            try {
                obj = inputStream.readObject();
            }
            catch (InvalidClassException ex) {}
        }
        while (true) {
            Sprite nxt = (Sprite) obj;
            nxt.hitBox.translate(-500,-150);
            nxt.locX -= 500;
            nxt.locY -= 150;
            if (nxt.instance instanceof Avatar) {
                avatar = nxt;
                step = 0.00;
            }
            else {
                allSprites.add(nxt);
            }
            if (nxt.instance instanceof Block) {
                blocks.add(nxt);
            }
            else if (nxt.instance instanceof Coin) {
                coins.add(nxt);
            }
            else if (nxt.instance instanceof Enemy) {
                enemies.add(nxt);
            }
            else if (nxt.instance instanceof Goal) {
                goals.add(nxt);
            }
            else if (nxt.instance instanceof HealthBonus) {
                healthBonuses.add(nxt);
            }
            else if (nxt.instance instanceof KeyInsert) {
                keyInserts.add(nxt);
            }
            else if (nxt.instance instanceof KeyHole) {
                keyHoles.add(nxt);
            }
            else if (nxt.instance instanceof Message) {
                messages.add(nxt);
            }
            else if (nxt.instance instanceof Spike) {
                spikes.add(nxt);
            }
            else if (nxt.instance instanceof Teleport) {
                teleports.add(nxt);
            }
            else if (nxt.instance instanceof TimeBonus) {
                timeBonuses.add(nxt);
            }
            maxX = Math.max(maxX,nxt.locX+75);
            maxY = Math.max(maxY,nxt.locY+75);
            try {
                obj = inputStream.readObject();
            }
            catch (EOFException ex){
                break;
            }
        }
        if (topDown) {
            for (int i = 0; i < 3; i++) {
                ((Avatar) avatar.instance).sprites[RIGHT][i] = new ImageIcon(((Avatar) avatar.instance).sprites[RIGHT][i].getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH));
            }
        }
        avatar.hitBox = new Rectangle(avatar.hitBox.x+5,avatar.hitBox.y+5,avatar.hitBox.width-10,avatar.hitBox.height-10);
        for (Sprite sprite : allSprites) {
            sprite.hitBox = new Rectangle(sprite.hitBox.x+5,sprite.hitBox.y+5,sprite.hitBox.width-10,sprite.hitBox.height-10);
            sprite.setVisible(true);
        }
        if (pointTotal == null) {
            for (Sprite sprite : goals) {
                ((Goal) sprite.instance).setReachable(false);
            }
        }
        else {
            for (Sprite sprite : goals) {
                ((Goal) sprite.instance).setReachable(true);
            }
        }
        System.out.println(maxX);
        System.out.println(maxY);
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void topDownMove(int dir) {
        if (dir == RIGHT) {
            System.out.printf("%d %d\n",offX,avatar.locX);
            if (avatar.locX + avatar.hitBox.width < getWidth()/3 || offX + avatar.locX > maxX-2*getWidth()/3)
                avatar.translate(1,0);
            else {
                offX++;
                for (Sprite sprite : allSprites) sprite.translate(-1,0);
            }

        } else if (dir == UP) {
            if (avatar.locY + avatar.hitBox.height > getHeight()/3 || offY <= 0)
                avatar.translate(0,-1);
            else {
                offY--;
                for (Sprite sprite : allSprites) sprite.translate(0,1);
            }
        } else if (dir == LEFT) {
            System.out.printf("%d %d\n",offX,avatar.locX);
            if (avatar.locX + avatar.hitBox.width > getWidth()/3 || offX <= 0)
                avatar.translate(-1,0);
            else {
                offX--;
                for (Sprite sprite : allSprites) sprite.translate(1,0);
            }
        } else if (dir == DOWN) {
            if (avatar.locY + avatar.hitBox.height < getHeight()/3 || offY + avatar.locY > maxY-2*getHeight()/3)
                avatar.translate(0,1);
            else {
                offY++;
                for (Sprite sprite : allSprites) sprite.translate(0,-1);
            }
        }
        if (inMotion) {
            step += 0.05;
            if (step >= 4) step -= 4;
        }
    }

    public void update() {
        timeVar++;
        if (timeVar == 100 && countdown != null) {
            timeVar = 0;
            countdown.decrement();
        }

        if (topDown) {
            if (keys[KeyEvent.VK_RIGHT]) {
                direction = RIGHT;
                inMotion = true;
            } else if (keys[KeyEvent.VK_UP]) {
                direction = UP;
                inMotion = true;
            } else if (keys[KeyEvent.VK_LEFT]) {
                direction = LEFT;
                inMotion = true;
            } else if (keys[KeyEvent.VK_DOWN]) {
                direction = DOWN;
                inMotion = true;
            }
            if (inMotion) {
                topDownMove(direction);
            }
            for (Sprite sprite : goals) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    if (((Goal) sprite.instance).getMaskID() != "blank") {
                        ((Goal) sprite.instance).setMaskID("blank");
                    }
                    if (((Goal) sprite.instance).isReachable()) {
                        gameOver = true;
                    }

                }
            }
            if (!gameOver) {
                for (Sprite sprite : messages) {
                    if (sprite.hitBox.intersects(avatar.hitBox)) {
                        curMessage = sprite;
                        if (sprite.hitBox.x - avatar.hitBox.x > 50) topDownMove(LEFT);
                        else if (avatar.hitBox.x - sprite.hitBox.x > 50) topDownMove(RIGHT);
                        else if (sprite.hitBox.y - avatar.hitBox.y > 50) topDownMove(UP);
                        else if (avatar.hitBox.y - sprite.hitBox.y > 50) topDownMove(DOWN);
                        break;
                    }
                }
                if (curMessage != null) {
                    Sprite deleteCoin = null;
                    for (Sprite sprite : coins) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            if (pointTotal != null) {
                                pointTotal.increase(((Coin) sprite.instance).getPts());
                            }
                            deleteCoin = sprite;
                        }
                    }
                    if (deleteCoin != null) {
                        coins.remove(deleteCoin);
                    }
                    Sprite deleteHealthBonus = null;
                    for (Sprite sprite : healthBonuses) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            if (health != null) {
                                health.setCur(Math.min(health.getCur() + ((Health) sprite.instance).getValue(), health.getValue()));
                            }
                            deleteHealthBonus = sprite;
                        }
                    }
                    if (deleteHealthBonus != null) {
                        healthBonuses.remove(deleteHealthBonus);
                    }
                    Sprite deleteKeyHole = null;
                    for (Sprite sprite : keyHoles) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            KeyHole keyInst = ((KeyHole) sprite.instance);
                            if (keyInst.getColor() == KeyHole.GREEN && greenKeys >= keyInst.getUnlockRequirement()) {
                                greenKeys -= keyInst.getUnlockRequirement();
                                deleteKeyHole = sprite;
                            } else if (keyInst.getColor() == KeyHole.RED && redKeys >= keyInst.getUnlockRequirement()) {
                                redKeys -= keyInst.getUnlockRequirement();
                                deleteKeyHole = sprite;
                            }
                        }
                    }
                    if (deleteKeyHole != null) {
                        keyHoles.remove(deleteKeyHole);
                    }
                    Sprite deleteKeyInsert = null;
                    for (Sprite sprite : keyInserts) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            KeyInsert keyInst = ((KeyInsert) sprite.instance);
                            if (keyInst.getColor() == KeyHole.GREEN) {
                                greenKeys += keyInst.getValue();
                                deleteKeyInsert = sprite;
                            } else if (keyInst.getColor() == KeyHole.RED) {
                                redKeys += keyInst.getValue();
                                deleteKeyInsert = sprite;
                            }
                        }
                    }
                    if (deleteKeyInsert != null) {
                        keyInserts.remove(deleteKeyInsert);
                    }
                    for (Sprite sprite : enemies) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            ((Avatar) avatar.instance).setHealth(((Avatar) avatar.instance).getHealth() - 1);

                        }
                    }
                }
            }
        }
    }
    //returns the status of whether the player can move in the direction, and updates any values depending on intersection
    public boolean checkTopDown() {
        Line2D avatarBorder = null;
        if (direction == UP) {
            avatarBorder.setLine(avatar.locX,avatar.locY,avatar.locX+75,avatar.locY);
        }
        else if (direction == DOWN) {
            avatarBorder.setLine(avatar.locX,avatar.locY+75,avatar.locX+75,avatar.locY+75);
        }
        else if (direction == LEFT) {
            avatarBorder.setLine(avatar.locX,avatar.locY,avatar.locX,avatar.locY+75);
        }
        else if (direction == RIGHT) {
            avatarBorder.setLine(avatar.locX+75,avatar.locY,avatar.locX+75,avatar.locY+75);
        }
        for (Sprite sprite : blocks) {
            if (sprite.hitBox.intersectsLine(avatarBorder)) {
                return false;
            }
        }
        for (Sprite sprite : keyHoles) {
            if (sprite.hitBox.intersectsLine(avatarBorder)) {
                KeyHole keyInst = ((KeyHole) sprite.instance);
                if (keyInst.getColor() == KeyHole.GREEN && greenKeys < keyInst.getUnlockRequirement()) {
                    return false;
                }
                else if (keyInst.getColor() == KeyHole.RED && redKeys < keyInst.getUnlockRequirement()) {
                    return false;
                }
            }
        }



        return true;
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[KeyEvent.VK_UP] = false;
            keys[KeyEvent.VK_LEFT] = false;
            keys[KeyEvent.VK_DOWN] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[KeyEvent.VK_RIGHT] = false;
            keys[KeyEvent.VK_LEFT] = false;
            keys[KeyEvent.VK_DOWN] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[KeyEvent.VK_UP] = false;
            keys[KeyEvent.VK_RIGHT] = false;
            keys[KeyEvent.VK_DOWN] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[KeyEvent.VK_UP] = false;
            keys[KeyEvent.VK_LEFT] = false;
            keys[KeyEvent.VK_RIGHT] = false;
        }
    }
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        if (inMotion && !(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_DOWN])) {
            inMotion = false;
            step = 0;
        }

    }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void paintComponent(Graphics g) {
        if (countdownRect == null) {
            int paintX = 0;
            countdownRect = new Rectangle(0,750,80+g.getFontMetrics().stringWidth("00:00:00"),75);

            paintX = countdownRect.x + countdownRect.width + 10;
            int paintY = countdownRect.y;
            pointTotalRect = new Rectangle(paintX,paintY,150,50);

            paintX += pointTotalRect.width;
            healthRect = new Rectangle(paintX,paintY,150,50);
        }
        g.setColor(Color.GRAY);
        g.fillRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        //drawing background, color or image
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(0,0,1200,750);
        }
        else g.drawImage(background,0,0,null);
        for (Sprite sprite : allSprites) {
            if (sprite.getVisible()) {
                if (0 <= sprite.locX + 75 && getWidth() >= sprite.locX && 0 <= sprite.locY + 75 && this.getWidth() >= sprite.locY) {
                    g.drawImage(sprite.getImg().getImage(), sprite.locX, sprite.locY, null);
                    g.setColor(Color.YELLOW);
                    g.drawRect(sprite.hitBox.x, sprite.hitBox.y, sprite.hitBox.width, sprite.hitBox.height);
                }
            }
        }
        if (inMotion) {
            if ((int)step % 2 == 0) {
                g.drawImage(((Avatar)avatar.instance).sprites[direction][0].getImage(), (int)avatar.locX, (int)avatar.locY, null);
            } else {
                g.drawImage(((Avatar)avatar.instance).sprites[direction][((int)step + 1) / 2].getImage(), (int)avatar.locX, (int)avatar.locY, null);
            }
        }
        else {
            g.drawImage(((Avatar)avatar.instance).sprites[direction][0].getImage(),(int)avatar.locX,(int)avatar.locY,null);
        }
        g.drawRect(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.width,avatar.hitBox.height);
        //drawing the timer and current time
        if (countdown != null) {
            g.setFont(font30);
            g.setColor(Color.BLACK);
            g.drawImage(countdownImage,0,750,null);
            g.drawString(countdown.getStrTime(),80,800);
        }
        //drawing the health count
        if (health != null) {
            g.setFont(font30);
            g.setColor(Color.BLACK);
            g.drawImage(healthImage,healthRect.x,healthRect.y,null);
            g.setColor(Color.YELLOW);
            g.drawLine(healthRect.x+80,healthRect.y+37,healthRect.x+healthRect.width,healthRect.y+37);
            g.setColor(Color.BLUE);
            g.drawString("0",EditPanel.ctrPosition(new Rectangle(healthRect.x+80,healthRect.y,healthRect.width-80,37),"0",g),healthRect.y+30);
            g.setColor(Color.WHITE);
            g.drawString(String.format("%d",health.getValue()),
                    EditPanel.ctrPosition(new Rectangle(healthRect.x+80,healthRect.y+37,healthRect.width-80,38),String.format("%d",health.getValue()),g),
                    healthRect.y+healthRect.height+15);
        }

    }

}
