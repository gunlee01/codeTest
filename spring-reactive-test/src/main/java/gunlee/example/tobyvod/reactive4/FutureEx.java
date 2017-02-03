package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 3.
 * Future is blocking
 */
@Slf4j
public class FutureEx {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> future = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        //f.isDone()
        log.info(future.get()); //Blocking
        log.info("Exit");

    }
}
