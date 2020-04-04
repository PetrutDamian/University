import copy
from heapq import *

from heapdict import heapdict


class GraphHelper:
    nodes = []
    n = None
    matrix = []
    goal = None
    start = None

    def __init__(self):
        pass

    @staticmethod
    def initializeQueueForDijkstra(queue):
        FileParser.parseSymmetrical()
        Node.start = GraphHelper.start
        Node.finish = GraphHelper.goal
        Node.pi = [-1] * (GraphHelper.n + 1)
        Node.dist = [float("inf")] * (GraphHelper.n + 1)
        Node.dist[GraphHelper.start] = 0
        Node.adjMatrix = GraphHelper.matrix
        Node.visited = [False] * (GraphHelper.n + 1)
        queue.adjustPriority(Node(Node.start), 0)
        Node.n = GraphHelper.n

    @staticmethod
    def initializeQueueForTsp(queue):
        FileParser.parseSymmetrical()
        NodeTsp.adjMatrix = GraphHelper.matrix
        NodeTsp.n = GraphHelper.n
        toVisit = {key: None for key in range(2, NodeTsp.n + 1)}
        queue.adjustPriority(NodeTsp(1, 0, toVisit, [1]), 0)
        NodeTsp.queue = queue
        NodeTsp.maxVisited = 1000
        NodeTsp.solution = None
        NodeTsp.bestCost = float("inf")
        NodeTsp.path = None

    @staticmethod
    def initializeQueueForTspGreedy(queue):
        FileParser.parseSymmetrical()
        NodeTspGreedy.adjMatrix = GraphHelper.matrix
        NodeTspGreedy.n = GraphHelper.n
        NodeTspGreedy.visitedList = [False] * (GraphHelper.n + 1)
        queue.adjustPriority(NodeTspGreedy(1), 0)
        NodeTspGreedy.leftToVisit = GraphHelper.n
        NodeTspGreedy.cost = 0
        NodeTspGreedy.path = []

    @staticmethod
    def initializeQueueForTspAverage(queue):
        FileParser.parseSymmetrical()
        NodeTspAverage.adjMatrix = GraphHelper.matrix
        NodeTspAverage.n = GraphHelper.n
        toVisit = {key: None for key in range(1, NodeTspAverage.n + 1)}
        queue.adjustPriority(NodeTspAverage(0, 0, toVisit, []), 0)
        NodeTspAverage.queue = queue
        NodeTspAverage.maxVisited = 1000
        NodeTspAverage.solution = None
        NodeTspAverage.bestCost = float("inf")
        NodeTspAverage.path = None
        NodeTspAverage.bestAverageCost = float("inf")


class NodeTspGreedy:
    n = None
    adjMatrix = None
    visitedList = []
    leftToVisit = None
    cost = 0
    path = []

    def __init__(self, elem):
        self.elem = elem

    def __hash__(self):
        return self.elem

    def isSolution(self):
        return NodeTspGreedy.leftToVisit == 1

    def returnSolution(self):
        return NodeTspGreedy.path, NodeTspGreedy.cost + NodeTspGreedy.adjMatrix[self.elem][1]

    def expand(self, queue):
        NodeTspGreedy.visitedList[self.elem] = True
        NodeTspGreedy.leftToVisit = NodeTspGreedy.leftToVisit - 1
        NodeTspGreedy.path.append(self.elem)
        minEdge = float("inf")
        chosen = None
        for i in range(1, NodeTspGreedy.n + 1):
            if not NodeTspGreedy.visitedList[i]:
                if NodeTspGreedy.adjMatrix[self.elem][i] < minEdge:
                    minEdge = NodeTspGreedy.adjMatrix[self.elem][i]
                    chosen = i
        NodeTspGreedy.cost += minEdge
        if chosen is not None:
            queue.adjustPriority(NodeTspGreedy(chosen), 0)


class Node:
    n = None
    pi = []
    dist = []
    start = None
    finish = None
    adjMatrix = None
    visited = None

    def __init__(self, value):
        self.value = value

    def __hash__(self):
        return self.value

    def isSolution(self):
        return self.value == Node.finish

    def returnSolution(self):
        cost = Node.dist[self.value]
        path = []
        while self.value != Node.start:
            path.append(self.value)
            self.value = Node.pi[self.value]
        path.append(Node.start)
        path.reverse()
        return str(path), cost

    def expand(self, queue):
        Node.visited[self.value] = True
        parent = self.value
        for i in range(1, Node.n + 1):
            if not Node.visited[i]:
                newDistance = Node.dist[parent] + Node.adjMatrix[parent][i]
                if newDistance < Node.dist[i]:
                    Node.dist[i] = newDistance
                    Node.pi[i] = parent
                    queue.adjustPriority(Node(i), newDistance)


