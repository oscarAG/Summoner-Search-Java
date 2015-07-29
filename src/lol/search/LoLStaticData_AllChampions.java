
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This endpoint receives and makes usable information about all champions from the static data champion endpoint. 
 * This information can include the key name, images, etc. The benefit of this is that you can retrieve the information all at once and parse as needed.
 * Instead of making 10 individual calls. 
 * @author Oscar
 */
public class LoLStaticData_AllChampions {
    private LoLSearch objLoLSearch;
    private final ArrayList<Integer> championId;
    private final String regionCode;
    
    public LoLStaticData_AllChampions(ArrayList<Integer> champArr, String cc){
        System.out.println("CONSTRUCTOR - LoLStaticData_AllChampions(arg, arg)");
        this.objLoLSearch = new LoLSearch();
        this.championId = champArr;
        this.regionCode = cc;
        
        getJSONResponse(); //grab json response from api
        
        System.out.println("END - LoLStaticData_AllChampions(arg, arg)");
    }
    private void getJSONResponse(){
        System.out.println("METHOD - LoLStaticData_AllChampions/getJSONResponse");
        String jsonResponse = null; //unparsed json response
        int serverResponseCode = 0; //response code of server
        try {
            //URL
            URL url = new URL(""
                    + "https://global.api.pvp.net/api/lol/static-data/" + this.regionCode  + "/v1.2/champion/"+ this.championId +"?champData=image&api_key=" + this.objLoLSearch);
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
