
#include <iostream>
#include <fstream>
#include <time.h> 
#include <random>
#include <vector>
#include <thread>
#include <chrono>
using namespace std;

using chrono::high_resolution_clock;
using chrono::duration_cast;

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


int  filter(int ma[10004][14], int kernel[5][5], int l, int c,int kernelLines,int kernelCols) {
    int sum = 0;
    int kl = 0, kc = 0;
    for (int i = l - kernelLines / 2;i <= l + kernelLines / 2;i++) {
        for (int j = c - kernelCols / 2; j <= c + kernelCols / 2; j++) {
            sum = sum + ma[i][j] * kernel[kl][kc++];
        }
        kl++;
        kc = 0;
    }
    return sum;
}
int  filter2(int** ma, int kernel[5][5], int l, int c, int kernelLines, int kernelCols) {
    int sum = 0;
    int kl = 0, kc = 0;
    for (int i = l - kernelLines / 2;i <= l + kernelLines / 2;i++) {
        for (int j = c - kernelCols / 2; j <= c + kernelCols / 2; j++) {
            sum = sum + ma[i][j] * kernel[kl][kc++];
        }
        kl++;
        kc = 0;
    }
    return sum;
}
void conv(int lStart, int cStart, int lEnd, int cEnd, int ma[10004][14], int kernel[5][5], int ma2[10000][10],int kernelSize,int m) {
    int j = cStart;
    for (int i = lStart;i <= lEnd;i++) {
        while (j < m) {
            if (i == lEnd && j == cEnd)
                break;
            ma2[i][j] = filter(ma, kernel, i + kernelSize / 2, j + kernelSize / 2, kernelSize, kernelSize);
            j++;
        }
        j = 0;
    }
    int z = 3;
}
void conv2(int lStart, int cStart, int lEnd, int cEnd, int** ma, int kernel[5][5], int** ma2, int kernelSize, int m) {
    int j = cStart;
    for (int i = lStart;i <= lEnd;i++) {
        while (j < m) {
            if (i == lEnd && j == cEnd)
                break;
            ma2[i][j] = filter2(ma, kernel, i + kernelSize / 2, j + kernelSize / 2, kernelSize, kernelSize);
            j++;
        }
        j = 0;
    }
    int z = 3;
}
void writeToFile(char* filename,int** ma,int n,int m) {
    ofstream file(filename);
    for (int i = 0;i < n;i++)
    {
        for (int j = 0;j < m;j++)
            file << ma[i][j] << " ";
        file << "\n";
    }
    file.close();
}
void readFile(char* filename, int n, int m, int l, int c, int ma[10004][14]) {
    int borderLines = l - 1;
    int borderColumns = c - 1;
    fstream file;
    file.open(filename);
    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
        {
            int nr;
            file >> nr;
            ma[i + l / 2][j + c / 2] = nr;
        }
}

int main()
{
    int p=16;
    int n = 10000, m = 10, k = 5, l = 5;
    int ma[10004][14] = { 0 };
    int ma2[10000][10] = { 0 };
    int kernel[5][5] = { {101,124,-199,555,123},{422,111,565,124,923},
        {199,-888,-230,-556,-892},{129,-220,-300,-500,400},{101,999,234,210,-930} };
    char s[100] = "date.txt";
    //generateRandomFile(s, n * m, 0, 1000);
   //---------------------------------------------------ALOCARE STATICA (Secvential)-----------------------------------------------------
    readFile(s, n, m, k, l, ma);
    /*
    *     for (int i = 0;i < n + k - 1;i++)
    {
        for (int j = 0;j < m + l - 1;j++)
            cout << ma[i][j] << " ";
        cout << endl;
    }
    cout << endl;
    */
    auto timestart = high_resolution_clock::now();

    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++) {
            ma2[i][j] = filter(ma, kernel, i + k / 2, j + l / 2,k,l);
        }

    auto timeend = high_resolution_clock::now();
    cout << "Secvential(static): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    int** aux = new int* [n];
    for (int i = 0;i <n;i++)
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
    int** ma3 = new int* [n + k - 1];
    for (int i = 0;i < n + k - 1;i++)
        ma3[i] = new int[m + l - 1];
    
    for (int i = 0;i < n + k - 1;i++)
        for (int j = 0;j < m + l - 1;j++)
            ma3[i][j] = ma[i][j];

    int** ma4 = new int* [n];
    for (int i = 0;i < n;i++)
        ma4[i] = new int[m];

   timestart = high_resolution_clock::now();

    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++) {
            ma4[i][j] = filter2(ma3, kernel, i + k / 2, j + l / 2, k,l);
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

    int ma2p[10000][10] = { 0 };

    timestart = high_resolution_clock::now();

    for (int i = 0;i < p;i++) {
        int toProcess = (remainder--) > 0 ? chunk + 1 : chunk;
        lEnd += toProcess / m;
        cEnd += toProcess % m;
        if (cEnd >= m)
            cEnd = cEnd % m, lEnd++;
        threads[i] = thread(conv,lStart,cStart,lEnd,cEnd,ma,kernel,ma2p,5,m);
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
    for (int i = 0;i < n;i++)
        for (int j = 0;j < m;j++)
            ma4[i][j] = 0;

    lStart = 0, lEnd = 0, cStart = 0, cEnd = 0;
    remainder = elements % p;

    timestart = high_resolution_clock::now();
    for (int i = 0;i < p;i++) {
        int toProcess = (remainder--) > 0 ? chunk + 1 : chunk;
        lEnd += toProcess / m;
        cEnd += toProcess % m;
        if (cEnd >= m)
            cEnd = cEnd % m, lEnd++;
        threads[i] = thread(conv2, lStart, cStart, lEnd, cEnd, ma3, kernel, ma4, 5, m);
        cStart = cEnd;
        lStart = lEnd;
    }

    for (int i = 0;i < p;i++) {
        threads[i].join();
    }
    timeend = high_resolution_clock::now();
    cout << "Paralel(dinamic): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    char name4[100] = "paralelDinamic.txt";

    writeToFile(name4, ma4, n, m);
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
