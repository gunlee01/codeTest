package gunlee.example.servlet.async3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 26.
 */
@SpringBootApplication
@Slf4j
public class MyApp3 {

    @RestController
    public static class ExController {

        @GetMapping("/ex1")
        public DeferredResult<String> ex1() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dr.setResult("deferred hello!");
            }).start();
            return dr;
        }

        ExecutorService es = Executors.newCachedThreadPool();

        @GetMapping("/ex2future")
        public DeferredResult<String> ex2() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            System.out.println("[oncode main]" + Thread.currentThread().getName());
            es.submit(() -> {
                System.out.println("[oncode thread]" + Thread.currentThread().getName());
                Thread.sleep(2000);
                return dr.setResult("Hi~");
            });

            return dr;
        }

        @GetMapping("/ex3future")
        public DeferredResult<Long> ex3() throws InterruptedException {
            DeferredResult<Long> dr = new DeferredResult<>();

            System.out.println("[oncode main]" + Thread.currentThread().getName());
            es.submit(() -> {
                System.out.println("[oncode thread]" + Thread.currentThread().getName());
                Thread.sleep(2000);
                return dr.setResult(12345678901234L);
            });

            return dr;
        }
    }

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8085");
        System.setProperty("server.tomcat.max-threads", "100");

        SpringApplication.run(MyApp3.class, args);
    }
}
