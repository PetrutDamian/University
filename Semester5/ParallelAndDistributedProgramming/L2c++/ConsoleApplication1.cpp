
#include <iostream>
#include <fstream>
#include <time.h> 
#include <random>
#include <vector>
#include <thread>
#include <chrono>
#include <atomic> 
#include <algorithm> 
using namespace std;

using chrono::high_resolution_clock;
using chrono::duration_cast;
atomic<int> barrier;


void generateRandomFile(char* filename, int size, int min, int max) {
    std::ofstream file(filename);
    random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> distr(min, max); // define the range

    for (int i = 1;i <= size;i++)
    {
        int rnd = distr(gen);
        file << rnd << " ";
    }
    file.close();
}

bool checkIfFilesAreEqual(char* file1, char* file2) {
    fstream f1, f2;
    char  c1, c2;
    bool equal = true;
    f1.open(file1, ios::in);
    f2.open(file2, ios::in);

    while (1) {
        c1 = f1.get();
        c2 = f2.get();
        if (c1 != c2) {
            equal = false;
            break;
        }
        if (c1 == EOF || c2 == EOF)
        {
            equal = false;
            if (c1 == EOF && c2 == EOF)
                equal = true;
            break;
        }
    }
    f1.close();
    f2.close();
    return equal;
}

int filter(int ma[1000][1000], int kernel[5][5], int l, int c,int kernelSize,int lines,int cols) {
    int sum = 0;
    int kl = 0, kc = 0;
    for (int i = l - kernelSize / 2;i <= l + kernelSize / 2;i++) {
        for (int j = c - kernelSize / 2; j <= c + kernelSize / 2; j++) {
            if (i >= 0 && i < lines && j >= 0 && j < cols)
                sum = sum + ma[i][j] * kernel[kl][kc];
            kc++;
        }
        kl++;
        kc = 0;
    }
    return sum;
}
int  filter2(int** ma, int kernel[5][5], int l, int c,int lines,int cols,int kernelSize) {
    int sum = 0;
    int kl = 0, kc = 0;
    for (int i = l - kernelSize / 2;i <= l + kernelSize / 2;i++) {
        for (int j = c - kernelSize / 2; j <= c + kernelSize / 2; j++) {
            if (i >= 0 && i < lines && j >= 0 && j < cols)
                sum = sum + ma[i][j] * kernel[kl][kc];
            kc++;
        }
        kl++;
        kc = 0;
    }
    return sum;
}
void conv(int lStart, int cStart, int lEnd, int cEnd, int ma[1000][1000], int kernel[5][5],int n,int m,int kernelSize) {
    int initialLines = (cEnd == 0 ? lEnd - lStart : lEnd - lStart + 1);
    int lines = initialLines + (kernelSize / 2 > n - lStart - initialLines ? n - lStart - initialLines : kernelSize / 2);
    lines = lines + (kernelSize / 2 > lStart ? lStart : kernelSize/2);

    int ma2[1000][1000];
    for (int i = 0;i < lines;i++)
        for (int j = 0;j < m;j++)
            ma2[i][j] = ma[i +(lStart-kernelSize/2 >0?lStart-kernelSize/2:0)][j];
    //bariera
    --barrier;
    int x;
    do {
        x = barrier.load();
    } while (x > 0);


     int j = cStart;
    for (int i = lStart;i <= lEnd;i++, j = 0) 
        for (;j < m;j++) 
        {
            if (i == lEnd && j == cEnd)
                break;
            ma[i][j] = filter(ma2, kernel, i + (kernelSize / 2 > lStart ? lStart : kernelSize/2) - lStart, j,kernelSize,lines,m);
        }
}
void conv2(int lStart, int cStart, int lEnd, int cEnd, int** ma, int kernel[5][5], int n,int m,int kernelSize) {
    int initialLines = (cEnd == 0 ? lEnd - lStart : lEnd - lStart + 1);
    int lines = initialLines+ (kernelSize / 2 > n - lStart - initialLines ? n - lStart - initialLines : kernelSize / 2);
    lines = lines + (kernelSize / 2 > lStart ? lStart : kernelSize/2);

    int** ma2 = new int* [lines];
    for (int i = 0;i < lines;i++)
        ma2[i] = new int[m];
    for (int i = 0;i < lines;i++)
        for (int j = 0;j < m;j++)
        {
            int q = ma[i + (lStart - kernelSize / 2 > 0 ? lStart - kernelSize / 2 : 0)][j];
            ma2[i][j] = ma[i + (lStart - kernelSize / 2 > 0 ? lStart - kernelSize / 2 : 0)][j];
        }
    //bariera
    --barrier;
    int x;
    do {
        x = barrier.load();
    } while (x > 0);


    int j = cStart;
    for (int i = lStart;i <= lEnd;i++, j = 0)
        for (;j < m;j++)
        {
            if (i == lEnd && j == cEnd)
                break;
            ma[i][j] = filter2(ma2, kernel, i + (kernelSize / 2 > lStart ? lStart : kernelSize/2) - lStart, j, lines, m,kernelSize);
        }

    for (int i = 0;i < lines;i++)
        delete[] ma2[i];
    delete[](ma2);
}
void writeToFile(char* filename, int** ma, int n, int m) {
    ofstream file(filename);
    for (int i = 0;i < n;i++)
    {
        for (int j = 0;j < m;j++)
            file << ma[i][j] << " ";
        file << "\n";
    }
    file.close();
}
void readFile(char* filename, int n, int m, int l, int c, int ma[1000][1000]) {
    int borderLines = l - 1;
    int borderColumns = c - 1;
    fstream file;
    file.open(filename);
    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
        {
            int nr;
            file >> nr;
            ma[i][j] = nr;
        }
}

