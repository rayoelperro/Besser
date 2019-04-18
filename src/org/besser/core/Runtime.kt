package org.besser.core

/**
 * Created by rayoe on 05/09/2018.
 */
fun canCompile(arr : Array<Token<*>>) : Boolean {
    var r = true
    for (a in arr) {
        if (a.value is String) {
            if (a.value == COMPILER) {
                r = false
            }
        } else {
            throw BesserRunningTimeError("The compiler version array must be a StringArray")
        }
    }
    return r
}

const val COMPILER = "KTBSS"
var line = 0