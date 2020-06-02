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
    private Countdown countdown;
    private Health health;
    private PointTotal pointTotal;
    private Font font30, font45, font60;


    private Sprite avatar;
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
        file = new FileInputStream("Level 1.txt");
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
    public void paintComponent(Graphics g) {
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}
