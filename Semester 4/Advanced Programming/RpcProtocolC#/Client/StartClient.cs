using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using services;
using network;

namespace Client
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            IServices server = new ServerProxy("127.0.0.1", 56565);
            ClientCtrl ctrl = new ClientCtrl(server);
            LoginForm login = new LoginForm(ctrl);
            Application.Run(login);
        }
    }
}
