package issue06mop



new PseudoGorm(table:'friends').findByName('Nina Meyers')


class PseudoGorm {

    String table

    def invokeMethod(String method, args) {
        if (method.startsWith("findBy")) {
            String field = method.substring(6)

            println "SELECT * FROM $table WHERE $field = '${args[0]}'"
        }
    }
}