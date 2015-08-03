
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;

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
    }
    
    public String[] getChampionsArray(){
        return champions;
    }
    
    public String[] getRegionsArray(){
        return regions;
    }
    
    /*Return ImageIcon with random champion artwork*/
    public ImageIcon getBackgroundImageIcon(){
        ImageIcon image = null;
        //Get a random number to select a champion from the champion array
        Random seed = new Random();
        int low = 0;
        int high = champions.length - 1;
        int random = seed.nextInt(high - low) + low;
        String randomChampion = champions[random];
        System.out.println(randomChampion + " chosen to be background.");
        
        //URL
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/"+randomChampion+"_0.jpg");
            //Get image
            BufferedImage c = ImageIO.read(url);
            image = new ImageIcon(c);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    /*Return Image icon with the KDA icon*/
    public ImageIcon getScoreboardIconOf(String iconString){
        String tempString = iconString;
        ImageIcon temp = null;
        File f = new File("assets\\scoreboardIcons\\" + iconString + ".png");
        if(f.isFile()){ //check if picture exists
            try {
                BufferedImage c = ImageIO.read(new File("assets\\scoreboardIcons\\" + iconString + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(20,20,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try { //                                               Note: the version number has to be older for it to work
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/"+"5.2.1"+"/img/ui/"+tempString+".png"); //link to the pic
                BufferedImage c = ImageIO.read(url);
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(30,30,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (MalformedURLException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return temp;
    }
    
}
