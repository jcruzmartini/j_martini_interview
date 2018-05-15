package g2webservices.interview.handlers;

import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;

/**
 * simple request handler for processing elevator requests
 * @author jmartini
 *
 */
public class ElevatorSimpleRequestHandler extends ElevatorRequestHandlerAbstract {
	
	public ElevatorSimpleRequestHandler(Elevator elevator) {
		super(elevator);
	}

	@Override
	public void process(ElevatorRequest request) {
		super.process(request);
	}

}
