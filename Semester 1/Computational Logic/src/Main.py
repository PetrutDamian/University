'''
Autor : Petrut Adrian Damian
grupa : 216
Modulul principal
'''
from src.Console import Console
from src.Service import Service
print("Autor : Petrut Adrian Damian\n")
if __name__ == '__main__':
    s = Service()
    c = Console(s)
    c.run()