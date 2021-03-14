
#include <fstream>
#include <time.h> 
#include <random>
using namespace std;

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
