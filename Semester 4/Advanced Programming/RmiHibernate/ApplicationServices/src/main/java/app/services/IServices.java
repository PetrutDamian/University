package app.services;

import app.model.Cursa;
import app.model.Rezervare;
import app.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface IServices{

    void login(User user, IObserver client) throws ServiceException;
    void logout(IObserver obs) throws ServiceException;
    List<Cursa> getAllCurse() throws ServiceException;

    Cursa findByDestinationAndDate(String destination, LocalDateTime date) throws ServiceException;

    List<Rezervare> getAllBookings(Integer id) throws ServiceException;

    void makeBooking(List<Rezervare> rezervari) throws ServiceException;
}
