using MinobrLabsProject.db.entities;
using System;

namespace MinobrLabsProject.sensors
{
    public class SensorCallback
    {
        MainForm mainForm;
        Random rnd;

        public SensorCallback(MainForm mainForm)
        {
            this.mainForm = mainForm;
            rnd = new Random();
        }

        public void onReceiveResult(Stat stat)
        {
            if (null == stat)
            {
                return;
            }

            mainForm.temporaryStorage.add(stat);
            mainForm.Invoke(mainForm.webViewDelegate, new object[] {stat.type, stat.getFloatVals(), stat.date});
        }
    }
}
