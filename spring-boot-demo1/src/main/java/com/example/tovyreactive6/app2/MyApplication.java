package com.example.tovyreactive6.app2;

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
 * 여기서 리팩토링한다
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 11.
 */
@SpringBootApplication
public class MyApplication {
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
                    .andError(e->dr.setErrorResult(e.toString()))
                    .andAccept(s->dr.setResult(s.getBody()));

            return dr;
        }

        public static class AcceptCompletion extends Completion {
            Consumer<ResponseEntity<String>> consumer;
            public AcceptCompletion(Consumer<ResponseEntity<String>> consumer) {
                this.consumer = consumer;
            }

            @Override
            void run(ResponseEntity<String> value) {
                consumer.accept(value);
            }
        }

        public static class ApplyCompletion extends Completion {
            Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn;
            public ApplyCompletion(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
                this.fn = fn;
            }

            @Override
            void run(ResponseEntity<String> value) {
                ListenableFuture<ResponseEntity<String>> lf = fn.apply(value);
                lf.addCallback(s->complete(s), e->error(e));
            }
        }

        public static class ErrorCompletion extends Completion {
            Consumer<Throwable> econ;
            public ErrorCompletion(Consumer<Throwable> econ) {
                this.econ = econ;
            }

            @Override
            void run(ResponseEntity<String> value) {
                if (next != null) next.run(value);
            }

            @Override
            void error(Throwable t) {
                econ.accept(t);
            }
        }

        public static class Completion {
            Completion next;

            public void andAccept(Consumer<ResponseEntity<String>> consumer) {
                Completion c = new AcceptCompletion(consumer);
                this.next = c;
            }

            public Completion andError(Consumer<Throwable> econ) {
                Completion c = new ErrorCompletion(econ);
                this.next = c;
                return c;
            }

            public Completion andApply(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
                Completion c = new ApplyCompletion(fn);
                this.next = c;
                return c;
            }

            public static Completion from(ListenableFuture<ResponseEntity<String>> lf) {
                Completion c = new Completion();
                lf.addCallback(s-> c.complete(s), e-> c.error(e));
                return c;
            };

            void error(Throwable e) {
                if(next != null) next.error(e);
            }
            void complete(ResponseEntity<String> s) {
                if(next != null) next.run(s);
            }

            void run(ResponseEntity<String> value) {};
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
        SpringApplication.run(MyApplication.class, args);
    }
}
