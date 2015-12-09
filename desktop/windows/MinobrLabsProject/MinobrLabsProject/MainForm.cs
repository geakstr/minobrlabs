using MinobrLabsProject.db;
using System;
using System.Data;
using System.IO;
using System.Linq;
using System.Windows.Forms;
using MinobrLabsProject.sensors;
using MinobrLabsProject.webview;

namespace MinobrLabsProject
{
    public partial class MainForm : Form
    {
        public WebViewDelegate webViewDelegate;
        private SensorsManager sensorManager;
        public TemporaryStorage temporaryStorage;
        private Preferences preferences;
        private bool isMainScreen;
        private bool isRecording;
        private bool isExperimentNow;

        public MainForm()
        {
            InitializeComponent();
            webViewDelegate = new WebViewDelegate(updateWebView);
            temporaryStorage = new TemporaryStorage();
            loadPreferences();
        }

        private void loadPreferences()
        {
            preferences = new Preferences();
            preferences.os = "browser";
            preferences.charts.accel = 0;
            preferences.charts.airTemperature = 0;
            preferences.charts.amperage = 0;
            preferences.charts.atmoPressure = 0;
            preferences.charts.gyro = 0;
            preferences.charts.humidity = 0;
            preferences.charts.light = 0;
            preferences.charts.microphone = 0;
            preferences.charts.ph = 0;
            preferences.charts.soluteTemperature = 0;
            preferences.charts.voltage = 0;
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            isMainScreen = true;
            isRecording = false;
            isExperimentNow = true;
            Enabled = false;
            persistButton.Enabled = false;
            webView.UseJavaScript = true;
            string path = Path.GetDirectoryName(Application.ExecutablePath);
            webView.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(documentCompleted);
            webView.UseDefaultContextMenu = false;
            webView.Url = new Uri(path + "/web/index.html");
            
            sensorManager = new SensorsManager();
            SensorCallback webViewCallback = new SensorCallback(this);
            sensorManager.setCallback(webViewCallback, webView);
        }

        private void documentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            Enabled = true;
            sensorManager.start();
        }

        private void updateWebView(string type, float[] vals, long date)
        {
            webView.StringByEvaluatingJavaScriptFromString(type + "([" + string.Join(", ", vals.Select(x => x.ToString()).ToArray()) + "]," + date + ')');
        }

        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            sensorManager.abort();
            Connection.close();
        }

        private void statButton_Click(object sender, EventArgs e)
        {
            if (isMainScreen)
            {
                webView.GetScriptManager.CallFunction("showStatsPage", new object[] { });

                statButton.Text = "ПОКАЗАТЕЛИ";
                isMainScreen = false;
                
                return;
            }
            webView.GetScriptManager.CallFunction("showMainPage", new object[] { });

            statButton.Text = "СТАТИСТИКА";
            isMainScreen = true;
        }

        private void recordingButton_Click(object sender, EventArgs e)
        {
            if (isRecording)
            {
                temporaryStorage.stopRecording();
                webView.GetScriptManager.CallFunction("isRecording", new object[] { false });

                isRecording = false;
                experimentButton.Enabled = true;
                persistButton.Enabled = false;
                recordingButton.Text = "ЗАПИСЫВАТЬ";
                return;
            }
            temporaryStorage.startRecording();
            webView.GetScriptManager.CallFunction("isRecording", new object[] { true });

            isRecording = true;
            experimentButton.Enabled = false;
            persistButton.Enabled = false;
            recordingButton.Text = "ОСТАНОВИТЬ";
        }
    }
}
