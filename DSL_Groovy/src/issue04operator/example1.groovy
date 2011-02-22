package issue04operator

def c1 = new Cuenta(saldo:900)
def c2 = new Cuenta(saldo:480)

c1 << c2
assert c1.saldo == 1380
assert c2.saldo == 0

c1 >> c2
assert c1.saldo == 0
assert c2.saldo == 1380


c1 + 120
assert c1.saldo == 120


class Cuenta {
    int saldo

    def leftShift(o) {
        saldo += o.saldo
        o.saldo = 0
    }

    def rightShift(o) {
        o.saldo += saldo
        saldo = 0
    }


    // La solucion al final del fichero... NO BAJAR!!! :-)










































































/////////////////////////////////////////////////////////////////////////////////////    def plus(cant) {
/////////////////////////////////////////////////////////////////////////////////////        saldo += cant
/////////////////////////////////////////////////////////////////////////////////////    }


}