package gunlee.example.servlet.async2;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 25.
 */
@Slf4j
public class SyncServletTest1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[sync1] start");
        try {
            Thread.sleep(1000);
            //log.error("test exception", new Exception());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("[sync1] mid1");
//        if(1==1) {
//            throw new RuntimeException("Intentional Exception");
//        }
        log.info("[sync1] end");
    }
}
