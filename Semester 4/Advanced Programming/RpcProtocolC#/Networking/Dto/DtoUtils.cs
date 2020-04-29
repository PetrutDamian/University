using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace network
{
    public class DtoUtils
    {
        public static DestinationDateDto createDestinationDateDto(String destination, DateTime date)
        {
            return new DestinationDateDto(destination, date);
        }
    }

}
