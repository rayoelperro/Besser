package org.besser.core

import java.io.File

/**
 * Created by rayoe on 04/09/2018.
 */
class GetFromFile(val elements: Array<Token<*>>, val file: String, val args : Array<Token<*>> = arrayOf<Token<*>>()) {
    fun implement(con : Context) {
        val levels : MutableList<Context> = mutableListOf()
        levels.add(Context(args, ContextType.Import))
        val lines: Array<String> = arrayOf(*(File(file).readLines(charset("UTF-8")).toTypedArray()))
        val lexic: Lexer = Lexer(lines)
        val parse: Parser = Parser(levels)
        parse.parse(lexic.getTokensPerLine() as Array<Array<Token<*>>>)
        for (e in elements) {
            val o = levels[0]["@" + (e.value as String)]
            if (o != null)
                con["@" + e.value] = o
        }
    }
}