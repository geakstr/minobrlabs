using MinobrLabsProject.db.entities;
using MinobrLabsProject.sensors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.sensors
{
    class SensorCallback
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

            //App.temporaryStorage().add(stat);
            mainForm.Invoke(mainForm.webViewDelegate, new Object[] {stat.type, stat.getFloatVals(), stat.date});
        }
    }
}
