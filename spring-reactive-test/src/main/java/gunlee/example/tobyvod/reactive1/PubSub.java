package gunlee.example.tobyvod.reactive1;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 26.
 */
public class PubSub {
    public static void main(String[] args) {
        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);

        Publisher p = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long l) {

                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
