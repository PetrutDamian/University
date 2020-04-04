from heapq import *

from src.Utils.Greedy import *


class Service:
    def __init__(self):
        pass

    def search(self, queue):
        while True:
            node = queue.pop()
            if node.isSolution():
                return node.returnSolution()
            node.expand(queue)

    def runDijkstra(self, x, y, filename):
        FileParser.filename = filename
        GraphHelper.start = x
        GraphHelper.goal = y
        return self.search(PQueue(GraphHelper.initializeQueueForDijkstra))

    def runTspOptimal(self, filename):
        FileParser.filename = filename
        return self.search(PQueue(GraphHelper.initializeQueueForTsp))

    def runTspPureGreedy(self, filename):
        FileParser.filename = filename
        return self.search(PQueue(GraphHelper.initializeQueueForTspGreedy))

    def runTspOptimalWithAverage(self, filename):
        FileParser.filename = filename
        return self.search(PQueue(GraphHelper.initializeQueueForTspAverage))

    def runTspOptimalWithAStar(self, filename):
        FileParser.filename = filename
        return self.search(PQueue(GraphHelper.initializeQueueForTspAStar))

