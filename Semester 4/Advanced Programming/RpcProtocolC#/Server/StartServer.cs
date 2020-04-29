using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Repositories;
using services;
using network;
using Thrift.Transport;
using Thrift.Protocol;
using Thrift.Server;

namespace server
{
    class StartServer
    {
        public static void Main(string[] args)
        {
           
            IUserRepo userRepo = new UserDbRepo();
            IRezervareRepo rezervareRepo = new RezervareDbRepo();
            ICursaRepo cursaRepo = new CursaDbRepo();
            IServices service = new Service(userRepo,cursaRepo,rezervareRepo);

            AbstractServer server = new RemoteConcurrentServer("127.0.0.1", 56565, service);
            server.Start();
            Console.WriteLine("server started ...");

            
        }
    }
}
