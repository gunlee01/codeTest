package gunlee.example.servlet.async3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 26.
 */
@SpringBootApplication
@Slf4j
public class MyApp3 {
    @Data
    @AllArgsConstructor
    static class Foo {
        String name;
        int value;
    }

    @RestController
    public static class ExController {
        @GetMapping("exToList")
        public String exToList() {
            List<Foo> fooList = new ArrayList<>();
            fooList.add(new Foo("a", 1));
            fooList.add(new Foo("b", 2));
            fooList.add(new Foo("c", 3));
            fooList.add(new Foo("d", 4));
            fooList.add(new Foo("e", 5));
            fooList.add(new Foo("f", 6));

            List<Foo> foo2List = fooList.stream().skip(2).limit(3).collect(Collectors.toList());

            System.out.println(foo2List);

            return foo2List.toString();
        }

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

        ExecutorService es = Executors.newFixedThreadPool(2);

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

        @GetMapping("/ex3_1")
        public String ex3_1() throws InterruptedException {

            Runnable r = () -> {
                try {
                    Thread.sleep(1000);
                    log.info("async lambda");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            es.submit(r);

            return "end ex3_1";
        }

        @GetMapping("/ex3_2")
        public String ex3_2() throws InterruptedException {
            int i = 10;

            Runnable r = () -> {
                try {
                    Thread.sleep(1000);
                    log.info("async lambda with variable capture : " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            es.submit(r);

            return "end ex3_2";
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
