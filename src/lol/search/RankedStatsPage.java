/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.search;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Oscar
 */
public class RankedStatsPage {
    //class objects
    private final GameStaticData objGameStaticData;
    private RankedStatsById objRankedStats;
    //carried over values
    private final String summonerName;
    private final String region;
    private final long summonerId;
    private final long summonerLevel;
    //values for this class
    private final JFrame masterFrame;
    private String season;
    
    public RankedStatsPage(JFrame frame, String region, Summoner_ByName objSummByName){ //constructor
        this.objGameStaticData = new GameStaticData();
        this.summonerName = objSummByName.getName();
        this.summonerId = objSummByName.getSummonerId();
        this.summonerLevel = objSummByName.getSummonerLevel();
        this.region = region;
        
        //set the background of the frame
        this.masterFrame = frame;
        this.masterFrame.add(getBackground());
        //printCarriedValues();
        
        setSeason();
        rankedStatsClassOperations(this.summonerId, this.region, this.season);
        this.objRankedStats.printValues(); //print values carried over to the ranked stats class
        
        frameRefresh(this.masterFrame);
    }
    
    private void printCarriedValues(){
        System.out.println("Carried values from operations:\n"+
                           "    Name: " + this.summonerName + "\n" +
                           "    Id: " + this.summonerId + "\n" +
                           "    Level: " + this.summonerLevel + "\n");
    }
    
    private void frameRefresh(JFrame frame){
        frame.revalidate();
        frame.repaint();
    }
    
    /*get/set background*/
    private JLabel getBackground(){ 
        JLabel background = new JLabel(this.objGameStaticData.getBackgroundImageIcon());
        background.setLayout(new GridLayout());
        background.add(mainPanel());
        return background;
    }
    
    /*main panel to be added to background label*/
    private JPanel mainPanel(){ 
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new FlowLayout()); //add panels to this
        //mainPanel.add(bodyPanel());
        return mainPanel;
    }
    
    /*creates an object of the ranked stats by id class, and sets the necessary variables*/
    private void rankedStatsClassOperations(long id, String region, String season){
        objRankedStats = new RankedStatsById(id, region, season);
    }
    /*set the season by which to retrieve ranked information from, for now it is 2015 by default*/
    private void setSeason(){
        this.season = "SEASON2015";
    }
}
