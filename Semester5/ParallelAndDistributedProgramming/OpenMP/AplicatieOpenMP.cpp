#include <iostream>
#include <vector>
#include <random>
#include "omp.h"
#include <chrono>
using namespace std;
using chrono::high_resolution_clock;
using chrono::duration_cast;
void printVector(vector<int> v) {

    for (int i = 0;i < v.size();i++)
        cout << v[i] << " ";
    cout << endl;
}

int main()
{
    int n = 1000000;
    int p = 12;
    int boundary = 5000;

    vector<int> a(n), b(n), c(n),d(n);
    omp_set_num_threads(p);

    for (int i = 0;i < n;i++) {
        a[i] = rand() % boundary;
        b[i] = rand() % boundary;
    }

    auto timestart = high_resolution_clock::now();
    for (int i = 0;i < n;i++)
        c[i] = a[i] + b[i];
    auto timeend = high_resolution_clock::now();
    cout << "Secvential: " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    timestart = high_resolution_clock::now();
    #pragma omp parallel for schedule(static)
    for (int i = 0;i < n;i++)
        d[i] = a[i] + b[i];
    timeend = high_resolution_clock::now();
    cout << "OpenMP ( schedule static): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;


    timestart = high_resolution_clock::now();
    #pragma omp parallel for schedule(dynamic,500)
    for (int i = 0;i < n;i++)
        d[i] = a[i] + b[i];
    timeend = high_resolution_clock::now();
    cout << "OpenMP(schedule dinamic): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    timestart = high_resolution_clock::now();
    #pragma omp parallel for schedule(guided,500)
    for (int i = 0;i < n;i++)
        d[i] = a[i] + b[i];
    timeend = high_resolution_clock::now();
    cout << "OpenMP(schedule guided): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    long long sum2 = 0;
    for (int i = 0;i < n;i++)
        sum2 += a[i];

    cout << sum2 << endl;

    timestart = high_resolution_clock::now();
    long long sum = 0;
    #pragma omp parallel for
    for (int i = 0;i < n;i++)
        #pragma omp critical
        sum += a[i];
    timeend = high_resolution_clock::now();
    cout << "OpenMP(critical): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;
    cout << sum << endl;

    int sum1 = 0;

    timestart = high_resolution_clock::now();
    #pragma omp parallel for private(sum1)
    for (int i = 0;i < n;i++)
        sum1 += a[i];

    timeend = high_resolution_clock::now();
    cout << "OpenMP(reduction): " << duration_cast<chrono::milliseconds>(timeend - timestart).count() << endl;

    cout << sum << endl;


    return 0;
}