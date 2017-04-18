using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WpfApplication
{
    public partial class ShopIdForm : Form
    {
        public ShopIdForm()
        {
            InitializeComponent();
            this.button_OK.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.button_cancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        }

        private void ShopIdForm_Load(object sender, EventArgs e)
        {

        }
    }
}
