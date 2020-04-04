package app.server;

import app.model.Cursa;
import app.model.Rezervare;
import app.model.User;

import app.repository.ICursaRepo;
import app.repository.IRezervareRepo;
import app.repository.IUserRepo;
import app.services.IObservable;
import app.services.IObserver;
import app.services.IServices;
import app.services.ServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IServices, IObservable {
    private IUserRepo userRepo;
    private ICursaRepo cursaRepo;
    private IRezervareRepo rezervareRepo;
    private List<IObserver> observers = new ArrayList<>();

    public Service(IUserRepo userRepo, ICursaRepo cursaRepo, IRezervareRepo rezervareRepo) {
        this.userRepo = userRepo;
        this.cursaRepo = cursaRepo;
        this.rezervareRepo = rezervareRepo;
    }

    public synchronized Cursa findByDestinationAndDate(String destination, LocalDateTime date){
        List<Cursa> curse = cursaRepo.findByDestinationAndDate(destination, date);
        if (curse.size()==0)
            return null;
        return curse.get(0);
    }

    @Override
    public synchronized List<Rezervare> getAllBookings(Integer id) {
        return rezervareRepo.findByIdCursa(id);
    }

    @Override
    public synchronized void makeBooking(List<Rezervare> rezervari) throws ServiceException{
        Cursa cursa = cursaRepo.findOne(rezervari.get(0).getIdCursa());
        cursa.decrementSeats(rezervari.size());
        cursaRepo.update(cursa);
        for(Rezervare r:rezervari)
            rezervareRepo.save(r);
        notify(cursa,rezervari);
    }
    private synchronized void notify(Cursa cursa,List<Rezervare> rezervari){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(IObserver obs:observers){
            executor.execute(()->{
                try {
                    obs.seatsDecremented(cursa);
                    obs.newBookings(rezervari);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

    public synchronized List<Cursa> getAllCurse(){
        return cursaRepo.findAll();
    }

    @Override
    public synchronized void login(User user,IObserver obs) throws ServiceException {
        User userFromRepo = userRepo.findOne(user.getId());
        if(userFromRepo==null)
            throw new ServiceException("User not found.");
        if (!user.getPassword().equals(userFromRepo.getPassword()))
            throw new ServiceException("Incorrect password!");
        addObserver(obs);
    }
    @Override
    public void logout(IObserver obs) {
        observers.remove(obs);
    }

    @Override
    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(IObserver obs) {
        observers.remove(obs);
    }
}
