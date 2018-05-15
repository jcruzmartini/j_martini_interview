package g2webservices.interview.notifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.enums.StatusEnum;
import g2webservices.interview.handlers.ElevatorRequestHandler;
import g2webservices.interview.handlers.ElevatorSimpleRequestHandler;
import g2webservices.interview.models.Floor;
import g2webservices.interview.models.elevator.Cabin;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorImpl;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

@RunWith(MockitoJUnitRunner.class)
public class FloorChangeNotifierTest {

	private ElevatorRequestHandler handler;
	
	@Mock
	private Cabin cabin;
	@Mock
	private Floor floor;
	
	private Elevator elevator;
	
	@Before
	public void setUp() throws Exception {
		final ElevatorState state = new ElevatorState(null, 0, StatusEnum.IDLE);
		elevator = new ElevatorImpl("E", state, 2, null, cabin);
		handler = new ElevatorSimpleRequestHandler(elevator);
	}

	@After
	public void tearDown() throws Exception {
		handler = null;
		elevator = null;
		floor = null;
		cabin = null;
	}

	@Test
	public void testSendNotificationOfChangeWhenNewFloorReached() {
		final ElevatorRequest request = new ElevatorRequest(2, 1);
		elevator.addObserver(floor);
		
		handler.process(request);
		
		verify(floor, times(2)).floorChanged(Matchers.anyInt(), Matchers.eq(DirectionEnum.UP));
		verify(cabin, times(2)).floorChanged(Matchers.anyInt(), Matchers.eq(DirectionEnum.UP));
		assertEquals(elevator.getState().getStatus(), StatusEnum.IDLE);
		assertEquals(elevator.getState().getCurrent(), Integer.valueOf(2));
	}
	
	@Test
	public void testNotSendNotificationOfChangeWhenNoObservers() {
		final ElevatorRequest request = new ElevatorRequest(5, 1);
		elevator.removeObserver(cabin);
		handler.process(request);
		
		verify(floor, never()).floorChanged(Matchers.anyInt(), Matchers.eq(DirectionEnum.UP));
		verify(cabin, never()).floorChanged(Matchers.anyInt(), Matchers.eq(DirectionEnum.UP));
		assertEquals(elevator.getState().getStatus(), StatusEnum.IDLE);
		assertEquals(elevator.getState().getCurrent(), Integer.valueOf(5));
	}
	
}
