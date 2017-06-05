package springcamp2017.session.asyncmonitoring.bcidemo.sampleweb;

import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 20.
 */
public class TestLoader {

    public static void main(String[] args) {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(10);
        AsyncRestTemplate art = new AsyncRestTemplate();
        String url1 = "http://localhost:8081/helloSpring";
        String url2 = "http://localhost:8081/welcomeToSpringCamp";
        long start = System.currentTimeMillis();

        es.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            System.out.println((now-start) + " : " + url1);

            art.getForEntity(url1, String.class);
        }, 0, 2, TimeUnit.SECONDS);

        es.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            System.out.println((now-start) + " : " + url2);

            art.getForEntity(url2, String.class);
        }, 1, 2, TimeUnit.SECONDS);
    }
}
