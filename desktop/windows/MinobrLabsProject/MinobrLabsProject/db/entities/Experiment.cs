using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.db.entities
{
    class Experiment
    {
        public string name;
        public long date;

        public Experiment() {}

        public Experiment(string name) {
            this.name = name;
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1);
            date = (long)ts.TotalMilliseconds;
        }

        private DateTime getDateTime()
        {
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeKind.Utc);
            dateTime = dateTime.AddSeconds(Math.Round(date / 1000d)).ToLocalTime();
            return dateTime;
        }

        public string ToString() {
            return string.Format("%s [%s]", name, date.ToString("dd.MM.yyyy HH:ss"));
        }
    }
}
