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

public class FutureTest {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile):");
            String keyword = in.nextLine();

            MatchCounter counter = new MatchCounter(new File(directory), keyword));
            FutureTask<Integer> task = new FutureTask<>(counter);
            Thread t = new Thread(task);
            t.start();

            try {
                System.out.printf(task.get() + " matching files.");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {}
        }
    }

    // 返回值位整数
    class MatchCounter implements Callable<Integer> {
        private File directory;
        private String keyword;

        public MatchCounter(File directory, String keyword) {
            this.directory = directory;
            this.keyword = keyword;
        }

        public Integer call() throws IOException {
            int count = 0;
            try {
                File[] files = directory.listFiles();
                List<Future<Integer>> results = new ArrayList<>();

                for (File file : files) {
                    if (file.isDirectory()) {
                        MatchCounter counter = new MatchCounter(directory, keyword);
                        FutureTask<Integer> task = new FutureTask<>(counter);
                        results.add(task);
                        Thread t = new Thread(task);
                        t.start();
                    } else {
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
}
