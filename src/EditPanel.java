/*
EditPanel.java
Sat Arora
Panel that allows for the creation or editing of a level
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditPanel extends JPanel implements MouseListener, KeyListener, java.io.Serializable {
    private Edit mainFrame; //storing the frame
    private Image coolBack; //background of the edit panel
    public static Image[] systemSprites,itemSprites,blockSprites,playerPlatSprites,enemyPlatSprites,playerTopDownSprites,enemyTopDownSprites; //loading sprites
    public static String[] systemStrings,itemStrings,blockStrings,playerPlatStrings,enemyPlatStrings,playerTopDownStrings,enemyTopDownStrings; //names of sprites in the image files, easier to load
    private Rectangle avatarRect,enemyRect,blockRect,itemRect,systemRect,spritesRect; //rectangles for labels for type of sprites
    private Rectangle gameRect; //rectangle that the game is played in
    private Image title; //title stored as image because fonts were unreachable

    public static final int AVATAR = 0, ENEMY = 1, BLOCK = 2, ITEM = 3, SYSTEM = 4; //ints for the sprite types
    private int curType; //the current type of sprite (used to display the right sprites available to put into the game)

    private Font font;
    public static final Color BROWN = new Color(210,105,30),
            TRANSPARENTRED = new Color(255,0,0,100),
            TRANSPARENTGREEN = new Color(0,255,0,100),
            TRANSPARENTBLUE = new Color(0,0,255,100) ;
    private Point mouse; //stores mouse position
    public static final int MOUSE = 0, TOOL = 1, DELETE = 2; //tools ints
    public int curTool = MOUSE; //current tool selected
    private Image[] toolImages;
    private Image[] toolClickedImages;
    private String[] toolStrings;
    private Ellipse2D.Double[] toolEllipses; //used for precise collision and drawing of tool icons
    private Ellipse2D.Double upEllipse, downEllipse, leftEllipse, rightEllipse, yesEllipse, noEllipse; //ellipses used for settings
    private String curSprite; //current sprite that is chosen
    private ImageIcon curImage; //current image that is chosen
    private boolean readyToPaste,readyToDelete,readyToModify; //booleans holding the current state of modification
    private boolean containsAvatar,isAvatar; //booleans that control there being only one avatar sprite in the entire game
    private boolean changeAvatarPrompt; //boolean that will show a change avatar screen if multiple avatars are present
    private int avatarX,avatarY,newavatarX,newavatarY; //holds position of the current avatar location
    private boolean[] keys;
    private Image promptBack,buttonImage,buttonClickedImage;
    private Rectangle yesRect,noRect; //also used for save/cancel
    private Avatar avatar; //avatar object stores the current avatar
    private Enemy curEnemy; //enemy object stores the current enemy chosen
    private Goal curGoal; //stores the current goal chosen
    private boolean changeAvatarSettings, changeEnemySettings,changeGoalSettings,changeMessageSettings,
            changeKeyHoleSettings,changeSpikeSettings,changeCoinSettings,changeHealthBonusSettings,
            changeTimeBonusSettings,changeKeyInsertSettings; //booleans that will show custom settings to the sprite type
    public static final int SPEED = 0, HEALTH = 1, BULLETSPEED = 2, DAMAGESPRITE = 3, STATIONARY = 4, DIRECTION = 5; //enemy and avatar settings headers
    public static final String[] avatarSettings = new String[]{"Speed","Health","Bullet Speed","Damage"},
            enemySettings = new String[]{"Speed","Health","Bullet Speed","Damage","Stationary","Direction"},
            goalSettings = new String[]{"Mask","Unlock"},
            goalMasks = new String[]{"None","Cement","Cloud","Dirt","Glass","Gold","Grass"},
            messageSettings = new String[]{"Title","Content"},
            keyHoleSettings = new String[]{"Unlock Requirement"},
            spikeSettings = new String[]{"Damage"},
            coinSettings = new String[]{"Points"},
            healthBonusSettings = new String[]{"Value"},
            timeBonusSetings = new String[]{"Value (s)"},
            keyInsertSettings = new String[]{"Value"}; //arrays that hold all the settings
    public static final int MASK = 0, UNLOCK = 1; //ints for goal settings
    private Rectangle[] avatarSettingsRects, enemySettingsRects, goalSettingsRects,messageSettingsRects,healthBonusSettingsRects,timeBonusSettingsRects,
            keyInsertSettingsRects; //rectangle arrays for locations of settings headers
    private int curSetting; //holds the current settings chosen
    private Rectangle[] oneToTenBlocks,oneToFiveBlocks; //rectangle arrays that hold the locations of the settings that require a scroll bar visual
    private int unsavedSpeed,unsavedHealth,unsavedDirection,unsavedBulletSpeed; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private boolean unsavedStationary; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private ImageIcon unsavedMask; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private String unsavedMaskID; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private int[] unsavedArrPoints; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private int dialogCtr; //holding the x-coordinate of the center of the settings menus
    public static final int TITLE = 0, CONTENT = 1; //ints that hold the current setting options for the message object
    private JTextArea titleArea,contentArea; //text areas to display messages
    private JScrollPane contentPane; //content area for encasing the content area for scrollbar support
    private Message curMessage; //holding the current message object
    public static final int UNLOCKREQUIREMENT = 0; //ints that hold the settings for the key hole
    private KeyHole curKeyHole; //storing the current key hole selected
    public static final int DAMAGE = 0; //int that holds the setting for the spike object
    private Spike curSpike; //storing the current spike used
    private int unsavedDamage; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    public static final int POINTS = 0; //int that holds the setting for the coin object
    private Coin curCoin; //storing the current coin object
    private int unsavedPts; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private boolean insertTeleport; //boolean that determines that an exit teleport must be inserted to partner with an entry
    private Teleport entry,exitt; //storing the current entry and exit teleports
    private Image teleportOut; //seperate exit teleport image
    private String teleportOutID; //file name of exit teleport image

    private HealthBonus curHealthBonus; //the current health bonus object chosen
    private int unsavedValue; //holding values while customizing sprites allows for the objects to keep old data if the cancel button is pressed
    private TimeBonus curTimeBonus; //the current time bonus object chosen
    private KeyInsert curKeyInsert; //the current key insert object chosen

    private boolean changeCountdownSettings,changeHealthSettings; //booleans to display settings for specified objects
    private String[] countdownSettings = {"Time"}; //headers for in-setting menus
    private Rectangle countDownRect,pointTotalRect,healthRect; //rectangles to display game assets on screen
    private Rectangle[] countdownSettingsRects; //location for in-setting menus
    private Countdown curCountdown; //the current count down object
    private PointTotal curPointTotal; //the current point total object used
    private Health curHealth; //the current health object used
    private Image left,right,up,down; //images for the buttons to scroll the game
    private Rectangle leftRect,rightRect,upRect,downRect; //rectangle collsions (using ellipses would not make that much of a difference as the scroll buttons are small)
    private int offX,offY; //storing the current offset
    private int numGoals =0; //holding the number of goals
    private boolean changeLevelSettings; //boolean for displaying level settings
    private String levelName, levelDescription; //name and description of level
    private ImageIcon levelBackground,unsavedBackground; 
    private boolean topDown; //stores whether the game type is top down or platform
    private String[] levelSettings; //headers for level settings menu
    private Rectangle[] levelSettingsRects; //rectangles for location of headers of level settings
    private Rectangle settingsRect; //rectangle for the level settings button
    private Color backgroundColor,unsavedBackgroundColor;
    public static final int NAME = 0, DESCRIPTION = 1, BACKGROUND = 2, NATURE = 3; //ints used for the options for level settings
    private Image[] backgrounds,miniBackgrounds; //options for backgrounds, and miniature options for displaying in settings menu
    private String[] backgroundStrings; //names of backgrounds to load files
    private Rectangle[] miniBackgroundsRects; //rectangle array that allow for background selection
    private JTextArea[] rgbAreas; //areas that allow to change RGB values
    private Rectangle previewColorRect; //rectangle on the side of level settings that is filled with the current level background
    private Rectangle topDownRect,platformRect; //rectangles for collision of the platform/top down setting
    private Rectangle saveRect; //rectangle for the save button

    //constructor
    public EditPanel(Edit e) {
        this.setLayout(null);
        mainFrame = e;
        addMouseListener(this);
        addKeyListener(this);
        avatarRect = new Rectangle(40,250,60,30);
        enemyRect = new Rectangle(100,250,60,30);
        blockRect = new Rectangle(160,250,60,30);
        itemRect = new Rectangle(220,250,60,30);
        systemRect = new Rectangle(280,250,60,30);
        spritesRect = new Rectangle(40,300,300,300);
        coolBack = new ImageIcon("scroll.png").getImage().getScaledInstance(1920,1080, Image.SCALE_SMOOTH);
        systemStrings = new String[]{"clock","score","health","frag"};
        systemSprites = new Image[4];
        for (int i = 0; i < 4; i++) {
            systemSprites[i] = new ImageIcon(String.format("System/%s.png",systemStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        itemStrings = new String[]{"point","bonuspoint","healthpack","timerbonus","greenkey","redkey"};
        itemSprites = new Image[6];
        for (int i = 0; i < 6; i++) {
            itemSprites[i] = new ImageIcon(String.format("Item/%s.png",itemStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        playerPlatStrings = new String[]{"hero","peng","pjump"};
        playerPlatSprites = new Image[3];
        for (int i = 0; i < 3; i++) {
            playerPlatSprites[i] = new ImageIcon(String.format("Platform/%sR0.png",playerPlatStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        enemyPlatStrings = new String[]{"ache","kara","poun"};
        enemyPlatSprites = new Image[3];
        for (int i = 0; i < 3; i++) {
            enemyPlatSprites[i] = new ImageIcon(String.format("Platform/%sR0.png",enemyPlatStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        playerTopDownStrings = new String[]{"blue","pfast","psnipe"};
        playerTopDownSprites = new Image[3];
        for (int i = 0; i < 3; i++) {
            playerTopDownSprites[i] = new ImageIcon(String.format("Top-Down/%sR0.png",playerTopDownStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        enemyTopDownStrings = new String[]{"red","reg","sniper","tank"};
        enemyTopDownSprites = new Image[4];
        for (int i = 0; i < 4; i++) {
            enemyTopDownSprites[i] = new ImageIcon(String.format("Top-Down/%sR0.png",enemyTopDownStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        blockStrings = new String[]{"cement","cloud","dirt","glass","gold","grass","goal","hiddenGoal",
                "message","greenLock","redLock","spike","teleportIn"};
        blockSprites = new Image[13];
        for (int i = 0; i < 13; i++) {
            blockSprites[i] = new ImageIcon(String.format("Block/%s.png",blockStrings[i])).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }

        curType = AVATAR;
        title = new ImageIcon("title.png").getImage();
        font = new Font("System San Francisco Display Regular.ttf",Font.BOLD,15);
        toolStrings = new String[]{"mouse","tool","delete"};
        toolImages = new Image[3]; toolClickedImages = new Image[3];
        toolEllipses = new Ellipse2D.Double[3];
        for (int i = 0; i < 3; i++) {
            toolImages[i] = new ImageIcon(String.format("Tools/%sIcon.png",toolStrings[i])).getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
            toolClickedImages[i] = new ImageIcon(String.format("Tools/%sClickedIcon.png",toolStrings[i])).getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
            toolEllipses[i] = new Ellipse2D.Double(1750,300+125*i,100,100);
        }
        curImage = null;
        curSprite = null;
        readyToPaste = false;
        gameRect = new Rectangle(500,150,1200,750);
        isAvatar = false;
        containsAvatar = false;
        changeAvatarPrompt = false;
        //dummy values for avatar location
        avatarX = -10;
        avatarY = -10;
        newavatarX = -10;
        newavatarY = -10;
        readyToDelete=false;
        readyToModify = false;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        promptBack = new ImageIcon("promptBlock.png").getImage().getScaledInstance(1720,880, Image.SCALE_SMOOTH);
        buttonImage = new ImageIcon("button.png").getImage().getScaledInstance(250,110, Image.SCALE_SMOOTH);
        buttonClickedImage = new ImageIcon("buttonClicked.png").getImage().getScaledInstance(250,110, Image.SCALE_SMOOTH);
        yesRect = new Rectangle(mainFrame.getWidth()/2-250,850,250,110);
        noRect = new Rectangle(mainFrame.getWidth()/2+50,850,250,110);
        avatar = null;
        curEnemy = null;
        curGoal = null;
        changeAvatarSettings = false;
        changeEnemySettings = false;
        changeGoalSettings = false;
        changeMessageSettings = false;
        changeKeyHoleSettings = false;
        changeSpikeSettings = false;
        curSetting = 0;
        oneToTenBlocks = new Rectangle[10];
        oneToFiveBlocks = new Rectangle[5];
        for (int i = 0; i < 10; i++) {
            oneToTenBlocks[i] = new Rectangle(150*i+250,500,50,50);
        }
        for (int i = 0; i < 5; i++) {
            oneToFiveBlocks[i] = new Rectangle(250+337*i,500,50,50);
        }
        titleArea = new JTextArea();
        titleArea.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,60));
        titleArea.setBackground(new Color(0,0,0,0));
        titleArea.setBounds(430,350,1000,200);
        titleArea.setWrapStyleWord(true);
        titleArea.setLineWrap(true);
        titleArea.setVisible(false);
        titleArea.setEditable(false);
        titleArea.addKeyListener(this);
        add(titleArea);

        curMessage = null;
        contentArea = new JTextArea();

        contentArea.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,25));
        contentArea.setBackground(new Color(0,0,0,0));
        contentArea.setBounds(250,300,1400,500);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setVisible(false);
        contentArea.setEditable(false);

        contentPane = new JScrollPane(contentArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.setBackground(contentArea.getBackground());
        contentPane.setVisible(false);
        contentPane.setBounds(contentArea.getBounds());
        contentPane.addKeyListener(this);
        add(contentPane);

        curKeyHole = null;
        curSpike = null;
        curCoin = null;
        curHealthBonus = null;
        curCountdown = null;
        countDownRect = null;
        changeCountdownSettings = false;
        curPointTotal = null;
        curHealth = null;
        changeHealthSettings = false;
        healthRect = null;

        left = new ImageIcon("left.png").getImage();
        right = new ImageIcon("right.png").getImage();
        up = new ImageIcon("up.png").getImage();
        down = new ImageIcon("down.png").getImage();
        offX = 0; offY = 0;
        upRect = new Rectangle(gameRect.x - 50,gameRect.y,40,40);
        downRect = new Rectangle(gameRect.x - 50,gameRect.y + gameRect.height - 40,40,40);
        leftRect = new Rectangle(gameRect.x,gameRect.y - 50,40,40);
        rightRect = new Rectangle(gameRect.x + gameRect.width - 40,gameRect.y - 50,40,40);

        changeLevelSettings = false;
        topDown = true;
        levelBackground = null;
        levelName = "Level 1";
        levelDescription = "";
        levelSettings = new String[]{"Name","Description","Background","Nature"};
        unsavedBackground = null;
        backgroundColor = new Color(0,0,0);
        unsavedBackgroundColor = null;
        backgroundStrings = new String[]{"Beach","City","Forest","Nebula Aqua-Pink",
                            "Nebula Blue","Nebula Red","Ocean","Underwater"};
        backgrounds = new Image[backgroundStrings.length];
        miniBackgrounds = new Image[backgroundStrings.length];
        miniBackgroundsRects = new Rectangle[backgroundStrings.length+1];
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i] = new ImageIcon(String.format("Backgrounds/%s.png",backgroundStrings[i])).getImage().getScaledInstance(gameRect.width,gameRect.height,Image.SCALE_SMOOTH);
            miniBackgroundsRects[i] = new Rectangle(350+250*(i%4),350+170*(i/4),250,160);
            miniBackgrounds[i] = new ImageIcon(String.format("Backgrounds/%s.png",backgroundStrings[i])).getImage().getScaledInstance(miniBackgroundsRects[i].width,miniBackgroundsRects[i].height,Image.SCALE_SMOOTH);
        }
        miniBackgroundsRects[backgroundStrings.length] = new Rectangle(350+250*(backgroundStrings.length%4),350+170*(backgroundStrings.length/4),1000,100);

        rgbAreas = new JTextArea[3];
        for (int i = 0; i < 3; i++) {
            rgbAreas[i] = new JTextArea();
            rgbAreas[i].setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,30));
            rgbAreas[i].setBackground(new Color(0,0,0,0));
            rgbAreas[i].setBounds(1500+30,350+100*i,80,40);
            rgbAreas[i].setWrapStyleWord(true);
            rgbAreas[i].setLineWrap(true);
            rgbAreas[i].setVisible(false);
            rgbAreas[i].setEditable(false);
            rgbAreas[i].addKeyListener(this);
            add(rgbAreas[i]);
        }
        previewColorRect = new Rectangle(1500,700,100,100);
        topDownRect = new Rectangle(350,500,400,200);
        platformRect = new Rectangle(1100,500,400,200);
        saveRect = new Rectangle(1750,0,1920-1750,100);
        teleportOutID = "teleportOut";
        teleportOut = new ImageIcon(String.format("Block/%s.png",teleportOutID)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);

        upEllipse = new Ellipse2D.Double(350,350,100,100);
        downEllipse = new Ellipse2D.Double(650,350,100,100);
        leftEllipse = new Ellipse2D.Double(350,650,100,100);
        rightEllipse = new Ellipse2D.Double(650,650,100,100);
        yesEllipse = new Ellipse2D.Double(upEllipse.x,upEllipse.y,upEllipse.width,upEllipse.height);
        noEllipse = new Ellipse2D.Double(downEllipse.x,downEllipse.y,downEllipse.width,downEllipse.height);
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    //update function that updates everything in the panel
    //very long method as many setttings can be changed
    public void update() throws IOException, ClassNotFoundException {
        mouse = getMousePosition(); //storing mouse position
        if (mouse == null) mouse = new Point(0,0);
        //inserting exit teleport is the biggest priority if applicable, as there needs to be an exit for every entry teleport
        if (insertTeleport) {
            //must satisfy all current conditions
            if (curTool == MOUSE && gameRect.contains(mouse) && readyToPaste && curType == BLOCK && curSprite == "teleportIn") {
                //storing sprite position
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                //creating new teleport, linking it to sprite class
                exitt = new Teleport(teleportOutID,sx,sy,new ImageIcon(teleportOut));
                exitt.init();
                //linking entry and exit teleports to each other in their respective objects
                entry.setExit(exitt);
                exitt.setEntry(entry);
                //immediately settings everything to null values in case they were taken for some reason for the next teleport
                entry = null; exitt = null; insertTeleport = false;
            }
            return; //ensures no other updates are being performed
        }
        //if there were multiple avatars present, the avatar settings need to be changed so that only one remains
        if (changeAvatarPrompt) {
            if (keys[KeyEvent.VK_Y] || yesRect.contains(mouse)) { //if the user wishes to use the new avatar
                changeAvatarPrompt = false;
                Sprite.delete(avatarX,avatarY); //deleting old avatar sprite
                //setting the avatar's location to the new x & y (the second sprite's values)
                avatarX = newavatarX;
                avatarY = newavatarY;
                avatar = new Avatar(curSprite,avatarX,avatarY,curImage); //creating new avatar object
                avatar.init(); //linking sprite object to avatar object
                //putting dummy values for new avatar location, as it doesn't exist
                newavatarY = -10;
                newavatarX = -10;

                curSprite = null;
                curImage = null;
                readyToPaste=true;
            }
            else if (keys[KeyEvent.VK_N] || noRect.contains(mouse)) {
                changeAvatarPrompt = false;
                //putting dummy values for new avatar location, as it doesn't exist
                newavatarX = -10;
                newavatarY = -10;
                curSprite = null;
                curImage = null;
                readyToPaste=true;
            }
            return; //ensures that no other settings are being checked on top of the avatar collision
        }
        if (changeAvatarSettings) { //if the user wanted to modify the avatar settings
            for (int i = 0; i < avatarSettingsRects.length; i++) {
                if (avatarSettingsRects[i].contains(mouse)) {
                    curSetting = i; //if the user clicked on the location of the heading, the current setting changes
                }
            }
            if (curSetting == SPEED) {
                //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedSpeed = i+1; //changing speed
                            break;
                        }
                    }
                }
            }
            else if (curSetting == HEALTH) {
                //checks for collisions with each of the ten options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedHealth = i+1; //changing health
                            break;

                        }
                    }
                }
            }
            else if (curSetting == BULLETSPEED) {
                //checks for collisions with each of the ten options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedBulletSpeed = i+1; //changing bullet speed
                            break;
                        }
                    }
                }
            }
            else if (curSetting == DAMAGESPRITE) {
                //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedDamage = i+1; //changing damage done by the bullet
                            break;
                        }
                    }
                }

            }
            if (yesRect.contains(mouse)) {//if the user wants to go forth with the changes
                //setting avatar stats to the values in each of the settings menus
                avatar.setHealth(unsavedHealth);
                avatar.setSpeed(unsavedSpeed);
                avatar.setBulletSpeed(unsavedBulletSpeed);
                avatar.setDamage(unsavedDamage);
                changeAvatarSettings = false;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeAvatarSettings = false;
            }
            return;
        }
        if (changeEnemySettings) {
            for (int i = 0; i < enemySettingsRects.length; i++) {
                if (enemySettingsRects[i].contains(mouse)) {
                    curSetting = i; //if the user clicked on the location of the heading, the current setting changes
                }
            }
            if (curSetting == SPEED && !unsavedStationary) {
                //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedSpeed = i+1; //changing speed
                            break;

                        }
                    }
                }
            }
            else if (curSetting == HEALTH) {
                //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedHealth = i+1; //changing health
                            break;

                        }
                    }
                }
            }
            else if (curSetting == BULLETSPEED) {
                //checks for collisions with each of the ten options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedBulletSpeed = i; //changing bullet speed
                            break;
                        }
                    }
                }
            }
            else if (curSetting == DAMAGESPRITE) {
                //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedDamage = i+1; //changing damage
                            break;
                        }
                    }
                }

            }
            else if (curSetting == STATIONARY) {
                if (yesEllipse.contains(mouse)) unsavedStationary = true;
                else if (noEllipse.contains(mouse)) unsavedStationary = false;

            }
            else if (curSetting == DIRECTION && find(enemyPlatStrings,curEnemy.getId()) == -1) {
                if (topDown) {
                    if (upEllipse.contains(mouse)) {
                        unsavedDirection = Enemy.UP;
                    }
                    else if (downEllipse.contains(mouse)) {
                        unsavedDirection = Enemy.DOWN;
                    }
                }
                if (leftEllipse.contains(mouse)) {
                    unsavedDirection = Enemy.LEFT;
                }
                else if (rightEllipse.contains(mouse)) {
                    unsavedDirection = Enemy.RIGHT;
                }
            }

            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                curEnemy.setHealth(unsavedHealth);
                curEnemy.setSpeed(unsavedSpeed);
                curEnemy.setBulletSpeed(unsavedBulletSpeed);
                curEnemy.setStationary(unsavedStationary);
                curEnemy.setDirection(unsavedDirection);
                curEnemy.setDamage(unsavedDamage);
                String dir,spriteType; //strings to get image from file directory for current image displayed in the editor
                if (topDown) spriteType = "Top-Down";
                else spriteType = "Platform";
                if (unsavedDirection == Enemy.RIGHT) dir = "R";
                else if (unsavedDirection == Enemy.UP) dir = "U";
                else if (unsavedDirection == Enemy.LEFT) dir = "L";
                else dir = "D";
                //loading image
                curEnemy.getSprite().setImg(new ImageIcon(new ImageIcon(String.format("%s/%s%s0.png",spriteType,curEnemy.getId(),dir)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));


                changeEnemySettings = false;
                curEnemy = null;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeEnemySettings = false;
                curEnemy = null;
            }
            return;
        }
        if (changeGoalSettings) {
            for (int i = 0; i < goalSettingsRects.length; i++) {
                if (goalSettingsRects[i].contains(mouse)) {
                    curSetting = i; //if the user clicked on the location of the heading, the current setting changes
                }
            }

            if (curSetting == UNLOCK) {
                //checks for collisions with each of the ten options in the scroll bar graphic (within a certain range)
                for (int i = 0; i < unsavedArrPoints.length; i++) {
                    if (Math.abs(mouse.y - (oneToFiveBlocks[i].y + oneToFiveBlocks[i].height / 2)) < 40) {
                        if (Math.abs(mouse.x - (oneToFiveBlocks[i].x + oneToFiveBlocks[i].width / 2)) < 50) {
                            unsavedArrPoints[i]++; //changing the digit value of the unlock requirement
                            unsavedArrPoints[i] %= 10;
                            break;
                        }
                    }
                }
            }
            if (curSetting == MASK) {
                for (int i = 0; i < goalMasks.length; i++) {
                    if (new Rectangle(dialogCtr-200+100*(i%4),300+100*(i/4),75,75).contains(mouse)) { //if the mask image is clicked on
                        if (i == 0) { //the "none" option was selected
                            unsavedMask = new ImageIcon(new ImageIcon("Block/blank.png").getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH));
                            unsavedMaskID = "blank";
                        }
                        else {
                            unsavedMask = new ImageIcon(blockSprites[i-1]);
                            unsavedMaskID = blockStrings[i-1];
                        }
                    }
                }
            }
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                curGoal.setMaskID(unsavedMaskID);
                curGoal.setMask(unsavedMask);
                //converting the digit array into a number
                curGoal.setPointsToOpen(unsavedArrPoints[0]*10000+unsavedArrPoints[1]*1000+unsavedArrPoints[2]*100+unsavedArrPoints[3]*10+unsavedArrPoints[4]);
                changeGoalSettings = false;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeGoalSettings = false;
            }
            return;
        }
        if (changeMessageSettings) {
            for (int i = 0; i < messageSettingsRects.length; i++) {
                if (messageSettingsRects[i].contains(mouse)) {
                    curSetting = i; //if the user clicked on the location of the heading, the current setting changes
                    //text areas need to be set visible or invisible depending on the new setting
                    if (i == 0) {
                        contentArea.setVisible(false); contentArea.setEditable(false); contentPane.setVisible(false);
                        titleArea.setVisible(true); titleArea.setEditable(true);
                    }
                    else if (i == 1) {
                        titleArea.setVisible(false); titleArea.setEditable(false);
                        contentArea.setVisible(true); contentArea.setEditable(true); contentPane.setVisible(true);
                    }
                }
            }
            if (curSetting == TITLE) {
                //removing all enters from the text area
                titleArea.setText(titleArea.getText().replaceAll("\n",""));
                //setting a character limit of 45
                if (titleArea.getText().length() > 45) titleArea.setText(titleArea.getText().substring(0,45));
            }

            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                changeMessageSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
                contentArea.setVisible(false); contentArea.setEditable(false); contentPane.setVisible(false);
                curMessage.setTitle(titleArea.getText());
                curMessage.setContent(contentArea.getText());
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeMessageSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
                contentArea.setVisible(false); contentArea.setEditable(false); contentPane.setVisible(false);
            }
            return;
        }
        if (changeKeyHoleSettings) {
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                try { //will only perform the saving if the title area text can be parsed to an integer
                    curKeyHole.setUnlockRequirement(Integer.parseInt(titleArea.getText()));
                    changeKeyHoleSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeKeyHoleSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeSpikeSettings) {
            //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedDamage = i+1; //changing the damage
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                curSpike.setDmg(unsavedDamage);
                changeSpikeSettings = false;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeSpikeSettings = false;
            }
            return;
        }
        if (changeCoinSettings) {
            //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedPts = i+1; //setting the point value
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                curPointTotal.decrease(curCoin.getPts()); //removing the old point value from the point total
                curCoin.setPts(unsavedPts);
                curPointTotal.increase(curCoin.getPts()); //adding the new point value to the point total
                changeCoinSettings = false;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeCoinSettings = false;
            }
            return;
        }
        if (changeHealthBonusSettings) {
            //checks for collisions with each of the five options in the scroll bar graphic (within a certain range)
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedValue = i+1;
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                curHealthBonus.setValue(unsavedValue);
                changeHealthBonusSettings = false;
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeHealthBonusSettings = false;
            }
            return;
        }
        if (changeTimeBonusSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n",""));
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                try { //only goes forth and saves everything if the title area text can be parsed into an integer
                    curTimeBonus.setValue(Integer.parseInt(titleArea.getText()));
                    changeTimeBonusSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeTimeBonusSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
        }
        if (changeKeyInsertSettings) {
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                try { //only goes forth and saves everything if the title area text can be parsed into an integer
                    curKeyInsert.setValue(Integer.parseInt(titleArea.getText()));
                    changeKeyInsertSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeKeyInsertSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeCountdownSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n","")); //removing all enter characters
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                if (titleArea.getText().charAt(2) == ':' && titleArea.getText().charAt(5) == ':' && titleArea.getText().length() == 8) { //if the input is entered in the specified manner
                    try { //only goes forth and saves everything if the hours, minutes, and seconds can be parsed into an integer
                        int tl = Integer.parseInt(titleArea.getText().substring(0,2))*3600+Integer.parseInt(titleArea.getText().substring(3,5))*60+Integer.parseInt(titleArea.getText().substring(6));
                        curCountdown.setTimeLeft(titleArea.getText());
                        changeCountdownSettings = false;
                        titleArea.setVisible(false); titleArea.setEditable(false);
                    }
                    catch (NumberFormatException e) {}
                }
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeCountdownSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeHealthSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n",""));
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                try { //only goes forth and saves everything if the title area text can be parsed into an integer
                    curHealth.setValue(Integer.parseInt(titleArea.getText()));
                    changeHealthSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                changeHealthSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (settingsRect.contains(mouse)) {
            //loading level settings into temporary variables
            changeLevelSettings = true;
            unsavedBackground = this.levelBackground;
            titleArea.setVisible(true); titleArea.setEditable(true);
            titleArea.setText(levelName);
            contentArea.setText(levelDescription);
            curSetting = NAME;
            if (backgroundColor != null) {
                rgbAreas[0].setText(Integer.toString(backgroundColor.getRed()));
                rgbAreas[1].setText(Integer.toString(backgroundColor.getBlue()));
                rgbAreas[2].setText(Integer.toString(backgroundColor.getGreen()));
            }


        }
        if (changeLevelSettings) {
            for (int i = 0; i < levelSettingsRects.length; i++) {
                if (levelSettingsRects[i].contains(mouse)) {
                    curSetting = i; //if the user clicked on the location of the heading, the current setting changes
                    //setting text areas and panes to be visible or invisible depending on the current setting
                    if (curSetting == NAME) {
                        titleArea.setVisible(true); titleArea.setEditable(true);
                        contentPane.setVisible(false); contentArea.setEditable(false); contentArea.setVisible(false);
                        for (JTextArea area : rgbAreas) {
                            area.setVisible(false); area.setEditable(false);
                        }
                    }
                    else if (curSetting == DESCRIPTION) {
                        contentPane.setVisible(true); contentArea.setEditable(true); contentArea.setVisible(true);
                        titleArea.setVisible(false); titleArea.setEditable(false);
                        for (JTextArea area : rgbAreas) {
                            area.setVisible(false); area.setEditable(false);
                        }
                    }
                    else if (curSetting == BACKGROUND) {
                        titleArea.setVisible(false); titleArea.setEditable(false);
                        contentPane.setVisible(false); contentArea.setEditable(false); contentArea.setVisible(false);
                        if (unsavedBackground == null) {
                            for (JTextArea area : rgbAreas) {
                                area.setVisible(true); area.setEditable(true);
                            }
                        }
                        else {
                            for (JTextArea area : rgbAreas) {
                                area.setVisible(false); area.setEditable(false);
                            }
                        }
                    }
                    else if (curSetting == NATURE) {
                        titleArea.setVisible(false); titleArea.setEditable(false);
                        contentPane.setVisible(false); contentArea.setEditable(false); contentArea.setVisible(false);
                        for (JTextArea area : rgbAreas) {
                            area.setVisible(false); area.setEditable(false);
                        }

                    }
                }
            }
            if (curSetting == BACKGROUND) {
                //goes through each mini background rectangle, changes the background and makes the RGB text areas invisible if it was clicked on
                for (int i = 0; i < backgrounds.length; i++) {
                    if (miniBackgroundsRects[i].contains(mouse)) {
                        unsavedBackground = new ImageIcon(backgrounds[i]); 
                        for (JTextArea area : rgbAreas) {
                            area.setVisible(false);
                            area.setEditable(false);
                        }
                    }
                }
                if (miniBackgroundsRects[backgrounds.length].contains(mouse)) { //if the custom color option was clicked on
                    unsavedBackground = null;
                    for (JTextArea area : rgbAreas) {
                        area.setVisible(true);
                        area.setEditable(true);
                    }
                }
            }
            if (curSetting == NATURE) {
                if (topDownRect.contains(mouse)) {
                    topDown = true;
                    Sprite.clear(); //removes all sprites that are incompatible (avatar and enemy)
                }
                else if (platformRect.contains(mouse)) {
                    topDown = false;
                    Sprite.clear(); //removes all sprites that are incompatbiel (avatar and enemy)
                }
            }
            if (yesRect.contains(mouse)) { //if the user wants to go forth with the changes
                if (unsavedBackground != null) { //if the background is chosen to be an iamge
                    levelBackground = unsavedBackground;
                    backgroundColor = null;
                    contentArea.setEditable(false); contentArea.setVisible(false); contentPane.setVisible(false);
                    titleArea.setEditable(false); titleArea.setVisible(false);
                    levelDescription = contentArea.getText();
                    levelName = titleArea.getText();
                    changeLevelSettings = false;
                    for (JTextArea area : rgbAreas) {
                        area.setVisible(false); area.setEditable(false);
                    }
                }
                else { //if the background is chosen to be a color
                    try {
                        int redValue = Integer.parseInt(rgbAreas[0].getText()),greenValue = Integer.parseInt(rgbAreas[1].getText()),blueValue = Integer.parseInt(rgbAreas[2].getText());
                        if (redValue >= 0 && redValue <= 255 && greenValue >= 0 && greenValue <= 255 && blueValue >= 0 && blueValue <= 255) {
                            levelBackground = null;
                            backgroundColor = new Color(redValue,greenValue,blueValue);
                            contentArea.setEditable(false); contentArea.setVisible(false); contentPane.setVisible(false);
                            titleArea.setEditable(false); titleArea.setVisible(false);
                            levelDescription = contentArea.getText();
                            levelName = titleArea.getText();
                            changeLevelSettings = false;
                            for (JTextArea area : rgbAreas) {
                                area.setVisible(false); area.setEditable(false);
                            }
                        }
                    }
                    catch (NumberFormatException e) {}
                }
            }
            else if (noRect.contains(mouse)) { //if the user wants to revert the changes to the old ones
                contentArea.setEditable(false); contentArea.setVisible(false); contentPane.setVisible(false);
                titleArea.setEditable(false); titleArea.setVisible(false);
                changeLevelSettings = false;
            }
            return;
        }
        if (saveRect.contains(mouse) && numGoals > 0 && avatar != null) {

            OutputStream file = new FileOutputStream(String.format("%s.txt",levelName),false); //loading file, setting it to overwrite
            ObjectOutputStream outStream = new ObjectOutputStream(file); //creating an object output stream for serialization
            //writing objects to file
            outStream.writeObject(topDown);
            //writes either background imageicon or background color (only one exists)
            if (backgroundColor != null) {
                outStream.writeObject(backgroundColor);
            }
            else {
                outStream.writeObject(levelBackground);
            }
            outStream.writeObject(levelName);
            outStream.writeObject(levelDescription);
            if (curCountdown != null) {
                outStream.writeObject(curCountdown);
            }
            if (curHealth != null) {
                outStream.writeObject(curHealth);
            }
            if (curPointTotal != null) {
                outStream.writeObject(curPointTotal);
            }
            Iterator<Map.Entry<Integer, Sprite>> it = Sprite.getSpriteHashMap().entrySet().iterator(); //creating iterator to go through each sprite in the hash map
            while (it.hasNext()) {
                Map.Entry<Integer, Sprite> pair = it.next(); //getting the next entry
                Sprite sprite = pair.getValue();
                try {
                    outStream.writeObject(sprite); //write sprite to file
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            file.close();
            outStream.close();
        }
        //changing the current menu type to display that type of sprite
        if (avatarRect.contains(mouse)) curType = AVATAR;
        else if (enemyRect.contains(mouse)) curType = ENEMY;
        else if (blockRect.contains(mouse)) curType = BLOCK;
        else if (itemRect.contains(mouse)) curType = ITEM;
        else if (systemRect.contains(mouse)) curType = SYSTEM;


        //changing the tools
        for (int i = 0; i < 3; i++) {
            if (toolEllipses[i].contains(mouse)) curTool = i;
            readyToPaste = false;
            readyToDelete = false;
            readyToModify = false;
            if (curTool != MOUSE) {
                curSprite = null;
                curImage = null;
                isAvatar = false;
            }
            if (curTool == DELETE) {
                readyToDelete = true;
            } else if (curTool == MOUSE) {
                readyToPaste = true;
            } else {
                readyToModify = true;
            }
        }
        //if the current setting is to select/insert
        if (curTool == MOUSE) {
            if (spritesRect.contains(mouse)) { //if the user wants a different sprite to be inserted
                int ind = (mouse.x - 40) / 75 + 4 * (mouse.y - 300) / 75 - 1; //getting index for selection of sprite from array
                if (ind >= 0) {
                    if (curType == AVATAR) { //if the avatar options are being displayed
                        //making sure the index determined by the mouse location is valid. and stores the sprite's id and imageicon if so
                        if (topDown && playerTopDownSprites.length > ind) {
                            curSprite = playerTopDownStrings[ind];
                            curImage = new ImageIcon(playerTopDownSprites[ind]);
                            isAvatar = true;
                        } else if (!topDown && playerPlatSprites.length > ind) {
                            curSprite = playerPlatStrings[ind];
                            curImage = new ImageIcon(playerPlatSprites[ind]);
                            isAvatar = true;
                        }
                    } else if (curType == ENEMY) { //if the enemy options are being displayed
                        //making sure the index determined by the mouse location is valid. and stores the sprite's id and imageicon if so
                        if (topDown && enemyTopDownSprites.length > ind) {
                            curSprite = enemyTopDownStrings[ind];
                            curImage = new ImageIcon(enemyTopDownSprites[ind]);
                            isAvatar = false;
                        } else if (!topDown && playerPlatSprites.length > ind) {
                            curSprite = enemyPlatStrings[ind];
                            curImage = new ImageIcon(enemyPlatSprites[ind]);
                            isAvatar = false;
                        }

                    } else if (curType == BLOCK) { //if the block options are being displayed
                        //making sure the index determined by the mouse location is valid. and stores the sprite's id and imageicon if so;
                        if (ind < blockSprites.length) {
                            curSprite = blockStrings[ind];
                            curImage = new ImageIcon(blockSprites[ind]);
                            isAvatar = false;
                        }
                    } else if (curType == ITEM) { //if the item options are being displayed
                        //making sure the index determined by the mouse location is valid. and stores the sprite's id and imageicon if so
                        if (itemSprites.length > ind) {
                            curSprite = itemStrings[ind];
                            curImage = new ImageIcon(itemSprites[ind]);
                            isAvatar = false;
                        }
                    } else if (curType == SYSTEM) { //if the system assets options are being displayed
                        if (systemSprites.length > ind) {
                            if (ind == 0) {
                                curCountdown = new Countdown();
                            } else if (ind == 1) {
                                curPointTotal = new PointTotal();
                            } else if (ind == 2) {
                                curHealth = new Health();
                            }
                        }
                    }
                }
            }
            if ((spritesRect.contains(mouse) || gameRect.contains(mouse)) && curSprite != null) readyToPaste = true; //ready to add sprite to game
            else readyToPaste = false;

            if (readyToPaste && gameRect.contains(mouse)) { //the user clicked to add the sprite on
                //getting the x and y position
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                if (!isAvatar || !containsAvatar) {
                    if (isAvatar) {
                        containsAvatar = true; //only avatar on the page
                        //creating avatar at sx and sy
                        avatarX = sx;
                        avatarY = sy;
                        avatar = new Avatar(curSprite,sx,sy,curImage);
                        avatar.init();
                    }
                    else if (curType == ENEMY) { //creating enemy
                        new Enemy(curSprite,sx,sy,curImage,3,5).init();
                    }
                    else if (curType == BLOCK) {
                        int idx = find(blockStrings,curSprite); //getting index of block string in the block array
                        if (idx < 6) new Block(curSprite, sx, sy, curImage).init(); //the normal block is for aesthetics and boundaries, no functional purpose
                        else if (idx < 7) { //the goal block (no mask)
                            new Goal(curSprite,sx,sy,curImage,"blank").init();
                            numGoals++;
                        }
                        else if (idx < 8) { //the goal block (with default cement mask)
                            new Goal(curSprite,sx,sy,curImage,"cement").init();
                            numGoals++;
                        }
                        else if (idx < 9) new Message(curSprite,sx,sy,curImage).init(); //empty message
                        else if (idx < 11) new KeyHole(curSprite,sx,sy,curImage,idx-9).init(); //green = 0, red = 1 for color
                        else if (idx < 12) new Spike(curSprite,sx,sy,curImage).init(); //spike block
                        else {
                            if (!insertTeleport) { //the entry teleport has not been inserted yet
                                entry = new Teleport(curSprite, sx, sy, curImage); //created entry
                                entry.init(); //linking to the Sprite object at sx,sy
                                insertTeleport = true; //the exit teleport must be inserted
                            }
                        }
                    }
                    else if (curType == ITEM) {
                        int idx = find(itemStrings,curSprite); //getting index of selection in itemStrings array
                        if (idx == 0) {
                            new Coin(curSprite,sx,sy,curImage,false).init(); //creating an unmodifiable coin
                            if (curPointTotal == null) curPointTotal = new PointTotal(1);
                            else curPointTotal.increase(1);
                        }
                        else if (idx == 1) {
                            new Coin(curSprite,sx,sy,curImage,true).init(); //creating a modifiable coin
                            if (curPointTotal == null) curPointTotal = new PointTotal(3);
                            else curPointTotal.increase(3);
                        }
                        else if (idx == 2) new HealthBonus(curSprite,sx,sy,curImage).init();
                        else if (idx == 3) new TimeBonus(curSprite,sx,sy,curImage).init();
                        else if (idx < 6) new KeyInsert(curSprite,sx,sy,curImage,idx-4).init();
                    }
                } else  {
                    changeAvatarPrompt = true; //more than one avatar has been inserted
                    newavatarX = sx;
                    newavatarY = sy;
                }
            }
        }
        else if (curTool == DELETE) {
            if (gameRect.contains(mouse)) {
                //getting position of prite
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                if (avatarX == sx && avatarY == sy) { //if the position is the same as the avatar's position
                    //removing avatar data, as the game no longer contains an avatar
                    avatar = null;
                    avatarX = -10;
                    avatarY = -10;
                    containsAvatar = false;
                }
                if (Sprite.getSpriteHashMap().containsKey(sx*200000+sy)) { //if a sprite exists at the location
                    if (Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance instanceof Coin) {
                        curPointTotal.decrease(((Coin) Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance).getPts()); //removing from total point total
                    } else if (Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance instanceof Teleport) {
                        Sprite.delete(((Teleport) Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance).getPartnerX(),
                                ((Teleport) Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance).getPartnerY()); //removes the partner teleport with the current one
                    } else if (Sprite.getSpriteHashMap().get(sx * 200000 + sy).instance instanceof Goal) {
                        numGoals--; //decreasing the number of goals in the game
                    }
                    Sprite.delete(sx, sy); //removing sprite from hash map
                }
            }
            if (countDownRect.contains(mouse)) {
                curCountdown = null; //removing the countdown
            }
        }
        else if (curTool == TOOL) {
            if (gameRect.contains(mouse)) { //sprite is to be modified
                //getting location of modification
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                int spriteKey = sx*200000+sy;
                if (Sprite.getSpriteHashMap().containsKey(spriteKey)) { //if there is a sprite at the given location
                    Object inst = Sprite.getSpriteHashMap().get(spriteKey).instance; //stores the instance, the data for the exact type of sprite
                    if (inst instanceof Enemy) { //check if the sprite was for an enemy
                        changeEnemySettings = true;
                        curSetting = 0;
                        curEnemy = (Enemy)inst; //storing the current enemy
                        //putting all data in temporary variables
                        unsavedHealth = curEnemy.getHealth();
                        unsavedSpeed = curEnemy.getSpeed();
                        unsavedStationary = curEnemy.isStationary();
                        unsavedDirection = curEnemy.getDirection();
                        unsavedBulletSpeed = curEnemy.getBulletSpeed();
                        unsavedDamage = curEnemy.getDamage();
                    }
                    else if (inst instanceof Avatar) { //check if the sprite was for an avatar
                        changeAvatarSettings = true;
                        curSetting = 0;
                        //putting all data in temporary variables
                        unsavedHealth = avatar.getHealth();
                        unsavedSpeed = avatar.getSpeed();
                        unsavedBulletSpeed = avatar.getBulletSpeed();
                        unsavedDamage = avatar.getDamage();
                    }
                    else if (inst instanceof Goal) { //check if the sprite was for a goal
                        curSetting = MASK;
                        changeGoalSettings = true;
                        curGoal = (Goal) inst; //storing the current goal
                        //putting all data in temporary variables
                        unsavedMask = curGoal.getMask();
                        unsavedMaskID = curGoal.getMaskID();
                        int unsavedPointstoOpen = curGoal.getPointsToOpen();
                        unsavedArrPoints = new int[]{unsavedPointstoOpen/10000,(unsavedPointstoOpen/1000)%10,(unsavedPointstoOpen/100)%10,(unsavedPointstoOpen/10)%10,(unsavedPointstoOpen)%10};
                    }
                    else if (inst instanceof Message) { //check if the sprite was for a goal
                        curSetting = TITLE;
                        curMessage = (Message) inst; //storing the current message
                        changeMessageSettings = true;
                        //putting all data in temporary variables
                        titleArea.setEditable(true);
                        titleArea.setVisible(true);
                        titleArea.setText(curMessage.getTitle());
                        contentArea.setText(curMessage.getContent());
                    }
                    else if (inst instanceof KeyHole) { //check if the sprite was for a key hole
                        curSetting = UNLOCKREQUIREMENT;
                        curKeyHole = (KeyHole) inst; //storing the current key hole
                        changeKeyHoleSettings = true;
                        //putting all data in temporary variables
                        titleArea.setEditable(true);
                        titleArea.setVisible(true);
                        titleArea.setText(Integer.toString(curKeyHole.getUnlockRequirement()));
                    }
                    else if (inst instanceof Spike) { //check if the sprite was for a spike
                        curSetting = DAMAGE;
                        changeSpikeSettings = true;
                        curSpike = (Spike) inst; //storing the current spike
                        //putting all data in temporary variables
                        unsavedDamage = curSpike.getDmg();
                    }
                    else if (inst instanceof Coin) { //check if the sprite was for a coin
                        curCoin = (Coin) inst; //storing the current coin
                        if (curCoin.isEditable()) {
                            //putting all data in temporary variables
                            unsavedPts = curCoin.getPts();
                            curSetting = POINTS;
                            changeCoinSettings = true;
                        }
                        else {
                            curCoin = null;
                        }
                    }
                    else if (inst instanceof HealthBonus) { //check if the sprite was for a health bonus
                        curSetting = POINTS;
                        changeHealthBonusSettings = true;
                        curHealthBonus = (HealthBonus) inst; //storing the current health bonus
                        //putting all data in temporary variables
                        unsavedValue = curHealthBonus.getValue();

                    }
                    else if (inst instanceof TimeBonus) { //check if the sprite was for a time bonus
                        curTimeBonus = (TimeBonus) inst;//storing the current time bonus
                        curSetting = POINTS;
                        //putting all data in temporary variables
                        unsavedValue = curTimeBonus.getValue();
                        changeTimeBonusSettings = true;
                        titleArea.setVisible(true);
                        titleArea.setEditable(true);
                        titleArea.setText(Integer.toString(unsavedValue));
                    }
                    else if (inst instanceof KeyInsert) { //check if the sprite was for a key
                        curKeyInsert = (KeyInsert) inst; //storing the current key
                        curSetting = POINTS;
                        //putting all data in temporary variables
                        unsavedValue = curKeyInsert.getValue();
                        changeKeyInsertSettings = true;
                        titleArea.setVisible(true);
                        titleArea.setEditable(true);
                        titleArea.setText(Integer.toString(unsavedValue));
                    }
                }
            }
            else if (countDownRect.contains(mouse) && curCountdown != null) { //checking if the countdown was selected
                changeCountdownSettings = true;
                //storing data in temporary variables
                titleArea.setVisible(true);
                titleArea.setEditable(true);
                titleArea.setText(curCountdown.getStrTime());
                curSetting = 0;
            }
            else if (healthRect.contains(mouse) && curHealth != null) { //checking if the health object was selected
                changeHealthSettings = true;
                //storing all data in temporary variables
                titleArea.setVisible(true);
                titleArea.setEditable(true);
                titleArea.setText(Integer.toString(curHealth.getValue()));
                curSetting = 0;
            }
        }
        //shifting the offset
        if (upRect.contains(mouse)) {
            offY = Math.max(offY-1,0);
        }
        else if (downRect.contains(mouse)) {
            offY = Math.min(offY+1,100);
        }
        else if (leftRect.contains(mouse)) {
            offX = Math.max(offX-1,0);
        }
        else if (rightRect.contains(mouse)) {
            offX = Math.min(offX+1,100);
        }
    }
    //method that draws the prompt for whether the new or old avatar is to be used
    public void paintChangeAvatar(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("WARNING",getTitlePosition("WARNING",g),175);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,45));
        g.setColor(BROWN);
        g.drawString("You have more than one avatar on the board, which is not allowed.",getTitlePosition("You have more than one avatar on the board, which is not allowed.",g),250);
        g.drawString("Press Y to use new avatar data, N to use old avatar data.",getTitlePosition("Press Y to use new avatar data, N to use old avatar data.",g),300);
    }
    //method that draws the settings for the avatar
    private void paintAvatarSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(avatarSettings[curSetting],getTitlePosition(avatarSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < avatarSettingsRects.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            if (curSetting == i || avatarSettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(avatarSettingsRects[i].x,avatarSettingsRects[i].y,avatarSettingsRects[i].width,avatarSettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(avatarSettings[i],avatarSettingsRects[i].x+10,avatarSettingsRects[i].y+30);
        }
        if (curSetting == SPEED) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for speed
            for (int i = 0; i < 5; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedSpeed-1].x,oneToFiveBlocks[unsavedSpeed-1].y,oneToFiveBlocks[unsavedSpeed-1].width,oneToFiveBlocks[unsavedSpeed-1].height);
        }
        else if (curSetting == HEALTH) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for health
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedHealth-1].x,oneToFiveBlocks[unsavedHealth-1].y,oneToFiveBlocks[unsavedHealth-1].width,oneToFiveBlocks[unsavedHealth-1].height);
        }
        else if (curSetting == BULLETSPEED) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for bullet speed
            for (int i = 0; i < 10; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToTenBlocks[i],String.format("%d",i+
                        1),g),oneToTenBlocks[i].y+oneToTenBlocks[i].width*3/2);
            }
            g.fillRect(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2-5,oneToTenBlocks[9].x+oneToTenBlocks[9].width-oneToTenBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToTenBlocks[unsavedBulletSpeed-1].x,oneToTenBlocks[unsavedBulletSpeed-1].y,oneToTenBlocks[unsavedBulletSpeed-1].width,oneToTenBlocks[unsavedBulletSpeed-1].height);
        }
        else if (curSetting == DAMAGESPRITE) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for damage
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedDamage-1].x,oneToFiveBlocks[unsavedDamage-1].y,oneToFiveBlocks[unsavedDamage-1].width,oneToFiveBlocks[unsavedDamage-1].height);
        }
    }
    //method that draws the settings for the enemy
    public void paintEnemySettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(enemySettings[curSetting],getTitlePosition(enemySettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        int range = enemySettingsRects.length;
        for (int i = 0; i < enemySettingsRects.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            if (curSetting == i || enemySettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(enemySettingsRects[i].x,enemySettingsRects[i].y,enemySettingsRects[i].width,enemySettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(enemySettings[i],enemySettingsRects[i].x+10,enemySettingsRects[i].y+30);
        }
        if (curSetting == SPEED) {
            g.setColor(Color.black);
            if (!unsavedStationary) {
                //drawing the scrollbar graphic for speed
                for (int i = 0; i < 5; i++) {
                    g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
                }
                g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
                g.setColor(Color.YELLOW);
                g.fillOval(oneToFiveBlocks[unsavedSpeed-1].x,oneToFiveBlocks[unsavedSpeed-1].y,oneToFiveBlocks[unsavedSpeed-1].width,oneToFiveBlocks[unsavedSpeed-1].height);
                //g.drawLine(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2,oneToTenBlocks[9].x+oneToTenBlocks[9].width,oneToTenBlocks[9].y+oneToTenBlocks[9].width/2);

            }
            else {
                g.drawString("Enemy is on stationary", getTitlePosition("Enemy is on stationary",g), 300);
            }
        }
        else if (curSetting == HEALTH) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for health
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedHealth-1].x,oneToFiveBlocks[unsavedHealth-1].y,oneToFiveBlocks[unsavedHealth-1].width,oneToFiveBlocks[unsavedHealth-1].height);
        }
        else if (curSetting == BULLETSPEED) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for bullet speed
            for (int i = 0; i < 10; i++) {
                g.drawString(String.format("%d",i),ctrPosition(oneToTenBlocks[i],String.format("%d",i),g),oneToTenBlocks[i].y+oneToTenBlocks[i].width*3/2);
            }
            g.fillRect(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2-5,oneToTenBlocks[9].x+oneToTenBlocks[9].width-oneToTenBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToTenBlocks[unsavedBulletSpeed].x,oneToTenBlocks[unsavedBulletSpeed].y,oneToTenBlocks[unsavedBulletSpeed].width,oneToTenBlocks[unsavedBulletSpeed].height);
        }
        else if (curSetting == DAMAGESPRITE) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for damage
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedDamage-1].x,oneToFiveBlocks[unsavedDamage-1].y,oneToFiveBlocks[unsavedDamage-1].width,oneToFiveBlocks[unsavedDamage-1].height);
        }
        else if (curSetting == STATIONARY) {
            //drawing the yes & no options for whether the enemy is stationary
            g.setColor(Color.YELLOW);
            if (unsavedStationary) {
                g.fillOval((int)noEllipse.x,(int)noEllipse.y,(int)noEllipse.width,(int)noEllipse.height);
                g.setColor(Color.RED);
                g.fillOval((int)yesEllipse.x,(int)yesEllipse.y,(int)yesEllipse.width,(int)yesEllipse.height);
            }
            else {
                g.fillOval((int)yesEllipse.x,(int)yesEllipse.y,(int)yesEllipse.width,(int)yesEllipse.height);
                g.setColor(Color.RED);
                g.fillOval((int)noEllipse.x,(int)noEllipse.y,(int)noEllipse.width,(int)noEllipse.height);
            }
            g.setColor(Color.BLACK);
            g.drawString("YES",ctrPosition(new Rectangle((int)yesEllipse.x,(int)yesEllipse.y,(int)yesEllipse.width,(int)yesEllipse.height),"YES",g),(int)yesEllipse.y+65);
            g.drawString("NO",ctrPosition(new Rectangle((int)noEllipse.x,(int)noEllipse.y,(int)noEllipse.width,(int)noEllipse.height),"NO",g),(int)noEllipse.y+65);
        }
        else if (curSetting == DIRECTION) {
            //drawing the current direction settings
            g.setColor(Color.YELLOW);
            if (topDown) { //platform version of the game does not have up/down in the game
                g.fillOval((int) upEllipse.x, (int) upEllipse.y, (int) upEllipse.width, (int) upEllipse.height);
                g.fillOval((int) downEllipse.x, (int) downEllipse.y, (int) downEllipse.width, (int) downEllipse.height);
            }
            g.fillOval((int)leftEllipse.x,(int)leftEllipse.y,(int)leftEllipse.width,(int)leftEllipse.height);
            g.fillOval((int)rightEllipse.x,(int)rightEllipse.y,(int)rightEllipse.width,(int)rightEllipse.height);
            g.setColor(Color.RED);
            if (unsavedDirection == Enemy.RIGHT) {
                g.fillOval((int)rightEllipse.x,(int)rightEllipse.y,(int)rightEllipse.width,(int)rightEllipse.height);
            }
            else if (unsavedDirection == Enemy.UP && topDown) {
                g.fillOval((int)upEllipse.x,(int)upEllipse.y,(int)upEllipse.width,(int)upEllipse.height);
            }
            else if (unsavedDirection == Enemy.LEFT) {
                g.fillOval((int)leftEllipse.x,(int)leftEllipse.y,(int)leftEllipse.width,(int)leftEllipse.height);
            }
            else if (unsavedDirection == Enemy.DOWN && topDown) {
                g.fillOval((int)downEllipse.x,(int)downEllipse.y,(int)downEllipse.width,(int)downEllipse.height);
            }
            g.setColor(Color.BLACK);
            g.drawString("RIGHT",ctrPosition(new Rectangle((int)rightEllipse.x,(int)rightEllipse.y,(int)rightEllipse.width,(int)rightEllipse.height),"RIGHT",g),(int)rightEllipse.y+65);
            if (topDown) g.drawString("UP",ctrPosition(new Rectangle((int)upEllipse.x,(int)upEllipse.y,(int)upEllipse.width,(int)upEllipse.height),"UP",g),(int)upEllipse.y+65);
            g.drawString("LEFT",ctrPosition(new Rectangle((int)leftEllipse.x,(int)leftEllipse.y,(int)leftEllipse.width,(int)leftEllipse.height),"LEFT",g),(int)leftEllipse.y+65);
            if (topDown) g.drawString("DOWN",ctrPosition(new Rectangle((int)downEllipse.x,(int)downEllipse.y,(int)downEllipse.width,(int)downEllipse.height),"DOWN",g),(int)downEllipse.y+65);

        }
    }
    //method that draws the settings for the goal
    public void paintGoalSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);

        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(goalSettings[curSetting],getTitlePosition(goalSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < goalSettingsRects.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            if (curSetting == i || goalSettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(goalSettingsRects[i].x,goalSettingsRects[i].y,goalSettingsRects[i].width,goalSettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(goalSettings[i],goalSettingsRects[i].x+10,goalSettingsRects[i].y+30);
        }
        if (curSetting == UNLOCK) {
            g.setColor(Color.black);
            //drawing the scrollbar graphic for speed
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,60));
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",unsavedArrPoints[i]),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
        }
        if (curSetting == MASK) {
            //drawing the options for the masks
            g.setColor(Color.black);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,10));
            g.drawString("NONE",ctrPosition(new Rectangle(getTitlePosition("",g)-200,300,75,75),"NONE",g),300+40);
            Graphics2D g2D = (Graphics2D)g;
            for (int i = 1; i < goalMasks.length; i++) {
                g.drawImage(blockSprites[i-1],dialogCtr-200+100*(i%4),300+100*(i/4),null);
                if (unsavedMaskID.equals(blockStrings[i-1])) {
                    //emphasizing selected choice with bolded border
                    g2D.setStroke(new BasicStroke(5));
                    g2D.drawRect(dialogCtr-200+100*(i%4),300+100*(i/4),75,75);
                    g2D.setStroke(new BasicStroke(1));
                }

            }
            if (unsavedMaskID.equals("blank")) { //if "NONE" was selected
                g2D.setStroke(new BasicStroke(5));
                g2D.drawRect(getTitlePosition("",g)-200,300,75,75);
                g2D.setStroke(new BasicStroke(1));
            }

        }
    }
    //method that draws the settings for the message
    public void paintMessageSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(messageSettings[curSetting],getTitlePosition(messageSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < messageSettingsRects.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            if (curSetting == i || messageSettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(messageSettingsRects[i].x,messageSettingsRects[i].y,messageSettingsRects[i].width,messageSettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(messageSettings[i],messageSettingsRects[i].x+10,messageSettingsRects[i].y+30);
        }
        g.setColor(Color.BLUE);
        if (curSetting == TITLE) {
            //emphasizing text area
            g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
        }
    }
    //method that draws the settings for the key hole
    public void paintKeyHoleSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(keyHoleSettings[curSetting],getTitlePosition(keyHoleSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    //method that draws the settings for the spike
    public void paintSpikeSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(spikeSettings[curSetting],getTitlePosition(spikeSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        //drawing the scrollbar graphic for damage
        for (int i = 0; i < oneToFiveBlocks.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedDamage-1].x,oneToFiveBlocks[unsavedDamage-1].y,oneToFiveBlocks[unsavedDamage-1].width,oneToFiveBlocks[unsavedDamage-1].height);
    }
    //method that draws the settings for the coin
    public void paintCoinSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(coinSettings[curSetting],getTitlePosition(coinSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        //drawing the scrollbar graphic for point value
        for (int i = 0; i < oneToFiveBlocks.length; i++) {
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedPts-1].x,oneToFiveBlocks[unsavedPts-1].y,oneToFiveBlocks[unsavedPts-1].width,oneToFiveBlocks[unsavedPts-1].height);
    }
    //method that draws the settings for the health bonus
    public void paintHealthBonusSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(healthBonusSettings[curSetting],getTitlePosition(healthBonusSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        //drawing the scrollbar graphic for health value
        for (int i = 0; i < oneToFiveBlocks.length; i++) {
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedValue-1].x,oneToFiveBlocks[unsavedValue-1].y,oneToFiveBlocks[unsavedValue-1].width,oneToFiveBlocks[unsavedValue-1].height);
    }
    //method that draws the settings for the time bonus
    public void paintTimeBonusSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(timeBonusSetings[curSetting],getTitlePosition(timeBonusSetings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
    }
    //method that draws the settings for the key
    public void paintKeyInsertSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(keyInsertSettings[curSetting],getTitlePosition(keyInsertSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    //method that draws the settings for the countdown
    public void paintCountdownSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(countdownSettings[curSetting],getTitlePosition(countdownSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.drawString("Enter in format ##:##:##",getTitlePosition("Enter in format ##:##:##",g),300);
        g.setColor(Color.BLUE);
        //emphasizing text area
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    //method that draws the settings for the health object
    public void paintHealthSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString("Health",getTitlePosition("Health",g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        //emphasizing text area
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    //method that draws the settings for the level
    public void paintLevelSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(levelSettings[curSetting],getTitlePosition(levelSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);

        for (int i = 0; i < levelSettingsRects.length; i++) { //drawing the headers, different colors if mouse is hovering over or if the option was selected
            if (curSetting == i || levelSettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(levelSettingsRects[i].x,levelSettingsRects[i].y,levelSettingsRects[i].width,levelSettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(levelSettings[i],levelSettingsRects[i].x+10,levelSettingsRects[i].y+30);
        }
        g.setColor(Color.BLUE);
        if (curSetting == NAME) {
            //emphasizing text area
            g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
        }
        else if (curSetting == BACKGROUND) {
            Graphics2D g2D = (Graphics2D)g;
            for (int i = 0; i < backgrounds.length; i++) {
                g.drawImage(miniBackgrounds[i],350+250*(i%4),350+170*(i/4),null); //drawing the mini background
                if (unsavedBackground == new ImageIcon(backgrounds[i])) {
                    //emphasizing current choice
                    g2D.setStroke(new BasicStroke(10));
                    g.drawRect(miniBackgroundsRects[i].x,miniBackgroundsRects[i].y,miniBackgroundsRects[i].width,miniBackgroundsRects[i].height);
                    g2D.setStroke(new BasicStroke(1));
                }
            }
            //drawing the custom color choice
            g2D.setStroke(new BasicStroke(5));
            g2D.drawRect(miniBackgroundsRects[backgrounds.length].x,miniBackgroundsRects[backgrounds.length].y,miniBackgroundsRects[backgrounds.length].width,miniBackgroundsRects[backgrounds.length].height);
            g2D.setStroke(new BasicStroke(1));
            if (unsavedBackground == null) {
                //drawing the color interface
                g.fillRect(miniBackgroundsRects[backgrounds.length].x,miniBackgroundsRects[backgrounds.length].y,miniBackgroundsRects[backgrounds.length].width,miniBackgroundsRects[backgrounds.length].height);
                g.drawString("R",1500,350);
                g.drawString("G",1500,450);
                g.drawString("B",1500,550);
                g.setColor(Color.black);
                for (JTextArea area : rgbAreas) {
                    g.drawRect(area.getX(),area.getY(),area.getWidth(),area.getHeight());
                }

            }
            g.setColor(Color.orange);
            g.drawString("CUSTOM COLOR",ctrPosition(miniBackgroundsRects[backgrounds.length],"CUSTOM COLOR",g),miniBackgroundsRects[backgrounds.length].height/2+miniBackgroundsRects[backgrounds.length].y);
            try {
                //only successfully draws everything if color values are all valid, and also fills the preview color if valid
                int redValue = Integer.parseInt(rgbAreas[0].getText()), greenValue = Integer.parseInt(rgbAreas[1].getText()), blueValue = Integer.parseInt(rgbAreas[2].getText());
                if (redValue >= 0 && redValue <= 255 && greenValue >= 0 && greenValue <= 255 && blueValue >= 0 && blueValue <= 255) {
                    g.setColor(new Color(redValue,greenValue,blueValue));
                    g.fillRect(previewColorRect.x,previewColorRect.y,previewColorRect.width,previewColorRect.height);
                }

            }
            catch (NumberFormatException e) {}
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRect(previewColorRect.x,previewColorRect.y,previewColorRect.width,previewColorRect.height);
            g2D.setStroke(new BasicStroke(1));
        }
        else if (curSetting == NATURE) {
            String out = "Changing this will remove all avatar and enemy sprites, as they don't support the functionality of the other mode.";
            g.setColor(Color.BLACK);
            g.drawString(out,getTitlePosition(out,g),825);
            Graphics2D g2D = (Graphics2D)g;
            g.setColor(Color.YELLOW);
            //distinguishing the top down and platform rect via color fill
            if (topDown) {
                g.fillRect(topDownRect.x,topDownRect.y,topDownRect.width,topDownRect.height);
            }
            else {
                g.fillRect(platformRect.x,platformRect.y,platformRect.width,platformRect.height);
            }

            g.setColor(Color.RED);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            g.drawString("TOP-DOWN",ctrPosition(topDownRect,"TOP-DOWN",g),topDownRect.y+100);
            g.drawString("PLATFORM",ctrPosition(platformRect,"PLATFORM",g),topDownRect.y+100);



            g2D.setStroke(new BasicStroke(10));
            g2D.setColor(Color.BLACK);
            g2D.drawRect(topDownRect.x,topDownRect.y,topDownRect.width,topDownRect.height);
            g2D.drawRect(platformRect.x,platformRect.y,platformRect.width,platformRect.height);
            g2D.setStroke(new BasicStroke(1));
        }
    }

    //main drawing method
    public void paintComponent(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        if (levelSettingsRects == null) { //settings that require the Graphics g are put here, only run once
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            settingsRect = new Rectangle(gameRect.x/2-g.getFontMetrics().stringWidth("LEVEL SETTINGS")/2-10,900,g.getFontMetrics().stringWidth("LEVEL SETTINGS")+20,50);

            levelSettingsRects = new Rectangle[levelSettings.length];
            int paintX = 250; //holds the current start position of header
            for (int i = 0; i < levelSettingsRects.length; i++) { //runs thru for all headers
                levelSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(levelSettings[i])+20,50); //creating the rectangle used for collision detection for headers
                paintX += levelSettingsRects[i].width + 20;
            }

            avatarSettingsRects = new Rectangle[avatarSettings.length];
            paintX = 250;
            for (int i = 0; i < avatarSettingsRects.length; i++) { //runs thru for all headers
                avatarSettingsRects[i] = new Rectangle(paintX,250, g.getFontMetrics().stringWidth(avatarSettings[i])+20,50); //creating the rectangle used for collision detection for headers
                //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[i]));
                paintX += avatarSettingsRects[i].width + 10;
            }

            enemySettingsRects = new Rectangle[enemySettings.length];
            paintX = 250;
            for (int i = 0; i < enemySettingsRects.length; i++) { //runs thru for all headers
                enemySettingsRects[i] = new Rectangle(paintX,250, g.getFontMetrics().stringWidth(enemySettings[i])+20,50); //creating the rectangle used for collision detection for headers
                //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[i]));
                paintX += enemySettingsRects[i].width + 10;
            }


            goalSettingsRects = new Rectangle[2];
            paintX = 250;
            for (int i = 0; i < goalSettingsRects.length; i++) { //runs thru for all headers
                goalSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(goalSettings[i])+20,50); //creating the rectangle used for collision detection for headers
                paintX += goalSettingsRects[i].width + 10;
            }

            dialogCtr = getTitlePosition("",g);

            messageSettingsRects = new Rectangle[2];
            paintX = 250;
            for (int i = 0; i < messageSettingsRects.length; i++) { //runs thru for all headers
                messageSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(messageSettings[i])+20,50); //creating the rectangle used for collision detection for headers
                paintX += goalSettingsRects[i].width + 10;
            }

            paintX = 250;


            healthBonusSettingsRects = new Rectangle[1];
            healthBonusSettingsRects[0] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(healthBonusSettings[0])+20,50);

            timeBonusSettingsRects = new Rectangle[1];
            timeBonusSettingsRects[0] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(timeBonusSetings[0])+20,50);

            keyInsertSettingsRects = new Rectangle[1];
            keyInsertSettingsRects[0] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(keyInsertSettings[0])+20,50);

            countDownRect = new Rectangle(gameRect.x,gameRect.y+gameRect.height,80+g.getFontMetrics().stringWidth("00:00:00"),75);
            countdownSettingsRects = new Rectangle[1];
            countdownSettingsRects[0] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(countdownSettings[0])+20,50);

            paintX = countDownRect.x + countDownRect.width + 10;
            int paintY = countDownRect.y;
            pointTotalRect = new Rectangle(paintX,paintY,150,50);

            paintX += pointTotalRect.width;
            healthRect = new Rectangle(paintX,paintY,150,50);



        }
        if (readyToDelete) mainFrame.setCursor(Cursor.CROSSHAIR_CURSOR); //separate cursor for deletion, looks nicer
        else mainFrame.setCursor(Cursor.getDefaultCursor());
        g.drawImage(coolBack,0,0,null); //drawing the background of the edit panel
        g.setFont(font);
        g.setColor(BROWN);
        g.fillRect(spritesRect.x,spritesRect.y,spritesRect.width,spritesRect.height);
        g.setColor(Color.black);
        //drawing the lines that seperate the sprties
        for (int lx = 40+75; lx < 300; lx += 75) g.drawLine(lx,300,lx,600);
        for (int ly = 300+75; ly < 600; ly+= 75) g.drawLine(40,ly,340,ly);

        g.setColor(Color.green);
        //drawing each of the sprite type headers
        g.fillRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
        g.fillRect(enemyRect.x,enemyRect.y,enemyRect.width,enemyRect.height);
        g.fillRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
        g.fillRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
        g.fillRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);

        g.setColor(Color.lightGray);
        int cnt = 0;
        if (curType == AVATAR) {
            //drawing sprite options for avatar
            g.fillRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
            if (topDown) {
                for (int i = 0; i < playerTopDownSprites.length; i++) { //draws the top down avatar options
                    g.drawImage(playerTopDownSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
            else {
                for (int i = 0; i < playerPlatSprites.length; i++) { //draws the platform avatar options
                    g.drawImage(playerPlatSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
        }
        else if (curType == ENEMY) {
            //drawing sprite options for enemy
            g.fillRect(enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
            if (topDown) {
                for (int i = 0; i < enemyTopDownSprites.length; i++) { //draws the top down enemy options
                    g.drawImage(enemyTopDownSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
            else {
                for (int i = 0; i < enemyPlatSprites.length; i++) { //draws the platform enemy options
                    g.drawImage(enemyPlatSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
        }
        else if (curType == BLOCK) {
            //drawing sprite options for blocks
            g.fillRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
            for (int i = 0; i < blockSprites.length; i++) {
                g.drawImage(blockSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        else if (curType == ITEM) {
            //drawing sprite options for items
            g.fillRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
            for (int i = 0; i < itemSprites.length; i++) {
                g.drawImage(itemSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        else if (curType == SYSTEM) {
            //drawing sprite options for system assets
            g.fillRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);
            for (int i = 0; i < systemSprites.length; i++) {
                g.drawImage(systemSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        if (backgroundColor != null){ //fills the game rectangle with the background color, if it exists
            g.setColor(backgroundColor);
            g.fillRect(gameRect.x,gameRect.y,gameRect.width,gameRect.height);
        }
        else g.drawImage(levelBackground.getImage(),gameRect.x,gameRect.y,null); //otherwise, it draws the current background image chosen
        if (settingsRect.contains(mouse)) g.setColor(Color.red); //emphasis on the level settings rectangle
        else g.setColor(Color.black);
        g.fillRect(settingsRect.x,settingsRect.y,settingsRect.width,settingsRect.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.drawString("LEVEL SETTINGS",settingsRect.x+10,settingsRect.y+35);
        g.setFont(font);
        //draws the blue lines on the game to make it easier to see the different locations to insert sprites
        g.setColor(Color.blue);
        for (int lx = 575; lx < 1700; lx += 75) g.drawLine(lx,150,lx,900);
        for (int ly = 225; ly < 950; ly += 75) g.drawLine(500,ly,1700,ly);

        g.drawImage(title,500,20,null);
        g.setColor(new Color(215,47,198));
        //drawing the headers for the sprite options
        g.drawString("Avatar",avatarRect.x+10,avatarRect.y+20);
        g.drawString("Enemy",enemyRect.x+7,enemyRect.y+20);
        g.drawString("Block",blockRect.x+10,blockRect.y+20);
        g.drawString("Items",itemRect.x+10,itemRect.y+20);
        g.drawString("System",systemRect.x+5,systemRect.y+20);
        g.setColor(Color.black);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setStroke(new BasicStroke(3));
        //emphasizing the options for the sprite headers
        g2D.drawRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
        g2D.drawRect(enemyRect.x,enemyRect.y,enemyRect.width,enemyRect.height);
        g2D.drawRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
        g2D.drawRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
        g2D.drawRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);
        g2D.setStroke(new BasicStroke(1));

        for (int i = 0; i < 3; i++) { //drawing the tool images
            if (curTool == i) {
                g.drawImage(toolImages[i],1750,300+125*i,null);
            }
            else {
                g.drawImage(toolClickedImages[i],1750,300+125*i,null);
            }
        }
        //drawing the current selection beside the mouse for aesthetics
        if (readyToPaste) {
            if (insertTeleport) { //if exit teleport needs to be inserted
                g.drawImage(teleportOut,mouse.x-75,mouse.y,null);
            }
            else if (curImage != null) { //otherwise, use the current sprite
                g.drawImage(curImage.getImage(), mouse.x - 75, mouse.y, null);
            }
        }
        //filling in the background of the sprite location to create a highlighting effect
        if (gameRect.contains(mouse) && (readyToPaste || readyToDelete || readyToModify)) {
            if (readyToPaste) {
                g.setColor(TRANSPARENTBLUE);
            }
            else if (readyToDelete) {
                g.setColor(TRANSPARENTRED);
            }
            else  {
                g.setColor(TRANSPARENTGREEN);
            }
            g.fillRect(((mouse.x - gameRect.x) / 75) * 75 + gameRect.x, ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y,75, 75); //filling the background of the sprite location
        }
        g.setColor(Color.YELLOW);
        Iterator<Map.Entry<Integer, Sprite>> it = Sprite.getSpriteHashMap().entrySet().iterator(); //iterator to run through each sprite in the hash map
        while (it.hasNext()) {
            Map.Entry<Integer, Sprite> pair = it.next(); //getting next entry
            int drawX = pair.getKey()/200000, drawY = pair.getKey()%200000; //getting the x and y locations from the key
            //if the sprite is in the current frame, accounting for the offset
            if (offX*75 + gameRect.x <= drawX && (offX + 16)*75 + gameRect.x >= drawX && offY*75 + gameRect.y <= drawY && (offY + 10)*75 + gameRect.y >= drawY) {
                g.drawImage(pair.getValue().getImg().getImage(), drawX - (offX*75), drawY - (offY*75), null);
                g.drawRect(pair.getValue().hitBox.x-(offX*75),pair.getValue().hitBox.y-(offY*75),pair.getValue().hitBox.width,pair.getValue().hitBox.height);
            }
        }
        //if the setting of an object is being modified
        if (changeAvatarPrompt || changeAvatarSettings || changeEnemySettings || changeGoalSettings ||
                changeMessageSettings || changeKeyHoleSettings || changeSpikeSettings || changeCoinSettings ||
                changeHealthBonusSettings || changeTimeBonusSettings || changeKeyInsertSettings ||
                changeCountdownSettings || changeHealthSettings || changeLevelSettings) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,1920,1080); //darkens the background
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,60));
            g.drawImage(promptBack,100,100,null); //drawing the background for the setting
            //calling the respective method for each
            if (changeAvatarPrompt) paintChangeAvatar(g);
            else if (changeAvatarSettings) paintAvatarSettings(g);
            else if (changeEnemySettings) paintEnemySettings(g);
            else if (changeGoalSettings) paintGoalSettings(g);
            else if (changeMessageSettings) paintMessageSettings(g);
            else if (changeKeyHoleSettings) paintKeyHoleSettings(g);
            else if (changeSpikeSettings) paintSpikeSettings(g);
            else if (changeCoinSettings) paintCoinSettings(g);
            else if (changeHealthBonusSettings) paintHealthBonusSettings(g);
            else if (changeTimeBonusSettings) paintTimeBonusSettings(g);
            else if (changeKeyInsertSettings) paintKeyInsertSettings(g);
            else if (changeCountdownSettings) paintCountdownSettings(g);
            else if (changeHealthSettings) paintHealthSettings(g);
            else if (changeLevelSettings) paintLevelSettings(g);

            g.setColor(Color.ORANGE);
            //drawing the save and cancel rectangles, with emphasis on each if they are hovered on
            g.fillRect(yesRect.x,yesRect.y,yesRect.width,yesRect.height);
            g.fillRect(noRect.x,noRect.y,noRect.width,noRect.height);
            //emphasis
            if (yesRect.contains(mouse)) g.drawImage(buttonClickedImage,yesRect.x,yesRect.y,null);
            else g.drawImage(buttonImage,yesRect.x,yesRect.y,null);
            if (noRect.contains(mouse)) g.drawImage(buttonClickedImage,noRect.x,noRect.y,null);
            else g.drawImage(buttonImage,noRect.x,noRect.y,null);

            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            g.drawString("SAVE",ctrPosition(yesRect,"SAVE",g),yesRect.y+70);
            g.drawString("CANCEL",ctrPosition(noRect,"CANCEL",g),noRect.y+70);
        }
        else {
            //no settings are being changed, so all other assets, buttons, and images can be drwan
            //drawing the scroll buttons
            g.drawImage(left,leftRect.x,leftRect.y,null);
            g.drawImage(right,rightRect.x,rightRect.y,null);
            g.drawImage(up,upRect.x,upRect.y,null);
            g.drawImage(down,downRect.x,downRect.y,null);

            if (curCountdown != null) {
                g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
                if (countDownRect.contains(mouse)) { //highlighting the background of the countdown object for emphasis if hovered on
                    g.setColor(TRANSPARENTGREEN);
                    g.fillRect(countDownRect.x,countDownRect.y,countDownRect.width,countDownRect.height);
                }
                g.setColor(Color.WHITE);
                g.drawImage(systemSprites[0],countDownRect.x,countDownRect.y,null);
                g.drawString(curCountdown.getStrTime(),countDownRect.x+80,countDownRect.y+50);
            }
            if (curPointTotal != null) {
                g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
                g.drawImage(systemSprites[1],pointTotalRect.x,pointTotalRect.y,null);
                g.setColor(Color.YELLOW);
                g.drawLine(pointTotalRect.x+80,pointTotalRect.y+37,pointTotalRect.x+pointTotalRect.width,pointTotalRect.y+37);
                g.setColor(Color.BLUE);
                //drawing the numerator of the fraction of killed/total
                g.drawString("0",ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y,pointTotalRect.width-80,37),"0",g),pointTotalRect.y+30);
                g.setColor(Color.WHITE);
                //darwing the denominator of the fraction of killed/total
                g.drawString(String.format("%d",curPointTotal.getTotal()),ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y+37,pointTotalRect.width-80,38),String.format("%d",curPointTotal.getTotal()),g),pointTotalRect.y+pointTotalRect.height+15);

            }
            if (curHealth != null) {
                if (healthRect.contains(mouse)) { //highlighting the background of the health object for emphasis if hovered on
                    g.setColor(TRANSPARENTGREEN);
                    g.fillRect(healthRect.x,healthRect.y,healthRect.width,healthRect.height);
                }
                g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
                g.drawImage(systemSprites[2],healthRect.x,healthRect.y,null);
                g.setColor(Color.YELLOW);
                g.drawLine(healthRect.x+80,healthRect.y+37,healthRect.x+healthRect.width,healthRect.y+37);
                g.setColor(Color.BLUE);
                //drawing the numerator of the fraction of remaining/initial health
                g.drawString("0",ctrPosition(new Rectangle(healthRect.x+80,healthRect.y,healthRect.width-80,37),"0",g),healthRect.y+30);
                g.setColor(Color.WHITE);
                //drawing the denominator of the fraction of remaining/initial health
                g.drawString(String.format("%d",curHealth.getValue()),ctrPosition(new Rectangle(healthRect.x+80,healthRect.y+37,healthRect.width-80,38),String.format("%d",curHealth.getValue()),g),healthRect.y+healthRect.height+15);
            }
            if (saveRect.contains(mouse))
                g.setColor(TRANSPARENTRED);
            else
                g.setColor(TRANSPARENTGREEN);
            g.fillRect(saveRect.x,saveRect.y,saveRect.width,saveRect.height);
            g.setColor(Color.BLACK);
            g.drawRect(saveRect.x,saveRect.y,saveRect.width,saveRect.height);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,60));
            g.drawString("Save",ctrPosition(saveRect,"Save",g),70);
        }
        g.setFont(font);
        g.setColor(Color.black);
        g.setFont(font);

    }

    //MouseListener methods
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e) {
        try {
            update(); //update only runs if the mouse was pressed
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //KeyListener methods
    public void keyTyped(KeyEvent e) { }


    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (changeAvatarPrompt) { //only used for Y/N confirmation in the change avatar prompt
            try {
                update();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    //method that returns the start of the position of a String so that it is centered around the middle of the screen
    public int getTitlePosition(String str,Graphics g) {
        return ctrPosition(new Rectangle(0,0,1920,1080),str,g);
    }

    //method that returns the start of the position of a String so that it is centered around the middle of the specified bounds
    public static int ctrPosition(Rectangle bounds, String str, Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        int width = g.getFontMetrics().stringWidth(str); //getting the width of the String
        int ctrX = bounds.x+(bounds.width-width)/2; //getting the x-position to draw the String in order to be centered
        return ctrX;
    }

    //method that finds the String specified in a given String array. If it DNE, it returns -1.
    public static int find(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(val)) return i;
        }
        return -1;
    }
}