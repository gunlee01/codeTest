package gunlee.example.servlet.async;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
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
@Slf4j
public class AsyncServlet extends HttpServlet {
    public static Map<Integer, HttpServletResponse> resMap = new HashMap<>();
    public static AtomicInteger counter = new AtomicInteger();

    ExecutorService es = Executors.newCachedThreadPool();
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    Future<?> globalFuture;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = counter.incrementAndGet();
        log.info("[[[[[[[[[Start ! : " + count);

        if ("1".equals(req.getParameter("a"))) {
            log.info("####11111111111");
            AsyncContext asyncContext = req.startAsync();
            //asyncContext.setTimeout(1500);

            Future<?> future = es.submit(() -> {
                doSomething(asyncContext, count);
                return null;
            });
            globalFuture = future;

        } else if ("2".equals(req.getParameter("a"))) {
            log.info("####222222222222");
            AsyncContext asyncContext = req.startAsync();

            log.info("AsyncContext=" + asyncContext);

            asyncContext.addListener(new AsyncListener() {
                AsyncContext myCtx = asyncContext;

                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    log.info("event:onComplete");
                    log.info("event:onComplete:myCtx=" + myCtx);
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    log.info("event:onTimeout");
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    log.info("event:onError");
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    log.info("event:onStartAsync");
                }
            });
            es.execute(() -> {
                try {
                    doSomething(asyncContext, count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        } else if ("3".equals(req.getParameter("a"))) {
            log.info("####33333333333");
            AsyncContext asyncContext = req.startAsync();

            es.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        AsyncServlet.this.doSomething(asyncContext, count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if(1==1){
            AsyncContext asyncContext = req.startAsync();
            AtomicInteger loop = new AtomicInteger();

            ses.scheduleAtFixedRate(() -> {
                doSomething2(asyncContext, count, loop.getAndIncrement());
            }, 0, 2, TimeUnit.SECONDS);
        }

        log.info("[[[[[[[[[doGet End ! : " + count);
    }

    private void doSomething2(AsyncContext asyncContext, int count, int loop) {
        log.info("[[[[[[[[[doSomething2 start ! : " + count + " :: " + loop);

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
            log.info("%%%%%%%%%%%% exeption $$$$$$$$$$$$$$$");
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[[[[[[[[[[[[[[Servlet] initialized !");
    }

    public void doSomething(AsyncContext asyncContext, int count) throws InterruptedException {
        log.info("[[[[[[[[[Async work start ! : " + count);
        Thread.sleep(3000);
        log.info("[[[[[[[[[Async work end ! : " + count);

        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.println("done " + count);
            out.close();
            //throw new IllegalArgumentException("My error");
            asyncContext.complete();
        } catch (Throwable e) {
            log.info("%%%%%%%%%%%% exeption $$$$$$$$$$$$$$$");
            e.printStackTrace();
        }
    }
}
