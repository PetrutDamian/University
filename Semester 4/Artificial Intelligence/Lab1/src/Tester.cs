using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Serv;
namespace LAB1B
{
    class Tester
    {
        private static void test1()
        {
            Debug.Assert(Service1.f1("a b c d e f").Equals("f"));
            Debug.Assert(Service1.f1("").Equals(""));
            Debug.Assert(Service1.f1("f e d c b a").Equals("f"));
            Debug.Assert(Service1.f1("bba dde bbe ").Equals("dde"));
            Debug.Assert(Service1.f1("bBa ade bbE ").Equals("bbE"));
        }
        private static void test2()
        {
            Debug.Assert(Service1.f2(1, 5, 4, 1).Equals(5));
            Debug.Assert(Service1.f2(1, 1, 1, 1).Equals(0));
            Debug.Assert(Service1.f2(-1, -1, 0, -1).Equals(1));
            Debug.Assert(Service1.f2(2, 2, 4, 2).Equals(2));
        }
        private static void test3()
        {
            int[] v1, v2;
            v1 = new int[] { 1, 0, 2, 0, 3 };
            v2 = new int[] { 1, 2, 0, 3, 1 };
            Debug.Assert(Service1.f3(5, v1, v2) == 4);
            v1 = new int[] { 0, 0, 0, 0 };
            v2 = new int[] { 1, -2, 3, 4 };
            Debug.Assert(Service1.f3(4, v1, v2) == 0);

        }
        private static void test9()
        {
            int[][] ma = new int[5][];
            ma[0] = new int[] { 0, 2, 5, 4, 1 };
            ma[1] = new int[] { 4, 8, 2, 3, 7 };
            ma[2] = new int[] { 6, 3, 4, 6, 2 };
            ma[3] = new int[] { 7, 3, 1, 8, 3 };
            ma[4] = new int[] { 1, 5, 7, 9, 4 };
            Debug.Assert(Service1.f9(ma, 5, 5, 1, 1, 3, 3).Equals(38));
        }
        public static void Test()
        {
            test1();
            test2();
            test3();
            test4();
            test5();
            test6();
            test7();
            test8();
            test9();
            test10();
            test11();
        }

        private static void test11()
        {
            int[,] ma = new int[,] {
                {1,1,1,1,0,0,1,1,0,1 },
                {1,0,0,1,1,0,1,1,1,1 },
                {1,0,0,1,1,1,1,1,1,1},
                {1,1,1,1,0,0,1,1,0,1},
                {1,0,0,1,1,0,1,1,0,0 },
                {1,1,0,1,1,0,0,1,0,1 },
                {1,1,1,0,1,0,1,0,0,1 },
                {1,1,1,0,1,1,1,1,1,1 }
            };
            int[,] result = Service1.f11(8, 10, ma);
            int[,] expected = new int[,]
            {
                {1,1,1,1,0,0,1,1,0,1 },
                {1,1,1,1,1,0,1,1,1,1 },
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,0,1},
                {1,1,1,1,1,1,1,1,0,0 },
                {1,1,1,1,1,1,1,1,0,1 },
                {1,1,1,0,1,1,1,0,0,1 },
                {1,1,1,0,1,1,1,1,1,1 }

            };
            for (int i = 0; i <= 7; i++)
                for (int j = 0; j <= 9; j++)
                    Debug.Assert(result[i, j] == expected[i, j]);

        }

        private static void test10()
        {
            int[,] ma = new int[3, 5] { { 0, 0, 0, 1, 1 }, { 0, 1, 1, 1, 1 }, { 0, 0, 1, 1, 1 } };
            Debug.Assert(Service1.f10(ma, 3, 5) == 2);
            ma = new int[2, 2] { { 1, 1 }, { 0, 0 } };
            Debug.Assert(Service1.f10(ma, 2, 2) == 1);
            ma = new int[2, 2] { { 0, 0 }, { 0, 1 } };
            Debug.Assert(Service1.f10(ma, 2, 2) == 2);
        }

        private static void test8()
        {
            Debug.Assert(Service1.f8(1).Aggregate((x, y) => x + " " + y).Equals("1"));
            Debug.Assert(Service1.f8(2).Aggregate((x, y) => x + " " + y).Equals("1 10"));
            Debug.Assert(Service1.f8(3).Aggregate((x, y) => x + " " + y).Equals("1 10 11"));
            Debug.Assert(Service1.f8(4).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100"));
            Debug.Assert(Service1.f8(5).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101"));
            Debug.Assert(Service1.f8(6).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101 110"));
            Debug.Assert(Service1.f8(7).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101 110 111"));
            Debug.Assert(Service1.f8(8).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101 110 111 1000"));
            Debug.Assert(Service1.f8(9).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101 110 111 1000 1001"));
            Debug.Assert(Service1.f8(10).Aggregate((x, y) => x + " " + y).Equals("1 10 11 100 101 110 111 1000 1001 1010"));
        }

        private static void test7()
        {
            int[] v = new int[] { 1, 2, 3, 4, 5 };
            Debug.Assert(Service1.f7(v, 2) == 4);
            Debug.Assert(Service1.f7(v, 1) == 5);
            Debug.Assert(Service1.f7(v, 5) == 1);

            v = new int[] { -1, 1, 4, -4, 2, 6 };
            Debug.Assert(Service1.f7(v, 2) == 4);
            Debug.Assert(Service1.f7(v, 1) == 6);
            Debug.Assert(Service1.f7(v, 6) == -4);
            Debug.Assert(Service1.f7(v, 3) == 2);
            Debug.Assert(Service1.f7(v, 4) == 1);

            v = new int[] { 0, -1 };
            Debug.Assert(Service1.f7(v, 1) == 0 && Service1.f7(v, 2) == -1);
        }

        private static void test6()
        {
            int[] v = new int[] { 2, 8, 7, 2, 2, 5, 2, 3, 1, 2, 2 };
            Debug.Assert(Service1.f6(v) == 2);
            v = new int[] { 1, 2, 3, 3, 2, 1, 1, 1, 1 };
            Debug.Assert(Service1.f6(v) == 1);
            v = new int[] { 2, 2, 3 };
            Debug.Assert(Service1.f6(v) == 2);
            v = new int[] { 0};
            Debug.Assert(Service1.f6(v) == 0);
        }

        private static void test5()
        {
            int[] x = new int[] { 1, 2, 3, 4, 4 };
            Debug.Assert(Service1.f5(x) == 4);
            x = new int[] { 7, 6, 5, 4, 1, 2, 3, 4 };
            Debug.Assert(Service1.f5(x) == 4);
            x = new int[] { 1, 2, 1, 3, 10, 9, 6, 7, 5, 4, 8 };
            Debug.Assert(Service1.f5(x) == 1);
            x = new int[] { 1, 1 };
            Debug.Assert(Service1.f5(x) == 1);
        }


        private static void test4()
        {
            string text = "ana are ana are mere rosii ana";
            List<string> result = Service1.f4(text);
            Debug.Assert(result.Count == 2 && result.Contains("mere") && result.Contains("rosii"));

            text = "a b c";
            result = Service1.f4(text);
            Debug.Assert(result.Count == 3 && result.Contains("a") && result.Contains("b") && result.Contains("c"));

            text = "a b c da d c b a a d da";
            result = Service1.f4(text);
            Debug.Assert(result.Count == 0);
        }
    }
}
