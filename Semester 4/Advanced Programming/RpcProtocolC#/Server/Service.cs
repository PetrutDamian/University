using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
using Repositories;
using services;
namespace server
{
    public class Service:IServices
    {
        private IUserRepo userRepo;
        private ICursaRepo cursaRepo;
        private IRezervareRepo rezervareRepo;
        private IList<IObserver> observers = new List<IObserver>();

        public Service(IUserRepo userRepo, ICursaRepo cursaRepo, IRezervareRepo rezervareRepo)
        {
            this.userRepo = userRepo;
            this.cursaRepo = cursaRepo;
            this.rezervareRepo = rezervareRepo;
        }
        public void makeBooking(List<Domain.Rezervare> rezervari)
        {
            Domain.Cursa cursa = cursaRepo.FindOne(rezervari[0].idCursa);
            cursa.locuriDisponibile -= rezervari.Count;
            cursaRepo.Update(cursa);
            foreach(var rezervare in rezervari)
                rezervareRepo.Save(rezervare);
            notify(cursa, rezervari);
        }

        private void notify(Domain.Cursa cursa, List<Domain.Rezervare> rezervari)
        {
            foreach(var obs in observers)
            {
                Task.Run(() =>
                {
                    obs.seatsDecremented(cursa);
                    obs.newBookings(rezervari);
                });
            }
        }

        public Domain.Cursa findByDestinationAndDate(String destination, DateTime date)
        {
            Console.WriteLine("Enter service findByDestinationAndDate");
            var curse = cursaRepo.FindByDestinationAndDate(destination, date);
            if (curse.ToList<Domain.Cursa>().Count == 0)
                throw new ServiceException("No transport found!");
            return cursaRepo.FindByDestinationAndDate(destination, date).ElementAt(0);
        }
        public List<Domain.Rezervare> getAllBookings(int id)
        {
            return rezervareRepo.FindByIdCursa(id).ToList<Domain.Rezervare>();
        }
        public List<Domain.Cursa> getAllCurse()
        {
            return cursaRepo.FindAll().ToList<Domain.Cursa>();
        }

        public Domain.User getUserById(String id)
        {
            return userRepo.FindOne(id);
        }

        public void login(Domain.User user, IObserver client)
        {
            Console.WriteLine("Service: enter login function, about to call repo.findUser");
            Domain.User user2 = userRepo.FindOne(user.id);
            Console.WriteLine("Service: User received from repository, about to check if valid");
            if (user2 == null || user2.password != user.password)
                throw new ServiceException("Login failed.");
            Console.WriteLine("didnt throw exception");
            observers.Add(client);
            Console.WriteLine("Service: User is valid, added to observers");
        }

        public void logout(IObserver obs)
        {
            observers.Remove(obs);
        }
    }
}
