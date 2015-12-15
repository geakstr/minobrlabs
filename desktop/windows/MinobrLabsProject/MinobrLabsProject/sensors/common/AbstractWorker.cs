using System.Threading;

namespace MinobrLabsProject.sensors.common
{
    public abstract class AbstractWorker
    {
        protected volatile bool running;
        private Thread thread;

        public AbstractWorker()
        {
            running = false;
            thread = new Thread(new ThreadStart(run));
            thread.SetApartmentState(ApartmentState.STA);
            thread.IsBackground = true;
        }
        
        public abstract void run();
        
        public void start()
        {
            if (!running)
            {
                running = true;
                thread.Start();
            }
        }

        public void kill()
        {
            running = false;
            thread.Abort();
        }
    }
}
