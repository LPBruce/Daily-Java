package demo.Concurrency.BlockingQueue;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

// 用例来自于《java核心技术 卷1》 p670，练习如何使用阻塞队列来控制一组线程，
// 书中一句话说得很好：Lock/Synchronized是java并发编程的底层构建块，在实际编程中，尽可能远离底
// 层结构，而是使用并发处理的专业人士实现的较高层次的结构要方便的多，要安全的多。
// 具体的话，java.util.concurrent提供的阻塞队列和卷2的并行流建议使用。
// 该程序是在一个目录及它的所有子目录下搜索所有文件，打印出包含指定关键字的行。
public class BlockingQueueTest {
    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 10;
    private static final File DUMMY = new File("");
    // 阻塞队列 
    private static ArrayBlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile):");
            String keyword = in.nextLine();

        Runnable enumerator = () -> {
            try {
                enumerate(new);
            } catch {

            }
        }






        }
    }

    public static void enumerate(File directory) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                enumerate(file);
            } else {
                queue.put(file);
            }
        }
    }


    public static void search(File file, String keyword) throws IOException {

    }
    
}
