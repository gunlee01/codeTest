package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 5.
 * CompletableFuture 한번 탐구해 보자 획지적임
 *
 * 여기서 사용하는 기본 thread는 SumpleAsyncTaskExecutor는 무한정 Thread를 만들기때문에 실전에 사용하면 안된다
 */
//@SpringBootApplication
@Slf4j
@EnableAsync
public class Test402Application {

    @Component
    public static class MyService {
        @Async
        //@Async("pool") - ThreadPoolTaskExecutor Bean이 여러개라면 이름을 지정해 줄 수 있다.
        public ListenableFuture<String> hello() throws InterruptedException {
            log.info("Hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }
    }

    @Bean
    ThreadPoolTaskExecutor pool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10);
        te.setQueueCapacity(50);
        te.setMaxPoolSize(100);
        te.setThreadNamePrefix("MyThread-");
        te.initialize();

        return te;
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext c = SpringApplication.run(Test402Application.class, args)) {
        }
    }

    @Autowired MyService myService;

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.debug("### debug log ####");
            log.info("run()");
            ListenableFuture<String> f = myService.hello();
            f.addCallback(s -> System.out.println(s), e -> System.out.println(e.getMessage()));
            log.info("exit");
        };
    }
}
