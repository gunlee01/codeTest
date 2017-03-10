package gunlee.bcitestapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 2.
 */
@SpringBootApplication
@Slf4j
public class TestApp1 {
	public static void main(String[] args) {
		System.setProperty("SERVER_PORT", "8091");
		System.setProperty("server.tomcat.max-threads", "100");

		SpringApplication.run(TestApp1.class, args);
	}

	@RestController
	@Slf4j
	public static class ExController1 {
		@GetMapping("/home1")
		public String home1(int inx) throws InterruptedException {
			log.info("[start]/home1");
			return "home1:" + inx;
		}

		@GetMapping("/sleep1")
		public String sleep1() throws InterruptedException {
			log.info("sleep start");
			TimeUnit.SECONDS.sleep(5);
			log.info("sleep end");
			return "sleep end";
		}
	}
}