class NodeTsp:
    maxVisited = None
    adjMatrix = []
    solution = None
    bestCost = float("inf")
    n = None
    queue = None
    path = None

    def __init__(self, node, value, toVisit, path):
        self.elem = node
        self.cost = value
        self.toVisit = toVisit
        self.path = path

    def __hash__(self):
        return self.elem

    def isSolution(self):
        if len(self.toVisit) < NodeTsp.maxVisited:
            print("Nou vizitat:" + str(len(self.toVisit)))
            NodeTsp.maxVisited = len(self.toVisit)

        if len(self.toVisit) == 0:
            myCost = self.cost + NodeTsp.adjMatrix[self.elem][1]
            if myCost < NodeTsp.bestCost:
                # if NodeTsp.bestCost is None:
                print("Solution Found with cost:" + str(myCost))
                NodeTsp.bestCost = myCost
                NodeTsp.path = self.path
            first = NodeTsp.queue.peek()
            if first.cost >= NodeTsp.bestCost:
                return True
            return False
        if NodeTsp.bestCost <= self.cost:
            return True
        return False

    def returnSolution(self):
        return NodeTsp.path, NodeTsp.bestCost

    def expand(self, queue):
        for key in self.toVisit:
            cost = self.cost + NodeTsp.adjMatrix[self.elem][key]
            dictCopy = copy.deepcopy(self.toVisit)
            dictCopy.pop(key)
            queue.adjustPriority(NodeTsp(key, cost, dictCopy, self.path + [key]), cost)


class NodeTspAverage(NodeTsp):
    bestAverageCost = float("inf")

    def expand(self, queue):
        for key in self.toVisit:
            cost = self.cost + NodeTspAverage.adjMatrix[self.elem][key]
            dictCopy = copy.deepcopy(self.toVisit)
            dictCopy.pop(key)
            queue.adjustPriority(NodeTspAverage(key, cost, dictCopy, self.path + [key]),
                                 cost / max(1, NodeTspAverage.n - len(dictCopy)))

    def isSolution(self):
        if len(self.toVisit) < NodeTspAverage.maxVisited:
            NodeTspAverage.maxVisited = len(self.toVisit)

        if len(self.toVisit) == 0:
            myCost = (self.cost + NodeTspAverage.adjMatrix[self.elem][self.path[0]])
            #myCost = self.cost
            myAverageCost = myCost / (NodeTspAverage.n + 1)
            #myAverageCost = myCost / NodeTspAverage.n
            if myAverageCost < NodeTspAverage.bestAverageCost:
                # if NodeTsp.bestCost is None:
                print("Solution Found with cost:" + str(myCost))
                NodeTspAverage.bestAverageCost = myAverageCost
                NodeTspAverage.bestCost = myCost
                NodeTspAverage.path = self.path
            first = NodeTspAverage.queue.peek()
            if first.cost >= NodeTspAverage.bestCost:
                return True
            return False
        elif NodeTspAverage.bestCost <= self.cost:
            return True
        return False

    def returnSolution(self):
        return NodeTspAverage.path, NodeTspAverage.bestCost



class FileParser:
    filename = None

    @staticmethod
    def parseSymmetrical():
        f = open("D:\DISK D\FACULTATE\ANUL 2\AI\Lab2\Resources/" + FileParser.filename)
        GraphHelper.n = int(f.readline())
        GraphHelper.matrix = [[0]*(GraphHelper.n+1)]
        for line in f:
            args = line.split(',')
            l = [0]
            for arg in args:
                l.append(float(arg))
            GraphHelper.matrix.append(l)

    @staticmethod
    def generateRandomAdjMatrix(n, filename):
        f = open("D:/DISK D/FACULTATE/ANUL 2/AI/Lab2/Resources/" + filename, "w+")
        GraphHelper.n = n
        matrix = [None]
        import random
        for i in range(1, n + 1):
            matrix.append([-1] * (n + 1))
        for i in range(1, n):
            for j in range(i, n + 1):
                if i == j:
                    matrix[i][i] = 0
                else:
                    matrix[i][j] = random.randint(1, 1000)
                    matrix[j][i] = matrix[i][j]
        matrix[n][n] = 0
        GraphHelper.matrix = matrix
        f.write(str(n) + "\n")
        for i in range(1, n + 1):
            for j in range(1, n + 1):
                f.write(str(matrix[i][j]))
                if j != n:
                    f.write(",")
            if i != n:
                f.write("\n")
        f.close()


class PQueue:
    def __init__(self, initialize):
        self.nodes = heapdict()
        self.initialize = initialize
        self.initialize(self)

    def pop(self):
        return self.nodes.popitem()[0]

    def adjustPriority(self, node, priority):
        self.nodes[node] = priority

    def peek(self):
        return self.nodes.peekitem()[0]
