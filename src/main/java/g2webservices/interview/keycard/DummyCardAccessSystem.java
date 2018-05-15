package g2webservices.interview.keycard;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import g2webservices.interview.models.keycard.KeyCardRequest;

/**
 * Card System Implementation using a dummy mechanism of authentication
 * Allowed keys are 902fbdd2b1df0c4f70b4a5d23525e932, c4fd1ef4041f00039def6df0331841de, d8c072a439c55274f145eae9f6583751
 * @author jmartini
 *
 */
public class DummyCardAccessSystem implements KeyCardAccessSystem {

	private final KeyCardReader reader;
	
	public DummyCardAccessSystem(KeyCardReader reader) {
		this.reader = reader;
	}

	private final List<String> keys = Arrays.asList(DigestUtils.md5Hex("ABC"), DigestUtils.md5Hex("ACC"),
			DigestUtils.md5Hex("CCA")); // mocked allowed keys for dummy card access system

	@Override
	public String getAccessKey() {
		// in a real world case , we need to
		// interact with reader keycard system here 
		// and get the token of access card that is being used
		return reader.read();
	}

	@Override
	public boolean validate() {
		final KeyCardRequest request = prepareRequest();
		return isValid(request);
	}

	@Override
	public KeyCardRequest prepareRequest() {
		final String key = getAccessKey();
		return KeyCardRequest.of(System.currentTimeMillis(), key);
	}

	@Override
	public boolean isValid(KeyCardRequest request) {
		// in a real world scenario this should encrypt keys and validate
		// against external db or API. 
		// Communication need to be done at this point
		final boolean isValid = keys.contains(request.getKey());
		System.out.println("Key Validation Result " + isValid );
		return isValid;
	}

}
