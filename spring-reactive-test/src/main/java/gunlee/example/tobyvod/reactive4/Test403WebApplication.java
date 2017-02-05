package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 5.
 * Servlet 3.0 비동기 서블릿
 *   http는 논블로킹 io, 그러나 서블릿 요청, 응답은 블로킹
 * Servlet 3.1 논블로킹 IO - Callback
 *
 *
 * 이건 spring-starter-web 에서 된다. reactive-web 에서는 안된다.
 */
@SpringBootApplication
@Slf4j
@EnableAsync
public class Test403WebApplication {

    @RestController
    public static class MyController {
        @GetMapping("/callable")
        public Callable<String> callable() throws InterruptedException {
            log.info("callable");
            //Thread.sleep(2000);
            return () -> {
                log.info("async");
                Thread.sleep(2000);
                return "hello";
            };
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Test403WebApplication.class, args);
    }
}
