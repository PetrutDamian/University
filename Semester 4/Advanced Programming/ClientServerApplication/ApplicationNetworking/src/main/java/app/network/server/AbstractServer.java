package app.network.server;

import app.services.ServiceException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket socket=null;
    public AbstractServer(int port){
        this.port = port;
    }
    public void start() throws ServiceException {
        try{
            socket = new ServerSocket(port);
            while(true) {
                System.out.println("Waiting for clients ...\n");
                Socket client = socket.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new ServiceException("Starting server error" + e);
        }finally {
            stop();
        }

    }
    protected abstract void processRequest(Socket client);
    public  void stop() throws ServiceException {
        System.out.println("Stopping server ... ");
        try{
            socket.close();
        } catch (IOException e) {
            throw new ServiceException("Closing server error "+e);
        }

    }
}
