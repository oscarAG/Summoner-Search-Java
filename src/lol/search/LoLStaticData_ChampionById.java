
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.objLoLSearch = new LoLSearch();
        this.countryCode = cc;
        this.apiKey = objLoLSearch.getApiKey();
        this.champId = id;
        
        getJSONResponse();
    }
    private void getJSONResponse(){
        String jsonResponse = null; //unparsed json response
        try {
            //URL
            URL url = new URL(""
                    + "https://global.api.pvp.net/api/lol/static-data/" + this.countryCode  + "/v1.2/champion/"+ this.champId +"?champData=image&api_key=" + apiKey);
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            //parseJSONResponse(jsonResponse); //parse the json response into usable values
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoLStaticData_ChampionById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoLStaticData_ChampionById.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
