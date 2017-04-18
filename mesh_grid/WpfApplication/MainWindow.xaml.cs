using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Forms;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.IO;

namespace WpfApplication
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private static bool isNewStart=true;

        private static int shopId=0;

        private List<Area> shopes;

        private Area currentArea;

        private Point prePoint;
        public MainWindow()
        {
            InitializeComponent();
            this.shopes = new List<Area>();
        }

        private void Canvas_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            
            if (isNewStart)
            {
                shopId++;
                this.currentArea = new Area();
                this.currentArea.ShopId = shopId;
                this.shopes.Add(this.currentArea);
                Point currentPoint = e.GetPosition(this.myCanvas);
                this.currentArea.Points.Add(currentPoint);

                var myPoint = new Ellipse();
                
                myPoint.Height = 1;
                myPoint.Width = 1;
                myPoint.Margin = new Thickness(currentPoint.X, currentPoint.Y, 0, 0);
                myPoint.Stroke = new SolidColorBrush(Colors.Red);
                this.myCanvas.Children.Add(myPoint);
                this.prePoint = currentPoint;
                isNewStart = false;
            }
            else
            {
                Point currentPoint = e.GetPosition(this.myCanvas);
                Line line = new Line();
                line.Stroke = Brushes.Red;
                line.X1 = this.prePoint.X;
                line.Y1 = this.prePoint.Y;
                line.X2 = currentPoint.X;
                line.Y2 = currentPoint.Y;
                this.myCanvas.Children.Add(line);
                this.currentArea.Points.Add(currentPoint);
                this.prePoint = currentPoint;
            }

        }

        private void myCanvas_MouseRightButtonUp(object sender, MouseButtonEventArgs e)
        {
            if (!isNewStart)
            {
                Point currentPoint = e.GetPosition(this.myCanvas);
                this.currentArea.Points.Add(currentPoint);
                Line line = new Line();
                line.Stroke = Brushes.Red;
                line.X1 = this.prePoint.X;
                line.Y1 = this.prePoint.Y;
                line.X2 = currentPoint.X;
                line.Y2 = currentPoint.Y;
                this.myCanvas.Children.Add(line);

                this.prePoint = currentPoint;
                currentPoint = this.currentArea.Points[0];
                line = new Line();
                line.Stroke = Brushes.Red;
                line.X1 = this.prePoint.X;
                line.Y1 = this.prePoint.Y;
                line.X2 = currentPoint.X;
                line.Y2 = currentPoint.Y;
                this.myCanvas.Children.Add(line);
                ShopIdForm input = new ShopIdForm();
                if (input.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                {
                    if (input.textBox_ShopId.Text != "")
                    {
                        this.currentArea.ShopId = Int32.Parse(input.textBox_ShopId.Text);
                    }
                    
                }
                //
                //System.Windows.Forms.MessageBox.Show("s", "d");
                isNewStart = true;
            }
            
        }

        private void myCanvas_MouseDown(object sender, MouseButtonEventArgs e)
        {
            //Console.WriteLine("mouse down");
        }

        private void Window_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            //this.myCanvas.Width = this.Width;
            //this.myCanvas.Height = this.Height;
            this.myView.Width = this.Width-20;
            this.myView.Height = this.Height-80;
        }

        private void start_Click(object sender, RoutedEventArgs e)
        {
            double width = this.myCanvas.Width;
            double height = this.myCanvas.Height;
            double radis = Double.Parse(this.radius.Text);
            JArray grid = new JArray();
            for(double y = radis; y < height; y += radis*2)
            {
                JArray row = new JArray();
                grid.Add(row);
                for(double x = radis; x < width; x += radis*2)
                {
                    bool isIn = false;
                    Point checkPoint = new Point(x, y);
                    foreach(var area in this.shopes)
                    {
                        if (Area.IsInPolygon(checkPoint, area.Points))
                        {
                            RectangleGeometry rc = new RectangleGeometry();
                            rc.Rect = new Rect(x - radis, y - radis, radis*2, radis*2);
                            System.Windows.Shapes.Path myPath = new System.Windows.Shapes.Path();
                            myPath.Fill = Brushes.SkyBlue;
                            myPath.Stroke = Brushes.Black;
                            myPath.StrokeThickness = 1;
                            myPath.Data = rc;
                            //rc.Fill = Brushes.SkyBlue;
                            this.myCanvas.Children.Add(myPath);
                            isIn = true;
                            row.Add(area.ShopId);
                            break;
                        }
                    }
                    if (!isIn)
                    {
                        row.Add(0);
                    }
                }
            }
            Console.WriteLine("grid:"+grid.ToString(Formatting.None));
            SaveFileDialog sfd = new SaveFileDialog();
            if (sfd.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                String fileName = sfd.FileName;
                SaveFile(fileName, grid.ToString(Formatting.None));
            }
        }

        protected void SaveFile(string fileName,String content)

        {

            try

            {

                //Stream stream = File.OpenWrite(fileName);
                FileStream fstream = new FileStream(fileName, FileMode.OpenOrCreate, FileAccess.Write);

                using (StreamWriter writer = new StreamWriter(fstream))

                {

                    writer.Write(content);

                }
            }

            catch (IOException ex)

            {
                Console.WriteLine(ex.Message);

            }
            
        }
        /// <summary>
        /// 点击加载楼层背景图
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void floorLayer_Click(object sender, RoutedEventArgs e)
        {
            
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.InitialDirectory = "c://";
            openFileDialog.Filter = "图片文件|*.png*";
            if (openFileDialog.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                string fName = openFileDialog.FileName;
                Image image = new Image();
                image.Source = new BitmapImage(new Uri(fName));
                this.myCanvas.Width = image.Source.Width;
                this.myCanvas.Height = image.Source.Height+40;
                this.myCanvas.Background = new ImageBrush
                {
                    ///ImageSource = new BitmapImage(new Uri("pack://application:"+fName))
                    ImageSource = image.Source
                    
                };
                
            }
        }

        private void myCanvas_MouseEnter(object sender, System.Windows.Input.MouseEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Cross;
        }

        private void myCanvas_MouseLeave(object sender, System.Windows.Input.MouseEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Arrow;
        }
    }
}
