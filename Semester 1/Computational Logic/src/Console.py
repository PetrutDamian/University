class Console(object):
    '''
    Clasa responsabila cu interfata utilizator 
    fiecare functionalitate trece mai intai prin "uiFunctionalitate" unde se construiesc parametri 
    si se convertesc variabilele in tipurile de date necesare apoi se apeleaza Service-ul unde se vor
    rula algoritmii, rezultatul va veni inapoi in functie  unde se va face afisarea
    '''

    def uiConversiiRapide(self, params):
        nr1 = params[0]
        b1 = int(params[1])
        b2 = int(params[2])
        nr2 = self.service.conversiiRapide(nr1, b1, b2)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b1) + ") = " + nr2 + "(" + str(b2) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiConversiiImpartiriSuccesive(self, params):
        nr1 = params[0]
        b1 = int(params[1])
        b2 = int(params[2])
        nr2 = self.service.conversiiImpartiriSuccesive(nr1, b1, b2)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b1) + ") = " + nr2 + "(" + str(b2) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiAdd(self, params):
        nr1 = params[0]
        nr2 = params[1]
        b = int(params[2])
        sum = self.service.add(nr1, nr2, b)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b) + ") + " + nr2 + "(" + str(b) + ") = " + sum + "(" + str(b) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiSub(self, params):
        nr1 = params[0]
        nr2 = params[1]
        b = int(params[2])
        dif = self.service.sub(nr1, nr2, b)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b) + ") - " + nr2 + "(" + str(b) + ") = " + dif + "(" + str(b) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiMul(self, params):
        nr = params[0]
        cif = params[1]
        b = int(params[2])
        prod = self.service.mul(nr, cif, b)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr + "(" + str(b) + ") * " + cif + "(" + str(b) + ") = " + prod + "(" + str(b) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiDiv(self, params):
        nr = params[0]
        cif = params[1]
        b = int(params[2])
        cat, rest = self.service.div(nr, cif, b)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr + "(" + str(b) + ") / " + cif + "(" + str(b) + ") = cat " + cat + " rest " + rest)
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiConversiiSubstitutie(self, params):
        nr1 = params[0]
        b1 = int(params[1])
        b2 = int(params[2])
        nr2 = self.service.conversiiSubstitutie(nr1, b1, b2)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b1) + ") = " + nr2 + "(" + str(b2) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def uiConversiiIntermediare(self, params):
        nr1 = params[0]
        b1 = int(params[1])
        b2 = int(params[2])
        nr2 = self.service.conversiiIntermediare(nr1, b1, b2)
        print(
            "------------------------------------------------------------------------------------------------------------")
        print(nr1 + "(" + str(b1) + ") = " + nr2 + "(" + str(b2) + ")")
        print(
            "------------------------------------------------------------------------------------------------------------")

    def __init__(self, service):
        self.service = service
        self.cmds = {"1": self.uiConversiiRapide, "2": self.uiConversiiImpartiriSuccesive, "5": self.uiAdd,
                     "6": self.uiSub, "7": self.uiMul, "8": self.uiDiv, "3": self.uiConversiiSubstitutie
            , "4": self.uiConversiiIntermediare}

    def mesaj(self):
        print(
            ''''
        Formatul unei linii de comenzi este : comanda/param1/param2/.../paramN
        Comenzi disponibile:
        1 - Conversii rapide  format:(1/nr/baza1/baza2) doar intre bazele 2 4 8 16
        2 - Conversie prin impartiri succesive format:(2/nr/baza1/baza2) baza1>baza2
        3 - Conversie prin substitutie format:(3/nr/baza1/baza2) baza1<baza2
        4 - Conversie prin baza intermediara format:(4/nr/baza1/baza2)
        5 - Adunarea a doua numere in aceeasi baza format:(5/nr1/nr2/baza) 
        6 - Scaderea a doua numere in aceeasi baza format:(6/nr1/nr2/baza) nr1>=nr2
        7 - Inmultirea cu o cifra format:(7/nr/cifra/baza)
        8 - Impartirea cu o cifra format:(8/nr/cifra/baza) cifra !=0
        ''')

    def run(self):
        print('''Aplicatie functioneaza pe baza liniei de comanda. O linie de comanda contine 
        numele comenzii si parametrii corespunzatori, toate separate prin "/"
        pentru a opri aplicatia introduceti o linie de comanda libera''')
        self.mesaj()

        x = input(">>")
        while x != "":
            cmd = x.split("/")
            if cmd[0] not in self.cmds:
                print("Comanda invalida!")
            else:
                params = cmd[1:]
                try:
                    self.cmds[cmd[0]](params)
                except:
                    print("Error")
            self.mesaj()
            x = input(">>")
