package gunlee.example.tobyvod.reactive2;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 26.
 */
public class DelegatePolyType<T,R> implements Subscriber<T> {
    Subscriber sub;

    public DelegatePolyType(Subscriber<? super R> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);

    }

    @Override
    public void onNext(T i) {
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
