using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
namespace Serv
{
    public class Service1
    {
        public static string f1(string text)
        {
           
            string[] args = text.Split(' ');
            string max = "";
            foreach(string str in args)
                if (max.CompareTo(str.ToLower()).Equals(-1))
                    max = str;
            return max;
        }
        public static double f2(double p1,double p2, double q1, double q2)
        {
            double a = p1 - q1;
            double b = p2 - q2;
            return Math.Sqrt(a * a + b * b);
        }

        internal static int f9(int[][] s,int n, int m,int p1,int p2,int q1,int q2)
        {
            int[][] aux = new int[n+1][];
            for (int i = 0; i <= n; i++)
                aux[i] = new int[m+1];
            for(int i = 0; i <= m; i++)
                aux[0][i] = 0;
            for (int i = 0; i <= n; i++)
                aux[i][0] = 0;
            for(int i = 1; i <= n; i++)
            for(int j = 1; j <= m; j++)
                    aux[i][j] = aux[i][j - 1] + aux[i - 1][j] + s[i-1][j-1] - aux[i-1][j-1];
            return aux[q1+1][q2+1] - aux[q1+1][p2 ] - aux[p1 ][q2+1] + aux[p1 ][p2 - 1];
        }

        internal static List<string> f4(string text)
        {
            Dictionary<string, int> d = new Dictionary<string, int>();
            string[] cuvs = text.Split(' ');
            foreach (string cuv in cuvs)
            {
                if (!d.ContainsKey(cuv))
                    d.Add(cuv, 1);
                else
                    d[cuv]++;
            }
            return d.Where(x => x.Value == 1).Select(x => x.Key).ToList();
        }

        internal static  int  f6(int[] v)
        {
            int n = v.Length;
            Dictionary<int, int> d = new Dictionary<int, int>();
            if (v.Length == 1)
                return v[0];
            for(int i=0;i<n;i++)
            {
                if (!d.ContainsKey(v[i]))
                    d.Add(v[i], 1);
                else
                {
                    d[v[i]]++;
                    if (d[v[i]] > n / 2)
                        return v[i];
                }
            }
            return -1;
        }

        internal static int[,] f11(int n, int m, int[,] ma)
        {
            
            Queue<int> q1 = new Queue<int>();
            Queue<int> q2 = new Queue<int>();
            int[,] result = new int[n, m];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    result[i, j] = 1;
            for(int j = 0; j < m; j++)
            {
                if(ma[0,j]==0)
                {
                    q1.Enqueue(0);
                    q2.Enqueue(j);
                }
                if (ma[n - 1, j] == 0)
                {
                    q1.Enqueue(n - 1);
                    q2.Enqueue(j);
                }
            }
            for (int j = 0; j < n; j++)
            {
                if (ma[j, 0] == 0)
                {
                    q1.Enqueue(j);
                    q2.Enqueue(0);
                }
                if (ma[j,m-1] == 0)
                {
                    q1.Enqueue(j);
                    q2.Enqueue(m-1);
                }
            }
            //parcurgere
            while (q1.Count > 0)
            {
                int l = q1.Dequeue();
                int c = q2.Dequeue();
                ma[l, c] = 1;
                result[l, c] = 0;
                if(l>0 && ma[l-1,c]==0)
                {
                    q1.Enqueue(l - 1);
                    q2.Enqueue(c);
                }
                if(l<n-1 && ma[l + 1, c] == 0)
                {
                    q1.Enqueue(l + 1);
                    q2.Enqueue(c);
                }
                if(c>0 && ma[l, c - 1] == 0)
                {
                    q1.Enqueue(l);
                    q2.Enqueue(c - 1);
                }
                if (c < m - 1 && ma[l, c + 1] == 0)
                {
                    q1.Enqueue(l);
                    q2.Enqueue(c + 1);
                }
            }
            return result;
        }

        internal static int f10(int[,] ma,int n,int m)
        {
            int max = -1;
            int index = -1;

            for(int i=0;i<n;i++)
            {
                int st = 0, dr = m-1;
                int index1 = -1;
                if (ma[i, n - 1] == 1)
                    index1 = n - 1;
                while (st < dr)
                {
                    int mij = (st + dr) / 2;
                    


                    if(ma[i,mij]+ma[i,mij+1]==1)
                    {
                        index1 = mij + 1;
                        break;
                    }

                    if (ma[i, mij] == 0)
                        st = mij + 1;
                    else
                        dr = mij - 1;
                }
                int nr1;
                if (st == dr)
                    if (ma[i, st] == 0)
                        index1 = m;
                    else
                        index1 = 0;
                nr1 = m - index1;
                if(nr1>max)
                {
                    max = nr1;
                    index = i;
                }
            }
            return index+1; 
        }

        internal static int f7(int[] v,int k)
        {
            KQueue q = new KQueue(k);
            foreach (int nr in v)
                q.add(nr);
            return q.get();
        }

        public static string[] f8(int n)
        {
            Queue<string> q = new Queue<string>();
            q.Enqueue("1");
            string[] str = new string[n];
            for(int i = 1; i <= n; i++)
            {
                str[i - 1] = q.Dequeue();
                q.Enqueue(str[i - 1] + "0");
                q.Enqueue(str[i - 1] + "1");
            }
            return str;

        }
        internal static int f5(int[] v)
        {
            int n = v.Length;
            int sum = 0;
            sum = v.Aggregate((x, y) => x + y);
            return sum-(n - 1) * n / 2;
        }

        internal static int f3(int n, int[] v1, int[] v2)
        {
            int prod = 0;
            for(int i = 0; i < n; i++)
            {
                if (v1[i] != 0 && v2[i] != 0)
                    prod += v1[i] * v2[i];
            }
            return prod;
        }
    }

}