package org.besser.core

/**
 * Created by rayoe on 04/09/2018.
 */
open class BesserSourceError(cause : String) : Throwable("Line $line: $cause")
class BesserRunningTimeError(cause: String) : BesserSourceError(cause)
class BesserAssertionError() : BesserSourceError("Assertion Error")

fun TOER(cause: String) : Nothing {
    throw BesserSourceError(cause)
}