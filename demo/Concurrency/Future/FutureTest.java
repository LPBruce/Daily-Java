package demo.Concurrency.Future;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureTest {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile):");
            String keyword = in.nextLine();

            Runnable enumerator = () -> {
                try {
                    enumerate(new File(directory));
                    // 虚拟目录，用来标志队列内容结束
                    queue.put(DUMMY);
                } catch(InterruptedException e) {}
            };
    
            // 开始一个生产者线程，讲所有温江放进一个阻塞队列中
            new Thread(enumerator).start();
            for (int i = 0; i < SEARCH_THREADS; i++) {
                Runnable searcher = () -> {
                    try {
                        boolean done = false;
                        while(!done) {
                            File file = queue.take();
                            if (file == DUMMY) {
                                queue.put(file);
                                done = true;
                            } else {
                                search(file, keyword);
                            }
                        }
                    } catch (IOException e) {
                    e.printStackTrace();
                    } catch (InterruptedException e) {
                    }
               };
               new Thread(searcher).start();
            };
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

        public Integer call() {
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
                    })
                } else {
                    if (search(file)) {
                        count++;
                    }
                }
            }

            for (Future<Integer> future : results) {
                
            }
        }
    }

    public static boolean search(File file) throws IOException {
        System.out.printf("%s文件搜索线程开始%n", file.getName());
        try (Scanner in = new Scanner(file, "UTF-8")) {
            int lineNumber = 0;
            while (in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
                }
            }
        }
    }
}
