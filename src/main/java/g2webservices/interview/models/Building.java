package g2webservices.interview.models;

import java.util.ArrayList;
import java.util.List;

import g2webservices.interview.models.elevator.Elevator;

public class Building {
	
	private int maxFloor;
	private int minFloor;
	private List<Elevator> elevators = new ArrayList<Elevator>();
	
	
	public Building(int maxFloor, int minFloor) {
		this.maxFloor = maxFloor;
		this.minFloor = minFloor;
	}


	public int getMaxFloor() {
		return maxFloor;
	}


	public int getMinFloor() {
		return minFloor;
	}


	public void addElevator(Elevator elevator){
		elevators.add(elevator);
	}
}
