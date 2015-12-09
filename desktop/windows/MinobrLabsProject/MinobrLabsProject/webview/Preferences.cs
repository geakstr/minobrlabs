namespace MinobrLabsProject.webview
{
    public class Preferences
    {
        public string os { get; set; }
        public Charts charts { get; set; }

        public Preferences()
        {
            charts = new Charts();
        }

        public class Charts
        {
            public int microphone { get; set; }
            public int accel { get; set; }
            public int gyro { get; set; }
            public int airTemperature { get; set; }
            public int humidity { get; set; }
            public int atmoPressure { get; set; }
            public int light { get; set; }
            public int soluteTemperature { get; set; }
            public int voltage { get; set; }
            public int amperage { get; set; }
            public int ph { get; set; }

            public Charts() { }
        }
    }
}
