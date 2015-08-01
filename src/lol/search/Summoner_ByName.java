
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
    private final String version;
    
    //end values
    private String jsonName;
    private int jsonProfileIconId;
    private long jsonId;
    private long jsonSummonerLevel;
    private long jsonRevisionDate;
    private ImageIcon profileIcon;
    
    public Summoner_ByName(String nameString, String cc){ //arg constructor 
        System.out.println("CONSTRUCTOR - Summoner_ByName(arg, arg)");
        //MatchHistoryPage objHistoryPage = new MatchHistoryPage();
        this.objLoLSearch = new LoLSearch();
        this.version = this.objLoLSearch.getVersion();
        this.summonerName = nameString;
        this.countryCode = cc;
        
        getJSONResponse(); //grab json response from api endpoint
        setProfileIcon(); //using the jsonProfileIconId, get the corresponding profile icon from the data dragon url
        System.out.println("END - Summoner_ByName(arg, arg)");
    }
    
    //get methods
    public String getName(){    return this.jsonName;    }
    public int getProfileIconId(){  return this.jsonProfileIconId;   }
    public long getSummonerId(){ return this.jsonId;   }
    public long getSummonerLevel(){ return this.jsonSummonerLevel;    }
    public long getRevisionDate(){  return this.jsonRevisionDate;   }
    public ImageIcon getProfileIcon(){  return this.profileIcon;    }
    
    private void getJSONResponse(){
        System.out.println("METHOD - Summoner_ByName/getJSONResponse");
        String jsonResponse = null; //unparsed json response
        int serverResponseCode = 0; //response code of server
        try {
            //URL
            URL url = new URL("https://" + this.countryCode + ".api.pvp.net/api/lol/" + this.countryCode + "/v1.4/summoner/by-name/" + this.summonerName + "?api_key=" + objLoLSearch.getApiKey());
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
    private void parseJSONResponse(String jsonResponse){
        System.out.println("METHOD - Summoner_ByName/parseJSONResponse");
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse); //object of the JSON
            JSONObject summonerObject = jsonObject.getJSONObject(this.summonerName);
            this.jsonId = summonerObject.getLong("id");
            this.jsonName = summonerObject.getString("name");
            this.jsonProfileIconId = summonerObject.getInt("profileIconId");
            this.jsonSummonerLevel = summonerObject.getLong("summonerLevel");
            this.jsonRevisionDate = summonerObject.getLong("revisionDate");
            System.out.println("    Success.");
            //print the information to confirm accuracy
            System.out.println("    ID: " + getSummonerId() + "\n" +
                               "    Name: " + getName() + "\n" +
                               "    ProfileIconId: " + getProfileIconId() + "\n" + 
                               "    SummonerLevel: " + getSummonerLevel() + "\n" +
                               "    RevisionDate: " + getRevisionDate());
        } catch (JSONException ex) {
            System.out.println("    JSONException::: Error retrieving JSON information. Check parseJSONResponse()");
        }
    }
    
    private void setProfileIcon(){
        System.out.println("METHOD - Summoner_ByName/setProfileIcon()");
        ImageIcon temp;
        int serverResponseCode = 0;
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/profileicon/" + this.jsonProfileIconId + ".png");
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
            this.profileIcon = temp;
            System.out.println("    Successful. RC(" + serverResponseCode + ")");
        } catch (MalformedURLException ex) {
            System.out.println("    MalformedURLException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check setProfileIcon()");
        } catch (IOException ex) {
            System.out.println("    IOException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check getJSONResponse()");
        }
    }
    
}
