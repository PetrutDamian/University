using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client
{
    public enum EventType
    {
        SeatsDecremented,
        NewBookings
    }
    public class newUpdateEventArgs:EventArgs
    {
        private readonly EventType eventType;
        private readonly Object data;

        public newUpdateEventArgs(EventType eventType, object data)
        {
            this.eventType = eventType;
            this.data = data;
        }

        public EventType EventType
        {
            get { return eventType; }
        }

        public object Data
        {
            get { return data; }
        }
    }
}

