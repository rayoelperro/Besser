package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
abstract class Doing(val action: Parser, val levels: MutableList<Context>) {
    abstract fun run(tks: Array<Token<*>>): Token<*>
}