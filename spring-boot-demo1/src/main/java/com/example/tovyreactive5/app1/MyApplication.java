package com.example.tovyreactive5.app1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 6.
 *
 * 여기부터 보자 - https://youtu.be/ExUfZkh7Puk?t=2447
 */
@SpringBootApplication
public class MyApplication {

    @RestController
    public static class MyController {
        RestTemplate rt = new RestTemplate();
        AsyncRestTemplate art = new AsyncRestTemplate();

        /**
         * Blocking Sample
         * thread 하나라서 2초에 하나씩 밖에 처리 못함
         */
        @GetMapping("/rest")
        public String rest(int idx) throws InterruptedException {
            String res = rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            return res;
        }

        @GetMapping("/restAsync")
        public ListenableFuture<ResponseEntity<String>> restAsync(int idx) throws InterruptedException {
            ListenableFuture<ResponseEntity<String>> res = art.getForEntity(
                    "http://localhost:8081/service?req={req}",
                    String.class,
                    "hello" + idx);

            //그냥 ListenableFuture를 응답하면 spring이 알아서 한다
            //원래는 callback을 등록해야 하고
            //그런데 callback에서 return은 의미가 없으니. 어 어떻게 해야하지 하고 고민하게 된다.
            //callback은 spring mvc가 알아서 해준다.
            return res;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
