package gunlee.example.tobyvod.reactive2;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 26.
 *         <p>
 *         Operator
 *         1. map
 *         https://youtu.be/DChIxy9g19o?t=2796 여기부터 재생
 */
@Slf4j
public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
        //Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
        Publisher<Integer> sumPub = sumPub(pub);
        sumPub.subscribe(logSub());
    }

    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return sub -> {
            pub.subscribe(new DelegateSub(sub) {
                int sum = 0;
                @Override
                public void onNext(Integer i) {
                    sum += i;
                }

                @Override
                public void onComplete() {
                    sub.onNext(sum);
                    sub.onComplete();
                }
            });
        };
    }

    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    @Override
                    public void onNext(Integer i) {
                        super.onNext(f.apply(i));
                    }
                });
            }
        };
    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer i) {
                log.debug("onNext:{}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.error("onError:{}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(List<Integer> iter) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iter.forEach(s -> sub.onNext(s));
                            sub.onComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
