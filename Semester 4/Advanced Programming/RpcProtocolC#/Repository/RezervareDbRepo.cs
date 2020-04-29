using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
namespace Repositories
{
    public class RezervareDbRepo : IRezervareRepo
    {
        public RezervareDbRepo()
        {

        }
        public void Delete(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Rezervare> FindAll()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Rezervare> FindByIdCursa(int idCursa)
        {
            IList<Rezervare> rezervari = new List<Rezervare>();
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "select * from Rezervari where idCursa=@idCursa";
                var param = cmd.CreateParameter();
                param.ParameterName = "@idCursa";
                param.Value = idCursa;
                cmd.Parameters.Add(param);
                using (var dataReader = cmd.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        int id = dataReader.GetInt32(0);
                        int idCursa2 = dataReader.GetInt32(1);
                        int nrLoc = dataReader.GetInt32(2);
                        string client = dataReader.GetString(3);
                        rezervari.Add(new Rezervare(id, idCursa2, nrLoc, client));
                    }
                }
            }
            con.Close();
            return rezervari;
        }

        public Rezervare FindOne(int id)
        {
            throw new NotImplementedException();

        }

        public void Save(Rezervare entity)
        {
            var con = DBUtils.getConnection();
            using (var cmd = con.CreateCommand())
            {
                cmd.CommandText = "insert into Rezervari (idCursa,nrLoc,client) values(@idCursa,@nrLoc,@client)";
                var paramId = cmd.CreateParameter();
                var paramNrLoc = cmd.CreateParameter();
                var paramClient = cmd.CreateParameter();

                paramId.ParameterName = "@idCursa";
                paramNrLoc.ParameterName = "@nrLoc";
                paramClient.ParameterName = "@client";

                paramId.Value = entity.idCursa;
                paramNrLoc.Value = entity.nrLoc;
                paramClient.Value = entity.client;

                cmd.Parameters.Add(paramId);
                cmd.Parameters.Add(paramNrLoc);
                cmd.Parameters.Add(paramClient);

                int result = cmd.ExecuteNonQuery();
            }
            con.Close();
        }

        public void Update(Rezervare entity)
        {
            throw new NotImplementedException();
        }
    }
}
