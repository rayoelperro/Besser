package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
class Execute(action: Parser): Doing(action, action.levels) {
    override fun run(tks: Array<Token<*>>): Token<*> {
        //println(Arrays.deepToString(tks))
        return when (tks.lineStructure().joinToString(" ")) {
        //SINGLE TYPES
            makeStructure("NUMBER") -> tks[0]
            makeStructure("STRING") -> tks[0]
            makeStructure("BOOL") -> tks[0]
            makeStructure("INSTANCE") -> tks[0]
            makeStructure("ARRAY") -> tks[0]
            makeStructure("CHANNEL") -> tks[0]
        //REPEAT
            makeStructure("REPEAT", "NUMBER", "THEN") -> {
                action.changeDoing((tks[1].value as String).toDouble().toInt(), null)
                Token("RES", "NONE")
            }
            makeStructure("REPEAT", "NUMBER", "AS", "ID", "THEN") -> {
                action.changeDoing((tks[1].value as String).toDouble().toInt(), tks[3].value as String)
                Token("RES", "NONE")
            }
        //CHANNEL
            makeStructure("CHAN", "NUMBER") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHAN", "STRING") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHAN", "BOOL") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHAN", "INSTANCE") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHAN", "ARRAY") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHAN", "CHANNEL") -> {
                Token(CHANNEL_TOKEN, Channel(tks[1]))
            }
            makeStructure("CHANNEL", "DOCK", "GET") -> {
                (tks[0].value as Channel).get()
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "NUMBER") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "STRING") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "BOOL") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "INSTANCE") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "ARRAY") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
            makeStructure("CHANNEL", "DOCK", "SET", "EQUALS", "CHANNEL") -> {
                (tks[0].value as Channel).set(tks[4])
                Token("RES", "NONE")
            }
        //TRY
            makeStructure("TRY", "THEN") -> {
                action.changeDoing(DoingType.TryBlock)
                Token("RES", "NONE")
            }
            makeStructure("EXCEPT", "EQUALS", "STRING") -> {
                throw BesserRunningTimeError(tks[2].value as String)
            }
            makeStructure("EXCEPT", "IF", "BOOL") -> {
                if ((tks[2].value as String).toBoolean())
                    throw BesserAssertionError()
                else
                    Token("RES", "NONE")
            }
            makeStructure("EXCEPT", "ON", "ARRAY") -> {
                if (canCompile(tks[2].value as Array<Token<*>>))
                    Token("RES", "NONE")
                else
                    throw BesserIncompatibleCompilerError()
            }
        //BLOCKS
            makeStructure("THEN") -> {
                action.changeDoing(DoingType.Block)
                Token("RES", "NONE")
            }
            makeStructure("ISOLATED", "THEN") -> {
                action.changeDoing(arrayOf())
                Token("RES", "NONE")
            }
            makeStructure("ISOLATED", "ARRAY", "THEN") -> {
                action.changeDoing(tks[1].value as Array<Token<*>>)
                Token("RES", "NONE")
            }
        //ON
            makeStructure("ON", "EVENT", "THEN") -> {
                action.changeDoing(tks[1].value as Event)
                Token("RES", "NONE")
            }
        //EVENT
            makeStructure("EVENTK", "ID", "OF", "INSTANCE", "REPEAT", "ID", "UNTIL", "ID") -> {
                val ev = Event(tks[3].value as Instance, tks[5].value as String, tks[7].value as String)
                action.levels.last()["@" + tks[1].value as String] = Token(EVENT_TOKEN, ev)
                Token("RES", "NONE")
            }
        //WHEN
            makeStructure("WHEN", "THEN") -> {
                action.changeDoing(DoingType.WhenCondition)
                Token("RES", "NONE")
            }
        //FOR
            makeStructure("FOR", "ID", "OF", "ARRAY", "THEN") -> {
                action.changeDoing(tks[1].value as String, tks[3].value as Array<Token<*>>)
                Token("RES", "NONE")
            }
        //WHILE
            makeStructure("WHILE", "BOOL", "THEN") -> {
                action.changeDoing(DoingType.WhileLoop, (tks[1].value as String).toBoolean())
                Token("RES", "NONE")
            }
        //IF
            makeStructure("IF", "BOOL", "THEN") -> {
                action.changeDoing(DoingType.IfCondition, (tks[1].value as String).toBoolean())
                Token("RES", "NONE")
            }
        //IMPORT
            makeStructure("IMPORT", "ARRAY") -> {
                Loader(tks[1].value as Array<Token<*>>, levels)
                Token("RES", "NONE")
            }
            makeStructure("IMPORT", "ARRAY", "OF", "STRING") -> {
                Loader(tks[3].value as String, tks[1].value as Array<Token<*>>, levels)
                Token("RES", "NONE")
            }
        //READ
            makeStructure("READ", "STRING") -> {
                print(tks[1].value)
                Token("STRING", readLine())
            }
            makeStructure("READ") -> Token("STRING", readLine())
        //PRINT
            makeStructure("PRINT", "STRING") -> {
                println(tks[1].value)
                Token("RES", "NONE")
            }
            makeStructure("PRINT", "BOOL", "STRING") -> {
                if ((tks[1].value as String).toBoolean())
                    println(tks[2].value)
                else
                    print(tks[2].value)
                Token("RES", "NONE")
            }
        //GET
            makeStructure("GET", "NUMBER") -> levels.last().arguments[(tks[1].value as String).toDouble().toInt()]
            makeStructure("GET", "ARRAY", "OF", "STRING") -> {
                GetFromFile(tks[1].value as Array<Token<*>>, tks[3].value as String)
                        .implement(levels.last())
                Token("RES", "NONE")
            }
            makeStructure("GET", "ARRAY", "OF", "STRING", "SET", "ARRAY") -> {
                GetFromFile(tks[1].value as Array<Token<*>>, tks[3].value as String, tks[5].value as Array<Token<*>>)
                        .implement(levels.last())
                Token("RES", "NONE")
            }
        //BOOK
            makeStructure("BOOK", "ID") -> {
                levels.last()["@" + tks[1].value as String] = Token("RES", "NONE")
                Token("RES", "NONE")
            }
        //SET
            makeStructure("SET", "ID", "EQUALS", "NUMBER") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
            makeStructure("SET", "ID", "EQUALS", "STRING") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
            makeStructure("SET", "ID", "EQUALS", "BOOL") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
            makeStructure("SET", "ID", "EQUALS", "INSTANCE") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
            makeStructure("SET", "ID", "EQUALS", "ARRAY") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
            makeStructure("SET", "ID", "EQUALS", "CHANNEL") -> {
                levels.last()["@" + tks[1].value as String] = tks[3]
                Token("RES", "NONE")
            }
        //AS
            makeStructure("STRING", "AS", "NUMT") -> {
                Token("NUMBER", tks[0].value)
            }
            makeStructure("BOOL", "AS", "NUMT") -> {
                Token("NUMBER", tks[0].value)
            }
            makeStructure("NUMBER", "AS", "STRT") -> {
                Token("STRING", tks[0].value)
            }
            makeStructure("BOOL", "AS", "STRT") -> {
                Token("STRING", tks[0].value)
            }
            makeStructure("NUMBER", "AS", "BOOLT") -> {
                Token("BOOL", tks[0].value)
            }
            makeStructure("STRING", "AS", "BOOLT") -> {
                Token("BOOL", tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JBYTET") -> {
                Token(JAVA_BYTE_TOKEN, tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JSHORT") -> {
                Token(JAVA_SHORT_TOKEN, tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JINTT") -> {
                Token(JAVA_INT_TOKEN, tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JLONGT") -> {
                Token(JAVA_LONG_TOKEN, tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JFLOATT") -> {
                Token(JAVA_FLOAT_TOKEN, tks[0].value)
            }
            makeStructure("NUMBER", "AS", "JDOUBLET") -> {
                Token(JAVA_DOUBLE_TOKEN, tks[0].value)
            }
            makeStructure("GET", "AS", "ARRAYT") -> {
                Token("ARRAY", levels.last().arguments.toTypedArray())
            }
        //PLUS
            makeStructure("NUMBER", "PLUS", "NUMBER") -> {
                Token("NUMBER", ((tks[0].value as String).toDouble() + (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "PLUS", "STRING") -> {
                Token("STRING", tks[0].value as String + tks[2].value as String)
            }
            makeStructure("ARRAY", "PLUS", "NUMBER") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.add(tks[2])
                Token("ARRAY", ar.toTypedArray())
            }
            makeStructure("ARRAY", "PLUS", "STRING") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.add(tks[2])
                Token("ARRAY", ar.toTypedArray())
            }
            makeStructure("ARRAY", "PLUS", "BOOL") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.add(tks[2])
                Token("ARRAY", ar.toTypedArray())
            }
            makeStructure("ARRAY", "PLUS", "INSTANCE") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.add(tks[2])
                Token("ARRAY", ar.toTypedArray())
            }
            makeStructure("ARRAY", "PLUS", "ARRAY") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.add(tks[2])
                Token("ARRAY", ar.toTypedArray())
            }
        //MINUS
            makeStructure("MINUS", "NUMBER") -> {
                Token("NUMBER", ((tks[1].value as String).toDouble() * -1).toString())
            }
            makeStructure("NUMBER", "MINUS", "NUMBER") -> {
                Token("NUMBER", ((tks[0].value as String).toDouble() - (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "MINUS", "STRING") -> {
                Token("STRING", (tks[0].value as String).replace(tks[2].value as String, ""))
            }
            makeStructure("STRING", "MINUS", "NUMBER") -> {
                Token("STRING", (tks[0].value as String).substring(0, (tks[0].value as String).length - (tks[2].value as String).toInt()))
            }
            makeStructure("NUMBER", "MINUS", "STRING") -> {
                Token("STRING", (tks[2].value as String).substring((tks[0].value as String).toInt()))
            }
            makeStructure("ARRAY", "MINUS", "NUMBER") -> {
                val ar = (tks[0].value as Array<Token<*>>).toMutableList()
                ar.removeAt((tks[2].value as String).toDouble().toInt())
                Token("ARRAY", ar.toTypedArray())
            }
        //TIMES
            makeStructure("NUMBER", "TIMES", "NUMBER") -> {
                Token("NUMBER", ((tks[0].value as String).toDouble() * (tks[2].value as String).toDouble()).toString())
            }
        //DIVIDED
            makeStructure("NUMBER", "DIVIDED", "NUMBER") -> {
                Token("NUMBER", ((tks[0].value as String).toDouble() / (tks[2].value as String).toDouble()).toString())
            }
        //MOD
            makeStructure("NUMBER", "MOD", "NUMBER") -> {
                Token("NUMBER", ((tks[0].value as String).toDouble() % (tks[2].value as String).toDouble()).toString())
            }
        //LESS
            makeStructure("NUMBER", "LESS", "NUMBER") -> {
                Token("BOOL", ((tks[0].value as String).toDouble() < (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "LESS", "STRING") -> {
                Token("BOOL", ((tks[0].value as String).length < (tks[2].value as String).length).toString())
            }
        //GREATER
            makeStructure("NUMBER", "GREATER", "NUMBER") -> {
                Token("BOOL", ((tks[0].value as String).toDouble() > (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "GREATER", "STRING") -> {
                Token("BOOL", ((tks[0].value as String).length > (tks[2].value as String).length).toString())
            }
        //AND
            makeStructure("BOOL", "AND", "BOOL") -> {
                Token("BOOL", ((tks[0].value as String).toBoolean() && (tks[2].value as String).toBoolean()).toString())
            }
        //OR
            makeStructure("BOOL", "OR", "BOOL") -> {
                Token("BOOL", ((tks[0].value as String).toBoolean() || (tks[2].value as String).toBoolean()).toString())
            }
        //XOR
            makeStructure("BOOL", "XOR", "BOOL") -> {
                Token("BOOL", ((tks[0].value as String).toBoolean() xor (tks[2].value as String).toBoolean()).toString())
            }
        //EQUALS
            makeStructure("NUMBER", "EQUALS", "NUMBER") -> {
                Token("BOOL", ((tks[0].value as String).toDouble() == (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "EQUALS", "STRING") -> {
                Token("BOOL", ((tks[0].value as String) == (tks[2].value as String)).toString())
            }
            makeStructure("BOOL", "EQUALS", "BOOL") -> {
                Token("BOOL", ((tks[0].value as String).toBoolean() == (tks[2].value as String).toBoolean()).toString())
            }
            makeStructure("INSTANCE", "EQUALS", "INSTANCE") -> {
                Token("BOOL", (tks[0].value as Instance) == (tks[2].value as Instance))
            }
        //NOT
            makeStructure("NUMBER", "NOT", "NUMBER") -> {
                Token("BOOL", ((tks[0].value as String).toDouble() != (tks[2].value as String).toDouble()).toString())
            }
            makeStructure("STRING", "NOT", "STRING") -> {
                Token("BOOL", ((tks[0].value as String) != (tks[2].value as String)).toString())
            }
            makeStructure("BOOL", "NOT", "BOOL") -> {
                Token("BOOL", ((tks[0].value as String).toBoolean() != (tks[2].value as String).toBoolean()).toString())
            }
            makeStructure("NOT", "BOOL") -> {
                Token("BOOL", (!(tks[1].value as String).toBoolean()).toString())
            }
        //FUN
            makeStructure("FUN", "ID", "THEN") -> {
                action.changeDoing(DoingType.SaveIntoFun, tks[1].value as String)
                Token("RES", "NONE")
            }
            makeStructure("FUN", "ID", "OF", "ELEMENT", "THEN") -> {
                action.changeDoing(tks[1].value as String, tks[3].value as Elem)
                Token("RES", "NONE")
            }
        //ELEM
            makeStructure("ELEM", "ID", "THEN") -> {
                action.changeDoing(DoingType.SaveIntoElem, tks[1].value as String)
                Token("RES", "NONE")
            }
            makeStructure("SELF", "ID") -> {
                if (levels.last().self != null) {
                    (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                } else {
                    TOER("Trying to access to unprovided self arguments")
                }
            }
            makeStructure("SELF", "ID", "EQUALS", "NUMBER") -> {
                if (levels.last().self != null) {
                    val t = (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                    if (t.id == tks[3].id)
                        (levels.last().self as Instance).properties[tks[1].value as String] = tks[3]
                    else
                        TOER("Wrong type ${t.id} != ${tks[3].id}")
                } else {
                    TOER("Trying to access to unprovided self arguments")
                }
                Token("RES", "NONE")
            }
            makeStructure("SELF", "ID", "EQUALS", "STRING") -> {
                if (levels.last().self != null) {
                    val t = (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                    if (t.id == tks[3].id)
                        (levels.last().self as Instance).properties[tks[1].value as String] = tks[3]
                    else
                        TOER("Wrong type ${t.id} != ${tks[3].id}")
                } else {
                    TOER("Trying to access to unprovided self arguments")
                }
                Token("RES", "NONE")
            }
            makeStructure("SELF", "ID", "EQUALS", "BOOL") -> {
                if (levels.last().self != null) {
                    val t = (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                    if (t.id == tks[3].id)
                        (levels.last().self as Instance).properties[tks[1].value as String] = tks[3]
                    else
                        TOER("Wrong type ${t.id} != ${tks[3].id}")
                } else {
                    TOER("Trying to access to unprovided self arguments")
                }
                Token("RES", "NONE")
            }
            makeStructure("SELF", "ID", "EQUALS", "INSTANCE") -> {
                if (levels.last().self != null) {
                    val t = (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                    if (t.id == tks[3].id)
                        (levels.last().self as Instance).properties[tks[1].value as String] = tks[3]
                    else
                        TOER("Wrong type ${t.id} != ${tks[3].id}")
                } else {
                    TOER("Trying to access to unprovided self arguments")
                }
                Token("RES", "NONE")
            }
            makeStructure("ELEMENT", "DOCK", "ID") -> {
                if (tks[0].value is JavaElem)
                    (tks[0].value as JavaElem).getField(tks[2].value as String)
                else
                    TOER("Only java elements have static fields")
            }
        //DOCK
            makeStructure("INSTANCE", "DOCK", "ID") -> {
                (tks[0].value as Instance).properties[tks[2].value] as Token<*>
            }
        //COLON
            makeStructure("NUMBER", "COLON", "NUMBER") -> {
                val a = (tks[0].value as String).toDouble().toInt()
                val b = (tks[2].value as String).toDouble().toInt()
                val arr = mutableListOf<Token<*>>()
                if (a > b) {
                    for (i in a downTo b)
                        arr.add(Token("NUMBER", i.toString()))
                } else {
                    for (i in a..b)
                        arr.add(Token("NUMBER", i.toString()))
                }
                Token("ARRAY", arr.toTypedArray())
            }
        //UNTIL
            makeStructure("NUMBER", "UNTIL", "NUMBER") -> {
                val a = (tks[0].value as String).toDouble().toInt()
                val b = (tks[2].value as String).toDouble().toInt()
                val arr = mutableListOf<Token<*>>()
                if (a > b) {
                    for (i in a downTo (b + 1))
                        arr.add(Token("NUMBER", i.toString()))
                } else {
                    for (i in a..(b - 1))
                        arr.add(Token("NUMBER", i.toString()))
                }
                Token("ARRAY", arr.toTypedArray())
            }
        //SIZE
            makeStructure("SIZE", "GET") -> {
                Token("NUMBER", levels.last().arguments.size.toString())
            }
            makeStructure("SIZE", "STRING") -> {
                Token("NUMBER", (tks[1].value as String).length.toString())
            }
            makeStructure("SIZE", "ARRAY") -> {
                Token("NUMBER", (tks[1].value as Array<Token<*>>).size.toString())
            }
            makeStructure("SIZE", "BOOL") -> {
                Token("NUMBER", if ((tks[1].value as String).toBoolean()) 1 else 0)
            }
        //OF
            makeStructure("FUNCTION", "OF", "ELEMENT") -> {
                (tks[2].value as Elem).methods += tks[0].value as Fun
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "STRING") -> {
                Token("STRING", (tks[2].value as String)[(tks[0].value as String).toInt()].toString())
            }
            makeStructure("NUMBER", "OF", "ARRAY") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()]
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "NUMBER") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "STRING") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "BOOL") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "INSTANCE") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "ARRAY") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
            makeStructure("NUMBER", "OF", "ARRAY", "EQUALS", "POINTER") -> {
                (tks[2].value as Array<Token<*>>)[(tks[0].value as String).toInt()] = tks[4]
                Token("RES", "NONE")
            }
        //FUNCTION
            makeStructure("FUNCTION", "ARRAY", "THEN") -> {
                action.changeDoing(tks[0].value as Fun, tks[1].value as Array<Token<*>>)
                Token("RES", "NONE")
            }
            makeStructure("FUNCTION", "THEN") -> {
                action.changeDoing(tks[0].value as Fun, arrayOf<Token<*>>())
                Token("RES", "NONE")
            }
            makeStructure("INSTANCE", "DOCK", "ID", "ARRAY", "THEN") -> {
                val ins = (tks[0].value as Instance)
                val met = ins.base.getMethod(tks[2].value as String)
                action.changeDoing(met, tks[3].value as Array<Token<*>>, ins)
                Token("RES", "NONE")
            }
            makeStructure("INSTANCE", "DOCK", "ID", "THEN") -> {
                val ins = (tks[0].value as Instance)
                val met = ins.base.getMethod(tks[2].value as String)
                action.changeDoing(met, arrayOf<Token<*>>(), ins)
                Token("RES", "NONE")
            }
        //SPAWN
            makeStructure("SPAWN", "THEN") -> {
                action.changeDoing(DoingType.Spawn)
                Token("RES", "NONE")
            }
            makeStructure("SPAWN", "FUNCTION", "ARRAY", "THEN") -> {
                action.changeDoing(tks[1].value as Fun, tks[2].value as Array<Token<*>>, null, true)
                Token("RES", "NONE")
            }
            makeStructure("SPAWN", "FUNCTION", "THEN") -> {
                action.changeDoing(tks[1].value as Fun, arrayOf<Token<*>>(), null, true)
                Token("RES", "NONE")
            }
            makeStructure("SPAWN", "INSTANCE", "DOCK", "ID", "ARRAY", "THEN") -> {
                val ins = (tks[1].value as Instance)
                val met = ins.base.getMethod(tks[3].value as String)
                action.changeDoing(met, tks[4].value as Array<Token<*>>, ins, true)
                Token("RES", "NONE")
            }
            makeStructure("SPAWN", "INSTANCE", "DOCK", "ID", "THEN") -> {
                val ins = (tks[1].value as Instance)
                val met = ins.base.getMethod(tks[3].value as String)
                action.changeDoing(met, arrayOf<Token<*>>(), ins, true)
                Token("RES", "NONE")
            }
            else -> {
                when {
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("YIELD", "COLON")) -> {
                        if (levels.last().funyield != null){
                            val args = tks.copyOfRange(2, tks.lineStructure().size)
                            wrongArray(args)
                            (levels.last().funyield as Fun).exec(args)
                        } else {
                            TOER("There is no closure function to call")
                        }
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("ARRAY", "ON")) -> {
                        val args = tks.copyOfRange(2, tks.lineStructure().size)
                        wrongTypes(args)
                        val values = tks[0].value as Array<Token<*>>
                        var bln : Boolean = true
                        if (args.size != values.size)
                            bln = false
                        for (a in 0 until args.size) {
                            val x = values[a].id
                            bln = when (args[a].id) {
                                "NUMT" -> x == "NUMBER"
                                "STRT" -> x == "STRING"
                                "BOOLT" -> x == "BOOL"
                                "ELEMT" -> x == "INSTANCE"
                                "ARRAYT" -> x == "ARRAY"
                                "CHANT" -> x == "CHANNEL"
                                "JBYTET" -> x == "JBYTE"
                                "JSHORTT" -> x == "JSHORT"
                                "JINTT" -> x == "JINT"
                                "JLONGT" -> x == "JLONG"
                                "JFLOATT" -> x == "JFLOAT"
                                "JDOUBLET" -> x == "JDOUBLE"
                                "JNULLT" -> x == "JNULL"
                                else -> TOER("Wrong type ${args[a].id}")
                            }
                        }
                        Token("BOOL", bln.toString())
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("ARRAY", "AS")) -> {
                        val args = tks.copyOfRange(2, tks.lineStructure().size)
                        wrongIds(args)
                        val values = tks[0].value as Array<Token<*>>
                        if (args.size != values.size)
                            TOER("Wrong number of arguments: ${values.size} instead of ${args.size}")
                        for (a in 0 until args.size) {
                            levels.last()["@" + args[a].value as String] = values[a]
                        }
                        Token("RES", "NONE")
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("ELEMENT", "DOCK", "ID", "EQUALS")) && tks.size == 5 -> {
                        if (tks[0].value is JavaElem) {
                            wrongType(tks[4])
                            (tks[0].value as JavaElem).setField(tks[2].value as String, tks[4])
                            Token("RES", "NONE")
                        } else {
                            TOER("Only java elements have static fields")
                        }
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("INSTANCE", "DOCK", "ID", "EQUALS")) && tks.size == 5 -> {
                        if (tks[0].value is JavaInstance) {
                            wrongType(tks[4])
                            (tks[0].value as JavaInstance).setField(tks[2].value as String, tks[4])
                            Token("RES", "NONE")
                        } else {
                            TOER("Only java elements have static fields")
                        }
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("SPAWN", "FUNCTION", "COLON")) -> {
                        val args = tks.copyOfRange(3, tks.lineStructure().size)
                        wrongArray(args)
                        Spawn.spawnFun(tks[1].value as Fun, args)
                        Token("RES", "NONE")
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("FUNCTION", "COLON")) -> {
                        val args = tks.copyOfRange(2, tks.lineStructure().size)
                        wrongArray(args)
                        (tks[0].value as Fun).exec(args)
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("ELEMENT", "COLON")) -> {
                        val args = tks.copyOfRange(2, tks.lineStructure().size)
                        wrongArray(args)
                        Token(INSTANCE_TOKEN, (tks[0].value as Elem).getInstance(args))
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("SPAWN", "INSTANCE", "DOCK", "ID", "COLON")) -> {
                        val args = tks.copyOfRange(5, tks.lineStructure().size)
                        wrongArray(args)
                        Spawn.spawnFun((tks[1].value as Instance).base.getMethod(tks[3].value as String), args, (tks[1].value as Instance))
                        Token("RES", "NONE")
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("INSTANCE", "DOCK", "ID", "COLON")) -> {
                        val args = tks.copyOfRange(4, tks.lineStructure().size)
                        wrongArray(args)
                        (tks[0].value as Instance).run(tks[2].value as String, args)
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("ELEMENT", "DOCK", "ID", "COLON")) -> {
                        val args = tks.copyOfRange(4, tks.lineStructure().size)
                        wrongArray(args)
                        if (tks[0].value is JavaElem)
                            (tks[0].value as JavaElem).run(tks[2].value as String, args)
                        else
                            TOER("Only java elements have static methods")
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("SET", "ID", "COLON")) -> {
                        val args = tks.copyOfRange(3, tks.lineStructure().size)
                        wrongArray(args)
                        levels.last()["@" + tks[1].value as String] = Token(ARRAY_TOKEN, args)
                        Token("RES", "NONE")
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("COLON")) -> {
                        val args = tks.copyOfRange(1, tks.lineStructure().size)
                        wrongArray(args)
                        Token(ARRAY_TOKEN, args)
                    }
                    tks.lineStructure().joinToString(" ").startsWith(makeStructure("SELF", "ID", "COLON")) -> {
                        val args = tks.copyOfRange(3, tks.lineStructure().size)
                        wrongArray(args)
                        if (levels.last().self != null) {
                            val t = (levels.last().self as Instance).properties[tks[1].value as String] as Token<*>
                            if (t.id == tks[3].id)
                                (levels.last().self as Instance).properties[tks[1].value as String] = Token(ARRAY_TOKEN, args)
                            else
                                TOER("Wrong type ${t.id} != ${tks[3].id}")
                        } else {
                            TOER("Trying to access to unprovided self arguments")
                        }
                        Token("RES", "NONE")
                    }
                    else -> TOER("Wrong syntax: " + tks.lineStructure().joinToString(" "))
                }
            }
        }
    }

    fun wrongTypes(a: Array<Token<*>>) {
        for (e in a)
            if (!arrayOf("NUMT", "STRT", "BOOLT", "ELEMT", "ARRAYT", "CHANNELT", "JBYTET", "JSHORTT", "JINTT", "JLONGT", "JFLOATT", "JDOUBLET", "JNULLT").contains(e.id))
                TOER("Trying to pass a wrong type: ${e.value}")
    }

    fun wrongIds(array: Array<Token<*>>) {
        for (id in array)
            if (id.id != "ID")
                TOER("This is not a valid variable name: ${id.id}")
    }

    fun wrongArray(array: Array<Token<*>>) {
        for (a in array)
            wrongType(a)
    }

    fun wrongType(a: Token<*>) {
        if (!arrayOf("STRING", "NUMBER", "BOOL", "ELEMENT", "INSTANCE", "ARRAY", "CHANNEL", JAVA_BYTE_TOKEN, JAVA_SHORT_TOKEN, JAVA_INT_TOKEN, JAVA_LONG_TOKEN, JAVA_FLOAT_TOKEN, JAVA_DOUBLE_TOKEN, JAVA_NULL_TOKEN).contains(a.id))
            TOER("Trying to pass a wrong type: ${a.value}")
    }
}