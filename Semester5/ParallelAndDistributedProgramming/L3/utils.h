#pragma once
bool checkIfFilesAreEqual(char* file1, char* file2);
void printNumber(int v[], int n);
std::pair<int, short*> readNumberFromFile(char file[100]);
std::pair<int, char*> readNumberFromFile2(char file[100]);
void writeToFile(short v[], int n, char* filename);
void writeNumberToFile(int cifre, char* filename);
void writeToFile2(short v[], int n, char* filename);
void writeToFile3(int v[], int n, char* filename);
int nrCifre(int n);