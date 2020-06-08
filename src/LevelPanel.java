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
    private App mainFrame; //storing the frame
    private boolean[] keys;
    private ObjectInputStream inputStream; //deserialization of objects
    private FileInputStream file; //input stream from file
    private boolean topDown; //holds whether the game is top dwon or platform
    private Color backgroundColor; //holds background color
    private Image background;
    private String name,description; //level name and description
    private Countdown countdown; //object that holds the count down data
    private Health health; //object that holds the health data
    private PointTotal pointTotal; //object that holds the point total data
    private Font font15, font25, font30, font45, font45Bold, font60; //different font sizes
    private int timeVar; //controlling the time (100 increments per second)

    private Image countdownImage,healthImage,pointImage; //images for system assets
    private int maxX, maxY; //maximum values for sprite x and y coordinates
    private int offX,offY; //offset x and y
    private Rectangle countdownRect,healthRect,pointTotalRect; //rectangles to draw system assets
    private int direction; //current direction of avatar
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    public static final Color LIGHTBLUE = new Color(173,216,230), LIGHTRED = new Color(252,125,120);

    private Sprite avatar;
    private boolean inMotion;
    private double step; //controls the current sprite to show
    private int immunity, messageImmunity; //ensures the sprite does not constantly come in contact with a block
    private boolean gameOver;
    private Sprite curMessage; //current message being displayed
    private Image promptBack;
    private Image keyboard,labels;
    private Image goalUnreachable;
    private Rectangle startRect;
    private JTextArea messageContentArea;
    private JScrollPane messageContentPane;
    private Rectangle continueRect; //continue from message
    private boolean mouseOn,keyOn; //holds true for when the mouse / keys are pressed
    private boolean letGoOfSpace; //prevents spamming of avatar bullets
    private Image redKeyImg,greenKeyImg;

    //down is positive for velocity, acceleration, and displacement
    private boolean onGround;
    private int velocityY;
    public static final int GRAVITY = 1;

    //detects when to draw special screens
    private boolean onStart;
    private boolean onPause;
    private boolean won, loss;

    //arrraylists for different types of sprites
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
            allSprites = new ArrayList<>(), //every sprite except avatar
            moveRestrictions = new ArrayList<>(); //sprites that would prevent the avatar from moving (blocks, keyhoes, spikes, messages)
    private ArrayList<Bullet> bullets = new ArrayList<>(); //holds all bullets in screen
    private int redKeys, greenKeys; //current amount of keys collected
    //constructor
    public LevelPanel(App a, File filePath) throws IOException, ClassNotFoundException {
        setLayout(null);
        mainFrame = a;
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
        pointImage = new ImageIcon("System/score.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
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


        //deserialization, lots of trial and error to perfect this
        file = new FileInputStream(filePath);
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
        //countdown, health, and point total are not necessarily included in the level; if statements prevent the data from being passed to the wrong class
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
            pointTotal.setCur(0);
            try {
                obj = inputStream.readObject();
            }
            catch (InvalidClassException ex) {}
        }
        while (true) {
            Sprite nxt = (Sprite) obj;
            nxt.hitBox.translate(-500,-150); //translation by the amount because of the original locations on the game rect of the edit panel
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
            sprite.hitBox = new Rectangle(sprite.hitBox.x+5,sprite.hitBox.y+5,sprite.hitBox.width-10,sprite.hitBox.height-10); //making hitbox constraints slightly more free
            sprite.setVisible(true);
        }
        for (Sprite sprite : enemies) {
            ((Enemy) sprite.instance).setHealth(((Enemy) sprite.instance).getHealth());
            ((Enemy) sprite.instance).setBulletPeriod(200);
            if (((Enemy) sprite.instance).isStationary()) {
                ((Enemy) sprite.instance).setSpeed(0);
            }
            if (!topDown){
                ((Enemy) sprite.instance).setOnGround(false); //if enemies start on air, they will land on ground
                ((Enemy) sprite.instance).setVelocityY(0);
                ((Enemy) sprite.instance).setJumpPeriod(randint(300,600));
                ((Enemy) sprite.instance).setJumpTimer(0);
                ((Enemy) sprite.instance).setInitialVelocityY(-1*randint(15,30));

            }
        }
        //loading text ares
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
        if (pointTotal != null) {
            for (Sprite sprite : coins) {
                pointTotal.increase(((Coin) sprite.instance).getPts());
            }
        }
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    //method that handles movement, whether its the avatar sprite moving or all the other sprites moving in the opposite direction
    public void topDownMove(int dir) {
        if (dir == RIGHT) {
            if (avatar.locX + avatar.hitBox.width < getWidth()/3 || offX + avatar.locX > maxX-2*getWidth()/3)
                avatar.translate(((Avatar) avatar.instance).getSpeed(),0); //moving avatar right
            else {
                offX++;
                for (Sprite sprite : allSprites) sprite.translate(-1*((Avatar) avatar.instance).getSpeed(),0); //moving all other sprites left
            }

        } else if (dir == UP) {
            if (avatar.locY + avatar.hitBox.height > getHeight()/3 || offY <= 0)
                avatar.translate(0,-1*((Avatar) avatar.instance).getSpeed()); //moving avatar up
            else {
                offY--;
                for (Sprite sprite : allSprites) sprite.translate(0,((Avatar) avatar.instance).getSpeed()); //moving all other sprites down
            }
        } else if (dir == LEFT) {
            if (avatar.locX + avatar.hitBox.width > getWidth()/3 || offX <= 0)
                avatar.translate(-1*((Avatar) avatar.instance).getSpeed(),0); //moving avatar left
            else {
                offX--;
                for (Sprite sprite : allSprites) sprite.translate(((Avatar) avatar.instance).getSpeed(),0); //moving all other sprites right
            }
        } else if (dir == DOWN) {
            if (avatar.locY + avatar.hitBox.height < getHeight()/3 || offY + avatar.locY > maxY-2*getHeight()/3)
                avatar.translate(0,((Avatar) avatar.instance).getSpeed()); //moving avatar down
            else {
                offY++;
                for (Sprite sprite : allSprites) sprite.translate(0,-1*((Avatar) avatar.instance).getSpeed()); //moving all sprites up
            }
        }

        step += 0.05; //incrementing step to eventually change the current sprite to display
        if (step >= 4) step -= 4;
    }
    //method that moves enemies
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
        ((Enemy) curSprite.instance).increaseStep(); //incrementing the step to eventually change the current sprite on display
    }
    //method that handles all the logic with the game, uses the other methods that are not draw methods in order to assist the update method
    public void update() throws IOException, ClassNotFoundException {
        Point mouse = getMousePosition(); //storing mouse position
        if (mouse == null ) mouse = new Point(0,0);
        if (gameOver) {
            if (keys[KeyEvent.VK_R] || (startRect.contains(mouse) && mouseOn)) { //if the user wants to replay
                mainFrame.switchPanel(mainFrame.LEVELPANEL); //switching the panel to another game
            }
            else if (keys[KeyEvent.VK_ESCAPE]) {
                mainFrame.switchPanel(mainFrame.SELECTPANEL); //swithcing the panel to the menu
            }
        }
        if (avatar.locY > maxY + 150 || avatar.locX > maxX) { //if the avatar has fallen or moved too far from the acutal sprites, it is assumed that they lost
            gameOver = true;
            loss = true;
        }
        else if (health != null && health.getCur() <= 0) { //if health constraints were added and the user ran out
            gameOver = true;
            loss = true;
        }
        else if (countdown != null && countdown.getTime() <= 0) { //if time constraints were added and the use ran out
            gameOver = true;
            loss = true;
        }
        if (onStart) { //on start screen
            if (startRect.contains(mouse) && mouseOn) { //user wants to start the game
                onStart = false;
                messageContentPane.setVisible(false);
                messageContentArea.setVisible(false);
                messageContentArea.setBounds(messageContentArea.getX(),messageContentArea.getY(),messageContentArea.getWidth(),messageContentArea.getHeight()*500/150);
                messageContentPane.setBounds(messageContentArea.getBounds());
            }
        }
        if (onPause) { //if the user wanted to unpause from a pause menu, the game continues
            if (startRect.contains(mouse) && mouseOn) {
                onPause = false;
            }
            return;
        }
        if (curMessage != null) { //if a message is currently being displayed
            if (continueRect.contains(mouse) && mouseOn) { //user wants to leave, has read the message already
                curMessage = null;
                messageContentPane.setVisible(false);
                messageContentArea.setVisible(false);
                if (!topDown) messageImmunity = 200;
            }
            return; //ensures that no other functionality is called after this
        }
        else if (messageImmunity > 0) messageImmunity--; //message immunity decreases, meaning the user will be able to view a message after a certain time after they opened one
        timeVar++; //increasing the time count
        ArrayList<Bullet> bulletDeletions = new ArrayList<>(); //bullets that are to be removed due to collisions
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (bullets.get(i).getHitBox().intersects(bullets.get(j).getHitBox()) && bullets.get(i).isAvatarBullet() != bullets.get(j).isAvatarBullet()) {
                    bulletDeletions.add(bullets.get(i));
                    bulletDeletions.add(bullets.get(j));
                }
            }
        }
        bullets.removeAll(bulletDeletions);
        Bullet.decrementAvatarTime(); //allows the user to not spam, but shoot another bullet after a certain amount of time after releasing one
        ArrayList<Sprite> enemyDeletions = new ArrayList<>();
        for (Sprite sprite : enemies) {
            if (((Enemy) sprite.instance).getHealth() == 0) { //enemy ran out of health
                enemyDeletions.add(sprite);
            }
            else {
                ((Enemy) sprite.instance).decrementHealhtBar(); //shows health bar for a certain time after an enemy takes damage
                ((Enemy) sprite.instance).incrementTimer(); //timing for periodic bullets
                if (((Enemy) sprite.instance).getBulletSpeed() > 0 && ((Enemy) sprite.instance).getBulletTimer() == 0) { //if ready to shoot
                    //adding bullet in direction the enemy is facing
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
        enemies.removeAll(enemyDeletions); //removing all obsolete enemies
        allSprites.removeAll(enemyDeletions);
        if (timeVar == 100 && countdown != null) { //if the timer is in effect, and there are 100 updates per second, will decrement time
            timeVar = 0;
            countdown.decrement();
        }
        immunity = max(immunity-1,0); //making sure the avatar can take damage after a certain period of time of being hit
        if (keys[KeyEvent.VK_SPACE] && Bullet.avatarReadyToShoot() && letGoOfSpace) { //if the avatar wants to shoot, and the timing hsa been enough since their last press
            if (direction == RIGHT) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height/2,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == LEFT) bullets.add(new Bullet(avatar.hitBox.x, avatar.hitBox.y+avatar.hitBox.height/2,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == UP && topDown) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width/2,avatar.hitBox.y,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            else if (direction == DOWN && topDown) bullets.add(new Bullet(avatar.hitBox.x+avatar.hitBox.width/2,avatar.hitBox.y+avatar.hitBox.height,
                    direction,true,((Avatar) avatar.instance).getBulletSpeed(),((Avatar) avatar.instance).getDamage()));
            Bullet.setAvatarTime(); //resetting the amount of time the avatar has to wait before shooting another bullet
            letGoOfSpace = false;
        }
        if (!keys[KeyEvent.VK_SPACE] && !letGoOfSpace) letGoOfSpace = true;
        for (int i = bullets.size()-1; i >= 0; i--) {
            //prevents index out of bounds due to concurrent modifications
            while (i >= bullets.size()) i--;
            if (bullets.size() == 0) break;
            Bullet curBullet = bullets.get(i);
            //moving bullets
            if (curBullet.getDir() == RIGHT) curBullet.translate(curBullet.getSpeed(),0);
            else if (curBullet.getDir() == LEFT) curBullet.translate(-1*curBullet.getSpeed(),0);
            else if (curBullet.getDir() == UP) curBullet.translate(0,-1*curBullet.getSpeed());
            else if (curBullet.getDir() == DOWN) curBullet.translate(0,curBullet.getSpeed());

            boolean removedIdx = false;
            //removal of bullet due to being in an unpopulated location
            if ((curBullet.getHitBox().x < 0 || curBullet.getHitBox().x > maxX+150) && onGround) {
                bullets.remove(i);
                removedIdx = true;
            }
            else if ((curBullet.getHitBox().y < 0 || curBullet.getHitBox().y > maxY+150) && onGround) {
                bullets.remove(i);
                removedIdx = true;
            }
            if (removedIdx) continue;
            //removal of bullet due to collision with other sprites
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
            if (curBullet.isAvatarBullet()) { //handles the avatar's bullets colliding with the enemy
                for (Sprite sprite : enemies) {
                    if (sprite.hitBox.intersects(curBullet.getHitBox())) {
                        ((Enemy) sprite.instance).setDrawHealthBar(200);
                        ((Enemy) sprite.instance).setCurHealth(max(0,((Enemy) sprite.instance).getHealth()-curBullet.getDamage()));
                        bullets.remove(i);
                    }
                }
            }
            else { //handles the enemy's bullets colliding with the avatar
                if (avatar.hitBox.intersects(curBullet.getHitBox())){
                    ((Avatar) avatar.instance).setDrawHealthBar(200);
                    ((Avatar) avatar.instance).setHealth(max(0,((Avatar) avatar.instance).getHealth()-curBullet.getDamage()));
                    bullets.remove(i);
                }
            }
        }
        for (Sprite sprite : goals) {
            if (sprite.hitBox.intersects(avatar.hitBox)) {
                if (!((Goal) sprite.instance).getMaskID().equals("blank")) { //takes out the mask
                    ((Goal) sprite.instance).setMaskID("blank");
                }
                if (isReachable()) { //if all constraints are met to open the goal and they reached it, they won the game
                    gameOver = true;
                    won = true;
                    return;
                }

            }
        }

        if (topDown) {
            //handles the direction of movement
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
                if (checkTopDown(avatar,direction)) { //if there is nothing in the way of movement
                    topDownMove(direction); //moves the avatar
                }

            }

            for (Sprite sprite : enemies) {
                if (((Enemy) sprite.instance).isStationary()) continue;
                if (!checkTopDown(sprite, ((Enemy) sprite.instance).getDirection())) { //reverses the enemy's direction if something is in their way
                    ((Enemy) sprite.instance).setDirection((((Enemy) sprite.instance).getDirection() + 2) % 4);
                }
                topDownMove(((Enemy) sprite.instance).getDirection(), sprite); //moves the enemy
            }



            if (!gameOver) { //does not run if there was a deciding factor in the game that was previously made
                for (Sprite sprite : messages) {
                    if (sprite.hitBox.intersects(avatar.hitBox)) { //if the sprite runs into a message block, it will show along with all the text
                        curMessage = sprite;
                        messageContentArea.setVisible(true); messageContentPane.setVisible(true);
                        messageContentArea.setText(((Message) sprite.instance).getContent());
                        //the avatar is pushed back in the opposite direction of arrival by a very small amount
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
                        if (sprite.hitBox.intersects(avatar.hitBox)) { //if a spike has come in contact with the avatar, it takes damage and moves back slighly
                            notIntersectWithSpike = false;
                            if (sprite.hitBox.x - avatar.hitBox.x > 40) topDownMove(LEFT);
                            else if (avatar.hitBox.x - sprite.hitBox.x > 40) topDownMove(RIGHT);
                            else if (sprite.hitBox.y - avatar.hitBox.y > 40) topDownMove(UP);
                            else if (avatar.hitBox.y - sprite.hitBox.y > 40) topDownMove(DOWN);
                            if (immunity == 0) { //only takes damage if there was a substantial amount of time taken since the last time damage was taken
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
                                if (((Teleport) sprite.instance).isEntry()) { //moves the avatar from one teleport end to the other by shifting all sprites in the opposite direction
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
                                if (sprite.hitBox.intersects(avatar.hitBox)) { //the time bonuses will have their value added to the count down
                                    if (countdown != null) {
                                        countdown.increease(((TimeBonus) sprite.instance).getValue());
                                        break;
                                    }
                                    deleteTimeBonus = sprite;
                                }
                            }
                            if (deleteTimeBonus != null) { //a time bonus is lost once ran into
                                timeBonuses.remove(deleteTimeBonus);
                                allSprites.remove(deleteTimeBonus);
                            }

                            Sprite deleteCoin = null;
                            for (Sprite sprite : coins) {
                                if (sprite.hitBox.intersects(avatar.hitBox)) { //coins increase the point total if ran into
                                    if (pointTotal != null) {
                                        pointTotal.setCur(pointTotal.getCur() + ((Coin) sprite.instance).getPts());
                                    }
                                    deleteCoin = sprite;
                                }
                            }
                            if (deleteCoin != null) { //the coin is lost after collision and adding to the point total
                                coins.remove(deleteCoin);
                                allSprites.remove(deleteCoin);
                            }
                            Sprite deleteHealthBonus = null;
                            for (Sprite sprite : healthBonuses) { //health bonuses increase the total health the avatar has
                                if (sprite.hitBox.intersects(avatar.hitBox)) {
                                    if (health != null) {
                                        health.setCur(min(health.getCur() + ((Health) sprite.instance).getValue(), health.getValue()));
                                    }
                                    deleteHealthBonus = sprite;
                                }
                            }
                            if (deleteHealthBonus != null) { //a health bonus is lost once it has been used
                                healthBonuses.remove(deleteHealthBonus);
                                allSprites.remove(deleteHealthBonus);
                            }
                            Sprite deleteKeyHole = null;
                            for (Sprite sprite : keyHoles) {
                                if (sprite.hitBox.intersects(avatar.hitBox)) {
                                    //keyholes open if the user has enough keys of the required color
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
                            if (deleteKeyHole != null) { //the key hole obstacle is removed
                                keyHoles.remove(deleteKeyHole);
                                allSprites.remove(deleteKeyHole);
                                moveRestrictions.remove(deleteKeyHole);
                            }
                            Sprite deleteKeyInsert = null;
                            for (Sprite sprite : keyInserts) { //keys that are ran into are added to the collection
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
                            if (deleteKeyInsert != null) { //the key is removed from the game map
                                keyInserts.remove(deleteKeyInsert);
                                allSprites.remove(deleteKeyInsert);
                            }
                            if (immunity == 0) {
                                for (Sprite sprite : enemies) {
                                    if (sprite.hitBox.intersects(avatar.hitBox)) { //if the avatar and an enemy collide, then the avatar takes damage
                                        ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - ((Enemy) sprite.instance).getDamage(),0));
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
            for (Sprite sprite : messages) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //if ran into a message, it will appear in the next update
                    curMessage = sprite;
                    return;
                }
            }
            //movement is only left and right, as well as jumping
            if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) {
                direction = RIGHT;
                inMotion = true;
            }
            else if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) {
                direction = LEFT;
                inMotion = true;
            }
            if (inMotion) {
                if (checkTopDown(avatar,direction)) { //checking if the avatar can move left and right
                    topDownMove(direction);
                }

            }
            if (!onGround) { //if in midair, or claimed to be midair
                velocityY += GRAVITY; //v2 = v1 + at
                //instead of translating the avatar, the other sprites and bullets move in the opposite direction to remove the worry of scrolling in the y-axis
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
                    if (sprite.hitBox.intersectsLine(avatarBorderDown)) { //if there is a block that is below it, that is considered to be some sort of ground
                        velocityY = 0;
                        onGround = true;
                        //shifting by the amount the avatar dug into the block
                        for (Sprite sp : allSprites) {
                            sp.translate(0, -curYTop + avatar.hitBox.y + avatar.hitBox.height);
                        }
                        for (Bullet b : bullets) {
                            b.translate(0, -curYTop + avatar.hitBox.y + avatar.hitBox.height);
                        }
                        if (sprite.instance instanceof KeyHole) { //keyh oles will open if the user had enough keys and landed on top of the key hole
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
                        //avatar that jumps and hits a block on the head will remove all of its upward velocity, sending it back down
                        velocityY = 0;
                        onGround = false;
                        for (Sprite sp : allSprites) {
                            sp.translate(0, -curYBottom + avatar.hitBox.y);
                        }
                        for (Bullet b : bullets) {
                            b.translate(0, -curYBottom + avatar.hitBox.y);
                        }
                        if (sprite.instance instanceof KeyHole) { //key hole is removed if the head of the avatar collided with the key hole, making sure the avatar had enough keys
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
                if (deleteKeyHole != null) { //removing any key holes that were hit
                    keyHoles.remove(deleteKeyHole);
                    allSprites.remove(deleteKeyHole);
                    moveRestrictions.remove(deleteKeyHole);
                }

            }
            else {
                //lines of the avatar borders to check for collision in specific areas
                Line2D avatarBorderDown = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height),
                        avatarBorderLeft = new Line2D.Double(avatar.hitBox.x,avatar.hitBox.y,avatar.hitBox.x,avatar.hitBox.y+avatar.hitBox.height-1),
                        avatarBorderRight = new Line2D.Double(avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y,avatar.hitBox.x+avatar.hitBox.width,avatar.hitBox.y+avatar.hitBox.height-1 );
                if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) && onGround) { //if the avatar wanted to jump
                    boolean jump = true;
                    for (Sprite sprite : moveRestrictions) { //checking if there are any sprites above on the sides making contact, would prevent the jump
                        if (sprite.hitBox.intersectsLine(avatarBorderLeft)) {
                            jump = false;
                            break;
                        }
                        if (sprite.hitBox.intersectsLine(avatarBorderRight)) {
                            jump = false;
                            break;
                        }
                    }
                    if (jump) { //giving the avatar upward velocity off the ground
                        onGround = false;
                        velocityY = -30;
                    }
                }
                boolean interWithSprite = false;
                for (Sprite sprite : moveRestrictions) { //checking to see if the avatar is really standing on a block
                    if (sprite.hitBox.intersectsLine(avatarBorderDown)) {
                        interWithSprite = true;
                        break;
                    }
                }
                if (!interWithSprite) { //ensures that the only sprites to be on ground are those that actually stand on something
                    onGround = false;
                }
            }
            for (Sprite sprite : enemies) {
                if (((Enemy) sprite.instance).isStationary()) continue;
                if (!checkTopDown(sprite, ((Enemy) sprite.instance).getDirection())) { //reverses the enemy's direction if something is in their way
                    ((Enemy) sprite.instance).setDirection((((Enemy) sprite.instance).getDirection() + 2) % 4);
                }
                topDownMove(((Enemy) sprite.instance).getDirection(), sprite); //moves the enemy
            }
            for (Sprite sprite : enemies) {
                Enemy enemy = (Enemy) sprite.instance;
                if (!enemy.isOnGround()) {
                    enemy.setVelocityY(enemy.getVelocityY() + GRAVITY); //changing the velocity of the enemy, v2 = v1 + at
                    sprite.translate(0,enemy.getVelocityY()); //moving the enemy downwards from a jump
                    for (Sprite restriction : moveRestrictions) {
                        //similar approach to the avatar constraints
                        Line2D enemyBorderUp = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y),
                                enemyBorderDown = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height),
                                enemyBorderLeft = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height),
                                enemyBorderRight = new Line2D.Double(enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height);

                        if (restriction.hitBox.intersectsLine(enemyBorderDown)) { //if the enemy is on top of a block, it is classified as being on ground
                            enemy.setVelocityY(0);
                            enemy.setOnGround(true);
                            sprite.translate(0,restriction.hitBox.y-(sprite.hitBox.y+sprite.hitBox.height)); //preventing the digging in of the enemy
                        }
                        if (restriction.hitBox.intersectsLine(enemyBorderUp)) { //an enemy directly below a block will take away all the upward velocity
                            velocityY = 0;
                            sprite.translate(0,restriction.hitBox.y+restriction.hitBox.height-sprite.hitBox.y);

                        }

                    }
                }
                else {
                    Line2D enemyBorderUp = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y),
                            enemyBorderDown = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height),
                            enemyBorderLeft = new Line2D.Double(enemy.hitBox.x,enemy.hitBox.y,enemy.hitBox.x,enemy.hitBox.y+enemy.hitBox.height),
                            enemyBorderRight = new Line2D.Double(enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y,enemy.hitBox.x+enemy.hitBox.width,enemy.hitBox.y+enemy.hitBox.height);

                    boolean interWithSprite = false;
                    for (Sprite sp : moveRestrictions) { //checking to see if the avatar is really standing on a block
                        if (sp.hitBox.intersectsLine(enemyBorderDown)) {
                            interWithSprite = true;
                            break;
                        }
                    }
                    if (!interWithSprite) { //ensures that the only sprites to be on ground are those that actually stand on something
                        onGround = false;
                    }
                    if (enemy.incJumpTimer()) { //if the enemy has waited long enough since last jump, it will jump
                        enemy.setVelocityY(enemy.getInitialVelocityY());
                        enemy.setOnGround(false);
                    }
                }
            }
            for (Sprite sprite : spikes) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //collision between enemy and avatar
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
                    if (((Teleport) sprite.instance).isEntry()) { //moving avatar to other end of teleporter
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
                if (sprite.hitBox.intersects(avatar.hitBox)) { //adding time bonus to countdown
                    if (countdown != null) {
                        countdown.increease(((TimeBonus) sprite.instance).getValue());
                    }
                    deleteTimeBonus = sprite;
                }
            }
            if (deleteTimeBonus != null) {
                timeBonuses.remove(deleteTimeBonus); //removing time bonus from game map
                allSprites.remove(deleteTimeBonus);
            }
            Sprite deleteCoin = null;
            for (Sprite sprite : coins) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //adding coin value to the total point count
                    if (pointTotal != null) {
                        pointTotal.setCur(pointTotal.getCur() + ((Coin) sprite.instance).getPts());
                    }
                    deleteCoin = sprite;
                }
            }
            if (deleteCoin != null) { //removing the collected coin from the game map
                coins.remove(deleteCoin);
                allSprites.remove(deleteCoin);
            }
            Sprite deleteHealthBonus = null;
            for (Sprite sprite : healthBonuses) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //adding health value to the avatar's life
                    if (health != null) {
                        health.setCur(min(health.getCur() + ((Health) sprite.instance).getValue(), health.getValue()));
                    }
                    deleteHealthBonus = sprite;
                }
            }
            if (deleteHealthBonus != null) { //removing from game map
                healthBonuses.remove(deleteHealthBonus);
                allSprites.remove(deleteHealthBonus);
            }
            Sprite deleteKeyHole = null;
            for (Sprite sprite : keyHoles) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //collision with key holes, they open if the avatar has enough keys of the correct color
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
            if (deleteKeyHole != null) { //removing the key hole obstacle from map
                keyHoles.remove(deleteKeyHole);
                allSprites.remove(deleteKeyHole);
                moveRestrictions.remove(deleteKeyHole);
            }
            Sprite deleteKeyInsert = null;
            for (Sprite sprite : keyInserts) {
                if (sprite.hitBox.intersects(avatar.hitBox)) { //taking keys from map and adding it to the amount of keys collected by avatar
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
            if (deleteKeyInsert != null) { //removing key from game map
                keyInserts.remove(deleteKeyInsert);
                allSprites.remove(deleteKeyInsert);
            }
            if (immunity == 0) {
                for (Sprite sprite : enemies) {
                    if (sprite.hitBox.intersects(avatar.hitBox)) { //collision with enemy and avatar will do damage to the avatar
                        ((Avatar) avatar.instance).setHealth(max(((Avatar) avatar.instance).getHealth() - ((Enemy) sprite.instance).getDamage(),0));
                        if (health != null) {
                            health.setCur(max(((Avatar) avatar.instance).getHealth() - ((Enemy) sprite.instance).getDamage(), 0));
                        }
                        immunity = 300;
                    }
                }
            }


        }
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
        if (curSprite.getId() == avatar.getId()) { //if the sprite is the avatar
            if (curSprite.hitBox.x < ((Avatar) curSprite.instance).getSpeed() && dir == LEFT) return false;
            if (curSprite.hitBox.y < ((Avatar) curSprite.instance).getSpeed() && dir == UP) return false;
            if (curSprite.hitBox.x + ((Avatar) curSprite.instance).getSpeed() > maxX && dir == RIGHT) return false;
            if (curSprite.hitBox.y + ((Avatar) curSprite.instance).getSpeed() > maxY && dir == DOWN) return false;

        }
        else { //otherwise, the sprite is the enemy
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



        return true; //no collisions, therefore the sprite can move
    }

    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyChar() == 'p' && !keyOn) { //clicking p will result in a pause/unpause
            if (!onStart && !won && !loss) { //only done if the user is in the game
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
        mouseOn = true;
    }
    public void mouseReleased(MouseEvent e) {
        mouseOn = false;
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void paintComponent(Graphics g) {
        g.setFont(font30);
        if (countdownRect == null) { //initializing objects that require the use of the graphics
            int paintX = 0;
            countdownRect = new Rectangle(0,750,80+g.getFontMetrics().stringWidth("00:00:00"),75);
            System.out.println(g.getFont().getSize());

            paintX = countdownRect.x + countdownRect.width + 10;
            int paintY = countdownRect.y;
            pointTotalRect = new Rectangle(paintX,paintY,150,50);

            paintX += pointTotalRect.width;
            healthRect = new Rectangle(paintX,paintY,150,50);
        }
        if (curMessage != null) {
            drawMessage(g); //drawing message if it exists
            return;
        }
        if (onStart) {
            drawStartMenu(g); //drawing start menu if game has not started
            return;
        }
        else if (onPause) {
            drawPauseMenu(g); //drawing pause menu
            return;
        }
        else if (gameOver) {
            drawEndMenu(g); //drawing game over menu
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
                    if (sprite.instance instanceof Enemy) { //drawing the most updated sprite for the enemy
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
                        else { //drawing the enemy sprite that is facing upwards/downwards
                            if (((Enemy) sprite.instance).getVelocityY() < 0) {
                                g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][3].getImage(), sprite.locX, sprite.locY, null);
                            }
                            else {
                                g.drawImage(((Enemy) sprite.instance).sprites[((Enemy) sprite.instance).getDirection()][4].getImage(), sprite.locX, sprite.locY, null);
                            }
                        }
                    }
                    else if (sprite.instance instanceof Goal) {
                        if (((Goal) sprite.instance).getMaskID().equals("blank")) { //if there is no mask, the goal is drawn
                            if (isReachable()) {

                                g.drawImage(sprite.getImg().getImage(),sprite.locX,sprite.locY,null);
                            }
                            else {
                                g.drawImage(goalUnreachable,sprite.locX,sprite.locY,null);
                            }
                        }
                        else { //the mask is drawn instead
                            g.drawImage(((Goal) sprite.instance).getMask().getImage(),sprite.locX,sprite.locY,null);
                        }
                    }
                    else { //all other sprties just have a regular image
                        g.drawImage(sprite.getImg().getImage(), sprite.locX, sprite.locY, null);
                    }
                    g.setColor(Color.BLACK);
                }
            }
        }

        for (Bullet bull : bullets) { //drawing bullets
            if (bull.isAvatarBullet()) g.setColor(LIGHTBLUE);
            else g.setColor(LIGHTRED);
            g.fillOval(bull.getHitBox().x,bull.getHitBox().y,bull.getHitBox().width,bull.getHitBox().height);
        }
        //drawing the avatar sprite
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
        //drawing the point total
        if (pointTotal != null) {
            g.setFont(font30);
            g.drawImage(pointImage,pointTotalRect.x,pointTotalRect.y,null);
            g.setColor(Color.YELLOW);
            g.drawLine(pointTotalRect.x+80,pointTotalRect.y+37,pointTotalRect.x+pointTotalRect.width,pointTotalRect.y+37);
            g.setColor(Color.BLUE);
            //drawing the numerator of the fraction of killed/total
            g.drawString(Integer.toString(pointTotal.getCur()),EditPanel.ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y,pointTotalRect.width-80,37),Integer.toString(pointTotal.getCur()),g),pointTotalRect.y+30);
            g.setColor(Color.WHITE);
            //darwing the denominator of the fraction of killed/total
            g.drawString(String.format("%d",pointTotal.getTotal()), EditPanel.ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y+37,pointTotalRect.width-80,38),String.format("%d",pointTotal.getTotal()),g),pointTotalRect.y+pointTotalRect.height+15);
        }//drawing the health bars of the enemies
        for (Sprite sprite : enemies) {
            if (((Enemy) sprite.instance).getDrawHealthBar() > 0) {
                g.setColor(Color.BLACK);
                g.fillRect(sprite.hitBox.x,sprite.hitBox.y-20,sprite.hitBox.width,10);
                g.setColor(Color.RED);
                g.fillRect(sprite.hitBox.x, sprite.hitBox.y-20,sprite.hitBox.width*((Enemy) sprite.instance).getHealth()/((Enemy) sprite.instance).getMaxHealth(),10);
            }
        }
        g.setFont(font45);
        if (redKeys > 0) { //drawing the count for the red keys
            g.drawImage(redKeyImg,800,750,null);
            g.drawString("x" + Integer.toString(redKeys),880,810);
        }
        if (greenKeys > 0) { //drawing the count for the green keys
            g.drawImage(greenKeyImg,1000,750,null);
            g.drawString("x" + Integer.toString(greenKeys),1080,810);
        }
    }
    public void drawMessage(Graphics g) {
        //message that draws the message menu
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
        //method that draws the start menu
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
        //method that draws the pause menu
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
        //method that draws the game over menu
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




    public boolean isReachable() { //determines whether the goal should be standing out (as in all condiitons are satisfied, meaning touching a goal would result in a win)
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


    //random integer from low to high, inclusive
    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)+low);
    }
}
