#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <fstream>
#include <time.h> 
using namespace std;


__global__ void blur(int* flat, int* result, int lines, int cols, int channels, int scale) {
    int tid = blockIdx.x * blockDim.x + threadIdx.x;
    if (tid < lines * cols * channels) {
        int line = tid / (cols * channels);
        int rest = tid % (cols * channels);
        int col = rest / channels;
        int channel = rest % channels;

        int sum = 0;
        int nr = 0;
        for (int i = 0 - scale / 2;i <= scale / 2;i++)
            for (int j = 0 - scale / 2;j <= scale / 2;j++) {
                int line2 = line + i;
                int col2 = col + j;
                if (line2 >= 0 && line2 < lines && col2 >= 0 && col2 < cols) { // check if pixel is outside of frame
                    nr++;
                    int index = line2 * cols * channels + col2 * channels + channel;
                    sum += flat[index];
                }

            }
        result[tid] = sum / nr;
    }
}

void blurSecvential(int* flat, int* result, int lines, int cols, int channels, int scale) {
    for (int i = 0;i < lines * cols * channels;i++) {
        int line = i / (cols * channels);
        int rest = i % (cols * channels);
        int col = rest / channels;
        int channel = rest % channels;

        int sum = 0;
        int nr = 0;
        for (int i = 0 - scale / 2;i <= scale / 2;i++) {
            for (int j = 0 - scale / 2;j <= scale / 2;j++) {
                int line2 = line + i;
                int col2 = col + j;
                if (line2 >= 0 && line2 < lines && col2 >= 0 && col2 < cols) { // check if pixel is outside of frame
                    nr++;
                    int index = line2 * cols * channels + col2 * channels + channel;
                    sum += flat[index];
                }
            }
        }
        result[i] = sum / nr;
    }
}


int* flatten(int*** matrix, int lines, int cols, int channels) {
    int* flat = (int*)malloc(lines * cols * channels * sizeof(int));
    int id = 0;
    for (int i = 0; i < lines; i++) {
        for (int j = 0; j < cols; j++) {
            for (int c = 0; c < channels; c++) {
                flat[id] = matrix[i][j][c];
                id++;
            }
        }
    }
    return flat;
}
int*** unflatten(int* arr, int lines, int cols, int channels) {
    int*** img = (int***)malloc(lines * sizeof(int**));
    int id = 0;

    for (int i = 0; i < lines; i++) {
        img[i] = (int**)malloc(cols * sizeof(int*));

        for (int j = 0; j < cols; j++) {
            img[i][j] = (int*)malloc(channels * sizeof(int));

            for (int c = 0; c < channels; c++) {
                img[i][j][c] = arr[id];
                id++;
            }
        }
    }
    return img;
}

int main()
{
    string name = "cake2";
    string readCmd = "python read.py " + name + ".png";
    string writeCmd = "python write.py " + name + "blur.png";

    int scale = 45;
    const char* command = readCmd.c_str(); 
    system(command);

    //read matrix

    ifstream fin("pixels.txt");
    int lines, cols, channels;
    fin >> lines >> cols >> channels;
    int*** matrix = (int***)malloc(lines * sizeof(int**));
    for (int i = 0; i < lines; i++) {

        matrix[i] = (int**)malloc(cols * sizeof(int*));
        for (int j = 0; j < cols; j++) {
            int* rgb = (int*)malloc(channels * sizeof(int));
            fin >> rgb[0] >> rgb[1] >> rgb[2];
            matrix[i][j] = rgb;
        }
    }
    fin.close();
    int* flat = flatten(matrix, lines, cols, channels);
    int size = lines * cols * channels;
    int* result = (int*)malloc(size * sizeof(int));

    int* deviceMatrix, * deviceResult;

    clock_t begin = clock();
    cudaMalloc(&deviceMatrix, size * sizeof(int));
    cudaMalloc(&deviceResult, size * sizeof(int));
    cudaMemcpy(
        deviceMatrix, flat,
        size * sizeof(int),
        cudaMemcpyHostToDevice
    );
    int num_threads = 1 <<10;
    int num_blocks = (size + num_threads - 1) / num_threads;

    blur <<<num_blocks, num_threads >>> (deviceMatrix, deviceResult, lines, cols, channels, scale);

    cudaMemcpy(
        result, deviceResult,
        size * sizeof(int),
        cudaMemcpyDeviceToHost
    );
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    int*** resultMatrix = unflatten(result, lines, cols, channels);

    ofstream out("pixels2.txt");
    out << lines << " " << cols << " " << channels << "\n";
    for (int i = 0; i < lines; i++) {
        for (int j = 0; j < cols; j++) {
            for (int k = 0; k < channels; k++) {
                out << resultMatrix[i][j][k] << " ";
            }
            out << "\n";
        }
    }
    out.close();

    command = writeCmd.c_str();
    system(command);
    printf("Paralel:  %f seconds", time_spent);

    begin = clock();
    blurSecvential(flat, result, lines, cols, channels, scale);
    end = clock();
    resultMatrix = unflatten(result, lines, cols, channels);

    ofstream fout("pixels2.txt");
    fout << lines << " " << cols << " " << channels << "\n";
    for (int i = 0; i < lines; i++) {
        for (int j = 0; j < cols; j++) {
            for (int k = 0; k < channels; k++) {
                fout << resultMatrix[i][j][k] << " ";
            }
            fout << "\n";
        }
    }
    fout.close();


    writeCmd = "python write.py " + name + "blur_secv.png";
    command = writeCmd.c_str();
    system(command);
    time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Secvential:  %f seconds", time_spent);
    return 0;
}