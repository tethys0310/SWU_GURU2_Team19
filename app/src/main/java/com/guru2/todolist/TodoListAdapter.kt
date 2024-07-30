package com.guru2.todolist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

class TodoListAdapter (val context: Context, val list:ArrayList<TodoExtract>): BaseAdapter()
{
    private val view1: View = LayoutInflater.from(context).inflate(R.layout.todo_category, null) //과목
    private val view2: View = LayoutInflater.from(context).inflate(R.layout.todo_item, null) //투두

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val item= list[position]

        //투두리스트 출력하는 view2
        if (!item.isCategory) {

            val title_category = view2.findViewById<TextView>(R.id.textView_title)
            val checkbox = view2.findViewById<CheckBox>(R.id.checkBox_check)

            title_category.text = item.title
            if (item.check) checkbox.setChecked(true)

            return view2
        }

        //카테고리 출력하는 view1
        val title_category = view1.findViewById<TextView>(R.id.textView_title)
        title_category.text = item.title

        val button_plus = view1.findViewById<Button>(R.id.button_plus)
        button_plus.setOnClickListener{
            //TodoActivityTest().toastTest(position)
        }

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