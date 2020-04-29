package app.network.server;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {

    public AbstractConcurrentServer(int port){
        super(port);
        System.out.println("Concurrent server created");
    }
    @Override
    protected void processRequest(Socket client) {
        Thread t = createWorker(client);
        t.start();
    }
    protected abstract Thread createWorker(Socket client);
}
