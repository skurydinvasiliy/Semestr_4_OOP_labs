import java.net.*;
import java.util.*;
import java.io.*;

/**
 *  Искатель классов обрабатывает аргументы командной строки, создает экземпляр
 *  пула URL-адресов, добавляет введенный URL-адрес в пул и создает количество
 *  задач искателя, введенных с потоками для их запуска. Затем, когда сканирование
 *  завершено, распечатывается список найденных URL-адресов.
 */
public class Crawler {
    /**
     * Метод для выполнения задач Кроулера.
     */
    public static void main(String[] args) {
        /** Переменные для текущей глубины и запрошенного количества потоков. **/
        int depth = 0;
        int numThreads = 0;

        /** Проверяет, была ли введенная длина правильной. Если нет, распечатывает
         *  сообщение об использовании и завершает работу. **/
        if (args.length != 3) {
            System.out.println("usage: java Crawler <URL> <depth> <number of crawler threads");
            System.exit(1);
        }
        /** Если ввод правильный, продолжайте. **/
        else {
            try {
                /** Разобрать строковый аргумент в целочисленное значение. **/
                depth = Integer.parseInt(args[1]);
                numThreads = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException nfe) {
                /** Второй или третий аргумент не является допустимым целым числом.
                 * Остановите и распечатайте сообщение об использовании. **/
                System.out.println("usage: java Crawler <URL> <depth> <number of crawler threads");
                System.exit(1);
            }
        }

        /** Пара глубинных URL-адресов для представления веб-сайта, введенного пользователем с глубиной 0. **/
        URLDepthPair currentDepthPair = new URLDepthPair(args[0], 0);

        /** Создайте пул URL и добавьте введенный пользователем веб-сайт. **/
        URLPool pool = new URLPool();
        pool.put(currentDepthPair);

        /** Переменные для общего количества потоков и начальных потоков. **/
        int totalThreads = 0;
        int initialActive = Thread.activeCount();

        /** Хотя ожидающие потоки не равны запрошенному количеству потоков,
         * если общее количество потоков меньше запрошенного количества потоков,
         * создайте больше потоков и запустите их на CrawlerTask. Остальное, спать. **/
        while (pool.getWaitThreads() != numThreads) {
            if (Thread.activeCount() - initialActive < numThreads) {
                CrawlerTask crawler = new CrawlerTask(pool);
                new Thread(crawler).start();
            }
            else {
                try {
                    Thread.sleep(100);  // 0.1 second
                }
                /** Поймать прерванное исключение. **/
                catch (InterruptedException ie) {
                    System.out.println("Caught unexpected " +
                            "InterruptedException, ignoring...");
                }
            }
        }

        /** Когда все потоки ожидают, распечатаем все обработанные URL
         * с глубиной. Распечатайте все обработанные URL с глубиной. **/
        Iterator<URLDepthPair> iter = pool.processedURLs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }        // Exit.
        System.exit(0);
    }
    /** Метод, который принимает URLDepthPair и возвращает LinkedList типа String.
     Подключается к сайту в URLDepthPair, находит все ссылки на сайте и добавляет
     их в новый LinkedList, который возвращается. */
    public static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) {

        /** Инициализируйте связанный список строк, в котором будут храниться найденные ссылки. **/
        LinkedList<String> URLs = new LinkedList<String>();

        /** Инициализируйте сокет. **/
        Socket sock;

        /** Попробуйте создать новый сокет с URL-адресом, переданным методу в URLDepthPair и порту 80. **/
        try {
            sock = new Socket(myDepthPair.getWebHost(), 80);
        }
        /** Поймать UnknownHostException и вернуть пустой список. **/
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return URLs;
        }
        /** Поймать IOException и вернуть пустой список. **/
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return URLs;
        }

        /** Попробуйте установить сокет на тайм-аут после 3 с. **/
        try {
            sock.setSoTimeout(3000);
        }
        /** Перехватите SocketException и верните пустой список. **/
        catch (SocketException exc) {
            System.err.println("SocketException: " + exc.getMessage());
            return URLs;
        }

        /** Строка для представления docPath из URL, переданного как
         * URLDepthPair, и строка для представления webHost. **/
        String docPath = myDepthPair.getDocPath();
        String webHost = myDepthPair.getWebHost();

        /** Инициализируйте OutputStream. **/
        OutputStream outStream;

        /** Попробуйте получитьOutputStream из сокета. **/
        try {
            outStream = sock.getOutputStream();
        }
        /** Поймать IOException и вернуть пустой список. **/
        catch (IOException exce) {
            System.err.println("IOException: " + exce.getMessage());
            return URLs;
        }
        /** Инициализирует PrintWriter. True означает, что PrintWriter будет сбрасываться после каждого вывода. **/
        PrintWriter myWriter = new PrintWriter(outStream, true);

        /** Отправка запроса на сервер. **/
        myWriter.println("GET " + docPath + " HTTP/1.1");
        myWriter.println("Host: " + webHost);
        myWriter.println("Connection: close");
        myWriter.println();

        /** Инициализируйте InputStream. **/
        InputStream inStream;

        /** Попробуй getInputStream из сокета. **/
        try {
            inStream = sock.getInputStream();
        }
        /** Поймать IOException и вернуть пустой список. **/
        catch (IOException excep){
            System.err.println("IOException: " + excep.getMessage());
            return URLs;
        }
        /** Создайте новый InputStreamReader и BufferedReader для чтения строк с сервера. **/
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader BuffReader = new BufferedReader(inStreamReader);

        /** Попробуйте прочитать строку из Buffered Reader. **/
        while (true) {
            String line;
            try {
                line = BuffReader.readLine();
            }
            /** Поймать IOException и вернуть пустой список. **/
            catch (IOException except) {
                System.err.println("IOException: " + except.getMessage());
                return URLs;
            }
            /** Закончили чтение документа! **/
            if (line == null)
                break;

            /** Переменные для представления индексов, где начинаются и
             * заканчиваются ссылки, а также текущего индекса. **/
            int beginIndex = 0;
            int endIndex = 0;
            int index = 0;

            while (true) {

                /** Константа для строки, указывающей на ссылку. **/
                String URL_INDICATOR = "a href=\"";

                /** Константа для строки, указывающей конец веб-хоста и начало docpath. **/
                String END_URL = "\"";

                /** Поиск нашего начала в текущей строке. **/
                index = line.indexOf(URL_INDICATOR, index);
                if (index == -1) // No more copies of start in this line
                    break;

                /** Продвинуть текущий индекс и установить в начало Index. **/
                index += URL_INDICATOR.length();
                beginIndex = index;

                /** Найдите наш конец в текущей строке и установите значение end Index. **/
                endIndex = line.indexOf(END_URL, index);
                index = endIndex;

                /** Установить ссылку на подстроку между начальным и конечным индексами.
                 * Добавьте к нашему списку URL. **/
                String newLink = line.substring(beginIndex, endIndex);
                URLs.add(newLink);
            }
        }
        /** Вернуть список URL. **/
        return URLs;
    }
}