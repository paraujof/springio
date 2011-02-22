package issue09builder


new BetterHtmlDsl().html {
    body {
        h1 "Titulo"
        p('class':'red', "Hola a todos")
        p('class':'blue') {
            img src:'groovy.gif', width: 200
        }
    }
}

class BetterHtmlDsl extends BuilderSupport {
    void setParent(Object parent, Object child) {}
    void nodeCompleted(Object parent, Object node) {
        println "</$node>"
    }
    Object createNode(Object name) {
        print "<$name>"
        name
    }
    Object createNode(Object name, Object value) {
        print "<$name>$value"
        name
    }
    Object createNode(Object name, Map atts) {
        print "<$name ${attList(atts)}>"
        name
    }
    Object createNode(Object name, Map atts, value) {
        print "<$name ${attList(atts)}>$value"
        name
    }
    String attList(atts) {
        def r = ""
        atts.each {
            r+=it.key+"='"+it.value+"' "
        }
        r.trim()
    }


}