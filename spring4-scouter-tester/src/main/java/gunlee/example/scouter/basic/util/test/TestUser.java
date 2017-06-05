package gunlee.example.scouter.basic.util.test;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by gunlee on 2017. 6. 5.
 */
@Getter
@Setter
public class TestUser {
	private String userId;
	private String userName;

	public String getAnyWithArg2(String a, int b) {
		return "Hello";
	}

	public String getAnyWithArg2x(String a, Integer b) {
		return "Hello";
	}
}
