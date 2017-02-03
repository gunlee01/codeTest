package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 3.
 * 좀더 세련되게
 *
 */
@Slf4j
public class FutureEx3 {
    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onEror(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //그냥 이 정도만 하면 될듯
            } catch (ExecutionException e) {
                ec.onEror(e.getCause());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            if (true) throw new RuntimeException("Async Error !!!");

            log.info("Async");
            return "Hello";
        },
                res -> System.out.println(res),
                e -> System.out.println("Error: " + e.getMessage()));

        es.execute(f);
        es.shutdown();
        log.info("Exit");

    }
}
