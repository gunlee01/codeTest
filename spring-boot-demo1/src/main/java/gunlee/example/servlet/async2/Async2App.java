package gunlee.example.servlet.async2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 25.
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class Async2App {

    @RestController
    public static class MyController {
        ExecutorService es = Executors.newCachedThreadPool();

        @GetMapping("/callable")
        public String callable() throws InterruptedException {
            log.info("async");
            Thread.sleep(2000);
            log.info("async-2");
            return "hello";
        }

        @GetMapping("/ex1")
        public Future<String> ex2() throws InterruptedException {
            log.info("future example start");
            log.info("future example end");
            Future<String> f = es.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(3000);
                    return "hello";
                }
            });
            return f;
        }
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean1(){
        return new ServletRegistrationBean(new AsyncServletTest1(),"/async1");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean2(){
        return new ServletRegistrationBean(new AsyncServletTest2(),"/async2");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean3(){
        return new ServletRegistrationBean(new AsyncServletTest3(),"/async3");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean4(){
        return new ServletRegistrationBean(new AsyncServletTest4(),"/async4");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean5(){
        return new ServletRegistrationBean(new SyncServletTest1(),"/sync1");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean6(){
        return new ServletRegistrationBean(new SyncServletTest2(),"/sync2");
    }

    public static void main(String[] args) {
        SpringApplication.run(gunlee.example.servlet.async2.Async2App.class, args);
    }
}
