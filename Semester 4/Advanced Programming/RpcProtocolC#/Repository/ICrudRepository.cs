using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace Repositories
{
    public interface ICrudRepository<ID, E> where E : Entity<ID>
    {
        void Save(E entity);
        void Delete(ID id);
        void Update(E entity);
        E FindOne(ID id);
        IEnumerable<E> FindAll();
    }
}
