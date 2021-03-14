import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends UnicastRemoteObject implements Observer, Serializable {
    private Random generator = new Random();
    private IServices server;
    private List<SpectacolDTO> spectacole;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Lock lock = new ReentrantLock();
    protected Client() throws RemoteException {
        Runnable task = this::sendBuyRequest;
        executor.scheduleAtFixedRate(task, 1,5, TimeUnit.SECONDS);
    }
    public void setServer(IServices server){
        this.server = server;
        this.spectacole = server.connect(this);
    }


    @Override
    public void serverShutdown() throws RemoteException {
        executor.shutdown();
        System.out.println("Server shutdown!");
        server = null;
    }

    @Override
    public void responseReceived(boolean accepted, String spectacol, List<Integer> locuri) throws RemoteException {
        System.out.println("Received notification from server.");
        System.out.println("Spectacol : "+spectacol+ " Locuri: "+locuri);
        System.out.println("Accepted: "+accepted);
    }

    @Override
    public void ticketsSold(String spectacol, List<Integer> tickets) throws RemoteException {
        lock.lock();
        for(int i=0;i<spectacole.size();i++)
            if(spectacole.get(i).name.equals(spectacol)) {
                spectacole.get(i).locuriDisponibile.removeIf(loc -> tickets.stream().anyMatch(t -> t.equals(loc)));
                if(spectacole.get(i).locuriDisponibile.size()==0)
                    spectacole.remove(i);
                break;
            }
        lock.unlock();
    }

    public void sendBuyRequest(){
        lock.lock();
        int randomSpectacolIndex = generator.nextInt(spectacole.size());
        String spectacolID = spectacole.get(randomSpectacolIndex).name;
        List<Integer> locuriDisponibile = spectacole.get(randomSpectacolIndex).locuriDisponibile;

        int nrBilete = generator.nextInt(Math.min(locuriDisponibile.size(),3))+1;
        Collections.shuffle(locuriDisponibile);
        List<Integer> locuriCerute = new ArrayList<>();
        for(int i=0;i<nrBilete;i++)
            locuriCerute.add(locuriDisponibile.get(i));
        lock.unlock();
            if(spectacole.size()>0)
        server.buyTickets(this,spectacolID,locuriCerute);
    }
}
