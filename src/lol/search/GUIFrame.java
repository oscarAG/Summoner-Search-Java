
package lol.search;

import javax.swing.JFrame;

/**
 * This class sets the defaults of the main frame that will hold the GUI contents of the application. 
 * @author Oscar
 */
public class GUIFrame {
    private final String version = "1.1.0";
    private final String applicationName = "League of Legends Player Search " + version; //title bar name of the application
    private final JFrame frame; //main frame of the application
    
    public GUIFrame(){
        frame = new JFrame(applicationName);
        FrameDefaults(frame); //size, close operations, etc.
        MainPage main = new MainPage(frame); //mainpage
        
        frame.setVisible(true);
    }
    
    
    private void FrameDefaults(JFrame frame){
        frame.setSize(1215, 717); //size
        frame.setResizable(false); //do not let the user resize the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); //position application in the middle of the monitor
    }
    
}
