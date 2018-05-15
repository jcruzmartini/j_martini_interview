package g2webservices.interview.models.elevator;

import g2webservices.interview.enums.DirectionEnum;
import g2webservices.interview.enums.StatusEnum;

public class ElevatorState {

	private DirectionEnum direction;
	private Integer current;
	private StatusEnum status;
	
	public ElevatorState(DirectionEnum direction, Integer current, StatusEnum status) {
		this.direction = direction;
		this.current = current;
		this.status = status;
	}
	
	public DirectionEnum getDirection() {
		return direction;
	}
	public void setDirection(DirectionEnum direction) {
		this.direction = direction;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	public boolean isRunning(){
		return status == StatusEnum.RUNNING;
	}
	
	public boolean isUnderMaintenance(){
		return status == StatusEnum.STOPPED;
	}
	
}
