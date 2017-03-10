package gunlee.example.boot.myapp;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 8.
 */
@SpringBootApplication
@EnableAsync
public class FrontApp {

	@RestController
	public static class TestController1 {
		RestTemplate rt = new RestTemplate();
		RestTemplate rtNetty = new RestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
		AsyncRestTemplate art = new AsyncRestTemplate();
		AsyncRestTemplate artNetty = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

		@GetMapping("/f1")
		public DeferredResult<String> f1(int idx) throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();
			dr.setResult(rt.getForObject("http://localhost:9082/b1?idx={req}", String.class, idx));
			return dr;
		}

		@GetMapping("/f2")
		public DeferredResult<String> f2(int idx) throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();
			dr.setResult(rtNetty.getForObject("http://localhost:9082/b1?idx={req}", String.class, idx));
			return dr;
		}

		@GetMapping("/f3")
		public ListenableFuture<ResponseEntity<String>> f3(int idx) throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();
			ListenableFuture<ResponseEntity<String>> res = art.getForEntity(
					"http://localhost:9082/b1?idx={req}",
					String.class,
					idx);

			return res;
		}

		@GetMapping("/f4")
		public ListenableFuture<ResponseEntity<String>> f4(int idx) throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();
			ListenableFuture<ResponseEntity<String>> res = artNetty.getForEntity(
					"http://localhost:9082/b1?idx={req}",
					String.class,
					idx);

			return res;
		}

	}

	@RestController
	@Slf4j
	public static class LambdaExController {

		@GetMapping("/ex1")
		public DeferredResult<String> ex1() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			new Thread(() -> {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dr.setResult("deferred hello!");
			}).start();
			return dr;
		}

		ExecutorService es = Executors.newCachedThreadPool();

		@GetMapping("/ex2")
		public DeferredResult<String> ex2() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			es.submit(() -> {
				Thread.sleep(2000);
				return dr.setResult("Hi~");
			});

			return dr;
		}

		//just lambda
		@GetMapping("/ex3")
		public DeferredResult<String> ex3() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			Runnable r = () -> {
				try {
					Thread.sleep(1000);
					log.info("sync runnable");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};

			r.run();
			dr.setResult("Hi ex3~");

			return dr;
		}

		//lambda and thread
		@GetMapping("/ex4")
		public DeferredResult<String> ex4() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			new Thread(() -> {
				try {
					Thread.sleep(1000);
					log.info("async runnable - ex4");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dr.setResult("deferred hello ex3!");
			}).start();

			Runnable r = () -> {
				try {
					Thread.sleep(1000);
					log.info("sync runnable - ex4");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};

			r.run();

			return dr;
		}
	}

	@RestController
	@Slf4j
	public static class NewRunnableExController {
		@Autowired
		MyAsyncService service;

		@GetMapping("/ex5")
		public DeferredResult<String> ex5() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dr.setResult("ex5 deferred hello!");
				}
			}).start();

			Thread t = new Thread();
			t.setName("my-thread-1");

			return dr;
		}

		public static ExecutorService es2 = Executors.newFixedThreadPool(3);

		@GetMapping("/ex6")
		public DeferredResult<String> ex6() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			es2.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.info("ex6 -1 after sleep");
				}
			});

			Thread.sleep(1000);

			es2.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.info("ex6 - 2 after sleep");
					dr.setResult("ex6");
				}
			});

			return dr;
		}

		@GetMapping("/ex7")
		public DeferredResult<String> ex7() throws InterruptedException {
			DeferredResult<String> dr = new DeferredResult<>();

			Callable<String> myCallable = new Callable<String>() {
				@Override
				public String call() throws Exception {
					Thread.sleep(1000);
					log.info("myCallable in thread : " + System.identityHashCode(this));
					dr.setResult("ex7");
					return "callable";
				}
			};

			log.info("myCallable1 : " + System.identityHashCode(myCallable));

			Future<String> f = es2.submit(myCallable);

			return dr;
		}
	}


	@RestController
	@Slf4j
	public static class SpringAsyncExController {
		@Autowired
		MyAsyncService service;

		@GetMapping("/a1")
		public ListenableFuture<String> a1() throws InterruptedException {
			ListenableFuture<String> result = service.work("imcaller");
			return result;
		}
	}

	@RestController
	@Slf4j
	public static class ManualThreadExController {
		@Autowired
		MyAsyncService service;

		@GetMapping("/mt1")
		public ListenableFuture<String> a1() throws InterruptedException {
			ListenableFuture<String> result = service.work("imcaller");
			return result;
		}
	}


	/**
	 * Srping에서 비동기 메소드 호출하는 방법
	 */
	@Service
	public static class MyAsyncService {
		@Async
		public ListenableFuture<String> work(String req) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			doIt();
			return new AsyncResult<>(req + "/asysncwork");
		}

		public static void doIt() {
			System.out.println("doIt!");
		}
	}

	public static void main(String[] args) {
		System.setProperty("SERVER_PORT", "9081");
		System.setProperty("server.tomcat.max-threads", "1000");

		SpringApplication.run(FrontApp.class, args);
	}
}
