package g2webservices.interview.models.keycard;

public class KeyCardRequest {

	private final long timestamp;
	private final String key; // this should be encrypted

	public KeyCardRequest(long timestamp, String key) {
		this.timestamp = timestamp;
		this.key = key;
	}

	public static KeyCardRequest of(final long timestamp, final String key) {
		return new KeyCardRequest(timestamp, key);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getKey() {
		return key;
	}
	
}
