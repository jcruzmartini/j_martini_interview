package g2webservices.interview.keycard;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import g2webservices.interview.models.keycard.KeyCardRequest;

@RunWith(MockitoJUnitRunner.class)
public class KeyCardAccessSystemTest {
	
	private KeyCardAccessSystem keyCard;
	private final String ALLOWED_KEY = DigestUtils.md5Hex("CCA");
	private final String NOT_ALLOWED_KEY = DigestUtils.md5Hex("XAZZA");
	
	@Mock
	private KeyCardReader reader;
	
	@Before
	public void setUp() throws Exception {
		keyCard = new DummyCardAccessSystem(reader);
	}

	@After
	public void tearDown() throws Exception {
		keyCard = null;
	}

	@Test
	public void testValidRequest() {
		KeyCardRequest request = KeyCardRequest.of(System.currentTimeMillis(), ALLOWED_KEY);
		final boolean isValid = keyCard.isValid(request);
		assertTrue(isValid);
	}
	
	@Test
	public void testNotValidRequest() {
		KeyCardRequest request = KeyCardRequest.of(System.currentTimeMillis(), NOT_ALLOWED_KEY);
		final boolean isValid = keyCard.isValid(request);
		assertFalse(isValid);
	}
	
	@Test
	public void testValidateRequestWithSuccess() {
		when(reader.read()).thenReturn(ALLOWED_KEY);
		final boolean isValid = keyCard.validate();
		assertTrue(isValid);
		verify(reader).read();
	}
	
	@Test
	public void testValidateRequestWithError() {
		when(reader.read()).thenReturn(NOT_ALLOWED_KEY);
		final boolean isValid = keyCard.validate();
		assertFalse(isValid);
		verify(reader).read();
	}
	
}
