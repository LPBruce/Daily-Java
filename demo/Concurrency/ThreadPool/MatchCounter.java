package demo.Concurrency.ThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

// 返回值位整数
// callable类似于Runnable, 但是具有返回值
// 利用MatchCounter实现Callable接口，主要是call函数
public class MatchCounter implements Callable<Integer> {
    private File directory;
    private String keyword;
    private ExecutorService pool;
    private int count;

    public MatchCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    // callable接口核心函数，是线程执行的核心流程，表示注意返回值位Integer
    public Integer call() throws IOException {
        System.out.printf("开启一个文件遍历线程：当前文件名为 %s%n", directory.getName());
        count = 0;

        try {
            File[] files = directory.listFiles();
            // Future用来保存异步线程计算结果，这里是个Future列表, 用来保存遍历结果
            List<Future<Integer>> results = new ArrayList<>();

            for (File file : files) {
                // 如果该file是个文件夹，则利用线程池进行计算。
                if (file.isDirectory()) {
                    // 如果是个文件夹，则新建一个Matcher类实例进行遍历
                    MatchCounter counter = new MatchCounter(file, keyword, pool);
                    // 将此实例传入FutureTash包装器，讲Matcher实现的callable接口转换成Future和Runnable，
                    // 将Callable传入线程池
                    Future<Integer> result = pool.submit(counter);
                    // 累加Future结果
                    results.add(result);
                } else {
                    // 判断改文件是否有关键字，如果有的话，则+1
                    if (search(file)) {
                        count++;
                    }
                }
            }
            
            // 获取遍历线程的结果
            for (Future<Integer> result : results) {
                try {
                    count += result.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {}
        return count;
    }

    public boolean search(File file) throws IOException {
        System.out.printf("%s文件搜索线程开始%n", file.getName());
        try {
            // 传入对应文件，找是否有对应关键字
            try (Scanner in = new Scanner(file, "UTF-8")) {
                boolean found = false;
                while (!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.contains(keyword)) {
                        found = true;
                        System.out.printf("找到了，%s:%s%n", file.getPath(), line);
                    }
                }
                return found;
            }
        } catch (IOException e) {
            return false;
        }
    }
}