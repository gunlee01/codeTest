package gunlee.example.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 14.
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class TraceMethodTestApp {
	@RestController
	public static class MyController {

		/**
		 * Inner class call
		 */
		@GetMapping("/test1")
		public String test1() throws InterruptedException {
			new InMyClass().doHello();
			return "hello test1";
		}

		/**
		 * anonymous class call
		 */
		@GetMapping("/test2")
		public String test2() throws InterruptedException {
			new Runnable() {
				@Override
				public void run() {
					doIt();
				}
			}.run();
			return "hello test2";
		}

		/**
		 * method reference call
		 */
		@GetMapping("/test3")
		public String test3() throws InterruptedException {
			Runnable r = () -> System.out.println("lambda");
			r.run();
			return "hello test3";
		}
		
		public static String staticTest() throws InterruptedException {
			Runnable r = () -> System.out.println("Inside");
			r.run();
			return "hello test3";
		}

		/**
		 * lambda call
		 */
		@GetMapping("/test4")
		public String test4() throws InterruptedException {
			Runnable r = () -> doIt();
			r.run();
			return "hello test4";
		}

		public void doIt() {
			log.info("do It");
		}
	}

	public static class InMyClass {
		public void doHello() {
			log.info("Hello~~ Inner class");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(TraceMethodTestApp.class, args);
	}
}
