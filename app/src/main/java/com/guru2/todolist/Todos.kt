package com.guru2.todolist

class Todos (val title: String, val check:Boolean) { //투두리스트
    override fun toString(): String {
        return "Todos ( title : ${title}, check : ${check} )"
    }
}