package com.guru2.todolist

class Category (val title : String, val todoArray : ArrayList<Todo>) { //과목

    fun addTodo(toDo : Todo) {
        todoArray.add(toDo)
    }

}