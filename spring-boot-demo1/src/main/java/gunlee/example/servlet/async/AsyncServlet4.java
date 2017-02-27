package gunlee.example.servlet.async;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 8.
 */
@Slf4j
public class AsyncServlet4 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[[[[[[[[[Start AsyncServlet4! & added=" + req.getAttribute("added"));

        if ("1".equals(req.getParameter("a"))) {
            AsyncContext actx = req.startAsync();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        log.info("[after3 sleep1]");
                        actx.complete();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {

        }

    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[[[[[[[[[[[[[[Servlet4] initialized !");
    }


}
