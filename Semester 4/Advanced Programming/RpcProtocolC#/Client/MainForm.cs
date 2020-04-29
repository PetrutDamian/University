using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Domain;
using services;
namespace Client
{
    public partial class MainForm : Form
    {
        private  ClientCtrl ctrl;
        private DataTable dataTableCurse = new DataTable();
        private DataTable dataTableRezervari = new DataTable();
        private Cursa cursaSelectata;
        private int actualBookings;
        private BindingSource bs;
        private BindingSource bs2;
        public MainForm(ClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;

            addColumnsToTables();
            bs = new BindingSource();
            bs.DataSource = dataTableCurse;
            gridCurse.DataSource = bs;
            populateCurse();
            bs2 = new BindingSource();
            bs2.DataSource = dataTableRezervari;
            gridRezervari.DataSource = bs2;
            ctrl.updateEvent += newUpdate;
        }
        public void newUpdate(object sender, newUpdateEventArgs e)
        {
            switch (e.EventType)
            {
                case EventType.SeatsDecremented:
                    Console.WriteLine("SeatsDecremented update found in switch.");
                    Cursa cursa = (Cursa)e.Data;
                    gridCurse.BeginInvoke(new eventToUpdateCursa(this.updateCursa), new Object[] { dataTableCurse, cursa });
                    break;
                case EventType.NewBookings:
                    Console.WriteLine("NewBOokngs update found in switch.");
                    List<Rezervare> bookings = (List<Rezervare>)e.Data;
                    gridRezervari.BeginInvoke(new eventToUpdateBookings(this.updateBookings),new Object[] {
                    dataTableRezervari,bookings});
                    break;
            }

        }
        private void updateCursa(DataTable table,Cursa cursa)
        {
            for (int i = 0; i < table.Rows.Count; i++)
            {
                var row = table.Rows[i];
                string destinatie = (string)row["Destinatie"];
                DateTime date = (DateTime)row["Data"];
                if (destinatie.Equals(cursa.destinatie) && date.Equals(cursa.date))
                {
                    row.SetField("Locuri", cursa.locuriDisponibile);
                    return;
                }
            }
        }
        private void updateBookings(DataTable table,List<Rezervare> bookings)
        {
            if (cursaSelectata == null || bookings.ElementAt(0).idCursa != cursaSelectata.id)
                return;
            foreach(var b in bookings)
                table.Rows[b.nrLoc - 1].SetField("Client", b.client);
            actualBookings += bookings.Count;
        }
        public delegate void eventToUpdateBookings(DataTable table, List<Rezervare> bookings);
        public delegate void eventToUpdateCursa(DataTable table, Cursa cursa);

            private void addColumnsToTables()
            {
                dataTableCurse.Columns.Add("Destinatie", typeof(string));
                dataTableCurse.Columns.Add("Data", typeof(DateTime));
                dataTableCurse.Columns.Add("Locuri", typeof(int));
                dataTableRezervari.Columns.Add("Numar loc", typeof(int));
                dataTableRezervari.Columns.Add("Client", typeof(string));
            }
            private void populateCurse()
            {
                dataTableCurse.Rows.Clear();
                List<Cursa> curse = ctrl.getAllCurse();
                foreach (Cursa cursa in curse)
                    dataTableCurse.Rows.Add(cursa.destinatie, cursa.date, cursa.locuriDisponibile);
            }
            private void populateRezervari()
            {
                dataTableRezervari.Rows.Clear();
                try
                {
                    Console.WriteLine("About to go into controller to getAllBookings");
                    List<Rezervare> rezervari = ctrl.getAllBookings(cursaSelectata.id);
                    Console.WriteLine("Received Bookings");
                    actualBookings = rezervari.Count;
                    foreach (var rezervare in rezervari)
                        dataTableRezervari.Rows.Add(rezervare.nrLoc, rezervare.client);
                    int nrRows = dataTableRezervari.Rows.Count;
                    for (int i = nrRows + 1; i <= 18; i++)
                        dataTableRezervari.Rows.Add(i, "-");
                }
                catch (ServiceException ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
            private void MainForm_Load(object sender, EventArgs e)
            {
                setVisible(false);
            }

            private void setVisible(bool value)
            {
                labelDestination.Visible = value;
                labelDate.Visible = value;
                labelClient.Visible = value;
                labelSeats.Visible = value;
                gridRezervari.Visible = value;
                textBoxDateLabel.Visible = value;
                textBoxClient.Visible = value;
                btnBooking.Visible = value;
                textBoxDestinationLabel.Visible = value;
                textBoxSeats.Visible = value;
            }

            private void btnBooking_Click(object sender, EventArgs e)
            {
                string client = textBoxClient.Text;
                string seatsStrin = textBoxSeats.Text;
                int seats;
                int.TryParse(seatsStrin, out seats);
                for (int i = 0; i < dataTableCurse.Rows.Count; i++)
                {
                    var row = dataTableCurse.Rows[i];
                    string destinatie = (string)row["Destinatie"];
                    DateTime date = (DateTime)row["Data"];
                    if (destinatie.Equals(cursaSelectata.destinatie) && date.Equals(cursaSelectata.date))
                    {
                        int nrLocuri = (int)row["Locuri"];
                        if (seats > nrLocuri)
                        {
                            MessageBox.Show("Nu sunt locuri suficiente!");
                            break;
                        }
                        else
                        {
                            List<Rezervare> rezervari = new List<Rezervare>();
                            for (int j = actualBookings + 1; j <= actualBookings + seats; j++)
                            {
                                Rezervare rezervare = new Rezervare(0, cursaSelectata.id, j, client);
                                rezervari.Add(rezervare);
                            }
                            try
                            {
                                ctrl.makeBooking(rezervari);
                            cursaSelectata.locuriDisponibile -= seats;

                        } catch (Exception ex)
                            {
                                MessageBox.Show(ex.Message);
                            }
                            break;
                        }
                    }
                }
            }

            private void btnSearch_Click(object sender, EventArgs e)
            {
                string destination = textBoxDestination.Text;
                string dateString = textBoxDate.Text;
                DateTime date;
                DateTime.TryParse(dateString, out date);
                Cursa c = null;
                try
                {
                    c = ctrl.findByDestinationAndDate(destination, date);
                    cursaSelectata = c;
                    setVisible(true);
                    textBoxDestinationLabel.Text = c.destinatie;
                    textBoxDateLabel.Text = date.ToLongTimeString();
                    Console.WriteLine("Found cursa" + c.destinatie);
                    populateRezervari();
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }

            private void btnLogout_Click(object sender, EventArgs e)
            {
                ctrl.logout();

                LoginForm f = new LoginForm(ctrl);
                this.Hide();
                f.ShowDialog();
                this.Close();

            }
        }
    }
