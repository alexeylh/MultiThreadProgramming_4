import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Main {
    static final int CAPACITY = 10;  // ёмкость очередей
    static BlockingQueue queueA = new ArrayBlockingQueue<String>(CAPACITY);
    static BlockingQueue queueB = new ArrayBlockingQueue<String>(CAPACITY);
    static BlockingQueue queueC = new ArrayBlockingQueue<String>(CAPACITY);

    public static void main(String[] args) throws InterruptedException {
        long startTs = System.currentTimeMillis(); // start time

        Thread producer = new Thread(() -> {
            for (int i = 0; i < getLimit(); i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                    // System.out.println("producer: шаг " + i);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        producer.start();

        Thread threadA = new Thread(new TextAnalyzer(queueA, 'a'));
        threadA.start();
        Thread threadB = new Thread(new TextAnalyzer(queueB, 'b'));
        threadB.start();
        Thread threadC = new Thread(new TextAnalyzer(queueC, 'c'));
        threadC.start();

        System.out.println("Анализаторы текстов запущены...");
        producer.join();
        threadA.join();
        threadB.join();
        threadC.join();

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("\nАнализ завершён успешно.  Time: " + (endTs - startTs) + " ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int getLimit() {
        return 10_000; // количество анализируемых текстов
    }
}
