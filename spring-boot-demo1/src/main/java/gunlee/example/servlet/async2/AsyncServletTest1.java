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
public class AsyncServletTest1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[async1] start");
        AsyncContext ctx = req.startAsync();

        ctx.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                log.info("[async1][listener]onComplete : t : " + event.getThrowable());
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.info("[async1][listener]onTimeout : t : " + event.getThrowable());
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                log.info("[async1][listener]onError : t : " + event.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("[async1][listener]onStartAsync");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    log.info("[async1][after sleep]");
                    ctx.dispatch("/async2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        doit();
        doit2();
        doit3();
        log.info("[async1] end");
    }

    public void doit() {
        for (int i=0; i<100000; i++) {
            if(i % 10000 == 0) {
                System.out.print(i + ",");
                System.out.println();
            }
        }
    }
    public void doit2() {
        for (int i=0; i<10000; i++) {
            if(i % 10000 == 0) {
                System.out.print(i + ",");
                System.out.println();
            }
        }
    }
    public void doit3() {
        for (int i=0; i<1000; i++) {
            if(i % 1000 == 0) {
                System.out.print(i + ",");
                System.out.println();
            }
        }
    }
}
