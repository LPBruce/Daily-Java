package demo.Concurrency.BlockingQueue;

import java.io.File;
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
    private static ArrayBlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    
}
