package dsl03dsl2

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily


def dsl = new PDFGenerator("/home/mazinger/pelis.pdf")

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


/* ************* EJERCICIO 3 **************

Pero, al incluir todos los metodos build(), p() y text() en la misma clase, son accesibles en
cualquier momento y en cualquier orden. Queremos que nuestro dsl no falle si el usuario
no hace un uso apropiado

Para corregir cada error, hay que meter validaciones (aunque ahora son pocas, podrian ser muchas)

Pista: buscar donde meter estas lineas que corrigen cada uno de los errores

    if (!document) return // Sin inicializar, E1
    if (p) return // Ya estamos en un p()     E4
    p = null                    // E4
    if (document) return // Ya hemos llamado a build anteriormente, E5
    if (!document) return // Sin inicializar, E2
    if (p == null) return // No hay p() definido, E3


Descomentar y... trabajar!

*/

/*
dsl.p {}                      // E1
dsl.text "ignorar"            // E2

dsl.build {
    text "ignorar"            // E3

    p {
        text "Bienvenidos a SpringIO"
    }

    build {}                  // E5

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        p {}                  // E4
        text "y de México, volvemos en 2011 con más fuerza."
        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        p {}                  // E4
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    p {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }
}

*/











class PDFGenerator {

    Document document
    File file
    Paragraph p

    PDFGenerator(String filename) {
        file = new File(filename)
        file.delete()
    }

    def build(Closure closure) {
        if (document) return //E5
        document = new Document()
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
        with(closure)
        document.close()
    }


    def p(Closure closure) {
        if (!document) return //E1

        p = new Paragraph()

        p.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL))
        p.setSpacingAfter(5);

        with(closure)

        document.add(p)

    }

    def text(String s) {
        if (!document) return //E2
        if (p == null) return //E2
        p.add(s)
    }



}
