package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication
@EnableAsync
@Slf4j
public class DemoApplication {

//	@RestController
//	public static class MyController {
//		@GetMapping("/callable")
//		public Callable<String> callable() throws InterruptedException {
//			log.info("callable");
//			//Thread.sleep(2000);
//			return () -> {
//				log.info("async");
//				Thread.sleep(2000);
//                log.info("async-2");
//				return "hello";
//			};
//		}
//	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
