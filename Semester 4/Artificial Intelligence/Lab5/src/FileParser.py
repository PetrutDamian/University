import os
import numpy
class FileParser:

    @staticmethod
    def parseMatrix(filename):
        currentDir = os.getcwd()
        filePath = os.path.join(currentDir, 'Data', filename)
        f = open(filePath)
        n = int(f.readline())
        matrix = []
        for line in f:
            args = line.split(',')
            l = []
            for arg in args:
                l.append(float(arg))
            matrix.append(l)
        return matrix,n

    @staticmethod
    def parseEuclidian(filename):
        currentDir = os.getcwd()
        filePath = os.path.join(currentDir, 'Data', filename)
        f = open(filePath)
        n = int(f.readline())
        x = []
        y = []
        matrix = [[0] * n for _ in range(n)]
        for line in f:
            args = line.split(" ")
            x.append(float(args[1]))
            y.append(float(args[2]))
        for i in range(0, n):
            pi = numpy.array((x[i], y[i]))
            for j in range(0, n):
                pj = numpy.array((x[j], y[j]))
                dist = numpy.linalg.norm(pi - pj)
                matrix[i][j] = dist

        return matrix, n

