package gunlee.example.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 8.
 */
public class AsyncServlet extends HttpServlet {
    public static Map<Integer, HttpServletResponse> resMap = new HashMap<>();
    public static AtomicInteger counter = new AtomicInteger();

    ExecutorService es = Executors.newCachedThreadPool();
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    Future<?> globalFuture;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = counter.incrementAndGet();
        System.out.println("[[[[[[[[[Start ! : " + count);

        if ("1".equals(req.getParameter("a"))) {
            AsyncContext asyncContext = req.startAsync();
            //asyncContext.setTimeout(1500);

            Future<?> future = es.submit(() -> {
                doSomething(asyncContext, count);
                return null;
            });
            globalFuture = future;
        } else if(1==1){
            AsyncContext asyncContext = req.startAsync();
            AtomicInteger loop = new AtomicInteger();

            ses.scheduleAtFixedRate(() -> {
                doSomething2(asyncContext, count, loop.getAndIncrement());
            }, 0, 2, TimeUnit.SECONDS);
        }

        System.out.println("[[[[[[[[[doGet End ! : " + count);
    }

    private void doSomething2(AsyncContext asyncContext, int count, int loop) {
        System.out.println("[[[[[[[[[doSomething2 start ! : " + count + " :: " + loop);

        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.println("doing " + loop);
            out.flush();
            if(loop >= 10) {
                globalFuture.cancel(true);
                out.println("end");
                out.flush();
                out.close();
                asyncContext.complete();
            }
            //asyncContext.complete();
        } catch (Throwable e) {
            System.out.println("%%%%%%%%%%%% exeption $$$$$$$$$$$$$$$");
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[[[[[[[[[[[[[[Servlet] initialized !");
    }

    public void doSomething(AsyncContext asyncContext, int count) throws InterruptedException {
        System.out.println("[[[[[[[[[Async work start ! : " + count);
        Thread.sleep(3000);
        System.out.println("[[[[[[[[[Async work end ! : " + count);

        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.println("done " + count);
            out.close();
            //throw new IllegalArgumentException("My error");
            asyncContext.complete();
        } catch (Throwable e) {
            System.out.println("%%%%%%%%%%%% exeption $$$$$$$$$$$$$$$");
            e.printStackTrace();
        }
    }
}
