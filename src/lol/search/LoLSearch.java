
package lol.search;

/**
 *
 * @author Oscar
 */
public class LoLSearch {

    private static String apiKey = "5ef85c1b-a4b7-4001-8b12-9a4fad596e08";
    
    public static void main(String[] args) {
        System.out.println("Application started.");
        /*Proceed to GUIFrame class*/
        GUIFrame objFrame = new GUIFrame();
    }
    
    public String getApiKey(){
            return apiKey;
    }
    
}
