namespace MinobrLabsProject
{
    partial class gaugeForm
    {
        /// <summary>
        /// Требуется переменная конструктора.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Освободить все используемые ресурсы.
        /// </summary>
        /// <param name="disposing">истинно, если управляемый ресурс должен быть удален; иначе ложно.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Код, автоматически созданный конструктором форм Windows

        /// <summary>
        /// Обязательный метод для поддержки конструктора - не изменяйте
        /// содержимое данного метода при помощи редактора кода.
        /// </summary>
        private void InitializeComponent()
        {
            this.gaugeBrowser = new WebKit.WebKitBrowser();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.SuspendLayout();
            // 
            // gaugeBrowser
            // 
            this.gaugeBrowser.AllowDrop = true;
            this.gaugeBrowser.BackColor = System.Drawing.Color.White;
            this.gaugeBrowser.Location = new System.Drawing.Point(12, 22);
            this.gaugeBrowser.Name = "gaugeBrowser";
            this.gaugeBrowser.Password = null;
            this.gaugeBrowser.PrivateBrowsing = false;
            this.gaugeBrowser.Size = new System.Drawing.Size(810, 477);
            this.gaugeBrowser.TabIndex = 0;
            this.gaugeBrowser.Url = null;
            this.gaugeBrowser.Username = null;
            // 
            // checkBox1
            // 
            this.checkBox1.Appearance = System.Windows.Forms.Appearance.Button;
            this.checkBox1.AutoSize = true;
            this.checkBox1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.checkBox1.FlatAppearance.BorderColor = System.Drawing.Color.Silver;
            this.checkBox1.FlatAppearance.CheckedBackColor = System.Drawing.Color.LightGray;
            this.checkBox1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.checkBox1.ForeColor = System.Drawing.SystemColors.ControlText;
            this.checkBox1.Location = new System.Drawing.Point(733, 12);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.Size = new System.Drawing.Size(89, 23);
            this.checkBox1.TabIndex = 1;
            this.checkBox1.Text = "СТАТИСТИКА";
            this.checkBox1.UseMnemonic = false;
            this.checkBox1.UseVisualStyleBackColor = true;
            this.checkBox1.CheckedChanged += new System.EventHandler(this.checkBox1_CheckedChanged);
            // 
            // gaugeForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Window;
            this.ClientSize = new System.Drawing.Size(834, 511);
            this.Controls.Add(this.checkBox1);
            this.Controls.Add(this.gaugeBrowser);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "gaugeForm";
            this.Text = "MinobrLabs";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private WebKit.WebKitBrowser gaugeBrowser;
        private System.Windows.Forms.CheckBox checkBox1;
    }
}

