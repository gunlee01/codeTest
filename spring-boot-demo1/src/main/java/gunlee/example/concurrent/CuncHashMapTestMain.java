package gunlee.example.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 14.
 */
@Slf4j
public class CuncHashMapTestMain {

	public static void main(String[] args) {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("has", "solo");
		map.put("r2", "d3");
		map.put("c3", "p0");
		map.put("has1", "solo");
		map.put("r23", "d3");
		map.put("c34", "p0");
		map.put("hasc", "solo");
		map.put("r2f", "d3");
		map.put("c33", "p0");

		System.out.println(ForkJoinPool.getCommonPoolParallelism());

		map.forEach((k, v) -> {
			log.info("key : {} and value : {}", k, v);
		});

		map.forEach(100, (key, value) -> {

			log.info("key : {} and value : {}", key, value);
		});
	}
}
