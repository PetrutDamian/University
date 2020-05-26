import numpy as np
from sklearn.metrics import accuracy_score
import matplotlib.pyplot as plt


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def sigmoidDerivative(x):
    return x * (1 - x)


def softmax(X):
    exps = np.exp(X)
    return exps / np.sum(exps)


def sigSoftTransfer(x, last):
    if not last:
        return sigmoid(x)
    else:
        return softmax(x)


def crossEntropySoftmaxError(computed, y):  # both need to be column vectors
    return computed - np.reshape(y, (len(y), 1))


def entropyLossForOne(x, y):
    loss = -1 * np.sum(np.log(x) * y)
    return loss


def crossEntropyLoss(x, y):
    lossForEach = [entropyLossForOne(x1, y1) for x1, y1 in zip(x, y)]
    return np.sum(lossForEach) / len(x)


def softMaxInit():
    d = {"transfer": sigSoftTransfer,
         "lastLayerError": crossEntropySoftmaxError,
         "transferDerivative": sigmoidDerivative,
         "lossFunction": crossEntropyLoss
         }
    return d


class MyNeuralNet:  # softmax
    def __init__(self, hidden=[5], epochs=2000, learningRate=0.003, init=softMaxInit, miniBatch=36):
        self.miniBatch = miniBatch
        self.lossOverTime = []  # for plotting
        self.learningRate = learningRate
        self.layers = hidden
        self.epochs = epochs
        self.W = []  # W[k][i][j] =  weight of edge connecting neuron j from layer k to neuron i from layer k+1
        self.deltaW = []
        self.neurons = []
        self.costDvActivations = None  # deltaCost/deltaActivation
        self.labels = None
        d = init()
        self.transfer = d["transfer"]
        self.lastLayerError = d["lastLayerError"]
        self.transferDerivative = d["transferDerivative"]
        self.computeLoss = d["lossFunction"]

    def initialize(self, x, y):
        self.layers.insert(0, len(x[0]))  # input layer
        self.layers.append(len(y[0]))  # output layer
        for i in range(len(self.layers) - 1):  # initialize weights randomly
            self.W.append(np.random.randn(self.layers[i + 1], self.layers[i]))  # W[k] : weights connecting
            self.deltaW.append(np.zeros((self.layers[i + 1], self.layers[i])))  # layer k to layer k+1
        self.costDvActivations = [np.zeros((noNeurons, 1)) for noNeurons in self.layers]

    def feedForward(self, inputs):
        self.neurons = [inputs]
        numberOfLayers = len(self.layers)
        for i in range(numberOfLayers - 1):
            a = np.dot(self.W[i], self.neurons[i])
            o = self.transfer(a, i == (numberOfLayers - 2))
            self.neurons.append(o)

    def layerDependency(self, k, j):
        # compute derivative of activations in layer k+1 with respect to activation of neuron j in layer k
        transferDependency = self.W[k][:, j]
        derivative = self.transferDerivative(self.neurons[k][j])
        activationDependency = np.reshape([derivative
                                           for _ in
                                           range(len(self.neurons[k + 1]))],
                                          (len(self.neurons[k + 1]), 1))
        return transferDependency * activationDependency

    def computeError(self, y):  # compute derivative of cost with respect to each neuron activation
        # compute derivative of cost with respect to last layer activation
        self.costDvActivations[-1] = self.lastLayerError(self.neurons[-1], y)
        for i in range(len(self.layers) - 2, 0, -1):  # compute cost dependency for the rest of the layers
            dependency = [np.sum(self.costDvActivations[i + 1] * self.layerDependency(i, j))
                          for j in range(self.layers[i])]
            columnDependency = np.reshape(dependency, (self.layers[i], 1))
            self.costDvActivations[i] = columnDependency

    def neuronsWeightDependency(self, layer, neuron):  # derivative of each neuron in 'layer' with respect to
        dependentNeuronLayer = self.neurons[layer]  # weight connecting 'neuron' in previous layer
        t1 = np.ones((self.layers[layer], 1)) - dependentNeuronLayer
        neuronValue = [self.neurons[layer - 1][neuron][0]]
        neuronValueColumn = np.reshape(neuronValue * self.layers[layer], (self.layers[layer], 1))
        result = dependentNeuronLayer * t1 * neuronValueColumn
        return result

    def computeDeltaW(self):
        for k in range(len(self.layers) - 1):  # for each layer
            for j in range(self.layers[k]):  # for each neuron in the current layer
                neuron = self.neurons[k][j]
                vectorNeuron = [neuron for _ in range(self.layers[k + 1])]
                deltaCostWeight = self.costDvActivations[k + 1] * vectorNeuron
                for i in range(self.layers[k + 1]):
                    self.deltaW[k][i][j] += deltaCostWeight[i]

    def adjustWeights(self):
        for k in range(len(self.W)):  # W[k] contains weigths of edges between layer k and k+1
            for i in range(len(self.W[k])):  # no of neurons in layer k+1
                for j in range(len(self.W[k][i])):  # no of neurons in layer k
                    self.W[k][i][j] = self.W[k][i][j] - self.learningRate * self.deltaW[k][i][j]

        self.deltaW = []
        for i in range(len(self.layers) - 1):
            self.deltaW.append(np.zeros((self.layers[i + 1], self.layers[i])))

    def propagateBackwards(self, y):
        self.computeError(y)
        self.computeDeltaW()

    def oneHotEncoding(self, y, classes):
        Y = np.zeros((len(y), len(classes)))
        for i in range(len(y)):
            for j in range(len(classes)):
                if y[i] == classes[j]:
                    Y[i][j] = 1
        return Y

    def fit(self, x, y, classLabels):
        self.labels = classLabels
        Y = self.oneHotEncoding(y, classLabels)
        self.initialize(x, Y)
        for i in range(self.epochs):
            if i % 5 == 0:
                print("reached epoch" + str(i))
            permutations = np.random.permutation(len(x))
            x = x[permutations]
            Y = Y[permutations]
            for j in range(0, len(x), self.miniBatch):
                batchX = x[j:j + self.miniBatch]
                batchY = Y[j:j + self.miniBatch]
                self.gradientStep(batchX, batchY)
                self.averageDeltaW(len(batchX))
                self.adjustWeights()

            self.addCurrentLoss(x, Y)
        self.plotting()

    def gradientStep(self, x, Y):
        self.costDvActivations = [np.zeros((noNeurons, 1)) for noNeurons in self.layers]
        for sample in range(len(x)):
            sampleColumnVector = np.reshape(x[sample], (len(x[0]), 1))
            self.feedForward(sampleColumnVector)
            self.propagateBackwards(Y[sample])

    def averageDeltaW(self, noSamples):
        for i in range(len(self.deltaW)):
            for j in range(len(self.deltaW[i])):
                for k in range(len(self.deltaW[i][j])):
                    self.deltaW[i][j][k] /= noSamples

    def addCurrentLoss(self, x, y):
        computedProbabilities = self.predictProbabilities(x)
        loss = self.computeLoss(computedProbabilities, y)
        self.lossOverTime.append(loss)

    def plotting(self):
        x = [i for i in range(self.epochs)]
        y = self.lossOverTime
        plt.plot(x, y)
        plt.xlabel("epoch")
        plt.ylabel("Loss")
        plt.title("Cross entropy loss " + str(y[-1]) + "\n: Eta = " + str(self.learningRate))
        plt.show()

    def predictProbabilities(self, x):
        predicted = []
        for sample in x:
            self.neurons = []
            inputs = np.reshape(sample, (len(x[0]), 1))
            self.feedForward(inputs)
            predicted.append(np.reshape(self.neurons[-1], (1, self.layers[-1])))
        return predicted

    def predict(self, x, index=False):
        predictedProb = self.predictProbabilities(x)
        predictedIndex = [np.argmax(p) for p in predictedProb]
        if index:
            return predictedIndex
        else:
            predictedClasses = [self.labels[i] for i in predictedIndex]
            return predictedClasses


