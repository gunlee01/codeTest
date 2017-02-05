package gunlee.example.scouterx.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 5.
 */
@Slf4j
public class RestUrl {

    @RestController
    public static class MyTestController {

        @GetMapping("/rest/{id}/test")
        public String m1() throws InterruptedException {
            log.info("m1");
            return "hello";
        }
    }
}
