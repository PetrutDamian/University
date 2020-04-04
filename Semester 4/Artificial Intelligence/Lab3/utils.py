from random import uniform, randint
import os 
import networkx as nx

class Chromosome:
    def __init__(self, problParam = None, genotype = None):
        self.__problParam = problParam
        self.__fitness = 0.0
        if genotype is not None:
            self.__repres = genotype
        else:
            n = problParam['noDim']
            repeat = int(n*problParam['initializeFactor'])
            self.__repres = [i for i in range(1,n+1)]
            adj = problParam['network']['mat']
            for i in range(0,repeat):
                k = randint(0,problParam['noDim']-1)
                color = self.__repres[k]
                for j in range(0,n):
                    if adj[k][j]!=0:
                        self.__repres[j] = color
    
    @property
    def repres(self):
        return self.__repres
    
    @property
    def fitness(self):
        return self.__fitness 
    
    @repres.setter
    def repres(self, l = []):
        self.__repres = l 
    
    @fitness.setter 
    def fitness(self, fit = 0.0):
        self.__fitness = fit 
    
    def crossover(self, c):
        n = len(self.__repres)
        times = int(self.__problParam['crossoverRate'])
        v = c.repres.copy()
        for i in range(0,times):
            r = randint(0, n-1)
            cluster = self.__repres[r]
            for j in range(0,n-1):
                if self.__repres[j]==cluster:
                    v[j] == cluster
                    
        offspring = Chromosome(c.__problParam, v)
        return offspring
    
    def mutation(self):
        n = len(self.__repres)-1
        i = randint(0, n)
        j = randint(0, n)
        clusteri = self.__repres[i]
        clusterj = self.__repres[j]
        if clusteri != clusterj:
            for k in range(0,n+1):
                if self.__repres[k] == clusterj:
                    self.__repres[k] = clusteri
        
    def __str__(self):
        return '\nChromo: ' + str(self.__repres) + ' has fit: ' + str(self.__fitness)
    
    def __repr__(self):
        return self.__str__()
    
    def __eq__(self, c):
        return self.__repres == c.__repres and self.__fitness == c.__fitness

def readNet(fileName):
    f = open(fileName, "r")
    net = {}
    n = int(f.readline())
    net['noNodes'] = n
    mat = []
    for i in range(n):
        mat.append([])
        line = f.readline()
        elems = line.split(" ")
        for j in range(n):
            mat[-1].append(int(elems[j]))
    net["mat"] = mat 
    degrees = []
    noEdges = 0
    for i in range(n):
        d = 0
        for j in range(n):
            if (mat[i][j] == 1):
                d += 1
            if (j > i):
                noEdges += mat[i][j]
        degrees.append(d)
    net["noedges"] = noEdges
    net["degrees"] = degrees
    f.close()
    return net

def generateNewValue(lim1, lim2):
    return uniform(lim1, lim2)

def readNetwork(filename):

    G = nx.read_gml(filename,label='id')
    net = {'noNodes': G.number_of_nodes(), 'noEdges': G.number_of_edges()}
    adj = [[0 for i in range(G.number_of_nodes())] for j in range(G.number_of_nodes())]
    dict = {}
    
    index = 0
    for nod in G.nodes():
        dict[nod] = index
        index = index + 1
        
    for edges in G.edges():
        n1 = dict[edges[0]]
        n2 = dict[edges[1]]
        adj[n1][n2] = 1
        adj[n2][n1] = 1
    
    net['mat'] = adj
    degrees = []
    for i in range(0, net['noNodes']):
        d = 0
        for j in range(0, net['noNodes']):
            if adj[i][j] == 1:
                d += 1
        degrees.append(d)
    net["degrees"] = degrees
    return net

