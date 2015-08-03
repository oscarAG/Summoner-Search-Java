
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to return the rank of the summoner searched
 * @author Oscar
 */
public class League_ById {
    LoLSearch LOL_SEARCH = new LoLSearch();
    private final String apiKey = LOL_SEARCH.getApiKey();
    private final String regionCode;
    private final long summId;
    //end values
    private String tier;
    private String division;
    
    
    public League_ById(String region, long id){//constructor
        this.regionCode = region;
        this.summId = id;
        
        getJSONResponse();
    }
    
    
    private void getJSONResponse(){
        String jsonResponse = null; //unparsed json response
        try {
            //URL
            URL url = new URL(""
                    + "https://"+ this.regionCode +".api.pvp.net/api/lol/"+ this.regionCode +"/v2.5/league/by-summoner/"+ this.summId +"/entry?api_key=" + this.apiKey);
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            parseJSONResponse(jsonResponse);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.division = "UNRANKED";
            this.tier = "";
        }
        
    }
    
    
    private void parseJSONResponse(String jsonString){
        JSONObject jsonObj = null;
        /*This array holds all 10 matches with their info.*/
        JSONArray jsonLeagueArr = null;
        try {
            String idString = Objects.toString(this.summId, null);
            jsonObj = new JSONObject(jsonString); //object of the JSON 
            jsonLeagueArr = jsonObj.getJSONArray(idString);
            
            //set end values
            this.tier = jsonLeagueArr.getJSONObject(0).getString("tier");
            JSONArray entries = jsonLeagueArr.getJSONObject(0).getJSONArray("entries");
            this.division = entries.getJSONObject(0).getString("division");
            //System.out.println(this.tier + " " + this.division);
            
            
        } catch (JSONException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getDivision(){
        return this.division;
    }
    public String getTier(){
        return this.tier;
    }
}
