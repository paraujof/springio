package dsl02dsl

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily


/*
Tricks: Una linea puede acabar con \ para evitar los retornos de carro
        Parentesis en las llamadas
*/

def dsl = new PDFGenerator("/home/mazinger/pelis.pdf")




/* ************* EJERCICIO 2 **************

Ahora los p() son closures que contienen llamadas a text()
Es necesario sacar la variable local p del metodo p() como atributo de clase,                    def v1 ="adios!!!"

para que cada llamada a text() puede localizar cual es el parrafo actual y
añadir el texto

Pista:

    def text(String s) {
        p.add(s)
    }


Descomentar y... trabajar!

*/

dsl.build {
    p {
        text "Bienvenidos a SpringIO"
    }
    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en 2011 con más fuerza."
        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    p {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }
}


class PDFGenerator {

    Document document
    File file

    PDFGenerator(String filename) {
        file = new File(filename)
        file.delete()
    }

    def build(Closure closure) {
        document = new Document()
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
        with(closure)
        document.close()
    }

    Paragraph p

    def p(Closure cl){
        p = new Paragraph()
        with(cl)
        document.add(p)

    }
    def p(String text) {
        p = new Paragraph()

        p.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL))
        p.setSpacingAfter(5);

        p.add(text);

        document.add(p)

    }

    def text(String s) {
        p.add(s)
    }


}
