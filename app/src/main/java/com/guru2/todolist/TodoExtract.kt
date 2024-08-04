package com.guru2.todolist

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.TextView

class TodoExtract (val isCategory: Boolean, val id : String, var title: String, val check:Boolean)  {
 //투두와 카테고리를 동시에 ListView에 집어 넣는 것에 대한 대안

    override fun toString(): String {
        return "TodoExtract ( isCategory : ${isCategory}, id : ${id}, title : ${title}, check : ${check} )"
    }
}