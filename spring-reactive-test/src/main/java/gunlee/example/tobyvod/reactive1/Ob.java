package gunlee.example.tobyvod.reactive1;

import java.util.Iterator;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 12.
 */
public class Ob {
	public static void main(String[] args) {

		Iterable<Integer> iter = () -> new Iterator<Integer>() {
			int i = 0;
			final static int MAX = 10;

			@Override
			public boolean hasNext() {
				return i < MAX;
			}

			@Override
			public Integer next() {
				return ++i;
			}
		};

		for(Integer i : iter) {
			System.out.println(i);
		}
	}
}
