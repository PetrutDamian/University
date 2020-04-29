using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace services
{
    public interface IServices
    {
        void login(User user, IObserver client);
        void logout(IObserver obs);
        List<Cursa> getAllCurse();
        Cursa findByDestinationAndDate(string destination, DateTime date);
        List<Rezervare> getAllBookings(int id);
        void makeBooking(List<Rezervare> rezervari);
    }
}
