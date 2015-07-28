
package lol.search;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Oscar
 */
public class GameStaticData {
    
    private final String[] champions = {   
        "Aatrox", "Ahri", "Akali", "Alistar", "Amumu", "Anivia", "Annie", "Ashe",
        "Azir", "Bard", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Cassiopia", 
        "Chogath", "Corki", "Darius", "Diana", "DrMundo", "Draven", "Ekko", "Elise",
        "Evelynn", "Ezreal", "Fiddlesticks", "Fiora", "Fizz", "Galio", "Gangplank", 
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
    
    public GameStaticData(){ //no arg constructor
        
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
}
