package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 3.
 * Future task - like callback
 * 그러나 깔끔하지는 않다
 */
@Slf4j
public class FutureEx2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<String>(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        }) {
            @Override protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(f);
        es.shutdown();
        log.info("Exit");

    }
}
