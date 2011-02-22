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
