package g2webservices.interview.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
	public static void simulate(int sec) {
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			// interruption expected
		}
	}
}
