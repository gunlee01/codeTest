package gunlee.example.servlet.async;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 8.
 */
@Slf4j
public class AsyncServlet extends HttpServlet {
    public static Map<Integer, HttpServletResponse> resMap = new HashMap<>();
    public static AtomicInteger counter = new AtomicInteger();

    ExecutorService es = Executors.newCachedThreadPool();
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    Future<?> globalFuture;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //new Runnable()
    }
}
