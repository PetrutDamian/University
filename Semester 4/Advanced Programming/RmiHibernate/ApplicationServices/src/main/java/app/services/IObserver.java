package app.services;

import app.model.Cursa;
import app.model.Rezervare;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IObserver  extends Remote {
    void seatsDecremented(Cursa cursa) throws ServiceException, RemoteException;
    void newBookings(List<Rezervare> rezervari) throws ServiceException,RemoteException;
}
