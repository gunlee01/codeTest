package gunlee.example.servlet.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * run run
 *
 *
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class AsyncApplication {

    @RestController
    public static class MyController {

        @GetMapping("/callable")
        public String callable() throws InterruptedException {
            log.info("async");
            Thread.sleep(2000);
            log.info("async-2");
            return "hello";
        }
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new AsyncServlet(),"/asyncservlet");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean2(){
        return new ServletRegistrationBean(new AsyncServlet2(),"/asyncservlet2");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean3(){
        return new ServletRegistrationBean(new AsyncServlet3(),"/asyncservlet3");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean4(){
        return new ServletRegistrationBean(new AsyncServlet4(),"/asyncservlet4");
    }

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }
}
