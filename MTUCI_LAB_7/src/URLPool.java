import java.util.*;

/** Класс пула URL для хранения списка URL для поиска с глубиной. Хранится как экземпляр URLDepthPair. **/

public class URLPool {

    /** Связанный список для представления ожидающих URL-адресов. **/
    private LinkedList<URLDepthPair> pendingURLs;

    /**  Связанный список для представления обработанных URL. **/
    public LinkedList<URLDepthPair> processedURLs;

    /** Список массивов для представления увиденных URL. **/
    private ArrayList<String> seenURLs = new ArrayList<String>();

    /** Int для отслеживания количества ожидающих потоков. **/
    public int waitingThreads;

    /** Конструктор для инициализации ожидающих потоков, обработанных URL-адресов и ожидающих URL-адресов.  **/
    public URLPool() {
        waitingThreads = 0;
        pendingURLs = new LinkedList<URLDepthPair>();
        processedURLs = new LinkedList<URLDepthPair>();
    }

    /** Синхронизированный метод для получения количества ожидающих потоков. **/
    public synchronized int getWaitThreads() {
        return waitingThreads;
    }

    /** Синхронизированный метод для возврата размера пула. **/
    public synchronized int size() {
        return pendingURLs.size();
    }

    /** Синхронизированный метод для добавления пары глубин в пул. **/
    public synchronized boolean put(URLDepthPair depthPair) {

        /** Переменная для отслеживания добавления пары глубин. **/
        boolean added = false;

        /** Если глубина меньше максимальной глубины, добавьте пару глубин в пул. **/
        if (depthPair.getDepth() < depthPair.getDepth()) {
            pendingURLs.addLast(depthPair);
            added = true;
            /** Что-то добавлено, чтобы разбудить потребителя. Уменьшение количества ожидающих потоков. **/
            waitingThreads--;
            this.notify();
        }
        /** Если глубина не меньше максимальной глубины, просто добавьте пару глубин в видимый список. **/
        else {
            seenURLs.add(depthPair.getURL());
        }
        /** Возвращено логическое значение. **/
        return added;
    }
    /** Синхронизированный метод для получения следующей пары глубин из пула. **/
    public synchronized URLDepthPair get() {

        /** Установить пару глубины на ноль. **/
        URLDepthPair myDepthPair = null;

        /** Пока пул пуст, подождите. **/
        if (pendingURLs.size() == 0) {
            waitingThreads++;
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                System.err.println("MalformedURLException: " + e.getMessage());
                return null;
            }
        }
        /** Удалить первую пару глубин, добавить к просмотренным и обработанным URL-адресам и вернуть их. **/
        myDepthPair = pendingURLs.removeFirst();
        seenURLs.add(myDepthPair.getURL());
        processedURLs.add(myDepthPair);
        return myDepthPair;
    }
    /**  Синхронизированный метод, чтобы получить список увиденных URL-адресов. **/
    public synchronized ArrayList<String> getSeenList() {
        return seenURLs;
    }
}