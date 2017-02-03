package gunlee.example.tobyvod.reactive3;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 2.
 * subscribeOn -> publisher가 느린경우
 * publishOn -> subscribe가 느린경우
 */
@Slf4j
public class FluxScEx {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("pubOn"))
                .log()
                .subscribeOn(Schedulers.newSingle("subOn"))
                .subscribe(System.out::println);

    }
}
