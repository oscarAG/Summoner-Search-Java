
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Oscar
 */
public class GameStaticData {
    
    private final String[] champions = {   
        "Aatrox", "Ahri", "Akali", "Alistar", "Amumu", "Anivia", "Annie", "Ashe",
        "Azir", "Bard", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Cassiopeia", 
        "Chogath", "Corki", "Darius", "Diana", "DrMundo", "Draven", "Ekko", "Elise",
        "Evelynn", "Ezreal", "FiddleSticks", "Fiora", "Fizz", "Galio", "Gangplank", 
        "Garen", "Gnar", "Gragas", "Graves", "Hecarim", "Heimerdinger", "Irelia", 
        "Janna", "JarvanIV", "Jax", "Jayce", "Jinx", "Kalista", "Karma", "Karthus",
        "Kassadin", "Katarina", "Kayle", "Kennen", "Khazix", "KogMaw", "Leblanc",
        "LeeSin", "Leona", "Lissandra", "Lucian", "Lulu", "Lux", "Malphite", 
        "Malzahar", "Maokai", "MasterYi", "MissFortune", "Mordekaiser", "Morgana",
        "Nami", "Nasus", "Nautilus", "Nidalee", "Nocturne", "Nunu", "Olaf", "Orianna",
        "Pantheon", "Poppy", "Quinn", "Rammus", "RekSai", "Renekton", "Rengar", 
        "Riven", "Rumble", "Ryze", "Sejuani", "Shaco", "Shen", "Shyvana", "Singed", 
        "Sion", "Sivir", "Skarner", "Sona", "Soraka", "Swain", "Syndra", "TahmKench",
        "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle", "Tryndamere",
        "TwistedFate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Velkoz",
        "Vi", "Viktor", "Vladimir", "Volibear", "Warwick", "MonkeyKing", "Xerath", "XinZhao",
        "Yasuo", "Yorick", "Zac", "Zed", "Ziggs", "Zilean", "Zyra"
    };
    private final String[] regions = {
        "North America",
        "Brazil",
        "EU Nordic & East",
        "EU West",
        "Korea",
        "Latin America North",
        "Latin America South",
        "Oceania",
        "Public Beta Environment",
        "Russia",
        "Turkey"
    };
    LoLSearch objLoLSearch = new LoLSearch();
    
    public GameStaticData(){ //no arg constructor
        System.out.println("CONSTRUCTOR - GameStaticData()");
    }
    
    public String[] getChampionsArray(){
        return champions;
    }
    
    public String[] getRegionsArray(){
        return regions;
    }
    
    /*Return ImageIcon with random champion artwork*/
    public ImageIcon getBackgroundImageIcon(){
        System.out.println("METHOD - GameStaticData/getBackgroundImageIcon");
        ImageIcon image = null;
        //Get a random number to select a champion from the champion array
        Random seed = new Random();
        int low = 0;
        int high = champions.length - 1;
        int random = seed.nextInt(high - low) + low;
        String randomChampion = champions[random];
        System.out.println("    " + randomChampion + " chosen to be background.");
        
        //URL
        int serverResponseCode = 0;
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/"+randomChampion+"_0.jpg");
            //Response code
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            serverResponseCode = connection.getResponseCode();
            //Get image
            BufferedImage c = ImageIO.read(url);
            image = new ImageIcon(c);
            System.out.println("    Successful. RC(" + serverResponseCode+")");
        } catch (MalformedURLException ex) {
            System.out.println("    MalformedURLException::: RC("+serverResponseCode+") Error retrieving background image. Check setBackground()");
        } catch (IOException ex) {
            System.out.println("    IOException::: RC("+serverResponseCode+") Error retrieving background image. Check setBackground()");
        }
        return image;
    }
    
    /*Return Image icon with the KDA icon*/
    public ImageIcon getScoreboardIconOf(String iconString){
        System.out.println("    METHOD - getScoreboardIconOf(arg)");
        int serverResponseCode = 0;
        String tempString = iconString;
        ImageIcon temp = null;
        try { //                                               Note: the version number has to be older for it to work
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/"+"5.2.1"+"/img/ui/"+tempString+".png"); //link to the pic
            //Response code
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            serverResponseCode = connection.getResponseCode();
            
            BufferedImage c = ImageIO.read(url);
            temp = new ImageIcon(c);
            //resize
            Image image = temp.getImage();
            Image newImg = image.getScaledInstance(30,30,Image.SCALE_SMOOTH);
            temp = new ImageIcon(newImg);
            System.out.println(    "Successfully got scoreboard icon of " + tempString + ".");
        } catch (IOException ex) {
            System.out.println("    IOException check getScoreboardIconOf()");
        }
        return temp;
    }
    
    //DOES NOT WORK
    public void getMostRecentVersion(String regionCode){
        System.out.println("    METHOD - getMostRecentVersion()");
        String jsonResponse = null;
        int serverResponseCode = 0;
        try {
            URL url = new URL("https://global.api.pvp.net/api/lol/static-data/"+regionCode+"/v1.2/versions?api_key=" + this.objLoLSearch.getApiKey());
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
            System.out.println(jsonResponse);
            JSONArray array = new JSONArray(jsonResponse);
            System.out.println(array);
            System.out.println("    Successful. RC(" + serverResponseCode +")");
        } catch (MalformedURLException ex) {
            System.out.println("    Malformed URL Exception. Issue getting most recent version.");
        } catch (IOException ex) {
            System.out.println("    IOException. Issue getting most recent version.");
        } catch (JSONException ex) {
            System.out.println("    JSONException. Issue getting most recent version.");
        }
    }
}
