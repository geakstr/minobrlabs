using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.db.entities
{
    public class Annotation
    {
        public long experiment;
        public int param;
        public long time;
        public string text;

        public Annotation() { }

        public Annotation(long experiment, int param, long time, string text)
        {
            this.experiment = experiment;
            this.param = param;
            this.time = time;
            this.text = text;
        }
        
        public string toString()
        {
            return string.Format(
                    "Annotation : [\n  experiment : %s\n  param : %s\n  time : %s\n  text : %s\n]",
                    experiment, param, time, text
            );
        }
    }
}
