
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Retrieve, parse, and make usable information from the JSON response corresponding to the Game/by summoner id/recent API endpoint.
 * This information includes information about each individual game, such as the fellow players, KDA, champions picked, etc.
 * @author Oscar
 */
public class Game_ById {
    //class objects
    private final LoLSearch objLoLSearch;
    //general variables
    private final String apiKey;
    private final String regionCode;
    private final String version;
    private final long summonerId;
    
    //end values
    private final ArrayList<Integer>    championIdList = new ArrayList<>(); //IDs of the champions played by the searched player
    private final ArrayList<Integer>    killsList = new ArrayList<>();
    private final ArrayList<Integer>    assistsList = new ArrayList<>();
    private final ArrayList<Integer>    deathsList = new ArrayList<>();
    private final ArrayList<Integer>    spellOneList = new ArrayList<>();
    private final ArrayList<Integer>    spellTwoList = new ArrayList<>();
    private final ArrayList<Integer>    goldEarnedList = new ArrayList<>();
    private final ArrayList<Integer>    minionsKilledList = new ArrayList<>();
    private final ArrayList<ImageIcon>  spellOneIconList = new ArrayList<>();
    private final ArrayList<ImageIcon>  spellTwoIconList = new ArrayList<>();
    private final ArrayList<Boolean>    outcomeList = new ArrayList<>();
    private final ArrayList<Long>       dateCreatedMilli = new ArrayList<>();
    private final ArrayList<String>     gameModeList = new ArrayList<>();
    private final ArrayList<String>     subTypeList = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>>     itemIdMasterList = new ArrayList<>();//IDs of the items picked by the searched player for the 10 matches
    private final ArrayList<ArrayList<ImageIcon>>   itemIconMasterList = new ArrayList<>(); //item icons of the 10 matches
    
    public Game_ById(long summId, String cc, String recentVersion){ //arg constructor
        this.objLoLSearch = new LoLSearch();
        this.version = recentVersion;
        this.apiKey = objLoLSearch.getApiKey();
        this.summonerId = summId;
        this.regionCode = cc;
        
        getJSONResponse();
        
    }
    
