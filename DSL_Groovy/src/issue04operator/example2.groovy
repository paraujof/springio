package issue04operator


def lm = new LimitedList(max:2, list:["a","b","c","d"])

println lm[0]
println lm[1]
try {
    lm[2]
} catch (e) {
    println "Comportamiento esperado: se ha usado LimitedLit mas de dos veces"
}


class LimitedList {
    int max = 10
    int used = 0
    def list = []


    // La solucion al final del fichero... NO BAJAR!!! :-)
































































































////////////////////////////////////////////////////////////////////    def getAt(v) {
////////////////////////////////////////////////////////////////////        used++
////////////////////////////////////////////////////////////////////        if (used > max) throw new RuntimeException("Usado demasiadas veces!")
////////////////////////////////////////////////////////////////////        return list[v]
////////////////////////////////////////////////////////////////////    }

}