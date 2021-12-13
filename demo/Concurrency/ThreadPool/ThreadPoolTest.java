package demo.Concurrency.ThreadPool;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile):");
            String keyword = in.nextLine();

            // 通过执行器的newCachedThreadPool新建线程池
            // 返回对象为实现了ExecutorService接口的ThreadPoolExecutor对象
            ExecutorService pool = Executors.newCachedThreadPool();

            // Callable接口包装器，该包装器将线程池传入
            MatchCounter counter = new MatchCounter(new File(directory), keyword, pool);
            // 将Callable对象交给线程池
            Future<Integer> result = pool.submit(counter);

            try {
                System.out.printf(result.get() + " matching files.");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {}

            // 关闭该线程池
            pool.shutdown();

            // 返回该线程池最大的线程数
            int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
            System.out.println("最大的线程池数" + largestPoolSize);
        }
    }
}
