package com.guru2.todolist

class Category (val title : String, val todoArray : ArrayList<Todos>) { //과목

    fun addTodo(toDo : Todos) {
        todoArray.add(toDo)
    }

}