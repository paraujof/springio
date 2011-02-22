package issue08firstdsl




def dsl = new MiniDsl()

dsl.url("http://google.com", 2) {
    String path = "/tmp"
    download(cache:false, folder:"/tmp") {
        println "Descargando $url en $path"
    }
}

class MiniDsl {
    String url
    int peso
    def url(String url, int peso, Closure hacer) {
        this.url = url
        this.peso = peso
        with hacer
    }

    def download(Map config, Closure hacer) {
        with hacer
    }

}