/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.search;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This page is what the user will see when he/she starts the application. 
 * @author Oscar
 */
public class MainPage {
    
    private JLabel masterLabel; //holds the background image and masterPanel
    private JPanel masterPanel; //holds swing components of the frame
    
    public MainPage(JFrame mainFrame){ //arg constructor
        loadFont();
        initializeMasterLabel(mainFrame);
        initializeMasterPanel(this.masterLabel);
        
        mainFrame.revalidate();
    }
    
    /*Set background of the frame and prepare for the main panel*/
    private void initializeMasterLabel(JFrame frame){
        System.out.println("METHOD - MainPage/initializeMasterLabel");
        //GameStaticData class object
        GameStaticData objGameStaticData = new GameStaticData();
        //background label
        this.masterLabel = new JLabel(objGameStaticData.getBackgroundImageIcon()); //use method from obj to get a background
        this.masterLabel.setLayout(new FlowLayout());
        frame.add(this.masterLabel);
    }
    
    /***Master panel methods*************************************************************************************************/
    /*Set the master panel that will hold all swing components in the frame for the main page*/
    private void initializeMasterPanel(JLabel label){
        System.out.println("METHOD - MainPage/initializeMasterPanel");
        this.masterPanel = new JPanel();
        this.masterPanel.setLayout(new BoxLayout(this.masterPanel, BoxLayout.Y_AXIS));
        this.masterPanel.setBackground(new Color(0,0,0,150));
        this.masterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,215, 0)); //border
        
        /*add components to the masterPanel*/
        addLogo(this.masterPanel);
        addSummonerLabel(this.masterPanel);
        
        
        label.add(this.masterPanel);
    }
    
    /*Add logo to the master panel*/
    private void addLogo(JPanel panel){
        System.out.println("METHOD - MainPage/addLogo");
        try {
            URL url = new URL("https://p2.zdassets.com/hc/theme_assets/43400/200033224/league-logo.png");
            BufferedImage c2 = ImageIO.read(url);
            ImageIcon leagueLogo = new ImageIcon(c2);
            Image logoImage = leagueLogo.getImage();
            ImageIcon newIcon = new ImageIcon(logoImage.getScaledInstance(450, 178, Image.SCALE_SMOOTH)); //resize
            JLabel logo = new JLabel(newIcon);
            JPanel logoHolder = new JPanel();
            logoHolder.add(logo);
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 0)); //border
            logoHolder.setOpaque(false);
            System.out.println("    Successful.");
            panel.add(logoHolder);
        } catch (MalformedURLException ex) {
            System.out.println("    MalformedURLException at addLogo method");
        } catch (IOException ex) {
            System.out.println("    IOException at addLogo method");
        }
    }
    
    /*Add summoner label to the master panel*/
    private void addSummonerLabel(JPanel panel){
        System.out.println("METHOD - MainPage/addSummonerLabel");
        //summmoner label
        JLabel label1 = new JLabel("SUMMONER:");
        label1.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 30)); //custom font
        label1.setForeground(Color.WHITE); //text color
        JPanel labelPanel = new JPanel();
        labelPanel.add(label1);
        JPanel labelHolder = new JPanel();
        labelHolder.add(labelPanel);
        labelHolder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); //border
        labelHolder.setOpaque(false);
        labelPanel.setBackground(new Color(0,0,0,0));
        panel.add(labelPanel);
    }
    
    /*Load font for use with labels*/
    private void loadFont(){
        System.out.println("METHOD - MainPage/loadFont");
        //load font
        try {
            //create the font to use. Specify the size!
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Sen-Regular.ttf")));
            System.out.println("    Successful.");
        } catch (IOException e) {
            System.out.println("    IOException at loadFont method");
        }
        catch(FontFormatException e)
        {
            System.out.println("    FontFormatException at loadFont method");
        }
    }
}
