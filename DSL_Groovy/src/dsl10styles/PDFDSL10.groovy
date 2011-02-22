package dsl10styles

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color


def dsl = new PDFGenerator("/tmp/pelis.pdf")

dsl.build {

    styles {
        title(name: "times-roman", size: 20, style: "bold underline", marginBottom: 20)
        p(name: "helvetica", size: 10, style: "normal", marginLeft: 20, marginRight: 80)
        foot(name: "courier", size: 12, style: "italic", color: "red")
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

/* ************* EJERCICIO 10 **************

Se cambia la manera de asignar valores a los estilos:
Ahora los estilos pueden ser definidos como un mapa, pero tambien como un Closure.
Dentro del Closure, las asignaciones que se hagan (es decir, var = valor), deben afectar
al estilo que se esta creando.


Pista:
    - Dado que los estilos se capturan con methodMissing(nane,args), para controlar
    si nos llega un mapa o un closure, debemos comprobar la clase que hay en el primer
    elemento del array args

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


Descomentar y... trabajar!
 */

/*
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
        def attributesMap = argsArray[0]
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

