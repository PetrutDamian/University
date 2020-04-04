package app.services;

public interface IObservable {
    void addObserver(IObserver obs);
    void removeObserver(IObserver obs);
}
