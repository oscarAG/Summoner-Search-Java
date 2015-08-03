
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Retrieve, parse, and make usable information from the JSON response corresponding to the summoner/by-name API endpoint.
 * This information includes the name of the player, profile icon id, summoner level, revision date
 * @author Oscar
 */
public class Summoner_ByName {
    
    private final LoLSearch objLoLSearch; //class object to retrieve api key
    private final String summonerName;
    private final String countryCode;
    private String version;
    
    //end values
    private String jsonName;
    private int jsonProfileIconId;
    private long jsonId;
    private long jsonSummonerLevel;
    private long jsonRevisionDate;
    private ImageIcon profileIcon;
    private boolean doesExist;
    
    public Summoner_ByName(String nameString, String cc, String recentVersion){ //arg constructor 
        //MatchHistoryPage objHistoryPage = new MatchHistoryPage();
        this.objLoLSearch = new LoLSearch();
        this.summonerName = nameString;
        this.countryCode = cc;
        this.version = recentVersion;
        getJSONResponse(); //grab json response from api endpoint
        setProfileIcon(); //using the jsonProfileIconId, get the corresponding profile icon from the data dragon url
    }
    
    //get methods
    public String getName(){    return this.jsonName;    }
    public int getProfileIconId(){  return this.jsonProfileIconId;   }
    public long getSummonerId(){ return this.jsonId;   }
    public long getSummonerLevel(){ return this.jsonSummonerLevel;    }
    public long getRevisionDate(){  return this.jsonRevisionDate;   }
    public ImageIcon getProfileIcon(){  return this.profileIcon;    }
    
    private void getJSONResponse(){
        String jsonResponse = null; //unparsed json response
        try {
            //URL
            URL url = new URL("https://" + this.countryCode + ".api.pvp.net/api/lol/" + this.countryCode + "/v1.4/summoner/by-name/" + this.summonerName + "?api_key=" + objLoLSearch.getApiKey());
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            
            parseJSONResponse(jsonResponse); //parse the json response into usable values
            this.doesExist = true;
        }catch (IOException ex) {
            this.doesExist = false;
        } 
    }
    private void parseJSONResponse(String jsonResponse){
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse); //object of the JSON
            JSONObject summonerObject = jsonObject.getJSONObject(this.summonerName);
            this.jsonId = summonerObject.getLong("id");
            this.jsonName = summonerObject.getString("name");
            this.jsonProfileIconId = summonerObject.getInt("profileIconId");
            this.jsonSummonerLevel = summonerObject.getLong("summonerLevel");
            this.jsonRevisionDate = summonerObject.getLong("revisionDate");
        } catch (JSONException ex) {
            Logger.getLogger(Summoner_ByName.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public boolean getDoesExist(){
        return this.doesExist;
    }
    private void setProfileIcon(){
        ImageIcon temp;
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/profileicon/" + this.jsonProfileIconId + ".png");
            
            BufferedImage c = ImageIO.read(url);
            temp = new ImageIcon(c);
            //resize
            Image image = temp.getImage();
            Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
            temp = new ImageIcon(newImg);
            this.profileIcon = temp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Summoner_ByName.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Summoner_ByName.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
