import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Observer extends Remote {
    void serverShutdown() throws RemoteException;
    void responseReceived(boolean accepted, String spectacol, List<Integer> locuri) throws RemoteException;
    void ticketsSold(String spectacol, List<Integer> tickets) throws  RemoteException;
}
