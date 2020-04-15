from random import seed

from src import Utils
from src.FileParser import FileParser
from src.GA import GA
import matplotlib.pyplot as plt
import numpy


class Console:
    def run(self):
        file = input("Input file:")
        nrGen = input("Number of generations:")
        nrPop = input("Population number:")
        turnirSize = input("Turnir size:")
        mutationRate = input("Mutation rate:")
        self.compute(file, int(nrGen), int(nrPop), float(turnirSize), float(mutationRate))

    def compute(self, file, nrGen, nrPop, turnirSize, mutationRate):
        #matrix, n = FileParser.parseMatrix(file)
        matrix, n = FileParser.parseEuclidian(file)

        problParam = {}
        problParam['noNodes'] = n
        problParam['matrix'] = matrix
        problParam['function'] = Utils.evaluateTsp


        gaParam = {}
        gaParam['noGen'] = nrGen
        gaParam['popSize'] = nrPop
        gaParam['turnirSize'] = turnirSize
        gaParam['mutationRate'] = mutationRate

        ga = GA(gaParam,problParam)
        ga.initialisation()
        ga.evaluation()

        allBestFitnesses = []
        allWorstFitness = []
        for g in range(gaParam['noGen']):
            ga.oneGenerationElitism()
            bestSolY = ga.bestChromosome().fitness
            allBestFitnesses.append(bestSolY)
            allWorstFitness.append(ga.worstChromosome().fitness)
            if g%50==0:
                print("reached generation " + str(g))

        plt.xlabel("Generation")
        plt.ylabel("Fitness")
        plt.title("Fitness evolution over time\nPopulation:"+str(gaParam['popSize'])
                  +"\nFitness reached:"+str(allBestFitnesses[-1])+"\nTurnir size:"
                  +str(gaParam['turnirSize']*100)+"%")
        x = [i for i in range(len(allBestFitnesses))]
        y = numpy.array(allBestFitnesses)
        plt.plot(x, y, label = "bestFitnessOverGenerations")
        z = numpy.array(allWorstFitness)
        plt.plot(x,z,label="WorstFitnessOverGenerations")
        plt.legend()
        plt.show()

        print("Best fitnesses across all generations:")
        print(allBestFitnesses)
        print("\nBest current chromosome")
        print(ga.bestChromosome().repres)
        print("Best fitness")
        print(ga.bestChromosome().fitness)




