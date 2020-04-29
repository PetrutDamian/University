package Observer;

import utils.EventTypes;

public interface Observable {
    void addObserver(Observer obs);
    void removeObserver(Observer obs);
    void notifyObservers(EventTypes e);
}
