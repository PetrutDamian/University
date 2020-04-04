package app.network.server;

import app.network.ClientWorker;
import app.services.IServices;

import java.net.Socket;

public class RemoteConcurrentServer extends AbstractConcurrentServer {
    private IServices server;
    public RemoteConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        Thread t = new Thread(worker);
        return t;
    }
}
