package org.besser.compiler

import org.besser.core.Context
import org.besser.core.Lexer
import org.besser.core.Parser
import org.besser.core.Token
import java.io.File

/**
 * Created by rayoe on 24/08/2018.
 */

/* Expected:

    get (:'join_in_str') of 'STRINGS'

    elem myElem then
        set name as STR
        set age as NUM
        set canDrive as BOOL
    end

    to myElem
        fun show then
            save res
            if (self canDrive) then
                set res = (@join_in_str: (self name) ' is ' (self age) ' years old and can drive')
            else
                set res = (@join_in_str: (self name) ' is ' (self age) ' years old and can not drive')
            end
        end @res
    end

    fun createElem then
        set resp = (myElem: (get 0) 16 false)
    end @resp

    on ready then
        set result = ((@createElem: 'Alberto').show:)
    end

 */

fun main(args: Array<String>) {
    val levels: MutableList<Context> = mutableListOf()
    levels.add(Context(args))
    if (args.size == 2) {
        val lines: Array<String> =
                if (args[0] == "play") {
                    arrayOf(args[1])
                } else if (args[0] == "run") {
                    arrayOf(*(File(args[1]).readLines(charset("UTF-8")).toTypedArray()))
                } else {
                    TODO("Argument one just can be either 'run' or 'play'")
                }
        val lexic: Lexer = Lexer(lines)
        val parse: Parser = Parser(levels)
        parse.parse(lexic.getTokensPerLine() as Array<Array<Token<*>>>)
    } else if (args.isEmpty()) {
        val parse: Parser = Parser(levels)
        val file = true
        if (file) {
            val lines: Array<String> = arrayOf(*(File(readLine()).readLines(charset("UTF-8")).toTypedArray()))
            val lexic: Lexer = Lexer(lines)
            val parse: Parser = Parser(levels)
            parse.parse(lexic.getTokensPerLine() as Array<Array<Token<*>>>)
        } else {
            while (true) {
                val lexic: Lexer = Lexer(arrayOf(readLine() as String))
                //println(Arrays.deepToString(lexic.getTokensPerLine()))
                parse.parse(lexic.getTokensPerLine() as Array<Array<Token<*>>>)
            }
        }
    } else {
        TODO("Only 2 arguments required")
    }
}