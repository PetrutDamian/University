import random

import numpy as np
from sklearn.datasets import load_iris
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn import linear_model
from src.LogisticRegression import MyLogisticRegression


def plotDataHistogram(x, variableName):
    n, bins, patches = plt.hist(x, 10)
    plt.title('Histogram of ' + variableName)
    plt.show()


def normalisation(trainData, testData):
    scaler = StandardScaler()
    f1 = [[x] for x in np.array(trainData)[:, 0]]
    scaler.fit(f1)
    f1Norm = scaler.transform(f1)
    t1 = [[x] for x in np.array(testData)[:, 0]]
    t1Norm = scaler.transform(t1)

    f2 = [[x] for x in np.array(trainData)[:, 1]]
    scaler.fit(f2)
    f2Norm = scaler.transform(f2)
    t2 = [[x] for x in np.array(testData)[:, 1]]
    t2Norm = scaler.transform(t2)

    train = [[x[0], y[0]] for x, y in zip(f1Norm, f2Norm)]
    test = [[x[0], y[0]] for x, y in zip(t1Norm, t2Norm)]
    return train, test


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
        precision = [(label, TP[label] / (TP[label] + FP[label])) if TP[label]+FP[label] !=0 else (label,0) for label in labelNames]
    recall = [(label, TP[label] / (TP[label] + FN[label])) if TP[label]+FN[label] !=0 else (label,0) for label in labelNames]

    return accuracy, precision, recall


if __name__ == '__main__':
    # Load data
    iris = load_iris()
    inputs = iris.data
    outputs = iris.target
    outputNames = iris.target_names
    featureNames = iris.feature_names
    feature1 = [feat[featureNames.index('sepal length (cm)')] for feat in inputs]
    feature2 = [feat[featureNames.index('sepal width (cm)')] for feat in inputs]
    feature3 = [feat[featureNames.index('petal length (cm)')] for feat in inputs]
    feature4 = [feat[featureNames.index('petal width (cm)')] for feat in inputs]
    inputs = [[feat[featureNames.index('petal length (cm)')], feat[featureNames.index('petal width (cm)')]] for feat in
              inputs]

    print("feature 3")
    print(feature3)
    print("feature4")
    print(feature4)
    print("end")
    # Plot histograms
    plotDataHistogram(feature1, 'sepal length (cm)')
    plotDataHistogram(feature2, 'sepal width (cm)')
    plotDataHistogram(feature3, 'petal length (cm)')
    plotDataHistogram(feature4, 'petal width (cm)')
    plotDataHistogram(outputs, 'species class')

    # split data into train and test subsets
    # np.random.seed(5)
    indexes = [i for i in range(len(inputs))]
    random.shuffle(indexes)
    k = 0.2
    accuracy_tool = []
    my_accuracy = []
    my_precision = []
    my_recall = []
    for q in np.arange(0, 1, k):
        trainSample = indexes[0:int(q * len(indexes))] + indexes[int((q + k) * len(indexes)):]
        testSample = indexes[int(q * len(indexes)):int((q + k) * len(indexes))]

        trainInputs = [inputs[i] for i in trainSample]
        trainOutputs = [outputs[i] for i in trainSample]
        testInputs = [inputs[i] for i in testSample]
        testOutputs = [outputs[i] for i in testSample]

        # normalise the features
        trainInputs, testInputs = normalisation(trainInputs, testInputs)

        # Solving the model
        classifier = linear_model.LogisticRegression(multi_class='ovr')
        classifier.fit(trainInputs, trainOutputs)
        computedTestOutputs = classifier.predict(testInputs)
        from sklearn.metrics import accuracy_score

        acc = accuracy_score(testOutputs, computedTestOutputs)
        accuracy_tool.append(acc)

        cls = MyLogisticRegression()
        cls.fit(trainInputs, trainOutputs, [i for i in range(len(outputNames))])
        computedManually = cls.predict(testInputs)
        accuracy, precision, recall = classificationEvaluation(testOutputs, computedManually, [0, 1, 2])
        my_accuracy.append(accuracy)
        my_precision.append(precision)
        my_recall.append(recall)
    tool_mean_accuracy = np.sum(accuracy_tool) / len(accuracy_tool)
    my_mean_accuracy = np.sum(my_accuracy) / len(my_accuracy)
    mean_precision = []
    mean_precision.append((0,sum([p[0][1] for p in my_precision])/len(my_precision)))
    mean_precision.append((1,sum([p[1][1] for p in my_precision])/len(my_precision)))
    mean_precision.append((2,sum([p[2][1] for p in my_precision])/len(my_precision)))

    mean_recall = []
    mean_recall.append((0, sum([p[0][1] for p in my_recall]) / len(my_recall)))
    mean_recall.append((1, sum([p[1][1] for p in my_recall]) / len(my_recall)))
    mean_recall.append((2, sum([p[2][1] for p in my_recall]) / len(my_recall)))

    print("Average accuracy(tool): ", tool_mean_accuracy)
    print("Average accuracy(manually): ", my_mean_accuracy)
    print("Precision:")
    print(my_precision)
    print("Recall")
    print(my_recall)
