package org.besser.core

import java.io.File

/**
 * Created by rayoe on 13/09/2018.
 */
class BuiltIn : LinkedHashMap<String, Token<*>>() {
    abstract class BFun(name: String) : Fun(name, mutableListOf(Context()), Context())

    fun sizeExp(array: Array<*>, exp: Int): Boolean {
        return (array.size == exp)
    }

    val file: Token<*> = Token("FUNCTION", object : BFun("FILE") {
        override fun exec(args: Array<Token<*>>, instance: Instance?, yield_fun: Fun?): Token<*> {
            if (sizeExp(args, 1)) {
                return Token("CHANNEL", object : Channel(args[0]) {
                    val s = args[0].value as String
                    override fun get(): Token<*> {
                        return Token("STRING", File(s).readText())
                    }

                    override fun set(nv: Token<*>) {
                        File(s).writeText(s)
                    }
                })
            } else {
                throw BesserRunningTimeError("The expected size must be 1")
            }
        }
    })

    init {
        this["@FILE"] = file
    }
}