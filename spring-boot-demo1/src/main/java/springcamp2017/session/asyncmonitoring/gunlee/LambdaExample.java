package springcamp2017.session.asyncmonitoring.gunlee;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.ExecutorService;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 17.
 */
public class LambdaExample {
    ExecutorService es;
    AsyncRestTemplate asyncRestTemplate;
    String url;

    public void lambdaEx1(int idx) {
        es.execute(() -> {
            System.out.println("inside lambda : " + idx);
        });
    }

    public void lambdaEx2(int idx) {
        ListenableFuture f1 = asyncRestTemplate.getForEntity(url, String.class);
        f1.addCallback(
            s -> System.out.println("inside callback : " + s),
            e -> System.out.println("inside callback error : " + e)
        );
    }
}
