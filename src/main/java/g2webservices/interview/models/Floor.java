package g2webservices.interview.models;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.notifier.FloorChangeObserver;

public class Floor implements FloorChangeObserver{
	
	private String name;
	private int number;

	public Floor(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public void floorChanged(int floor, DirectionEnum direction) {
		System.out.println("Notification received in Floor #" + number + " .Floor changed to " + floor + " Moving in direction " + direction );
	}
}
