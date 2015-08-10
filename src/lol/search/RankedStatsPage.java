/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.json.JSONObject;

/**
 *
 * @author Oscar
 */
public class RankedStatsPage {
    //class objects
    private GameStaticData OBJ_GAME_STATIC_DATA;
    private RankedStatsById OBJ_RANKED_STATS_BY_ID;
    private final League_ById OBJ_LEAGUE_BYID; 
    //carried over values
    private final String version;
    private final String summonerName;
    private final String region;
    private final long summonerId;
    private final long summonerLevel;
    private final ImageIcon profileIcon;
    private ArrayList<JSONObject> objChampRankedList = new ArrayList<>();
    private ArrayList<String> champKeyList = new ArrayList<>();
    private final String tier;
    private final String division;
    private final int wins;
    private final int losses;
    private final String winPercentage;
    //values for this class
    private final Dimension headerDimension = new Dimension(1200, 55);
    private final JFrame masterFrame;
    private String season;
    private final ArrayList<JLabel> spacers = new ArrayList<>();
    private JLabel background;
    private ArrayList<JButton> champButtons = new ArrayList<>();
    private int counter = 0;
    private final JLabel loadArtLabel = new JLabel();
    private final JLabel bodyHeaderText = new JLabel();
    private final Color backgroundColor = new Color(0,0,0,215);
    
