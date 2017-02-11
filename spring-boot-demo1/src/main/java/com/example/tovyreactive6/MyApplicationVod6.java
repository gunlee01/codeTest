package com.example.tovyreactive6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * app2.MyApplication에서 리팩토링
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 11.
 */
@SpringBootApplication
public class MyApplicationVod6 {
    @RestController
    public static class MyController {
        public static final String URL1 = "http://localhost:8081/service?req={req}";
        public static final String URL2 = "http://localhost:8081/service2?req={req}";
        AsyncRestTemplate rt = new AsyncRestTemplate();

        @Autowired
        MyService myService;

        /**
         * 콜백헬을 벗어나는 원리
         */
        @GetMapping("/rest")
        public DeferredResult<String> rest(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();

            Completion
                    .from(rt.getForEntity(URL1, String.class, "hello" + idx))
                    .andApply(s->rt.getForEntity(URL2, String.class, s.getBody()))
                    .andAccept(s->dr.setResult(s.getBody()));

            return dr;
        }

        public static class Completion {
            Completion next;
            Consumer<ResponseEntity<String>> consumer;
            Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn;

            public Completion() {
            }

            public Completion(Consumer<ResponseEntity<String>> consumer) {
                this.consumer = consumer;
            }

            public Completion(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
                this.fn = fn;
            }

            public void andAccept(Consumer<ResponseEntity<String>> consumer) {
                Completion c = new Completion(consumer);
                this.next = c;
            }

            public Completion andApply(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
                Completion c = new Completion(fn);
                this.next = c;
                return c;
            }

            public static Completion from(ListenableFuture<ResponseEntity<String>> lf) {
                Completion c = new Completion();
                lf.addCallback(s-> c.complete(s), e-> c.error(e));
                return c;
            };

            private void error(Throwable e) {
            }
            private void complete(ResponseEntity<String> s) {
                if(next != null) next.run(s);
            }

            private void run(ResponseEntity<String> value) {
                if (consumer != null) consumer.accept(value);
                else if (fn != null) {
                    ListenableFuture<ResponseEntity<String>> lf = fn.apply(value);
                    lf.addCallback(s->complete(s), e->error(e));
                }
            }


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
            te.initialize();
            return te;
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplicationVod6.class, args);
    }
}
