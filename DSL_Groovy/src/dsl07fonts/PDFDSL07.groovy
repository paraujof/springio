package dsl07fonts

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color


def dsl = new PDFGenerator("/home/mazinger/pelis.pdf")


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

/* ************* EJERCICIO 7 **************
Se añade la utilizacion de esas fuentes en cada uno de los elementos p() y text() con un atributo font

- Pista: modificar los metodos p() y text() para que acepten tambien un Map como parametro antes del,
  Closure (el Closure debe ir siempre el último)

- Para añadir una fuente dentro del p():
    p.setFont(fonts[atts.font?:'default']?:new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto

- Para añadir una fuente dentro del text():
    new Phrase(s, fonts[atts.font]


Descomentar y... trabajar!
 */

/*
dsl.build {

    fonts {
        title(name: "times-roman", size: 20, style: "bold underline")
        'default'(name: "helvetica", size: 10, style: "normal")
        special(name: "courier", size: 12, style: "italic", color:"blue")
    }


    p font:'title', "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text font:"special", "2011 con más fuerza. "

        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    p font: "special", {
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
    Map fonts = [:]
    def p(Closure closure) {

        Paragraph p = new Paragraph()
        p.setFont(fonts.default?:new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto
        p.setSpacingAfter(5);

        new ParagraphBuilder(parentParagraph:p).with(closure)

        parentDocument.add(p)
    }
    def p(String s) {
        p {
            text s
        }
    }
    def fonts(Closure closure) {
        new FontBuilder(fonts: fonts).with(closure)
    }
}

class FontBuilder {
    Map fonts
    def methodMissing(String name, argsArray) {
        def attributesMap = argsArray[0]
        Font font = new Font()
        font.setSize(attributesMap.size?:12)
        font.setFamily(attributesMap.name?:"helvetica")
        font.setStyle(attributesMap.style?:"normal")
        if (attributesMap.color != null) {
            Color awtColor = Color."${attributesMap.color}"
            font.setColor(awtColor.red, awtColor.green, awtColor.blue)
        }
        fonts[name] = font
    }
}


class ParagraphBuilder {
    Paragraph parentParagraph
    def text(String s) {
        parentParagraph.add(s)
    }
}






























