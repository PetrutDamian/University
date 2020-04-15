import numpy as np
from numpy import inf
import random
from src.FileParser import FileParser




def initialize(problParams, aocParams):
    pheromne = aocParams['c'] * np.ones((aocParams['noAnts'], problParams['noNodes']))
    visibility = 1 / problParams['matrix']
    visibility[visibility == inf] = 0
    initialPoint = [random.randint(0, n - 1) for _ in range(0, aocParams['noAnts'])]
    path = np.ones((aocParams['noAnts'], problParams['noNodes'] + 1))
    for line in range(aocParams['noAnts']):
        for col in range(problParams['noNodes']+1):
            path[line][col] = int(path[line][col])
    return pheromne, visibility, initialPoint, path


def placeAntsAtStartingPosition(initialPoint, path, nrAnts):
    for i in range(nrAnts):
        path[i, 0] = initialPoint[i]


def runAlgorithm(problParams, aocParams):
    globalBestCost = 40000
    pheromone, initialVisibility, initialPoint, path = initialize(problParams, aocParams)
    bestRoutes = []
    bestCosts = []
    xIterations = []
    latestBest = 0
    for iteration in range(aocParams['noIterations']):
        xIterations.append(iteration)
        placeAntsAtStartingPosition(initialPoint, path, aocParams['noAnts'])
        tourCost = []
        deltaPheromone = []
        for ant in range(aocParams['noAnts']):
            visibility = np.array(initialVisibility)
            for step in range(problParams['noNodes'] - 1):
                crtNode = int(path[ant][step])
                visibility[:, crtNode] = 0
                pheromoneVector = np.power(pheromone[crtNode, :], aocParams['alpha'])
                visibilityVector = np.power(visibility[crtNode, :], aocParams['betha'])
                weightedProduct = np.multiply(pheromoneVector, visibilityVector)
                q = random.uniform(0, 1)
                if q >= aocParams['q0']:
                    j = np.argmax(weightedProduct)
                else:
                    sum = np.sum(weightedProduct)
                    probabilitiesWithZeros = weightedProduct / sum
                    indexes = np.nonzero(probabilitiesWithZeros)
                    probabilities = []
                    for el in probabilitiesWithZeros:
                        if el != 0:
                            probabilities.append(el)

                    roulette = np.array(list(set(np.cumsum(probabilities))))
                    r = random.uniform(0, 1)
                    for i in range(len(roulette)):
                        if roulette[i] >= r:
                            j = indexes[0][i]
                            break
                path[ant][step + 1] = j

            path[ant][problParams['noNodes']] = path[ant][0]
            costOfChosenEdges = [(problParams['matrix'])[int(path[ant][i])][int(path[ant][i + 1])] for i in
                                 range(problParams['noNodes'])]
            tourCost.append(np.sum(costOfChosenEdges))
            deltaPheromone.append(1 / tourCost[-1])

        # update pheromone trail
        pheromone = aocParams['e'] * pheromone
        for ant in range(aocParams['noAnts']):
            for j in range(problParams['noNodes']):
                v1 = int(path[ant][j])
                v2 = int(path[ant][j + 1])
                pheromone[v1][v2] += deltaPheromone[ant]

        bestAnt = np.argmin(tourCost)
        latestBest = tourCost[bestAnt]
        bestCosts.append(tourCost[bestAnt])
        if(tourCost[bestAnt] < globalBestCost):
            globalBestCost = tourCost[bestAnt]
        for j in range(problParams['noNodes']):
            v1 = int(path[bestAnt][j])
            v2 = int(path[bestAnt][j+1])
            pheromone[v1][v2] += deltaPheromone[bestAnt]*aocParams['elitist']
        route = []
        for i in range(problParams['noNodes']):
            route.append(path[bestAnt][i]+1)
        bestRoutes.append(route)
    plt.xlabel("Generations")
    plt.ylabel("Fitness")
    plt.title("Fitness evolution: \nIterations:"+str(len(xIterations)) + "evaporation persistence:" +str(aocParams['e'])+
              "\nGlobalBest:"+str(globalBestCost)+"\nLatestBest:"+str(latestBest))
    plt.plot(xIterations, bestCosts, label = "bestFitnessOverTime")
    plt.show()



if __name__ == '__main__':
    import matplotlib.pyplot as plt
    # name of the input file
    filename = "berlin.txt"
    matrix, n = FileParser.parseEuclidian(filename)
    problemParams = {'matrix': np.array(matrix), 'noNodes': n}
    aocParams = {'e': 0.6, 'alpha': 0.5, 'betha': 6, 'q0': 0.2, 'Q': 2, 'c': 0.1, 'noIterations': 500, 'noAnts': n,'elitist':9999}
    runAlgorithm(problemParams, aocParams)
