package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 5.
 *
 * 이때 서블릿에는 thread 하나만 쓴다
 *
 * http://localhost:8080/dr/count 해보면 100개이다.
 * 날린다음에 http://localhost:8080/event?msg=load-test 를 날려본다.
 *
 */
@Slf4j
public class LoadTest1Thread {
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        //String url = "http://localhost:8080/callable";
        String url = "http://localhost:8080/dr";

        StopWatch main = new StopWatch();
        main.start();

        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread {}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                rt.getForObject(url, String.class);

                sw.stop();
                log.info("Elapsed: {} -> {}", idx, sw.getTotalTimeSeconds());
            });
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        main.stop();
    }
}
