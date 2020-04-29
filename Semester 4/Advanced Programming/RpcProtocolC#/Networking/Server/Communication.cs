using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace network
{
    [Serializable]
    public enum RequestType
    {
        LOGIN,GetAllCurse,FindCursa,GetAllBookings,MakeBooking,LOGOUT
    }
    [Serializable]
    public enum ResponseType
    {
        OK,
        Error,
        SeatsDecremented,
        NewBookings
    }
    [Serializable]
    public class Response
    {
        public ResponseType type;
        public Object data;

        private Response() { }
        public class Builder
        {
            public static Response create()
            {
                return new Response();
            }
        }
        public Response Type(ResponseType type)
        {
            this.type = type;
            return this;
        }
        public Response Data(Object data)
        {
            this.data = data;
            return this;
        }
    }
    [Serializable]
    public class Request
    {
        public RequestType type;
        public Object data;

        private Request() { }
        public class Builder
        {
            public static Request create()
            {
                return new Request();
            }
        }
        public Request Type(RequestType type)
        {
            this.type = type;
            return this;
        }
        public Request Data(Object data)
        {
            this.data = data;
            return this;
        }
    }

    
}