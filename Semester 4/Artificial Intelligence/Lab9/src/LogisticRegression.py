import copy
from math import exp
import random

from numpy.linalg import inv
import numpy as np
import matplotlib.pyplot as plt
from sklearn.metrics import accuracy_score


def sigmoid(x):
    return 1 / (1 + exp(-x))


class MyInternalLogisticRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []
        self.x = []
        self.y = []
        self.learningRate = 0
        self.noEpochs = 0

    def fit(self, X, Y, learningRate, noEpochs, Batches=5):
        self.noEpochs = noEpochs
        self.x = X
        self.y = Y
        self.learningRate = learningRate
        self.coef_ = [0.0 for _ in range(1 + len(X[0]))]  # beta or w coefficients y = w0 + w1 * x1 + w2 * x2 + ...
        self.logLoss = []

        for nr in range(noEpochs):
            index = [i for i in range(len(X))]
            random.shuffle(index)
            xrand = [X[i] for i in index]
            yrand = [Y[i] for i in index]
            self.x = xrand
            self.y = yrand
            size = len(X) // Batches
            errors = []
            for q in range(Batches):
                for i in range(q * size, (q + 1) * size):  # for each sample in batch
                    ycomputed = sigmoid(self.eval(self.x[i], self.coef_))  # estimate the output
                    errors.append(ycomputed - self.y[i])  # compute the error for the current sample
                for i in range(q * size, (q + 1) * size):
                    for j in range(0, len(self.x[0])):
                        self.coef_[j + 1] = self.coef_[j + 1] - self.learningRate * errors[i] * self.x[i][j]
                    self.coef_[0] = self.coef_[0] - self.learningRate * errors[i] * 1

            computed = self.predict(X)
            logLoss = np.sum([-r * np.log(c) - (1 - r) * np.log(1 - c) for c, r in zip(computed, Y)]) / len(Y)
            self.logLoss.append(logLoss)

    def plot(self, i):
        ep = [i for i in range(self.noEpochs)]
        plt.plot(ep, self.logLoss)
        plt.xlabel("Epoch")
        plt.ylabel("Log loss")
        plt.title("LogisticRegressor class: " + str(i) + " loss: " + str(self.logLoss[-1]))
        plt.show()

    def eval(self, xi, coef):
        yi = coef[0]
        for j in range(len(xi)):
            yi += coef[j + 1] * xi[j]
        return yi

    def predictOneSample(self, sampleFeatures):
        coefficients = [c for c in self.coef_]
        computedFloatValue = self.eval(sampleFeatures, coefficients)
        computed01Value = sigmoid(computedFloatValue)
        return computed01Value

    def predict(self, test):
        return [self.predictOneSample(t) for t in test]


class MyLogisticRegression:
    def __init__(self):
        self.processors = []

    def predict(self, test):
        labels = []
        for sample in test:
            predicted = []
            for i in range(len(self.processors)):
                predicted.append(self.processors[i].predictOneSample(sample))
            labels.append(np.argmax(predicted))
        return labels

    def fit(self, x, y, labels, learningRate=[0.0001, 0.00004, 0.0001], noEpochs=[5000,5000,5000]):
        error = []
        print("all labels: " + str(y))
        for i in range(len(labels)):
            cp = copy.deepcopy(x)
            p = MyInternalLogisticRegression()
            yi = np.where(np.array(y) == i, 1, 0)
            self.processors.append(p)
            p.fit(np.array(cp), yi, learningRate[i], noEpochs[i])
            p.plot(i)
