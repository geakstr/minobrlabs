using System;

namespace MinobrLabsProject.db.entities
{
    public class Experiment
    {
        public string name;
        public long date;

        public Experiment() {}

        public Experiment(string name) {
            this.name = name;
            setDate(DateTime.Now);
        }

        public void setDate(DateTime date)
        {
            TimeSpan ts = date.ToUniversalTime() - new DateTime(1970, 1, 1);
            this.date = (long)ts.TotalMilliseconds;
        }

        public DateTime getDateTime()
        {
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeKind.Utc);
            dateTime = dateTime.AddSeconds(Math.Round(date / 1000d)).ToLocalTime();
            return dateTime;
        }

        public override string ToString() {
            return string.Format("%s [%s]", name, date.ToString("dd.MM.yyyy HH:ss"));
        }
    }
}
