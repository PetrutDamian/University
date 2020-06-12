class Service(object):
    '''
    Clasa responsabila cu executarea functionalitatilor aplicatiei
    '''

    def __init__(self):
        pass

    def div(self, nr, cif, b):
        '''
        calculeaza catul si restul impartirii lui nr la cif in baza b 
        returneaza cele 2 numere sub forma de string
        '''
        from src.Auxiliare import cifra, litera
        cat = ""
        t = 0
        digit2 = cifra(cif)
        # incepem algoritmul
        while nr != "":
            digit1 = cifra(nr[0])
            total = t * b + digit1
            cat = cat + litera(total // digit2)
            t = total % digit2
            nr = nr[1:]
        rest = litera(t)
        # eliminam zerourile nesemnificative
        while cat != "0" and cat[0] == '0':
            cat = cat[1:]
        return cat, rest

    def mul(self, nr, cif, b):
        '''
        calculeaza inmultirea lui nr cu cifra cif in baza b 
        returneaza produsul sub forma de string
        '''
        from src.Auxiliare import cifra, litera
        digit2 = cifra(cif)
        rez = ""
        t = 0  # initializam restul
        # incepem algoritmul
        while nr != "":
            digit1 = cifra(nr[-1])
            prod = digit1 * digit2 + t
            rest = prod % b
            rez = litera(rest) + rez
            t = prod // b
            nr = nr[:-1]
        # tratam cazul in care exista transport la final
        while t != 0:
            rest = t % b
            rez = litera(rest) + rez
            t = t // b
        # eliminam zerourile nesemnificative
        while rez != "0" and rez[0] == '0':
            rez = rez[1:]
        return rez

    def sub(self, nr1, nr2, b):
        '''
        Calculeaza diferenta a douna numere in aceeasi baza
        returneaza diferenta reprezentata in string
        '''
        from src.Auxiliare import cifra, litera
        # completam cu zerouri nesemnificative daca numerel nu au acelasi numar de cifre
        if len(nr1) < len(nr2):
            nr1 = "0" * (len(nr2) - len(nr1)) + nr1
        elif len(nr1) > len(nr2):
            nr2 = "0" * (len(nr1) - len(nr2)) + nr2
        t = 0  # initializam transportul
        rez = ""
        while nr2 != "":
            digit1 = cifra(nr1[-1])
            digit2 = cifra(nr2[-1])
            dif = digit1 - digit2 + t
            t = 0
            if dif < 0:
                t = -1
                dif = dif + b
            rez = litera(dif) + rez
            nr1 = nr1[:-1]
            nr2 = nr2[:-1]
            # eliminam zerourile nesemnificative
        while rez != "0" and rez[0] == '0':
            rez = rez[1:]
        return rez

    def add(self, nr1, nr2, b):
        '''
        Face adunarea dintre 2 numere in aceeasi baza
        returneaza suma reprezentata in string
        '''
        from src.Auxiliare import cifra, litera
        # completam cu zerouri nesemnificative daca numerel nu au acelasi numar de cifre
        if len(nr1) < len(nr2):
            nr1 = "0" * (len(nr2) - len(nr1)) + nr1
        elif len(nr1) > len(nr2):
            nr2 = "0" * (len(nr1) - len(nr2)) + nr2
        t = 0  # initializam transportul
        rez = ""
        # incepem algoritmul de adunare
        while nr1 != "":
            digit1 = cifra(nr1[-1])
            digit2 = cifra(nr2[-1])
            sum = digit1 + digit2 + t
            rest = sum % b
            t = sum // b
            rez = litera(rest) + rez
            nr1 = nr1[:-1]
            nr2 = nr2[:-1]
        # tratam cazul in care mai ramane transport
        while t != 0:
            rest = t % b
            t = t // b
            rez = litera(rest) + rez
        return rez

    def conversiiIntermediare(self, nr, b1, b2):
        '''
        converteste nr din baza 1 in baza2 trecand prin baza intermediara 10
        '''
        # prima data ajungem in baza 10 prin substitutie
        from src.Auxiliare import cifra, litera
        power = 1
        rez = ""
        t = 0
        while nr != "":
            digit1 = cifra(nr[-1])
            t = t + power * digit1
            power = power * b1
            nr = nr[:-1]
        # t contine acum nr in baza 10
        # procedam mai departe cu impartiri succesive
        while t != 0:
            r = t % b2
            t = t // b2
            rez = litera(r) + rez
        return rez

    def conversiiSubstitutie(self, nr, b1, b2):
        '''
        converteste nr de la baza b1 la baza b2 prin substitutie
        
        '''
        from src.Auxiliare import litera, cifra
        rez = ""
        t = "0"
        while nr != "":
            t = self.mul(t, litera(b1), b2)
            t = self.add(t, nr[0], b2)
            nr = nr[1:]
        return t

    def conversiiImpartiriSuccesive(self, nr, b1, b2):
        '''
        converteste nr de la baza b1 la baza b2 prin impartiri succesive
        '''
        from src.Auxiliare import cifra, litera
        rez = ""
        while nr != "0":
            cat, rest = self.div(nr, litera(b2), b1)
            rez = rest + rez
            nr = cat
        return rez

    def conversiiRapide(self, nr1, b1, b2):
        '''
        face conversia rapida a lui nr1 din baza b1 in baza b2, b1,b2 apartin {2,4,8,16}
        '''
        tabela_hexa = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"]
        tabela_4cifre = ["0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010",
                         "1011", "1100", "1101", "1110", "1111"]
        tabela_3cifre = ["000", "001", "010", "011", "100", "101", "110", "111"]
        tabela_2cifre = ["00", "01", "10", "11"]
        tabela2_16 = {"0000": "0", "0001": "1", "0010": "2", "0011": "3", "0100": "4", "0101": "5", "0110": "6",
                      "0111": "7", "1000": "8", "1001": "9", "1010": "A", "1011": "B", "1100": "C", "1101": "D",
                      "1110": "E", "1111": "F"}
        tabela2_8 = {"000": "0", "001": "1", "010": "2", "011": "3", "100": "4", "101": "5", "110": "6",
                     "111": "7"}
        tabela2_4 = {"00": "0", "01": "1", "10": "2", "11": "3"}
        nr_baza2 = ""
        len_nr1 = len(nr1)
        # transformarea in baza 2
        if b1 == 2:
            nr_baza2 = nr1
        if b1 != 2:
            while nr1 != "":
                cifra = nr1[-1]
                for i in range(0, 16):
                    if tabela_hexa[i] == cifra:
                        if b1 == 16:
                            nr_baza2 = tabela_4cifre[i] + nr_baza2
                        elif b1 == 8:
                            nr_baza2 = tabela_3cifre[i] + nr_baza2
                        elif b1 == 4:
                            nr_baza2 = tabela_2cifre[i] + nr_baza2
                        nr1 = nr1[:-1]
        # completarea cu zerouri, daca e necesar
        if b2 != 2:
            if b2 == 16:
                nr = 4
            elif b2 == 8:
                nr = 3
            elif b2 == 4:
                nr = 2
            if len(nr_baza2) % nr != 0:
                nr_baza2 = "0" * (nr - len(nr_baza2) % nr) + nr_baza2
        # transformarea in a doua baza
        if b2 != 2:
            nr2 = ""
            while nr_baza2 != "":
                grup = nr_baza2[:nr]
                nr_baza2 = nr_baza2[nr:]
                if b2 == 4:
                    nr2 = nr2 + tabela2_4[grup]
                elif b2 == 8:
                    nr2 = nr2 + tabela2_8[grup]
                elif b2 == 16:
                    nr2 = nr2 + tabela2_16[grup]
            # eliminam zerourile nesemnificative
            while nr2[0] == "0" and nr2 != "0":
                nr2 = nr2[1:]
                # returnam numarul, sub forma de string, convertit la baza a doua
            return nr2
        else:
            # eliminam zerourile nesemnificative
            while nr_baza2[0] == "0" and nr_baza2 != "0":
                nr_baza2 = nr_baza2[1:]
                # returnam nuarul, sub forma de string, convertit la baza a doua
            return nr_baza2
