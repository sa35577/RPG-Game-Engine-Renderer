import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Map;

public class EditPanel extends JPanel implements MouseListener, KeyListener {
    private Edit mainFrame;
    private Image coolBack;
    private Image[] systemSprites,itemSprites,blockSprites,playerPlatSprites,enemyPlatSprites,playerTopDownSprites,enemyTopDownSprites;
    private String[] systemStrings,itemStrings,blockStrings,playerPlatStrings,enemyPlatStrings,playerTopDownStrings,enemyTopDownStrings;
    private Rectangle avatarRect,enemyRect,blockRect,itemRect,systemRect,spritesRect;
    private Rectangle gameRect;
    private Image title;
    private boolean topDown;
    public static final int AVATAR = 0, ENEMY = 1, BLOCK = 2, ITEM = 3, SYSTEM = 4;
    private int curType;
    private Image[] maroonTexts = new Image[5];
    private Image[] blueTexts = new Image[5];
    private Font font;
    public static final Color BROWN = new Color(210,105,30),TRANSPARENTRED = new Color(255,0,0,100),TRANSPARENTGREEN = new Color(0,255,0,100),TRANSPARENTBLUE = new Color(0,0,255,100) ;
    private Point mouse;
    public static final int MOUSE = 0, TOOL = 1, DELETE = 2;
    public int curTool = MOUSE;
    private Image[] toolImages;
    private Image[] toolClickedImages;
    private String[] toolStrings;
    private Ellipse2D.Double[] toolEllipses;
    private String curSprite;
    private Image curImage;
    private boolean readyToPaste,readyToDelete,readyToModify;
    private boolean containsAvatar,isAvatar;
    private boolean changeAvatarPrompt;
    private int avatarX,avatarY,newavatarX,newavatarY;
    private boolean[] keys;
    String s = "";
    private Image promptBack,buttonImage,buttonClickedImage;
    private boolean overYes,overNo;
    private Rectangle yesRect,noRect;
    private Avatar avatar;
    private Enemy curEnemy;
    private Goal curGoal;
    private boolean changeAvatarSettings, changeEnemySettings,changeGoalSettings,changeMessageSettings,changeKeyHoleSettings;
    public static final int SPEED = 0, HEALTH = 1;
    public static final String[] avatarSettings = new String[]{"Speed","Health"}, goalSettings = new String[]{"Mask","Unlock"}, goalMasks = new String[]{"None","Cement","Cloud","Dirt","Glass","Gold","Grass"}, messageSettings = new String[]{"Title","Content"}, keyHoleSettings = new String[]{"Unlock Requirement"};
    public static final int MASK = 0, UNLOCK = 1;
    private Rectangle[] avatarSettingsRects,goalSettingsRects,messageSettingsRects,keyHoleSettingsRects;
    private int curSetting;
    private Rectangle[] oneToTenBlocks,oneToFiveBlocks;
    private int unsavedSpeed,unsavedHealth;
    private Image unsavedMask;
    private int[] unsavedArrPoints;
    private int unsavedUnlockRequirement;
    private int dialogCtr;
    public static final int TITLE = 0, CONTENT = 1;
    private JTextArea titleArea,contentArea;
    private JScrollPane contentPane;
    private Message curMessage;
    public static final int UNLOCKREQUIREMENT = 0;
    private KeyHole curKeyHole;

