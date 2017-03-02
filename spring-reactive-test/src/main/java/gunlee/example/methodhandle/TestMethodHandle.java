package gunlee.example.methodhandle;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 27.
 */
public class TestMethodHandle {

	@Test
	public void test1() throws Throwable {
		MethodType methodType = MethodType.methodType(int.class, new Class<?>[]{String.class});
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle methodHandle = lookup.findStatic(Counter.class, "count", methodType);
		int count = (int)methodHandle.invokeExact("foo");

		assertThat(count, is(3));
	}
}

class Example {
	void doSomething() {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
	}

	private void foo() {

	}
}

class Counter {
	static int count(String name) {
		return name.length();
	}
}
