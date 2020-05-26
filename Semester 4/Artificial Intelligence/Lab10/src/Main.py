import numpy as np
from sklearn import neural_network
import matplotlib.pyplot as plt
from PIL import Image
from src.MyNeuralNetwork import MyNeuralNet, MyNeuralNetBias
from src.NeuralNetwork2 import MyNeuralNetwork2
from src.NeuralNetwork3 import MyNeuralNetwork3

from src.Utils import training, evalMultiClass, classification, normalisation, splitData, loadIrisData, \
    data2FeaturesMoreClasses, loadDigitData, plotConfusionMatrix, showPictures


def classificationEvaluation(real, predicted, labelNames):
    accuracy = sum([1 if real[i] == predicted[i] else 0 for i in range(len(real))]) / len(real)
    TP = {label: 0 for label in labelNames}
    FP = {label: 0 for label in labelNames}
    FN = {label: 0 for label in labelNames}

    for i in range(len(real)):
        if real[i] == predicted[i]:
            TP[real[i]] += 1
        elif real[i] != predicted[i]:
            FP[predicted[i]] += 1
            FN[real[i]] += 1
        precision = [(label, TP[label] / (TP[label] + FP[label])) if TP[label] + FP[label] != 0 else (label, 0) for
                     label in labelNames]
    recall = [(label, TP[label] / (TP[label] + FN[label])) if TP[label] + FN[label] != 0 else (label, 0) for label in
              labelNames]

    return accuracy, precision, recall


def flatten(mat):
    x = []
    for line in mat:
        for el in line:
            x.append(el)
    return x


def solveIrisProblem():
    inputs, outputs, outputNames = loadIrisData()
    trainInputs, trainOutputs, testInputs, testOutputs = splitData(inputs, outputs)

    # plot the training data distribution on classes
    plt.hist(trainOutputs, 3, rwidth=0.8)
    plt.xticks(np.arange(len(outputNames)), outputNames)
    plt.show()

    # plot the data in order to observe the shape of the classifier required in this problem
    data2FeaturesMoreClasses(trainInputs, trainOutputs, outputNames)

    # normalise the data
    trainInputs, testInputs = normalisation(trainInputs, testInputs)

    # non-liniar classifier and softmax approach for multi-class with tool
    classifier = neural_network.MLPClassifier(hidden_layer_sizes=(5,), activation='relu', max_iter=100, solver='sgd',
                                              verbose=10, random_state=1, learning_rate_init=.1)

    training(classifier, trainInputs, trainOutputs)
    predictedLabels = classification(classifier, testInputs)
    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), predictedLabels, outputNames)

    print('acc(tool): ', acc)
    print('precision(tool): ', prec)
    print('recall(tool): ', recall)

    cls = MyNeuralNetBias([5], 600, 0.1, miniBatch=2, decay=0.00001)
    trainOutputsNames = [outputNames[output] for output in trainOutputs]
    cls.fit(trainInputs, trainOutputsNames, outputNames)

    result = cls.predict(testInputs, index=True)
    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), result, outputNames)
    print('acc(manual): ', acc)
    print('precision(manual): ', prec)
    print('recall(manual): ', recall)
    plotConfusionMatrix(cm, outputNames, "Iris classification")


def solveDigitsProblem():
    inputs, outputs, outputNames = loadDigitData()
    trainInputs, trainOutputs, testInputs, testOutputs = splitData(inputs, outputs)
    trainInputsFlatten = [flatten(el) for el in trainInputs]
    testInputsFlatten = [flatten(el) for el in testInputs]
    trainInputsNormalised, testInputsNormalised = normalisation(trainInputsFlatten, testInputsFlatten)

    classifier = neural_network.MLPClassifier(hidden_layer_sizes=(5,), activation='relu', max_iter=100, solver='sgd',
                                              verbose=10, random_state=1, learning_rate_init=.1)
    training(classifier, trainInputsNormalised, trainOutputs)
    predictedLabels = classification(classifier, testInputsNormalised)
    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), predictedLabels, outputNames)
    print('acc(tool): ', acc)
    print('precision(tool): ', prec)
    print('recall(tool): ', recall)

    cls = MyNeuralNetBias([128], 30, 0.1, miniBatch=4, decay=0.005)
    cls.fit(trainInputsNormalised, trainOutputs, outputNames)
    result = cls.predict(testInputsNormalised)
    result = np.reshape(result, (len(result)))

    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), result, outputNames)
    print('acc(manual): ', acc)
    print('precision(manual): ', prec)
    print('recall(manual): ', recall)


def transformToInterval(vector, mini, maxi):
    rgb = [int((x - mini) * 255 / (maxi - mini)) for x in vector]
    return rgb


def transformToSepia(rgb):
    labels = []
    for sample in range(len(rgb)):
        if sample % 2 == 0:
            labels.append('sepia')
            for i in range(0, len(rgb[sample]), 3):
                r, g, b = rgb[sample][i], rgb[sample][i + 1], rgb[sample][i + 2]
                rgb[sample][i] = int(r * 0.393 + g * 0.769 + b * 0.189)
                rgb[sample][i + 1] = int(r * 0.349 + g * 0.686 + b * 0.168)
                rgb[sample][i + 2] = int(r * 0.272 + g * 0.534 + b * 0.131)
        else:
            labels.append('normal')
    return rgb, labels



def solveSepiaProblem():
    inputs, outputs, outputsNames = loadDigitData()
    minimum = np.min(inputs)
    maximum = np.max(inputs)
    gray = [transformToInterval(flatten(el), minimum, maximum) for el in inputs]
    grayMatrix = []
    for i in range(0, 64, 8):
        grayMatrix.append(gray[0][i:i + 8])
    grayMatrix = np.array(grayMatrix)

    rgb = []
    for sample in gray:  # colour digits
        x = []
        i = (np.random.randint(3) + 1) * -1
        for pixel in sample:
            x.append(0)
            x.append(0)
            x.append(0)
            x[i] = pixel
        rgb.append(x)
    # trainInputs, trainOutputs, testInputs, testOutputs = splitData(rgb, outputs)
    rgb, outputs = transformToSepia(rgb)
    showPictures(rgb, 10, 'sepia')
    labelNames = ['sepia', 'normal']
    trainInputs, trainOutputs, testInputs, testOutputs = splitData(rgb, outputs)
    trainInputsNormalised, testInputsNormalised = normalisation(trainInputs, testInputs)

    classifier = neural_network.MLPClassifier(hidden_layer_sizes=(64,), activation='relu', max_iter=150, solver='sgd',
                                              verbose=10, random_state=1, learning_rate_init=.1)
    training(classifier, trainInputsNormalised, trainOutputs)
    predictedLabels = classification(classifier, testInputsNormalised)
    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), predictedLabels, labelNames)
    print('acc(tool): ', acc)
    print('precision(tool): ', prec)
    print('recall(tool): ', recall)

    cls = MyNeuralNetBias([128], 40, 0.1, miniBatch=16, decay=0.01)
    cls.fit(trainInputsNormalised, trainOutputs, labelNames)
    predictedLabels = cls.predict(testInputsNormalised)

    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), predictedLabels, labelNames)
    print('acc(manual): ', acc)
    print('precision(manual): ', prec)
    print('recall(manual): ', recall)


if __name__ == '__main__':
    solveIrisProblem()
    #solveDigitsProblem()
    #solveSepiaProblem()
    #75%





