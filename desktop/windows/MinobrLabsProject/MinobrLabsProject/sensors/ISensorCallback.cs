using MinobrLabsProject.db.entities;

namespace MinobrLabsProject.sensors
{
    public interface ISensorCallback
    {
        void onReceiveResult(Stat stat);
    }
}
