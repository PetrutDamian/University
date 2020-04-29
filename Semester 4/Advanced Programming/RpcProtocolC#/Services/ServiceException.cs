using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace services
{
    [Serializable]
    public class ServiceException:Exception
    {
    public ServiceException() { }
    public ServiceException(String message) : base(message) {}
}
}
