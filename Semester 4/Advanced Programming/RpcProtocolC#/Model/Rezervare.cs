using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain
{
    [Serializable]
    public class Rezervare : Entity<int>
    {
        public int idCursa { get; }
        public int nrLoc { get; }
        public string client { get; }

        public Rezervare(int id, int idCursa, int nrLoc, string client) : base(id)
        {
            this.idCursa = idCursa;
            this.nrLoc = nrLoc;
            this.client = client;
        }
    }
}
