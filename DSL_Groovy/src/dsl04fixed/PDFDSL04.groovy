package dsl04fixed

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily


def dsl = new PDFGenerator("/tmp/pelis.pdf")


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

/* ************* EJERCICIO 4 **************

Para corregir cada error, hay que meter validaciones (aunque ahora son pocas, podrian ser muchas)

Se hace una refactorizacion, creando nuevas clases para que cada nivel tenga una clase que de un
contexto y metodos distinos, de manera que usar algo fuera de su sitio, da un error de
metodo no encontrado, no un comportamiento anomalo.

El usuario se da por enterado siempre de la misma manera, nos ahorramos validaciones y los errores
mostrados son "naturales"

(El DSL no cambia)


Pista: añadir estas dos clases

    class PDFBuilder {
        Document parentDocument

        def p(Closure closure) {

            Paragraph p = new Paragraph()
            p.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL))
            p.setSpacingAfter(5);

            new ParagraphBuilder(parentParagraph:p).with(closure)

            parentDocument.add(p)
        }
    }

    class ParagraphBuilder {
        Paragraph parentParagraph
        def text(String s) {
            parentParagraph.add(s)
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
        if (document) return // Ya hemos llamado a build anteriormente, E5
        document = new Document()
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
        with(closure)
        document.close()
    }


    def p(Closure closure) {
        if (!document) return // Sin inicializar, E1
        if (p) return // Ya estamos en un p()     E4
        p = new Paragraph()

        p.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL))
        p.setSpacingAfter(5);

        with(closure)

        document.add(p)

        p = null                    // E4

    }

    def text(String s) {
        if (!document) return // Sin inicializar, E2
        if (p == null) return // No hay p() definido, E3
        p.add(s)
    }



}
