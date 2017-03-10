package gunlee01.toy.bcitest;

import java.lang.instrument.Instrumentation;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 2.
 */
public class AgentMain {
	private static Instrumentation instrumentation;
	public static Instrumentation getInstrumentation() {
		return instrumentation;
	}

	public static void premain(String args, Instrumentation inst) throws Exception {
		System.out.println("[gun] start premain");
		try {
			instrumentation = inst;
			instrumentation.addTransformer(new TestTransformer());

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
