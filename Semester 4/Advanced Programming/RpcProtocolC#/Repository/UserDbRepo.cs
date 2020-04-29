using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace Repositories
{
    public class UserDbRepo : IUserRepo
    {
        private static readonly ILog log = LogManager.GetLogger("UserDbRepo");
        public UserDbRepo()
        {
            log.Info("Creating UserDbRepo");
        }

        public void Delete(string id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<User> FindAll()
        {
            throw new NotImplementedException();
        }

        public User FindOne(string id)
        {
            Console.WriteLine("Enter function findOne with key " + id);
            var connection = DBUtils.getConnection();
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "Select * from Users where id=@id";
                var param = cmd.CreateParameter();
                param.ParameterName = "@id";
                param.Value = id;
                cmd.Parameters.Add(param);
                using (var dataReader = cmd.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        string username = dataReader.GetString(0);
                        string password = dataReader.GetString(1);
                        connection.Close();
                        return new User(username, password);
                    }
                }
            }
            return null;
        }

        public void Save(User entity)
        {
            throw new NotImplementedException();
        }

        public void Update(User entity)
        {
            throw new NotImplementedException();
        }
    }
}
