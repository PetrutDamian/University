class MyLinearRegression:
    def __init__(self):
        self.coef_ = []
        self.intercept = 0.0

    def fit(self, x, y):
        X1 = [val[0] for val in x]
        X2 = [val[1] for val in x]
        Y = y
        N = len(X1)

        X1square = [x ** 2 for x in X1]
        X2square = [x ** 2 for x in X2]
        X1Y = [x * y for x, y in zip(X1, Y)]
        X2Y = [x * y for x, y in zip(X2, Y)]
        X1X2 = [x1 * x2 for x1, x2 in zip(X1, X2)]

        SX1square = sum(X1square)
        SX2square = sum(X2square)
        SX1Y = sum(X1Y)
        SX2Y = sum(X2Y)
        SX1 = sum(X1)
        SX2 = sum(X2)
        SX1X2 = sum(X1X2)

        SY = sum(Y)

        sx1square = SX1square - (SX1 ** 2) / N
        sx2square = SX2square - (SX2 ** 2) / N
        sx1y = SX1Y - SX1 * SY / N
        sx2y = SX2Y - SX2 * SY / N
        sx1x2 = SX1X2 - SX1 * SX2 / N

        b1 = (sx2square * sx1y - sx1x2 * sx2y) / (sx1square * sx2square - sx1x2 ** 2)
        b2 = (sx1square * sx2y - sx1x2 * sx1y) / (sx1square * sx2square - sx1x2 ** 2)
        Ym = SY / N
        X1m = SX1 / N
        X2m = SX2 / N
        b0 = Ym - b1 * X1m - b2 * X2m
        self.intercept = b0
        self.coef_ = [b1, b2]

    def predict(self, x):
        return [self.intercept + self.coef_[0]*val[0] + self.coef_[1]*val[1] for val in x]
