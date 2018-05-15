package g2webservices.interview.handlers;

import java.util.Set;

import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorRequest;

public interface ElevatorRequestHandler {

	void process(ElevatorRequest request);
	boolean isValid(ElevatorRequest request);
	boolean openDoor();
	void closeDoor();
	void addStop(Integer floor);
	void removeStop(Integer floor);
	Elevator getElevator();
	Set<Integer> getIntermediateStops();
}
