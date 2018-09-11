package org.besser.core

/**
 * Created by rayoe on 24/08/2018.
 */
class Parser(val levels: MutableList<Context>) {

    var actualDoingType : DoingType = DoingType.Execute
    private var actualDoing : Doing = Execute(this)

    fun parse(tokens: Array<Array<Token<*>>>) {
        tokens.forEach {
            line++
            levels.last().noGroupedLine = it
            if (actualDoingType == DoingType.Execute)
                parseLine(it)
            else
                actualDoing.run(it)
        }
    }

    fun parseLine(ln: Array<Token<*>>): Token<*> {
        val elems = mutableListOf<Token<*>>()
        val subs = mutableListOf<Token<*>>()
        var deep = 0
        for (token in ln) {
            if (token.id == OPERATOR_TOKEN && (token.value == "(" || token.value == ")")) {
                if (token.value == "(") {
                    if (deep > 0){
                        subs += token
                    }
                    deep++
                } else {
                    if (deep < 0)
                        TOER("More closed parentheses than opened")
                    deep--
                    if (deep == 0) {
                        elems += parseLine(subs.toTypedArray())
                        subs.clear()
                    }else {
                        subs += token
                    }
                }
            } else if (deep > 0) {
                subs += token
            } else {
                elems += token
            }
        }
        if (deep > 0) {
            TOER("More opened parentheses than closed")
        }
        if (deep < 0) {
            TOER("More closed parentheses than opened")
        }
        val tkp = elems.toTypedArray()
        val tkv = tkp.getVars(levels.last()).nameOperators().nameKeywords()
        return run(tkv)
    }

    fun run(tks: Array<Token<*>>): Token<*> {
        return actualDoing.run(tks)
    }

    fun changeDoing(func : Fun, array: Array<Token<*>>, ins : Instance? = null) {
        actualDoingType = DoingType.Closure
        actualDoing = Closure(this, func, array, ins)
    }

    fun changeDoing(array: Array<Token<*>>) {
        actualDoingType = DoingType.Isolated
        actualDoing = Isolated(this, array)
    }

    fun changeDoing(doingtype: DoingType, condition : Boolean) {
        actualDoingType = doingtype
        actualDoing = when(doingtype) {
            DoingType.IfCondition -> IfCondition(this, condition)
            DoingType.WhileLoop -> WhileLoop(this, condition, levels.last().noGroupedLine as Array<Token<*>>)
            else -> {
                TOER("Wrong Doing")
            }
        }
    }

    fun changeDoing(doingtype : DoingType) {
        actualDoingType = doingtype
        actualDoing = when(doingtype) {
            DoingType.Execute -> Execute(this)
            DoingType.WhenCondition -> WhenCondition(this)
            DoingType.Block -> Block(this)
            DoingType.TryBlock -> TryBlock(this)
            else -> {
                TOER("Wrong Doing")
            }
        }
    }

    fun changeDoing(range : Int, variable: String?) {
        actualDoingType = DoingType.RepeatLoop
        actualDoing = RepeatLoop(this, range, variable)
    }

    fun changeDoing(event: Event) {
        actualDoingType = DoingType.Event
        actualDoing = OnEvent(this, event)
    }

    fun changeDoing(variable : String, elements : Array<Token<*>>) {
        actualDoingType = DoingType.ForLoop
        actualDoing = ForLoop(this, variable, elements)
    }

    fun changeDoing(name : String, elem : Elem) {
        actualDoingType = DoingType.ApplyToElem
        actualDoing = SaveIntoFun(this, name, elem)
    }

    fun changeDoing(type : DoingType, name : String) {
        actualDoingType = type
        actualDoing = when(type) {
            DoingType.SaveIntoFun -> SaveIntoFun(this, name)
            DoingType.SaveIntoElem -> SaveIntoElem(this, name)
            else -> TODO("Unchecked doing")
        }
    }
}