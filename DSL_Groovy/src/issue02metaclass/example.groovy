package issue02metaclass


Integer.metaClass.getCOOKIES = {
    return new Cookie(ammount: delegate)
}
class Cookie {
    Integer ammount
    String toString() {
        "$ammount cookies!" as String
    }
}
assert 5.COOKIES.toString() == "5 cookies!"
println 5.COOKIES

String.metaClass.question = {
  return "¿${delegate}?"
}

assert "como estas".question() == "¿como estas?"
println "como estas".question()


// La solucion al final del fichero... NO BAJAR!!! :-)





















































































////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////// String.metaClass.question = {
//////////////////////////////////////////////////////    return "¿${delegate}?"
///////////////////////////////////////////////////////}

