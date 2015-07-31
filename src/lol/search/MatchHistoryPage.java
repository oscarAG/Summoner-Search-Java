
package lol.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class will create the page that holds the 10 recent matches with information. 
 * @author Oscar
 */
public class MatchHistoryPage {
    
    private final GameStaticData objGameStaticData;
    private final Dimension headerDimension = new Dimension(1200, 55);
    private final Dimension bodyDimension = new Dimension(1200, 620);
    private final Dimension matchDimension = new Dimension(1190, 56);
    private final Dimension outcomeColorDimension = new Dimension(10,45);
    //values taken from other classes
    private final JFrame masterFrame;
    private final String summonerName;
    private final long summonerLevel;
    private final ArrayList<ImageIcon> championIcons;
    private final ArrayList<ArrayList<Integer>> itemIdMasterList; //list with lists of items picked in each match
    private final ArrayList<Integer> killsList; //list with the kills from the 10 matches
    private final ArrayList<Integer> assistsList; //list with the assists from the 10 matches
    private final ArrayList<Integer> deathsList; //list with the deaths from the 10 matches
    private final ArrayList<ArrayList<ImageIcon>> itemIconMasterList;
    private final ImageIcon profileIcon;
    private final ArrayList<Boolean> outcomeList;
    private final ArrayList<ImageIcon> spellOneIconList;
    private final ArrayList<ImageIcon> spellTwoIconList;
    private final ArrayList<Integer> goldList;
    private final ArrayList<Integer> minionsKilledList;
    
    public MatchHistoryPage(JFrame frame, Summoner_ByName objSBN, Game_ById objGBI, LoLStaticData_AllChampions objAC){
        System.out.println("CONSTRUCTOR - MatchHistoryPage(arg, arg, arg, arg)");
        this.objGameStaticData = new GameStaticData(); //used to set background
        this.masterFrame = frame;
        
        //values carried over
        this.summonerName = objSBN.getName();
        this.summonerLevel = objSBN.getSummonerLevel();
        this.championIcons = objAC.getChampionIcons();
        this.itemIdMasterList = objGBI.getItemIdMasterList();
        this.killsList = objGBI.getKillsList();
        this.itemIconMasterList = objGBI.getItemIconMasterList();
        this.assistsList = objGBI.getAssistsList();
        this.deathsList = objGBI.getDeathsList();
        this.profileIcon = objSBN.getProfileIcon();
        this.outcomeList = objGBI.getOutcomeList();
        this.spellOneIconList = objGBI.getSpellOneIconList();
        this.spellTwoIconList = objGBI.getSpellTwoIconList();
        this.goldList = objGBI.getGoldEarnedList();
        this.minionsKilledList = objGBI.getMinionsKilled();
        printCarriedValues(); //check to confirm necessary values have been carried over successfully
        
        /*Set background*/
        JLabel backgroundLabel = new JLabel(objGameStaticData.getBackgroundImageIcon());
        backgroundLabel.setLayout(new GridLayout());
        backgroundLabel.add(mainPanel());
        
        this.masterFrame.add(backgroundLabel);
        this.masterFrame.revalidate();
        
        
        System.out.println("END - MatchHistoryPage(arg, arg, arg, arg)\n");
    }
    private JPanel mainPanel(){
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new FlowLayout()); //add panels to this
        
