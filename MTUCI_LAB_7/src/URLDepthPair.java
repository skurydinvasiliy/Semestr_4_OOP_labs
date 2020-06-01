import java.net.*;

/** Класс для представления пар [URL, глубина] для нашего сканера. **/
public class URLDepthPair {

/** Поля для представления текущего URL и текущей глубины. **/

    private int currentDepth;
    private String currentURL;

    /** Конструктор, который устанавливает ввод для текущего URL и глубины. **/
    public URLDepthPair(String URL, int depth) {
        currentDepth = depth;
        currentURL = URL;
    }
    /** Метод, который возвращает текущий URL. **/

    public String getURL() {
        return currentURL;
    }
    /** Метод, который возвращает текущую глубину. **/

    public int getDepth() {
        return currentDepth;
    }
    /** Метод, который возвращает текущий URL и текущую глубину в строковом формате. **/
    public String toString() {
        String stringDepth = Integer.toString(currentDepth);
        return stringDepth + '\t' + currentURL;
    }
    /** Метод, который возвращает docPath текущего URL. **/
    public String getDocPath() {
        try {
            URL url = new URL(currentURL);
            return url.getPath();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
            return null;
        }
    }
    /**  Метод, который возвращает webHost текущего URL. **/
    public String getWebHost() {
        try {
            URL url = new URL(currentURL);
            return url.getHost();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
            return null;
        }
    }
}