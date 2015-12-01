using MinobrLabsProject.db.entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MinobrLabsProject.db
{
    class TemporaryStorage
    {
        private List<Stat> data;

        private bool recording = false;
        private bool wasRecording = false;

        public TemporaryStorage()
        {
            this.data = new List<Stat>();
            this.recording = false;
            this.wasRecording = false;
        }

        public void startRecording()
        {
            recording = true;
            wasRecording = true;
        }

        public void stopRecording()
        {
            recording = false;
        }

        public void clear()
        {
            data.Clear();
            recording = false;
            wasRecording = false;
        }

        public void persist(Experiment experiment)
        {
            if (wasRecording) {
                long id = DataManager.put(experiment);
                foreach (Stat stat in data) {
                    stat.experimentId = id;
                }
                DataManager.put(data);
                clear();
            }
        }

        public void add(Stat stat)
        {
            if (recording) {
                data.Add(stat);
            }
        }
    }
}
