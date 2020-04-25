import numpy as np


class FileParser:
    @staticmethod
    def getInputRegression(filename):
        f = open(filename)
        samples = int(f.readline())
        real = []
        predicted = []

        for i in range(samples):
            line = f.readline()
            args = line.split(' ')
            l = [int(arg) for arg in args]
            real.append(l)

        for i in range(samples):
            line = f.readline()
            args = line.split(' ')
            l = [int(arg) for arg in args]
            predicted.append(l)

        return real, predicted

    @staticmethod
    def getInputStrictClassification(filename):
        f = open(filename)
        samples = int(f.readline())
        real = []
        predicted = []
        labels = f.readline().strip().split(' ')

        line = f.readline().strip()
        real = line.split(' ')

        line = f.readline().strip()
        predicted = line.split(' ')

        return real, predicted, labels

    @staticmethod
    def getInputBinaryClassificationProbabilities(filename):
        f = open(filename)
        samples = int(f.readline())
        predicted = []

        line = f.readline().strip()
        real = [int(arg) for arg in line.split(' ')]

        for i in range(samples):
            args = f.readline().strip().split(' ')
            p1 = float(args[0])
            p0 = float(args[1])
            predicted.append((p1, p0))

        return real, predicted

    @staticmethod
    def getInputMultiClassClasificationProbabilities(filename):
        f = open(filename)
        samples = int(f.readline())
        labels = []
        real = []
        predicted = []

        line = f.readline().strip()
        labels = line.split(' ')
        line = f.readline().strip()
        real = line.split(' ')

        for i in range(samples):
            line = f.readline().strip()
            probs = [float(p) for p in line.split(' ')]
            predicted.append(probs)

        return real, predicted, labels

    @staticmethod
    def getInputMultiLabelProbabilities(filename):
        f = open(filename)
        samples = int(f.readline())
        labels = []
        real = []
        predicted = []

        line = f.readline().strip()
        labels = line.split(' ')
        for i in range(samples):
            line = f.readline().strip()
            l = [int(arg) for arg in line.split(' ')]
            real.append(l)
        for i in range(samples):
            line = f.readline().strip()
            l = [float(arg) for arg in line.split(' ')]
            predicted.append(l)
        return real, predicted, labels