package dsl09usingfonts2

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color


def dsl = new PDFGenerator("/tmp/pelis.pdf")

dsl.build {

    fonts {
        title(name: "times-roman", size: 20, style: "bold underline")
        p(name: "helvetica", size: 10, style: "normal")
        foot(name: "courier", size: 12, style: "italic", color: "red")
    }


    title "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text font: "foot", "2011 con más fuerza. "

        text "En la siguiente edición vamos a tener dos días llenos de novedades "
        text "y sorpresas con todo lo relacionado con Spring, Groovy/Grails y Cloud."
    }
    foot {
        text "No pierdas la oportunidad de compartir conocimiento con los "
        text "expertos y profesionales en este evento referente"
    }

}

/* ************* EJERCICIO 9 ************** OPCIONAL!!!!!!!

Se cambian los fonts con nuevos valores para los parrafos solo.

Hay que crear una clase style que contenga un Font con los atributos del Font. Los atributos
de parrafo solo se usan en p()

- Pista:
    1 Crear la clase
        class Style {
            Map atts
            Font font

            def propertyMissing(String name) {
                return atts[name]
            }
        }

    2 Cambiar FontBuilder por StyleBuilder. Añadir el estilo
        styles[name] = new Style(font: font, atts: attributesMap)

    3 En ParagraphBuilder, la condicion chequea que exista style, pero usa font
        if (styles[atts.style]?.font) {
            parentParagraph.add(new Phrase(s, styles[atts.style]?.font))
        }

    4 en p()

        Style style = styles[atts.style ?: 'p']
        p.setFont(style?.font ?: new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto

        p.setSpacingBefore(style?.marginTop ?: 0)
        p.setSpacingAfter(style?.marginBottom ?: 5)
        p.setIndentationLeft(style?.marginLeft ?: 0)
        p.setIndentationRight(style?.marginRight ?: 0)



Descomentar y... trabajar!
 */

/*
dsl.build {

    styles {
        title(name: "times-roman", size: 20, style: "bold underline", marginBottom:20)
        p(name: "helvetica", size: 10, style: "normal", marginLeft:20, marginRight:80)
        foot(name: "courier", size: 12, style: "italic", color:"red")
    }


    title "Bienvenidos a SpringIO"

    p {
        text "Después del gran éxito del año pasado en la edición de Madrid "
        text "y de México, volvemos en "

        text style:"foot", "2011 con más fuerza. "

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
    Map fonts = [:]

    def methodMissing(String name, argsArray) {
        if (argsArray[0] instanceof Map) {
            argsArray[0].font = name
        } else {
            def argsList = argsArray as java.util.List
            argsList.add(0, [font: name])
            argsArray = argsList as Object[]
        }
        invokeMethod("p", argsArray)
    }

    def p(Map atts = [:], Closure closure) {

        Paragraph p = new Paragraph()
        p.setFont(fonts[atts.font ?: 'p'] ?: new Font(FontFamily.HELVETICA, 12, Font.NORMAL)) // Font por defecto
        p.setSpacingAfter(5);

        new ParagraphBuilder(parentParagraph: p, fonts: fonts).with(closure)

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
        font.setSize(attributesMap.size ?: 12)
        font.setFamily(attributesMap.name ?: "helvetica")
        font.setStyle(attributesMap.style ?: "normal")
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
