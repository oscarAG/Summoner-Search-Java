
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    private final long summonerId;
    private final String regionCode;
    private String version;
    //end values
    private final ArrayList<Integer> championIdList = new ArrayList<>(); //IDs of the champions played by the searched player
    private final ArrayList<ArrayList<Integer>> itemIdMasterList = new ArrayList<>();//IDs of the items picked by the searched player for the 10 matches
    private final ArrayList<Integer> killsList = new ArrayList<>();
    private final ArrayList<Integer> assistsList = new ArrayList<>();
    private final ArrayList<Integer> deathsList = new ArrayList<>();
    private ArrayList<ArrayList<ImageIcon>> itemIconMasterList = new ArrayList<>(); //item icons of the 10 matches
    
    
    
    public Game_ById(long summId, String cc){ //arg constructor
        System.out.println("CONSTRUCTOR - Game_ById(arg, arg)");
        this.objLoLSearch = new LoLSearch();
        this.version = this.objLoLSearch.getVersion();
        this.apiKey = objLoLSearch.getApiKey();
        this.summonerId = summId;
        this.regionCode = cc;
        
        getJSONResponse();
        
        System.out.println("END - Game_ById(arg, arg)\n");
    }
    
    //get methods
    public ArrayList<Integer> getChampionIdList(){  return championIdList;   }
    
    private void getJSONResponse(){
        System.out.println("METHOD - Game_ById/getJSONResponse");
        String jsonResponse = null; //unparsed json response
        int serverResponseCode = 0; //response code of server
        try {
            //URL
            URL url = new URL(""
                    + "https://" + this.regionCode + ".api.pvp.net/api/lol/" + this.regionCode + "/v1.3/game/by-summoner/" + this.summonerId + "/recent?api_key=" + apiKey);
            //Response code
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            serverResponseCode = connection.getResponseCode();
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            System.out.println("    Successful. RC(" + serverResponseCode +")");
            
            parseJSONResponse(jsonResponse); //parse the json response into usable values
            
        } catch (MalformedURLException ex) {
            System.out.println("    MalformedURLException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check getJSONResponse()");
        } catch (IOException ex) {
            System.out.println("    IOException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check getJSONResponse()");
        }
    }
    private void parseJSONResponse(String jsonString){
        System.out.println("METHOD - Game_ById/parseJSONResponse");
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
            System.out.println("    Success.");
        } catch (JSONException ex) {
            System.out.println("    JSONException::: Error parsing JSON Object. Check parseJSONResponse(arg)");
        }
    }
    private void setChampionIdList(JSONArray array){
        System.out.println("METHOD - Game_ById/setChampionIdList(arg)");
        for(int i = 0; i < array.length(); i++){
            //get champion id from each game object
            try {
                this.championIdList.add(array.getJSONObject(i).getInt("championId"));
            } catch (JSONException ex) {
                System.out.println("    JSONException::: Error retrieving championIdList. Check setChampionIdList(arg)");
            }
            System.out.println("    Champion ID for game " + i + ": " + this.championIdList.get(i));
        }
    }
    private void setStatsObject(JSONArray array){ //set 10 stats objects to further grab information per match
        System.out.println("METHOD - Game_ById/setStatsObject()");
        ArrayList<JSONObject> statsObjects = new ArrayList<>(); //holds the 10 stats objects 
        for(int i = 0; i < array.length(); i++){ 
            try {
                //initialize the list
                statsObjects.add(array.getJSONObject(i).getJSONObject("stats"));
                this.itemIdMasterList.add(setItemIdList(statsObjects.get(i))); //add item list for this match to the master list 
                this.killsList.add(setKills(statsObjects.get(i))); //add kills for this match
                this.assistsList.add(setAssists(statsObjects.get(i))); //add assists for this match
                this.deathsList.add(setDeaths(statsObjects.get(i))); //add deaths for this match
            } catch (JSONException ex) {
                System.out.println("    JSONException::: Error setting statsObjectList. Check setStatsObject(arg)");
            }
        }
        printItemIdMasterList();
    }
    private ArrayList<Integer> setItemIdList(JSONObject stats){
        ArrayList<Integer> itemIdList = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            if(stats.has("item"+i)){
                try {
                    itemIdList.add(stats.getInt("item" + i));
                } catch (JSONException ex) {
                    System.out.println("    JSONException::: Error setting itemIdList. Check setItemIdList(arg)");
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
    public ArrayList<ArrayList<Integer>> getItemIdMasterList(){
        return this.itemIdMasterList;
    }
    private void setItemIconMasterList(){ //get images from data dragon
        System.out.println("METHOD - Game_ById/setItemIconMasterList()");
        for(int i = 0; i < this.itemIdMasterList.size(); i++){
            ArrayList<ImageIcon> tempImageList = new ArrayList<>();
            for(int j = 0; j < this.itemIdMasterList.get(i).size(); j++){
                tempImageList.add(getItemIconOf(this.itemIdMasterList.get(i).get(j))); //initialize temp list
            }
            this.itemIconMasterList.add(tempImageList); //add temp list to master list
        }
    }
    private ImageIcon getItemIconOf(int id){
        System.out.println("METHOD - Game_ById/getItemIconOf(arg)");
        int serverResponseCode = 0; //response code of server
        ImageIcon temp = null;
        if(id == 0){
            //if item slot empty
            System.out.println("    Slot empty.");
        }
        else{
            try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/item/" + id + ".png"); //link to the pic
            //Response code
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            serverResponseCode = connection.getResponseCode();
            
            BufferedImage c = ImageIO.read(url);
            temp = new ImageIcon(c);
            //resize
            Image image = temp.getImage();
            Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
            temp = new ImageIcon(newImg);
            System.out.println("    Successful. RC(" + serverResponseCode + ")");
            } catch (MalformedURLException ex) {
                System.out.println("    MalformedURLException::: Problem setting item icons. Check getItemIconOf(arg)");
            } catch (IOException ex) {
                System.out.println("    IOException::: Problem setting item icons. Check getItemIconOf(arg)");
            }
        }
        
        return temp;
    }
    public ArrayList<ArrayList<ImageIcon>> getItemIconMasterList(){
        return this.itemIconMasterList;
    }
    
    private int setKills(JSONObject stats){ //grab kills from a single match
        int kills = 0;
        if(stats.has("championsKilled")){
            try {
                kills = stats.getInt("championsKilled");
            } catch (JSONException ex) {
                System.out.println("    JSONException::: Error setting kills. Check setKills(arg)");
            }
        }
        else{
            this.killsList.add(0);
        }
        return kills;
    }
    public ArrayList<Integer> getKillsList(){
        return this.killsList;
    }
    private int setAssists(JSONObject stats){ //grab assists from a single match
        int assists = 0;
        if(stats.has("assists")){
            try {
                assists = stats.getInt("assists");
            } catch (JSONException ex) {
                System.out.println("    JSONException::: Error setting assists. Check setAssists(arg)");
            }
        }
        else{
            assists = 0;
        }
        return assists;
    }
    public ArrayList<Integer> getAssistsList(){
        return this.assistsList;
    }
    private int setDeaths(JSONObject stats){ //grab assists from a single match
        int deaths = 0;
        if(stats.has("numDeaths")){
            try {
                deaths = stats.getInt("numDeaths");
            } catch (JSONException ex) {
                System.out.println("    JSONException::: Error setting numDeaths. Check setDeaths(arg)");
            }
        }
        else{
            deaths = 0;
        }
        return deaths;
    }
    public ArrayList<Integer> getDeathsList(){
        return this.deathsList;
    }
}
