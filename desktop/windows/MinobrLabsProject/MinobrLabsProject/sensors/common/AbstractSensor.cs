using System;

namespace MinobrLabsProject.sensors.common
{
    public abstract class AbstractSensor
    {
        public readonly float[] val;

        public AbstractSensor()
        {
            val = new float[1];
        }

        public void setVal(float[] val)
        {
            Array.Copy(val, 0, this.val, 0, 1);
        }

        public bool update(float val)
        {
            this.val[0] = val;

            return true;
        }

        public bool update(float[] val)
        {
            setVal(val);
            return true;
        }

        public bool update()
        {
            return update(val);
        }
    }
}
