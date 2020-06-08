/*
LevelPanel.java
Sat Arora
The part of the application that will actually run the game.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import static java.lang.Math.max;
import static java.lang.Math.min;


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
    private Font font15, font25, font30, font45, font45Bold, font60; //different font sizes
    private int timeVar; //controlling the time (100 increments per second)

    private Image countdownImage,healthImage;
    private int maxX, maxY;
    private int offX,offY;
    private Rectangle countdownRect,healthRect,pointTotalRect;
    private int direction;
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    public static final Color LIGHTBLUE = new Color(173,216,230), LIGHTRED = new Color(252,125,120);

    private Sprite avatar;
    private boolean inMotion;
    private double step;
    private int immunity, messageImmunity;
    private boolean gameOver;
    private Sprite curMessage;
    private Image promptBack;
    private Image keyboard,labels;
    private Image goalUnreachable;
    private Rectangle startRect;
    private JTextArea messageContentArea;
    private JScrollPane messageContentPane;
    private Rectangle continueRect;
    private boolean mouseOn,keyOn;
    private boolean letGoOfSpace;
    private Image redKeyImg,greenKeyImg;
    private Sprite stupidSprite = null;

    //down is positive
    private boolean onGround;
    private int velocityY;
    public static final int GRAVITY = 1;

    private boolean onStart;
    private boolean onPause;
    private boolean won, loss;


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
            allSprites = new ArrayList<>(),
            moveRestrictions = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private int redKeys, greenKeys;

    public LevelPanel(Level lvl) throws IOException, ClassNotFoundException {
        setLayout(null);
        mainFrame = lvl;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        addMouseListener(this);
        font15 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,15);
        font25 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,25);
        font30 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30);
        font45 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,45);
        font45Bold = new Font("System San Francisco Display Regular.ttf",Font.BOLD,45);


        font60 = new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,60);
        redKeys = 0;
        greenKeys = 0;
        timeVar = 0;
        maxX = 0; maxY = 0;
        offX = 0; offY = 0;
        countdownImage = new ImageIcon("System/clock.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        healthImage = new ImageIcon("System/health.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        goalUnreachable = new ImageIcon("Block/goalNotReady.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        redKeyImg = new ImageIcon("Item/redkey.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        greenKeyImg = new ImageIcon("Item/greenkey.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);

        countdown = null;
        health = null;
        pointTotal = null;
        immunity = 0;
        messageImmunity = 0;
        
        direction = RIGHT;
        promptBack = new ImageIcon("promptBlock.png").getImage().getScaledInstance(1100,800,Image.SCALE_SMOOTH);
        
        file = new FileInputStream("Eagle.txt");
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
            maxX = max(maxX,nxt.locX+75);
            maxY = max(maxY,nxt.locY+75);
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
        ((Avatar) avatar.instance).setDamage(1);
        for (Sprite sprite : enemies) {
            ((Enemy) sprite.instance).setHealth(((Enemy) sprite.instance).getHealth());
            ((Enemy) sprite.instance).setDamage(1);
            ((Enemy) sprite.instance).setBulletPeriod(200);
            if (((Enemy) sprite.instance).isStationary()) {
                ((Enemy) sprite.instance).setSpeed(0);
            }
            if (!topDown){
                ((Enemy) sprite.instance).setOnGround(false);
                ((Enemy) sprite.instance).setVelocityY(0);
                ((Enemy) sprite.instance).setJumpPeriod(randint(300,600));
                ((Enemy) sprite.instance).setJumpTimer(0);
                ((Enemy) sprite.instance).setInitialVelocityY(-1*randint(15,30));

            }
        }

        messageContentArea = new JTextArea();
        messageContentArea.setFont(font25);
        messageContentArea.setBackground(new Color(0,0,0,0));
        messageContentArea.setBounds(100,120,1000,150);
        messageContentArea.setWrapStyleWord(true);
        messageContentArea.setLineWrap(true);
        messageContentArea.setEditable(false);


        messageContentPane = new JScrollPane(messageContentArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        messageContentPane.setBackground(messageContentArea.getBackground());
        messageContentPane.setVisible(false);
        messageContentPane.setBounds(messageContentArea.getBounds());
        messageContentPane.addKeyListener(this);
        add(messageContentPane);
        messageContentPane.setVisible(true);
        messageContentArea.setText(description);
        messageContentArea.setVisible(true);

        continueRect = new Rectangle(mainFrame.getWidth()/2-125,710,250,90);
        velocityY = 0;
        onGround = false;

        moveRestrictions.addAll(blocks);
        moveRestrictions.addAll(keyHoles);
        moveRestrictions.addAll(messages);
        moveRestrictions.addAll(spikes);


        keyboard = new ImageIcon("keyboard.png").getImage().getScaledInstance(680,280,Image.SCALE_SMOOTH);
        labels = new ImageIcon("instructions2.png").getImage().getScaledInstance(240,140,Image.SCALE_SMOOTH);
        onStart = true;
        onPause = false;
        won = false;
        loss = false;
        startRect = new Rectangle(1200/2-100,700,200,100);
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void topDownMove(int dir) {
        if (dir == RIGHT) {
            if (avatar.locX + avatar.hitBox.width < getWidth()/3 || offX + avatar.locX > maxX-2*getWidth()/3)
                avatar.translate(((Avatar) avatar.instance).getSpeed(),0);
            else {
                offX++;
                for (Sprite sprite : allSprites) sprite.translate(-1*((Avatar) avatar.instance).getSpeed(),0);
            }

        } else if (dir == UP) {
            if (avatar.locY + avatar.hitBox.height > getHeight()/3 || offY <= 0)
                avatar.translate(0,-1*((Avatar) avatar.instance).getSpeed());
            else {
                offY--;
                for (Sprite sprite : allSprites) sprite.translate(0,((Avatar) avatar.instance).getSpeed());
            }
        } else if (dir == LEFT) {
            if (avatar.locX + avatar.hitBox.width > getWidth()/3 || offX <= 0)
                avatar.translate(-1*((Avatar) avatar.instance).getSpeed(),0);
            else {
                offX--;
                for (Sprite sprite : allSprites) sprite.translate(((Avatar) avatar.instance).getSpeed(),0);
            }
        } else if (dir == DOWN) {
            if (avatar.locY + avatar.hitBox.height < getHeight()/3 || offY + avatar.locY > maxY-2*getHeight()/3)
                avatar.translate(0,((Avatar) avatar.instance).getSpeed());
            else {
                offY++;
                for (Sprite sprite : allSprites) sprite.translate(0,-1*((Avatar) avatar.instance).getSpeed());
            }
        }

        step += 0.05;
        if (step >= 4) step -= 4;
    }

    public void topDownMove(int dir, Sprite curSprite) {
        if (dir == RIGHT) {
            curSprite.translate(((Enemy) curSprite.instance).getSpeed(),0);
        } else if (dir == UP) {
            curSprite.translate(0,-1*((Enemy) curSprite.instance).getSpeed());
        } else if (dir == LEFT) {
            curSprite.translate(-1*((Enemy) curSprite.instance).getSpeed(),0);
        } else if (dir == DOWN) {
            curSprite.translate(0,((Enemy) curSprite.instance).getSpeed());
        }
        ((Enemy) curSprite.instance).increaseStep();
    }

    public void update() {
        Point mouse = getMousePosition();
        if (mouse == null ) mouse = new Point(0,0);
        if (onStart) {
            if (startRect.contains(mouse) && mouseOn) {
                onStart = false;
                messageContentPane.setVisible(false);
                messageContentArea.setVisible(false);
                messageContentArea.setBounds(messageContentArea.getX(),messageContentArea.getY(),messageContentArea.getWidth(),messageContentArea.getHeight()*500/150);
                messageContentPane.setBounds(messageContentArea.getBounds());
            }
        }
        if (onPause) {
            if (startRect.contains(mouse) && mouseOn) {
                onPause = false;
            }
            return;
        }
        if (curMessage != null) {
            if (continueRect.contains(mouse) && mouseOn) {
                curMessage = null;
                messageContentPane.setVisible(false);
                messageContentArea.setVisible(false);
                if (!topDown) messageImmunity = 200;
            }
            return;
        }
        else if (messageImmunity > 0) messageImmunity--;
        timeVar++;
        ArrayList<Bullet> bulletDeletions = new ArrayList<>();
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (bullets.get(i).getHitBox().intersects(bullets.get(j).getHitBox()) && bullets.get(i).isAvatarBullet() != bullets.get(j).isAvatarBullet()) {
                    bulletDeletions.add(bullets.get(i));
                    bulletDeletions.add(bullets.get(j));
                }
            }
        }
        bullets.removeAll(bulletDeletions);
        Bullet.decrementAvatarTime();
        ArrayList<Sprite> enemyDeletions = new ArrayList<>();
        for (Sprite sprite : enemies) {
            if (((Enemy) sprite.instance).getHealth() == 0) {
                enemyDeletions.add(sprite);
            }
            else {
                ((Enemy) sprite.instance).decrementHealhtBar();
                ((Enemy) sprite.instance).incrementTimer();
                if (((Enemy) sprite.instance).getBulletSpeed() > 0 && ((Enemy) sprite.instance).getBulletTimer() == 0) {
                    if (((Enemy) sprite.instance).getDirection() == RIGHT)
                        bullets.add(new Bullet(sprite.hitBox.x + sprite.hitBox.width, sprite.hitBox.y + sprite.hitBox.height / 2,
                                RIGHT, false, ((Enemy) sprite.instance).getBulletSpeed(), ((Enemy) sprite.instance).getDamage()));
                    else if (((Enemy) sprite.instance).getDirection() == LEFT)
                        bullets.add(new Bullet(sprite.hitBox.x, sprite.hitBox.y + sprite.hitBox.height / 2,
                                LEFT, false, ((Enemy) sprite.instance).getBulletSpeed(), ((Enemy) sprite.instance).getDamage()));
                    else if (((Enemy) sprite.instance).getDirection() == UP)
                        bullets.add(new Bullet(sprite.hitBox.x + sprite.hitBox.width / 2, sprite.hitBox.y,
                                UP, false, ((Enemy) sprite.instance).getBulletSpeed(), ((Enemy) sprite.instance).getDamage()));
                    else if (((Enemy) sprite.instance).getDirection() == DOWN)
                        bullets.add(new Bullet(sprite.hitBox.x + sprite.hitBox.width / 2, sprite.hitBox.y + sprite.hitBox.height,
                                DOWN, false, ((Enemy) sprite.instance).getBulletSpeed(), ((Enemy) sprite.instance).getDamage()));
                }
            }
        }
        enemies.removeAll(enemyDeletions);
        allSprites.removeAll(enemyDeletions);
        if (timeVar == 100 && countdown != null) {
            timeVar = 0;
            countdown.decrement();
        }
        immunity = max(immunity-1,0);
        if (keys[KeyEvent.VK_SPACE] && Bullet.avatarReadyToShoot() && letGoOfSpace) {
            if (direction == RIGHT) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height/2,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == LEFT) bullets.add(new Bullet(avatar.hitBox.x, avatar.hitBox.y+avatar.hitBox.height/2,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == UP && topDown) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width/2,avatar.hitBox.y,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == DOWN && topDown) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width/2,avatar.hitBox.y+avatar.hitBox.height,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            Bullet.setAvatarTime();
            letGoOfSpace = false;
        }
        if (!keys[KeyEvent.VK_SPACE] && !letGoOfSpace) letGoOfSpace = true;
        for (int i = bullets.size()-1; i >= 0; i--) {
            while (i >= bullets.size()) i--;
            if (bullets.size() == 0) break;
            Bullet curBullet = bullets.get(i);
            if (curBullet.getDir() == RIGHT) curBullet.translate(curBullet.getSpeed(),0);
            else if (curBullet.getDir() == LEFT) curBullet.translate(-1*curBullet.getSpeed(),0);
            else if (curBullet.getDir() == UP) curBullet.translate(0,-1*curBullet.getSpeed());
            else if (curBullet.getDir() == DOWN) curBullet.translate(0,curBullet.getSpeed());

            boolean removedIdx = false;

            if ((curBullet.getHitBox().x < 0 || curBullet.getHitBox().x > maxX+150) && onGround) {

                bullets.remove(i);
                removedIdx = true;
            }
            else if ((curBullet.getHitBox().y < 0 || curBullet.getHitBox().y > maxY+150) && onGround) {
                bullets.remove(i);
                removedIdx = true;
            }
            if (removedIdx) continue;


            for (Sprite sprite : blocks) {
                if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                    bullets.remove(i);
                    removedIdx = true;
                    continue;
                }
            }
            if (removedIdx) continue;
            for (Sprite sprite : spikes) {
                if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                    bullets.remove(i);
                    removedIdx = true;
                    continue;
                }
            }
            if (removedIdx) continue;
            for (Sprite sprite : goals) {
                if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                    bullets.remove(i);
                    removedIdx = true;
                    continue;
                }
            }
            if (removedIdx) continue;
            for (Sprite sprite : keyHoles) {
                if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                    bullets.remove(i);
                    removedIdx = true;
                    continue;
                }
            }
            if (removedIdx) continue;
            for (Sprite sprite : messages) {
                if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                    bullets.remove(i);
                    removedIdx = true;
                    continue;
                }
            }
            if (removedIdx) continue;
            if (curBullet.isAvatarBullet()) {
                for (Sprite sprite : enemies) {
                    if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                        ((Enemy) sprite.instance).setDrawHealthBar(200);
                        ((Enemy) sprite.instance).setCurHealth(max(0,((Enemy) sprite.instance).getHealth()-curBullet.getDamage()));
                        bullets.remove(i);
                    }
                }
            }
            else {
                if (avatar.hitBox.intersects(curBullet.getHitBox())){
                    ((Avatar) avatar.instance).setDrawHealthBar(200);
                    ((Avatar) avatar.instance).setHealth(max(0,((Avatar) avatar.instance).getHealth()-curBullet.getDamage()));
                    bullets.remove(i);
                }
            }
        }
        for (Sprite sprite : goals) {
            if (sprite.hitBox.intersects(avatar.hitBox)) {
                if (!((Goal) sprite.instance).getMaskID().equals("blank")) {
                    ((Goal) sprite.instance).setMaskID("blank");
                }
                if (isReachable()) {
                    gameOver = true;
                    won = true;
                    return;
                }

            }
        }

        if (topDown) {
            if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) {
                direction = RIGHT;
                inMotion = true;
            } else if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) {
                direction = UP;
                inMotion = true;
            } else if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) {
                direction = LEFT;
                inMotion = true;
            } else if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) {
                direction = DOWN;
                inMotion = true;
            }
            if (inMotion) {
                if (checkTopDown(avatar,direction)) {
                    topDownMove(direction);
                }

            }

            for (Sprite sprite : enemies) {
                if (((Enemy) sprite.instance).isStationary()) continue;
                if (!checkTopDown(sprite, ((Enemy) sprite.instance).getDirection())) {
                    ((Enemy) sprite.instance).setDirection((((Enemy) sprite.instance).getDirection() + 2) % 4);
                }
                topDownMove(((Enemy) sprite.instance).getDirection(), sprite);
            }



            if (!gameOver) {
                for (Sprite sprite : messages) {
                    if (sprite.hitBox.intersects(avatar.hitBox)) {
                        curMessage = sprite;
                        messageContentArea.setVisible(true); messageContentPane.setVisible(true);
                        messageContentArea.setText(((Message) sprite.instance).getContent());
                        if (sprite.hitBox.x - avatar.hitBox.x > 40) topDownMove(LEFT);
                        else if (avatar.hitBox.x - sprite.hitBox.x > 40) topDownMove(RIGHT);
                        else if (sprite.hitBox.y - avatar.hitBox.y > 40) topDownMove(UP);
                        else if (avatar.hitBox.y - sprite.hitBox.y > 40) topDownMove(DOWN);
                        break;
                    }
                }
                if (curMessage == null) {
                    boolean notIntersectWithSpike = true;

                    for (Sprite sprite : spikes) {
                        if (sprite.hitBox.intersects(avatar.hitBox)) {
                            notIntersectWithSpike = false;
                            if (sprite.hitBox.x - avatar.hitBox.x > 40) topDownMove(LEFT);
                            else if (avatar.hitBox.x - sprite.hitBox.x > 40) topDownMove(RIGHT);
                            else if (sprite.hitBox.y - avatar.hitBox.y > 40) topDownMove(UP);
                            else if (avatar.hitBox.y - sprite.hitBox.y > 40) topDownMove(DOWN);
                            if (immunity == 0) {
                                ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - ((Spike) sprite.instance).getDmg(), 0));
                                immunity = 300;
                                if (health != null) {
                                    health.setCur(max(((Avatar) avatar.instance).getHealth() - ((Spike) sprite.instance).getDmg(), 0));
                                }
                            }
                        }
                    }

                    if (notIntersectWithSpike) {
                        boolean moved = false;
                        for (Sprite sprite : teleports) {
                            if (sprite.hitBox.intersects(avatar.hitBox)) {
                                if (((Teleport) sprite.instance).isEntry()) {
                                    moved = true;
                                    int curX = ((Teleport) sprite.instance).getPartnerX(), curY = ((Teleport) sprite.instance).getPartnerY();
                                    for (Sprite sp : allSprites) {
                                        sp.translate(avatar.locX-curX, avatar.locY-curY);
                                    }
                                }
                            }
                        }
                        if (!moved) {
                            Sprite deleteTimeBonus = null;
                            for (Sprite sprite : timeBonuses) {
                                if (sprite.hitBox.intersects(avatar.hitBox)) {
                                    if (countdown != null) {
                                        countdown.increease(((TimeBonus) sprite.instance).getValue());
                                        break;
                                    }
                                    deleteTimeBonus = sprite;
                                }
                            }
                            if (deleteTimeBonus != null) {
                                timeBonuses.remove(deleteTimeBonus);
                                allSprites.remove(deleteTimeBonus);
                            }

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
                                allSprites.remove(deleteCoin);
                            }
                            Sprite deleteHealthBonus = null;
                            for (Sprite sprite : healthBonuses) {
                                if (sprite.hitBox.intersects(avatar.hitBox)) {
                                    if (health != null) {
                                        health.setCur(min(health.getCur() + ((Health) sprite.instance).getValue(), health.getValue()));
                                    }
                                    deleteHealthBonus = sprite;
                                }
                            }
                            if (deleteHealthBonus != null) {
                                healthBonuses.remove(deleteHealthBonus);
                                allSprites.remove(deleteHealthBonus);
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
                                allSprites.remove(deleteKeyHole);
                                moveRestrictions.remove(deleteKeyHole);
                            }
                            Sprite deleteKeyInsert = null;
                            for (Sprite sprite : keyInserts) {
                                if (sprite.hitBox.intersects(avatar.hitBox)) {
                                    KeyInsert keyInst = ((KeyInsert) sprite.instance);
                                    if (keyInst.getGameColor() == KeyHole.GREEN) {
                                        greenKeys += keyInst.getValue();
                                        deleteKeyInsert = sprite;
                                    } else if (keyInst.getGameColor() == KeyHole.RED) {
                                        redKeys += keyInst.getValue();
                                        deleteKeyInsert = sprite;
                                    }
                                }
                            }
                            if (deleteKeyInsert != null) {
                                keyInserts.remove(deleteKeyInsert);
                                allSprites.remove(deleteKeyInsert);
                            }
                            if (immunity == 0) {
                                for (Sprite sprite : enemies) {
                                    if (sprite.hitBox.intersects(avatar.hitBox)) {
                                        ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - 1,0));
                                        if (health != null) {
                                            health.setCur(max(((Avatar) avatar.instance).getHealth() - ((Enemy) sprite.instance).getDamage(), 0));
                                        }
                                        immunity = 300;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            //higher priorities

            for (Sprite sprite : messages) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    curMessage = sprite;
                    return;
                }
            }
            if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) {
                direction = RIGHT;
                inMotion = true;
            }
            else if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) {
                direction = LEFT;
                inMotion = true;
            }
            if (inMotion) {
                if (checkTopDown(avatar,direction)) {
                    topDownMove(direction);
                }

            }
            if (!onGround) {
                velocityY += GRAVITY;
                for (Sprite sprite : allSprites) {
                    sprite.translate(0,-1*velocityY);
                }
                for (Bullet b : bullets) {
                    b.translate(0,-1*velocityY);
                }
                Line2D avatarBorderUp = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y),
                        avatarBorderDown = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height),
                        avatarBorderLeft = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height),
                        avatarBorderRight = new Line2D.Double(avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height);
                Sprite deleteKeyHole = null;
                for (Sprite sprite : moveRestrictions) {
                    int curYTop = sprite.hitBox.y;
                    if (sprite.hitBox.intersectsLine(avatarBorderDown)) {
                        velocityY = 0;
                        onGround = true;
                        for (Sprite sp : allSprites) {
                            sp.translate(0, -curYTop + avatar.hitBox.y + avatar.hitBox.height);
                        }
                        for (Bullet b : bullets) {
                            b.translate(0, -curYTop + avatar.hitBox.y + avatar.hitBox.height);
                        }
                        if (sprite.instance instanceof KeyHole) {
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
                    int curYBottom = sprite.hitBox.y+sprite.hitBox.height;
                    if (sprite.hitBox.intersectsLine(avatarBorderUp)) {
                        velocityY = 0;
                        onGround = false;
                        for (Sprite sp : allSprites) {
                            sp.translate(0, -curYBottom + avatar.hitBox.y);
                        }
                        for (Bullet b : bullets) {
                            b.translate(0, -curYBottom + avatar.hitBox.y);
                        }
                        if (sprite.instance instanceof KeyHole) {
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
                }
                if (deleteKeyHole != null) {
                    keyHoles.remove(deleteKeyHole);
                    allSprites.remove(deleteKeyHole);
                    moveRestrictions.remove(deleteKeyHole);
                }

            }
            else {
                Line2D avatarBorderDown = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height),
                        avatarBorderLeft = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height-1),
                        avatarBorderRight = new Line2D.Double(avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height-1 );
                if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) && onGround) {
                    boolean jump = true;
                    for (Sprite sprite : moveRestrictions) {
                        if (sprite.hitBox.intersectsLine(avatarBorderLeft)) {
                            jump = false;
                            break;
                        }
                        if (sprite.hitBox.intersectsLine(avatarBorderRight)) {
                            jump = false;
                            break;
                        }
                    }
                    if (jump) {
                        onGround = false;
                        velocityY = -30;
                    }
                }
                boolean interWithSprite = false;
                for (Sprite sprite : moveRestrictions) {
                    if (sprite.hitBox.intersectsLine(avatarBorderDown)) {
                        interWithSprite = true;
                        break;
                    }
                }
                if (!interWithSprite) {
                    onGround = false;
                }
            }
            for (Sprite sprite : enemies) {
                Enemy enemy = (Enemy) sprite.instance;
                if (!enemy.isOnGround()) {
                    enemy.setVelocityY(enemy.getVelocityY() + GRAVITY);
                    sprite.translate(0,enemy.getVelocityY());
                    for (Sprite restriction : moveRestrictions) {
                        Line2D enemyBorderUp = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y),
                                enemyBorderDown = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height),
                                enemyBorderLeft = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height),
                                enemyBorderRight = new Line2D.Double(enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height);

                        if (restriction.hitBox.intersectsLine(enemyBorderDown)) {
                            enemy.setVelocityY(0);
                            enemy.setOnGround(true);
                            sprite.translate(0,restriction.hitBox.y-(sprite.hitBox.y+sprite.hitBox.height));
                        }
                        if (restriction.hitBox.intersectsLine(enemyBorderUp)) {
                            velocityY = 0;
                            onGround = true;
                            sprite.translate(0,restriction.hitBox.y+restriction.hitBox.height-sprite.hitBox.y);

                        }

                    }
                }
                else {
                    if (enemy.incJumpTimer()) {
                        enemy.setVelocityY(enemy.getInitialVelocityY());
                        enemy.setOnGround(false);
                    }
                }
            }
            for (Sprite sprite : spikes) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    if (immunity == 0) {
                        ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - ((Spike) sprite.instance).getDmg(), 0));
                        immunity = 300;
                        if (health != null) {
                            health.setCur(max(((Avatar) avatar.instance).getHealth() - ((Spike) sprite.instance).getDmg(), 0));
                        }
                        return;
                    }
                }
            }
            for (Sprite sprite : teleports) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    if (((Teleport) sprite.instance).isEntry()) {
                        int curX = ((Teleport) sprite.instance).getPartnerX(), curY = ((Teleport) sprite.instance).getPartnerY();
                        for (Sprite sp : allSprites) {
                            sp.translate(avatar.locX-curX, avatar.locY-curY);
                        }
                        return;
                    }
                }
            }

            Sprite deleteTimeBonus = null;
            for (Sprite sprite : timeBonuses) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    if (countdown != null) {
                        countdown.increease(((TimeBonus) sprite.instance).getValue());
                    }
                    deleteTimeBonus = sprite;
                }
            }
            if (deleteTimeBonus != null) {
                timeBonuses.remove(deleteTimeBonus);
                allSprites.remove(deleteTimeBonus);
            }
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
                allSprites.remove(deleteCoin);
            }
            Sprite deleteHealthBonus = null;
            for (Sprite sprite : healthBonuses) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    if (health != null) {
                        health.setCur(min(health.getCur() + ((Health) sprite.instance).getValue(), health.getValue()));
                    }
                    deleteHealthBonus = sprite;
                }
            }
            if (deleteHealthBonus != null) {
                healthBonuses.remove(deleteHealthBonus);
                allSprites.remove(deleteHealthBonus);
            }
            Sprite deleteKeyHole = null;
            for (Sprite sprite : keyHoles) {
                if (keyHoles.size() == 6 && sprite.hitBox.y == avatar.hitBox.y+avatar.hitBox.height) {
                    stupidSprite = sprite;
                }
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
                allSprites.remove(deleteKeyHole);
                moveRestrictions.remove(deleteKeyHole);
            }
            Sprite deleteKeyInsert = null;
            for (Sprite sprite : keyInserts) {
                if (sprite.hitBox.intersects(avatar.hitBox)) {
                    KeyInsert keyInst = ((KeyInsert) sprite.instance);
                    if (keyInst.getGameColor() == KeyHole.GREEN) {
                        greenKeys += keyInst.getValue();
                        deleteKeyInsert = sprite;
                    } else if (keyInst.getGameColor() == KeyHole.RED) {
                        redKeys += keyInst.getValue();
                        deleteKeyInsert = sprite;
                    }
                }
            }
            if (deleteKeyInsert != null) {
                keyInserts.remove(deleteKeyInsert);
                allSprites.remove(deleteKeyInsert);
            }
            if (immunity == 0) {
                for (Sprite sprite : enemies) {
                    if (sprite.hitBox.intersects(avatar.hitBox)) {
                        ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - 1,0));
                        if (health != null) {
                            health.setCur(max(((Avatar) avatar.instance).getHealth() - ((Enemy) sprite.instance).getDamage(), 0));
                        }
                        immunity = 300;
                    }
                }
            }


        }
    }
    public boolean onPlatform() {
        for (Sprite sprite : blocks) {
            if (sprite.hitBox.contains(avatar.hitBox)) {
                return true;
            }
        }
        for (Sprite sprite : spikes) {
            if (sprite.hitBox.contains(avatar.hitBox)) {
                return true;
            }
        }
        return false;
    }
    //returns the status of whether the player can move in the direction, and updates any values depending on intersection
    public boolean checkTopDown(Sprite curSprite, int dir) {
        Line2D spriteBorder=null;
        if (dir == UP) {
            spriteBorder = new Line2D.Double(curSprite.locX,curSprite.locY,curSprite.locX+curSprite.hitBox.width,curSprite.locY);
        }
        else if (dir == DOWN) {
            spriteBorder = new Line2D.Double(curSprite.locX,curSprite.locY+curSprite.hitBox.height,curSprite.locX+curSprite.hitBox.width,curSprite.locY+curSprite.hitBox.height);
        }
        else if (dir == LEFT) {
            spriteBorder = new Line2D.Double(curSprite.locX,curSprite.locY,curSprite.locX,curSprite.locY+curSprite.hitBox.height);
        }
        else if (dir == RIGHT) {
            spriteBorder = new Line2D.Double(curSprite.locX+curSprite.hitBox.width,curSprite.locY,curSprite.locX+curSprite.hitBox.width,curSprite.locY+curSprite.hitBox.height);
        }
        if (curSprite.getId() == avatar.getId()) {
            if (curSprite.hitBox.x < ((Avatar) curSprite.instance).getSpeed() && dir == LEFT) return false;
            if (curSprite.hitBox.y < ((Avatar) curSprite.instance).getSpeed() && dir == UP) return false;
            if (curSprite.hitBox.x + ((Avatar) curSprite.instance).getSpeed() > maxX && dir == RIGHT) return false;
            if (curSprite.hitBox.y + ((Avatar) curSprite.instance).getSpeed() > maxY && dir == DOWN) return false;

        }
        else {
            if (curSprite.hitBox.x < ((Enemy) curSprite.instance).getSpeed() && dir == LEFT) return false;
            if (curSprite.hitBox.y < ((Enemy) curSprite.instance).getSpeed() && dir == UP) return false;
            if (curSprite.hitBox.x + ((Enemy) curSprite.instance).getSpeed() > maxX && dir == RIGHT) return false;
            if (curSprite.hitBox.y + ((Enemy) curSprite.instance).getSpeed() > maxY && dir == DOWN) return false;
            for (Sprite sprite : spikes) {
                if (sprite.hitBox.intersects(curSprite.hitBox)) return false;
            }

        }
        for (Sprite sprite : blocks) {
            if (sprite.hitBox.intersectsLine(spriteBorder)) {
                return false;
            }
        }
        for (Sprite sprite : keyHoles) {
            if (sprite.hitBox.intersectsLine(spriteBorder)) {
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

    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyChar() == 'p' && !keyOn) {
            if (!onStart && !won && !loss) {
                if (onPause) onPause = false;
                else onPause = true;
                keyOn = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            if (topDown) {
                keys[KeyEvent.VK_UP] = false;
                keys[KeyEvent.VK_W] = false;
                keys[KeyEvent.VK_LEFT] = false;
                keys[KeyEvent.VK_A] = false;
                keys[KeyEvent.VK_DOWN] = false;
                keys[KeyEvent.VK_S] = false;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            if (topDown) {
                keys[KeyEvent.VK_RIGHT] = false;
                keys[KeyEvent.VK_D] = false;
                keys[KeyEvent.VK_LEFT] = false;
                keys[KeyEvent.VK_A] = false;
                keys[KeyEvent.VK_DOWN] = false;
                keys[KeyEvent.VK_S] = false;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            if (topDown) {
                keys[KeyEvent.VK_UP] = false;
                keys[KeyEvent.VK_W] = false;
                keys[KeyEvent.VK_RIGHT] = false;
                keys[KeyEvent.VK_D] = false;
                keys[KeyEvent.VK_DOWN] = false;
                keys[KeyEvent.VK_S] = false;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            if (topDown) {
                keys[KeyEvent.VK_UP] = false;
                keys[KeyEvent.VK_W] = false;
                keys[KeyEvent.VK_LEFT] = false;
                keys[KeyEvent.VK_A] = false;
                keys[KeyEvent.VK_RIGHT] = false;
                keys[KeyEvent.VK_D] = false;
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        if (e.getKeyChar() == 'p') {
            keyOn = false;
        }
        if (inMotion && !(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D] || keys[KeyEvent.VK_UP]|| keys[KeyEvent.VK_W] || keys[KeyEvent.VK_LEFT]|| keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]|| keys[KeyEvent.VK_A])) {
            inMotion = false;
            step = 0;
        }

    }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) {
        System.out.printf("%d %d",e.getX(),e.getY());
        mouseOn = true;
    }
    public void mouseReleased(MouseEvent e) {
        mouseOn = false;
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void paintComponent(Graphics g) {
        System.out.println(onPause);
        if (countdownRect == null) {
            int paintX = 0;
            countdownRect = new Rectangle(0,750,80+g.getFontMetrics().stringWidth("00:00:00"),75);

            paintX = countdownRect.x + countdownRect.width + 10;
            int paintY = countdownRect.y;
            pointTotalRect = new Rectangle(paintX,paintY,150,50);

            paintX += pointTotalRect.width;
            healthRect = new Rectangle(paintX,paintY,150,50);
        }
        if (curMessage != null) {
            drawMessage(g);
            return;
        }
        if (onStart) {
            drawStartMenu(g);
            return;
        }
        else if (onPause) {
            drawPauseMenu(g);
            return;
        }
        else if (gameOver) {
            drawEndMenu(g);
            return;
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
                    /*g.drawImage(sprite.getImg().getImage(), sprite.locX, sprite.locY, null);
                    g.setColor(Color.YELLOW);
                    g.drawRect(sprite.hitBox.x, sprite.hitBox.y, sprite.hitBox.width, sprite.hitBox.height);*/
                    if (sprite.instance instanceof Enemy) {
                        if (((Enemy) sprite.instance).isOnGround() || topDown) {
                            if (((Enemy) sprite.instance).isStationary()) {
                                g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][0].getImage(), sprite.locX, sprite.locY, null);
                            } else {
                                if ((int) ((Enemy) sprite.instance).getStep() % 2 == 0) {
                                    g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][0].getImage(), sprite.locX, sprite.locY, null);
                                } else {
                                    g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][((int) ((Enemy) sprite.instance).getStep() + 1) / 2].getImage(), sprite.locX, sprite.locY, null);
                                }
                            }
                        }
                        else {
                            if (((Enemy) sprite.instance).getVelocityY() < 0) {
                                g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][3].getImage(), sprite.locX, sprite.locY, null);
                            }
                            else {
                                g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][4].getImage(), sprite.locX, sprite.locY, null);
                            }
                        }
                    }
                    else if (sprite.instance instanceof Goal) {
                        if (((Goal) sprite.instance).getMaskID().equals("blank")) {
                            if (isReachable()) {

                                g.drawImage(sprite.getImg().getImage(),sprite.locX,sprite.locY,null);
                            }
                            else {
                                g.drawImage(goalUnreachable,sprite.locX,sprite.locY,null);
                            }
                        }
                        else {
                            g.drawImage(((Goal) sprite.instance).getMask().getImage(),sprite.locX,sprite.locY,null);
                        }
                    }
                    else {
                        g.drawImage(sprite.getImg().getImage(), sprite.locX, sprite.locY, null);
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(sprite.hitBox.x, sprite.hitBox.y, sprite.hitBox.width, sprite.hitBox.height);
                }
            }
        }

        for (Bullet bull : bullets) {
            if (bull.isAvatarBullet()) g.setColor(LIGHTBLUE);
            else g.setColor(LIGHTRED);
            g.fillOval(bull.getHitBox().x,bull.getHitBox().y,bull.getHitBox().width,bull.getHitBox().height);
        }
        if (onGround || topDown) {
            if (inMotion) {
                if ((int) step % 2 == 0) {
                    g.drawImage(((Avatar) avatar.instance).sprites[direction][0].getImage(), (int) avatar.locX, (int) avatar.locY, null);
                } else {
                    g.drawImage(((Avatar) avatar.instance).sprites[direction][((int) step + 1) / 2].getImage(), (int) avatar.locX, (int) avatar.locY, null);
                }
            } else {
                g.drawImage(((Avatar) avatar.instance).sprites[direction][0].getImage(), (int) avatar.locX, (int) avatar.locY, null);
            }
        }
        else {
            if (velocityY < 0){
                g.drawImage(((Avatar) avatar.instance).sprites[direction][3].getImage(),(int) avatar.locX, (int) avatar.locY, null);
            }
            else {
                g.drawImage(((Avatar) avatar.instance).sprites[direction][4].getImage(),(int) avatar.locX, (int) avatar.locY, null);
            }
        }
        g.drawRect(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.width,avatar.hitBox.height);
        g.setColor(Color.GRAY);
        g.fillRect(0,750,this.getWidth(),this.getHeight());
        g.setColor(Color.YELLOW);
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
            g.drawString(Integer.toString(health.getCur()),
                    EditPanel.ctrPosition(new Rectangle(healthRect.x+80,healthRect.y,healthRect.width-80,37),Integer.toString(health.getCur()),g),
                    healthRect.y+30);
            g.setColor(Color.WHITE);
            g.drawString(String.format("%d",health.getValue()),
                    EditPanel.ctrPosition(new Rectangle(healthRect.x+80,healthRect.y+37,healthRect.width-80,38),String.format("%d",health.getValue()),g),
                    healthRect.y+healthRect.height+15);
        }
        for (Sprite sprite : enemies) {
            if (((Enemy) sprite.instance).getDrawHealthBar() > 0) {
                g.setColor(Color.BLACK);
                g.fillRect(sprite.hitBox.x,sprite.hitBox.y-20,sprite.hitBox.width,10);
                g.setColor(Color.RED);
                g.fillRect(sprite.hitBox.x, sprite.hitBox.y-20,sprite.hitBox.width*((Enemy) sprite.instance).getHealth()/((Enemy) sprite.instance).getMaxHealth(),10);
            }
        }
        g.setFont(font45);
        if (redKeys > 0) {
            g.drawImage(redKeyImg,800,750,null);
            g.drawString("x" + Integer.toString(redKeys),880,810);
        }
        if (greenKeys > 0) {
            g.drawImage(greenKeyImg,1000,750,null);
            g.drawString("x" + Integer.toString(greenKeys),1080,810);
        }
    }
    public void drawMessage(Graphics g) {
        Point mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(promptBack,50,30,null);
        g.setFont(font45Bold);
        Message cur = (Message) curMessage.instance;
        g.drawString(cur.getTitle(),EditPanel.ctrPosition(this.getBounds(),cur.getTitle(),g),100);
        if (continueRect.contains(mouse)) {
            g.setColor(Color.ORANGE);
            g.fillRect(continueRect.x,continueRect.y,continueRect.width,continueRect.height);
        }
        g.setFont(font45);
        g.setColor(Color.BLACK);
        g.drawRect(continueRect.x,continueRect.y,continueRect.width,continueRect.height);
        g.setFont(font30);
        g.drawString("Continue",EditPanel.ctrPosition(continueRect,"Continue",g),continueRect.y+continueRect.height/2+10);
    }
    public void drawStartMenu(Graphics g) {
        Point mouse = getMousePosition();
        if (mouse == null ) mouse = new Point(0,0);
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.BLACK);
        g.drawImage(promptBack,50,30,null);
        g.setFont(font45Bold);
        g.drawString(name,EditPanel.ctrPosition(this.getBounds(),name,g),100);
        g.drawRect(messageContentPane.getX(),messageContentPane.getY(),messageContentPane.getWidth(),messageContentPane.getHeight());
        g.setFont(font30);
        g.drawString("DIRECTIONS",EditPanel.ctrPosition(this.getBounds(),"DIRECTIONS",g),325);
        g.drawImage(keyboard,100,350,null);
        g.drawImage(labels,780,350,null);
        if (startRect.contains(mouse)) g.setColor(Color.RED);
        else g.setColor(new Color(0,0,0,0));
        g.fillRect(startRect.x,startRect.y,startRect.width,startRect.height);
        g.setFont(font45Bold);
        g.setColor(Color.BLACK);
        g.drawString("START",EditPanel.ctrPosition(startRect,"START",g),startRect.y+2*startRect.height/3);
    }
    public void drawPauseMenu(Graphics g) {
        Point mouse = getMousePosition();
        if (mouse == null ) mouse = new Point(0,0);
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(0,0,getWidth(),getHeight());
        }
        else {
            g.drawImage(background,0,0,null);
        }
        g.setColor(Color.BLACK);
        g.drawImage(promptBack,50,30,null);
        g.setFont(font45Bold);
        g.drawString("PAUSE",EditPanel.ctrPosition(this.getBounds(),"PAUSE",g),100);
        g.setFont(font30);
        g.drawString("DIRECTIONS",EditPanel.ctrPosition(this.getBounds(),"DIRECTIONS",g),325);
        g.drawImage(keyboard,100,350,null);
        g.drawImage(labels,780,350,null);
        if (startRect.contains(mouse)) g.setColor(Color.RED);
        else g.setColor(new Color(0,0,0,0));
        g.fillRect(startRect.x,startRect.y,startRect.width,startRect.height);
        g.setFont(font30);
        g.setColor(Color.BLACK);
        g.drawString("UNPAUSE",EditPanel.ctrPosition(startRect,"UNPAUSE",g),startRect.y+2*startRect.height/3);
    }
    public void drawEndMenu(Graphics g) {
        Point mouse = getMousePosition();
        if (mouse == null ) mouse = new Point(0,0);
        g.setColor(Color.BLACK);
        g.drawImage(promptBack,50,30,null);
        g.setFont(font45Bold);
        g.drawString("GAME OVER",EditPanel.ctrPosition(this.getBounds(),"GAME OVER",g),100);
        g.setFont(font60);
        if (won) {
            g.drawString("YOU WON!!!",EditPanel.ctrPosition(this.getBounds(),"YOU WON!!!",g),200);
        }
        else if (loss) {
            g.drawString("YOU LOST!!!",EditPanel.ctrPosition(this.getBounds(),"YOU LOST!!!",g),200);
        }
        g.setFont(font30);
        String menuScreenStr = "Press ESC to go back to the menu screen!";
        String replayStr = "Press R or the button to replay!";
        g.drawString(menuScreenStr,EditPanel.ctrPosition(this.getBounds(),menuScreenStr,g),300);
        g.drawString(replayStr,EditPanel.ctrPosition(this.getBounds(),replayStr,g),350);
        if (startRect.contains(mouse)) g.setColor(Color.RED);
        else g.setColor(new Color(0,0,0,0));
        g.fillRect(startRect.x,startRect.y,startRect.width,startRect.height);
        g.setColor(Color.BLACK);
        g.drawString("REPLAY",EditPanel.ctrPosition(startRect,"REPLAY",g),startRect.y+2*startRect.height/3);

    }




    public boolean isReachable() {
        if (health != null) {
            if (health.getCur() <= 0) return false;
        }
        if (countdown != null) {
            if (countdown.getTime() < 0) return false;
        }
        if (pointTotal != null) {
            if (coins.size() > 0) return false;
        }
        return true;
    }



    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)+low);
    }
}
