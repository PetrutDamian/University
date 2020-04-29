using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain
{
    [Serializable]
    public class User : Entity<string>
    {
        public string password { get; }

        public User(string id, string password) : base(id)
        {
            this.password = password;
        }

    }
}
