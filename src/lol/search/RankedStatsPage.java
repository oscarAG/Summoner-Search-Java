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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Oscar
 */
public class RankedStatsPage {
    //class objects
    private final GameStaticData OBJ_GAME_STATIC_DATA;
    private RankedStatsById OBJ_RANKED_STATS_BY_ID;
    private final AllChampionsById OBJ_ALL_CHAMPS_BY_ID;
    private final League_ById OBJ_LEAGUE_BYID; 
    //carried over values
    private final String version;
    private final String summonerName;
    private final String region;
    private final long summonerId;
    private final long summonerLevel;
    private final ImageIcon profileIcon;
    private ArrayList<JSONObject> objChampRankedList = new ArrayList<>();
    private final ArrayList<String> champKeyList = new ArrayList<>();
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
    private final ArrayList<JButton> champButtons = new ArrayList<>();
    private int counter = 0;
    private final JLabel loadArtLabel = new JLabel();
    private final JLabel defaultHeader = new JLabel();
    private final JLabel nameHeader = new JLabel();
    private final JLabel titleHeader = new JLabel();
    private final Color backgroundColor = new Color(0,0,0,215);
    private final Color valueOrange = new Color(255,153,51);
    private final ArrayList<Integer> champIdList = new ArrayList<>();
    private final JLabel winsLabel = new JLabel();
    private final JLabel lossesLabel = new JLabel();
    private final JLabel totalWins = new JLabel();
    private final JLabel totalLosses = new JLabel();
    private final JLabel winPercentLabel = new JLabel();
    private final JLabel winPercent = new JLabel();
    private final JLabel totalGames = new JLabel();
    private final JLabel totalGamesPlayed = new JLabel();
    private int totalGamesInt;
    private final JLabel leftSideHeaderLabel = new JLabel();
    private final JLabel rightSideHeaderLabel = new JLabel();
    private final JLabel avgKillsLabel = new JLabel();
    private final JLabel avgKillsLabelValue = new JLabel();
    private final JLabel avgDeathsLabel = new JLabel();
    private final JLabel avgDeathsLabelValue = new JLabel();
    private final JLabel avgAssistsLabel = new JLabel();
    private final JLabel avgAssistsLabelValue = new JLabel();
    private final JLabel avgMinionKillsLabel = new JLabel();
    private final JLabel avgMinionKillsLabelValue = new JLabel();
    private final JLabel totalKillsLabel = new JLabel();
    private final JLabel totalKillsLabelValue = new JLabel();
    private final JLabel totalDeathsLabel = new JLabel();
    private final JLabel totalDeathsLabelValue = new JLabel();
    private final JLabel totalAssistsLabel = new JLabel();
    private final JLabel totalAssistsLabelValue = new JLabel();
    private final JLabel totalMinionsLabel = new JLabel();
    private final JLabel totalMinionsLabelValue = new JLabel();
    
