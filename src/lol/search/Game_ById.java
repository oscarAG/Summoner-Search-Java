
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final String countryCode;
    //end values
    private final ArrayList<Integer> championIdList = new ArrayList<>(); //IDs of the champions played by the searched player
    
    public Game_ById(long summId, String cc){ //arg constructor
        System.out.println("CONSTRUCTOR - Game_ById(arg, arg)");
        this.objLoLSearch = new LoLSearch();
        this.apiKey = objLoLSearch.getApiKey();
        this.summonerId = summId;
        this.countryCode = cc;
        
        getJSONResponse();
        
        System.out.println("END - Game_ById(arg, arg)");
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
                    + "https://" + this.countryCode + ".api.pvp.net/api/lol/" + this.countryCode + "/v1.3/game/by-summoner/25148999/recent?api_key=" + apiKey);
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
}
