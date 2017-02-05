package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test/{id}/1")
    public String m1() throws InterruptedException {
        log.info("test-m1");
        return "hello-m1";
    }

    @GetMapping("/test/{id}/{dept}/book")
    public String m2() throws InterruptedException {
        log.info("test-m2");
        return "hello-m2";
    }

    @GetMapping("/test/{id}/xxx/pass")
    public String m3() throws InterruptedException {
        log.info("test-m3-pass-to-m1");
        return m1();
    }

    @GetMapping("/test/{id}/ [:SRM]/xx")
    public String m4() throws InterruptedException {
        log.info("test-m4");
        return "m4";
    }
}
