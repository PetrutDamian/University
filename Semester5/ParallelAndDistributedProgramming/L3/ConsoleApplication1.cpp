// ConsoleApplication1.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include <utility> 
#include <iostream>
#include <fstream>
#include <chrono>
#include <mpi.h>
#include <thread>
#include "utils.h";

using namespace std;
using chrono::high_resolution_clock;
using chrono::duration_cast;

int main(int argc, char** argv)
{
    char s_nr1[100] = "Numar1.txt";
    char s_nr2[100] = "Numar2.txt";
    auto start = high_resolution_clock::now();
    auto end = high_resolution_clock::now();
    int rank, cores, max1;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &cores);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    short* a = new short[1];
    short* b = new short[1];
    short* c = new short[1];
    int chunk;
    int carry = 0;

    if (rank == 0) {
       
        //writeNumberToFile(100, s_nr1);
        //writeNumberToFile(1000000, s_nr2);
        pair<int, short*> nr1 = readNumberFromFile(s_nr1);
        pair<int, short*> nr2 = readNumberFromFile(s_nr2);

        writeToFile2(nr1.second, nr1.first, new char[] {"Numar1V2.txt"});
        writeToFile2(nr2.second, nr2.first, new char[] {"Numar2V2.txt"});


        max1 = max(nr1.first, nr2.first) + 1;
        if (max1 % cores != 0)
            max1 += (cores - max1 % cores);
        short* result = new short[max1];

        //----------------------------------------------- SECVENTIAL START----------------------------------------------------------
        start = high_resolution_clock::now();
        for (int i = 0;i < max1;i++) {
            int sum = 0;
            if (i < nr1.first && i < nr2.first)
                sum = nr1.second[i] + nr2.second[i] + carry;
            else {
                if (i >= nr1.first && i >= nr2.first)
                    sum = carry;
                else
                    sum = carry + (i >= nr1.first ? nr2.second[i] : nr1.second[i]);
            }
            carry = sum / 10;
            result[i] = sum % 10;
        }
        end = high_resolution_clock::now();
        char s_nr3a[100] = "Numar3A.txt";
        writeToFile(result, max1, s_nr3a);
        cout << "Secvential: " << duration_cast<chrono::milliseconds>(end - start).count() << endl;
        //----------------------------------------------- SECVENTIAL END----------------------------------------------------------
        //prepare numbers
        a = new short[max1];
        b = new short[max1];
        c = new short[max1];
        for (int i = 0;i < max1;i++) {
            a[i] = i < nr1.first ? nr1.second[i] : 0;
            b[i] = i < nr2.first ? nr2.second[i] : 0;
        }
        chunk = max1 / cores;
        start = high_resolution_clock::now();
        for (int i = 1;i < cores;i++)
            MPI_Send(&chunk, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
    }
    else
        MPI_Recv(&chunk, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    //----------------------------------------------- PARALEL SCATTER START----------------------------------------------------------

    short* auxA = new short[chunk];
    short* auxB = new short[chunk];
    short* auxC = new short[chunk];

    MPI_Scatter(a, chunk, MPI_SHORT, auxA, chunk, MPI_SHORT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, chunk, MPI_SHORT, auxB, chunk, MPI_SHORT, 0, MPI_COMM_WORLD);

    carry = 0;
    for (int i = 0;i < chunk;i++) {
        int sum = auxA[i] + auxB[i] + carry;
        auxC[i] = sum % 10;
        carry = sum / 10;
    }

    int oldCarry = 0;

    if (rank > 0) {
        if (carry > 0) {
            MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
        }

        MPI_Recv(&oldCarry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        for (int i = 0;i < chunk && oldCarry>0;i++) {
            int sum = auxC[i] + oldCarry;
            auxC[i] = sum % 10;
            oldCarry = sum / 10;
        }
        if (oldCarry > 0) {
            MPI_Send(&oldCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
        }
        else if (carry == 0 && rank < cores - 1) {
            MPI_Send(&oldCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
        }
    }
    else {
        MPI_Send(&carry, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
    }

    MPI_Gather(auxC, chunk, MPI_SHORT, c, chunk, MPI_SHORT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        end = high_resolution_clock::now();
        char s_nr3B[100] = "Numar3B.txt";
        char s_nr3A[100] = "Numar3A.txt";
        writeToFile(c, chunk * cores, s_nr3B);
        cout << "Paralel Scatter: " << duration_cast<chrono::milliseconds>(end - start).count() << endl;
        cout << "Checking if Numar3A.txt and Numar3B.txt are equal..." << endl;
        if (checkIfFilesAreEqual(s_nr3A, s_nr3A))
            cout << "True" << endl;
        else
            cout << "False" << endl;
    }
    //----------------------------------------------- PARALEL SCATTER END----------------------------------------------------------

    //----------------------------------------------- PARALEL ISEND START----------------------------------------------------------
    chunk = 0;
    int nr01, nr02, max2;
    char s_file1[100] = "Numar1V2.txt";
    char s_file2[100] = "Numar2V2.txt";
    ifstream fin1(s_file1);
    ifstream fin2(s_file2);
    fin1 >> nr01;
    fin2 >> nr02;
    max2 = max(nr01, nr02) + 1;
    if (max2 % (cores - 1) != 0)
        max2 += (cores - 1 - max2 % (cores - 1));
    chunk = max2 / (cores - 1);


    if (rank == 0) {
        char c;
        short* nr03 = new short[max2];
        short* send_a;
        short* send_b;
        // PROCESS 0 SENDS CHUNKS 
        MPI_Request request;
        for (int i = 1;i < cores;i++) {
            send_a = new short[chunk];
            for (int j = 0;j < chunk;j++) {
                if (nr01 > 0) {
                    fin1.get(c);
                    if (c != '\n') {
                        nr01--;
                        send_a[j] = c - '0';
                    }
                    else
                        j--;
                }
                else
                    send_a[j] = 0;
            }
            MPI_Isend(send_a, chunk, MPI_SHORT, i, 0, MPI_COMM_WORLD, &request);
            MPI_Request_free(&request);
            send_b = new  short[chunk];
            for (int j = 0;j < chunk;j++) {
                if (nr02 > 0) {
                    fin2.get(c);
                    if (c != '\n') {
                        nr02--;
                        send_b[j] = c - '0';
                    }
                    else
                        j--;
                }
                else
                    send_b[j] = 0;
            }
            MPI_Isend(send_b, chunk, MPI_SHORT, i, 0, MPI_COMM_WORLD, &request);
            MPI_Request_free(&request);
        }
        start = high_resolution_clock::now();
        fin1.close();
        fin2.close();
        for (int i = 1;i < cores;i++)
            MPI_Recv(nr03 + (i - 1) * chunk, max2, MPI_SHORT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        end = high_resolution_clock::now();
        cout << "Paralel ISEND: " << duration_cast<chrono::milliseconds>(end - start).count() << endl;
        char s_nr3C[100] = "Numar3C.txt";
        writeToFile(nr03, max2, s_nr3C);
        cout << "Checking if Numar3B.txt and Numar3C.txt are equal..." << endl;
        if (checkIfFilesAreEqual(new char[] {"Numar3B.txt"}, s_nr3C))
            cout << "True" << endl;
        else
            cout << "False" << endl;
    }
    else {
        fin1.close();
        fin2.close();

        short* recv_a = new short[chunk];
        short* recv_b = new short[chunk];
        short* send_c = new short[chunk];
        int testCarry = 0;
        int previousCarry = 0;
        MPI_Request request_a;
        MPI_Request request_b;
        MPI_Request request_carry;

        MPI_Recv(recv_a, chunk, MPI_SHORT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(recv_b, chunk, MPI_SHORT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        if (rank > 1) {
            MPI_Irecv(&previousCarry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, &request_carry);
            MPI_Test(&request_carry, &testCarry, MPI_STATUS_IGNORE);
        }
        else
            testCarry = 1;

        bool only9 = true;
        int carry = 0;
        if (testCarry) {
            carry = previousCarry;
            previousCarry = 0;
        }
        for (int i = 0;i < chunk;i++) {
            int sum = recv_a[i] + recv_b[i] + carry;
            send_c[i] = sum % 10;
            carry = sum / 10;
            if (send_c[i] != 9)
                only9 = false;
        }

        if (rank < cores - 1) {
            MPI_Request request_send_carry;
            if (testCarry) {
                MPI_Isend(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD, &request_send_carry);
                MPI_Request_free(&request_send_carry);
            }
            else {
                if (carry) {
                    MPI_Isend(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD, &request_send_carry);
                    MPI_Request_free(&request_send_carry);
                    MPI_Wait(&request_carry, MPI_STATUS_IGNORE);
                }
                else {
                    if (!only9) {
                        MPI_Isend(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD, &request_send_carry);
                        MPI_Request_free(&request_send_carry);
                        MPI_Wait(&request_carry, MPI_STATUS_IGNORE);
                    }
                    else {
                        MPI_Wait(&request_carry, MPI_STATUS_IGNORE);
                        MPI_Isend(&previousCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD, &request_send_carry);
                        MPI_Request_free(&request_send_carry);
                    }
                }
            }
        }
        else 
            if (!testCarry) 
                MPI_Wait(&request_carry, MPI_STATUS_IGNORE);

        for (int i = 0;i < chunk && previousCarry>0;i++) {
            int sum = send_c[i] + previousCarry;
            send_c[i] = sum % 10;
            previousCarry = sum / 10;
        }
        MPI_Send(send_c, chunk, MPI_SHORT, 0, 0, MPI_COMM_WORLD);
    }
    //----------------------------------------------- PARALEL ISEND END----------------------------------------------------------
    int* aux_A;
    int* aux_B;
    int* aux_C;
    int max3, size;
    int* vecA = new int[0];
    int* vecB= new int[0];
    int* vecC =new int[0];
    if (rank > 0) {
        int n1, n2;
        ifstream fin1(s_nr1);
        ifstream fin2(s_nr2);
        fin1 >> n1;
        fin2 >> n2;
        fin1.close();
        fin1.close();
        max3 = max(n1, n2);
        size = max3 / 9 + (max3 % 9 == 0 ? 0 : 1);
    }
    else {
        auto a = readNumberFromFile2(s_nr1);
        auto b = readNumberFromFile2(s_nr2);

        max3 = max(a.first, b.first);
        size = max3 / 9 + (max3 % 9 == 0 ? 0 : 1);
        int sizeA = a.first / 9 + (a.first % 9 == 0 ? 0 : 1);
        int sizeB = b.first / 9 + (b.first % 9 == 0 ? 0 : 1);

        vecA = new int[size];
        vecB = new int[size];
        vecC = new int[size];
        int indexA = (sizeA - sizeB < 0 ? sizeA - sizeB : 0);
        int indexB = (sizeB - sizeA < 0 ? sizeB - sizeA : 0);
        int indexA2 = 0;
        int indexB2 = 0;
        start = high_resolution_clock::now();
        for (int i = 0;i < size;i++) {
            int nr = 0;
            if (indexA < 0)
                indexA++;
            else {
                for (int i = 1;i <= 9 && indexA2<a.first;i++) 
                    nr = nr * 10 + (a.second[indexA2++] - (int)'0');
            }
            vecA[i] = nr;
        }
        for (int i = 0;i < size;i++) {
            int nr = 0;
            if (indexB < 0)
                indexB++;
            else {
                for (int i = 1;i <= 9 && indexB2 < b.first;i++)
                    nr = nr * 10 + (b.second[indexB2++] - (int)'0');
            }
            vecB[i] = nr;
        }   
    }

    int begin = 0, finish = 0;
    int rest = size % cores;
    chunk = size / cores;
    int* disp = new int[cores];
    int* chunksizes  = new int[cores];
    for (int i = 0;i < cores;i++) {
        finish += chunk;
        if (rest-- > 0)
            finish++;
        disp[i] = begin;
        chunksizes[i] = finish - begin;
        begin = finish;
    }

    aux_A = new int[chunksizes[rank]];
    aux_B = new int[chunksizes[rank]];
    aux_C = new int[chunksizes[rank]];
    MPI_Scatterv(vecA, chunksizes, disp, MPI_INT, aux_A, chunksizes[rank], MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(vecB, chunksizes, disp, MPI_INT, aux_B, chunksizes[rank], MPI_INT, 0, MPI_COMM_WORLD);


    carry = 0;
    int prevCarry = 0;
    int ultimeleCifre = max3 % 9 == 0 ? 9 : max3 % 9;
    int modulo = 1;
    while (ultimeleCifre > 0)
        modulo *= 10, ultimeleCifre--;
    if (rank == cores - 1) {
        int i = chunksizes[rank] - 1;
        int sum = aux_A[i] + aux_B[i] + carry;
        aux_C[i] = sum % modulo;
        carry = sum / modulo;
        i--;
        for (;i >=0;i--) {
            int sum = aux_A[i] + aux_B[i] + carry;
            aux_C[i] = sum % 1000000000;
            carry = sum / 1000000000;
        }
    }
    else 
        for (int i = chunksizes[rank]-1;i >=0;i--) {
            int sum = aux_A[i] + aux_B[i] + carry;
            aux_C[i] = sum % 1000000000;
            carry = sum / 1000000000;
        }


    if (rank == 0)
        aux_C[chunksizes[rank] - 1] += carry * 1000000000, carry = 0;
    else if (carry) 
        MPI_Send(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD);


    if (rank != cores - 1) 
        MPI_Recv(&prevCarry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);


    for (int i = chunksizes[rank]-1;i >=0 && prevCarry!=0;i--) {
        int sum = aux_A[i] + aux_B[i] + prevCarry;
        aux_C[i] = sum % 1000000000;
        prevCarry = sum / 1000000000;
    }
    if (rank == 0)
        aux_C[chunksizes[rank] - 1] += prevCarry * 1000000000;
    if (rank > 0 && carry == 0) 
        MPI_Send(&prevCarry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD);
    MPI_Gatherv(aux_C, chunksizes[rank], MPI_INT, vecC, chunksizes, disp, MPI_INT, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        end = high_resolution_clock::now();
        writeToFile3(vecC, size, new char[] {"Numar3D.txt"});
        cout << "Paralel V3: " << duration_cast<chrono::milliseconds>(end - start).count() << endl;
        cout << "Checking if Numar3C.txt and Numar3D.txt are equal..." << endl;
        if (checkIfFilesAreEqual(new char[] {"Numar3C.txt"}, new char[] {"Numar3D.txt"}))
            cout << "True" << endl;
        else
            cout << "False" << endl;
    }

    MPI_Finalize();

    return 0;
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
