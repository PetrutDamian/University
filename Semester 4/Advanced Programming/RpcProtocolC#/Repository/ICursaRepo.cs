using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace Repositories
{
    public interface ICursaRepo : ICrudRepository<int, Cursa>
    {
        IEnumerable<Cursa> FindByDestinationAndDate(string Destination, DateTime date);
    }
}
