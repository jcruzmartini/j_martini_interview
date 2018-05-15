package g2webservices.interview.manager;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.handlers.ElevatorRequestHandler;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

/**
 * Request manager implementation for handling elevator request using a direction strategy
 * Direction Strategy: The decision of where elevator should go, is decided based in the direction and 
 * current position of the elevator 
 * @author jmartini
 *
 */
public class ElevatorRequestManagerImpl implements ElevatorRequestManager {

	private final ElevatorRequestHandler handler;
	private final Deque<ElevatorRequest> requests;
	private final Elevator elevator;

	
	public ElevatorRequestManagerImpl(Elevator elevator, ElevatorRequestHandler handler) {
		this.handler = handler;
		this.requests = new ConcurrentLinkedDeque<ElevatorRequest>();
		this.elevator = elevator;
	}

	@Override
	public void send(ElevatorRequest request) {
		System.out.println("Adding new request " + request);
		final ElevatorState state = getElevator().getState();
		if (state.isRunning() && (request.getTarget() > state.getCurrent() && state.getDirection() == DirectionEnum.UP)
				|| (request.getTarget() < state.getCurrent() && state.getDirection() == DirectionEnum.DOWN)) {
			handler.addStop(request.getTarget());
			return;
		} 
		if (!state.isUnderMaintenance()){
			requests.add(request);
		}
	}

	@Override
	public void run() {
		while (true && !getElevator().getState().isUnderMaintenance()) {
			if (!getRequests().isEmpty()) {
				handler.process(requests.poll());
			}
		}
	}

	@Override
	public Deque<ElevatorRequest> getRequests() {
		return requests;
	}

	@Override
	public Elevator getElevator() {
		return elevator;
	}

}
