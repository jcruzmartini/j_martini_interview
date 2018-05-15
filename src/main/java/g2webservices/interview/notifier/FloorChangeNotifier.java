package g2webservices.interview.notifier;

import java.util.Set;

public interface FloorChangeNotifier {
	
	void addObserver(FloorChangeObserver observer);
	void removeObserver(FloorChangeObserver observer);
    void notifyFloorChange();
    Set<FloorChangeObserver> getObservers();
    
}
