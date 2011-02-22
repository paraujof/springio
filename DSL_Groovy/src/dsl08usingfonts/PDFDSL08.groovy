package dsl08usingfonts

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color


def dsl = new PDFGenerator("/tmp/pelis.pdf")


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

/* ************* EJERCICIO 8 **************
Se añade la utilizacion de esas fuentes en cada uno de los elementos
con el nombre del propio elemento

Pista:

    def methodMissing(String name, argsArray)

    Lo que antes era atts.font, ahora es name
    En argsArray es un Object[] que puede tener
        1 elemento con un Closure
            En este caso, se crea un map, con name como atributo "font"
        2 elementos, el primero es un Map, el segundo es un Closure
            En este caso, se modifica el map para que el elemento "font" sea el name

    invokeMethod("p", argsArray)



Descomentar y... trabajar!
 */

/*
dsl.build {

    fonts {
        title(name: "times-roman", size: 20, style: "bold underline")
        p(name: "helvetica", size: 10, style: "normal")
        foot(name: "courier", size: 12, style: "italic", color:"red")
    }


    title "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text font:"foot", "2011 con más fuerza. "

        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    foot {
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
    def p(Map atts = [:], Closure closure) {

        Paragraph p = new Paragraph()
        p.setFont(fonts[atts.font?:'default']?:new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto
        p.setSpacingAfter(5);

        new ParagraphBuilder(parentParagraph:p, fonts:fonts).with(closure)

        parentDocument.add(p)
    }
    def p(Map atts, String s) {
        p atts, {
            text s
        }
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
    Map fonts
    def text(Map atts = [:], String s) {
        if (fonts[atts.font]) {
            parentParagraph.add(new Phrase(s, fonts[atts.font]))
        } else {
            parentParagraph.add(s)
        }
    }
}




