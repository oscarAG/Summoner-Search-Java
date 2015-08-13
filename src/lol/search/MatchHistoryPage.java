
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
import java.util.Date;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    private String version;
    //values taken from other classes
    private final JFrame masterFrame;
    private final String summonerName;
    private final long summonerLevel;
    private final ArrayList<ImageIcon> championIcons;
    private final ArrayList<ImageIcon> spellOneIconList;
    private final ArrayList<ImageIcon> spellTwoIconList;
    private final ArrayList<ArrayList<Integer>> itemIdMasterList; //list with lists of items picked in each match
    private final ArrayList<Integer> killsList; //list with the kills from the 10 matches
    private final ArrayList<Integer> assistsList; //list with the assists from the 10 matches
    private final ArrayList<Integer> deathsList; //list with the deaths from the 10 matches
    private final ArrayList<Integer> goldList;
    private final ArrayList<Integer> minionsKilledList;
    private final ArrayList<ArrayList<ImageIcon>> itemIconMasterList;
    private final ImageIcon profileIcon;
    private final ArrayList<Boolean> outcomeList;
    private final ArrayList<Long> dateCreatedEpochList;
    private final ArrayList<String> subTypeList;
    private final ArrayList<String> gameModeList;
    private final ArrayList<String> dateStringList = new ArrayList<>();
    private final String tier;
    private final String division;
    
    //public MatchHistoryPage(){} //empty constructor
    public MatchHistoryPage(String regionCode,JFrame frame, Summoner_ByName objSBN, Game_ById objGBI, LoLStaticData_AllChampions objAC, League_ById objL){
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
        this.dateCreatedEpochList = objGBI.getDateCreatedLongList();
        this.subTypeList = objGBI.getSubTypeList();
        this.gameModeList = objGBI.getGameModeList();
        this.tier = objL.getTier();
        this.division = objL.getDivision();
        
        setConvertedDateList(this.dateCreatedEpochList);
        
        //printCarriedValues(); //check to confirm necessary values have been carried over successfully
        
        /*Set background*/
        JLabel backgroundLabel = new JLabel(objGameStaticData.getBackgroundImageIcon());
        backgroundLabel.setLayout(new GridLayout());
        backgroundLabel.add(mainPanel());
        
        this.masterFrame.add(backgroundLabel);
        this.masterFrame.revalidate();
        System.out.println("Done.");
    }
    private void setConvertedDateList(ArrayList<Long> epDate){ //change epoch long date into formatted string
        Date realDate;
        for(int i = 0; i < epDate.size(); i++){
            realDate = new Date(epDate.get(i));
            String realDateString = realDate.toString();
            String[] parts = realDateString.split(" ");
            String newDate = parts[1] + " " + parts[2] + ", " + parts[5];
            this.dateStringList.add(newDate);
        }
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
            headerPanel.setLayout(new BorderLayout());
            //headerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            headerPanel.setBackground(new Color(0,0,0,170));
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
                centerPanel.setLayout(new FlowLayout());
                centerPanel.setOpaque(false);
                //profile icon
                JLabel profileIconLabel = new JLabel(this.profileIcon);
                centerPanel.add(profileIconLabel);
                //summoner name
                JLabel summonerNameLabel = new JLabel(this.summonerName);
                summonerNameLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 20)); //custom font
                summonerNameLabel.setForeground(Color.WHITE); //text color
                JPanel namePanel = new JPanel();
                namePanel.setOpaque(false);
                namePanel.add(summonerNameLabel);
                centerPanel.add(namePanel);
                //empty panel
                JPanel emptyPanel = new JPanel();
                JLabel emptyLabel = new JLabel("-");
                emptyPanel.add(emptyLabel);
                emptyLabel.setForeground(new Color(0,0,0,0));
                emptyPanel.setOpaque(false);
                //centerPanel.add(emptyPanel);
                //summoner level
                String level = String.valueOf(this.summonerLevel);
                JLabel summonerLevelLabel = new JLabel("Level: "+ level);
                summonerLevelLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
                summonerLevelLabel.setForeground(Color.WHITE); //text color
                JPanel levelPanel = new JPanel();
                levelPanel.setOpaque(false);
                levelPanel.add(summonerLevelLabel);
                //centerPanel.add(levelPanel);
                //tier
                JLabel tierLabel = new JLabel(this.tier);
                tierLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
                tierLabel.setForeground(new Color(219,219,219)); //text color
                centerPanel.add(tierLabel);
                //division
                JLabel divisionLabel = new JLabel(this.division);
                divisionLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
                divisionLabel.setForeground(new Color(219,219,219)); //text color
                centerPanel.add(divisionLabel);
                
                headerPanel.add(centerPanel, BorderLayout.CENTER);
                //empty panel to balance right side
                JPanel ee = new JPanel();
                ee.setOpaque(false);
                ee.setPreferredSize(new Dimension(110,50));
                headerPanel.add(ee, BorderLayout.LINE_END);
                return headerPanel;
    }
    
    private JPanel bodyPanel(){
        ArrayList<JPanel> panelArrList = new ArrayList<>();
        //body, which holds the 10 recent matches
        JPanel bodyPanel = new JPanel();
        bodyPanel.setPreferredSize(bodyDimension); //dimension of the body panel
        //bodyPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        bodyPanel.setOpaque(false);
        ImageIcon scoreIcon = this.objGameStaticData.getScoreboardIconOf("score");
        ImageIcon goldIcon = this.objGameStaticData.getScoreboardIconOf("gold");
        ImageIcon minionIcon = this.objGameStaticData.getScoreboardIconOf("minion");
        for(int i = 0; i < this.championIcons.size(); i ++){ //loop for the 10 maches
            JPanel matchPanel = new JPanel();
            matchPanel.setLayout(new FlowLayout()); //layout of each ind match
            matchPanel.setBackground(new Color(0,0,0,170));
            matchPanel.setPreferredSize(matchDimension);
            //mode, sub type
            JPanel modeSubPanel = new JPanel();
            modeSubPanel.setPreferredSize(new Dimension(170,40));
            //modeSubPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            modeSubPanel.setOpaque(false);
            modeSubPanel.setLayout(new BoxLayout(modeSubPanel, BoxLayout.Y_AXIS));
            JLabel modeLabel = new JLabel(this.gameModeList.get(i));
            modeLabel.setForeground(Color.WHITE);
            modeLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
            JLabel subTypeLabel = new JLabel(this.subTypeList.get(i));
            subTypeLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 13)); //custom font
            subTypeLabel.setForeground(Color.WHITE);
            modeSubPanel.add(modeLabel);
            modeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            modeSubPanel.add(subTypeLabel);
            subTypeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            matchPanel.add(modeSubPanel);
            //spacer panel
            JLabel spacer = new JLabel("----");
            spacer.setForeground(new Color(0,0,0,0));
            matchPanel.add(spacer);
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
            scoreMasterPanel.setPreferredSize(new Dimension(90,40));
            JPanel scoreIconPanel = new JPanel(); //holds the crossed swords icon
            //scoreIconPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            //scoreMasterPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            scoreIconPanel.setLayout(new BoxLayout(scoreIconPanel, BoxLayout.X_AXIS));
            scoreIconPanel.setOpaque(false);
            JLabel scoreLabel = new JLabel(scoreIcon);
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
            JLabel bagLabel = new JLabel(goldIcon);
            goldPanel.add(bagLabel);
            double tempGoldValue = (double)(this.goldList.get(i)) / 1000;
            String goldString = new DecimalFormat("###.#").format(tempGoldValue); 
            JLabel goldLabel = new JLabel(goldString + "k");
            goldLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            goldLabel.setForeground(Color.WHITE);
            goldPanel.add(goldLabel);
            goldMasterPanel.add(goldPanel);
            
            //minions icon
            JPanel minionMasterPanel = new JPanel();
            minionMasterPanel.setOpaque(false);
            minionMasterPanel.setLayout(new BoxLayout(minionMasterPanel, BoxLayout.X_AXIS));
            minionMasterPanel.setPreferredSize(new Dimension(64,40));
            JPanel minionPanel = new JPanel();
            //minionPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            minionPanel.setLayout(new BoxLayout(minionPanel, BoxLayout.X_AXIS));
            minionPanel.setOpaque(false);
            JLabel minionLabel = new JLabel(minionIcon);
            minionPanel.add(minionLabel);
            int minionsKilledTemp = this.minionsKilledList.get(i);
            JLabel minionValueLabel = new JLabel(minionsKilledTemp + "");
            minionValueLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            minionValueLabel.setForeground(Color.WHITE);
            minionPanel.add(minionValueLabel);
            minionMasterPanel.add(minionPanel);
            
            //date
            JPanel datePanel = new JPanel();
            //datePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            datePanel.setPreferredSize(new Dimension(110,30));
            datePanel.setOpaque(false);
            JLabel dateLabel = new JLabel(this.dateStringList.get(i));
            dateLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 15)); //custom font
            dateLabel.setForeground(Color.WHITE);
            datePanel.add(dateLabel);

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
            matchPanel.add(datePanel);
            
            panelArrList.add(matchPanel);
        }
        for(int i = 0; i < panelArrList.size(); i++){
            bodyPanel.add(panelArrList.get(i));
        }

        return bodyPanel;
    }
    private void printCarriedValues(){
        System.out.println("Name: " + this.summonerName+
                            "\nLevel: " + this.summonerLevel+
                            "\nKills List: " + this.killsList+
                            "\nDeaths List: " + this.deathsList+
                            "\nAssists List: " + this.assistsList+
                            "\nOutcome List: " + this.outcomeList+
                            "\nMinions List: " + this.minionsKilledList+
                            "\nGold List: " + this.goldList+
                            "\nSub Type List: " + this.subTypeList+
                            "\nGame Mode List: " + this.gameModeList+
                            "\nDate Created Epoch List: " + this.dateCreatedEpochList);
        if(!this.championIcons.isEmpty()){
            System.out.println("championIcons has been initialized.");
        }
        else{
            System.out.println("championIcons has NOT been initialized.");
        }
        if(!this.itemIconMasterList.isEmpty()){
            System.out.println("itemIconMasterList has been initialized.");
        }
        else{
            System.out.println("itemIconMasterList has NOT been initialized.");
        }
        if(!this.spellOneIconList.isEmpty()){
            System.out.println("spellOneIconList has been initialized.");
        }
        else{
            System.out.println("spellOneIconList has NOT been initialized.");
        }
        if(!this.spellTwoIconList.isEmpty()){
            System.out.println("spellTwoIconList has been initialized.");
        }
        else{
            System.out.println("spellTwoIconList has NOT been initialized.");
        }
    }
    
}