    public RankedStatsPage(String version, JFrame frame, String region, Summoner_ByName objSummByName){ //constructor
        
        this.OBJ_GAME_STATIC_DATA = new GameStaticData();
        this.summonerName = objSummByName.getName();
        this.summonerId = objSummByName.getSummonerId();
        this.summonerLevel = objSummByName.getSummonerLevel();
        this.version = version;
        this.region = region;
        this.profileIcon = objSummByName.getProfileIcon();
        //call to other class for more info
        this.OBJ_LEAGUE_BYID = new League_ById(this.region, this.summonerId);
        this.tier = this.OBJ_LEAGUE_BYID.getTier();
        this.division = this.OBJ_LEAGUE_BYID.getDivision();
        this.wins = this.OBJ_LEAGUE_BYID.getWins();
        this.losses = this.OBJ_LEAGUE_BYID.getLosses();
        setSeason("SEASON2015");
        rankedStatsClassOperations(this.version, this.summonerId, this.region, this.season);
        this.winPercentage = getWinPercentage(this.wins, this.losses);
        //this.objRankedStats.printValues(); //print values carried over to the ranked stats class
        printCarriedValues();
        //set the background of the frame
        this.masterFrame = frame;
        JLabel backgroundLabel = getBackground();
        this.masterFrame.add(backgroundLabel);
        //printCarriedValues();
        
        frameRefresh(this.masterFrame);
    }
    private JPanel headerPanel(){
        //init spacers for header
        for(int i = 0; i < 10; i++){
            JLabel label = new JLabel("--");
            label.setForeground(new Color(0,0,0,0));
            spacers.add(label);
        }
        //header -- to set this semi-transparent i had to remove setOpaque and replace with setBackground(...)
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BorderLayout());
            //headerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            headerPanel.setBackground(backgroundColor);
            headerPanel.setPreferredSize(headerDimension);
                //back button
                JPanel buttonHolder = new JPanel();
                ImageIcon buttonImage = new ImageIcon("assets\\other\\button.png");
                ImageIcon buttonPressedImage = new ImageIcon("assets\\other\\buttonPressed.png");
                Image tempImage = buttonImage.getImage();
                Image newTempImg = tempImage.getScaledInstance(75, 35, Image.SCALE_SMOOTH);
                buttonImage = new ImageIcon(newTempImg);
                JButton backButton = new JButton("BACK");
                backButton.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 10)); //custom font
                backButton.setForeground(Color.WHITE); //text color
                backButton.setBackground(new Color(0,0,0,0));
                backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                backButton.setHorizontalTextPosition(AbstractButton.CENTER);
                backButton.setPreferredSize(new Dimension(75,35));
                //pressed button
                Image tempImage2 = buttonPressedImage.getImage();
                Image newTempImg2 = tempImage2.getScaledInstance(75, 35, Image.SCALE_SMOOTH);
                buttonPressedImage = new ImageIcon(newTempImg2);
                backButton.setIcon(buttonImage);
                backButton.setRolloverIcon(buttonPressedImage);
                backButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){ //button pressed
                        System.out.println("Going back...\n");
                        masterFrame.getContentPane().removeAll();
                        masterFrame.revalidate();
                        masterFrame.repaint();
                        MainPage MAIN_PAGE = new MainPage(masterFrame);
                    }
                });
                buttonHolder.add(backButton);
                buttonHolder.setOpaque(false);
                headerPanel.add(buttonHolder, BorderLayout.LINE_START);
                //centerpanel
                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new GridLayout(1,2));
                centerPanel.setOpaque(false);
                //centerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                //rightcenter
                JPanel rightCenter = new JPanel();
                rightCenter.setOpaque(false);
                rightCenter.setLayout(new GridLayout(2,1));
                //top center panel
                JPanel topCenter = new JPanel();
                topCenter.setOpaque(false);
                topCenter.setLayout(new BoxLayout(topCenter, BoxLayout.X_AXIS));
                //profile icon
                JPanel proIconPanel = new JPanel();
                proIconPanel.setOpaque(false);
                proIconPanel.setLayout(new BoxLayout(proIconPanel, BoxLayout.Y_AXIS));
                JLabel profileIconLabel = new JLabel(this.profileIcon);
                //profileIconLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                profileIconLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
                proIconPanel.add(profileIconLabel);
                centerPanel.add(proIconPanel);
                //empty spacer
                topCenter.add(spacers.get(0));
                //summoner name
                JLabel summonerNameLabel = new JLabel(this.summonerName);
                summonerNameLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
                summonerNameLabel.setForeground(Color.WHITE); //text color
                summonerNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                topCenter.add(summonerNameLabel);
                //empty spacer
                topCenter.add(spacers.get(1));
                //tier
                JLabel tierLabel = new JLabel(this.tier);
                tierLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
                tierLabel.setForeground(new Color(219,219,219)); //text color
                tierLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                topCenter.add(tierLabel);
                //empty spacer
                topCenter.add(spacers.get(2));
                //division
                JLabel divisionLabel = new JLabel(this.division);
                divisionLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
                divisionLabel.setForeground(new Color(219,219,219)); //text color
                divisionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                topCenter.add(divisionLabel);
                //bottom center panel
                JPanel bottomCenter = new JPanel();
                bottomCenter.setOpaque(false);
                bottomCenter.setLayout(new BoxLayout(bottomCenter, BoxLayout.X_AXIS));
                //empty spacer
                bottomCenter.add(spacers.get(3));
                //wins
                JLabel winsLabel = new JLabel("W: " + this.wins + "   L: " + this.losses + "   Win Ratio: " + this.winPercentage + "%");
                winsLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 14)); //custom font
                winsLabel.setForeground(new Color(219,219,219)); //text color
                winsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                bottomCenter.add(winsLabel);
                
                rightCenter.add(topCenter);
                rightCenter.add(bottomCenter);
                centerPanel.add(rightCenter);
                headerPanel.add(centerPanel, BorderLayout.CENTER);
                //empty panel to balance right side
                JPanel ee = new JPanel();
                ee.setOpaque(false);
                ee.setPreferredSize(new Dimension(260,50));
                headerPanel.add(ee, BorderLayout.LINE_END);
                return headerPanel;
    }
    private JPanel bodyPanel(){
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.X_AXIS));
        body.setBackground(backgroundColor);
        body.setPreferredSize(new Dimension(1200,530));
        
        //load art 
        this.loadArtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.loadArtLabel.setPreferredSize(new Dimension(290, 504));
        body.add(loadArtLabel);
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(800,514));
        panel.setOpaque(false);
        this.bodyHeaderText.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 40)); //custom font
        this.bodyHeaderText.setForeground(Color.WHITE);
        this.bodyHeaderText.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(this.bodyHeaderText);
        body.add(panel);
        
        return body;
    }
    private JScrollPane championSelectPanel(){
        JPanel mainPanel = new JPanel(new FlowLayout());
        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        mainPanel.setBackground(backgroundColor);
        for(int i = 0; i < this.champKeyList.size(); i++){
            if(this.champKeyList.get(i) == null){
                counter++;
                continue;
            }
            int position = counter;
            ImageIcon champImageIcon = this.OBJ_RANKED_STATS_BY_ID.getChampionIconOf(this.champKeyList.get(i));
            JButton champButton = new JButton();
            champButton.setIcon(champImageIcon);
            champButton.setPreferredSize(new Dimension(55,55));
            champButton.setBackground(Color.BLACK);
            champButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){ //button pressed
                    background.setIcon(OBJ_GAME_STATIC_DATA.getBackgroundImageIcon(champKeyList.get(position)));
                    loadArtLabel.setIcon(OBJ_GAME_STATIC_DATA.initLoadingArt(champKeyList.get(position)));
                    bodyHeaderText.setText(champKeyList.get(position));
                    masterFrame.revalidate();
                    masterFrame.repaint();
                }
            });
            champButtons.add(champButton);
            //champButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            counter++;
        }
        for(int i = 0; i < champButtons.size(); i++){
            mainPanel.add(champButtons.get(i));
            mainPanel.revalidate();
        }
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 85));
        scrollPane.setBackground(new Color(0,0,0,100));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = new Color(124,124,124,255);
                this.trackColor = Color.BLACK;
            }
        });
        
        return scrollPane;
    }
    private void printCarriedValues(){
        System.out.println("Carried values from operations:\n"+
                           "    Name: " + this.summonerName + "\n" +
                           "    Id: " + this.summonerId + "\n" +
                           "    Level: " + this.summonerLevel + "\n" +
                           "    Division: " + this.division + "\n" +
                           "    Tier: "  + this.tier + "\n" + 
                           "    Wins: " + this.wins + "\n" + 
                           "    Losses: " + this.losses + "\n" +
                           "    Win %: " + this.winPercentage + "%" + "\n");
    }
    
    private void frameRefresh(JFrame frame){
        frame.revalidate();
        frame.repaint();
    }
    
    /*get/set background*/
    private JLabel getBackground(){ 
        this.background = new JLabel();
        background.setIcon(this.OBJ_GAME_STATIC_DATA.getBackgroundImageIcon());
        background.setLayout(new GridLayout());
        background.add(mainPanel());
        return background;
    }
    
    /*main panel to be added to background label*/
    private JPanel mainPanel(){ 
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new FlowLayout()); //add panels to this
        mainPanel.add(headerPanel());
        mainPanel.add(bodyPanel());
        mainPanel.add(championSelectPanel());
        //mainPanel.add(bodyPanel());
        return mainPanel;
    }
    
    /*creates an object of the ranked stats by id class, and sets the necessary variables*/
    private void rankedStatsClassOperations(String version, long id, String region, String season){
        this.OBJ_RANKED_STATS_BY_ID = new RankedStatsById(version, id, region, season);
        this.objChampRankedList = this.OBJ_RANKED_STATS_BY_ID.getObjChampRankedList(); //has the json objects of the champions ranked stats
        this.champKeyList = this.OBJ_RANKED_STATS_BY_ID.getRankedChampKeyList(); //gets the string keys of the champions
    }
    /*set the season by which to retrieve ranked information from, for now it is 2015 by default*/
    private void setSeason(String season){
        this.season = season;
    }
    
    private String getWinPercentage(int w, int l){
        double winPer = (double)(w)/((double)(w)+(double)(l));
        winPer = winPer*100;
        return new DecimalFormat("##.##").format(winPer); //return formatted string of win percentage.
    }
}
