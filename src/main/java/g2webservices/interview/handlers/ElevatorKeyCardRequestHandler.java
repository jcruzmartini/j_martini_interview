package g2webservices.interview.handlers;

import g2webservices.interview.keycard.KeyCardAccessSystem;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;

/**
 * keycard request handler for processing elevator requests
 * @author jmartini
 *
 */
public class ElevatorKeyCardRequestHandler extends ElevatorRequestHandlerAbstract {
	
	private final KeyCardAccessSystem keyCardSystem;
	
	public ElevatorKeyCardRequestHandler(Elevator elevator, KeyCardAccessSystem keyCard) {
		super(elevator);
		this.keyCardSystem = keyCard;
	}

	@Override
	public void process(ElevatorRequest request) {
		super.process(request);
	}

	@Override
	public boolean openDoor() {
		final int current = getElevator().getState().getCurrent();
		if (getElevator().getRestrictedFloors().contains(current)){
			if (!keyCardSystem.validate()){
				System.out.println("Access Key Denied");
				return false;
			}
		}
		return super.openDoor();
	}

	
}
