import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class EditPanel extends JPanel implements MouseListener, KeyListener, java.io.Serializable {
    private Edit mainFrame;
    private Image coolBack;
    public static Image[] systemSprites,itemSprites,blockSprites,playerPlatSprites,enemyPlatSprites,playerTopDownSprites,enemyTopDownSprites;
    public static String[] systemStrings,itemStrings,blockStrings,playerPlatStrings,enemyPlatStrings,playerTopDownStrings,enemyTopDownStrings;
    private Rectangle avatarRect,enemyRect,blockRect,itemRect,systemRect,spritesRect;
    private Rectangle gameRect;
    private Image title;

    public static final int AVATAR = 0, ENEMY = 1, BLOCK = 2, ITEM = 3, SYSTEM = 4;
    private int curType;
    private Image[] maroonTexts = new Image[5];
    private Image[] blueTexts = new Image[5];
    private Font font;
    public static final Color BROWN = new Color(210,105,30),
            TRANSPARENTRED = new Color(255,0,0,100),
            TRANSPARENTGREEN = new Color(0,255,0,100),
            TRANSPARENTBLUE = new Color(0,0,255,100) ;
    private Point mouse;
    public static final int MOUSE = 0, TOOL = 1, DELETE = 2;
    public int curTool = MOUSE;
    private Image[] toolImages;
    private Image[] toolClickedImages;
    private String[] toolStrings;
    private Ellipse2D.Double[] toolEllipses;
    private Ellipse2D.Double upEllipse, downEllipse, leftEllipse, rightEllipse, yesEllipse, noEllipse;
    private String curSprite;
    private ImageIcon curImage;
    private boolean readyToPaste,readyToDelete,readyToModify;
    private boolean containsAvatar,isAvatar;
    private boolean changeAvatarPrompt;
    private int avatarX,avatarY,newavatarX,newavatarY;
    private boolean[] keys;
    String s = "";
    private Image promptBack,buttonImage,buttonClickedImage;
    private Rectangle yesRect,noRect;
    private Avatar avatar;
    private Enemy curEnemy;
    private Goal curGoal;
    private boolean changeAvatarSettings, changeEnemySettings,changeGoalSettings,changeMessageSettings,
            changeKeyHoleSettings,changeSpikeSettings,changeCoinSettings,changeHealthBonusSettings,
            changeTimeBonusSettings,changeKeyInsertSettings;
    public static final int SPEED = 0, HEALTH = 1, BULLETSPEED = 2, STATIONARY = 3, DIRECTION = 4;
    public static final String[] avatarSettings = new String[]{"Speed","Health","Bullet Speed"},
            enemySettings = new String[]{"Speed","Health","Stationary","Direction","Bullet Speed"},
            goalSettings = new String[]{"Mask","Unlock"},
            goalMasks = new String[]{"None","Cement","Cloud","Dirt","Glass","Gold","Grass"},
            messageSettings = new String[]{"Title","Content"},
            keyHoleSettings = new String[]{"Unlock Requirement"},
            spikeSettings = new String[]{"Damage"},
            coinSettings = new String[]{"Points"},
            healthBonusSettings = new String[]{"Value"},
            timeBonusSetings = new String[]{"Value (s)"},
            keyInsertSettings = new String[]{"Value"};
    public static final int MASK = 0, UNLOCK = 1;
    private Rectangle[] avatarSettingsRects, enemySettingsRects, goalSettingsRects,messageSettingsRects,healthBonusSettingsRects,timeBonusSettingsRects,
            keyInsertSettingsRects;
    private int curSetting;
    private Rectangle[] oneToTenBlocks,oneToFiveBlocks;
    private int unsavedSpeed,unsavedHealth,unsavedDirection,unsavedBulletSpeed;
    private boolean unsavedStationary;
    private Image unsavedMask;
    private String unsavedMaskID;
    private int[] unsavedArrPoints;
    private int dialogCtr;
    public static final int TITLE = 0, CONTENT = 1;
    private JTextArea titleArea,contentArea;
    private JScrollPane contentPane;
    private Message curMessage;
    public static final int UNLOCKREQUIREMENT = 0;
    private KeyHole curKeyHole;
    public static final int DAMAGE = 0;
    private Spike curSpike;
    private int unsavedDamage;
    public static final int POINTS = 0;
    private Coin curCoin;
    private int unsavedPts;
    private boolean insertTeleport;
    private Teleport entry,exitt;
    private Image teleportOut;
    private String teleportOutID;

    private HealthBonus curHealthBonus;
    private int unsavedValue;

    private TimeBonus curTimeBonus;

    private KeyInsert curKeyInsert;

    private boolean changeCountdownSettings,changeHealthSettings;
    private String[] countdownSettings = {"Time"};
    private Rectangle countDownRect,pointTotalRect,healthRect;
    private Rectangle[] countdownSettingsRects;
    private Countdown curCountdown;
    private PointTotal curPointTotal;
    private Health curHealth;
    private Image left,right,up,down;
    private Rectangle leftRect,rightRect,upRect,downRect;
    private int offX,offY;
    private int numGoals = 1;
    private boolean changeLevelSettings;
    private String levelName, levelDescription;
    private Image levelBackground,unsavedBackground;
    private boolean topDown;
    private String[] levelSettings;
    private Rectangle[] levelSettingsRects;
    private Rectangle settingsRect;
    private Color backgroundColor,unsavedBackgroundColor;
    public static final int NAME = 0, DESCRIPTION = 1, BACKGROUND = 2, NATURE = 3;
    private Image[] backgrounds,miniBackgrounds;
    private String[] backgroundStrings;
    private Rectangle[] miniBackgroundsRects;
    private JTextArea[] rgbAreas;
    private Rectangle previewColorRect;
    private Rectangle rect1,rect2;

    private Rectangle saveRect;


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

        for (int i = 0; i < 5; i++) {
            maroonTexts[i] = new ImageIcon(String.format("Text Images/%dmaroon.png",i)).getImage();
            blueTexts[i] = new ImageIcon(String.format("Text Images/%dblue.png",i)).getImage();
        }
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
        rect1 = new Rectangle(350,500,400,200);
        rect2 = new Rectangle(1100,500,400,200);
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
    public void update() throws IOException, ClassNotFoundException {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        if (insertTeleport) {
            if (curTool == MOUSE && gameRect.contains(mouse) && readyToPaste && curType == BLOCK && curSprite == "teleportIn") {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                exitt = new Teleport(teleportOutID,sx,sy,new ImageIcon(teleportOut));
                exitt.init();
                entry.setExit(exitt);
                exitt.setEntry(entry);
                entry = null; exitt = null; insertTeleport = false;
            }
            return;
        }
        if (changeAvatarPrompt) {
            if (keys[KeyEvent.VK_Y] || yesRect.contains(mouse)) {
                changeAvatarPrompt = false;
                Sprite.delete(avatarX,avatarY);
                avatarX = newavatarX;
                avatarY = newavatarY;
                avatar = new Avatar(curSprite,avatarX,avatarY,curImage);
                avatar.init();
                newavatarY = -10;
                newavatarX = -10;
                curSprite = null;
                curImage = null;
                readyToPaste=true;
            }
            else if (keys[KeyEvent.VK_N] || noRect.contains(mouse)) {
                changeAvatarPrompt = false;
                newavatarX = -10;
                newavatarY = -10;
                curSprite = null;
                curImage = null;
                readyToPaste=true;

            }

            return;
        }
        if (changeAvatarSettings) {
            for (int i = 0; i < avatarSettingsRects.length; i++) {
                if (avatarSettingsRects[i].contains(mouse)) {
                    curSetting = i;
                }
            }
            if (curSetting == SPEED) {
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedSpeed = i+1;
                            break;
                        }
                    }
                }
            }
            else if (curSetting == HEALTH) {
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedHealth = i+1;
                            break;

                        }
                    }
                }
            }
            else if (curSetting == BULLETSPEED) {
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedBulletSpeed = i+1;
                            break;
                        }
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                avatar.setHealth(unsavedHealth);
                avatar.setSpeed(unsavedSpeed);
                avatar.setBulletSpeed(unsavedBulletSpeed);
                changeAvatarSettings = false;
            }
            else if (noRect.contains(mouse)) {
                changeAvatarSettings = false;
            }
            return;
        }
        if (changeEnemySettings) {
            for (int i = 0; i < enemySettingsRects.length; i++) {
                if (enemySettingsRects[i].contains(mouse)) {
                    curSetting = i;
                }
            }
            if (curSetting == SPEED && !unsavedStationary) {
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedSpeed = i+1;
                            break;

                        }
                    }
                }
            }
            else if (curSetting == HEALTH) {
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedHealth = i+1;
                            break;

                        }
                    }
                }
            }
            else if (curSetting == BULLETSPEED) {
                if (Math.abs(mouse.y - (oneToTenBlocks[0].y + oneToTenBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 10; i++) {
                        if (Math.abs(mouse.x-(oneToTenBlocks[i].x+oneToTenBlocks[i].width/2)) < 50) {
                            unsavedBulletSpeed = i+1;
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

            if (yesRect.contains(mouse)) {
                curEnemy.setHealth(unsavedHealth);
                curEnemy.setSpeed(unsavedSpeed);
                curEnemy.setBulletSpeed(unsavedBulletSpeed);
                curEnemy.setStationary(unsavedStationary);
                curEnemy.setDirection(unsavedDirection);
                String dir,spriteType;
                if (topDown) spriteType = "Top-Down";
                else spriteType = "Platform";
                if (unsavedDirection == Enemy.RIGHT) dir = "R";
                else if (unsavedDirection == Enemy.UP) dir = "U";
                else if (unsavedDirection == Enemy.LEFT) dir = "L";
                else dir = "D";
                curEnemy.getSprite().setImg(new ImageIcon(new ImageIcon(String.format("%s/%s%s0.png",spriteType,curEnemy.getId(),dir)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));


                changeEnemySettings = false;
                curEnemy = null;
            }
            else if (noRect.contains(mouse)) {
                changeEnemySettings = false;
                curEnemy = null;
            }
            return;
        }
        if (changeGoalSettings) {
            for (int i = 0; i < goalSettingsRects.length; i++) {
                if (goalSettingsRects[i].contains(mouse)) {
                    if (i == 0) curSetting = MASK;
                    else curSetting = UNLOCK;
                }
            }

            if (curSetting == UNLOCK) {
                for (int i = 0; i < unsavedArrPoints.length; i++) {
                    if (Math.abs(mouse.y - (oneToFiveBlocks[i].y + oneToFiveBlocks[i].height / 2)) < 40) {
                        if (Math.abs(mouse.x - (oneToFiveBlocks[i].x + oneToFiveBlocks[i].width / 2)) < 50) {
                            unsavedArrPoints[i]++;
                            unsavedArrPoints[i] %= 10;
                            break;
                        }
                    }
                }
            }
            if (curSetting == MASK) {
                for (int i = 0; i < goalMasks.length; i++) {
                    //System.out.println(goalMasks.length);
                    if (new Rectangle(dialogCtr-200+100*(i%4),300+100*(i/4),75,75).contains(mouse)) {
                        if (i == 0) {
                            unsavedMask = null;
                            unsavedMaskID = "";
                        }
                        else {
                            unsavedMask = blockSprites[i-1];
                            unsavedMaskID = blockStrings[i-1];
                        }
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                curGoal.setMaskID(unsavedMaskID);
                //curGoal.setMask(unsavedMask);
                curGoal.setPointsToOpen(unsavedArrPoints[0]*10000+unsavedArrPoints[1]*1000+unsavedArrPoints[2]*100+unsavedArrPoints[3]*10+unsavedArrPoints[4]);
                changeGoalSettings = false;
            }
            else if (noRect.contains(mouse)) {
                changeGoalSettings = false;
            }
            return;
        }
        if (changeMessageSettings) {
            for (int i = 0; i < messageSettingsRects.length; i++) {
                if (messageSettingsRects[i].contains(mouse)) {
                    curSetting = i;
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
                titleArea.setText(titleArea.getText().replaceAll("\n",""));
                if (titleArea.getText().length() > 45) titleArea.setText(titleArea.getText().substring(0,45));
            }

            if (yesRect.contains(mouse)) {
                changeMessageSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
                contentArea.setVisible(false); contentArea.setEditable(false); contentPane.setVisible(false);
                curMessage.setTitle(titleArea.getText());
                curMessage.setContent(contentArea.getText());
            }
            else if (noRect.contains(mouse)) {
                changeMessageSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
                contentArea.setVisible(false); contentArea.setEditable(false); contentPane.setVisible(false);
            }
            return;
        }
        if (changeKeyHoleSettings) {
            if (yesRect.contains(mouse)) {
                try {
                    curKeyHole.setUnlockRequirement(Integer.parseInt(titleArea.getText()));
                    changeKeyHoleSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            if (noRect.contains(mouse)) {
                changeKeyHoleSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeSpikeSettings) {
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedDamage = i+1;
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                curSpike.setDmg(unsavedDamage);
                changeSpikeSettings = false;
            }
            else if (noRect.contains(mouse)) {
                changeSpikeSettings = false;
            }
            return;
        }
        if (changeCoinSettings) {
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedPts = i+1;
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                curPointTotal.decrease(curCoin.getPts());
                curCoin.setPts(unsavedPts);
                curPointTotal.increase(curCoin.getPts());
                changeCoinSettings = false;
            }
            else if (noRect.contains(mouse)) {
                changeCoinSettings = false;
            }
            return;
        }
        if (changeHealthBonusSettings) {
            if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                for (int i = 0; i < 5; i++) {
                    if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                        unsavedValue = i+1;
                        break;
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                curHealthBonus.setValue(unsavedValue);
                changeHealthBonusSettings = false;
            }
            else if (noRect.contains(mouse)) {
                changeHealthBonusSettings = false;
            }
            return;
        }
        if (changeTimeBonusSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n",""));
            if (yesRect.contains(mouse)) {
                try {
                    curTimeBonus.setValue(Integer.parseInt(titleArea.getText()));
                    changeTimeBonusSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            else if (noRect.contains(mouse)) {
                changeTimeBonusSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
        }
        if (changeKeyInsertSettings) {
            if (yesRect.contains(mouse)) {
                try {
                    curKeyInsert.setValue(Integer.parseInt(titleArea.getText()));
                    changeKeyInsertSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            if (noRect.contains(mouse)) {
                changeKeyInsertSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeCountdownSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n",""));
            if (yesRect.contains(mouse)) {
                String s = titleArea.getText();
                if (s.charAt(2) == ':' && s.charAt(5) == ':' && s.length() == 8) {
                    try {
                        int tl = Integer.parseInt(s.substring(0,2))*3600+Integer.parseInt(s.substring(3,5))*60+Integer.parseInt(s.substring(6));
                        //System.out.println(tl);
                        curCountdown.setTimeLeft(s);
                        changeCountdownSettings = false;
                        titleArea.setVisible(false); titleArea.setEditable(false);
                    }
                    catch (NumberFormatException e) {}
                }
            }
            else if (noRect.contains(mouse)) {
                changeCountdownSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (changeHealthSettings) {
            titleArea.setText(titleArea.getText().replaceAll("\n",""));
            if (yesRect.contains(mouse)) {
                try {
                    curHealth.setValue(Integer.parseInt(titleArea.getText()));
                    changeHealthSettings = false;
                    titleArea.setVisible(false); titleArea.setEditable(false);
                }
                catch (NumberFormatException e) {}
            }
            else if (noRect.contains(mouse)) {
                changeHealthSettings = false;
                titleArea.setVisible(false); titleArea.setEditable(false);
            }
            return;
        }
        if (settingsRect.contains(mouse)) {
            changeLevelSettings = true;
            unsavedBackground = this.levelBackground;
            titleArea.setVisible(true); titleArea.setEditable(true);
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
                    curSetting = i;
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
                for (int i = 0; i < backgrounds.length; i++) {
                    if (miniBackgroundsRects[i].contains(mouse)) {
                        unsavedBackground = backgrounds[i];
                        for (JTextArea area : rgbAreas) {
                            area.setVisible(false);
                            area.setEditable(false);
                        }
                    }
                }
                if (miniBackgroundsRects[backgrounds.length].contains(mouse)) {
                    unsavedBackground = null;
                    for (JTextArea area : rgbAreas) {
                        area.setVisible(true);
                        area.setEditable(true);
                    }
                }
            }
            if (curSetting == NATURE) {
                if (rect1.contains(mouse)) {
                    topDown = true;
                    Sprite.clear();
                }
                else if (rect2.contains(mouse)) {
                    topDown = false;
                    Sprite.clear();
                }
            }
            if (yesRect.contains(mouse)) {
                if (unsavedBackground != null) {
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
                else {
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
            else if (noRect.contains(mouse)) {
                contentArea.setEditable(false); contentArea.setVisible(false); contentPane.setVisible(false);
                titleArea.setEditable(false); titleArea.setVisible(false);
                changeLevelSettings = false;
            }
            return;
        }
        if (saveRect.contains(mouse) && numGoals > 0 && avatar != null) {

            OutputStream file = new FileOutputStream(String.format("%s.txt",levelName),false);
            ObjectOutputStream outStream = new ObjectOutputStream(file);
            //writing objects to file
            outStream.writeObject(topDown);
            if (backgroundColor != null) {
                outStream.writeObject(backgroundColor);
            }
            else {
                outStream.writeObject(new ImageIcon(levelBackground));
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

            Iterator<Map.Entry<Integer, Sprite>> it = Sprite.getSpriteHashMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Sprite> pair = it.next();
                Sprite sprite = pair.getValue();
                outStream.writeObject(sprite);
            }
            file.close();
            outStream.close();/*
            FileInputStream america = new FileInputStream(String.format("%s.txt",levelName));
            ObjectInputStream in = new ObjectInputStream(america);
            Sprite first = (Sprite)in.readObject();
            if (first.instance instanceof Message) {
                System.out.println(((Message) first.instance).getContent());
                System.out.println(((Message) first.instance).getTitle());
            }
            boolean b = (boolean)in.readObject();
            if (b) System.out.println("true");
            else System.out.println("false");
            in.readObject();
            String s = (String)in.readObject();
            System.out.println(s);
            s = (String)in.readObject();
            System.out.println(s);
            Color c = (Color)in.readObject();
            if (c != null) System.out.printf("%d %d %d",c.getRed(),c.getGreen(),c.getBlue());
            */


            System.out.println(55);
        }
        if (avatarRect.contains(mouse)) curType = AVATAR;
        else if (enemyRect.contains(mouse)) curType = ENEMY;
        else if (blockRect.contains(mouse)) curType = BLOCK;
        else if (itemRect.contains(mouse)) curType = ITEM;
        else if (systemRect.contains(mouse)) curType = SYSTEM;



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
            }
            else if (curTool == MOUSE) {
                readyToPaste = true;
            }
            else {
                readyToModify = true;
            }

        }
        if (curTool == MOUSE) {
            if (spritesRect.contains(mouse)) {
                int ind = (mouse.x - 40) / 75 + 4 * (mouse.y - 300) / 75 - 1;
                if (curType == AVATAR) {
                    if (topDown && playerTopDownSprites.length > ind) {
                        curSprite = playerTopDownStrings[ind];
                        curImage = new ImageIcon(playerTopDownSprites[ind]);
                        isAvatar = true;
                    }
                    else if (!topDown && playerPlatSprites.length > ind) {
                        curSprite = playerPlatStrings[ind];
                        curImage = new ImageIcon(playerPlatSprites[ind]);
                        isAvatar = true;
                    }
                }
                else if (curType == ENEMY) {
                    if (topDown && enemyTopDownSprites.length > ind) {
                        curSprite = enemyTopDownStrings[ind];
                        curImage = new ImageIcon(enemyTopDownSprites[ind]);
                        isAvatar = false;
                    }
                    else if (!topDown && playerPlatSprites.length > ind) {
                        curSprite = enemyPlatStrings[ind];
                        curImage = new ImageIcon(enemyPlatSprites[ind]);
                        isAvatar = false;
                    }

                }
                else if (curType == BLOCK) {
                    //System.out.println(ind);
                    if (ind < blockSprites.length) {
                        curSprite = blockStrings[ind];
                        //System.out.println(curSprite);
                        curImage = new ImageIcon(blockSprites[ind]);
                        isAvatar = false;
                    }
                }
                else if (curType == ITEM) {
                    if (itemSprites.length > ind) {
                        curSprite = itemStrings[ind];
                        curImage = new ImageIcon(itemSprites[ind]);
                        isAvatar = false;
                    }
                }
                else if (curType == SYSTEM) {
                    if (systemSprites.length > ind) {
                        if (ind == 0) {
                            curCountdown = new Countdown();
                        }
                        else if (ind == 1) {
                            curPointTotal = new PointTotal();
                        }
                        else if (ind == 2) {
                            curHealth = new Health();
                        }
                    }
                }
            }
            if ((spritesRect.contains(mouse) || gameRect.contains(mouse)) && curSprite != null) readyToPaste = true;
            else readyToPaste = false;

            if (readyToPaste && gameRect.contains(mouse)) {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                if (!isAvatar || !containsAvatar) {
                   // new Sprite(curSprite, sx, sy, curImage);
                    if (isAvatar) {
                        containsAvatar = true;
                        avatarX = sx;
                        avatarY = sy;
                        //avatar = new Avatar(curSprite);
                        avatar = new Avatar(curSprite,sx,sy,curImage);
                        avatar.init();
                    }
                    else if (curType == ENEMY) {
                        new Enemy(curSprite,sx,sy,curImage,3,5).init();
                    }
                    else if (curType == BLOCK) {
                        int idx = find(blockStrings,curSprite);
                        if (idx < 6) new Block(curSprite, sx, sy, curImage).init();
                        else if (idx < 7) {
                            new Goal(curSprite,sx,sy,curImage,null).init();
                            numGoals++;
                        }
                        else if (idx < 8) {
                            new Goal(curSprite,sx,sy,curImage,"cement").init();
                            numGoals++;
                        }
                        else if (idx < 9) new Message(curSprite,sx,sy,curImage).init();
                        else if (idx < 11) new KeyHole(curSprite,sx,sy,curImage,idx-9).init();
                        else if (idx < 12) new Spike(curSprite,sx,sy,curImage).init();
                        else {
                            if (!insertTeleport) {
                                entry = new Teleport(curSprite, sx, sy, curImage);
                                entry.init();
                                insertTeleport = true;
                            }
                        }
                    }
                    else if (curType == ITEM) {
                        int idx = find(itemStrings,curSprite);
                        if (idx == 0) {
                            new Coin(curSprite,sx,sy,curImage,false).init();
                            if (curPointTotal == null) curPointTotal = new PointTotal(1);
                            else curPointTotal.increase(1);
                        }
                        else if (idx == 1) {
                            new Coin(curSprite,sx,sy,curImage,true).init();
                            if (curPointTotal == null) curPointTotal = new PointTotal(3);
                            else curPointTotal.increase(3);
                        }
                        else if (idx == 2) new HealthBonus(curSprite,sx,sy,curImage).init();
                        else if (idx == 3) new TimeBonus(curSprite,sx,sy,curImage).init();
                        else if (idx < 6) new KeyInsert(curSprite,sx,sy,curImage,idx-4).init();
                    }/*
                    else if (curType == SYSTEM) {
                        System sys = new System(curSprite,sx,sy,curImage);
                        sys.init();
                    }*/
                } else  {
                    changeAvatarPrompt = true;
                    newavatarX = sx;
                    newavatarY = sy;
                }
            }
        }
        else if (curTool == DELETE) {
            if (gameRect.contains(mouse)) {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                if (Sprite.getSpriteHashMap().get(sx*200000+sy).instance instanceof Coin) {
                    curPointTotal.decrease(((Coin) Sprite.getSpriteHashMap().get(sx*200000+sy).instance).getPts());
                }
                else if (Sprite.getSpriteHashMap().get(sx*200000+sy).instance instanceof Teleport) {
                    Sprite.delete(((Teleport) Sprite.getSpriteHashMap().get(sx*200000+sy).instance).getPartnerX(),
                            ((Teleport) Sprite.getSpriteHashMap().get(sx*200000+sy).instance).getPartnerY());
                }
                else if (Sprite.getSpriteHashMap().get(sx*200000+sy).instance instanceof Goal) {
                    numGoals--;
                }
                Sprite.delete(sx,sy);
            }
            if (countDownRect.contains(mouse)) {
                curCountdown = null;
            }
        }
        else if (curTool == TOOL) {
            if (gameRect.contains(mouse)) {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x + offX*75;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y + offY*75;
                int spriteKey = sx*200000+sy;
                if (Sprite.getSpriteHashMap().containsKey(spriteKey)) {
                    Object inst = Sprite.getSpriteHashMap().get(spriteKey).instance;
                    if (inst instanceof Enemy) {
                        changeEnemySettings = true;
                        curSetting = 0;
                        curEnemy = (Enemy)inst;
                        unsavedHealth = curEnemy.getHealth();
                        unsavedSpeed = curEnemy.getSpeed();
                        unsavedStationary = curEnemy.isStationary();
                        unsavedDirection = curEnemy.getDirection();
                        unsavedBulletSpeed = curEnemy.getBulletSpeed();
                    }
                    else if (inst instanceof Avatar) {
                        changeAvatarSettings = true;
                        curSetting = 0;
                        unsavedHealth = avatar.getHealth();
                        unsavedSpeed = avatar.getSpeed();
                        unsavedBulletSpeed = avatar.getBulletSpeed();
                    }
                    else if (inst instanceof Goal) {
                        curSetting = MASK;
                        changeGoalSettings = true;
                        curGoal = (Goal) inst;
                        unsavedMask = curGoal.getMask().getImage();
                        int unsavedPointstoOpen = curGoal.getPointsToOpen();
                        unsavedArrPoints = new int[]{unsavedPointstoOpen/10000,(unsavedPointstoOpen/1000)%10,(unsavedPointstoOpen/100)%10,(unsavedPointstoOpen/10)%10,(unsavedPointstoOpen)%10};
                    }
                    else if (inst instanceof Message) {
                        curSetting = TITLE;
                        curMessage = (Message) inst;
                        changeMessageSettings = true;
                        titleArea.setEditable(true);
                        titleArea.setVisible(true);
                        titleArea.setText(curMessage.getTitle());
                        contentArea.setText(curMessage.getContent());
                    }
                    else if (inst instanceof KeyHole) {
                        curSetting = UNLOCKREQUIREMENT;
                        curKeyHole = (KeyHole) inst;
                        changeKeyHoleSettings = true;
                        titleArea.setEditable(true);
                        titleArea.setVisible(true);
                        titleArea.setText(Integer.toString(curKeyHole.getUnlockRequirement()));
                    }
                    else if (inst instanceof Spike) {
                        curSetting = DAMAGE;
                        changeSpikeSettings = true;
                        curSpike = (Spike) inst;
                        unsavedDamage = curSpike.getDmg();
                    }
                    else if (inst instanceof Coin) {
                        curCoin = (Coin) inst;
                        if (curCoin.isEditable()) {
                            unsavedPts = curCoin.getPts();
                            curSetting = POINTS;
                            changeCoinSettings = true;
                        }
                        else {
                            curCoin = null;
                        }
                    }
                    else if (inst instanceof HealthBonus) {
                        curHealthBonus = (HealthBonus) inst;
                        curSetting = POINTS;
                        unsavedValue = curHealthBonus.getValue();
                        changeHealthBonusSettings = true;
                    }
                    else if (inst instanceof TimeBonus) {
                        curTimeBonus = (TimeBonus) inst;
                        curSetting = POINTS;
                        unsavedValue = curTimeBonus.getValue();
                        changeTimeBonusSettings = true;
                        titleArea.setVisible(true);
                        titleArea.setEditable(true);
                        titleArea.setText(Integer.toString(unsavedValue));
                    }
                    else if (inst instanceof KeyInsert) {
                        curKeyInsert = (KeyInsert) inst;
                        curSetting = POINTS;
                        unsavedValue = curKeyInsert.getValue();
                        changeKeyInsertSettings = true;
                        titleArea.setVisible(true);
                        titleArea.setEditable(true);
                        titleArea.setText(Integer.toString(unsavedValue));
                    }
                }
            }
            else if (countDownRect.contains(mouse) && curCountdown != null) {
                changeCountdownSettings = true;
                titleArea.setVisible(true);
                titleArea.setEditable(true);
                titleArea.setText(curCountdown.getStrTime());
                curSetting = 0;
            }
            else if (healthRect.contains(mouse) && curHealth != null) {
                changeHealthSettings = true;
                titleArea.setVisible(true);
                titleArea.setEditable(true);
                titleArea.setText(Integer.toString(curHealth.getValue()));
                curSetting = 0;
            }
        }
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

    public void paintChangeAvatar(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("WARNING",getTitlePosition("WARNING",g),175);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,45));
        g.setColor(BROWN);
        g.drawString("You have more than one avatar on the board, which is not allowed.",getTitlePosition("You have more than one avatar on the board, which is not allowed.",g),250);
        g.drawString("Press Y to use new avatar data, N to use old avatar data.",getTitlePosition("Press Y to use new avatar data, N to use old avatar data.",g),300);
    }

    private void paintAvatarSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(avatarSettings[curSetting],getTitlePosition(avatarSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < avatarSettingsRects.length; i++) {
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
            for (int i = 0; i < 10; i++) {
                //g.fillRect(oneToTenBlocks[i].x,oneToTenBlocks[i].y,oneToTenBlocks[i].width,oneToTenBlocks[i].height);
                g.drawString(String.format("%d",i+1),ctrPosition(oneToTenBlocks[i],String.format("%d",i+1),g),oneToTenBlocks[i].y+oneToTenBlocks[i].width*3/2);
            }
            g.fillRect(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2-5,oneToTenBlocks[9].x+oneToTenBlocks[9].width-oneToTenBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToTenBlocks[unsavedSpeed-1].x,oneToTenBlocks[unsavedSpeed-1].y,oneToTenBlocks[unsavedSpeed-1].width,oneToTenBlocks[unsavedSpeed-1].height);
            //g.drawLine(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2,oneToTenBlocks[9].x+oneToTenBlocks[9].width,oneToTenBlocks[9].y+oneToTenBlocks[9].width/2);
        }
        else if (curSetting == HEALTH) {
            g.setColor(Color.black);
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedHealth-1].x,oneToFiveBlocks[unsavedHealth-1].y,oneToFiveBlocks[unsavedHealth-1].width,oneToFiveBlocks[unsavedHealth-1].height);
        }
        else if (curSetting == BULLETSPEED) {
            g.setColor(Color.black);
            for (int i = 0; i < 10; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToTenBlocks[i],String.format("%d",i+1),g),oneToTenBlocks[i].y+oneToTenBlocks[i].width*3/2);
            }
            g.fillRect(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2-5,oneToTenBlocks[9].x+oneToTenBlocks[9].width-oneToTenBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToTenBlocks[unsavedBulletSpeed-1].x,oneToTenBlocks[unsavedBulletSpeed-1].y,oneToTenBlocks[unsavedBulletSpeed-1].width,oneToTenBlocks[unsavedBulletSpeed-1].height);
        }
    }

    public void paintEnemySettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(enemySettings[curSetting],getTitlePosition(enemySettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        int range = enemySettingsRects.length;
        for (int i = 0; i < enemySettingsRects.length; i++) {
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
                for (int i = 0; i < 10; i++) {
                    g.drawString(String.format("%d", i + 1), ctrPosition(oneToTenBlocks[i], String.format("%d", i + 1), g), oneToTenBlocks[i].y + oneToTenBlocks[i].width * 3 / 2);
                }
                g.fillRect(oneToTenBlocks[0].x, oneToTenBlocks[0].y + oneToTenBlocks[0].width / 2 - 5, oneToTenBlocks[9].x + oneToTenBlocks[9].width - oneToTenBlocks[0].x, 10);
                g.setColor(Color.YELLOW);
                g.fillOval(oneToTenBlocks[unsavedSpeed - 1].x, oneToTenBlocks[unsavedSpeed - 1].y, oneToTenBlocks[unsavedSpeed - 1].width, oneToTenBlocks[unsavedSpeed - 1].height);
            }
            else {
                g.drawString("Enemy is on stationary", getTitlePosition("Enemy is on stationary",g), 300);
            }
        }
        else if (curSetting == HEALTH) {
            g.setColor(Color.black);
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedHealth-1].x,oneToFiveBlocks[unsavedHealth-1].y,oneToFiveBlocks[unsavedHealth-1].width,oneToFiveBlocks[unsavedHealth-1].height);
        }
        else if (curSetting == BULLETSPEED) {
            g.setColor(Color.black);
            for (int i = 0; i < 10; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToTenBlocks[i],String.format("%d",i+1),g),oneToTenBlocks[i].y+oneToTenBlocks[i].width*3/2);
            }
            g.fillRect(oneToTenBlocks[0].x,oneToTenBlocks[0].y+oneToTenBlocks[0].width/2-5,oneToTenBlocks[9].x+oneToTenBlocks[9].width-oneToTenBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToTenBlocks[unsavedBulletSpeed-1].x,oneToTenBlocks[unsavedBulletSpeed-1].y,oneToTenBlocks[unsavedBulletSpeed-1].width,oneToTenBlocks[unsavedBulletSpeed-1].height);
        }
        else if (curSetting == STATIONARY) {
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
            g.setColor(Color.YELLOW);
            if (topDown) {
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

    public void paintGoalSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);

        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(goalSettings[curSetting],getTitlePosition(goalSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < goalSettingsRects.length; i++) {
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
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,60));
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",unsavedArrPoints[i]),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
        }
        if (curSetting == MASK) {
            g.setColor(Color.black);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,10));
            //g.drawRect(getTitlePosition("",g)-200,300,75,75);
            g.drawString("NONE",ctrPosition(new Rectangle(getTitlePosition("",g)-200,300,75,75),"NONE",g),300+40);
            Graphics2D g2D = (Graphics2D)g;
            for (int i = 1; i < goalMasks.length; i++) {
                g.drawImage(blockSprites[i-1],dialogCtr-200+100*(i%4),300+100*(i/4),null);
                if (unsavedMask == blockSprites[i-1]) {
                    g2D.setStroke(new BasicStroke(5));
                    g2D.drawRect(dialogCtr-200+100*(i%4),300+100*(i/4),75,75);
                    g2D.setStroke(new BasicStroke(1));
                }

            }
            if (unsavedMask == null) {
                g2D.setStroke(new BasicStroke(5));
                g2D.drawRect(getTitlePosition("",g)-200,300,75,75);
                g2D.setStroke(new BasicStroke(1));
            }

        }
    }
    public void paintMessageSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(messageSettings[curSetting],getTitlePosition(messageSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < messageSettingsRects.length; i++) {
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
            g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
        }
        //System.out.println(contentArea.getBounds());
    }

    public void paintKeyHoleSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(keyHoleSettings[curSetting],getTitlePosition(keyHoleSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }

    public void paintSpikeSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(spikeSettings[curSetting],getTitlePosition(spikeSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        for (int i = 0; i < oneToFiveBlocks.length; i++) {
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedDamage-1].x,oneToFiveBlocks[unsavedDamage-1].y,oneToFiveBlocks[unsavedDamage-1].width,oneToFiveBlocks[unsavedDamage-1].height);
    }

    public void paintCoinSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(coinSettings[curSetting],getTitlePosition(coinSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        for (int i = 0; i < oneToFiveBlocks.length; i++) {
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedPts-1].x,oneToFiveBlocks[unsavedPts-1].y,oneToFiveBlocks[unsavedPts-1].width,oneToFiveBlocks[unsavedPts-1].height);
    }
    public void paintHealthBonusSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(healthBonusSettings[curSetting],getTitlePosition(healthBonusSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.black);
        for (int i = 0; i < oneToFiveBlocks.length; i++) {
            g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
        }
        g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
        g.setColor(Color.YELLOW);
        g.fillOval(oneToFiveBlocks[unsavedValue-1].x,oneToFiveBlocks[unsavedValue-1].y,oneToFiveBlocks[unsavedValue-1].width,oneToFiveBlocks[unsavedValue-1].height);
    }

    public void paintTimeBonusSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(timeBonusSetings[curSetting],getTitlePosition(timeBonusSetings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
    }

    public void paintKeyInsertSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(keyInsertSettings[curSetting],getTitlePosition(keyInsertSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    public void paintCountdownSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(countdownSettings[curSetting],getTitlePosition(countdownSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.drawString("Enter in format ##:##:##",getTitlePosition("Enter in format ##:##:##",g),300);
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }
    public void paintHealthSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString("Health",getTitlePosition("Health",g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
    }

    public void paintLevelSettings(Graphics g) {
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(levelSettings[curSetting],getTitlePosition(levelSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.setColor(Color.BLUE);

        for (int i = 0; i < levelSettingsRects.length; i++) {
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
            g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());
        }
        else if (curSetting == BACKGROUND) {
            Graphics2D g2D = (Graphics2D)g;
            for (int i = 0; i < backgrounds.length; i++) {
                g.drawImage(miniBackgrounds[i],350+250*(i%4),350+170*(i/4),null);
                if (unsavedBackground == backgrounds[i]) {
                    g2D.setStroke(new BasicStroke(10));
                    g.drawRect(miniBackgroundsRects[i].x,miniBackgroundsRects[i].y,miniBackgroundsRects[i].width,miniBackgroundsRects[i].height);
                    g2D.setStroke(new BasicStroke(1));
                }
            }

            g2D.setStroke(new BasicStroke(5));
            g2D.drawRect(miniBackgroundsRects[backgrounds.length].x,miniBackgroundsRects[backgrounds.length].y,miniBackgroundsRects[backgrounds.length].width,miniBackgroundsRects[backgrounds.length].height);
            g2D.setStroke(new BasicStroke(1));
            if (unsavedBackground == null) {
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
            if (topDown) {
                g.fillRect(rect1.x,rect1.y,rect1.width,rect1.height);
            }
            else {
                g.fillRect(rect2.x,rect2.y,rect2.width,rect2.height);
            }

            g.setColor(Color.RED);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            g.drawString("TOP-DOWN",ctrPosition(rect1,"TOP-DOWN",g),rect1.y+100);
            g.drawString("PLATFORM",ctrPosition(rect2,"PLATFORM",g),rect1.y+100);



            g2D.setStroke(new BasicStroke(10));
            g2D.setColor(Color.BLACK);
            g2D.drawRect(rect1.x,rect1.y,rect1.width,rect1.height);
            g2D.drawRect(rect2.x,rect2.y,rect2.width,rect2.height);
            g2D.setStroke(new BasicStroke(1));
        }
    }


    public void paintComponent(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        if (levelSettingsRects == null) {
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));

            settingsRect = new Rectangle(gameRect.x/2-g.getFontMetrics().stringWidth("LEVEL SETTINGS")/2-10,900,g.getFontMetrics().stringWidth("LEVEL SETTINGS")+20,50);

            levelSettingsRects = new Rectangle[levelSettings.length];
            int paintX = 250;
            for (int i = 0; i < levelSettingsRects.length; i++) {
                levelSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(levelSettings[i])+20,50);
                paintX += levelSettingsRects[i].width + 20;
            }


            avatarSettingsRects = new Rectangle[avatarSettings.length];
            paintX = 250;
            for (int i = 0; i < avatarSettingsRects.length; i++) {
                avatarSettingsRects[i] = new Rectangle(paintX,250, g.getFontMetrics().stringWidth(avatarSettings[i])+20,50);
                //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[i]));
                paintX += avatarSettingsRects[i].width + 10;
            }

            enemySettingsRects = new Rectangle[enemySettings.length];
            paintX = 250;
            for (int i = 0; i < enemySettingsRects.length; i++) {
                enemySettingsRects[i] = new Rectangle(paintX,250, g.getFontMetrics().stringWidth(enemySettings[i])+20,50);
                //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[i]));
                paintX += enemySettingsRects[i].width + 10;
            }


            goalSettingsRects = new Rectangle[2];
            paintX = 250;
            for (int i = 0; i < goalSettingsRects.length; i++) {
                goalSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(goalSettings[i])+20,50);
                paintX += goalSettingsRects[i].width + 10;
            }

            dialogCtr = getTitlePosition("",g);

            messageSettingsRects = new Rectangle[2];
            paintX = 250;
            for (int i = 0; i < messageSettingsRects.length; i++) {
                messageSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(messageSettings[i])+20,50);
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
        //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[0]));
        if (readyToDelete) mainFrame.setCursor(Cursor.CROSSHAIR_CURSOR);
        else mainFrame.setCursor(Cursor.getDefaultCursor());
        g.drawImage(coolBack,0,0,null);
        g.setFont(font);
        g.setColor(BROWN);
        g.fillRect(spritesRect.x,spritesRect.y,spritesRect.width,spritesRect.height);
        g.setColor(Color.black);
        for (int lx = 40+75; lx < 300; lx += 75) g.drawLine(lx,300,lx,600);
        for (int ly = 300+75; ly < 600; ly+= 75) g.drawLine(40,ly,340,ly);

        g.setColor(Color.green);
        g.fillRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
        g.fillRect(enemyRect.x,enemyRect.y,enemyRect.width,enemyRect.height);
        g.fillRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
        g.fillRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
        g.fillRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);

        g.setColor(Color.lightGray);
        int cnt = 0;
        if (curType == AVATAR) {
            g.fillRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
            if (topDown) {
                for (int i = 0; i < playerTopDownSprites.length; i++) {
                    g.drawImage(playerTopDownSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
            else {
                for (int i = 0; i < playerPlatSprites.length; i++) {
                    g.drawImage(playerPlatSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
        }
        else if (curType == ENEMY) {
            g.fillRect(enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
            if (topDown) {
                for (int i = 0; i < enemyTopDownSprites.length; i++) {
                    g.drawImage(enemyTopDownSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
            else {
                for (int i = 0; i < enemyPlatSprites.length; i++) {
                    g.drawImage(enemyPlatSprites[i], 40 + 75 * (cnt % 4), 300 + 75 * (cnt / 4), null);
                    ++cnt;
                }
            }
        }
        else if (curType == BLOCK) {
            g.fillRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
            for (int i = 0; i < blockSprites.length; i++) {
                g.drawImage(blockSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        else if (curType == ITEM) {
            g.fillRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
            for (int i = 0; i < itemSprites.length; i++) {
                g.drawImage(itemSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        else if (curType == SYSTEM) {
            g.fillRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);
            for (int i = 0; i < systemSprites.length; i++) {
                g.drawImage(systemSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        //g.drawImage(maroonTexts[curType],40+90*curType,250,null);
        if (backgroundColor != null){
            g.setColor(backgroundColor);
            g.fillRect(gameRect.x,gameRect.y,gameRect.width,gameRect.height);
        }
        else g.drawImage(levelBackground,gameRect.x,gameRect.y,null);
        if (settingsRect.contains(mouse)) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillRect(settingsRect.x,settingsRect.y,settingsRect.width,settingsRect.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.drawString("LEVEL SETTINGS",settingsRect.x+10,settingsRect.y+35);
        g.setFont(font);
        g.setColor(Color.blue);
        for (int lx = 575; lx < 1700; lx += 75) g.drawLine(lx,150,lx,900);
        for (int ly = 225; ly < 950; ly += 75) g.drawLine(500,ly,1700,ly);

        g.drawImage(title,500,20,null);
        g.setColor(new Color(215,47,198));
        g.drawString("Avatar",avatarRect.x+10,avatarRect.y+20);
        g.drawString("Enemy",enemyRect.x+7,enemyRect.y+20);
        g.drawString("Block",blockRect.x+10,blockRect.y+20);
        g.drawString("Items",itemRect.x+10,itemRect.y+20);
        g.drawString("System",systemRect.x+5,systemRect.y+20);
        g.setColor(Color.black);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setStroke(new BasicStroke(3));
        g2D.drawRect(avatarRect.x,avatarRect.y,avatarRect.width,avatarRect.height);
        g2D.drawRect(enemyRect.x,enemyRect.y,enemyRect.width,enemyRect.height);
        g2D.drawRect(blockRect.x,blockRect.y,blockRect.width,blockRect.height);
        g2D.drawRect(itemRect.x,itemRect.y,itemRect.width,itemRect.height);
        g2D.drawRect(systemRect.x,systemRect.y,systemRect.width,systemRect.height);
        g2D.setStroke(new BasicStroke(1));

        for (int i = 0; i < 3; i++) {
            if (curTool == i) {
                g.drawImage(toolImages[i],1750,300+125*i,null);
            }
            else {
                g.drawImage(toolClickedImages[i],1750,300+125*i,null);
            }
        }
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        if (readyToPaste) {
            if (insertTeleport) {
                g.drawImage(teleportOut,mouse.x-75,mouse.y,null);
            }
            else if (curImage != null) {
                g.drawImage(curImage.getImage(), mouse.x - 75, mouse.y, null);
            }
        }
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
            g.fillRect(((mouse.x - gameRect.x) / 75) * 75 + gameRect.x, ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y,75, 75);
        }
        g.setColor(Color.YELLOW);
        Iterator<Map.Entry<Integer, Sprite>> it = Sprite.getSpriteHashMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Sprite> pair = it.next();
            int drawX = pair.getKey()/200000, drawY = pair.getKey()%200000;
            //System.out.printf("%d %d \n",drawX,drawY);
            if (offX*75 + gameRect.x <= drawX && (offX + 16)*75 + gameRect.x >= drawX && offY*75 + gameRect.y <= drawY && (offY + 10)*75 + gameRect.y >= drawY) {
                g.drawImage(pair.getValue().getImg().getImage(), drawX - (offX*75), drawY - (offY*75), null);
                g.drawRect(pair.getValue().hitBox.x,pair.getValue().hitBox.y,pair.getValue().hitBox.width,pair.getValue().hitBox.height);
            }
        }
        if (changeAvatarPrompt || changeAvatarSettings || changeEnemySettings || changeGoalSettings ||
                changeMessageSettings || changeKeyHoleSettings || changeSpikeSettings || changeCoinSettings ||
                changeHealthBonusSettings || changeTimeBonusSettings || changeKeyInsertSettings ||
                changeCountdownSettings || changeHealthSettings || changeLevelSettings) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,1920,1080);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,60));
            g.drawImage(promptBack,100,100,null);
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
            g.fillRect(yesRect.x,yesRect.y,yesRect.width,yesRect.height);
            g.fillRect(noRect.x,noRect.y,noRect.width,noRect.height);
            g.drawImage(buttonImage,yesRect.x,yesRect.y,null);
            g.drawImage(buttonImage,noRect.x,noRect.y,null);
            if (yesRect.contains(mouse)) g.drawImage(buttonClickedImage,yesRect.x,yesRect.y,null);
            else g.drawImage(buttonImage,yesRect.x,yesRect.y,null);
            if (noRect.contains(mouse)) g.drawImage(buttonClickedImage,noRect.x,noRect.y,null);
            else g.drawImage(buttonImage,noRect.x,noRect.y,null);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            g.drawString("SAVE",ctrPosition(yesRect,"SAVE",g),yesRect.y+70);
            g.drawString("CANCEL",ctrPosition(noRect,"CANCEL",g),noRect.y+70);
        }
        else {
            g.drawImage(left,leftRect.x,leftRect.y,null);
            g.drawImage(right,rightRect.x,rightRect.y,null);
            g.drawImage(up,upRect.x,upRect.y,null);
            g.drawImage(down,downRect.x,downRect.y,null);

            if (curCountdown != null) {
                g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
                if (countDownRect.contains(mouse)) {
                    g.setColor(TRANSPARENTGREEN);
                    g.fillRect(countDownRect.x,countDownRect.y,countDownRect.width,countDownRect.height);
                }
                if (healthRect.contains(mouse)) {
                    g.setColor(TRANSPARENTGREEN);
                    g.fillRect(healthRect.x,healthRect.y,healthRect.width,healthRect.height);
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
                g.drawString("0",ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y,pointTotalRect.width-80,37),"0",g),pointTotalRect.y+30);
                g.setColor(Color.WHITE);
                g.drawString(String.format("%d",curPointTotal.getTotal()),ctrPosition(new Rectangle(pointTotalRect.x+80,pointTotalRect.y+37,pointTotalRect.width-80,38),String.format("%d",curPointTotal.getTotal()),g),pointTotalRect.y+pointTotalRect.height+15);

            }
            if (curHealth != null) {
                g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
                g.drawImage(systemSprites[2],healthRect.x,healthRect.y,null);
                g.setColor(Color.YELLOW);
                g.drawLine(healthRect.x+80,healthRect.y+37,healthRect.x+healthRect.width,healthRect.y+37);
                g.setColor(Color.BLUE);
                g.drawString("0",ctrPosition(new Rectangle(healthRect.x+80,healthRect.y,healthRect.width-80,37),"0",g),healthRect.y+30);
                g.setColor(Color.WHITE);
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

        g.drawString(String.format("%d",Sprite.getSpriteHashMap().size()),50,50);
        if (curImage != null) {
            g.drawImage(curImage.getImage(),0,0,null);
            g.drawImage(blockSprites[0],50,0,null);
        }
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        g.drawString(String.format("%d %d",mouse.x,mouse.y),50,50);
        g.drawString(String.format("%d %d",offX,offY),50,100);




    }
    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e) {
        try {
            update();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        //
        //System.out.printf("%d %d\n",e.getX(),e.getY());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        keys[e.getKeyCode()] = true;
        if (changeAvatarPrompt || changeMessageSettings || changeKeyHoleSettings) {
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
    public int getTitlePosition(String str,Graphics g) {
        return ctrPosition(new Rectangle(0,0,1920,1080),str,g);
    }
    public int ctrPosition(Rectangle bounds, String str, Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        int width = g.getFontMetrics().stringWidth(str);
        int ctrX = bounds.x+(bounds.width-width)/2;
        return ctrX;
    }
    public static int find(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(val)) return i;
        }
        return -1;
    }
}