using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Serv;
namespace LAB1B
{
    class Program
    {
        private static void message()
        {
            Console.WriteLine(">");
        }
        static void Main(string[] args)
        {

            Tester.Test();
            while (true)
            {
                message();
                string cmd = Console.ReadLine();
                int x;
                int.TryParse(cmd, out x);
                switch (x)
                {
                    case 0:
                        return;
                    case 1:
                        f1();
                        break;
                    case 2:
                        f2();
                        break;
                    case 3:
                        f3();
                        break;
                    case 4:
                        f4();
                        break;
                    case 5:
                        f5();
                        break;
                    case 6:
                        f6();
                        break;
                    case 7:
                        f7();
                        break;
                    case 8:
                        f8();
                        break;
                    case 9:
                        f9();
                        break;
                    case 10:
                        f10();
                        break;
                    case 11:
                        f11();
                        break;
                }
            }
        }

        private static void f11()
        {
            int n, m;
            Console.WriteLine("n:");
            int.TryParse(Console.ReadLine(), out n);
            Console.WriteLine("m:");
            int.TryParse(Console.ReadLine(), out m);
            int[,] ma = new int[n, m];
            for (int i = 0; i < n; i++)
            {
                string[] nrs = Console.ReadLine().Split(' ');
                for (int j = 0; j < m; j++)
                    int.TryParse(nrs[j], out ma[i, j]);
            }
            int[,] res = Service1.f11(n, m, ma);
            for(int i=0;i<n;i++)
            {
                for (int j = 0; j < m; j++)
                    Console.Write(res[i, j] + " ");
                Console.WriteLine("");
            }
        }

        private static void f10()
        {
            int n, m;
            Console.WriteLine("n:");
            int.TryParse(Console.ReadLine(), out n);
            Console.WriteLine("m:");
            int.TryParse(Console.ReadLine(), out m);
            int[,] ma = new int[n, m];
         
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    int.TryParse(Console.ReadLine(), out ma[i,j]);
            int x = ma.Length;
            long y = ma.LongLength;
            Console.WriteLine("raspuns: "+ Service1.f10(ma,n,m));
        }

        private static void f8()
        {
            int n;
            Console.WriteLine("n: ");
            int.TryParse(Console.ReadLine(), out n);
            Console.WriteLine("Raspuns : "+ Service1.f8(n).Aggregate((x,y)=>x+ " " +y));
        }

        public static int[] readN()
        {
            int n;
            Console.WriteLine("n :");
            int.TryParse(Console.ReadLine(), out n);
            int[] v = new int[n];
            for (int i = 0; i < n; i++)
                int.TryParse(Console.ReadLine(), out v[i]);
            return v;
        }
        private static void f7()
        {
            int k;
            Console.WriteLine("K : ");
            int.TryParse(Console.ReadLine(), out k);
            Console.WriteLine("Raspus: " + Service1.f7(readN(),k)); 
        }
        

        private static void f6()
        {

            Console.WriteLine("Raspuns : " + Service1.f6(readN()));
        }

        private static void f5()
        {
            Console.WriteLine("Raspuns : " + Service1.f5(readN()));
        }

        private static void f4()
        {
            Console.WriteLine("Text:");
            string text = Console.ReadLine();
            Console.WriteLine("Raspuns: " + Service1.f4(text).Aggregate((x, y) => x + " " + y));
        }

        private static void f3()
        {
            Console.WriteLine("n:");
            int n;
            int.TryParse(Console.ReadLine(), out n);
            int[] v1 = new int[n];
            int[] v2 = new int[n];
            Console.WriteLine("v1:");
            for(int i=0;i<n;i++)
                int.TryParse(Console.ReadLine(), out v1[i]);
            Console.WriteLine("v2:");
            for (int i = 0; i < n; i++)
                int.TryParse(Console.ReadLine(), out v2[i]);
            Console.WriteLine("Raspuns: " + Service1.f3(n,v1,v2));
        }

        private static void f1()
        {
            string text;
            Console.WriteLine("Introduce text:");
            text = Console.ReadLine();
            Console.WriteLine("Raspuns : " + Service1.f1(text));

        }
        private static void f2()
        {
            double p1, p2, q1, q2;
            Console.WriteLine("Punctele : ");
            double.TryParse(Console.ReadLine(), out p1);
            double.TryParse(Console.ReadLine(), out p2);
            double.TryParse(Console.ReadLine(), out q1);
            double.TryParse(Console.ReadLine(), out q2);
            Console.WriteLine("Raspuns : " + Service1.f2(p1, p2, q1, q2));
        }
        private static void f9()
        {
            int lines, cols;
            Console.WriteLine("lines:");
            int.TryParse(Console.ReadLine(), out lines);
            Console.WriteLine("cols:");
            int.TryParse(Console.ReadLine(), out cols);
            int[][] s = new int[lines][];
            for (int i = 0; i < lines; i++)
                s[i] = new int[cols];
            for (int i = 0; i < lines; i++)
                for (int j = 0; j < cols; j++)
                    int.TryParse(Console.ReadLine(), out s[i][j]);
            int p1, p2, q1, q2;
            Console.WriteLine("Punctele : ");
            int.TryParse(Console.ReadLine(), out p1);
            int.TryParse(Console.ReadLine(), out p2);
            int.TryParse(Console.ReadLine(), out q1);
            int.TryParse(Console.ReadLine(), out q2);

            Console.WriteLine("Raspuns : " + Service1.f9(s,lines,cols,p1,p2,q1,q2));
        }
    }
}
