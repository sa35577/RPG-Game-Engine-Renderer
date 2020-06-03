import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
            timeBonuses = new ArrayList<>();
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
            obj = inputStream.readObject();
        }
        while (true) {
            Sprite nxt = (Sprite) obj;
            nxt.locX -= 500;
            nxt.locY -= 150;
            if (nxt.instance instanceof Avatar) {
                avatar = nxt;
            }
            else if (nxt.instance instanceof Block) {
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
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void update() {
        timeVar++;
        if (timeVar == 100 && countdown != null) {
            timeVar = 0;
            countdown.decrement();
        }
        if (topDown) {
            if (keys[KeyEvent.VK_RIGHT]) {direction = RIGHT; inMotion = true;}
            else if (keys[KeyEvent.VK_UP]) {direction = UP; inMotion = true; }
            else if (keys[KeyEvent.VK_LEFT]) {direction = LEFT; inMotion = true; }
            else if (keys[KeyEvent.VK_DOWN]) {direction = DOWN; inMotion = true; }

            if (direction ==RIGHT) {
                if (avatar.locX < 600 || offX + this.getWidth() > maxX+75)
                    avatar.locX++;

            }
            else if (direction == UP) {
                avatar.locY--;
            }
            else if (direction == LEFT) {
                avatar.locX--;
            }
            else if (direction == DOWN) {
                avatar.locY++;
            }

        }
        
    }

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
        for (Sprite spike : spikes) g.drawImage(spike.getImg().getImage(),spike.locX,spike.locY,null);

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
}
