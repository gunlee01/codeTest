package springcamp2017.session.asyncmonitoring.gunlee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 15.
 */
public class SimpleLambda {
    int mi = 1000;
    ExecutorService es = Executors.newFixedThreadPool(3);

    public void foo() {
        int i = 100;

        Runnable r = () -> {
            this.mi = 50;
            System.out.println("[lambda expression] Hello spring camp ! " + i + " " + mi);
        };
        System.out.println("[1]" + this.mi);
        r.run();

        System.out.println("[2]" + this.mi);

        Runnable r2 = () -> {
            this.mi = 30;
            System.out.println("[lambda expression2] Hello spring camp ! " + i);
        };

        es.execute(r2);
        System.out.println("[3]" + this.mi);

        Runnable r3 = () -> {
            System.out.println("[lambda expression3] Hello spring camp ! " + i);
        };

        r3.run();
    }

    public static void main(String[] args) {
        new SimpleLambda().foo();
    }
}
