package gunlee.example.tobyvod.reactive3;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 2.
 * 따로 subOn 을 하지 않아도 알아서 thread를 만들어 주는 것도 있다
 */
@Slf4j
public class FluxScEx2 {
    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofMillis(200))
                .take(10)
                .subscribe(s -> log.debug("onNext:{}", s));
        log.debug("exit");
        TimeUnit.SECONDS.sleep(10);
    }
}
