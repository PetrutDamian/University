using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{

    
    delegate int CompareFunction(int a, int b);
    class Heap
    {
        CompareFunction comparator;
        private List<int> all;
        public Heap(CompareFunction fnc)
        {
            comparator = fnc;
            all = new List<int>();
        }
        public int size()
        {
            return all.Count;
        }
        public int peek()
        {
            return all[0];
        }
        private void collapse(int index)
        {
            while (index < all.Count)
            {
                int ci = index * 2 + 1;
                if (ci >= all.Count)
                    break;
                if (ci + 1 < all.Count && comparator(all[ci + 1], all[ci]) == -1)
                    ci++;
                if (comparator(all[index], all[ci]) <= 0)
                    break;
                int aux = all[ci];
                all[ci] = all[index];
                all[index] = aux;
                index = ci;
            }
        }
        private void bubbleUp(int index)
        {
            int pi;
            while (index > 0)
            {
                pi = (index-1)/ 2;
                if (comparator(all[pi], all[index]) <= 0)
                    break;
                int aux = all[pi];
                all[pi] = all[index];
                all[index] = aux;
                index = pi;
            }
        }
        public int pop()
        {
            int aux = all[0];
            all[0] = all[all.Count - 1];
            all.RemoveAt(all.Count - 1);
            collapse(0);
            return aux;
        }
        public void insert(int nr)
        {
            all.Add(nr);
            bubbleUp(all.Count - 1);
        }
    }
    class KQueue
    {
        int k;
        Heap kHeap;
        Heap mainHeap;
        public KQueue(int k)
        {
            this.k = k;
            CompareFunction f1 = (x, y) =>
            {
                if (x < y) return -1;
                if (x == y) return 0;
                return 1;
            };
            CompareFunction f2 = (x, y) =>
            {
                if (x > y) return -1;
                if (x == y) return 0;
                return 1;
            };
            kHeap = new Heap(f1);
            mainHeap = new Heap(f2);
        }
        public void add(int nr)
        {
            if (kHeap.size() < k)
                kHeap.insert(nr);
            else
            {
                if (nr <= kHeap.peek())
                    mainHeap.insert(nr);
                else
                {
                    int old = kHeap.pop();
                    mainHeap.insert(old);
                    kHeap.insert(nr);
                }
            }
        }
        public int get()
        {
            return kHeap.peek();
        }
    }
}
