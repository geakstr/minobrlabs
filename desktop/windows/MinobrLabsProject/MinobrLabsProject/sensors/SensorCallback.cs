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

        public void onReceiveResult(double val)
        {
            Stat stat = new Stat();
            stat.setDate(DateTime.Now);
            stat.type = "microphone";
            stat.vals = "[" + val + "]";
            stat.experiment = -1;
            mainForm.temporaryStorage.add(stat);
            mainForm.Invoke(mainForm.webViewDelegate, new object[] { stat.type, stat.vals, stat.date });
        }
    }
}
