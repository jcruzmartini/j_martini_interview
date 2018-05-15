package g2webservices.interview.models.elevator;

public class ElevatorRequest {

	private int target;
	private float weight;
	
	public ElevatorRequest(int target, float weight) {
		this.target = target;
		this.weight = weight;
	}

	public int getTarget() {
		return target;
	}

	public float getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "ElevatorRequest [target=" + target + ", weight=" + weight + "]";
	}
	
}
