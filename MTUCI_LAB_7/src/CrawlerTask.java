import java.util.*;

/** CrawlerTask реализует интерфейс Runnable. Каждый экземпляр имеет ссылку на экземпляр класса
 * URLPool. Получает пару глубин URL из пула (ожидание, если недоступно), извлекает веб-страницу,
 * получает все URL со страницы и добавляет новую пару URLDepth в пул URL для каждого найденного URL. **/
public class CrawlerTask implements Runnable {

    /** Поле для данной пары глубин. **/
    public URLDepthPair depthPair;

    /** Поле для пула URL. **/
    public URLPool myPool;

    /** Конструктор для установки пула переменных URL для пула, переданного методу. **/
    public CrawlerTask(URLPool pool) {
        myPool = pool;
    }

    /** Метод для запуска задач CrawlerTask. **/
    public void run() {

        /** Получить следующую пару глубин из пула. **/
        depthPair = myPool.get();

        /** Глубина пары глубин. **/
        int myDepth = depthPair.getDepth();

        /** Получить все ссылки с сайта и сохранить их в новом связанном списке. **/
        LinkedList<String> linksList = new LinkedList<String>();
        linksList = Crawler.getAllLinks(depthPair);

        /** Перебирание ссылок с сайта. **/
        for (int i=0;i<linksList.size();i++) {
            String newURL = linksList.get(i);

            /** Создать новую пару глубин для каждой найденной ссылки и добавить в пул. **/
            URLDepthPair newDepthPair = new URLDepthPair(newURL, myDepth + 1);
            myPool.put(newDepthPair);
        }
    }
}