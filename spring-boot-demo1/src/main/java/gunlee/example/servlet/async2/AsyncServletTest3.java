package gunlee.example.servlet.async2;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 25.
 */
@Slf4j
public class AsyncServletTest3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[async3] start");
        AsyncContext ctx = req.startAsync();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ctx.dispatch("/async2");
                ctx.dispatch("/async3");
            }
        }).start();
        log.info("[async3] end");
    }
}
