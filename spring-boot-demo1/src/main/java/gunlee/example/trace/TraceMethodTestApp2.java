package gunlee.example.trace;

public class TraceMethodTestApp2 {
	
		public void test3() throws InterruptedException {
			Runnable r = () -> System.out.println("lambda");
			r.run();
		}
		
		public void test4() throws ClassNotFoundException {
			Class.forName("xxxxx");
		}
		
		public void do1() {
			System.out.println("Hello:");
		}
		
//		public static String staticTest() throws InterruptedException {
//			Runnable r = () -> System.out.println("Inside");
//			r.run();
//			return "hello test3";
//		}
}
