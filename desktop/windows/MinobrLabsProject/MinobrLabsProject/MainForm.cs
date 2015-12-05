using MinobrLabsProject.db;
using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.IO;
using System.Text;
using System.Linq;
using System.Windows.Forms;
using System.Threading.Tasks;
using MinobrLabsProject.sensors;
using MinobrLabsProject.db.entities;
using MinobrLabsProject.webview;

namespace MinobrLabsProject
{  
    public partial class MainForm : Form
    {
        private SensorsManager sensorManager;
        public WebViewDelegate webViewDelegate;
        private Preferences preferences;

        public MainForm()
        {
            InitializeComponent();
            webViewDelegate = new WebViewDelegate(updateWebView);
            loadPreferences();
        }

        private void loadPreferences()
        {
            preferences = new Preferences();
            preferences.os = "android";
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
            Enabled = false;
            webView.UseJavaScript = true;
            string path = Path.GetDirectoryName(Application.ExecutablePath);
            webView.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(documentCompleted);
            webView.UseDefaultContextMenu = false;
            webView.Url = new Uri(path + "/web/index.html");
            
            sensorManager = new SensorsManager();
            SensorCallback webViewCallback = new SensorCallback(this);
            sensorManager.setCallback(webViewCallback, webView);
        }

        void documentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            Enabled = true;
            sensorManager.start();
        }

        private void statButton_CheckedChanged(object sender, EventArgs e)
        {
            webView.GetScriptManager.CallFunction("init", new object[] {  });
        }

        private void updateWebView(string type, float[] vals, long date)
        {
            webView.StringByEvaluatingJavaScriptFromString(type + "([" + string.Join(", ", vals.Select(x => x.ToString()).ToArray()) + "]," + date + ')');
        }

        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            sensorManager.stop();
            sensorManager.abort();
            Connection.close();
        }

        private void statButton_Click(object sender, EventArgs e)
        {
            if (preferences.os.Equals("android"))
            {
                statButton.Text = "ПОКАЗАТЕЛИ";
                preferences.os = "browser";
                webView.GetScriptManager.CallFunction("showStatsPage", new object[] { });
            }
            else
            {
                statButton.Text = "СТАТИСТИКА";
                preferences.os = "android";
                webView.GetScriptManager.CallFunction("showMainPage", new object[] { });
            }
        }
    }
}