    public RankedStatsPage(String version, JFrame frame, String region, Summoner_ByName objSummByName){ //constructor
        this.OBJ_GAME_STATIC_DATA = new GameStaticData();
        this.summonerName = objSummByName.getName();
        this.summonerId = objSummByName.getSummonerId();
        this.summonerLevel = objSummByName.getSummonerLevel();
        this.version = version;
        this.region = region;
        this.profileIcon = objSummByName.getProfileIcon();
        this.OBJ_ALL_CHAMPS_BY_ID = new AllChampionsById(this.region);
        //call to other class for more info
        this.OBJ_LEAGUE_BYID = new League_ById(this.region, this.summonerId);
        this.tier = this.OBJ_LEAGUE_BYID.getTier();
        this.division = this.OBJ_LEAGUE_BYID.getDivision();
        this.wins = this.OBJ_LEAGUE_BYID.getWins();
        this.losses = this.OBJ_LEAGUE_BYID.getLosses();
        this.totalGamesInt = wins+losses;
        setSeason("SEASON2015");
        rankedStatsClassOperations(this.version, this.summonerId, this.region, this.season);
        this.winPercentage = getWinPercentage(this.wins, this.losses);
        sort(); //sorts the rankedJSONObject lists
        setChampIdList();
        setChampKeyList();
        //System.out.println(champIdList);
        //System.out.println(champKeyList);
        //this.objRankedStats.printValues(); //print values carried over to the ranked stats class
        //printCarriedValues();
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
                        MainPage MAIN_PAGE = new MainPage(masterFrame, summonerName);
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
                //season
                JLabel winsLabel = new JLabel(this.season);
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
    private JPanel bodyPanel(JPanel body){
        body.setLayout(new BoxLayout(body, BoxLayout.X_AXIS));
        body.setBackground(backgroundColor);
        body.setPreferredSize(new Dimension(1200,530));
        
        //load art 
        this.loadArtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.loadArtLabel.setPreferredSize(new Dimension(290, 504));
        this.loadArtLabel.setIcon(OBJ_GAME_STATIC_DATA.initLoadingArt(champKeyList.get(0)));
        body.add(this.loadArtLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout());
        rightPanel.setPreferredSize(new Dimension(800,514));
        rightPanel.setOpaque(false);
        //rightPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setPreferredSize(new Dimension(910, 55));
        headerPanel.setOpaque(false);
        //headerPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.defaultHeader.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 40)); //custom font
        this.defaultHeader.setForeground(Color.WHITE);
        this.defaultHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.defaultHeader.setText(" Season Totals: ");
        this.nameHeader.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 40)); //custom font
        this.nameHeader.setForeground(valueOrange);
        this.nameHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.nameHeader.setText("Overall");
        this.titleHeader.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 16)); //custom font
        this.titleHeader.setForeground(new Color(255,128,0));
        this.titleHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        //nameHeader.setAlignmentY(Component.TOP_ALIGNMENT);
        titleHeader.setAlignmentY(Component.TOP_ALIGNMENT);
        headerPanel.add(defaultHeader);
        headerPanel.add(nameHeader);
        headerPanel.add(titleHeader);
        rightPanel.add(headerPanel);
        rightPanel.add(statsPanel());
        body.add(rightPanel);
        
        return body;
    }
    private JPanel statsPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(910, 464));
            JPanel statsPanelTotals = new JPanel();
            statsPanelTotals.setLayout(new BoxLayout(statsPanelTotals, BoxLayout.X_AXIS));
            statsPanelTotals.setOpaque(false);
            //statsPanelTotals.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            //totals
            statsPanelTotals.setPreferredSize(new Dimension(910, 45));
                totalJLabel(winsLabel, "   W: ", Color.WHITE);
                winsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                totalJLabel(totalWins, ""+this.wins, valueOrange);
                totalJLabel(lossesLabel, "   L: ", Color.WHITE);
                totalJLabel(totalLosses,"" + this.losses, valueOrange);
                totalJLabel(winPercentLabel, "   Win Ratio: ",Color.WHITE);
                totalJLabel(winPercent, winPercentage + "%", valueOrange);
                totalJLabel(totalGames, "   Total Games Played: ", Color.WHITE);
                totalJLabel(this.totalGamesPlayed, String.valueOf(totalGamesInt), valueOrange);
                statsPanelTotals.add(winsLabel);
                statsPanelTotals.add(totalWins);
                statsPanelTotals.add(lossesLabel);
                statsPanelTotals.add(totalLosses);
                statsPanelTotals.add(winPercentLabel);
                statsPanelTotals.add(winPercent);
                statsPanelTotals.add(totalGames);
                statsPanelTotals.add(totalGamesPlayed);
            JPanel totalsAndAverages = new JPanel();
            totalsAndAverages.setOpaque(false);
            totalsAndAverages.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            totalsAndAverages.setPreferredSize(new Dimension(910, 405));
            totalsAndAverages.setLayout(new GridLayout());
                JPanel leftSide = new JPanel();
                leftSide.setOpaque(false);
                leftSide.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    JPanel leftSideHeader = new JPanel();
                    leftSideHeader.setOpaque(false);
                    leftSideHeader.setLayout(new FlowLayout());
                    leftSideHeader.setPreferredSize(new Dimension(455, 35));
                    //leftSideHeader.setBorder(BorderFactory.createLineBorder(Color.CYAN));
                        totalJLabel(this.leftSideHeaderLabel, "   Per Game Averages:", Color.WHITE);
                        leftSideHeader.add(this.leftSideHeaderLabel);
                    JPanel leftSideBody = new JPanel();
                    leftSideBody.setOpaque(false);
                    leftSideBody.setLayout(new BoxLayout(leftSideBody, BoxLayout.Y_AXIS));
                    leftSideBody.setPreferredSize(new Dimension(455, 360));
                    //leftSideBody.setBorder(BorderFactory.createLineBorder(Color.RED));
                        JPanel avgKillsPanel = new JPanel();
                            avgKillsPanel.setOpaque(false);
                            //avgKillsPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
                        JPanel avgDeathsPanel = new JPanel();
                            avgDeathsPanel.setOpaque(false);
                        JPanel avgAssistsPanel = new JPanel();
                            avgAssistsPanel.setOpaque(false);
                        JPanel avgMinionsPanel = new JPanel();
                            avgMinionsPanel.setOpaque(false);
                        totalJLabel(this.avgKillsLabel, "   Avg. Kills: ", Color.WHITE);
                        totalJLabel(this.avgDeathsLabel, "   Avg. Deaths: ",Color.WHITE);
                        totalJLabel(this.avgAssistsLabel, "   Avg. Assists: ", Color.WHITE);
                        totalJLabel(this.avgMinionKillsLabel, "   Avg. Minions Killed: ", Color.WHITE);
                        double totalKills = 00000;
                        double totalDeaths = 00000;
                        double totalAssists = 00000;
                        double totalMinions = 00000;
                        double avgKills = 99999;
                        double avgAssists = 99999;
                        double avgDeaths = 99999;
                        double avgMinions = 99999;
                        try {
                            double totalGamesPlayed = this.objChampRankedList.get(0).getJSONObject("stats").getInt("totalSessionsPlayed");
                            //operations
                            totalKills = this.objChampRankedList.get(0).getJSONObject("stats").getInt("totalChampionKills");
                            avgKills = totalKills/totalGamesPlayed;
                            totalDeaths = this.objChampRankedList.get(0).getJSONObject("stats").getInt("totalDeathsPerSession");
                            avgDeaths = totalDeaths/totalGamesPlayed;
                            totalAssists = this.objChampRankedList.get(0).getJSONObject("stats").getInt("totalAssists");
                            avgAssists = totalAssists/totalGamesPlayed;
                            totalMinions = this.objChampRankedList.get(0).getJSONObject("stats").getInt("totalMinionKills");
                            avgMinions = totalMinions/totalGamesPlayed;
                        } catch (JSONException ex) {
                            Logger.getLogger(RankedStatsPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String avgKillsString =new DecimalFormat("##.##").format(avgKills);
                        String avgDeathsString = new DecimalFormat("##.##").format(avgDeaths);
                        String avgAssistsString = new DecimalFormat("##.##").format(avgAssists);
                        String avgMinionsString = new DecimalFormat("##.##").format(avgMinions);
                        totalJLabel(this.avgKillsLabelValue, avgKillsString, valueOrange);
                        totalJLabel(this.avgDeathsLabelValue, avgDeathsString, valueOrange);
                        totalJLabel(this.avgAssistsLabelValue, avgAssistsString, valueOrange);
                        totalJLabel(this.avgMinionKillsLabelValue, avgMinionsString, valueOrange);
                        avgKillsPanel.add(avgKillsLabel);
                        avgKillsPanel.add(avgKillsLabelValue);
                        avgDeathsPanel.add(avgDeathsLabel);
                        avgDeathsPanel.add(avgDeathsLabelValue);
                        avgAssistsPanel.add(avgAssistsLabel);
                        avgAssistsPanel.add(avgAssistsLabelValue);
                        avgMinionsPanel.add(avgMinionKillsLabel);
                        avgMinionsPanel.add(avgMinionKillsLabelValue);
                        leftSideBody.add(avgKillsPanel);
                        leftSideBody.add(avgDeathsPanel);
                        leftSideBody.add(avgAssistsPanel);
                        leftSideBody.add(avgMinionsPanel);
                leftSide.add(leftSideHeader);
                leftSide.add(leftSideBody);
                JPanel rightSide = new JPanel();
                /**/
                rightSide.setOpaque(false);
                rightSide.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    JPanel rightSideHeader = new JPanel();
                    rightSideHeader.setOpaque(false);
                    rightSideHeader.setLayout(new FlowLayout());
                    rightSideHeader.setPreferredSize(new Dimension(455, 35));
                    //rightSideHeader.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                        totalJLabel(this.rightSideHeaderLabel, "   Season Totals:", Color.WHITE);
                        rightSideHeader.add(this.rightSideHeaderLabel);
                    JPanel rightSideBody = new JPanel();
                    rightSideBody.setOpaque(false);
                    rightSideBody.setLayout(new BoxLayout(rightSideBody, BoxLayout.Y_AXIS));
                    rightSideBody.setPreferredSize(new Dimension(455, 360));
                        JPanel totalKillsPanel = new JPanel();
                            totalKillsPanel.setOpaque(false);
                        JPanel totalDeathsPanel = new JPanel();
                            totalDeathsPanel.setOpaque(false);
                        JPanel totalAssistsPanel = new JPanel();
                            totalAssistsPanel.setOpaque(false);
                        JPanel totalMinionsPanel = new JPanel();
                            totalMinionsPanel.setOpaque(false);
                        totalJLabel(this.totalKillsLabel, "   Total Kills: ", Color.WHITE);
                        totalJLabel(this.totalKillsLabelValue, new DecimalFormat("#######").format(totalKills), valueOrange);
                        totalJLabel(this.totalDeathsLabel, "   Total Deaths: ", Color.WHITE);
                        totalJLabel(this.totalDeathsLabelValue, new DecimalFormat("#######").format(totalDeaths), valueOrange);
                        totalJLabel(this.totalAssistsLabel, "   Total Assists: ", Color.WHITE);
                        totalJLabel(this.totalAssistsLabelValue, new DecimalFormat("#######").format(totalAssists), valueOrange);
                        totalJLabel(this.totalMinionsLabel, "   Total Minions Killed: ", Color.WHITE);
                        totalJLabel(this.totalMinionsLabelValue, new DecimalFormat("#######").format(totalMinions), valueOrange);
                        totalKillsPanel.add(totalKillsLabel);
                        totalKillsPanel.add(totalKillsLabelValue);
                        totalDeathsPanel.add(totalDeathsLabel);
                        totalDeathsPanel.add(totalDeathsLabelValue);
                        totalAssistsPanel.add(totalAssistsLabel);
                        totalAssistsPanel.add(totalAssistsLabelValue);
                        totalMinionsPanel.add(totalMinionsLabel);
                        totalMinionsPanel.add(totalMinionsLabelValue);
                        rightSideBody.add(totalKillsPanel);
                        rightSideBody.add(totalDeathsPanel);
                        rightSideBody.add(totalAssistsPanel);
                        rightSideBody.add(totalMinionsPanel);
                    //rightSideBody.setBorder(BorderFactory.createLineBorder(Color.RED));
                rightSide.add(rightSideHeader);
                rightSide.add(rightSideBody);
            totalsAndAverages.add(leftSide);
            totalsAndAverages.add(rightSide);
        panel.add(statsPanelTotals);
        panel.add(totalsAndAverages);
        return panel;
    }
    private void totalJLabel(JLabel label, String text, Color color){
        label.setText(text);
        label.setForeground(color);
        label.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 16)); //custom font
    }
    private void setChampIdList(){
        for(int i = 0; i < this.objChampRankedList.size(); i++){
            try {
                this.champIdList.add(this.objChampRankedList.get(i).getInt("id"));
            } catch (JSONException ex) {
                Logger.getLogger(RankedStatsPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private JScrollPane championSelectPanel(){
        JPanel mainPanel = new JPanel(new FlowLayout());
        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        mainPanel.setBackground(backgroundColor);
        for(int i = 0; i < this.objChampRankedList.size(); i++){
            int position = counter;
            ImageIcon champImageIcon = this.OBJ_RANKED_STATS_BY_ID.getChampionIconOf(this.champKeyList.get(i));
            JButton champButton = new JButton();
            champButton.setIcon(champImageIcon);
            if(i == 0){
                champButton.setIcon(this.profileIcon);
                champButton.setToolTipText("Overall Stats");
            }
            champButton.setPreferredSize(new Dimension(55,55));
            champButton.setBackground(Color.BLACK);
            champButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){ //button pressed
                    background.setIcon(OBJ_GAME_STATIC_DATA.getBackgroundImageIcon(champKeyList.get(position)));
                    loadArtLabel.setIcon(OBJ_GAME_STATIC_DATA.initLoadingArt(champKeyList.get(position)));
                    nameHeader.setText(OBJ_ALL_CHAMPS_BY_ID.getChampNameFromId(champIdList.get(position)));
                    
                    titleHeader.setText(" " + OBJ_ALL_CHAMPS_BY_ID.getChampTitleFromId(champIdList.get(position)));
                    String sessionsWon = "";
                    String sessionsLost = "";
                    String winPercentString = "";
                    try {
                        int won = objChampRankedList.get(position).getJSONObject("stats").getInt("totalSessionsWon");
                        sessionsWon = Integer.toString(won);
                        int lost = objChampRankedList.get(position).getJSONObject("stats").getInt("totalSessionsLost");
                        sessionsLost = Integer.toString(lost);
                        winPercentString = getWinPercentage(won, lost);
                        totalGamesInt = won+lost;
                        avgKillsLabelValue.setText(
                                new DecimalFormat("##.##").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalChampionKills")/(double)totalGamesInt));
                        avgAssistsLabelValue.setText(
                                new DecimalFormat("##.##").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalAssists")/(double)totalGamesInt));
                        avgDeathsLabelValue.setText(
                                new DecimalFormat("##.##").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalDeathsPerSession")/(double)totalGamesInt));
                        avgMinionKillsLabelValue.setText(
                                new DecimalFormat("##.##").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalMinionKills")/(double)totalGamesInt));
                        totalKillsLabelValue.setText(new DecimalFormat("#######").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalChampionKills")));
                        totalDeathsLabelValue.setText(new DecimalFormat("#######").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalDeathsPerSession")));
                        totalAssistsLabelValue.setText(new DecimalFormat("#######").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalAssists")));
                        totalMinionsLabelValue.setText(new DecimalFormat("#######").format((double)objChampRankedList.get(position).getJSONObject("stats").getInt("totalMinionKills")));
                    } catch (JSONException ex) {
                        Logger.getLogger(RankedStatsPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    totalWins.setText(sessionsWon);
                    totalLosses.setText(sessionsLost);
                    winPercent.setText(winPercentString + "%");
                    totalGamesPlayed.setText(String.valueOf(totalGamesInt));
                    masterFrame.revalidate();
                    masterFrame.repaint();
                }
            });
            champButton.setToolTipText(OBJ_ALL_CHAMPS_BY_ID.getChampNameFromId(champIdList.get(position)));
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
                           "    Win %: " + this.winPercentage + "%" + "\n"
                            + this.champIdList);
    }
    
    private void frameRefresh(JFrame frame){
        frame.revalidate();
        frame.repaint();
    }
    
    /*get/set background*/
    private JLabel getBackground(){ 
        this.background = new JLabel();
        background.setIcon(this.OBJ_GAME_STATIC_DATA.getStatsBackground());
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
        //body panel
        JPanel body = new JPanel();
        mainPanel.add(bodyPanel(body));
        mainPanel.add(championSelectPanel());
        return mainPanel;
    }
    
    /*creates an object of the ranked stats by id class, and sets the necessary variables*/
    private void rankedStatsClassOperations(String version, long id, String region, String season){
        this.OBJ_RANKED_STATS_BY_ID = new RankedStatsById(version, id, region, season);
        this.objChampRankedList = this.OBJ_RANKED_STATS_BY_ID.getObjChampRankedList(); //has the json objects of the champions ranked stats
    }
    
    private void setChampKeyList(){
        for(int i = 0; i < champIdList.size();i++){
            champKeyList.add(this.OBJ_ALL_CHAMPS_BY_ID.getChampKeyFromId(champIdList.get(i)));
        }
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
    
    private void sort(){
        int count = 0;
        int max = 0;
        int maxPosition = 0;
        
        for(int i = 0; i < objChampRankedList.size(); i++){
            for(int j = count; j < objChampRankedList.size(); j++){
                try {
                    if(this.objChampRankedList.get(j).getJSONObject("stats").getInt("totalSessionsPlayed") > max){
                        max = this.objChampRankedList.get(j).getJSONObject("stats").getInt("totalSessionsPlayed");
                        maxPosition = j;
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(RankedStatsPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Collections.swap(objChampRankedList, maxPosition, i);
            max = 0;
            maxPosition=0;
            count++;
        }
        
        for(int i = 0; i < objChampRankedList.size(); i++){
            System.out.println(objChampRankedList.get(i));
        }
        
        
    }
}
