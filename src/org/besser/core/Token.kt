package org.besser.core

/**
 * Created by rayoe on 24/08/2018.
 */
data class Token<T>(val id: String, val value: T)

fun Array<Token<*>>.lineStructure(): Array<String> {
    val toret = mutableListOf<String>()
    this.forEach { toret += it.id }
    return toret.toTypedArray()
}

fun makeStructure(vararg args: String): String {
    return args.joinToString(" ")
}

fun Array<Token<*>>.nameOperators(): Array<Token<*>> {
    val toret = mutableListOf<Token<*>>()
    this.forEach {
        if (it.id == "OPERATOR")
            toret += Token(Operators_Name[Operators.indexOf(it.value)], it.value)
        else
            toret += it
    }
    return toret.toTypedArray()
}

fun Array<Token<*>>.nameKeywords(): Array<Token<*>> {
    val toret = mutableListOf<Token<*>>()
    this.forEach {
        if (it.id == "KEYWORD")
            toret += Token(Keywords_Name[Keywords.indexOf(it.value)], it.value)
        else
            toret += it
    }
    return toret.toTypedArray()
}

fun Array<Token<*>>.getVars(con: Context): Array<Token<*>> {
    val toret = mutableListOf<Token<*>>()
    this.forEach {
        if (it.id == VARIABLE_TOKEN) {
            if (con.containsKey(it.value)) {
                toret += con[it.value] as Token<*>
            } else {
                TOER("Reference to undeclared variable: ${it.value}")
            }
        } else {
            toret += it
        }
    }
    return toret.toTypedArray()
}

val Keywords = arrayOf("print", "read", "fun", "elem", "of", "self", "then", "end", "get", "set", "as", "import", "if", "else", "while", "when", "book", "for", "event", "repeat", "until", "on", "size", "isolated", "try", "except", "yield", "chan", "NUM", "STR", "BOOL", "ELEM", "ARRAY", "CHANNEL", "JBYTE", "JSHORT", "JINT", "JLONG", "JFLOAT", "JDOUBLE", "JNULL")
val Keywords_Name = arrayOf("PRINT", "READ", "FUN", "ELEM", "OF", "SELF", "THEN", "END", "GET", "SET", "AS", "IMPORT", "IF", "ELSE", "WHILE", "WHEN", "BOOK", "FOR", "EVENTK", "REPEAT", "UNTIL", "ON", "SIZE", "ISOLATED", "TRY", "EXCEPT", "YIELD", "CHAN", "NUMT", "STRT", "BOOLT", "ELEMT", "ARRAYT", "CHANT", "JBYTET", "JSHORTT", "JINTT", "JLONGT", "JFLOATT", "JDOUBLET", "JNULLT")

val Operators = arrayOf("+", "-", "*", "/", "%", "<", ">", "&", "|", "^", "(", ")", "=", "!", ":", ".")
val Operators_Name = arrayOf("PLUS", "MINUS", "TIMES", "DIVIDED", "MOD", "LESS", "GREATER", "AND", "OR", "XOR", "OPEN", "CLOSE", "EQUALS", "NOT", "COLON", "DOCK")

val Spaces = arrayOf(' ', ' ')

const val COMMENT_TOKEN = "#"

const val ID_TOKEN = "ID"
const val OPERATOR_TOKEN = "OPERATOR"
const val KEYWORD_TOKEN = "KEYWORD"
const val STRING_TOKEN = "STRING"
const val NUMBER_TOKEN = "NUMBER"
const val BOOL_TOKEN = "BOOL"
const val VARIABLE_TOKEN = "VARIABLE"
const val FUNCTION_TOKEN = "FUNCTION"
const val ELEMENT_TOKEN = "ELEMENT"
const val INSTANCE_TOKEN = "INSTANCE"
const val ARRAY_TOKEN = "ARRAY"
const val CHANNEL_TOKEN = "CHANNEL"
const val EVENT_TOKEN = "EVENT"
//JAVAVALS
const val JAVA_BYTE_TOKEN = "JBYTE"
const val JAVA_SHORT_TOKEN = "JSHORT"
const val JAVA_INT_TOKEN = "JINT"
const val JAVA_LONG_TOKEN = "JLONG"
const val JAVA_FLOAT_TOKEN = "JFLOAT"
const val JAVA_DOUBLE_TOKEN = "JDOUBLE"
const val JAVA_NULL_TOKEN = "JNULLT"