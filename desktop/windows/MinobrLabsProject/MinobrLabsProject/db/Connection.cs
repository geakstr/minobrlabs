using System.Data.SQLite;
using System.Windows.Forms;
using System.IO;

namespace MinobrLabsProject.db
{
    public class Connection
    {
        private static SQLiteConnection connection;
        private static string path = Path.GetDirectoryName(Application.ExecutablePath) + "/database";

        public static SQLiteConnection getConnection()
        {
            System.IO.Directory.CreateDirectory(path);
            path += "/minobrlabs.db";
            bool DBWasCreated = false;
            if (!File.Exists(path))
            {
                SQLiteConnection.CreateFile(path);
                DBWasCreated = true;
            }

            if (connection == null)
            {
                connection = new SQLiteConnection("Data Source=" + path + ";Version=3;");
                connection.Open();
            }

            if (DBWasCreated)
            {
                DataManager.createDatabase(connection);
            }
            return connection;
        }

        public static void close()
        {
            if (connection != null)
            {
                connection.Close();
                connection.Dispose();
                connection = null;
            }
        }

    }
}
