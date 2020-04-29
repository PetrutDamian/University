package app.network;

import app.model.Cursa;
import app.model.Rezervare;
import app.model.User;
import app.network.dto.DestinationDateDto;
import app.services.IObserver;
import app.services.IServices;
import app.services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class ClientWorker implements Runnable, IObserver {
    private IServices service;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(IServices service, Socket client){
        this.service = service;
        this.socket=client;
        try{
            output =new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input =  new ObjectInputStream(socket.getInputStream());
            connected = true;
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(connected){
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//    pentru?
        }
        try{
            input.close();
            output.close();
            socket.close();
        }catch (IOException ex){
            System.out.println("Eroare in clientWorker "+ex);
        }
    }

    private synchronized void sendResponse(Response response) throws IOException {
        output.writeObject(response);
        output.flush();
    }
    private Response createErrorResponse(Exception ex){
        return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();

    }
    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request) {
        Response response = null;
        switch (request.type()){
            case LOGIN:
                System.out.println("Login request received...");
                User user = (User)request.data();
                try{
                    service.login(user,this);
                    return okResponse;
                }catch (ServiceException ex){
                    connected = false;
                    return createErrorResponse(ex);
                }

            case LOGOUT:
                System.out.println("Logout request received...");
                try {
                    service.logout(this);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                connected=false;
                break;

            case GETALLCURSE:
                System.out.println("GETALLCURSE request received ...");
                try{
                    List<Cursa> curse = service.getAllCurse();
                    return new Response.Builder().type(ResponseType.OK).data(curse).build();
                }catch (ServiceException ex){
                    return createErrorResponse(ex);
                }

            case FindByDestinationAndDate:
                System.out.println("FindByDestinationAndDate request received ....");
                try{
                    DestinationDateDto dto = (DestinationDateDto) request.data();
                    String destination  = dto.getDestination();
                    LocalDateTime date = dto.getDate();
                    Cursa cursa = service.findByDestinationAndDate(destination,date);
                    return new Response.Builder().type(ResponseType.OK).data(cursa).build();
                } catch (ServiceException e) {
                    return createErrorResponse(e);
                }
            case GetAllBookings:
                System.out.println("GetAllBookings request received ...");
                Integer cursaId = (Integer)request.data();
                try{
                    List<Rezervare> rezervari = service.getAllBookings(cursaId);
                    return new Response.Builder().type(ResponseType.OK).data(rezervari).build();
                } catch (ServiceException e) {
                    return createErrorResponse(e);
                }
            case MakeBooking:
                System.out.println("MakeBooking request received ...");
                List<Rezervare> rezervari = (List<Rezervare>)request.data();
                try{
                    service.makeBooking(rezervari);
                    return okResponse;
                }catch (ServiceException ex){
                    return createErrorResponse(ex);
                }
        }
        return response;
    }

    @Override
    public synchronized void seatsDecremented(Cursa cursa) throws ServiceException {
        Response response = new Response.Builder().type(ResponseType.SeatsDecremented).data(cursa).build();
        try{
            sendResponse(response);
        } catch (IOException e) {
            throw new ServiceException("SeatsDecremented error "+e);
        }

    }

    @Override
    public synchronized void newBookings(List<Rezervare> rezervari) throws ServiceException {
        Response response = new Response.Builder().type(ResponseType.NewBookings).data(rezervari).build();
        try{
            sendResponse(response);
        } catch (IOException e) {
            throw new ServiceException("NewBookings error "+e);
        }

    }
}
