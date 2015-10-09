
package lol.search;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
    
    /*
        GET METHODS
    */
    public String[] getChampionsArray(){    return champions;   }
    public String[] getRegionsArray(){      return regions;     }
    
    /*Return ImageIcon with random champion artwork*/
    public ImageIcon getBackgroundImageIcon(){
        ImageIcon image = null;
        //Get a random number to select a champion from the champion array
        Random seed = new Random();
        int low = 0;
        int high = champions.length - 1;
        int random = seed.nextInt(high - low) + low;
        String randomChampion = champions[random];
        //System.out.println(randomChampion + " chosen to be background."); //test
        
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
            System.out.println(randomChampion);
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    /*Return ImageIcon with chosen champion artwork*/
    public ImageIcon getBackgroundImageIcon(String key){
        ImageIcon image = null;
        File f = new File("assets\\other\\" + key + ".png");
        if(f.isFile()){ //check if picture exists
            try {
                BufferedImage c = ImageIO.read(new File("assets\\other\\" + key + ".png"));
                image = new ImageIcon(c);
                //resize
                Image imageImg = image.getImage();
                Image newImg = imageImg.getScaledInstance(1215, 717,Image.SCALE_SMOOTH);
                image = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(key == null){
            try {
                BufferedImage c = ImageIO.read(new File("assets\\other\\" + "statsBackground" + ".jpg"));
                image = new ImageIcon(c);
                //resize
                Image imageImg = image.getImage();
                Image newImg = imageImg.getScaledInstance(1215, 717,Image.SCALE_SMOOTH);
                image = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //URL
        else{
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/"+key+"_0.jpg");
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
        }
        
        return image;
    }
    //return artwork for background of initial stats frame
    public ImageIcon getStatsBackground(){
        ImageIcon temp = new ImageIcon();
        File f = new File("assets\\other\\" + "statsBackground" + ".jpg");
        if(f.isFile()){ //check if picture exists
            try {
                BufferedImage c = ImageIO.read(new File("assets\\other\\" + "statsBackground"+ ".jpg"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(1215, 717,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return temp;
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
    
    /*check to see if the loading art is there, and if not, grab it from the api and save it locally.*/
    public ImageIcon initLoadingArt(String key){
        ImageIcon temp = null;
        File f = new File("assets\\loadIcons\\" + key + ".png"); 
        if(f.isFile()){
            try {
                //System.out.println("Image exists.");
                BufferedImage c = ImageIO.read(new File("assets\\loadIcons\\" + key + ".png"));
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(290,520,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
            } catch (IOException ex) {
                Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
        else{
            try {
                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/loading/"+key+"_0.jpg"); //link to the pic
                BufferedImage c = ImageIO.read(url);
                temp = new ImageIcon(c);
                //resize
                Image image = temp.getImage();
                Image newImg = image.getScaledInstance(290,504,Image.SCALE_SMOOTH);
                temp = new ImageIcon(newImg);
                ImageIO.write(c, "png",new File("assets\\loadIcons\\" + key + ".png")); //save to directory if it doesnt exist
                System.out.println(key + " saved to directory.");
            } catch (MalformedURLException ex) {
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return temp;
    }
}
