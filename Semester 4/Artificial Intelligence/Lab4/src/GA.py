from random import randint, seed


def generateARandomPermutation(n):
    perm = [i for i in range(n)]
    pos1 = randint(1, n - 1)
    pos2 = randint(1, n - 1)
    perm[pos1], perm[pos2] = perm[pos2], perm[pos1]
    return perm


# permutation-based representation
class Chromosome:
    def __init__(self, problParam, mutationRate):
        self.__problParam = problParam  # problParam has to store the number of nodes/cities
        self.__repres = generateARandomPermutation(self.__problParam['noNodes'])
        self.__fitness = 0.0
        self.__mutationRate = mutationRate

    @property
    def repres(self):
        return self.__repres

    @property
    def fitness(self):
        return self.__fitness

    @repres.setter
    def repres(self, l=[]):
        self.__repres = l

    @fitness.setter
    def fitness(self, fit=0.0):
        self.__fitness = fit

    def crossover(self, c):
        # order XO
        pos1 = randint(-1, self.__problParam['noNodes'] - 1)
        pos2 = randint(-1, self.__problParam['noNodes'] - 1)
        if pos2 < pos1:
            pos1, pos2 = pos2, pos1
        k = 0
        newrepres = self.__repres[pos1: pos2]
        for el in c.__repres[pos2:] + c.__repres[:pos2]:
            if el not in newrepres:
                if len(newrepres) < self.__problParam['noNodes'] - pos1:
                    newrepres.append(el)
                else:
                    newrepres.insert(k, el)
                    k += 1

        offspring = Chromosome(self.__problParam, self.__mutationRate)
        offspring.repres = newrepres
        return offspring

    def mutation(self):
        import random
        r = random.uniform(0, 1)
        if r <= self.__mutationRate:
            self.__mutate()

    def __mutate(self):
        pos1 = randint(0, self.__problParam['noNodes'] - 1)
        pos2 = randint(0, self.__problParam['noNodes'] - 1)
        if pos2 < pos1:
            pos1, pos2 = pos2, pos1
        el = self.__repres[pos2]
        del self.__repres[pos2]
        self.__repres.insert(pos1 + 1, el)

    def __str__(self):
        return "\nChromo: " + str(self.__repres) + " has fit: " + str(self.__fitness)

    def __repr__(self):
        return self.__str__()

    def __eq__(self, c):
        return self.__repres == c.__repres and self.__fitness == c.__fitness


class GA:
    def __init__(self, _gaParam=None, problParam=None):
        self.__param = _gaParam
        self.__problParam = problParam
        self.__population = []

    @property
    def population(self):
        return self.__population

    def initialisation(self):
        for _ in range(0, self.__param['popSize']):
            c = Chromosome(self.__problParam, self.__param['mutationRate'])
            self.__population.append(c)

    def evaluation(self):
        for c in self.__population:
            c.fitness = self.__problParam['function'](c.repres, self.__problParam['matrix'])

    def bestChromosome(self):
        best = self.__population[0]
        for c in self.__population:
            if (c.fitness < best.fitness):
                best = c
        return best

    def worstChromosome(self):
        best = self.__population[0]
        for c in self.__population:
            if (c.fitness > best.fitness):
                best = c
        return best

    def selection(self):
        nr = len(self.__population)
        rate = self.__param['turnirSize']
        turneuSize = int(nr * rate)
        individ = self.__population[randint(0, len(self.__population) - 1)]
        cost = individ.fitness
        for i in range(turneuSize):
            individ2 = self.__population[randint(0, len(self.__population) - 1)]
            cost2 = individ2.fitness
            if cost2 < cost:
                cost = cost2
                individ = individ2
        return individ

    def oneGeneration(self):
        newPop = []
        for _ in range(self.__param['popSize']):
            p1 = self.selection()
            p2 = self.selection()
            off = p1.crossover(p2)
            off.mutation()
            newPop.append(off)
        self.__population = newPop
        self.evaluation()

    def oneGenerationElitism(self):
        newPop = [self.bestChromosome()]
        for _ in range(self.__param['popSize'] // 2):
            p1 = self.selection()
            p2 = self.selection()
            off = p1.crossover(p2)
            off.mutation()
            newPop.append(off)
            off2 = p2.crossover(p1)
            off2.mutation()
            newPop.append(off2)
        self.__population = newPop
        self.evaluation()

    def oneGenerationSteadyState(self):
        for _ in range(self.__param['popSize']):
            p1 = self.selection()
            p2 = self.selection()
            off = p1.crossover(p2)
            off.mutation()
            off.fitness = self.__problParam['function'](off.repres, self.__problParam['matrix'])
            worst = self.worstChromosome()
            if (off.fitness < worst.fitness):
                worst = off
