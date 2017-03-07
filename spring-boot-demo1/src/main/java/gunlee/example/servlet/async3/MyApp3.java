package gunlee.example.servlet.async3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

            es.submit(() -> {
                Thread.sleep(2000);
                return dr.setResult("Hi~");
            });

            return dr;
        }

        //just lambda
        @GetMapping("/ex3")
        public DeferredResult<String> ex3() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            Runnable r = () -> {
                try {
                    Thread.sleep(1000);
                    log.info("sync runnable");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            r.run();
            dr.setResult("Hi ex3~");

            return dr;
        }

        //lambda and thread
        @GetMapping("/ex4")
        public DeferredResult<String> ex4() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("async runnable - ex4");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dr.setResult("deferred hello ex3!");
            }).start();

            Runnable r = () -> {
                try {
                    Thread.sleep(1000);
                    log.info("sync runnable - ex4");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            r.run();

            return dr;
        }

        @GetMapping("/ex5")
        public DeferredResult<String> ex5() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dr.setResult("ex5 deferred hello!");
                }
            }).start();

            Thread t = new Thread();
            t.setName("my-thread-1");

            return dr;
        }

        public static ExecutorService es2 = Executors.newFixedThreadPool(3);

        @GetMapping("/ex6")
        public DeferredResult<String> ex6() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            es2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("ex6 -1 after sleep");
                }
            });

            Thread.sleep(1000);

            es2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("ex6 - 2 after sleep");
                    dr.setResult("ex6");
                }
            });

            return dr;
        }

        @GetMapping("/ex7")
        public DeferredResult<String> ex7() throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            Callable<String> myCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(1000);
                    log.info("myCallable in thread : " + System.identityHashCode(this));
                    dr.setResult("ex7");
                    return "callable";
                }
            };

            log.info("myCallable1 : " + System.identityHashCode(myCallable));

            Future<String> f = es2.submit(myCallable);

            return dr;
        }
    }

    public static class MyThread extends Thread {
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("MyThread !!! after sleep");
        }
    }

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8085");
        System.setProperty("server.tomcat.max-threads", "100");

        SpringApplication.run(MyApp3.class, args);
    }
}
