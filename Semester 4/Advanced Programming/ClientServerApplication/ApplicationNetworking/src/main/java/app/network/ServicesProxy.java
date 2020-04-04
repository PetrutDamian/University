package app.network;

import app.model.Cursa;
import app.model.Rezervare;
import app.model.User;
import app.network.dto.DestinationDateDto;
import app.network.dto.DtoUtils;
import app.services.IObserver;
import app.services.IServices;
import app.services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProxy implements IServices {
    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private volatile boolean finished;
    private BlockingQueue<Response> responseQueue;
    private User crtUser;
    private IObserver client;
    public ServicesProxy (String host, int port) {
        this.host = host;
        this.port = port;
        responseQueue =new LinkedBlockingQueue<Response>();
    }

    public void login(User user, IObserver client) throws ServiceException{
        createConnectionSocket();
        Request request = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR) {
            String error = response.data().toString();
            closeConnection();
            throw new ServiceException(error);
        }
        if(response.type()==ResponseType.OK){
            crtUser = user;
            this.client=client;
            return;
        }
    }
    private void checkErrorResponse(Response response) throws ServiceException {
        if (response.type()==ResponseType.ERROR)
            throw new ServiceException(response.data().toString());
    }

    @Override
    public void logout(IObserver obs) throws  ServiceException{
        Request request = new Request.Builder().type(RequestType.LOGOUT).build();
        sendRequest(request);
        closeConnection();
    }

    @Override
    public List<Cursa> getAllCurse() throws ServiceException {
        Request request = new Request.Builder().type(RequestType.GETALLCURSE).build();
        sendRequest(request);
        Response response = readResponse();
        checkErrorResponse(response);
        return (List<Cursa>)response.data();
    }

    @Override
    public Cursa findByDestinationAndDate(String destination, LocalDateTime date) throws ServiceException {
        DestinationDateDto dto = DtoUtils.createDestinationDateDto(destination,date);
        Request request = new Request.Builder().type(
                RequestType.FindByDestinationAndDate).data(dto).build();
        sendRequest(request);
        Response response = readResponse();
        checkErrorResponse(response);
        return (Cursa)response.data();

    }

    @Override
    public List<Rezervare> getAllBookings(Integer id) throws ServiceException {
        Request request = new Request.Builder().type(RequestType.GetAllBookings).data(id).build();
        sendRequest(request);
        Response response = readResponse();
        checkErrorResponse(response);
        return (List<Rezervare>)response.data();

    }

    @Override
    public void makeBooking(List<Rezervare> rezervari) throws ServiceException {
        Request request = new Request.Builder().type(RequestType.MakeBooking).data(rezervari).build();
        sendRequest(request);
        Response response = readResponse();
        checkErrorResponse(response);
    }

    private void closeConnection() {
        finished=true;
        try{
            input.close();
            output.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try{
            response = responseQueue.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) throws ServiceException {
        try{
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private void createConnectionSocket() {
        try{
            socket =new Socket(host,port);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            finished = false;
            startWatcher();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startWatcher() {
        Thread thread = new Thread(new WatcherThread());
        thread.start();
    }
    private boolean isUpdate(Response response){
        if(response.type()==ResponseType.NewBookings)
            return true;
        if(response.type()==ResponseType.SeatsDecremented)
            return true;
        return false;
    }
    private void handleUpdate(Response response){
        switch (response.type()){
            case SeatsDecremented:
                try {
                    client.seatsDecremented((Cursa)(response.data()));
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                break;
            case NewBookings:
                try{
                    client.newBookings((List<Rezervare>)(response.data()));
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private class WatcherThread implements Runnable{
        @Override
        public void run() {
            while(!finished){
                try{
                    Object response = input.readObject();
                    if(isUpdate((Response)response))
                        handleUpdate((Response)response);
                    else{
                        try{
                            responseQueue.put((Response)response);
                        }catch (InterruptedException ex){
                            ex.printStackTrace();
                        }
                    }
                }catch (IOException | ClassNotFoundException ex){
                    System.out.println("Reading error :"+ex);
                }
            }
        }
    }

}
