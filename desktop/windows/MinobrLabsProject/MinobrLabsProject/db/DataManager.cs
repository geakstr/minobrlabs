using System.Collections.Generic;
using System.Data.SQLite;
using MinobrLabsProject.db.entities;
using System.Data;

namespace MinobrLabsProject.db
{
    public class DataManager
    {
        public static void createDatabase(SQLiteConnection connection)
        {
            string sql = "CREATE TABLE experiment (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date INTEGER);";
            sql += "CREATE TABLE accel(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE atmopressure(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE airtemperature(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE amperage(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE gyro(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE humidity(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE light(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE microphone(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE ph(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE solutetemperature(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id));";
            sql += "CREATE TABLE voltage(_id INTEGER PRIMARY KEY AUTOINCREMENT, vals TEXT, date INTEGER, experimentId INTEGER, FOREIGN KEY(experimentId) REFERENCES experiment(_id))";
            SQLiteCommand command = new SQLiteCommand(sql, connection);
            command.ExecuteNonQuery();
        }

        public static long put(Experiment experiment)
        {
            SQLiteConnection connection = Connection.getConnection();
            string sql = "INSERT INTO experiment(name, date) VALUES(@ename, @edate)";
            SQLiteCommand command = new SQLiteCommand(sql, connection);
            command.Parameters.Add("@ename", DbType.String);
            command.Parameters["@ename"].Value = experiment.name;
            command.Parameters.Add("@edate", DbType.Int64);
            command.Parameters["@edate"].Value = experiment.date;
            command.ExecuteNonQuery();

            sql = "SELECT MAX(_id) FROM experiment";
            command = new SQLiteCommand(sql, connection);
            SQLiteDataReader rdr = command.ExecuteReader();
            long id = -1;
            while (rdr.Read())
            {
                id = rdr.GetInt64(0);
            }
            return id;
        }

        public static void put(List<Stat> data)
        {
            SQLiteConnection connection = Connection.getConnection();
            foreach (Stat stat in data)
            {
                string sql = "INSERT INTO " + stat.type + "(vals, date, experimentId) VALUES(@vals, @sdate, @experimentId)";
                SQLiteCommand command = new SQLiteCommand(sql, connection);
                command.Parameters.Add("@vals", DbType.String);
                command.Parameters["@vals"].Value = stat.vals;
                command.Parameters.Add("@sdate", DbType.Int64);
                command.Parameters["@sdate"].Value = stat.date;
                command.Parameters.Add("@experimentId", DbType.Int64);
                command.Parameters["@experimentId"].Value = stat.experimentId;
                command.ExecuteNonQuery();
            }
        }
    }
}
