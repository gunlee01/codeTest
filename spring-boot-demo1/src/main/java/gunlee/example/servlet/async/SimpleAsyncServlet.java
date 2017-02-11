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
public class SimpleAsyncServlet extends HttpServlet {
	AtomicInteger counter = new AtomicInteger();
	ExecutorService es = Executors.newCachedThreadPool();
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = counter.incrementAndGet();

        AsyncContext asyncContext = req.startAsync();
        Future<?> future = es.submit(() -> {
            doSomething(asyncContext, count);
            return null;
        });
    }
    
    protected void doGet2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = counter.incrementAndGet();

        AsyncContext asyncContext = req.startAsync();
        es.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(asyncContext + " " + count);
			}
        });
    }
    
    protected void doGet3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = counter.incrementAndGet();

        AsyncContext asyncContext = req.startAsync();
        es.execute(MyRunnable::run);
    }
    
    public static class MyRunnable {
    	public static void run() {
    		System.out.println("xx");
    	}
    }

    public void init() throws ServletException {
        super.init();
    }

    public void doSomething(AsyncContext asyncContext, int count) throws InterruptedException {
        Thread.sleep(3000);

        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.println("done " + count);
            out.close();
            asyncContext.complete();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
