'''
Modul auxiliar
'''


def cifra(litera):
    '''
    primeste o cifra in format string si returneaza int
    '''
    dictl = {"0": 0, "1": 1, "2": 2, "3": 3, "4": 4, "5": 5, "6": 6, "7": 7, "8": 8, "9": 9, "A": 10, "B": 11, "C": 12,
             "D": 13,
             "E": 14, "F": 15}
    return dictl[litera]


def litera(cifra):
    "primeste o cifra de tip int si returneaza litera corespunzatoare de format string"

    if cifra <= 9:
        return str(cifra)
    dictc = {10: "A", 11: "B", 12: "C", 13: "D", 14: "E", 15: "F"}
    return dictc[cifra]
