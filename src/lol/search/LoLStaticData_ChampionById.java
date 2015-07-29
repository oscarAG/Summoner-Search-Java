
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This endpoint receives and makes usable information about an individual champion from the static data champion-by-id endpoint. 
 * This information can include the key name, id, image, lore, spells, etc. 
 * @author Oscar
 */
public class LoLStaticData_ChampionById {
    
    private final LoLSearch objLoLSearch;
    //general variables
    private final String apiKey;
    private final String countryCode;
    private final long champId;
    
    public LoLStaticData_ChampionById(long id, String cc){
        System.out.println("CONSTRUCTOR - LoLStaticData_ChampionById(arg, arg)");
        this.objLoLSearch = new LoLSearch();
        this.countryCode = cc;
        this.apiKey = objLoLSearch.getApiKey();
        this.champId = id;
        
        getJSONResponse();
        System.out.println("END - LoLStaticData_ChampionById(arg, arg)");
    }
    private void getJSONResponse(){
        System.out.println("METHOD - LoLStaticData_ChampionById/getJSONResponse");
        String jsonResponse = null; //unparsed json response
        int serverResponseCode = 0; //response code of server
        try {
            //URL
            URL url = new URL(""
                    + "https://global.api.pvp.net/api/lol/static-data/" + this.countryCode  + "/v1.2/champion/"+ this.champId +"?champData=image&api_key=" + apiKey);
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
            
            //parseJSONResponse(jsonResponse); //parse the json response into usable values
            
        } catch (MalformedURLException ex) {
            System.out.println("    MalformedURLException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check getJSONResponse()");
        } catch (IOException ex) {
            System.out.println("    IOException::: RC(" + serverResponseCode + ") Error retrieving JSON Response. Check getJSONResponse()");
        }
    }
}
