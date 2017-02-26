package gunlee.example.servlet.async2;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 25.
 */
@Slf4j
public class AsyncServletTest2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[async2] start");
        AsyncContext ctx = req.startAsync();

        //ctx.setTimeout(1500);

        ctx.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                log.info("[async2][listener]onComplete : t : " + event.getThrowable());
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.info("[async2][listener]onTimeout : t : " + event.getThrowable());
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                log.info("[async2][listener]onError : t : " + event.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("[async2][listener]onStartAsync");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("[async2] after sleep");
                ctx.dispatch("/sync1");
            }
        }).start();
        log.info("[async2] end");
    }
}