class MyNeuralNetBias:  # sigmoid neural network softmax
    def __init__(self, hidden=[5], epochs=2000, learningRate=0.003, init=softMaxInit, miniBatch=4, decay=0.001):
        self.lossOverTime = []  # for plotting
        self.miniBatch = miniBatch
        self.learningRate = learningRate
        self.decay = decay
        self.layers = hidden
        self.epochs = epochs
        self.W = []  # W[k][i][j] =  weight of edge connecting neuron j from layer k to neuron i from layer k+1
        self.deltaW = []
        self.bias = []
        self.deltaBias = []
        self.neurons = []
        self.learningRateOverTime = []
        self.costDvActivations = None  # deltaCost/deltaActivation
        self.labels = None
        d = init()
        self.transfer = d["transfer"]
        self.lastLayerError = d["lastLayerError"]
        self.transferDerivative = d["transferDerivative"]
        self.computeLoss = d["lossFunction"]

    def initialize(self, x, y):
        self.layers.insert(0, len(x[0]))  # input layer
        self.layers.append(len(y[0]))  # output layer
        for i in range(len(self.layers) - 1):  # initialize weights randomly
            self.W.append(np.random.randn(self.layers[i + 1], self.layers[i]))  # W[k] : weights connecting
            self.deltaW.append(np.zeros((self.layers[i + 1], self.layers[i])))  # layer k to layer k+1
            self.bias.append(np.zeros((self.layers[i + 1], 1)))
            self.deltaBias.append(np.zeros((self.layers[i + 1], 1)))
        self.costDvActivations = [np.zeros((noNeurons, 1)) for noNeurons in self.layers]

    def feedForward(self, inputs):
        self.neurons = [inputs]
        numberOfLayers = len(self.layers)
        for i in range(numberOfLayers - 1):
            a = np.dot(self.W[i], self.neurons[i])
            a = a + self.bias[i]
            o = self.transfer(a, i == (numberOfLayers - 2))
            self.neurons.append(o)

    def layerDependency(self, k, j):
        # compute derivative of activations in layer k+1 with respect to activation of neuron j in layer k
        transferDependency = self.W[k][:, j]
        derivative = self.transferDerivative(self.neurons[k][j])
        activationDependency = np.reshape([derivative
                                           for _ in
                                           range(len(self.neurons[k + 1]))],
                                          (len(self.neurons[k + 1]), 1))
        return transferDependency * activationDependency

    def computeError(self, y):  # compute derivative of cost with respect to each neuron activation
        # compute derivative of cost with respect to last layer activation
        self.costDvActivations[-1] = self.lastLayerError(self.neurons[-1], y)
        for i in range(len(self.layers) - 2, 0, -1):  # compute cost dependency for the rest of the layers
            dependency = [np.sum(self.costDvActivations[i + 1] * self.layerDependency(i, j))
                          for j in range(self.layers[i])]
            columnDependency = np.reshape(dependency, (self.layers[i], 1))
            self.costDvActivations[i] = columnDependency

    def neuronsWeightDependency(self, layer, neuron):  # derivative of each neuron in 'layer' with respect to
        dependentNeuronLayer = self.neurons[layer]  # weight connecting 'neuron' in previous layer
        t1 = np.ones((self.layers[layer], 1)) - dependentNeuronLayer
        neuronValue = [self.neurons[layer - 1][neuron][0]]
        neuronValueColumn = np.reshape(neuronValue * self.layers[layer], (self.layers[layer], 1))
        result = dependentNeuronLayer * t1 * neuronValueColumn
        return result

    def computeDeltaW(self):
        for k in range(len(self.layers) - 1):  # for each layer
            self.deltaBias[k] = self.costDvActivations[k + 1]
            for j in range(self.layers[k]):  # for each neuron in the current layer
                neuron = self.neurons[k][j]
                vectorNeuron = [neuron for _ in range(self.layers[k + 1])]
                deltaCostWeight = self.costDvActivations[k + 1] * vectorNeuron
                for i in range(self.layers[k + 1]):
                    self.deltaW[k][i][j] += deltaCostWeight[i]

    def adjustWeights(self):
        for k in range(len(self.W)):  # W[k] contains weigths of edges between layer k and k+1
            for i in range(len(self.W[k])):  # no of neurons in layer k+1
                for j in range(len(self.W[k][i])):  # no of neurons in layer k
                    self.W[k][i][j] = self.W[k][i][j] - self.learningRate * self.deltaW[k][i][j]
        self.deltaW = []
        for i in range(len(self.layers) - 1):
            self.deltaW.append(np.zeros((self.layers[i + 1], self.layers[i])))

        for k in range(len(self.layers) - 1):
            self.bias[k] -= self.deltaBias[k]
            self.deltaBias[k] = np.zeros((self.layers[k + 1], 1))

    def propagateBackwards(self, y):
        self.computeError(y)
        self.computeDeltaW()

    def oneHotEncoding(self, y, classes):
        Y = np.zeros((len(y), len(classes)))
        for i in range(len(y)):
            for j in range(len(classes)):
                if y[i] == classes[j]:
                    Y[i][j] = 1
        return Y

    def fit(self, x, y, classLabels):
        self.labels = classLabels
        Y = self.oneHotEncoding(y, classLabels)
        self.initialize(x, Y)
        for i in range(self.epochs):
            self.learningRateOverTime.append(self.learningRate)
            self.learningRate = self.learningRate * 1 / (1 + self.decay * i)
            if i % 5 == 0:
                print("reached epoch" + str(i))
            permutations = np.random.permutation(len(x))
            x = x[permutations]
            Y = Y[permutations]
            for j in range(0, len(x), self.miniBatch):
                batchX = x[j:j + self.miniBatch]
                batchY = Y[j:j + self.miniBatch]
                self.gradientStep(batchX, batchY)
                self.averageDeltaW(len(batchX))
                self.adjustWeights()
            self.addCurrentLoss(x, Y)
        self.plotting()

    def gradientStep(self, x, Y):
        self.costDvActivations = [np.zeros((noNeurons, 1)) for noNeurons in self.layers]
        for sample in range(len(x)):
            sampleColumnVector = np.reshape(x[sample], (len(x[0]), 1))
            self.feedForward(sampleColumnVector)
            self.propagateBackwards(Y[sample])

    def averageDeltaW(self, noSamples):
        for i in range(len(self.deltaW)):
            for j in range(len(self.deltaW[i])):
                for k in range(len(self.deltaW[i][j])):
                    self.deltaW[i][j][k] /= noSamples
        for k in range(len(self.deltaBias)):
            for i in range(len(self.deltaBias[k])):
                self.deltaBias[k][i] /= noSamples

    def addCurrentLoss(self, x, y):
        computedProbabilities = self.predictProbabilities(x)
        loss = self.computeLoss(computedProbabilities, y)
        self.lossOverTime.append(loss)

    def plotting(self):
        x = [i for i in range(self.epochs)]
        y = self.lossOverTime
        plt.plot(x, y)
        plt.xlabel("epoch")
        plt.ylabel("Loss")
        plt.title("Cross entropy loss " + str(y[-1]) + "\n: Eta = " + str(self.learningRate))
        plt.show()

        plt.plot(x, self.learningRateOverTime)
        plt.xlabel("epoch")
        plt.ylabel("learning rate")
        plt.title("Learning rate over time, decay:" + str(self.decay)+ "current lr="+str(self.learningRate))
        plt.show()

    def predictProbabilities(self, x):
        predicted = []
        for sample in x:
            self.neurons = []
            inputs = np.reshape(sample, (len(x[0]), 1))
            self.feedForward(inputs)
            predicted.append(np.reshape(self.neurons[-1], (1, self.layers[-1])))
        return predicted

    def predict(self, x, index=False):
        predictedProb = self.predictProbabilities(x)
        predictedIndex = [np.argmax(p) for p in predictedProb]
        if index:
            return predictedIndex
        else:
            predictedClasses = [self.labels[i] for i in predictedIndex]
            return predictedClasses
