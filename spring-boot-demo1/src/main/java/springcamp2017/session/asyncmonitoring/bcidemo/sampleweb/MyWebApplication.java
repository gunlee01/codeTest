package springcamp2017.session.asyncmonitoring.bcidemo.sampleweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 21.
 */
@SpringBootApplication
@Slf4j
public class MyWebApplication {

    @RestController
    public static class MyController {

        @GetMapping("/welcomeToSpringCamp")
        public String welcomeToSpringCamp() throws InterruptedException {
            log.info("welcome to spring camp");
            Thread.sleep(4000);
            return "welcomeToSpringCamp !";
        }

        @GetMapping("/helloSpring")
        public String helloSpring() throws InterruptedException {
            log.info("hello spring");
            Thread.sleep(4000);
            return "helloSpring !";
        }
    }


    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8081");
        System.setProperty("server.tomcat.max-threads", "10");

        SpringApplication.run(MyWebApplication.class, args);
    }
}
