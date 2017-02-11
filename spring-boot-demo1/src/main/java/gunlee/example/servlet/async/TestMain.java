package gunlee.example.servlet.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 11.
 */
public class TestMain {
    public static void main(String[] args) {
        doItRunnable();
        doItCallable();
        doItLambdaRunnable();
        doItLambdaCallble();
    }

    private static ExecutorService es = Executors.newCachedThreadPool();

    private static void doItRunnable() {
        String arg1 = "arg1";
        es.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(arg1);
                System.out.println("========= doItRunnable ==============");
                System.out.println(this.getClass().getName());
            }
        });
    }

    private static void doItCallable() {
        String arg1 = "arg1";
        Future<String> f = es.submit(new Callable<String>() {
            @Override
            public String call() {
                System.out.println(arg1);
                System.out.println("========= doItCallable ==============");
                System.out.println(this.getClass().getName());
                return arg1;
            }
        });
    }

    private static void doItLambdaCallble() {
        String arg1 = "arg1";
        es.execute(() -> System.out.println(arg1));
        System.out.println("========= doItLambdaCallble ==============");
        //System.out.println(this.getClass().getName());
    }

    private static void doItLambdaRunnable() {
        String arg1 = "arg1";
        Future<String> f = es.submit(() -> {
            System.out.println(arg1);
            return arg1;
        });
    }

}
