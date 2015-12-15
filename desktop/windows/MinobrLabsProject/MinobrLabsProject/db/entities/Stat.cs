using System;

namespace MinobrLabsProject.db.entities
{
    public class Stat
    {
        public string vals;
        public long date;
        public long experiment;
        public string type;

        public Stat() {}

        public Stat(long date, string vals, string type, long experiment)
        {
            this.vals = vals;
            this.type = type;
            this.experiment = experiment;
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

        public string toString() {
            return string.Format(
                    "%s : [\n  vals : %s\n  date : %s\n  experimentId : %s\n]",
                    vals,
                    getDateTime().ToString("dd.MM.yyyy HH:mm"),
                    experiment
            );
        }
    }
}
