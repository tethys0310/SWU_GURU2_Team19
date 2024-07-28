package com.guru2.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class TodoListAdapter (val context: Context, val list:ArrayList<Category>): BaseAdapter()
{
    var counter = 0 //과목수
    var isCheckingTodo = false //투두 체크 중인지 확인 할 수 있게.
    var counterTodo = 0 //투두 개수 비교용

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val item= list[counter]

        //투두리스트 출력하는 view2
        if (isCheckingTodo) {

            val view2: View = LayoutInflater.from(context).inflate(R.layout.todo_item, null)

            val title_category = view2.findViewById<TextView>(R.id.textView_title)
            val checkbox = view2.findViewById<CheckBox>(R.id.checkBox_check)

            val todoItem= item.todoArray[counterTodo]
            title_category.text = item.todoArray[counterTodo].title
            if (todoItem.check) checkbox.setChecked(true)

            if (counterTodo++ == item.todoArray.size) isCheckingTodo = false //체크 끝났으면 다음에 못 들어오게 막아주기

            return view2
        }

        //카테고리 출력하는 view1
        val view1: View = LayoutInflater.from(context).inflate(R.layout.todo_category, null)
        val title_category = view1.findViewById<TextView>(R.id.textView_title)
        title_category.text = item.title


        if (item.todoArray.isNotEmpty()) isCheckingTodo = true //투두 비어있지 않다면 들어갈 수 있게 열어주기
        counter += 1 //과목 1개 추가~

        return view1
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }
    override fun getCount(): Int {
        return list.size
    }
    override fun getItemId(position: Int): Long = position.toLong()
}