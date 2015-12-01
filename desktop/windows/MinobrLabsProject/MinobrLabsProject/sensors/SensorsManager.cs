using MinobrLabsProject.sensors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using WebKit;

namespace MinobrLabsProject.sensors
{
    class SensorsManager
    {
        private volatile bool running;
        private int sleepTime;
        private SensorCallback callback;
        private Thread thread;

        public SensorsManager()
        {
            this.running = false;
            sleepTime = 300;
        }

        public void setCallback(SensorCallback callback, WebKitBrowser webView)
        {
            this.callback = callback;
            thread = new Thread(new ThreadStart(run));
            thread.SetApartmentState(ApartmentState.STA);
            thread.IsBackground = true;
        }

        public void run()
        {
            while (isRunning())
            {
                if (callback != null)
                {
                    callback.onReceiveResult(RemoteSensors.getData());
                }
                sleep();
            }
        }

        public void start()
        {
            if (!running)
            {
                thread.Start();
                running = true;
            }
        }

        public void stop()
        {
            running = false;
        }

        public void abort()
        {
            thread.Abort();
        }

        public void setSleepTime(int sleepTime)
        {
            this.sleepTime = sleepTime;
        }

        public bool isRunning()
        {
            return running;
        }

        public static float[] lowPass(float[] input, float[] output, float alpha)
        {
            if (output == null)
            {
                return input;
            }
            for (int i = 0; i < input.Length; i++)
            {
                output[i] = lowPass(input[i], output[i], alpha);
            }
            return output;
        }

        public static float lowPass(float input, float output, float alpha)
        {
            return alpha * output + (1 - alpha) * input;
        }

        protected void sleep()
        {
            try
            {
                Thread.Sleep(sleepTime);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message, e);
            }
        }

    }
}
