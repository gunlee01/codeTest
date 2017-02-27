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
public class AsyncServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("a");
        log.info("[[[[[[[[[Start AsyncServlet2! : " + a);

        if ("1".equals(a)) {
            AsyncContext actx = req.startAsync();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        log.info("[after sleep]");
                        //actx.complete();
                        req.setAttribute("added", "ok");
                        actx.dispatch("/asyncservlet3?a=1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else if ("2".equals(a)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            req.getRequestDispatcher("//asyncservlet3?a=1").forward(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[[[[[[[[[[[[[[Servlet2] initialized !");
    }


}
