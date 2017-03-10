package gunlee.example.anyMain;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 13.
 */
public class TestMain1 {
	public static void main(String[] args) {
		System.out.println("Thread : " + Thread.currentThread().getName());

		Thread t = new Thread(new MyRunnable());
		t.start();

		new Thread(()->System.out.println("Hello")).start();
	}

	public static class MyRunnable implements Runnable {

		@Override
		public void run() {
			System.out.println("Runnable In Run");
		}
	}

}
