package g2webservices.interview.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.enums.StatusEnum;
import g2webservices.interview.handlers.ElevatorRequestHandler;
import g2webservices.interview.handlers.ElevatorSimpleRequestHandler;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorSimpleRequestHandlerTest {
	
	private ElevatorRequestHandler handler;
	@Mock
	private ElevatorState elevatorState;
	@Mock
	private Elevator elevator;
	
	@Before
	public void setUp() throws Exception {
		handler = new ElevatorSimpleRequestHandler(elevator);
	}

	@After
	public void tearDown() throws Exception {
		elevator = null;
		handler = null;
	}

	@Test
	public void testMove0To10() {
		final ElevatorState state = new ElevatorState(null, 0, StatusEnum.IDLE);
		final ElevatorRequest request = new ElevatorRequest(10, 1);
		
		when(elevator.getState()).thenReturn(state);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(elevator.openDoor()).thenReturn(true);
		
		handler.process(request);
		
		verify(elevator, times(10)).up();
		verify(elevator, times(1)).openDoor();
		verify(elevator, times(1)).closeDoor();
		verify(elevator, never()).down();
		
		assertEquals(elevator.getState().getStatus(), StatusEnum.IDLE);
		assertEquals(elevator.getState().getDirection(), DirectionEnum.UP);
	}
	
	@Test
	public void testMove10To0() {
		final ElevatorState state = new ElevatorState(null, 10, StatusEnum.IDLE);
		final ElevatorRequest request = new ElevatorRequest(0, 1);
		
		when(elevator.getState()).thenReturn(state);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(elevator.openDoor()).thenReturn(true);

		handler.process(request);
		
		verify(elevator, times(10)).down();
		verify(elevator, times(1)).openDoor();
		verify(elevator, times(1)).closeDoor();
		verify(elevator, never()).up();
		
		assertEquals(elevator.getState().getStatus(), StatusEnum.IDLE);
		assertEquals(elevator.getState().getDirection(), DirectionEnum.DOWN);
	}
	
	@Test
	public void testMove50To40WithIntermediateStops() {
		final ElevatorRequest requestTo50 = new ElevatorRequest(40, 1);
		
		when(elevator.getState()).thenReturn(elevatorState);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		when(elevator.openDoor()).thenReturn(true);
		when(elevatorState.getCurrent()).thenReturn(50, 50, 49, 48, 47, 46, 45, 44);
		
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
	public void testMaxWheightRequest() {
		final ElevatorState state = new ElevatorState(null, 20, StatusEnum.IDLE);
		final ElevatorRequest request = new ElevatorRequest(16, 3);
		
		when(elevator.getState()).thenReturn(state);
		when(elevator.getMaxCapacity()).thenReturn(1f);
		
		handler.process(request);
		
		verify(elevator, times(1)).alarm();;
		verify(elevator, times(1)).stop();
		verify(elevator, never()).up();
		verify(elevator, never()).down();
		verify(elevator, never()).openDoor();
		verify(elevator, never()).closeDoor();
	}
	
	@Test
	public void testSameFloorRequest() {
		final ElevatorState state = new ElevatorState(null, 20, StatusEnum.IDLE);
		final ElevatorRequest request = new ElevatorRequest(20, 1);
		
		when(elevator.getState()).thenReturn(state);
		when(elevator.getMaxCapacity()).thenReturn(2f);
		
		handler.process(request);

		verify(elevator, times(1)).openDoor();
		verify(elevator, times(1)).closeDoor();
		verify(elevator, never()).up();
		verify(elevator, never()).down();
		verify(elevator, never()).notifyFloorChange();
		assertEquals(elevator.getState().getStatus(), StatusEnum.IDLE);
	}
	
}
