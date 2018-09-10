package org.besser.core

/**
 * Created by rayoe on 24/08/2018.
 */
class Lexer(val lines: Array<String>) {
    fun getTokensPerLine() : Array<Array<Token<String>>> {
        val tok: MutableList<Array<Token<String>>> = mutableListOf()
        lines.forEachIndexed { index, _ ->
            if(!(lines[index].replace(" ", "").replace("  ", "").isEmpty()))
            {
                val sgl = getTokens(index)
                if (sgl.isNotEmpty())
                    tok += sgl
            }
        }
        return tok.toTypedArray()
    }

    fun getTokens(line: Int): Array<Token<String>> {
        val tok : MutableList<Token<String>> = mutableListOf()
        var act : String = ""
        var ins : Boolean = false
        var nom : Boolean = false
        lines[line].forEachIndexed { index, c ->
            if (nom) {
                return@forEachIndexed
            } else if (ins) {
                if(c == '\''){
                    tok += Token(STRING_TOKEN, act)
                    act = ""
                    ins = false
                }
                else
                    act += c
            } else if (c == '\'') {
                if(act.isNotEmpty()) {
                    val sng = getSingle(act)
                    tok += sng
                    act = ""
                }
                ins = true
            } else if (c in Spaces) {
                if(act.isNotEmpty()) {
                    val sng = getSingle(act)
                    tok += sng
                    act = ""
                }
            } else if (c.toString() in Operators) {
                if (act.isNotEmpty()){
                    val sng = getSingle(act)
                    tok += sng
                    act = ""
                }
                val scn = getSingle(c.toString())
                tok += scn
            } else if (c.toString() == COMMENT_TOKEN) {
                if (act.isNotEmpty()){
                    val sng = getSingle(act)
                    tok += sng
                    act = ""
                }
                nom = true
            } else {
                act += c
                if (index == lines[line].length - 1 && act.isNotEmpty()) {
                    val sng = getSingle(act)
                    tok += sng
                    act = ""
                }
            }
        }
        return tok.toTypedArray()
    }

    fun getSingle(key: String): Token<String> {
        val op = (if (key in Operators)
            OPERATOR_TOKEN
        else if(key in Keywords)
            KEYWORD_TOKEN
        else if (key.matches("-?\\d+(\\,\\d+)?".toRegex()))
            NUMBER_TOKEN
        else if (key == "true" || key == "false")
            BOOL_TOKEN
        else if (key[0] == '@')
            VARIABLE_TOKEN
        else
            ID_TOKEN
        )
        return Token(op, if (op == NUMBER_TOKEN) key.replace(",", ".") else key)
    }
}