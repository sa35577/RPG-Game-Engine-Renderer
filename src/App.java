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
    public App() throws IOException, ClassNotFoundException {
        super("Gamestar Mechanic");
        myTimer = new Timer(10, new TickListener());	 // trigger every 10 ms
        //edit = new EditPanel(this);
        //level = new LevelPanel(this);
        select = new SelectPanel(this);
        panelManager = new JPanel(new CardLayout());

        // Setting up the CardLayout in panelManager
        //panelManager.add(edit, EDITPANEL);
        //panelManager.add(level, LEVELPANEL);
        panelManager.add(select, SELECTPANEL);
        switchPanel(SELECTPANEL);
        setResizable(false);
        setLocationRelativeTo(null);
        add(panelManager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = ImageIO.read(new File("richman.jpeg"));
        setIconImage(icon);
        setVisible(true);
        // Starting a timer to update the frames


    }
    public void switchPanel(String targetPanel) throws IOException, ClassNotFoundException {
        Component[] cpts = panelManager.getComponents();
        boolean containsLevel = false, containsEdit = false;
        for (Component cpt : cpts) {
            if (cpt instanceof LevelPanel) containsLevel = true;
            else if (cpt instanceof EditPanel) containsEdit = true;
        }
        if (targetPanel.equals("edit")) {
            setSize(1920,1080);
            if (containsEdit) {
                panelManager.remove(edit);
            }
            edit = new EditPanel(this,select.getFile());
            panelManager.add(edit,EDITPANEL);
        }
        else if (targetPanel.equals("level")) {
            setSize(1200,900);
            if (containsLevel) {
                panelManager.remove(level);
            }
            level = new LevelPanel(this,select.getFile());
            panelManager.add(level,LEVELPANEL);
        }
        else {
            setSize(1200, 900);
        }
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
                    try {
                        level.update();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    level.repaint();
                    break;
                case SELECTPANEL:
                    select.repaint();
                    break;
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.setProperty("sun.java2d.opengl", "True");
        App game = new App();
    }
    public void start() {
        myTimer.start();

    }
}
