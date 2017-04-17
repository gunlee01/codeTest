package springcamp2017.session.asyncmonitoring.gunlee.threadlocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */

public class SimpleThreadLocal {
    private static Map threadLocalMap = new ConcurrentHashMap();

    public void set(Object o) {
        threadLocalMap.put(Thread.currentThread(), o);
    }

    public Object get() {
        return threadLocalMap.get(Thread.currentThread());
    }
}


