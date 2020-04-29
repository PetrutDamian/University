package ObserverPattern;


public interface Observable {
    void addObserver(Observer obs);
    void removeObserver(Observer obs);
    void notifyObservers(EventTypes e);
}