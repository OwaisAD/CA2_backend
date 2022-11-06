package datafetching;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelPinger {
    public static ExecutorService executorService = Executors.newFixedThreadPool(8);
    public static String[] urls = {
            "https://api.nytimes.com",
            "https://omdbapi.com/",
    };

    public static List<Future<String>> futureList = new ArrayList<>();

    public static List<String> getStatusFromAllServers() throws Exception {
        for(String url : urls) {
            PingURL pingURL = new PingURL(url);
            Future<String> future = executorService.submit(() -> {
                try {
                    return pingURL.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            futureList.add(future);
        }

        List<String> resultList = new ArrayList<>();
        for (Future<String> future : futureList) {
            resultList.add(future.get());
        }
        return resultList;

    }

    public static void main(String[] args) throws Exception {
        long timeStart = System.nanoTime();
        List<String> results = getStatusFromAllServers();
        for(String r: results){
            System.out.println(r);
        }
        long timeEnd = System.nanoTime();
        long total = (timeEnd - timeStart) / 1_000_000;
        System.out.println("Time to check URLS: " + total + "ms.");
        executorService.shutdown();
    }
}
