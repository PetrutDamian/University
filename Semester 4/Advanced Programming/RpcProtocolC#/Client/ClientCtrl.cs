using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
using services;
namespace Client
{
    public class ClientCtrl:IObserver
    {
        public event EventHandler<newUpdateEventArgs> updateEvent;
        private readonly IServices server;
        private User user;
        public ClientCtrl(IServices server)
        {
            this.server = server;
            user = null;
        }
        public void login(string username,string password)
        {
            User user = new User(username, password);
            server.login(user, this);
            Console.WriteLine("Successfully logged in.");
            this.user = user;

        }
        public void newBookings(List<Rezervare> rezervari)
        {
            newUpdateEventArgs args = new newUpdateEventArgs(EventType.NewBookings, rezervari);
            callEvent(args);
        }

        public void seatsDecremented(Cursa cursa)
        {
            newUpdateEventArgs args = new newUpdateEventArgs(EventType.SeatsDecremented, cursa);
            callEvent(args);
           
        }

        protected virtual void callEvent(newUpdateEventArgs args)
        {
            if (updateEvent == null) return;
            updateEvent(this, args);
            
        }

        public List<Cursa> getAllCurse()
        {
            return server.getAllCurse();
        }

        public Cursa findByDestinationAndDate(string destination, DateTime date)
        {

            return server.findByDestinationAndDate(destination, date);
        }

        public List<Rezervare> getAllBookings(int id)
        {
            Console.WriteLine("about to go to server proxy getAllBookings");
            return server.getAllBookings(id);
        }

        public void makeBooking(List<Rezervare> rezervari)
        {
            server.makeBooking(rezervari);
        }
        public void logout()
        {
            server.logout(this);
            user = null;
            updateEvent = null;
        }
    }
}
