package g2webservices.interview.manager;

import java.util.Deque;

import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;

public interface ElevatorRequestManager extends Runnable{

	void send(ElevatorRequest request);
	Deque<ElevatorRequest> getRequests();
	Elevator getElevator();
}
