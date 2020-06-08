import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class App extends JFrame {
    // Declaring constants
    public static final String EDITPANEL = "edit";
    public static final String LEVELPANEL = "level";
    public static final String SELECTPANEL = "select";
    // Declaring fields
    private EditPanel edit;
    private LevelPanel level;
    private SelectPanel select;
    private JPanel panelManager;
    private String activePanel;
    private Timer myTimer; // Timer to call the game functions each frame
    private int runTime; // Variable to keep track of the milliseconds that have passed since the start of the game
    public App() throws IOException {
        super("Gamestar Mechanic");
        edit = new EditPanel(this);
        level = new LevelPanel(this);
        select = new SelectPanel(this);
        panelManager = new JPanel(new CardLayout());
        // Setting up the CardLayout in panelManager
        panelManager.add(edit, EDITPANEL);
        panelManager.add(level, LEVELPANEL);
        panelManager.add(select, SELECTPANEL);
        switchPanel(EDITPANEL);
        setSize(1920,1080);
        setResizable(false);
        setLocationRelativeTo(null);
        add(panelManager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = ImageIO.read(new File("richman.jpeg"));
        setIconImage(icon);
        setVisible(true);
        // Starting a timer to update the frames
        myTimer = new Timer(10, new TickListener());	 // trigger every 10 ms
        myTimer.start();

    }
    public void switchPanel(String targetPanel){
        CardLayout cardLayout = (CardLayout) panelManager.getLayout();
        cardLayout.show(panelManager, targetPanel);
        activePanel = targetPanel;
        addNotify(); // Getting the focus of the current panel
    }
    // TickListener Class
    class TickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt){
            switch(activePanel){
                case EDITPANEL:
                    edit.repaint();
                    break;
                case LEVELPANEL:
                    level.update();
                    level.repaint();
                    break;
                case SELECTPANEL:
                    break;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "True");
        App game = new App();
    }
}
