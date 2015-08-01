
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This endpoint receives and makes usable information about all champions from the static data champion endpoint. 
 * This information can include the key name, images, etc. The benefit of this is that you can retrieve the information all at once and parse as needed.
 * Instead of making 10 individual calls. 
 * @author Oscar
 */
public class LoLStaticData_AllChampions {
    private LoLSearch objLoLSearch;
    private final ArrayList<Integer> championIdList;
    private final String regionCode;
    private String version;
    
    //end values
    private ArrayList<String> championKeys = new ArrayList<>();
    private ArrayList<ImageIcon> championIcons = new ArrayList<>();
    
    public LoLStaticData_AllChampions(ArrayList<Integer> champArr, String cc){
        System.out.println("CONSTRUCTOR - LoLStaticData_AllChampions(arg, arg)");
        this.objLoLSearch = new LoLSearch();
        this.version = objLoLSearch.getVersion();
        this.championIdList = champArr;
        this.regionCode = cc;
        
        getJSONResponse(); //grab json response from api
        
        System.out.println("END - LoLStaticData_AllChampions(arg, arg)\n");
    }
    private void getJSONResponse(){ /*IMPORTANT - This url call only brings back information with championdata focused on returning an image, not all the stats per champion*/
        System.out.println("METHOD - LoLStaticData_AllChampions/getJSONResponse");
        String jsonResponse = null; //unparsed json response
        int serverResponseCode = 0; //response code of server
        try { 
            //URL
            URL url = new URL(""
                    + "https://global.api.pvp.net/api/lol/static-data/" + this.regionCode  + "/v1.2/champion?champData=image&api_key=" + this.objLoLSearch.getApiKey());
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
    private void parseJSONResponse(String response){
        System.out.println("METHOD - LoLStaticData_AllChampions/parseJSONResponse");
        try {
            JSONObject jsonObj = new JSONObject(response); //object of the JSON
            JSONObject dataObj = jsonObj.getJSONObject("data"); //holds all champion objects
            getChampionInfoFromIds(dataObj);
            setChampionIcons(); //set champion icons from the champ keys
            System.out.println("    JSON Response has completed parsing.");
        } catch (JSONException ex) {
            System.out.println("    JSONException::: Error parsing JSON object. Check parseJSONResponse");
        }
    }
    
    private void getChampionInfoFromIds(JSONObject object){
        System.out.println("METHOD - LoLStaticData_AllChampions/getChampionKeysFromids(arg)");
        int counter = 0;
        //traverse through the entire array, placing the champion into a temporary and checking to see if ID matches
        for(int i = 0; i < this.championIdList.size(); i++){ //traverse through the champion id list
            //print information regarding dataObj for testing
            Iterator<?> keys = object.keys(); //iterator to traverse through entire champion list
            while(keys.hasNext()){ //operations per instance of each champion object
                try {
                    String key = (String)keys.next();
                    if(object.get(key) instanceof JSONObject){
                        JSONObject champObject = object.getJSONObject(key);
                        //compare the id of the current champion to the id of the champion played by the player
                        if(champObject.getInt("id") == this.championIdList.get(i)){ 
                            this.championKeys.add(champObject.getString("key")); //if equal, add key to list
                        }
                    }
                } catch (JSONException ex) {
                    System.out.println("    JSONException::: Error with JSON object. Check getChampionKeysFromIds");
                }
            }
            counter++;
        }
        System.out.println("    Objects in data JSON Object: " + counter); //paola rocks my socks 
        printChampionKeys();
    }
    private void printChampionKeys(){
        for(int i = 0; i < this.championKeys.size(); i++){
            System.out.println("    Champion Key for match " + i + ": " + this.championKeys.get(i));
        }
    }
    public ArrayList<String> getChampionKeys(){
        return this.championKeys;
    }
    private void setChampionIcons(){
        for(int i = 0; i < this.championKeys.size(); i++){
            this.championIcons.add(getChampionIconOf(this.championKeys.get(i)));
        }
    }
    public ArrayList<ImageIcon> getChampionIcons(){
        return this.championIcons;
    }
    private ImageIcon getChampionIconOf(String champKey){
        System.out.println("METHOD - LoLStaticData_AllChampions/getChampionIconOf(arg)");
        int serverResponseCode = 0; //response code of server
        ImageIcon temp = null;
        File f = new File("assets\\championIcons\\" + champKey + ".png");
        if(f.isFile()){ //check if picture exists
            System.out.println("    " + champKey + " file exists.");
            try {
                BufferedImage c = ImageIO.read(new File("assets\\championIcons\\" + champKey + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(46,46,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
                System.out.println("    Success.");
            } catch (MalformedURLException ex) {
                System.out.println("    Could not get picture from file. Check getChampionIconOf");
            } catch (IOException ex) {
                System.out.println("    Could not get picture from file. Check getChampionIconOf");
            }
        }
        else{
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/champion/"+champKey+".png"); //link to the pic
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
                System.out.println("    MalformedURLException::: Problem setting champion icons. Check getChampionIconOf(arg)");
            } catch (IOException ex) {
                System.out.println("    IOException::: Problem setting champion icons. Check getChampionIconOf(arg)");
            }
        }
        
        return temp;
    }
}
