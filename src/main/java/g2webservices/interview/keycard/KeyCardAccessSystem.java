package g2webservices.interview.keycard;

import g2webservices.interview.models.keycard.KeyCardRequest;

public interface KeyCardAccessSystem {
	
	String getAccessKey();
	boolean validate();
	KeyCardRequest prepareRequest();
	boolean isValid(KeyCardRequest request);

}
