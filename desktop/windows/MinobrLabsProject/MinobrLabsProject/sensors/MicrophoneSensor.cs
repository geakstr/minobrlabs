using MinobrLabsProject.db.entities;
using NAudio.Wave;
using System;
using System.Collections.Generic;

namespace MinobrLabsProject.sensors
{
    public class MicrophoneSensor
    {
        private WaveIn waveIn;
        private double val;

        public MicrophoneSensor()
        {
            waveIn = new WaveIn();
        }

        public Stat getVals()
        {
            return new Stat(new float[] { (float) val }, "microphone");
        }

        public void open()
        {
            if (WaveIn.DeviceCount > 0)
            {
                int diviceId = 0;
                waveIn.DeviceNumber = diviceId;
                waveIn.DataAvailable += waveIn_DataAvailable;
                int sampleRate = 44100;
                int channels = WaveIn.GetCapabilities(diviceId).Channels;
                waveIn.WaveFormat = new WaveFormat(sampleRate, channels);
                waveIn.StartRecording();
            }
        }

        private void waveIn_DataAvailable(object sender, WaveInEventArgs e)
        {
            byte[] buffer = e.Buffer;
            double sum = .0;
            for (int i = 0; i < buffer.Length; i += 2)
            {
                double sample = BitConverter.ToInt16(buffer, i) / 32768d;
                sum += sample * sample;
            }
            double rms = Math.Sqrt(sum / buffer.Length / 2d);
            double decibel = 92.8 + 20d * Math.Log10(rms);
            val = decibel;
        }

        public void stop()
        {
            waveIn.StopRecording();
        }
    }
}
