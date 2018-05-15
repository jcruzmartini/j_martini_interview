package g2webservices.interview.models.elevator;

import java.util.Set;

import g2webservices.interview.notifier.FloorChangeNotifier;

public interface Elevator extends FloorChangeNotifier{

	String getName();
	ElevatorState getState();
	boolean openDoor();
	boolean closeDoor();
	void up();
	void down();
	void stop();
	void alarm();
	float getMaxCapacity();
	Set<Integer> getRestrictedFloors(); 
}
