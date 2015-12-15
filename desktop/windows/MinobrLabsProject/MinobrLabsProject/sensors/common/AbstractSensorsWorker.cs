using System;
using System.Threading;

namespace MinobrLabsProject.sensors.common
{
    public abstract class AbstractSensorsWorker<T> : AbstractWorker where T : ISensorsManager
    {
        private readonly T sensors;

        public AbstractSensorsWorker(T sensors) : base()
        {
            this.sensors = sensors;
        }
        
        public override void run()
        {
            try
            {
                while (running)
                {
                    sensors.update();
                    sleep();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Sensors worker problems", e);
            }
        }

        protected void sleep()
        {
            Thread.Sleep(200); //TODO Брать время из конфига
        }
    }
}
