from random import shuffle

from src.Evaluation import lossForRegression
from src.main import plot2D


class GradientDescent:
    def __init__(self):
        self.coef_ = []
        self.intercept_ = 0.0

    def fit(self, x1, y, learningRate=0.005, noEpochs=2000, noBatches=6):
        B = []
        y1 = [[i] for i in y]
        x = x1.copy()
        for i in range(len(x)):
            x[i].append(y[i])

        self.coef_ = [0.0 for _ in range(len(x[0]))]  # beta or w coefficients y = w0 + w1 * x1 + w2 * x2 + ...
        # self.coef_ = [random.random() for _ in range(len(x[0]) + 1)]    #beta or w coefficients
        for epoch in range(noEpochs):
            B.append(lossForRegression(y1, [[x] for x in self.predict(x1)]))
            shuffle(x)
            batches = []
            length = len(x) // noBatches
            for i in range(0, noBatches):
                batches.append(x[(i * length):(i * length + length)])
            for i in range(noBatches):
                crtError = []
                for xi in batches[i]:
                    computed = self.eval(xi[:-1])
                    crtError.append(computed - xi[-1])
                for j in range(0, len(x[0]) - 1):  # update the coefficients
                    self.coef_[j] = self.coef_[j] - learningRate * crtError[j] * x[i][j]
                self.coef_[len(x1[0]) - 1] = self.coef_[len(x1[0]) - 1] - learningRate * crtError[len(x1[0])-1]

        self.intercept_ = self.coef_[-1]
        self.coef_ = self.coef_[:-1]
        A = [i for i in range(noEpochs)]
        plot2D(A, B, 'ro', 'Epoch', 'MeanSquareError', 'Cost function over TIme -Gradient descent')

    def predict(self, x):
        return [self.intercept_ + self.coef_[0] * val[0] + self.coef_[1] * val[1] for val in x]

    def eval(self, xi):
        yi = self.coef_[-1]
        for j in range(len(xi)):
            yi += self.coef_[j] * xi[j]
        return yi
