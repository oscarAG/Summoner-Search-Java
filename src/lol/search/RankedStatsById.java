
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * This class is used to return information for the ranked stats page. 
 * Two endpoints are accessed through this class: stats summary, and stats ranked
 * @author Oscar
 */
public class RankedStatsById {
    //class objects
    private LoLSearch objLolSearch = new LoLSearch();
    private AllChampionsById objAllChamps;
    //necessary inputs
    private final String version;
    private final String season;
    private final String region;
    private final long id;
    //end values
    private ArrayList<JSONObject> rankedObjs = new ArrayList<>();
    private ArrayList<String> rankedChampKeyList = new ArrayList<>();
    private boolean doesExist;
    
    public RankedStatsById(String version, long id, String region, String season){
        this.version = version;
        this.id = id;
        this.region = region;
        this.season = season;
        this.objAllChamps  = new AllChampionsById(this.region);
        try {
            //init URLs
            URL summaryURL = new URL(getURL("summary"));
            URL rankedURL = new URL(getURL("ranked"));
            
            //get json responses
            String summaryJsonString = getJSONResponse(summaryURL);
            String rankedJsonString = getJSONResponse(rankedURL);
            
            //parse responses
            this.rankedObjs = setRankedObjList(rankedJsonString);
            this.rankedChampKeyList = setRankedChampKeyList();
             
        } catch (MalformedURLException ex) {
            Logger.getLogger(RankedStatsById.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //return either the url for the summary or ranked
    private String getURL(String type){
        String str = null;
        switch (type) {
            case "ranked":
                str = "https://"+this.region+".api.pvp.net/api/lol/"+this.region+"/v1.3/stats/by-summoner/"+this.id+"/ranked?season="+this.season+"&api_key=" + objLolSearch.getApiKey();
                break;
            case "summary":
                str = "https://"+this.region+".api.pvp.net/api/lol/"+this.region+"/v1.3/stats/by-summoner/"+this.id+"/summary?season="+this.season+"&api_key=" + objLolSearch.getApiKey();
                break;
            default:
                System.out.println("!!!ERROR WITH URL STRING!!!");
        }
        return str;
    }
    
    //return json response from a URL
    private String getJSONResponse(URL url){
        String jsonResponse = null; //unparsed json response
        try {
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            doesExist = true;
        } catch (IOException ex) {
            System.out.println("Error retrieving ranked information.");
            doesExist = false;
        } 
        return jsonResponse;
    }
    public boolean doesExist(){
        return this.doesExist;
    }
    //get all champion objects from ranked response in an arraylist
    private ArrayList<JSONObject> setRankedObjList(String response){
        ArrayList<JSONObject> list = new ArrayList<>();
        if(doesExist == true){
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray array = obj.getJSONArray("champions");
                for(int i = 0; i < array.length(); i++){
                    list.add(array.getJSONObject(i));
                }
            } catch (JSONException ex) {
                Logger.getLogger(RankedStatsById.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return list;
    }
    
    //init champ keys list
    private ArrayList<String> setRankedChampKeyList(){
        ArrayList<String> list = new ArrayList<>();
        try {
            for(int i = 0; i < this.rankedObjs.size(); i++){
                list.add(this.objAllChamps.getChampKeyFromId(this.rankedObjs.get(i).getInt("id")));
            }
        } catch (JSONException ex) {
            Logger.getLogger(RankedStatsById.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    //get method
    public ArrayList<JSONObject> getObjChampRankedList(){
        return this.rankedObjs;
    }
    public ArrayList<String> getRankedChampKeyList(){
        return this.rankedChampKeyList;
    }
    
    //print some values
    public void printValues(){
        System.out.println("Ranked stats class has been called.\n" +
                           "The values set for use with the ranked stats class are: \n" + 
                           "    Id: " + this.id + "\n" + 
                           "    Region: " + this.region + "\n" + 
                           "    Season: " + this.season + "\n");
    }
    public ImageIcon getChampionIconOf(String champKey){
        ImageIcon temp = null;
        File f = new File("assets\\championIcons\\" + champKey + ".png");
        if(f.isFile()){ //check if picture exists
            try {
                BufferedImage c = ImageIO.read(new File("assets\\championIcons\\" + champKey + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(55,55,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        else{
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + this.version + "/img/champion/"+champKey+".png"); //link to the pic
                BufferedImage c = ImageIO.read(url);
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(55,55,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
                ImageIO.write(c, "png",new File("assets\\championIcons\\" + champKey + ".png")); //save to directory if it doesnt exist
                System.out.println(champKey + " saved to directory.");
            } catch (MalformedURLException ex) {
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("Error for: " + champKey);
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return temp;
    }
}