        mainPanel.add(headerPanel()); //add header panel to main panel
        mainPanel.add(bodyPanel());
        return mainPanel;
    }
    private JPanel headerPanel(){
        //header -- to set this semi-transparent i had to remove setOpaque and replace with setBackground(...)
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new FlowLayout());
            //headerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            headerPanel.setBackground(new Color(0,0,0,170));
            headerPanel.setPreferredSize(headerDimension);
                //profile icon
                JLabel profileIconLabel = new JLabel(this.profileIcon);
                headerPanel.add(profileIconLabel);
                //summoner name
                JLabel summonerNameLabel = new JLabel(this.summonerName);
                summonerNameLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 20)); //custom font
                summonerNameLabel.setForeground(Color.WHITE); //text color
                JPanel namePanel = new JPanel();
                namePanel.setOpaque(false);
                namePanel.add(summonerNameLabel);
                headerPanel.add(namePanel);
                //empty panel
                JPanel emptyPanel = new JPanel();
                JLabel emptyLabel = new JLabel("----");
                emptyPanel.add(emptyLabel);
                emptyLabel.setForeground(new Color(0,0,0,0));
                emptyPanel.setOpaque(false);
                headerPanel.add(emptyPanel);
                //summoner level
                String level = String.valueOf(this.summonerLevel);
                JLabel summonerLevelLabel = new JLabel(level);
                summonerLevelLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 20)); //custom font
                summonerLevelLabel.setForeground(Color.WHITE); //text color
                JPanel levelPanel = new JPanel();
                levelPanel.setOpaque(false);
                levelPanel.add(summonerLevelLabel);
                headerPanel.add(levelPanel);
                
                return headerPanel;
    }
    private JPanel bodyPanel(){
        ArrayList<JPanel> panelArrList = new ArrayList<>();
        //body, which holds the 10 recent matches
        JPanel bodyPanel = new JPanel();
        bodyPanel.setPreferredSize(bodyDimension); //dimension of the body panel
        //bodyPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        bodyPanel.setOpaque(false);
        for(int i = 0; i < this.championIcons.size(); i ++){ //loop for the 10 maches
            JPanel matchPanel = new JPanel();
            matchPanel.setLayout(new FlowLayout()); //layout of each ind match
            matchPanel.setBackground(new Color(0,0,0,170));
            matchPanel.setPreferredSize(matchDimension);
            //outcome color 
            JPanel outcomeColorPanel = new JPanel();
            JLabel outcomeColorLabel = new JLabel("--");
            outcomeColorLabel.setForeground(new Color(0,0,0,0));
            outcomeColorLabel.setOpaque(false);
            
            //set color if won or lost
            if(this.outcomeList.get(i)){ //if true
                outcomeColorPanel.setBackground(new Color(0,210,0,180)); //green
            }
            else{
                outcomeColorPanel.setBackground(new Color(210,0,0,180)); //red
            }
            outcomeColorPanel.add(outcomeColorLabel);
            outcomeColorPanel.setPreferredSize(outcomeColorDimension);

            //champion label
            JLabel champIconLabel = new JLabel(championIcons.get(i));
          
            //spacer
            JLabel spacer0 = new JLabel("---");
            spacer0.setForeground(new Color(0,0,0,0));
            JLabel spacer1 = new JLabel("---");
            spacer1.setForeground(new Color(0,0,0,0));
            JLabel spacer2 = new JLabel("---");
            spacer2.setForeground(new Color(0,0,0,0));
            JLabel spacer3 = new JLabel("---");
            spacer3.setForeground(new Color(0,0,0,0));
            JLabel spacer4 = new JLabel("---");
            spacer4.setForeground(new Color(0,0,0,0));
            //summoner spells
            JPanel summonerSpellsPanel = new JPanel();
                summonerSpellsPanel.setLayout(new BoxLayout(summonerSpellsPanel, BoxLayout.Y_AXIS)); //top to bottom
                summonerSpellsPanel.setOpaque(false);
                    JLabel summonerSpellOneLabel = new JLabel(this.spellOneIconList.get(i));
                    JLabel summonerSpellTwoLabel = new JLabel(this.spellTwoIconList.get(i));
                    summonerSpellsPanel.add(summonerSpellOneLabel);
                    summonerSpellsPanel.add(summonerSpellTwoLabel);
           
            //items icon
            /*The items icon(picture of money bag) needs to go here*/
            
            //item icons
            JPanel itemsPanel = new JPanel(); //holds the item labels
            itemsPanel.setOpaque(false);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.X_AXIS));
            for(int j = 0; j < this.itemIconMasterList.get(i).size(); j++){
                JLabel itemLabel = new JLabel(this.itemIconMasterList.get(i).get(j));
                itemLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,3));
                itemLabel.setOpaque(false);
                itemsPanel.add(itemLabel);
            }
            
            //kills icon
            JPanel scoreMasterPanel = new JPanel();
            scoreMasterPanel.setOpaque(false);
            scoreMasterPanel.setLayout(new BoxLayout(scoreMasterPanel, BoxLayout.X_AXIS));
            scoreMasterPanel.setPreferredSize(new Dimension(110,40));
            JPanel scoreIconPanel = new JPanel(); //holds the crossed swords icon
            //scoreIconPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            scoreIconPanel.setLayout(new BoxLayout(scoreIconPanel, BoxLayout.X_AXIS));
            scoreIconPanel.setOpaque(false);
            JLabel scoreLabel = new JLabel(this.objGameStaticData.getScoreboardIconOf("score"));
            scoreIconPanel.add(scoreLabel);
            JLabel KDALabel = new JLabel(this.killsList.get(i) + "/" + this.deathsList.get(i) + "/" + this.assistsList.get(i));
            KDALabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            KDALabel.setForeground(Color.WHITE);
            scoreIconPanel.add(KDALabel);
            scoreMasterPanel.add(scoreIconPanel);

            //gold icon
            JPanel goldMasterPanel = new JPanel();
            goldMasterPanel.setOpaque(false);
            goldMasterPanel.setLayout(new BoxLayout(goldMasterPanel, BoxLayout.X_AXIS));
            goldMasterPanel.setPreferredSize(new Dimension(64,40));
            JPanel goldPanel = new JPanel();
            //goldPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            goldPanel.setLayout(new BoxLayout(goldPanel, BoxLayout.X_AXIS));
            goldPanel.setOpaque(false);
            JLabel bagLabel = new JLabel(this.objGameStaticData.getScoreboardIconOf("items"));
            goldPanel.add(bagLabel);
            int tempGoldValue = this.goldList.get(i) / 1000;
            JLabel goldLabel = new JLabel(tempGoldValue + "k");
            goldLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            goldLabel.setForeground(Color.WHITE);
            goldPanel.add(goldLabel);
            goldMasterPanel.add(goldPanel);
            
            //minons icon
            JPanel minionMasterPanel = new JPanel();
            minionMasterPanel.setOpaque(false);
            minionMasterPanel.setLayout(new BoxLayout(minionMasterPanel, BoxLayout.X_AXIS));
            minionMasterPanel.setPreferredSize(new Dimension(64,40));
            JPanel minionPanel = new JPanel();
            //minionPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            minionPanel.setLayout(new BoxLayout(minionPanel, BoxLayout.X_AXIS));
            minionPanel.setOpaque(false);
            JLabel minionLabel = new JLabel(this.objGameStaticData.getScoreboardIconOf("minion"));
            minionPanel.add(minionLabel);
            int minionsKilledTemp = this.minionsKilledList.get(i);
            JLabel minionValueLabel = new JLabel(minionsKilledTemp + "");
            minionValueLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            minionValueLabel.setForeground(Color.WHITE);
            minionPanel.add(minionValueLabel);
            minionMasterPanel.add(minionPanel);
            
            JLabel dateLabel = new JLabel("dateLabel");
            dateLabel.setForeground(Color.WHITE);

            matchPanel.add(outcomeColorPanel);
            matchPanel.add(champIconLabel);
            matchPanel.add(summonerSpellsPanel);
            matchPanel.add(spacer0);
            matchPanel.add(itemsPanel);
            matchPanel.add(spacer1);
            matchPanel.add(scoreMasterPanel);
            matchPanel.add(spacer2);
            matchPanel.add(goldMasterPanel);
            matchPanel.add(spacer3);
            matchPanel.add(minionMasterPanel);
            matchPanel.add(spacer4);
            //matchPanel.add(dateLabel);
            
            panelArrList.add(matchPanel);
        }
        for(int i = 0; i < panelArrList.size(); i++){
            bodyPanel.add(panelArrList.get(i));
        }

        return bodyPanel;
    }
    private void printCarriedValues(){
        System.out.println("METHOD - MatchHistoryPage/printCarriedValues()\n    Carried over values:");
        System.out.println("        Name: " + this.summonerName);
        System.out.println("        Level: " + this.summonerLevel);
        if(!this.championIcons.isEmpty()){
            System.out.println("        championIcons has been initialized.");
        }
        else{
            System.out.println("        championIcons has NOT been initialized.");
        }
        if(!this.itemIdMasterList.isEmpty()){
            System.out.println("        itemIdMasterList has been initialized.");
        }
        else{
            System.out.println("        itemIdMasterList has NOT been initialized.");
        }
        if(!this.itemIconMasterList.isEmpty()){
            System.out.println("        itemIconMasterList has been initialized.");
        }
        else{
            System.out.println("        itemIconMasterList has NOT been initialized.");
        }
        if(!this.spellOneIconList.isEmpty()){
            System.out.println("        spellOneIconList has been initialized.");
        }
        else{
            System.out.println("        spellOneIconList has NOT been initialized.");
        }
        if(!this.spellTwoIconList.isEmpty()){
            System.out.println("        spellTwoIconList has been initialized.");
        }
        else{
            System.out.println("        spellTwoIconList has NOT been initialized.");
        }
        System.out.println("        Kills List: " + this.killsList);
        System.out.println("        Deaths List: " + this.deathsList);
        System.out.println("        Assists List: " + this.assistsList);
        System.out.println("        Outcome List: " + this.outcomeList);
    }
    
}
