package com.example.tovyreactive6.app3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
 * Generic 으로 만드는 것
 * 이거 직접 한번 해봐야 할 듯.
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
                    .andApply(s->myService.work(s.getBody()))
                    .andError(e->dr.setErrorResult(e.toString()))
                    .andAccept(s->dr.setResult(s));

            return dr;
        }
    }

    public static class AcceptCompletion<S> extends Completion<S, Void> {
        Consumer<S> consumer;
        public AcceptCompletion(Consumer<S> consumer) {
            this.consumer = consumer;
        }

        @Override
        void run(S value) {
            consumer.accept(value);
        }
    }

    public static class ApplyCompletion<S, T> extends Completion<S, T> {
        public Function<S, ListenableFuture<T>> fn;
        public ApplyCompletion(Function<S, ListenableFuture<T>> fn) {
            this.fn = fn;
        }

        @Override
        void run(S value) {
            ListenableFuture<T> lf = fn.apply(value);
            lf.addCallback(s->complete(s), e->error(e));
        }
    }

    public static class ErrorCompletion<T> extends Completion<T,T> {
        Consumer<Throwable> econ;
        public ErrorCompletion(Consumer<Throwable> econ) {
            this.econ = econ;
        }

        @Override
        void run(T value) {
            if (next != null) next.run(value);
        }

        @Override
        void error(Throwable t) {
            econ.accept(t);
        }
    }

    public static class Completion<S, T> {
        Completion next;

        public void andAccept(Consumer<T> consumer) {
            Completion<T, Void> c = new AcceptCompletion<>(consumer);
            this.next = c;
        }

        public Completion<T, T> andError(Consumer<Throwable> econ) {
            Completion<T, T> c = new ErrorCompletion<>(econ);
            this.next = c;
            return c;
        }

        public <V> Completion<T,V> andApply(Function<T, ListenableFuture<V>> fn) {
            Completion<T, V> c = new ApplyCompletion<>(fn);
            this.next = c;
            return c;
        }

        /**
         * 얘는 static이니까 어차피 class에서 정의한 타입 파라미터와는 상관이 없다
         * 메소드의 타입 파라미터를 정의하자
         */
        public static <S, T> Completion<S, T> from(ListenableFuture<T> lf) {
            Completion<S, T> c = new Completion<>();
            lf.addCallback(s-> c.complete(s), e-> c.error(e));
            return c;
        };

        void error(Throwable e) {
            if(next != null) next.error(e);
        }
        void complete(T s) {
            if(next != null) next.run(s);
        }

        void run(S value) {};
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

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