int main()
{
  
    int p = 4;
    barrier = p;
    int n = 1000, m = 1000, k = 5, l = 5;
    int ma[1000][1000] = { 0 };
    int ma2[1000][1000] = { 0 };
    int kernel[5][5] = { {566,233,-214,-425,122},{566,233,-214,-425,122},{566,233,-214,-425,122},{566,233,-214,-425,122},{566,233,-214,-425,122} };
    char s[100] = "date.txt";
    //generateRandomFile(s, n * m, 0, 10000);
   //---------------------------------------------------ALOCARE STATICA (Secvential)-----------------------------------------------------
    readFile(s, n, m, k, l, ma);
    /*
    *         for (int i = 0;i < n;i++)
    {
        for (int j = 0;j < m;j++)
            cout << ma[i][j] << " ";
        cout << endl;
   }
    cout << endl;
    */
 

    auto timestart = high_resolution_clock::now();

    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++) {
            ma2[i][j] = filter(ma, kernel, i, j,k,n,m);
        }

    auto timeend = high_resolution_clock::now();
    cout << "Secvential(static): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    int** aux = new int* [n];
    for (int i = 0;i < n;i++)
        aux[i] = new int[m];

    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
            aux[i][j] = ma2[i][j];
    char name[100] = "secvStatic.txt";
    writeToFile(name, aux, n, m);
    /*
    *     for (int i = 0;i < n;i++)
    {
        for (int j = 0;j < m;j++)
            cout << ma2[i][j] << " ";
        cout << endl;
    }
    */
    //---------------------------------------------------ALOCARE DINAMICA (Secvential)----------------------------------------------------
    int** ma3 = new int* [n];
    for (int i = 0;i < n;i++)
        ma3[i] = new int[m];

    for (int i = 0;i < n;i++)
        for (int j = 0;j <m ;j++)
            ma3[i][j] = ma[i][j];

    int** ma4 = new int* [n];
    for (int i = 0;i < n;i++)
        ma4[i] = new int[m];

    timestart = high_resolution_clock::now();

    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++) {
            ma4[i][j] = filter2(ma3, kernel,i,j,n,m,k);
        }

    timeend = high_resolution_clock::now();
    cout << "Secvential(dinamic): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    char name2[100] = "secvDinamic.txt";
    writeToFile(name2, ma4, n, m);
    cout << "Checking if secvStatic.txt and secvDinamic.txt are equal..." << endl;
    if (checkIfFilesAreEqual(name, name2))
        cout << "true" << endl;
    else
        cout << "false" << endl;


    //---------------------------------------------------ALOCARE STATICA (Paralel)----------------------------------------------------
    vector<thread> threads(p);
    int elements = n * m;
    int chunk = elements / p;
    int remainder = elements % p;

    int lStart = 0, lEnd = 0, cStart = 0, cEnd = 0;

    int ma2p[1000][1000] = { 0 };
    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
            ma2p[i][j] = ma[i][j];


    timestart = high_resolution_clock::now();

    for (int i = 0;i < p;i++) {
        int toProcess = (remainder--) > 0 ? chunk + 1 : chunk;
        lEnd += toProcess / m;
        cEnd += toProcess % m;
        if (cEnd >= m)
            cEnd = cEnd % m, lEnd++;

        threads[i] = thread(conv, lStart, cStart, lEnd, cEnd, ma2p, kernel, n,m,k);
        cStart = cEnd;
        lStart = lEnd;
    }

    for (int i = 0;i < p;i++) {
        threads[i].join();
    }

    timeend = high_resolution_clock::now();
    cout << "Paralel(static): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    char name3[100] = "paralelStatic.txt";
    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
            aux[i][j] = ma2p[i][j];

    writeToFile(name3, aux, n, m);

    cout << "Checking if files secvStatic.txt and paralelStatic.txt are equal..." << endl;
    if (checkIfFilesAreEqual(name, name3))
        cout << "true" << endl;
    else
        cout << "false" << endl;
    ;

    

    //---------------------------------------------------ALOCARE DINAMICA (Paralel)----------------------------------------------------
    vector<thread> threads2(p);
    barrier.store(p);

    lStart = 0, lEnd = 0, cStart = 0, cEnd = 0;
    remainder = elements % p;

    /*
    for (int i = 0;i < n;i++)
    {
        for (int j = 0;j < m;j++)
            cout << ma3[i][j] << " ";
        cout << endl;
    }
    */

    timestart = high_resolution_clock::now();
    for (int i = 0;i < p;i++) {
        int toProcess = (remainder--) > 0 ? chunk + 1 : chunk;
        lEnd += toProcess / m;
        cEnd += toProcess % m;
        if (cEnd >= m)
            cEnd = cEnd % m, lEnd++;
        threads[i] = thread(conv2, lStart, cStart, lEnd, cEnd, ma3, kernel,n,m,k);
        cStart = cEnd;
        lStart = lEnd;
    }

    for (int i = 0;i < p;i++) {
        threads[i].join();
    }
    timeend = high_resolution_clock::now();
    cout << "Paralel(dinamic): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    char name4[100] = "paralelDinamic.txt";

    writeToFile(name4, ma3, n, m);
    cout << "Checking if files paralelDinamic.txt and paralelStatic.txt are equal..." << endl;
    if (checkIfFilesAreEqual(name4, name3))
        cout << "true" << endl;
    else
        cout << "false" << endl;
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
