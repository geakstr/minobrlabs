using MinobrLabsProject.db.entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.sensors
{
    interface ISensorCallback
    {
        void onReceiveResult(Stat stat);
    }
}
