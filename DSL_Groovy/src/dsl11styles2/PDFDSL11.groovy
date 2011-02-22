package dsl11styles2

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color


def dsl = new PDFGenerator("/tmp/pelis.pdf")

dsl.build {

    styles {
        title(name: "times-roman", size: 20, style: "bold underline", marginBottom: 20)
        p {
            name = "helvetica"
            size = 10
            style = "normal"
            marginLeft = 20
            marginRight = 10
            color = 'gray'
        }
        foot(name: "courier", size: 12, style: "italic", color: "blue")
    }


    title "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text style: "foot", "2011 con más fuerza. "

        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    foot {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }
}

/* ************* EJERCICIO 11 **************

Se hace que la definicion y generacion se hagan en momentos diferentes
Se añade un binding ademas, para que cada ejecucion pueda usar datos distintos.

Pistas:

    - PDFGenerator, en el metodo define() debe guardar el closure como atributo de clase
    y ya no debe tener los atributos document y file. Estos dos ultimos (document,file) deben
    ser variables locales que se crean, usan y se destruyen en build.

    - El metodo build debe aceptar un mapa con el binding. Este map se debe estar accesible
    desde PDFBuilderarrastrar y ParagraphBuilder.

    - Para que estas clases puedan usar los datos del binding coo si fueran suyas, podemos
    interceptar el acceso con:

    def propertyMissing(String name) {
        binding[name]
    }


Descomentar y... trabajar!
 */

/*
def dsl = new PDFGenerator()

dsl.define {

    styles {
        title(name: "times-roman", size: 20, style: "bold underline", marginBottom: 20)
        p {
            name = "helvetica"
            size = 10
            style = "normal"
            marginLeft = 20
            marginRight = 10
            color = 'gray'
        }
        foot(name: "courier", size: 12, style: "italic", color: "orange")
    }


    title "Bienvenido a SpringIO ${nombre}"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text style: "foot", "2011 con más fuerza. "

        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    foot {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }
}

dsl.build("/tmp/doc1.pdf", [nombre:"Alberto"])
dsl.build("/tmp/doc2.pdf", [nombre:"Luis"])

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
        new PDFBuilder(parentDocument: document).with(closure) // El with se hace con otro objeto
        document.close()
    }

}

class PDFBuilder {
    Document parentDocument
    Map styles = [:]

    def methodMissing(String name, argsArray) {
        if (argsArray[0] instanceof Map) {
            argsArray[0].style = name
        } else {
            def argsList = argsArray as java.util.List
            argsList.add(0, [style: name])
            argsArray = argsList as Object[]
        }
        invokeMethod("p", argsArray)
    }

    def p(Map atts = [:], Closure closure) {

        Paragraph p = new Paragraph()
        Style style = styles[atts.style ?: 'p']
        p.setFont(style?.font ?: new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto

        p.setSpacingBefore(style?.marginTop ?: 0)
        p.setSpacingAfter(style?.marginBottom ?: 5)
        p.setIndentationLeft(style?.marginLeft ?: 0)
        p.setIndentationRight(style?.marginRight ?: 0)

        new ParagraphBuilder(parentParagraph: p, styles: styles).with(closure)

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

    def styles(Closure closure) {
        new StyleBuilder(styles: styles).with(closure)
    }
}

class StyleBuilder {
    Map styles

    def methodMissing(String name, argsArray) {
        if (argsArray[0] instanceof Map) {
            def attributesMap = argsArray[0]
            createStyle(attributesMap, name)
        } else if (argsArray[0] instanceof Closure) {
            def closure = argsArray[0]
            def attributesMap = [:]
            attributesMap.with(closure)
            createStyle(attributesMap, name)
        }
    }

    private def createStyle(attributesMap, String name) {
        Font font = new Font()
        font.setSize(attributesMap.size ?: 12)
        font.setFamily(attributesMap.name ?: "helvetica")
        font.setStyle(attributesMap.style ?: "normal")
        if (attributesMap.color != null) {
            Color awtColor = Color."${attributesMap.color}"
            font.setColor(awtColor.red, awtColor.green, awtColor.blue)
        }
        styles[name] = new Style(font: font, atts: attributesMap)
    }
}


class ParagraphBuilder {
    Paragraph parentParagraph
    Map styles

    def text(Map atts = [:], String s) {
        if (styles[atts.style]?.font) {
            parentParagraph.add(new Phrase(s, styles[atts.style]?.font))
        } else {
            parentParagraph.add(s)
        }
    }
}

class Style {
    Map atts
    Font font

    def propertyMissing(String name) {
        return atts[name]
    }
}

