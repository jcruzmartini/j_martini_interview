package g2webservices.interview.models.elevator;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.notifier.FloorChangeObserver;

public class Cabin implements FloorChangeObserver{

	private final String name;

	public Cabin(String name) {
		this.name = name;
	}

	public void floorChanged(int floor, DirectionEnum direction) {
		System.out.println("Notification received in " + name + " .Floor changed to " + floor + " Moving in direction " + direction );
	}
}
