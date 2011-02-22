package dsl06defaultfont

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color

def dsl = new PDFGenerator("/tmp/pelis.pdf")


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

/* ************* EJERCICIO 6 **************
Se añade la definicion de varias fuentes, con una por defecto llamada 'default'

- Pistas:
    PDFBuilder debe guardar todas las fuentes (en vez de una sola) en un Map
    Sacar el metodo font() como methodMissing en una nueva clase llamada FontBuilder
    No olvidar enviar todas las fuentes al FontBuilder, o hacer que el FontBuilder añada
    la fuente al mapa de todas las fuentes


Descomentar y... trabajar!
*/

/*
dsl.build {

    fonts {
        title(name: "times-roman", size: 20, style: "bold underline")
        'default'(name: "helvetica", size: 10, style: "normal")
        special(name: "courier", size: 12, style: "italic", color:"blue")
    }


    p  "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en 2011 con más fuerza. "
        text "En la siguiente edición vamos a tener dos días llenos de novedades "
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
    Font font
    def p(Closure closure) {

        Paragraph p = new Paragraph()
        p.setFont(font?:new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto
        p.setSpacingAfter(5);

        new ParagraphBuilder(parentParagraph:p).with(closure)

        parentDocument.add(p)
    }
    def p(String s) {
        p {
            text s
        }
    }
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
}

class ParagraphBuilder {
    Paragraph parentParagraph
    def text(String s) {
        parentParagraph.add(s)
    }
}


















































