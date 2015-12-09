using MinobrLabsProject.db.entities;
using System;

namespace MinobrLabsProject.sensors
{
    public class RemoteSensors
    {
        private Random rnd;

        public RemoteSensors()
        {
            rnd = new Random();
        }

        public Stat getVals()
        {
            if (rnd.Next(2) == 0)
            {
                return new Stat(new float[] { rnd.Next(100) }, "voltage");
            }
            else
            {
                return new Stat(new float[] { rnd.Next(100) }, "humidity");
            }
        }
    }
}
