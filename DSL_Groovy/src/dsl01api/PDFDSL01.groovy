package dsl01api

import com.itextpdf.text.pdf.*
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily


/*
PDFGenerator es una clase normal que permite crear pdfs y añadirles parrafos
Probar a lanzar muchos parrafos para ver los saltos de pagina en el pdf generado
 */



/* ************* EJERCICIO 1 **************

Primer paso de conversion de un api a dsl: los metodo open() y close() desaparecen y
se convierten en un único metodo build() que acepta un closure y lo ejecuta, cambiando el
contexto de este closure a PDFGenerator (this)

Una de las ventajas de englobar nuestro closure en un metodo, es que podemos
hacer cosas antes de ejecutar el closure (open) y cosas al acabar (document.close(),
pero podria ser cualquier cosa, incluso devolver un valor)

Pista:



Descomentar y... trabajar!

*/
/*

dsl = new PDFGenerator("/home/mazinger/document.pdf");
dsl.build {

    p("Bienvenidos a SpringIO")

    p("""Después del gran éxito del año pasado en la edición de Madrid y de México, volvemos en 2011 con más fuerza. En la siguiente edición vamos a tener dos días llenos de novedades y sorpresas con to do lo relacionado con Spring, Groovy/Grails y Cloud.""")

    p("No pierdas la oportunidad de compartir conocimiento con los expertos y profesionales en este evento referente")

}
*/


class PDFGenerator {

    Document document
    File file

    PDFGenerator(String filename) {
        file = new File(filename)
        file.delete()
        open()
    }

    def open() {
        document = new Document()
        PdfWriter.getInstance(document, new FileOutputStream(file))
        document.open()
    }

    def close() {
        document.close()
    }


    def p(String text) {
        Paragraph p = new Paragraph()

        p.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL))
        p.setSpacingAfter(5);

        p.add(text);

        document.add(p)

    }



}
