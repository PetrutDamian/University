class TspPath:
    def __init__(self, costMatrix):
        self.path = []
        self.cost = 0
        self.matrix = costMatrix
        self.remaining = [x for x in range(2, len(costMatrix) + 1)]

    def __lt__(self, other):
        myCost = self.cost
        otherCost = other.cost
        if myCost < other:
            return True
        else:
            if myCost == otherCost:
                if len(self.remaining) < len(other.remaining):
                    return True
            return False
