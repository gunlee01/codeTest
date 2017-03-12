package com.example.tovyreactive5.app1;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 6.
 *
 * 여기부터 보자 - https://youtu.be/ExUfZkh7Puk?t=2447
 */
@SpringBootApplication
@Slf4j
@EnableAsync
public class MyApplication {

    @RestController
    public static class MyController {
        public static final String URL1 = "http://localhost:8081/service?req={req}";
        public static final String URL2 = "http://localhost:8081/service2?req={req}";
        RestTemplate rt = new RestTemplate();

        AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

        @Autowired
        MyService myService;

        /**
         * Blocking Sample
         * thread 하나라서 2초에 하나씩 밖에 처리 못함
         */
        @GetMapping("/rest")

        public String rest(int idx) throws InterruptedException {
            String res = rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            return res;
        }

        /**
         * 비동기로 처리한다
         * 하나의 thread 만 사용하기 위하여 AryncRestTemplate 사용하며
         * 응답도 Listenable Future를 리턴한다
         */
        @GetMapping("/restAsync")
        public ListenableFuture<ResponseEntity<String>> restAsync(int idx) throws InterruptedException {

            ListenableFuture<ResponseEntity<String>> res = art.getForEntity(
                    "http://localhost:8081/service?req={req}",
                    String.class,
                    "hello" + idx);

            //그냥 ListenableFuture를 응답하면 spring이 알아서 한다
            //원래는 callback을 등록해야 하고
            //그런데 callback에서 return은 의미가 없으니. 어 어떻게 해야하지 하고 고민하게 된다.
            //callback은 spring mvc가 알아서 해준다.
            doit();
            doit2();
            doit3();

            return res;
        }

        @GetMapping("/restAsync2")
        public DeferredResult<String> restAsync2(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();
            dr.setResult(rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx));
            return dr;
        }

        public void doit() {
            for (int i=0; i<100000; i++) {
                if(i % 10000 == 0) {
                    System.out.print(i + ",");
                }
                System.out.println();
            }
        }
        public void doit2() {
            for (int i=0; i<10000; i++) {
                if(i % 10000 == 0) {
                    System.out.print(i + ",");
                }
                System.out.println();
            }
        }
        public void doit3() {
            for (int i=0; i<1000; i++) {
                if(i % 1000 == 0) {
                    System.out.print(i + ",");
                }
                System.out.println();
            }
        }

        /**
         * 결과를 가공하고 싶다면
         * deferredResult를 사용하여야 한다
         */
            @GetMapping("/restAsyncDeferred")
        public DeferredResult<String> restAsyncDeferred(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();
            //TODO ListenableFuture callaback hook !!

            log.info("[It's restAsyncDeferred start]-" + idx);
            ListenableFuture<ResponseEntity<String>> f1 = art.getForEntity(
                    "http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            f1.addCallback(s -> {
                log.info("[It's callback]-" + idx);
                dr.setResult(s.getBody() + "/work");
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });

            return dr;
        }

        ExecutorService es = Executors.newFixedThreadPool(10);

        @GetMapping("/gun1")
        public DeferredResult<String> gun1(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();
            es.execute(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("My lambda runnable executed!");
                dr.setResult("ok /gun1");
            });
            log.info("invoked /gun1");
            return dr;
        }

        @GetMapping("/restAsyncDeferred2")
        public DeferredResult<String> restAsyncDeferred2(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> f1 = art.getForEntity(URL1, String.class, "hello" + idx);

            f1.addCallback(s -> {
                //TODO addCallback이 호출되어야 끝나야 하는데.... 그전에 끝나는 군.. 우짜지?
                //일단 어쩔수는 없을 듯

                ListenableFuture<ResponseEntity<String>> f2 = art.getForEntity(
                        URL2, String.class, s.getBody()
                );
                f2.addCallback(s2 -> {
                    ListenableFuture<String> f3 = myService.work(s2.getBody());
                    f3.addCallback(s3 -> {
                        dr.setResult(s3);
                    }, e-> {
                        dr.setErrorResult(e.getMessage());
                    });
                }, e -> {
                    dr.setErrorResult(e.getMessage());
                });
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });

            return dr;
        }

        /**
         * 여기는 6화 방송임
         */
        @GetMapping("/restAsyncDeferred3")
        public DeferredResult<String> restAsyncDeferred3(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> f1 = art.getForEntity(URL1, String.class, "hello" + idx);

            f1.addCallback(s -> {
                ListenableFuture<ResponseEntity<String>> f2 = art.getForEntity(URL2, String.class, s.getBody()
                );
                f2.addCallback(s2 -> {
                    ListenableFuture<String> f3 = myService.work(s2.getBody());
                    f3.addCallback(s3 -> {
                        dr.setResult(s3);
                    }, e-> {
                        dr.setErrorResult(e.getMessage());
                    });
                }, e -> {
                    dr.setErrorResult(e.getMessage());
                });
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });

            return dr;
        }

        @GetMapping("/restAsyncChain")
        public DeferredResult<String> restAsyncChain(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();
            dr.setResult(rt.getForObject("http://localhost:8082/r1restAsync?idx={idx}", String.class, idx));
            return dr;
        }

        /**
         * Srping에서 비동기 메소드 호출하는 방법
         */
        @Service
        public static class MyService {
            @Async
            public ListenableFuture<String> work(String req) {
                return new AsyncResult<>(req + "/asysncwork");
            }
        }

        @Bean
        public ThreadPoolTaskExecutor myThreadPool() {
            ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
            te.setCorePoolSize(1);
            te.setMaxPoolSize(1);

            te.initialize();
            return te;
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
