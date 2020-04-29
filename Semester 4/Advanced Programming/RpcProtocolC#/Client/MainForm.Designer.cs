namespace Client
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.labelDestination = new System.Windows.Forms.Label();
            this.labelSeats = new System.Windows.Forms.Label();
            this.labelClient = new System.Windows.Forms.Label();
            this.labelDate = new System.Windows.Forms.Label();
            this.btnSearch = new System.Windows.Forms.Button();
            this.textBoxDestination = new System.Windows.Forms.TextBox();
            this.textBoxDate = new System.Windows.Forms.TextBox();
            this.btnLogout = new System.Windows.Forms.Button();
            this.btnBooking = new System.Windows.Forms.Button();
            this.textBoxClient = new System.Windows.Forms.TextBox();
            this.textBoxSeats = new System.Windows.Forms.TextBox();
            this.textBoxDateLabel = new System.Windows.Forms.TextBox();
            this.textBoxDestinationLabel = new System.Windows.Forms.TextBox();
            this.gridCurse = new System.Windows.Forms.DataGridView();
            this.gridRezervari = new System.Windows.Forms.DataGridView();
            ((System.ComponentModel.ISupportInitialize)(this.gridCurse)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.gridRezervari)).BeginInit();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.ForeColor = System.Drawing.Color.DarkCyan;
            this.label1.Location = new System.Drawing.Point(341, 18);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(102, 24);
            this.label1.TabIndex = 0;
            this.label1.Text = "Destination";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.ForeColor = System.Drawing.Color.DarkCyan;
            this.label2.Location = new System.Drawing.Point(341, 64);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(48, 24);
            this.label2.TabIndex = 1;
            this.label2.Text = "Date";
            // 
            // labelDestination
            // 
            this.labelDestination.AutoSize = true;
            this.labelDestination.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelDestination.ForeColor = System.Drawing.SystemColors.ControlDarkDark;
            this.labelDestination.Location = new System.Drawing.Point(341, 150);
            this.labelDestination.Name = "labelDestination";
            this.labelDestination.Size = new System.Drawing.Size(102, 24);
            this.labelDestination.TabIndex = 2;
            this.labelDestination.Text = "Destination";
            // 
            // labelSeats
            // 
            this.labelSeats.AutoSize = true;
            this.labelSeats.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelSeats.ForeColor = System.Drawing.SystemColors.ControlDarkDark;
            this.labelSeats.Location = new System.Drawing.Point(341, 289);
            this.labelSeats.Name = "labelSeats";
            this.labelSeats.Size = new System.Drawing.Size(123, 24);
            this.labelSeats.TabIndex = 3;
            this.labelSeats.Text = "Seats to book";
            // 
            // labelClient
            // 
            this.labelClient.AutoSize = true;
            this.labelClient.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelClient.ForeColor = System.Drawing.SystemColors.ControlDarkDark;
            this.labelClient.Location = new System.Drawing.Point(341, 243);
            this.labelClient.Name = "labelClient";
            this.labelClient.Size = new System.Drawing.Size(113, 24);
            this.labelClient.TabIndex = 4;
            this.labelClient.Text = "Client Name";
            // 
            // labelDate
            // 
            this.labelDate.AutoSize = true;
            this.labelDate.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelDate.ForeColor = System.Drawing.SystemColors.ControlDarkDark;
            this.labelDate.Location = new System.Drawing.Point(341, 200);
            this.labelDate.Name = "labelDate";
            this.labelDate.Size = new System.Drawing.Size(48, 24);
            this.labelDate.TabIndex = 5;
            this.labelDate.Text = "Date";
            // 
            // btnSearch
            // 
            this.btnSearch.BackColor = System.Drawing.Color.DarkSeaGreen;
            this.btnSearch.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.btnSearch.FlatAppearance.BorderColor = System.Drawing.Color.Red;
            this.btnSearch.FlatAppearance.BorderSize = 0;
            this.btnSearch.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnSearch.ForeColor = System.Drawing.Color.DarkCyan;
            this.btnSearch.Location = new System.Drawing.Point(645, 18);
            this.btnSearch.Name = "btnSearch";
            this.btnSearch.Size = new System.Drawing.Size(143, 75);
            this.btnSearch.TabIndex = 6;
            this.btnSearch.Text = "Search";
            this.btnSearch.UseVisualStyleBackColor = false;
            this.btnSearch.Click += new System.EventHandler(this.btnSearch_Click);
            // 
            // textBoxDestination
            // 
            this.textBoxDestination.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxDestination.Location = new System.Drawing.Point(471, 16);
            this.textBoxDestination.Name = "textBoxDestination";
            this.textBoxDestination.Size = new System.Drawing.Size(141, 29);
            this.textBoxDestination.TabIndex = 7;
            // 
            // textBoxDate
            // 
            this.textBoxDate.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxDate.Location = new System.Drawing.Point(471, 64);
            this.textBoxDate.Name = "textBoxDate";
            this.textBoxDate.Size = new System.Drawing.Size(141, 29);
            this.textBoxDate.TabIndex = 8;
            // 
            // btnLogout
            // 
            this.btnLogout.BackColor = System.Drawing.Color.DarkSeaGreen;
            this.btnLogout.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.btnLogout.FlatAppearance.BorderColor = System.Drawing.Color.Red;
            this.btnLogout.FlatAppearance.BorderSize = 0;
            this.btnLogout.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnLogout.ForeColor = System.Drawing.Color.DarkCyan;
            this.btnLogout.Location = new System.Drawing.Point(665, 420);
            this.btnLogout.Name = "btnLogout";
            this.btnLogout.Size = new System.Drawing.Size(123, 30);
            this.btnLogout.TabIndex = 9;
            this.btnLogout.Text = "Logout";
            this.btnLogout.UseVisualStyleBackColor = false;
            this.btnLogout.Click += new System.EventHandler(this.btnLogout_Click);
            // 
            // btnBooking
            // 
            this.btnBooking.BackColor = System.Drawing.Color.DarkSeaGreen;
            this.btnBooking.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.btnBooking.FlatAppearance.BorderColor = System.Drawing.Color.Red;
            this.btnBooking.FlatAppearance.BorderSize = 0;
            this.btnBooking.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnBooking.ForeColor = System.Drawing.Color.DarkCyan;
            this.btnBooking.Location = new System.Drawing.Point(345, 348);
            this.btnBooking.Name = "btnBooking";
            this.btnBooking.Size = new System.Drawing.Size(267, 50);
            this.btnBooking.TabIndex = 10;
            this.btnBooking.Text = "Make a booking";
            this.btnBooking.UseVisualStyleBackColor = false;
            this.btnBooking.Click += new System.EventHandler(this.btnBooking_Click);
            // 
            // textBoxClient
            // 
            this.textBoxClient.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxClient.Location = new System.Drawing.Point(471, 243);
            this.textBoxClient.Name = "textBoxClient";
            this.textBoxClient.Size = new System.Drawing.Size(141, 29);
            this.textBoxClient.TabIndex = 11;
            // 
            // textBoxSeats
            // 
            this.textBoxSeats.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxSeats.Location = new System.Drawing.Point(471, 289);
            this.textBoxSeats.Name = "textBoxSeats";
            this.textBoxSeats.Size = new System.Drawing.Size(141, 29);
            this.textBoxSeats.TabIndex = 12;
            // 
            // textBoxDateLabel
            // 
            this.textBoxDateLabel.BackColor = System.Drawing.SystemColors.Window;
            this.textBoxDateLabel.Font = new System.Drawing.Font("Arial", 14.25F, ((System.Drawing.FontStyle)((System.Drawing.FontStyle.Bold | System.Drawing.FontStyle.Italic))), System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxDateLabel.ForeColor = System.Drawing.Color.DarkCyan;
            this.textBoxDateLabel.Location = new System.Drawing.Point(471, 198);
            this.textBoxDateLabel.Name = "textBoxDateLabel";
            this.textBoxDateLabel.ReadOnly = true;
            this.textBoxDateLabel.Size = new System.Drawing.Size(141, 29);
            this.textBoxDateLabel.TabIndex = 13;
            // 
            // textBoxDestinationLabel
            // 
            this.textBoxDestinationLabel.BackColor = System.Drawing.SystemColors.Window;
            this.textBoxDestinationLabel.Font = new System.Drawing.Font("Arial", 14.25F, ((System.Drawing.FontStyle)((System.Drawing.FontStyle.Bold | System.Drawing.FontStyle.Italic))), System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textBoxDestinationLabel.ForeColor = System.Drawing.Color.DarkCyan;
            this.textBoxDestinationLabel.Location = new System.Drawing.Point(471, 150);
            this.textBoxDestinationLabel.Name = "textBoxDestinationLabel";
            this.textBoxDestinationLabel.ReadOnly = true;
            this.textBoxDestinationLabel.Size = new System.Drawing.Size(141, 29);
            this.textBoxDestinationLabel.TabIndex = 14;
            // 
            // gridCurse
            // 
            this.gridCurse.AllowUserToAddRows = false;
            this.gridCurse.AllowUserToDeleteRows = false;
            this.gridCurse.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.gridCurse.Location = new System.Drawing.Point(3, 3);
            this.gridCurse.Name = "gridCurse";
            this.gridCurse.ReadOnly = true;
            this.gridCurse.Size = new System.Drawing.Size(323, 221);
            this.gridCurse.TabIndex = 15;
            // 
            // gridRezervari
            // 
            this.gridRezervari.AllowUserToAddRows = false;
            this.gridRezervari.AllowUserToDeleteRows = false;
            this.gridRezervari.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.gridRezervari.Location = new System.Drawing.Point(3, 243);
            this.gridRezervari.Name = "gridRezervari";
            this.gridRezervari.ReadOnly = true;
            this.gridRezervari.Size = new System.Drawing.Size(323, 195);
            this.gridRezervari.TabIndex = 16;
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Control;
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.gridRezervari);
            this.Controls.Add(this.gridCurse);
            this.Controls.Add(this.textBoxDestinationLabel);
            this.Controls.Add(this.textBoxDateLabel);
            this.Controls.Add(this.textBoxSeats);
            this.Controls.Add(this.textBoxClient);
            this.Controls.Add(this.btnBooking);
            this.Controls.Add(this.btnLogout);
            this.Controls.Add(this.textBoxDate);
            this.Controls.Add(this.textBoxDestination);
            this.Controls.Add(this.btnSearch);
            this.Controls.Add(this.labelDate);
            this.Controls.Add(this.labelClient);
            this.Controls.Add(this.labelSeats);
            this.Controls.Add(this.labelDestination);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Cursor = System.Windows.Forms.Cursors.Default;
            this.Name = "MainForm";
            this.Text = "MainForm";
            this.Load += new System.EventHandler(this.MainForm_Load);
            ((System.ComponentModel.ISupportInitialize)(this.gridCurse)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.gridRezervari)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label labelDestination;
        private System.Windows.Forms.Label labelSeats;
        private System.Windows.Forms.Label labelClient;
        private System.Windows.Forms.Label labelDate;
        private System.Windows.Forms.Button btnSearch;
        private System.Windows.Forms.TextBox textBoxDestination;
        private System.Windows.Forms.TextBox textBoxDate;
        private System.Windows.Forms.Button btnLogout;
        private System.Windows.Forms.Button btnBooking;
        private System.Windows.Forms.TextBox textBoxClient;
        private System.Windows.Forms.TextBox textBoxSeats;
        private System.Windows.Forms.TextBox textBoxDateLabel;
        private System.Windows.Forms.TextBox textBoxDestinationLabel;
        private System.Windows.Forms.DataGridView gridCurse;
        private System.Windows.Forms.DataGridView gridRezervari;
    }
}