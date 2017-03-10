package gunlee.example.boot.remoteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 8.
 */
@SpringBootApplication
public class BackEndApp {
	@RestController
	public static class TestController1 {
		RestTemplate rt = new RestTemplate();
		//AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

		@GetMapping("/b1")
		public String restAsync(int idx, HttpServletRequest req) throws InterruptedException {
			System.out.println("X-Scouter-Gxid : " + req.getHeader("X-Scouter-Gxid"));
			System.out.println("X-Scouter-Callee : " + req.getHeader("X-Scouter-Callee"));
			System.out.println("X-Scouter-Caller : " + req.getHeader("X-Scouter-Caller"));

			Thread.sleep(2000);
			return "b1:" + idx;
		}
	}

	public static void main(String[] args) {
		System.setProperty("SERVER_PORT", "9082");

		SpringApplication.run(BackEndApp.class, args);
	}
}
