from src.Service.Service import Service
from src.Utils.Greedy import *


class Console:
    def __init__(self):
        self.serv = Service()

    def __message(self):
        print("\n-------------------------------------------------------------------------------------------------\n" +
              "0 : Generate random graph\n1 : Travelling salesman (optimal using total distance)\n2 : Shortest path from x to y\n3 : "
              + "Travelling salesman (greedy approach)\n4 : Travelling salesman (optimal using average distance)\n")

    def run(self):
        import time
        while True:
            self.__message()
            cmd = int(input(">>"))
            if cmd == 0:
                n = int(input("n = "))
                FileParser.generateRandomAdjMatrix(n, "rand1.txt")
            elif cmd == 2:
                x = int(input("x = "))
                y = int(input("y ="))
                filename = (input("filename :"))
                startTime = time.time()
                path, cost = self.serv.runDijkstra(x, y, filename)
                endTime = time.time()
                print("Time :" + str(endTime - startTime))
                print("Cost from node " + str(x) + " to node " + str(y) + " = " + str(cost) + "\nPath :" + str(path))
            elif cmd == 1:
                filename = (input("filename :"))
                startTime = time.time()
                path, cost = self.serv.runTspOptimal(filename)
                endTime = time.time()
                print("Time :" + str(endTime - startTime))
                print("Cost to traverse all nodes" + str(cost) + "\n Path :" + str(path))

            elif cmd == 3:
                filename = (input("filename"))
                startTime = time.time()
                path, cost = self.serv.runTspPureGreedy(filename)
                endTime = time.time()
                print("Time :" + str(endTime - startTime))
                print("Cost to traverse all nodes" + str(cost) + "\n Path :" + str(path))

            elif cmd == 4:
                filename = (input("filename :"))
                startTime = time.time()
                path, cost = self.serv.runTspOptimalWithAverage(filename)
                endTime = time.time()
                print("Time :" + str(endTime - startTime))
                print("Cost to traverse all nodes" + str(cost) + "\n Path :" + str(path))

            elif cmd == 5:
                filename = (input("filename :"))
                startTime = time.time()
                path, cost = self.serv.runTspOptimalWithAStar(filename)
                endTime = time.time()
                print("Time :" + str(endTime - startTime))
                print("Cost to traverse all nodes" + str(cost) + "\n Path :" + str(path))
