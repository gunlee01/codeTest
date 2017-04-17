package springcamp2017.session.asyncmonitoring.gunlee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */
public class RunnablePropagationTest {
    static ExecutorService es = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {


        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("[My id]" + System.identityHashCode(this));
            }
        };

        System.out.println("[Calling runnable id]" + System.identityHashCode(r));

        es.execute(r);
    }
}


class Executor {
    public void exeucte(Runnable r) {

    }
}

//class RunnableWrapper extends Runnable {
//    Runnable r;
//
//    public RunnableWrapper(Runnable r) {
//        this.r = r;
//    }
//
//    @Override
//    public void run() {
//
//    }
//}