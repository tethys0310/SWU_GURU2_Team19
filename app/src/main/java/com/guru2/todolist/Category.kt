package com.guru2.todolist

class Category (val id : String, val title : String, val todoArray : ArrayList<Todos>) {

    //TodoActivity에서 사용하는 간소화 된 Subject
    //아이디, 제목, 투두가 들어있는 ArrayList로 구성

    fun addTodo(toDo : Todos) {
        todoArray.add(toDo)
    }
}