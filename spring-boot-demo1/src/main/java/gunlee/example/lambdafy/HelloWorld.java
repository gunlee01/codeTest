package gunlee.example.lambdafy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 16.
 */
@Slf4j
public class HelloWorld implements Runnable {

	@Override
	public void run() {
		System.out.println("Hello ASM!!");
	}

	public static void main(String[] args) {
		Runnable r = () -> doIt();
		r.run();
	}

	private static void doIt() {
		log.info("doIt");
		log.error("err", new Exception());
	}
}
