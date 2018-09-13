package org.besser.core

/**
 * Created by rayoe on 05/09/2018.
 */
open class Channel(private var value : Token<*>) {
    open fun set(nv : Token<*>) {
        value = nv
    }
    open fun get() = value
}