package g2webservices.interview.keycard;

import java.util.Scanner;

/**
 * Access key reader for reading input from passenger
 * @author jmartini
 *
 */
public class ManualUserKeyReader implements KeyCardReader {

	@Override
	public String read() {
		System.out.println("Please Enter ACCESS KEY");
		Scanner sc = new Scanner(System.in);
		final String input = sc.nextLine();
		sc.close();
		return input; 
	}

}
