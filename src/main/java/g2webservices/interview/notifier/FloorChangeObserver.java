package g2webservices.interview.notifier;

import g2webservices.interview.enums.DirectionEnum;

public interface FloorChangeObserver {

    void floorChanged(int floor, DirectionEnum direction);
    
}
