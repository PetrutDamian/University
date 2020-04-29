using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using services;
using Domain;
namespace Client
{
    public partial class LoginForm : Form
    {
        private ClientCtrl ctrl;
        public LoginForm(ClientCtrl ctrl)
        {
            
            InitializeComponent();
            this.ctrl = ctrl;
        }

        private void LoginForm_Load(object sender, EventArgs e)
        {
        }


        private void loginBtnClick(object sender, EventArgs e)
        {
            string username = textBoxUsername.Text;
            string password = textBoxPassword.Text;
            try
            {
                ctrl.login(username, password);
                MainForm window = new MainForm(ctrl);
                window.Text = "User: " + username;
                window.Show();
                this.Hide();
            }catch(Exception ex)
            {
                MessageBox.Show(this, "Login Error " + ex.Message/*+ex.StackTrace*/, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
        }
    }
}
