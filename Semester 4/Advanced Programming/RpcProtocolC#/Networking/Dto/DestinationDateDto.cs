using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace network
{
    [Serializable]
    public class DestinationDateDto
    {
        string destination;
        DateTime date;

    public String toString()
    {
        return "DestinationDateDto{" +
                "destination='" + destination + '\'' +
                ", date=" + date +
                '}';
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setDate(DateTime date)
    {
        this.date = date;
    }

    public String getDestination()
    {
        return destination;
    }

    public DateTime getDate()
    {
        return date;
    }

    public DestinationDateDto(String destination, DateTime date)
    {
        this.destination = destination;
        this.date = date;
    }
}
}
