using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain
{
    [Serializable]
    public class Entity<ID>
    {
        public ID id { get; }
        public Entity(ID id)
        {
            this.id = id;
        }
    }
}
