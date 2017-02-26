package com.example.tovyreactive5.app1;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 6.
 */
@SpringBootApplication
public class Remote1Service {

    @RestController
    public static class MyController2 {
        RestTemplate rt = new RestTemplate();
        AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

        @GetMapping("/r1restAsync")
        public DeferredResult<String> restAsync(int idx) throws InterruptedException {
            DeferredResult<String> dr = new DeferredResult<>();
            dr.setResult(rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx));
            return dr;
        }
    }

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8082");
        System.setProperty("server.tomcat.max-threads", "1000");

        SpringApplication.run(Remote1Service.class, args);
    }
}
