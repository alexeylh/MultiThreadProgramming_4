import java.util.concurrent.BlockingQueue;

public class TextAnalyzer implements Runnable {
    private BlockingQueue texts;
    private int maxCount;
    private String leader;
    private char searchSymbol;
    private int iterationsCount;

    public TextAnalyzer(BlockingQueue texts, char searchSymbol) {
        this.texts = texts;
        this.searchSymbol = searchSymbol;
    }

    private void analyzeText(String text) {
        int count = (int) text.chars().filter(ch -> ch == searchSymbol).count();
        if (count > maxCount) {
            maxCount = count;
            leader = text;
        }
    }

    private void printResult() {
        System.out.println("Количество букв '" + searchSymbol + "' в этом тексте: " + maxCount + "\n" + leader);
    }

    @Override
    public void run() {
        // разбираем очередь, пока не выполним план
        while (iterationsCount < Main.getLimit()) {
            try {
                String text = (String) texts.take();
                analyzeText(text);
                iterationsCount++;
                // System.out.println("analyzer " + searchSymbol + ": шаг " + iterationsCount);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        printResult();
    }
}
