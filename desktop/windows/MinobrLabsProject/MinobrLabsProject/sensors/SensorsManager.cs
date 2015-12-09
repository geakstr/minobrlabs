using System.Threading;
using WebKit;

namespace MinobrLabsProject.sensors
{
    public class SensorsManager
    {
        private volatile bool running;
        private int sleepTime;
        private SensorCallback callback;
        private Thread thread;
        private RemoteSensors remoteSensors;
        private MicrophoneSensor microphoneSensor;

        public SensorsManager()
        {
            running = false;
            sleepTime = 300;
            remoteSensors = new RemoteSensors();
            microphoneSensor = new MicrophoneSensor();
            microphoneSensor.open();
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
                    callback.onReceiveResult(remoteSensors.getVals());
                    callback.onReceiveResult(microphoneSensor.getVals());
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

        public void abort()
        {
            running = false;
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
            Thread.Sleep(sleepTime);
        }

    }
}
