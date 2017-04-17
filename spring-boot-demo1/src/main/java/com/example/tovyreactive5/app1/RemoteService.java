package com.example.tovyreactive5.app1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 6.
 */
@SpringBootApplication
public class RemoteService {
    @RestController
    public static class MyController {
        @GetMapping("/service5000")
        public String service5000() throws InterruptedException {
            Thread.sleep(   5000);
            return "/service5000";
        }

        @GetMapping("/service")
        public String rest(String req) throws InterruptedException {
            Thread.sleep(   2000);
            return req + "/service1";
        }

        @GetMapping("/service2")
        public String rest2(String req) throws InterruptedException {
            Thread.sleep(   2000);
            return req + "/service2";
        }
    }

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8081");
        System.setProperty("server.tomcat.max-threads", "1000");

        SpringApplication.run(RemoteService.class, args);
    }
}
