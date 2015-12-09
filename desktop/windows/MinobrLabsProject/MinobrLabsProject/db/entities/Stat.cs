using System;
using System.Linq;

namespace MinobrLabsProject.db.entities
{
    public class Stat
    {
        public string vals;
        public long date;
        public long experimentId;
        public string type;

        public Stat() {}

        public Stat(string vals, string type)
        {
            this.vals = vals;
            this.type = type;
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1);
            date = (long)ts.TotalMilliseconds;
            experimentId = -1L;
        }

        public Stat(float[] vals, string type) : this('[' + string.Join(", ", vals.Select(x => x.ToString()).ToArray()) + ']', type) {}

        public float[] getFloatVals()
        {
            return vals.Replace(" ", "").Replace("[", "").Replace("]", "").Split(',').Select(n => float.Parse(n)).ToArray();
        }

        private DateTime getDateTime()
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
                    experimentId
            );
        }
    }
}
