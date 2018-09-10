package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
class SaveIntoElem(action : Parser, name : String): Doing(action, action.levels) {

    val element : Elem = Elem(name)
    var deep = 1

    fun appendVar(name : String, type : String) {
        element.fields[name] = type
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val chs = tks.nameKeywords()
        when (chs.lineStructure().joinToString(" ")) {
            makeStructure("SET", "ID", "AS", "STRT") -> appendVar(chs[1].value as String, "STRING")
            makeStructure("SET", "ID", "AS", "NUMT") -> appendVar(chs[1].value as String, "NUMBER")
            makeStructure("SET", "ID", "AS", "BOOLT") -> appendVar(chs[1].value as String, "BOOL")
            makeStructure("SET", "ID", "AS", "ELEMT") -> appendVar(chs[1].value as String, "INSTANCE")
            makeStructure("SET", "ID", "AS", "ARRAYT") -> appendVar(chs[1].value as String, "ARRAY")
            makeStructure("SET", "ID", "AS", "CHANT") -> appendVar(chs[1].value as String, "CHANNEL")
            makeStructure("END") -> {
                deep--
                if (deep == 0){
                    action.levels.last()["@" + element.name] = Token(ELEMENT_TOKEN, element)
                    action.changeDoing(DoingType.Execute)
                }
            }
            else -> TOER("Wrong elem declaration: " + tks.lineStructure().joinToString(" "))
        }
        return Token("RES", "NONE")
    }
}