    /*
        GET METHODS
    */
    public ArrayList<Integer> getChampionIdList(){      return this.championIdList;     }
    public ArrayList<Integer> getSpellOneList(){        return this.spellOneList;       }
    public ArrayList<Integer> getSpellTwoList(){        return this.spellTwoList;       }
    public ArrayList<Integer> getKillsList(){           return this.killsList;          }
    public ArrayList<Integer> getAssistsList(){         return this.assistsList;        }
    public ArrayList<Integer> getDeathsList(){          return this.deathsList;         }
    public ArrayList<ImageIcon> getSpellOneIconList(){  return this.spellOneIconList;   }
    public ArrayList<ImageIcon> getSpellTwoIconList(){  return this.spellTwoIconList;   }
    public ArrayList<String> getSubTypeList(){          return this.subTypeList;        }
    public ArrayList<String> getGameModeList(){         return this.gameModeList;       }
    public ArrayList<Long> getDateCreatedLongList(){    return this.dateCreatedMilli;   }
    public ArrayList<ArrayList<Integer>> getItemIdMasterList(){     return this.itemIdMasterList;   }
    public ArrayList<ArrayList<ImageIcon>> getItemIconMasterList(){ return this.itemIconMasterList; }
    
    
    /*
        JSON METHODS
    */
    private void getJSONResponse(){
        String jsonResponse = null; //unparsed json response
        try {
            //URL
            URL url = new URL(""
                    + "https://" + this.regionCode + ".api.pvp.net/api/lol/" + this.regionCode + "/v1.3/game/by-summoner/" + this.summonerId + "/recent?api_key=" + apiKey);
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            parseJSONResponse(jsonResponse); //parse the json response into usable values
        } catch (MalformedURLException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void parseJSONResponse(String jsonString){
        JSONObject jsonObj = null;
        /*This array holds all 10 matches with their info.*/
        JSONArray jsonGamesArray = null;
        try {
            jsonObj = new JSONObject(jsonString); //object of the JSON 
            jsonGamesArray = jsonObj.getJSONArray("games");

            //set end values
            setChampionIdList(jsonGamesArray); //set champion id list end value
            setStatsObject(jsonGamesArray); //set stats object 
            setItemIconMasterList(); //get item pictures
            setSpellOneList(jsonGamesArray); //set spell one list
            setSpellTwoList(jsonGamesArray); //set spell two list
            setDateCreatedList(jsonGamesArray); //set date created list
            setGameModeList(jsonGamesArray); //set game mode list
            setSubTypeList(jsonGamesArray); //set sub type list
        } catch (JSONException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
        SET METHODS
    */
    private void setSubTypeList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            try {
                String subType = array.getJSONObject(i).getString("subType");
                subType = subType.replaceAll("_", " "); //replace underscores with spaces
                this.subTypeList.add(subType);
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setGameModeList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            try {
                String gameMode = array.getJSONObject(i).getString("gameMode");
                gameMode = gameMode.replace("_", " "); //replace underscores with spaces
                this.gameModeList.add(gameMode);
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setDateCreatedList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            try {
                this.dateCreatedMilli.add(array.getJSONObject(i).getLong("createDate"));
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setSpellOneList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            //get champion id from each game object
            try {
                this.spellOneList.add(array.getJSONObject(i).getInt("spell1"));
                
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } 
            this.spellOneIconList.add(setSpellIconOf(this.spellOneList.get(i)));
        }
    }
    
    private void setSpellTwoList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            //get champion id from each game object
            try {
                this.spellTwoList.add(array.getJSONObject(i).getInt("spell2"));
                this.spellTwoIconList.add(setSpellIconOf(this.spellTwoList.get(i)));
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setChampionIdList(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            //get champion id from each game object
            try {
                this.championIdList.add(array.getJSONObject(i).getInt("championId"));
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    private ImageIcon setSpellIconOf(int id){ //sets the spell icon of a specific item id
        String tempSpellKey = getSummonerSpellKeyOfId(id);
        ImageIcon temp = null;
        File f = new File("assets\\spellIcons\\" + tempSpellKey + ".png");
        if(f.isFile()){ //check if picture exists
            try {
                BufferedImage c = ImageIO.read(new File("assets\\spellIcons\\" + tempSpellKey + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(23,23,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        else{
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/5.14.1/img/spell/" + tempSpellKey + ".png"); //link to the pic
                BufferedImage c = ImageIO.read(url);
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(23,23,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        return temp;
    }
    private String getSummonerSpellKeyOfId(int id){
        String jsonResponse = "";
        String summonerSpellKey = "";
        try {
            URL url = new URL("https://global.api.pvp.net/api/lol/static-data/"+this.regionCode+"/v1.2/summoner-spell/" + id + "?spellData=key&api_key=" + this.apiKey);
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            JSONObject jsonObject = new JSONObject(jsonResponse); //object of the JSON
            summonerSpellKey = jsonObject.getString("key");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        }
        return summonerSpellKey;
    }
    
    private void setStatsObject(JSONArray array){ //set 10 stats objects to further grab information per match
        ArrayList<JSONObject> statsObjects = new ArrayList<>(); //holds the 10 stats objects 
        for(int i = 0; i < array.length(); i++){ 
            try {
                //initialize the list
                statsObjects.add(array.getJSONObject(i).getJSONObject("stats"));
                this.itemIdMasterList.add(setItemIdList(statsObjects.get(i))); //add item list for this match to the master list 
                this.killsList.add(setKills(statsObjects.get(i))); //add kills for this match
                this.assistsList.add(setAssists(statsObjects.get(i))); //add assists for this match
                this.deathsList.add(setDeaths(statsObjects.get(i))); //add deaths for this match
                this.outcomeList.add(outcomeOfGame(statsObjects.get(i))); //add outcome for this match
                this.goldEarnedList.add(getGoldEarned(statsObjects.get(i))); //add gold earned for this match
                this.minionsKilledList.add(getMinionsKilled(statsObjects.get(i))); //add minions killed for this match
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //printItemIdMasterList();
    }
    private int getMinionsKilled(JSONObject stats){
        int minions = 0;
        if(stats.has("minionsKilled")){
            try {
                minions = stats.getInt("minionsKilled");
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return minions;
    }
    public ArrayList<Integer> getMinionsKilled(){
        return this.minionsKilledList;
    }
    public ArrayList<Integer> getGoldEarnedList(){
        return this.goldEarnedList;
    }
    private int getGoldEarned(JSONObject stats){
        int goldEarned = 0;
        if(stats.has("goldEarned")){
            try {
                goldEarned = stats.getInt("goldEarned");
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return goldEarned;
    }
    private boolean outcomeOfGame(JSONObject stats){
        boolean outcome = false;
        try {
            outcome = stats.getBoolean("win");
        } catch (JSONException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outcome;
    }
    public ArrayList<Boolean> getOutcomeList(){
        return this.outcomeList;
    }
    private ArrayList<Integer> setItemIdList(JSONObject stats){
        ArrayList<Integer> itemIdList = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            if(stats.has("item"+i)){
                try {
                    itemIdList.add(stats.getInt("item" + i));
                } catch (JSONException ex) {
                    Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            else{
                itemIdList.add(0);
            }
        }
        return itemIdList;
    }
    private void printItemIdMasterList(){
        for(int i = 0; i < this.itemIdMasterList.size(); i++){
            System.out.println("Items for game " + i + ":");
            System.out.println(this.itemIdMasterList.get(i));
        }
    }
    
    private void setItemIconMasterList(){ //get images from data dragon
        for(int i = 0; i < this.itemIdMasterList.size(); i++){
            ArrayList<ImageIcon> tempImageList = new ArrayList<>();
            for(int j = 0; j < this.itemIdMasterList.get(i).size(); j++){
                tempImageList.add(getItemIconOf(this.itemIdMasterList.get(i).get(j))); //initialize temp list
            }
            this.itemIconMasterList.add(tempImageList); //add temp list to master list
        }
    }
    private ImageIcon getItemIconOf(int id){
        ImageIcon temp = null;
        File f = new File("assets\\itemIcons\\" + id + ".png");
        if(f.isFile()){ //check if picture exists in folder
            try {
                BufferedImage c = ImageIO.read(new File("assets\\itemIcons\\" + id + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(43,43,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        else{ //if it does not exist in a folder... try to grab from internet with most recent version
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/item/" + id + ".png"); //link to the pic
                BufferedImage c = ImageIO.read(url);
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
                ImageIO.write(c, "png",new File("assets\\itemIcons\\" + id + ".png")); //save to directory if it doesnt exist
                System.out.println(id + " saved to directory.");
            } catch (ProtocolException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                try { //if most recent version does not return an item, try earlier version
                    System.out.println("Error grabbing item " + id + " from internet. Trying earlier version...");
                    URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + "5.1.1" + "/img/item/" + id + ".png"); //link to the pic
                    BufferedImage c = ImageIO.read(url);
                    temp = new ImageIcon(c);
                    //resize
                    Image image = temp.getImage();
                    Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
                    temp = new ImageIcon(newImg);
                    ImageIO.write(c, "png",new File("assets\\itemIcons\\" + id + ".png")); //save to directory if it doesnt exist
                    System.out.println(id + " saved to directory.");
                    System.out.println("Item successfully taken from internet.");
                } catch (MalformedURLException ex1) {
                    Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (IOException ex1) {
                    try { //if most recent version does not return an item, try earlier version
                        System.out.println("Error grabbing item " + id + " from internet. Trying earlier version...");
                        URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + "4.1.2" + "/img/item/" + id + ".png"); //link to the pic
                        BufferedImage c = ImageIO.read(url);
                        temp = new ImageIcon(c);
                        //resize
                        Image image = temp.getImage();
                        Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
                        temp = new ImageIcon(newImg);
                        ImageIO.write(c, "png",new File("assets\\itemIcons\\" + id + ".png")); //save to directory if it doesnt exist
                        System.out.println(id + " saved to directory.");
                        System.out.println("Item successfully taken from internet.");
                    } catch (IOException ex2) {
                        Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
            }
        }
        
        return temp;
    }
    
    private int setKills(JSONObject stats){ //grab kills from a single match
        int kills = 0;
        if(stats.has("championsKilled")){
            try {
                kills = stats.getInt("championsKilled");
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return kills;
    }
    
    private int setAssists(JSONObject stats){ //grab assists from a single match
        int assists = 0;
        if(stats.has("assists")){
            try {
                assists = stats.getInt("assists");
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return assists;
    }
    
    private int setDeaths(JSONObject stats){ //grab assists from a single match
        int deaths = 0;
        if(stats.has("numDeaths")){
            try {
                deaths = stats.getInt("numDeaths");
            } catch (JSONException ex) {
                Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return deaths;
    }
}
