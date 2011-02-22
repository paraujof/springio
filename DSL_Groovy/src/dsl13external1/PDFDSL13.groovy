package dsl13external1

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import java.awt.Color

def dsl = new PDFGenerator()

dsl.define("/tmp/definition1.groovy")


dsl.build("/tmp/doc1.pdf", [nombre:"Fernando"])
dsl.build("/tmp/doc2.pdf", [nombre:"Claudia"])

/* ************* EJERCICIO 13 **************

El script definition1.groovy debe tener codigo que tenga sentido dentro de un closure,
no puede tener imports o definiciones de clases porque fallaria, como definition2.

Probar con dsl.define("/tmp/definition2.groovy")

Intentar solucion estatica: que nuestro script contenga los metodos en tiempo de compilacion.

Una solucion es hacer que nuestro script herede de una clase que contenga los metodos
que necesitamos. Pero para que nuestro script herede de una clase nuestra, esta debe
ser tambien un script (es decir, que herede de Script) y que sea abstracta, para que
se pueda sobreescribir el metodo run()

Ahora tenemos una clase intermedia que debe implementar los metodos de PDFBuilder,
así que podemos usar la anotacion @Delegate, que transforma nuestra clase en tiempo
de compilacion, añadiendo a nuestra clase todos los metodos de la clase de la que delega.

Pista:

    Solo se cambia el PDFGenerator, el resto de clases se quedan igual

    PdfWriter.getInstance(document, new FileOutputStream(file))
    document.open()

    CompilerConfiguration configuration = new CompilerConfiguration(scriptBaseClass:PDFGeneratorScript.class.getName())
    GroovyShell shell = new GroovyShell(this.class.getClassLoader(), binding as Binding, configuration)
    PDFGeneratorScript script = shell.parse(text)
    script.setPdfBuilder(new PDFBuilder(parentDocument: document, binding:binding))
    script.run()

    document.close()


abstract class PDFGeneratorScript extends Script {
    @Delegate PDFBuilder pdfBuilder
}

*/

class PDFGenerator {

    Closure closure
    def define(String path) {
        String text = new File(path).text

        GroovyShell shell = new GroovyShell([dsl:this] as Binding)

        shell.evaluate(" dsl.define { $text }")


    }
    def define(Closure closure) {
        this.closure = closure
    }

    def build(String filename, binding = [:]) {
        Document document = new Document()
        File file = new File(filename)
        file.delete()
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
        new PDFBuilder(parentDocument: document, binding:binding).with(closure) // El with se hace con otro objeto
        document.close()
    }
}

class PDFBuilder {
    Document parentDocument
    Map styles = [:]
    Map binding = [:]

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

        new ParagraphBuilder(parentParagraph: p, styles: styles, binding: binding).with(closure)

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

    def propertyMissing(String name) {
        binding[name]
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
    Map binding

    def propertyMissing(String name) {
        binding[name]
    }

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

