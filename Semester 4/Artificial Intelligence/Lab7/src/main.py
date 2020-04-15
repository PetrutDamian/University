import csv
import os
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from sklearn import linear_model #sklearn used for comparison

from src.MyRegression import MyLinearRegression


def loadData(fileName, inputVar1, inputVar2, outputVariabName):
    data = []
    dataNames = []
    with open(fileName) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                dataNames = row
            else:
                data.append(row)
            line_count += 1

    indexVar1 = dataNames.index(inputVar1)
    indexVar2 = dataNames.index(inputVar2)

    inputGDP = [float(data[i][indexVar1]) for i in range(len(data))]
    inputFree = [float(data[i][indexVar2]) for i in range(len(data))]
    selectedOutput = dataNames.index(outputVariabName)
    outputs = [float(data[i][selectedOutput]) for i in range(len(data))]

    return inputGDP, inputFree, outputs


def plotDataHistogram(x, variableName):
    n, bins, patches = plt.hist(x, 10)
    plt.title('Histogram of ' + variableName)
    plt.show()


def plot2D(input, output, marker, xlabel, ylabel, title):
    plt.plot(input, output, marker)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.title(title)
    plt.show()


def plot3D(x, y, z, c1, marker1, xlabel, ylabel, zlabel, label):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d', label=label)
    ax.scatter(x, y, z, c=c1, marker=marker1)
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    ax.set_zlabel(zlabel)
    plt.show()
    plt.close(fig)


def plot3Dtest(train, test, xl, yl, zl, c1, c2, marker1, title, label, trainLabel, testLabel):
    fig = plt.figure()
    plt.title(title)
    ax1 = fig.add_subplot(111, projection='3d', label=label)
    ax1.scatter(train[0], train[1], train[2], c=c1, marker=marker1, label=trainLabel)
    ax1.scatter(test[0], test[1], test[2], c=c2, marker=marker1, label=testLabel)
    ax1.set_xlabel(xl)
    ax1.set_ylabel(yl)
    ax1.set_zlabel(zl)
    plt.legend()
    plt.show()


def split(input1, input2, outputs):
    #np.random.seed(5)
    indexes = [i for i in range(len(input1))]
    trainSample = np.random.choice(indexes, int(0.8 * len(input1)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    trainInputs1 = [inputs1[i] for i in trainSample]
    trainInputs2 = [inputs2[i] for i in trainSample]
    trainOutputs = [outputs[i] for i in trainSample]

    testInputs1 = [inputs1[i] for i in testSample]
    testInputs2 = [inputs2[i] for i in testSample]
    testOutputs = [outputs[i] for i in testSample]
    return [trainInputs1, trainInputs2, trainOutputs], [testInputs1, testInputs2, testOutputs]


def solve(train):
    # df = pd.DataFrame('GDP':train[0])
    xx = [[i, j] for i, j in zip(train[0], train[1])]
    regressor = linear_model.LinearRegression()
    regressor.fit(xx, train[2])
    return [regressor.intercept_, regressor.coef_[0], regressor.coef_[1]]


def plotModel(train, w):
    noOfPoints = 100
    step1 = (max(train[0]) - min(train[0])) / noOfPoints
    step2 = (max(train[1]) - min(train[1])) / noOfPoints
    val1 = min(train[0])
    val2 = min(train[1])
    x = []
    y = []
    for i in range(1, noOfPoints):
        x.append(val1)
        y.append(val2)
        val1 += step1
        val2 += step2
    z = [w[0] + w[1] * a + w[2] * b for a, b in zip(x, y)]
    plot3Dtest(train, [x, y, z], 'GDP per capita', 'Freedom', 'Happiness', 'r', 'b', 'o', 'model vs training data', '3',
               'trainData', 'model')

    fig = plt.figure()
    ax1 = fig.add_subplot(111, projection='3d', label='5')
    plt.title('model vs training data')
    ax = fig.gca(projection='3d')
    X, Y = np.meshgrid(np.array(x), np.array(y))
    Z = w[0] + w[1] * X + w[2] * Y
    surf = ax.plot_surface(X, Y, Z, alpha='0.5')

    ax1.scatter(train[0], train[1], train[2], c='r', marker='o', label='trainingData')
    ax1.set_xlabel('GDP')
    ax1.set_ylabel('Freedom')
    ax1.set_zlabel('Happiness')
    plt.legend()
    plt.show()


if __name__ == '__main__':
    crtDir = os.getcwd()
    filePath = os.path.join(crtDir, 'data', 'world-happiness-report-2017.csv')

    inputs1, inputs2, outputs = loadData(filePath, 'Economy..GDP.per.Capita.', 'Freedom', 'Happiness.Score')

    plotDataHistogram(inputs1, 'capita GDP')
    plotDataHistogram(inputs2, 'Freedom')
    plotDataHistogram(outputs, 'Happiness score')

    plot2D(inputs1, outputs, 'ro', 'GDP capita', 'Happiness', 'GDP capita vs Happiness')
    plot2D(inputs2, outputs, 'bo', 'Freedom', 'Happiness', 'Freedom vs Happiness')
    plot3D(inputs1, inputs2, outputs, 'r', 'o', 'GDP capita', 'Freedom', 'Happiness', '1')

    train, test = split(inputs1, inputs2, outputs)
    plot3Dtest(train, test, 'GDP capita', 'Freedom', 'Happiness', 'r', 'g', 'o', 'train and test data', '2', 'train',
               'test')
    # 2.5283663171908195  +  1.876338735881282  * x1 + 2.3147487475033257 * x2 with sklearn
    regressor = MyLinearRegression()
    x = [[x1, x2] for x1, x2 in zip(train[0], train[1])]
    regressor.fit(x, train[2])
    w = [regressor.intercept, regressor.coef_[0], regressor.coef_[1]]
    print('the learnt model: f(x) = ', str(w[0]), ' + ', str(w[1]), ' * x1 + ' + str(w[2]) + ' * x2')

    plotModel(train, w)

    x = [[x1, x2] for x1, x2 in zip(test[0], test[1])]
    predicted = regressor.predict(x)
    plot3Dtest([test[0], test[1], predicted], [test[0], test[1], test[2]], 'GDp per capita', 'Freedom', 'Happiness',
               'y', 'g', '^', 'TEST: computed vs real','7','computed','real')

    print("---------------------------Evaluation------------------------------------------------------------------")
    from src.Evaluation import lossForRegression
    real = [[] for i in range(len(test[2]))]
    for i in range(len(test[2])):
        real[i].append(test[2][i])
    computed = [[] for i in range(len(test[2]))]
    for i in range(len(test[2])):
        computed[i].append(predicted[i])
    meanSquaredError = lossForRegression(real,computed)
    print("MeanSquaredError : " + str(meanSquaredError))
