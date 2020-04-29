using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using services;
using Domain;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;

namespace network
{
    [Serializable]
    public class ServerProxy : IServices
    {
        private string host;
        private int port;
        private Queue<Response> responses;
        private volatile bool finished;
        private IObserver client;
        private NetworkStream stream;
        private TcpClient connection;
        private IFormatter formatter;
        private EventWaitHandle _waitHandle;

        public ServerProxy(string host,int port)
        {
            this.host = host;
            this.port = port;
            responses = new Queue<Response>();
        }
        private void checkErrorResponse(Response response)
        {
            if (response.type == ResponseType.Error)
                throw new ServiceException((string)response.data);
        }
        public virtual  void login(User user, IObserver client)
        {
            initializeConnection();
            Console.WriteLine("Sending login request");
            sendRequest(Request.Builder.create().Type(RequestType.LOGIN).Data(user));
            Response response = readResponse();
            if (response.type.Equals(ResponseType.OK))
            {
                this.client = client;
                return;
            }
            if (response.type.Equals(ResponseType.Error))
            {
                Console.WriteLine("Response received : error, couldn't login");
                closeConnection();
                throw new ServiceException((string)response.data);
            }
        }

        private void closeConnection()
        {
            finished = true;
            try
            {
                stream.Close();
                connection.Close();
                _waitHandle.Close();
                client = null;

            }catch(Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private Response readResponse()
        {
            Response response = null;
            try
            {
                _waitHandle.WaitOne();
                lock (responses)
                {
                    response = responses.Dequeue();
                }
            }catch(Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return response;
        }

        private void sendRequest(Request request)
        {
            try
            {
                formatter.Serialize(stream, request);
                stream.Flush();
                Console.WriteLine("Request sent!"+request.type);
            }catch(Exception e)
            {
                throw new ServiceException("Error sending object " + e);
            }
            
        }

        private void initializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                finished = false;
                _waitHandle = new AutoResetEvent(false);
                startReader();

            }catch(Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            
        }

        private void startReader()
        {
            Thread t = new Thread(run);
            t.Start();
        }
        public virtual void run()
        {
            while (!finished)
            {
                try
                {
                    object response = formatter.Deserialize(stream);
                    Console.WriteLine("response received" + ((Response)response).type);
                    if (isUpdate((Response)response))
                        handleUpdate((Response)response);
                    else
                    {
                        lock (responses)
                        {
                            responses.Enqueue((Response)response);
                        }
                        _waitHandle.Set();
                    }
                    
                }catch(Exception e)
                {
                    Console.WriteLine("Reading error " + e);
                }
            }
        }

        private void handleUpdate(Response response)
        {
            switch (response.type)
            {
                case ResponseType.SeatsDecremented:
                    Console.WriteLine("Seats decremented update received");
                    try
                    {
                        client.seatsDecremented((Cursa)response.data);
                    }catch(Exception e)
                    {
                        Console.WriteLine(e.StackTrace);
                    }
                    break;
                case ResponseType.NewBookings:
                    try
                    {
                        client.newBookings((List<Rezervare>)response.data);
                    }catch(Exception e)
                    {
                        Console.WriteLine(e.StackTrace);
                    }
                    break;
            }
        }

        private bool isUpdate(Response response)
        {
            return response.type == ResponseType.SeatsDecremented || response.type == ResponseType.NewBookings;
        }

        public virtual Cursa findByDestinationAndDate(string destination, DateTime date)
        {
            Request request =
                Request.Builder.create().Type(RequestType.FindCursa).Data(DtoUtils.createDestinationDateDto(destination, date));
            sendRequest(request);
            Response response = readResponse();
            checkErrorResponse(response);
            return (Cursa)response.data;
        }

        public virtual List<Rezervare> getAllBookings(int id)
        {
            Console.WriteLine("creating request");
            Request request = Request.Builder.create().Type(RequestType.GetAllBookings).Data(id);
            Console.WriteLine("about to send getAllBookings request");
            sendRequest(request);
            Response response = readResponse();
            Console.WriteLine("i received a response");
            checkErrorResponse(response);
            return (List<Rezervare>)response.data;
        }

        public virtual List<Cursa> getAllCurse()
        {
            Request request = Request.Builder.create().Type(RequestType.GetAllCurse);
            sendRequest(request);
            Response  response = readResponse();
            checkErrorResponse(response);
            return (List<Cursa>)response.data;
        }


        public virtual void logout(IObserver obs)
        {
            Request request = Request.Builder.create().Type(RequestType.LOGOUT);
            sendRequest(request);
            closeConnection();
        }

        public virtual void makeBooking(List<Rezervare> rezervari)
        {
            Request request = Request.Builder.create().Type(RequestType.MakeBooking).Data(rezervari);
            sendRequest(request);
            Response response = readResponse();
            Console.WriteLine("ServerProxy: MakeBooking response received:" + response.type);
            checkErrorResponse(response);
        }
    }
}