    public EditPanel(Edit e) {
        this.setLayout(null);
        mainFrame = e;
        addMouseListener(this);
        addKeyListener(this);
        topDown = true;
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

        itemStrings = new String[]{"point","bonuspoint","healthpack","timerbonus","redkey","greenkey"};
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
        overYes = false;
        overNo = false;
        avatar = null;
        curEnemy = null;
        curGoal = null;
        changeAvatarSettings = false;
        changeEnemySettings = false;
        changeGoalSettings = false;
        changeMessageSettings = false;
        changeKeyHoleSettings = false;
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
        titleArea.addKeyListener(this);;
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



    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void update() {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
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
        if (changeAvatarSettings || changeEnemySettings) {
            for (int i = 0; i < avatarSettingsRects.length; i++) {
                if (avatarSettingsRects[i].contains(mouse)) {
                    if (i == 0) curSetting = SPEED;
                    else curSetting = HEALTH;
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
            else {
                if (Math.abs(mouse.y - (oneToFiveBlocks[0].y + oneToFiveBlocks[0].height/2)) < 40) {
                    for (int i = 0; i < 5; i++) {
                        if (Math.abs(mouse.x-(oneToFiveBlocks[i].x+oneToFiveBlocks[i].width/2)) < 50) {
                            unsavedHealth = i+1;
                            break;

                        }
                    }
                }
            }

            if (yesRect.contains(mouse)) {
                if (changeAvatarSettings) {
                    avatar.setHealth(unsavedHealth);
                    avatar.setSpeed(unsavedSpeed);
                }
                else if (changeEnemySettings) {
                    curEnemy.setHealth(unsavedHealth);
                    curEnemy.setSpeed(unsavedSpeed);
                }
                changeAvatarSettings = false;
                changeEnemySettings = false;
                readyToModify = true;
            }
            else if (noRect.contains(mouse)) {
                changeAvatarSettings = false;
                changeEnemySettings = false;
                curEnemy = null;
                readyToModify = true;
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
                    System.out.println(goalMasks.length);
                    if (new Rectangle(dialogCtr-200+100*(i%4),300+100*(i/4),75,75).contains(mouse)) {
                        if (i == 0) unsavedMask = null;
                        else unsavedMask = blockSprites[i-1];
                    }
                }
            }
            if (yesRect.contains(mouse)) {
                curGoal.setMask(unsavedMask);
                curGoal.setPointsToOpen(unsavedArrPoints[0]*10000+unsavedArrPoints[1]*1000+unsavedArrPoints[2]*100+unsavedArrPoints[3]*10+unsavedArrPoints[4]);
                changeGoalSettings = false;
                readyToModify = true;
            }
            else if (noRect.contains(mouse)) {
                changeGoalSettings = false;
                readyToModify = true;
            }
            return;
        }
        if (changeMessageSettings) {
            for (int i = 0; i < messageSettingsRects.length; i++) {
                if (messageSettingsRects[i].contains(mouse)) {
                    curSetting = i;
                    if (i == 0) {
                        contentArea.setVisible(false);
                        contentArea.setEditable(false);
                        contentPane.setVisible(false);
                        titleArea.setVisible(true);
                        titleArea.setEditable(true);
                    }
                    else if (i == 1) {
                        titleArea.setVisible(false);
                        titleArea.setEditable(false);
                        contentArea.setVisible(true);
                        contentArea.setEditable(true);
                        contentPane.setVisible(true);
                    }
                }
            }
            if (curSetting == TITLE) {
                titleArea.setText(titleArea.getText().replaceAll("\n",""));
                if (titleArea.getText().length() > 45) titleArea.setText(titleArea.getText().substring(0,45));
            }

            if (yesRect.contains(mouse)) {
                changeMessageSettings = false;
                titleArea.setVisible(false);
                titleArea.setEditable(false);
                contentArea.setVisible(false);
                contentArea.setEditable(false);
                contentPane.setVisible(false);
                curMessage.setTitle(titleArea.getText());
                curMessage.setContent(contentArea.getText());
                readyToModify = true;
            }
            else if (noRect.contains(mouse)) {
                changeMessageSettings = false;
                titleArea.setVisible(false);
                titleArea.setEditable(false);
                contentArea.setVisible(false);
                contentArea.setEditable(false);
                contentPane.setVisible(false);
                readyToModify = true;
            }
            return;
        }
        if (changeKeyHoleSettings) {
            if (yesRect.contains(mouse)) {
                try {
                    curKeyHole.setUnlockRequirement(Integer.parseInt(titleArea.getText()));
                    changeKeyHoleSettings = false;
                    titleArea.setVisible(false);
                    titleArea.setEditable(false);
                    readyToModify = true;
                }
                catch (NumberFormatException e) {}
            }
            if (noRect.contains(mouse)) {
                changeKeyHoleSettings = false;
                titleArea.setVisible(false);
                titleArea.setEditable(false);
                readyToModify = true;

            }
            return;
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
                    if (playerTopDownSprites.length + playerPlatSprites.length > ind) {
                        if (playerTopDownSprites.length <= ind) {
                            ind -= playerTopDownSprites.length;
                            curSprite = playerPlatStrings[ind];
                            curImage = playerPlatSprites[ind];
                        }
                        else {
                            curSprite = playerTopDownStrings[ind];
                            curImage = playerTopDownSprites[ind];
                        }
                        isAvatar = true;
                    }
                } else if (curType == ENEMY) {
                    if (enemyTopDownSprites.length + enemyPlatSprites.length > ind) {
                        if (enemyTopDownSprites.length <= ind) {
                            ind -= enemyTopDownSprites.length;
                            curSprite = enemyPlatStrings[ind];
                            curImage = enemyPlatSprites[ind];
                        }
                        else {
                            curSprite = enemyTopDownStrings[ind];
                            curImage = enemyTopDownSprites[ind];
                        }
                        isAvatar = false;
                    }
                } else if (curType == BLOCK) {
                    //System.out.println(ind);
                    if (ind < blockSprites.length) {
                        curSprite = blockStrings[ind];
                        //System.out.println(curSprite);
                        curImage = blockSprites[ind];
                        isAvatar = false;
                    }
                } else if (curType == ITEM) {
                    if (itemSprites.length > ind) {
                        curSprite = itemStrings[ind];
                        curImage = itemSprites[ind];
                        isAvatar = false;
                    }
                } else if (curType == SYSTEM) {
                    if (systemSprites.length > ind) {
                        curSprite = systemStrings[ind];
                        curImage = systemSprites[ind];
                        isAvatar = false;
                    }
                }
            }
            if ((spritesRect.contains(mouse) || gameRect.contains(mouse)) && curSprite != null) readyToPaste = true;
            else readyToPaste = false;

            if (readyToPaste && gameRect.contains(mouse)) {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y;
                if (!isAvatar || !containsAvatar) {
                    new Sprite(curSprite, sx, sy, curImage);
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
                        else if (idx < 7) new Goal(curSprite,sx,sy,curImage,null).init();
                        else if (idx < 8) new Goal(curSprite,sx,sy,curImage,blockSprites[0]).init();
                        else if (idx < 9) new Message(curSprite,sx,sy,curImage).init();
                        else if (idx < 11) new KeyHole(curSprite,sx,sy,curImage,idx-9).init();
                    }
                    else if (curType == ITEM) {
                        Item item = new Item(curSprite,sx,sy,curImage);
                        item.init();
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
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y;
                Sprite.delete(sx,sy);

            }
        }
        else if (curTool == TOOL) {
            if (gameRect.contains(mouse)) {
                int sx = ((mouse.x - gameRect.x) / 75) * 75 + gameRect.x;
                int sy = ((mouse.y - gameRect.y) / 75) * 75 + gameRect.y;
                int spriteKey = sx*200000+sy;
                if (Sprite.getSpriteHashMap().containsKey(spriteKey)) {
                    Object inst = Sprite.getSpriteHashMap().get(spriteKey).instance;
                    if (inst instanceof Enemy) {
                        changeEnemySettings = true;
                        curSetting = 0;
                        unsavedHealth = ((Enemy)inst).getHealth();
                        unsavedSpeed = ((Enemy)inst).getSpeed();
                        curEnemy = (Enemy)inst;
                    }
                    else if (inst instanceof Avatar) {
                        changeAvatarSettings = true;
                        curSetting = 0;
                        unsavedHealth = avatar.getHealth();
                        unsavedSpeed = avatar.getSpeed();
                    }
                    else if (inst instanceof Goal) {
                        curSetting = MASK;
                        changeGoalSettings = true;
                        unsavedMask = ((Goal) inst).getMask();
                        int unsavedPointstoOpen = ((Goal) inst).getPointsToOpen();
                        unsavedArrPoints = new int[]{unsavedPointstoOpen/10000,(unsavedPointstoOpen/1000)%10,(unsavedPointstoOpen/100)%10,(unsavedPointstoOpen/10)%10,(unsavedPointstoOpen)%10};
                        curGoal = (Goal)inst;
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
                }
            }
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
        else {
            g.setColor(Color.black);
            for (int i = 0; i < oneToFiveBlocks.length; i++) {
                g.drawString(String.format("%d",i+1),ctrPosition(oneToFiveBlocks[i],String.format("%d",i+1),g),oneToFiveBlocks[i].y+oneToFiveBlocks[i].width*3/2);
            }
            g.fillRect(oneToFiveBlocks[0].x,oneToFiveBlocks[0].y+oneToFiveBlocks[0].width/2-5,oneToFiveBlocks[4].x+oneToFiveBlocks[4].width-oneToFiveBlocks[0].x,10);
            g.setColor(Color.YELLOW);
            g.fillOval(oneToFiveBlocks[unsavedHealth-1].x,oneToFiveBlocks[unsavedHealth-1].y,oneToFiveBlocks[unsavedHealth-1].width,oneToFiveBlocks[unsavedHealth-1].height);
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
        System.out.println(contentArea.getBounds());
    }

    public void paintKeyHoleSettings(Graphics g) {
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        g.drawString("SETTINGS",getTitlePosition("SETTINGS",g),175);
        g.setColor(Color.BLUE);
        g.drawString(keyHoleSettings[curSetting],getTitlePosition(keyHoleSettings[curSetting],g),250);
        g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
        for (int i = 0; i < keyHoleSettingsRects.length; i++) {
            if (curSetting == i || keyHoleSettingsRects[i].contains(mouse))
                g.setColor(Color.RED);
            else
                g.setColor(Color.ORANGE);
            g.fillRect(keyHoleSettingsRects[i].x,keyHoleSettingsRects[i].y,keyHoleSettingsRects[i].width,keyHoleSettingsRects[i].height);
            g.setColor(Color.blue);
            g.drawString(keyHoleSettings[i],keyHoleSettingsRects[i].x+10,keyHoleSettingsRects[i].y+30);
        }
        g.setColor(Color.BLUE);
        g.drawRect(titleArea.getX(),titleArea.getY(),titleArea.getWidth(),titleArea.getHeight());


    }



    public void paintComponent(Graphics g) {

        if (avatarSettingsRects == null) {
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.TRUETYPE_FONT,30));
            avatarSettingsRects = new Rectangle[2];
            int paintX = 250;
            for (int i = 0; i < avatarSettingsRects.length; i++) {
                avatarSettingsRects[i] = new Rectangle(paintX,250, g.getFontMetrics().stringWidth(avatarSettings[i])+20,50);
                //System.out.println(g.getFontMetrics().stringWidth(avatarSettings[i]));
                paintX += avatarSettingsRects[i].width + 10;
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

            keyHoleSettingsRects = new Rectangle[1];
            paintX = 250;
            for (int i = 0; i < keyHoleSettingsRects.length; i++) {
                keyHoleSettingsRects[i] = new Rectangle(paintX,250,g.getFontMetrics().stringWidth(keyHoleSettings[i])+20,50);
                paintX += keyHoleSettingsRects[i].width + 10;
            }

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
            for (int i = 0; i < playerTopDownSprites.length; i++) {
                g.drawImage(playerTopDownSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
            for (int i = 0; i < playerPlatSprites.length; i++) {
                g.drawImage(playerPlatSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
        }
        else if (curType == ENEMY) {
            g.fillRect(enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
            for (int i = 0; i < enemyTopDownSprites.length; i++) {
                g.drawImage(enemyTopDownSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
            }
            for (int i = 0; i < enemyPlatSprites.length; i++) {
                g.drawImage(enemyPlatSprites[i],40+75*(cnt%4),300+75*(cnt/4),null);
                ++cnt;
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
        g.setColor(Color.BLACK);
        g.fillRect(gameRect.x,gameRect.y,gameRect.width,gameRect.height);

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
                g.drawImage(toolClickedImages[i],1750,300+125*i,null);
            }
            else {
                g.drawImage(toolImages[i],1750,300+125*i,null);
            }
        }
        mouse = getMousePosition();
        if (mouse == null) mouse = new Point(0,0);
        if (readyToPaste) {
            g.drawImage(curImage, mouse.x - 75, mouse.y, null);
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
        Iterator<Map.Entry<Integer, Sprite>> it = Sprite.getSpriteHashMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Sprite> pair = it.next();
            g.drawImage(pair.getValue().getImg(),pair.getKey()/200000,pair.getKey()%200000,null);
            if (pair.getValue().instance instanceof Enemy){
                g.drawString("1",50,100);
            }
        }
        if (changeAvatarPrompt || changeAvatarSettings || changeEnemySettings || changeGoalSettings || changeMessageSettings || changeKeyHoleSettings) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,1920,1080);
            g.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,60));
            g.drawImage(promptBack,100,100,null);
            if (changeAvatarPrompt) paintChangeAvatar(g);
            else if (changeAvatarSettings || changeEnemySettings) paintAvatarSettings(g);
            else if (changeGoalSettings) paintGoalSettings(g);
            else if (changeMessageSettings) paintMessageSettings(g);
            else if (changeKeyHoleSettings) paintKeyHoleSettings(g);
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

        g.setFont(font);
        g.setColor(Color.black);
        g.setFont(font);

        g.drawString(String.format("%d",Sprite.getSpriteHashMap().size()),50,50);
        if (curImage != null) {
            g.drawImage(curImage,0,0,null);
            g.drawImage(blockSprites[0],50,0,null);
        }

    }
    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        update();
        //
        // System.out.printf("%d %d\n",e.getX(),e.getY());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        keys[e.getKeyCode()] = true;
        if (changeAvatarPrompt || changeMessageSettings || changeKeyHoleSettings) update();

    }

    @Override
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
