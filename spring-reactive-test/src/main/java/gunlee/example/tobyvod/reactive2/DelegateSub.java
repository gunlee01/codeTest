package gunlee.example.tobyvod.reactive2;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 26.
 */
public class DelegateSub implements Subscriber<Integer> {
    Subscriber sub;

    public DelegateSub(Subscriber sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);

    }

    @Override
    public void onNext(Integer i) {
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}
