package issue07delegate

Closure accessToV1 = {
    println v1
    metodoInterno()
}
def config = new Config().runInside(accessToV1)

class Config {

    def v1 = "hola"
    def metodoInterno() {
        println "interno"
    }
    def runInside(Closure c) {
        c.setDelegate(this)  // DESCOMENTAR ESTA LINE
        c()
    }
}


