using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MinobrLabsProject
{
    public partial class gaugeForm : Form
    {
        public gaugeForm()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string path = Path.GetDirectoryName(Application.ExecutablePath) + "\\web\\index.html";
            gaugeBrowser.Url = new Uri(path);
            gaugeBrowser.UseDefaultContextMenu = false;
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            if (checkBox1.Checked)
            {
                string path = Path.GetDirectoryName(Application.ExecutablePath) + "\\web\\stats.html";
                gaugeBrowser.Url = new Uri(path);
            }
            else
            {
                string path = Path.GetDirectoryName(Application.ExecutablePath) + "\\web\\index.html";
                gaugeBrowser.Url = new Uri(path);
            }
        }
    }
}
