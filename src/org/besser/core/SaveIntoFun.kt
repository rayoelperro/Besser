package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
class SaveIntoFun(action: Parser, name: String, val elem: Elem? = null): Doing(action, action.levels) {

    val function = Fun(name, action.levels, action.levels.last())
    private var deep = 1

    fun save() {
        if (elem == null)
            action.levels.last()["@" + function.name] = Token(FUNCTION_TOKEN, function)
        else
            elem.methods += function
        action.changeDoing(DoingType.Execute)
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val cheks = tks.nameKeywords()
        when {
            cheks.lineStructure().joinToString(" ") == makeStructure("END") -> {
                deep--
                if (deep == 0){
                    save()
                } else {
                    function.lines += tks
                }
            }
            cheks.lineStructure().joinToString(" ") == makeStructure("END", "VARIABLE") -> {
                deep--
                if (deep == 0){
                    function.end_var = cheks[1].value as String
                    save()
                }
            }
            else -> {
                if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                function.lines += tks
            }
        }
        return Token("RES", "NONE")
    }
}