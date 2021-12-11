package demo.Concurrency.Future;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

// 返回值位整数
// callable类似于Runnable, 但是具有返回值
// 利用MatchCounter实现Callable接口，主要是call函数
class MatchCounter implements Callable<Integer> {
    private File directory;
    private String keyword;
    public MatchCounter(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }
    // callable接口核心函数，是线程执行的核心流程，表示注意返回值位Integer
    public Integer call() throws IOException {
        int count = 0;
        try {
            File[] files = directory.listFiles();
            // Future用来保存异步线程计算结果，这里是个Future列表, 用来保存遍历结果
            List<Future<Integer>> results = new ArrayList<>();
            for (File file : files) {
                // 如果该file是个文件夹，意味着需要重新开启一个线程进行分析计算。
                // 因为需要返回值，所以使用使用callable和Future组合的形式
                if (file.isDirectory()) {
                    // 如果是个文件夹，则新建一个Matcher类实例进行遍历
                    MatchCounter counter = new MatchCounter(directory, keyword);
                    // 将此实例传入FutureTash包装器，讲Matcher实现的callable接口转换成Future和Runnable，
                    // 返回实现二者的接口的实例 Task
                    FutureTask<Integer> task = new FutureTask<>(counter);
                    // 将Tash接口实例放入Future列表中，用于后期获得线程结果
                    results.add(task);
                    // 新建线程，传入task
                    Thread t = new Thread(task);
                    t.start();
                } else {
                    // 判断改文件是否有关键字，如果有的话，则+1
                    if (search(file)) {
                        count++;
                    }
                }
            }
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
                        // System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
                    }
                }
                return found;
            }
        } catch (IOException e) {
            return false;
        }
    }
}