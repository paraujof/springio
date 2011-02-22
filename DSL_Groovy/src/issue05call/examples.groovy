package issue05call


class MyFunction {
    void call() {
        println "Executed!"
    }
}


def m = new MyFunction()
m.call()
m()

new MyFunction()()