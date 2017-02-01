package gunlee.example.tobyvod.reactive2;

import reactor.core.publisher.Flux;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 1.
 *
 * 보자
 * http://projectreactor.io/docs/core/release/api/
 *  -Flux
 */
public class PubSub7ReactorEx {
    public static void main(String[] args) {
//        Flux.<Integer>create(e -> {
//            e.next(1);
//            e.next(2);
//            e.next(3);
//            e.complete();
//        })
//                .log()
//                .map(s->s*10)
//                .log()
//                .subscribe(System.out::println);

        Flux.<Integer>create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
                .log()
                .map(s->s*10)
                .reduce(0, (a,b)->a+b)
                .log()
                .subscribe(System.out::println);
    }
}
