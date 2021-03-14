import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements IServices {
    private boolean closed = false;
    private List<Observer> observers = new ArrayList<>();
    private Repository repository = new Repository();

    private ExecutorService buyTicketsExecutor = Executors.newFixedThreadPool(8);
    private ScheduledExecutorService validateExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService shutdownExecutor = Executors.newSingleThreadScheduledExecutor();
    public Server() {
        Runnable shutdown = ()->{
            System.out.println("Shutting down");
            closed = true;
            buyTicketsExecutor.shutdown();
            validateExecutor.shutdown();
            notifyClosed();
        };
        shutdownExecutor.schedule(shutdown,120,TimeUnit.SECONDS);
        shutdownExecutor.shutdown();
        validateExecutor.scheduleAtFixedRate(repository::checkIntegrity, 5,5, TimeUnit.SECONDS);
    }
    private void notifyClosed(){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(Observer obs:observers){
            executor.submit(()->{
                try {
                    obs.serverShutdown();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized List<SpectacolDTO> connect(Observer client) {
        if(!closed){
            observers.add(client);
            System.out.println("Client connected");
        }
        return repository.getDTO();
    }

    @Override
    public synchronized void buyTickets(Observer client,String spectacol, List<Integer> locuri) {
        if(!closed){
            Runnable task = ()->{
                boolean succeded = repository
                        .sellTickets(spectacol,new Vanzare(LocalDateTime.now(),locuri.size(),locuri));
                try {
                    client.responseReceived(succeded,spectacol,locuri);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                observers.forEach(o-> {
                    try {
                        o.ticketsSold(spectacol,locuri);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            };
            buyTicketsExecutor.submit(task);
        }
    }
}
