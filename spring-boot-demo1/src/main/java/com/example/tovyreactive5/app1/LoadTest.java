package com.example.tovyreactive5.app1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 5.
 *
 * 왜 하나씩 나가는지 찾아봐야 할듯
 * RestTemplate 왜 이모양이얌???
 */
@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8080/rest?idx={idx}";
//        String url = "http://localhost:8080/restAsync?idx={idx}";
//        String url = "http://localhost:8080/restAsyncDeferred?idx={idx}";
//        String url = "http://localhost:8080/restAsyncDeferred2?idx={idx}";

        CyclicBarrier barrier = new CyclicBarrier(101);

        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);

                log.info("Thread {}", idx);

                StopWatch sw = new StopWatch();

                barrier.await(); // 101번의 접근이 도달해야 그 순간에 블로킹이 풀린다
                sw.start();
                String res = rt.getForObject(url, String.class, idx);
                sw.stop();

                log.info("Elapsed: {}  {} / {}", idx, sw.getTotalTimeSeconds(), res);

                return null; //Callable로 하기 위해.. (barrier의 단순히 throw exception 처리 때문에 일단 이렇게 함)
            });
        }

        barrier.await();
        StopWatch main = new StopWatch();
        main.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        main.stop();
        log.info("Total: {}", main.getTotalTimeSeconds());
    }
}
