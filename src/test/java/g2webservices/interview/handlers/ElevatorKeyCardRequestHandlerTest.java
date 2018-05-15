package g2webservices.interview.handlers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import g2webservices.interview.handlers.ElevatorKeyCardRequestHandler;
import g2webservices.interview.handlers.ElevatorRequestHandler;
import g2webservices.interview.keycard.DummyCardAccessSystem;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorKeyCardRequestHandlerTest {
	
	private ElevatorRequestHandler handler;
	@Mock
	private Elevator elevator;
	@Mock
	private ElevatorState elevatorState;

	@Mock
	private DummyCardAccessSystem keyCard;
	
	@Before
	public void setUp() throws Exception {
		handler = new ElevatorKeyCardRequestHandler(elevator, keyCard);
	}

	@After
	public void tearDown() throws Exception {
		elevator = null;
		handler = null;
		keyCard = null;
	}

	@Test
	public void testRequestForRestrictedWithAuthenticationOK() {
		final ElevatorRequest request = new ElevatorRequest(-1, 1f);
		when(elevator.getRestrictedFloors()).thenReturn(new HashSet<>(Arrays.asList(-1, 50)));
		when(elevator.getState()).thenReturn(elevatorState);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(keyCard.validate()).thenReturn(true);
		when(elevator.openDoor()).thenReturn(true);
		when(elevatorState.getCurrent()).thenReturn(0, 0, -1);
		
		handler.process(request);

		verify(elevator, times(1)).down();
		verify(keyCard, times(1)).validate();
		verify(elevator, times(1)).openDoor();
		verify(elevator, times(1)).closeDoor();
	}
	
	@Test
	public void testMove50To40WithIntermediateStopsToSecurizedFloorWithAccessOK() {
		final ElevatorRequest requestTo50 = new ElevatorRequest(40, 1f);
		
		when(elevator.getState()).thenReturn(elevatorState);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(elevator.getRestrictedFloors()).thenReturn(new HashSet<>(Arrays.asList(45)));
		when(elevator.openDoor()).thenReturn(true);
		when(elevatorState.getCurrent()).thenReturn(50, 50, 49, 48, 47, 46, 45, 45, 44);
		when(keyCard.validate()).thenReturn(true);
		when(elevator.openDoor()).thenReturn(true);
		
		//intermediate stops added by request manager
		handler.addStop(45);
		handler.addStop(44);
		
		handler.process(requestTo50);
		
		verify(elevator, times(10)).down();
		verify(elevator, times(3)).openDoor();
		verify(elevator, times(3)).closeDoor();
		verify(elevator, never()).up();
		assertTrue(handler.getIntermediateStops().isEmpty());
		
	}
	
	@Test
	public void testMove50To40WithIntermediateStopsToSecurizedFloorWithAccessFailed() {
		final ElevatorRequest requestTo50 = new ElevatorRequest(40, 1);
		
		when(elevator.getState()).thenReturn(elevatorState);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(elevator.getRestrictedFloors()).thenReturn(new HashSet<>(Arrays.asList(45)));
		when(elevator.openDoor()).thenReturn(true);
		when(elevatorState.getCurrent()).thenReturn(50, 50, 49, 48, 47, 46, 45, 45, 44);
		when(keyCard.validate()).thenReturn(false);
		when(elevator.openDoor()).thenReturn(false);
		
		//intermediate stops added by request manager
		handler.addStop(45);
		handler.addStop(44);
		
		handler.process(requestTo50);
		
		verify(elevator, times(10)).down();
		verify(elevator, times(2)).openDoor();
		verify(elevator, times(2)).closeDoor();
		verify(elevator, never()).up();
		assertTrue(handler.getIntermediateStops().isEmpty());
		
	}
	
	@Test
	public void testRequestForRestrictedWithAuthenticationFailed() {
		final ElevatorRequest request = new ElevatorRequest(-1, 1);
		when(elevator.getRestrictedFloors()).thenReturn(new HashSet<>(Arrays.asList(-1,50)));
		when(elevator.getState()).thenReturn(elevatorState);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(keyCard.validate()).thenReturn(false);
		when(elevatorState.getCurrent()).thenReturn(0, 0, -1);
		
		handler.process(request);

		verify(elevator, times(1)).down();
		verify(keyCard, times(1)).validate();
		verify(elevator, never()).openDoor();
		verify(elevator, never()).closeDoor();
	}

}
