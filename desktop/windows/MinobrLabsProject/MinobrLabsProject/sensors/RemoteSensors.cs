using MinobrLabsProject.db.entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.sensors
{
    static class RemoteSensors
    {
        private static Random rnd = new Random();

        public static Stat getData()
        {
            if (rnd.Next(2) == 0)
            {
                return new Stat(new float[] { rnd.Next(100) }, "microphone");
            }
            else
            {
                return new Stat(new float[] { rnd.Next(100) }, "humidity");
            }
        }
    }
}
