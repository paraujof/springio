package issue03category

class RandomCategory{
    static String random(String self) {
        self[new Random().nextInt(self.size())]
    }
}

use (RandomCategory) {
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
    println "tallerdsl".random()
}



// La solucion al final del fichero... NO BAJAR!!! :-)

























































































////////////////////////////////////////////////////////////////////////////////class RandomCategory {
////////////////////////////////////////////////////////////////////////////////    static String random(String self) {
////////////////////////////////////////////////////////////////////////////////        self[new Random().nextInt(self.size())]
////////////////////////////////////////////////////////////////////////////////    }
////////////////////////////////////////////////////////////////////////////////}

