package gunlee.example.scouter.basic;

import gunlee.example.scouter.basic.util.test.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gunlee on 2017. 6. 3.
 */
@SpringBootApplication
@Slf4j
public class BasicTest {

	@RestController
	public static class SampleController {

		@GetMapping("/hello")
		public String hello() {
			return "Hello";
		}

		//test agent scripting plugin - use reflecting test
		@GetMapping("/callMethodTest")
		public String callMethod() {
			TestUser testUser = new TestUser();
			doTest1(testUser);
			return "callMethodTest";
		}

		private void doTest1(TestUser user) {
			log.debug("doTest1()");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BasicTest.class, args);
	}
}
