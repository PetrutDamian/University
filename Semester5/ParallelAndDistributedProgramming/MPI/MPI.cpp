

#include <mpi.h>
#include <stdio.h>
#include <random>
#include <iostream>


using namespace std;

void printVector(int v[],int n) {
    for (int i = 0;i < n;i++)
        cout << v[i] << " ";
    cout << endl;
}

int main(int argc, char** argv)
{
    const int n = 10;
    const int range = 10;
    int a[n], b[n], c[n];

    int  namelen, myid, numprocs;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    
    if (myid == 0) 
        for (int i = 0;i < n;i++) {
            a[i] = rand() % range;
            b[i] = rand() % range;
        }
    
   int chunksize = n / numprocs;


   int* chunksizes = new int[numprocs];
   int* disp = new int[numprocs];

   int start = 0, end = 0;
   int size = n / numprocs;
   int rest = n % numprocs;

   for (int i = 0;i < numprocs;i++) {
       end += size;
       if (rest-- > 0)
           end++;
       disp[i] = start;
       chunksizes[i] = end - start;
      // cout << " start: " << start << " " << end << endl;
       start = end;
   }

   int* a_aux = new int[chunksizes[myid]];
   int* b_aux = new int[chunksizes[myid]];
   int* c_aux = new int[chunksizes[myid]];
   MPI_Scatterv(a, chunksizes, disp, MPI_INT, a_aux, chunksizes[myid], MPI_INT, 0, MPI_COMM_WORLD);
   MPI_Scatterv(b, chunksizes, disp, MPI_INT, b_aux, chunksizes[myid], MPI_INT, 0, MPI_COMM_WORLD);


   for (int i = 0;i < chunksizes[myid];i++)
       c_aux[i] = a_aux[i] + b_aux[i];

   MPI_Gatherv(c_aux, chunksizes[myid], MPI_INT, c, chunksizes,disp, MPI_INT,0, MPI_COMM_WORLD);
   
   if (myid == 0) {

       printVector(a, n);
       printVector(b, n);
       printVector(c, n);
   }
    MPI_Finalize();
    return 0;
}


/*

int main(int argc, char** argv)
{
    const int n = 10;
    const int range = 10;
    int a[n], b[n], c[n];

    int  namelen, myid, numprocs;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    int start = 0, end = 0;
    MPI_Status status;
    if (myid == 0) {
        for (int i = 0;i < n;i++) {
            a[i] = rand() % range;
            b[i] = rand() % range;
        }
        int size = n / (numprocs - 1);
        int rest = n % (numprocs - 1);
        for (int i = 1;i < numprocs;i++) {
            end += size;
            if (rest-- > 0)
                end++;
            MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(a + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(b + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
            start = end;
        }

        int from = 0;
        rest = n % (numprocs - 1);
        for (int i = 1;i < numprocs;i++) {
            MPI_Recv(c + from, (rest>0?1+size:size), MPI_INT, i, 0, MPI_COMM_WORLD, &status);
            from = from + size + (rest-- > 0 ? 1 : 0);
        }
        printVector(a, n);
        printVector(b, n);
        printVector(c, n);
    }
    else {
        MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(a+start,end-start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(b+start,end-start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

        for (int i = start; i < end;i++) {
            c[i] = a[i] + b[i];
        }
        MPI_Send(c + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);

        cout << "start: " << start << " end:" << end << " myId:" << myid << endl;
    }



    MPI_Finalize();
    return 0;
}

*/