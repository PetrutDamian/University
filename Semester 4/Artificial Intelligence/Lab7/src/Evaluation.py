import numpy as np
from math import sqrt


def predictionError(real, predicted):
    # real and predicted are lists with noSamples elements, each element is a list with the target outputs
    k = len(real[0])
    noSamples = len(real)

    real = np.array(real)
    predicted = np.array(predicted)

    meanAbsoluteError = sum(
        [sum([abs(r - c) for r, c in zip(real[:, i], predicted[:, i])]) / noSamples for i in range(k)]) / k
    rootMeanSquaredError = sum(
        [sqrt(sum([(r - c) ** 2 for r, c in zip(real[:, i], predicted[:, i])]) / noSamples) for i in range(k)]) / k

    return meanAbsoluteError, rootMeanSquaredError


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

    precision = [(label, TP[label] / (TP[label] + FP[label])) for label in labelNames]
    recall = [(label, TP[label] / (TP[label] + FN[label])) for label in labelNames]

    return accuracy, precision, recall

def lossForClassification(real, predicted):
    import math
    return sum([-1 * (real[i]*math.log(predicted[i][0]) + (1-real[i]) * math.log(1 - predicted[i][0])) for i in range(len(real))]) / len(real)

def lossForRegression(real, predicted):
    noSamples = len(real)
    k = len(real[0])
    real = np.array(real)
    predicted = np.array(predicted)

    meanSquaredError = sum([sum([(r - c) ** 2 for r, c in zip(real[:, i], predicted[:, i])]) / noSamples for i in range(k)]) / k
    return meanSquaredError

def lossForClassificationMultiClass(real, predicted, labels):
    import math
    samples = len(real)
    logLoss = 0
    for i in range(samples):
        true = real[i]
        index = -1
        for j in range(len(labels)):
            if labels[j] == true:
                index = j
                break
        logLoss += math.log(predicted[i][index])
    logLoss *= -1
    return logLoss

def lossForClassificationMultiLabel(real, predicted, labels):
    samples = len(real)
    import math
    totalLoss = 0
    for i in range(samples):
        logLoss = sum([math.log(predicted[i][j]) if real[i][j] == 1 else 0 for j in range(len(labels))]) * -1
        totalLoss += logLoss
    totalLoss = totalLoss / samples
    return totalLoss

if __name__ == '__main__':
    import FileParser
    real1, predicted1 = FileParser.FileParser.getInputRegression("regression.txt")
    meanAbsoluteError, rootMeanSquaredError = predictionError(real1, predicted1)
    print("-------------------     REGRESSION : MeanAbsoluteError, rootMeanSquaredError       ----------------------")
    print(meanAbsoluteError, rootMeanSquaredError)
    print("---------------------------------------------------------------------------------------------------------")

    real2, predicted2, labels = FileParser.FileParser.getInputStrictClassification("classification.txt")
    accuracy, precision, recall = classificationEvaluation(real2, predicted2, labels)
    print("-------------------     CLASSIFICATION : Accuracy, Precision, Recall       -----------------------------")
    print(accuracy)
    print(precision)
    print(recall)
    print("-------------------------------------------------------------------------------------------------------")

    real3, predicted3 = FileParser.FileParser.getInputBinaryClassificationProbabilities("binaryProbabilities.txt")
    print("-------------------     BINARY CLASSIFICATION : Loss       -------------------------------------------")
    loss = lossForClassification(real3, predicted3)
    print(loss)
    print("---------------------------------------------------------------------------------------------------------")

    real4, predicted4 = FileParser.FileParser.getInputRegression("regression.txt")
    loss = lossForRegression(real4, predicted4)
    print("-------------------     REGRESSION (MULTI-TARGET) : Loss       -------------------------------------------")
    print(loss)
    print("---------------------------------------------------------------------------------------------------------")

    real5, predicted5, labels2 = FileParser.FileParser.getInputMultiClassClasificationProbabilities("multiClassProbabilities.txt")
    loss = lossForClassificationMultiClass(real5, predicted5, labels2)
    print("-------------------     MULTI-CLASS CLASSIFICATION WITH PROBABILITIES : Loss       ----------------------")
    print(loss)
    print("---------------------------------------------------------------------------------------------------------")

    real6, predicted6, labels3 = FileParser.FileParser.getInputMultiLabelProbabilities("multiLabelProbabilities.txt")
    loss = lossForClassificationMultiLabel(real6, predicted6, labels3)
    print("-------------------     MULTI-LABEL CLASSIFICATION WITH PROBABILITIES : Loss       ----------------------")
    print(loss)
    print("---------------------------------------------------------------------------------------------------------")


