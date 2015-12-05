namespace MinobrLabsProject
{
    partial class MainForm
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
            this.webView = new WebKit.WebKitBrowser();
            this.statButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // webView
            // 
            this.webView.AllowDrop = true;
            this.webView.BackColor = System.Drawing.Color.White;
            this.webView.Location = new System.Drawing.Point(12, 41);
            this.webView.Name = "webView";
            this.webView.Password = null;
            this.webView.PrivateBrowsing = false;
            this.webView.Size = new System.Drawing.Size(860, 458);
            this.webView.TabIndex = 0;
            this.webView.Url = null;
            this.webView.Username = null;
            // 
            // statButton
            // 
            this.statButton.Location = new System.Drawing.Point(782, 12);
            this.statButton.Name = "statButton";
            this.statButton.Size = new System.Drawing.Size(90, 23);
            this.statButton.TabIndex = 1;
            this.statButton.Text = "СТАТИСТИКА";
            this.statButton.UseVisualStyleBackColor = true;
            this.statButton.Click += new System.EventHandler(this.statButton_Click);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Window;
            this.ClientSize = new System.Drawing.Size(884, 511);
            this.Controls.Add(this.statButton);
            this.Controls.Add(this.webView);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "MainForm";
            this.Text = "MinobrLabs";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.MainForm_FormClosed);
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.ResumeLayout(false);

        }

        #endregion

        private WebKit.WebKitBrowser webView;
        private System.Windows.Forms.Button statButton;
    }
}

