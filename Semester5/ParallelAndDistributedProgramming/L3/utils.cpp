#pragma once
#include <fstream>
#include <iostream>
#include <utility> 
#include <random>
using namespace std;
void writeNumberToFile(int cifre, char* filename) {
    ofstream file(filename);
    file << cifre << "\n";
    for (int i = 0;i < cifre;i++)
    {
        if (i == 0)
            file << rand() % 9 + 1;
        else
            file << rand() % 10;
    }
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


pair<int, short*> readNumberFromFile(char file[100]) {
    ifstream  fin(file);
    int nr;
    fin >> nr;
    int n = nr;
    char c;
    short* vector = new short[nr];
    while (fin.get(c)) {
        if (c != '\n')
            vector[--nr] = c - '0';
    }
    fin.close();
    return make_pair(n, vector);
}
std::pair<int, char*> readNumberFromFile2(char file[100]) {
    ifstream fin(file);
    int nr;
    fin >> nr;
    char c;
    int i = 0;
    char* sir = new char[nr];
    while (fin.get(c)) {
        if (c != '\n')
            sir[i++] = c;
    }
    fin.close();
    return make_pair(nr, sir);
}

void printNumber(int v[], int n) {
    for (int i = n - 1;i >= 0;i--)
        cout << v[i] << " ";
    cout << endl;
}


void writeToFile(short v[], int n, char* filename) {
    ofstream file(filename);
    bool nonzero = false;
    for (int i = n - 1;i >= 0;i--)
    {
        if (v[i] != 0)
            nonzero = true;
        if (nonzero)
            file << v[i];
    }
}
void writeToFile2(short v[], int n, char* filename) {
    ofstream file(filename);
    file << n << endl;
    for (int i = 0;i < n;i++)
        file << v[i];
}

int nrCifre(int n) {
    int nr = 0;
    if (n == 0)
        return 0;
    while (n > 0)
        nr++, n /= 10;
    return nr;
}
void writeToFile3(int v[], int n, char* filename) {
    ofstream file(filename);
    for (int i = 0;i < n;i++) {
        if (i == n-1)
            file << v[i];
        else {
            int cifre = nrCifre(v[i]);
            for (int j = 1;j <= (9 - cifre);j++)
                file << '0';
            file << v[i];
        }
    }
}
