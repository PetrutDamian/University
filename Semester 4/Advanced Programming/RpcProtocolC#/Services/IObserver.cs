using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace services
{
    
    public interface IObserver
    {
        void seatsDecremented(Cursa cursa);
        void newBookings(List<Rezervare> rezervari);
    }
}
