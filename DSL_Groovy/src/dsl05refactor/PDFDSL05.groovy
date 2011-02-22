package dsl05refactor

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily


def dsl = new PDFGenerator("/tmp/pelis.pdf")


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

/* ************* EJERCICIO 4 **************

Se añade la definicion de la fuente por defecto a usar en el documento
Ademas, se permite que los parrafos tengan solo un texto, sin necesidad de abrir una seccion

Descomentar y... trabajar!

- NO HAY Pista para: p("Bienvenidos a SpringIO")

- Pista para font:

    def font(Map attributesMap) {
        font = new Font()
        font.setSize(attributesMap.size?:12)
        font.setFamily(attributesMap.name?:"helvetica")
        font.setStyle(attributesMap.style?:"normal")
        if (attributesMap.color != null) {
            Color awtColor = Color."${attributesMap.color}"
            font.setColor(awtColor.red, awtColor.green, awtColor.blue)
        }
    }


*/

/*
dsl.build {

    font (name: "times-roman", size: 20, style: "bold underline")

    p  "Bienvenidos a SpringIO"

    font (name: "helvetica", size: 10, style: "normal")

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en 2011 con más fuerza. "
        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }

    font (name: "courier", size: 12, style: "italic", color:"blue")

    p {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }

}
*/

class PDFGenerator {

    Document document
    File file

    PDFGenerator(String filename) {
        document = new Document()
        file = new File(filename)
        file.delete()
    }

    def build(Closure closure) {
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
        new PDFBuilder(parentDocument:document).with(closure) // El with se hace con otro objeto
        document.close()
    }

}

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
