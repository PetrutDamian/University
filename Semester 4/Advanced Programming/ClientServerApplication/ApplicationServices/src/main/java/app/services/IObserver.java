package app.services;

import app.model.Cursa;
import app.model.Rezervare;

import java.util.List;

public interface IObserver {
    void seatsDecremented(Cursa cursa) throws ServiceException;
    void newBookings(List<Rezervare> rezervari) throws ServiceException;
}
