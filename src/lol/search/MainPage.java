/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This page is what the user will see when he/she starts the application. 
 * @author Oscar
 */
public class MainPage {
    
    private GameStaticData objGameStaticData; //class object to retrieve information from GameStaticData
    private LoLSearch objLoLSearch; //class object to retrieve api key
    
    private JLabel masterLabel; //holds the background image and masterPanel
    private JPanel masterPanel; //holds swing components of the frame
    private final JTextField summonerTextField = new JTextField(20);; //textfield for the user input
    private JComboBox regionsComboBox; //regions combobox, will be initialized in method
    private JButton searchButton; //button 
    private String regionCodeValue; //region code of region selected
    
    public MainPage(JFrame mainFrame){ //arg constructor
        System.out.println("CONSTRUCTOR - MainPage(arg)");
        loadFont();
        initializeMasterLabel(mainFrame);
        initializeMasterPanel(mainFrame, this.masterLabel);
        
        mainFrame.revalidate();
    }
    
    /*Set background of the frame and prepare for the main panel*/
    private void initializeMasterLabel(JFrame frame){
        System.out.println("METHOD - MainPage/initializeMasterLabel");
        //GameStaticData class object
        objGameStaticData = new GameStaticData();
        //background label
        this.masterLabel = new JLabel(objGameStaticData.getBackgroundImageIcon()); //use method from obj to get a background
        this.masterLabel.setLayout(new FlowLayout());
        frame.add(this.masterLabel);
    }
    
    /***Master panel methods*************************************************************************************************/
    /*Set the master panel that will hold all swing components in the frame for the main page*/
    private void initializeMasterPanel(JFrame frame, JLabel label){
        System.out.println("METHOD - MainPage/initializeMasterPanel");
        
        this.masterPanel = new JPanel();
        this.masterPanel.setLayout(new BoxLayout(this.masterPanel, BoxLayout.Y_AXIS));
        this.masterPanel.setBackground(new Color(0,0,0,150));
        this.masterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,215, 0)); //border
            /*add components to the masterPanel*/
            addLogo(this.masterPanel);
            addSummonerLabel(this.masterPanel);
            addSummonerTextField(this.masterPanel);
            addSearchButton(frame, this.masterPanel);
            addRegionsComboBox(this.masterPanel);
        
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
            System.out.println("    MalformedURLException ::: Check addLogo()");
        } catch (IOException ex) {
            System.out.println("    IOException ::: Check addLogo()");
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
    
    /*Add input textfield to the master panel*/
    private void addSummonerTextField(JPanel panel){
        System.out.println("METHOD - MainPage/addSummonerTextField");
        //textfield
        JPanel textFieldHolder = new JPanel();
        this.summonerTextField.setForeground(Color.BLACK);
        this.summonerTextField.setBackground(Color.LIGHT_GRAY);
        this.summonerTextField.setHorizontalAlignment(SwingConstants.CENTER);
        this.summonerTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.summonerTextField.setText("Osxander");
        textFieldHolder.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); //border
        textFieldHolder.add(this.summonerTextField);
        textFieldHolder.setOpaque(false);
        panel.add(textFieldHolder);
    }
    
    /*Add button to master panel*/
    private void addSearchButton(JFrame frame, JPanel panel){
        System.out.println("METHOD - MainPage/addSearchButton");
        //button
        JPanel buttonHolder = new JPanel();
        this.searchButton = new JButton("Player History");
        this.searchButton.setPreferredSize(new Dimension(250,50));
        this.searchButton.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 30)); //custom font
        this.searchButton.setForeground(Color.WHITE); //text color
        this.searchButton.setBackground(Color.DARK_GRAY);
        this.searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.searchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ //button pressed
                System.out.println("The button was pressed.");
                String nameInput = summonerTextField.getText().toLowerCase().replaceAll(" ", ""); //change text format for URL
                System.out.println("nameInput text: " + nameInput);
                String comboBoxValue = getComboBoxValue(regionsComboBox).toString(); //get combobox string
                ConvertToCountryCode(comboBoxValue); //convert it to country code ex. na, eu, ru, etc.
                System.out.println("country code: " + regionCodeValue);
                //prepare frame for next page
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                System.out.println("Frame contents removed...");
                System.out.println("Proceeding to GetSummonerByNameJSONResponse...\n");
                /*This is where the next page will be called. JSON information must be retrieved from another class.*/
                
            }
        });
        buttonHolder.add(this.searchButton);
        buttonHolder.setOpaque(false);
        panel.add(buttonHolder);
    }
    
    /*Get selected value from combobox*/
    private Object getComboBoxValue(JComboBox cb){
        return cb.getSelectedItem();
    }
    private void ConvertToCountryCode(String country){
        switch (country) {
            case "North America":
                this.regionCodeValue = "na";
                break;
            case "Brazil":
                this.regionCodeValue = "br";
                break;
            case "EU Nordic & East":
                this.regionCodeValue = "eune";
                break;
            case "EU West":
                this.regionCodeValue = "euw";
                break;
            case "Korea":
                this.regionCodeValue = "kr";
                break;
            case "Latin America North":
                this.regionCodeValue = "lan";
                break;
            case "Latin America South":
                this.regionCodeValue = "las";
                break;
            case "Oceania":
                this.regionCodeValue = "oce";
                break;
            case "Public Beta Environment":
                this.regionCodeValue = "pbe";
                break;
            case "Russia":
                this.regionCodeValue = "ru";
                break;
            case "Turkey":
                this.regionCodeValue = "tr";
                break;
        }
    }
    /*Add combobox to the master panel for the different regions*/
    private void addRegionsComboBox(JPanel panel){
        System.out.println("METHOD - MainPage/addRegionsComboBox");
        //combobox
        JPanel comboBoxPanel = new JPanel();
        this.regionsComboBox = new JComboBox(this.objGameStaticData.getRegionsArray()); //different regions in the combo box
        this.regionsComboBox.setEditable(true); //allow the user to choose
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); //border
        comboBoxPanel.add(this.regionsComboBox);
        comboBoxPanel.setOpaque(false);
        panel.add(comboBoxPanel);
    }
    
    /*Load font for use with labels*/
    private void loadFont(){
        System.out.println("METHOD - MainPage/loadFont");
        //load font
        try {
            //create the font to use
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Sen-Regular.ttf")));
            System.out.println("    Successful.");
        } catch (IOException e) {
            System.out.println("    IOException ::: Check loadFont()");
        }
        catch(FontFormatException e)
        {
            System.out.println("    FontFormatException ::: Check loadFont()");
        }
    }
}
