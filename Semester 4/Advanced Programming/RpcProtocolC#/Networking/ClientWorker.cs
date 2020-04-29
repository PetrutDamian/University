using Domain;
using services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
namespace network
{
    public class ClientWorker : IObserver
    {
        private IServices service;
        private TcpClient client;
        private volatile Boolean connected;
        private NetworkStream stream;
        private IFormatter formatter;
        public ClientWorker(IServices service, TcpClient client)
        {
            this.service = service;
            this.client = client;
            try
            {
                stream = client.GetStream();
                formatter = new BinaryFormatter();
                connected = true;
            }catch(Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }

        }
        public virtual void newBookings(List<Rezervare> rezervari)
        {
            Response response = Response.Builder.create().Type(ResponseType.NewBookings).Data(rezervari);
            sendResponse(response);
        }

        public virtual  void seatsDecremented(Cursa cursa)
        {
            Response response = Response.Builder.create().Type(ResponseType.SeatsDecremented).Data(cursa);
            sendResponse(response);
        }
        public virtual void run()
        {
            while (connected)
            {
                try
                {
                    object request = formatter.Deserialize(stream);
                    Console.WriteLine("ClientWorker: request received "+((Request)request).type);

                    Response response = handleRequest((Request)request);
                    if (response != null)
                    {
                        sendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            try
            {
                stream.Close();
                client.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error " + e);
            }
        }

        private void sendResponse(Response response)
        {
            Console.WriteLine("CLientWorker: sending Response : " + response.type);
            lock (stream)
            {
                formatter.Serialize(stream, response);
                stream.Flush();
            }
            Console.WriteLine("Response was sent.");
        }

        private Response handleRequest(Request request)
        {
            Response response = null;
            switch (request.type)
            {
                case RequestType.LOGIN:
                    Console.WriteLine("login request received");
                    User user = (User)request.data;
                    try
                    {
                        lock (service)
                        {
                            service.login(user, this);
                        }
                        Console.WriteLine("User logged in, about to create response");
                        return Response.Builder.create().Type(ResponseType.OK);
                    }
                    catch (ServiceException e)
                    {
                        connected = false;
                        return Response.Builder.create().Type(ResponseType.Error).Data(e.Message);
                    }

                case RequestType.GetAllCurse:
                    Console.WriteLine("GetAllCurse request received");
                    try
                    {
                        IList<Cursa> curse;
                        lock (service)
                        {
                            curse = service.getAllCurse();
                        }
                        return Response.Builder.create().Type(ResponseType.OK).Data(curse);
                    }catch(ServiceException e)
                    {
                        return Response.Builder.create().Type(ResponseType.Error).Data(e.Message);
                    }
                case RequestType.FindCursa:
                    Console.WriteLine("FindCursa request received");
                    try
                    {
                        Cursa cursa;
                        lock (service)
                        {
                            DestinationDateDto dto = (DestinationDateDto)request.data;
                            string destination = dto.getDestination();
                            DateTime date = dto.getDate();
                            cursa = service.findByDestinationAndDate(destination, date);
                            Console.WriteLine("Retreived cursa from service");
                        }
                        return Response.Builder.create().Type(ResponseType.OK).Data(cursa);
                    }catch(ServiceException e)
                    {
                        return Response.Builder.create().Type(ResponseType.Error).Data(e.Message);
                    }
                case RequestType.LOGOUT:
                    Console.WriteLine("Logout request received");
                        lock (service)
                        {
                            service.logout(this);
                        }
                    connected = false;
                    return null;
                case RequestType.GetAllBookings:
                    Console.WriteLine("GetAllBookings request received");
                    try
                    {
                        List<Rezervare> bookings = null;
                        lock (service)
                        {
                            bookings = service.getAllBookings((int)request.data);
                        }
                        return Response.Builder.create().Type(ResponseType.OK).Data(bookings);
                    }catch  (ServiceException ex)
                    {
                        return Response.Builder.create().Type(ResponseType.Error).Data(ex.Message);
                    }
                case RequestType.MakeBooking:
                    Console.WriteLine("MakeBooking request received");
                    try
                    {
                        lock (service)
                        {
                            service.makeBooking((List<Rezervare>)request.data);
                        }
                        return Response.Builder.create().Type(ResponseType.OK);
                    }
                    catch(ServiceException ex)
                    {
                        return Response.Builder.create().Type(ResponseType.Error).Data(ex.Message);
                    }
            }
            return response;
        }
    }
}
