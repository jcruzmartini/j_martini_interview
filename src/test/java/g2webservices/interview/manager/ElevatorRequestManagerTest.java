package g2webservices.interview.manager;

import static org.junit.Assert.assertTrue;
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
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorRequestManagerTest {

	private ElevatorRequestManager manager;

	@Mock
	private ElevatorRequestHandler handler;
	@Mock
	private ElevatorState elevatorState;
	@Mock
	private Elevator elevator;

	@Before
	public void setUp() throws Exception {
		manager = new ElevatorRequestManagerImpl(elevator, handler);
	}

	@After
	public void tearDown() throws Exception {
		manager = null;
		handler = null;
		elevator = null;
		elevatorState = null;
	}

	@Test
	public void testSendRequestWhenRunningRequestInAnotherDirection() {
		final ElevatorState state = new ElevatorState(null, 0, StatusEnum.IDLE);
		final ElevatorRequest up = new ElevatorRequest(10, 1);
		final ElevatorRequest down = new ElevatorRequest(-1, 1);

		when(elevator.getState()).thenReturn(state);
		
		manager.send(up);
		manager.send(down);
		
		assertTrue(manager.getRequests().size() == 2);
	}
	
	@Test
	public void testSendRequestWhenRunningRequestWithIntermediateStop() {
		final ElevatorState state = new ElevatorState(null, 0, StatusEnum.IDLE);
		final ElevatorState stateRunning = new ElevatorState(DirectionEnum.UP, 4, StatusEnum.RUNNING);
		final ElevatorRequest up = new ElevatorRequest(10, 1);
		final ElevatorRequest upTo12 = new ElevatorRequest(12, 1);
		final ElevatorRequest upTo20 = new ElevatorRequest(20, 1);

		when(elevator.getState()).thenReturn(state, stateRunning);
		
		manager.send(up);
		manager.send(upTo12);
		manager.send(upTo20);
		
		verify(handler).addStop(12);
		verify(handler).addStop(20);
		assertTrue(manager.getRequests().size() == 1);
	}
	
	
	@Test
	public void testConsumingPendingRequestInQueue() {
		final ElevatorState state = new ElevatorState(null, 0, StatusEnum.IDLE);
		final ElevatorState running = new ElevatorState(null, 5, StatusEnum.RUNNING);
		final ElevatorState stateMaintenance = new ElevatorState(null, 0, StatusEnum.STOPPED);
		final ElevatorRequest up = new ElevatorRequest(10, 1);
		final ElevatorRequest downToBasement = new ElevatorRequest(-1, 1);

		when(elevator.getState()).thenReturn(state, running, running, running, running, stateMaintenance);
		
		manager.send(up);
		manager.send(downToBasement);
		manager.run();
		
		
		verify(handler, times(1)).process(up);
		verify(handler, times(1)).process(downToBasement);
		assertTrue(manager.getRequests().size() == 0);
	}
	
	@Test
	public void testNotEnqueingRequestInQueueWhenUnderMaintenance() {
		final ElevatorState stateMaintenance = new ElevatorState(null, 0, StatusEnum.STOPPED);
		final ElevatorRequest up = new ElevatorRequest(10, 1);
		final ElevatorRequest downToBasement = new ElevatorRequest(-1, 1);

		when(elevator.getState()).thenReturn(stateMaintenance);
		
		manager.send(up);
		manager.send(downToBasement);
		manager.run();
		
		
		verify(handler, times(0)).process(up);
		verify(handler, times(0)).process(downToBasement);
		assertTrue(manager.getRequests().size() == 0);
	}

}
