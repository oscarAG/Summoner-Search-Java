
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to return information for the ranked stats page. 
 * Two endpoints are accessed through this class: stats summary, and stats ranked
 * @author Oscar
 */
public class RankedStatsById {
    //class objects
    private LoLSearch objLolSearch = new LoLSearch();
    //necessary inputs
    private final String season;
    private final String region;
    private final long id;
    
    public RankedStatsById(long id, String region, String season){
        this.id = id;
        this.region = region;
        this.season = season;
        
        try {
            //init URLs
            URL summaryURL = new URL(getURL("summary"));
            URL rankedURL = new URL(getURL("ranked"));
            
            //get json responses
            String summaryJsonString = getJSONResponse(summaryURL);
            String rankedJsonString = getJSONResponse(rankedURL);
            
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
            //parseJSONResponse(jsonResponse); //parse the json response into usable values
            //this.doesExist = true;
        } catch (IOException ex) {
            Logger.getLogger(RankedStatsById.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonResponse;
    }
    
    //print some values
    public void printValues(){
        System.out.println("Ranked stats class has been called.\n" +
                           "The values set for use with the ranked stats class are: \n" + 
                           "    Id: " + this.id + "\n" + 
                           "    Region: " + this.region + "\n" + 
                           "    Season: " + this.season + "\n");
    }
}
