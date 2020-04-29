using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
using log4net;
namespace Repositories
{
    public class CursaDbRepo : ICursaRepo
    {
        public CursaDbRepo()
        {
            log.Info("Creating CursaDbRepo");
        }
        private static readonly ILog log = LogManager.GetLogger("CursaDbRepo");
        public void Delete(int id)
        {
            log.InfoFormat("Enter Delete id = {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "delete from Curse where id = @id";
                IDbDataParameter param = cmd.CreateParameter();
                param.ParameterName = "@id";
                param.Value = id;
                cmd.Parameters.Add(param);
                int result = cmd.ExecuteNonQuery();
                con.Close();
            }
            log.InfoFormat("Exit Delete");
        }

        public IEnumerable<Cursa> FindAll()
        {
            log.InfoFormat("Enter findAll()");
            IList<Cursa> curse = new List<Cursa>();
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {

                cmd.CommandText = "Select * from Curse";

                using (var dataReader = cmd.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        int id2 = dataReader.GetInt32(0);
                        string destinatie = dataReader.GetString(1);
                        DateTime date = dataReader.GetDateTime(2);
                        int locuri = dataReader.GetInt32(3);
                        Cursa cursa = new Cursa(id2, destinatie, date, locuri);
                        curse.Add(cursa);
                    }
                    con.Close();
                }
            }
            log.InfoFormat("Exit findAll with :{0}", curse);
            return curse;
        }

        public IEnumerable<Cursa> FindByDestinationAndDate(string destination, DateTime date)
        {
            log.InfoFormat("Enter findByDestinationAndDate with destination={0} ; date={1}", destination, date);
            IList<Cursa> curse = new List<Cursa>();
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "Select * from Curse where destinatie = @destination and date = @date";
                IDbDataParameter destParam = cmd.CreateParameter();
                destParam.ParameterName = "@destination";
                destParam.Value = destination;
                IDbDataParameter dateParam = cmd.CreateParameter();
                dateParam.ParameterName = "@date";
                dateParam.Value = date;
                cmd.Parameters.Add(destParam);
                cmd.Parameters.Add(dateParam);

                using (var dataReader = cmd.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        int id2 = dataReader.GetInt32(0);
                        string destinatie = dataReader.GetString(1);
                        DateTime date2 = dataReader.GetDateTime(2);
                        int locuri = dataReader.GetInt32(3);
                        Cursa cursa = new Cursa(id2, destinatie, date, locuri);
                        curse.Add(cursa);
                    }
                    con.Close();
                }
            }
            log.InfoFormat("Exit findByDestinationAndDate with :{0}", curse);
            return curse;
        }

        public Cursa FindOne(int id)
        {
            log.InfoFormat("Enter FIndOne with key = {0}", id);
            var con = DBUtils.getConnection();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Curse where id=@id";
                IDbDataParameter param = comm.CreateParameter();
                param.ParameterName = "@id";
                param.Value = id;
                comm.Parameters.Add(param);
                using (var dataReader = comm.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        int id2 = dataReader.GetInt32(0);
                        string destinatie = dataReader.GetString(1);
                        DateTime date = dataReader.GetDateTime(2);
                        int locuri = dataReader.GetInt32(3);
                        Cursa cursa = new Cursa(id2, destinatie, date, locuri);
                        log.InfoFormat("Exit findOne with :{0}", cursa);
                        con.Close();
                        return cursa;
                    }
                }
            }
            log.InfoFormat("Exit findOne with :{0}", null);
            return null;
        }

        public void Save(Cursa entity)
        {
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "insert into Curse values(@id,@destinatie,@date,@locuri)";
                var idParam = cmd.CreateParameter();
                var destParam = cmd.CreateParameter();
                var dateParam = cmd.CreateParameter();
                var locuriParam = cmd.CreateParameter();

                idParam.ParameterName = "@id";
                destParam.ParameterName = "@destinatie";
                dateParam.ParameterName = "@date";
                locuriParam.ParameterName = "@locuri";

                idParam.Value = entity.id;
                destParam.Value = entity.destinatie;
                dateParam.Value = entity.date;
                locuriParam.Value = entity.locuriDisponibile;

                cmd.Parameters.Add(idParam);
                cmd.Parameters.Add(destParam);
                cmd.Parameters.Add(dateParam);
                cmd.Parameters.Add(locuriParam);

                int result = cmd.ExecuteNonQuery();
                con.Close();
            }
        }

        public void Update(Cursa entity)
        {
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "update Curse set destinatie=@dest,date=@date,locuri=@locuri where id=@id";
                var destParam = cmd.CreateParameter();
                var dateParam = cmd.CreateParameter();
                var locuriParam = cmd.CreateParameter();
                var idParam = cmd.CreateParameter();

                destParam.ParameterName = "@dest";
                dateParam.ParameterName = "@date";
                locuriParam.ParameterName = "@locuri";
                idParam.ParameterName = "@id";

                destParam.Value = entity.destinatie;
                dateParam.Value = entity.date;
                locuriParam.Value = entity.locuriDisponibile;
                idParam.Value = entity.id;

                cmd.Parameters.Add(destParam);
                cmd.Parameters.Add(dateParam);
                cmd.Parameters.Add(locuriParam);
                cmd.Parameters.Add(idParam);

                int result = cmd.ExecuteNonQuery();
                con.Close();
            }
        }

    }
}
