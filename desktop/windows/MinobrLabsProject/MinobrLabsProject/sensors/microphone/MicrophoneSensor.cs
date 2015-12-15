using MinobrLabsProject.sensors.common;
using NAudio.Wave;
using System;

namespace MinobrLabsProject.sensors.microphone
{
    public class MicrophoneSensor : AbstractSensor
    {
        private double lastPressure;
        private WaveIn waveIn;

        public MicrophoneSensor() : base()
        {
            lastPressure = 0d;
            waveIn = new WaveIn();
        }

        public double getPressure()
        {
            return lastPressure;
        }

        public bool start()
        {
            if (WaveIn.DeviceCount > 0)
            {
                int diviceId = 0;
                waveIn.DeviceNumber = diviceId;
                waveIn.DataAvailable += update;
                int sampleRate = 44100;
                int channels = WaveIn.GetCapabilities(diviceId).Channels;
                waveIn.WaveFormat = new WaveFormat(sampleRate, channels);
                waveIn.StartRecording();
                return true;
            }
            return false;
        }

        private void update(object sender, WaveInEventArgs e)
        {
            byte[] buffer = e.Buffer;
            double sum = .0;
            for (int i = 0; i < buffer.Length; i += 2)
            {
                double sample = BitConverter.ToInt16(buffer, i) / 32768d;
                sum += sample * sample;
            }
            double rms = Math.Sqrt(sum / buffer.Length / 2d);
            lastPressure = 92.8 + 20d * Math.Log10(rms);
        }

        public void stop()
        {
            waveIn.StopRecording();
        }
    }
}
