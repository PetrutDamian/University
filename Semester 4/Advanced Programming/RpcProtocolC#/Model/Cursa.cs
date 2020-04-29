using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain
{
    [Serializable]
    public class Cursa : Entity<int>
    {
        public string destinatie { get; }
        public DateTime date { get; }
        public int locuriDisponibile { get; set; }

        public override string ToString()
        {
            return "Cursa{" +
                    "destinatie='" + destinatie + '\'' +
                    ", date=" + date +
                    ", locuriDisponibile=" + locuriDisponibile +
                    ", id=" + this.id +
                    '}';
        }
        public Cursa(int id, String destinatie, DateTime date) : base(id)
        {
            this.destinatie = destinatie;
            this.date = date;
            this.locuriDisponibile = 18;
        }
        public Cursa(int id, String destinatie, DateTime date, int nrLocuri) : base(id)
        {
            this.destinatie = destinatie;
            this.date = date;
            this.locuriDisponibile = nrLocuri;
        }
    }
}
