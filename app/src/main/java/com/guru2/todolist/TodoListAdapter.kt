package com.guru2.todolist

import android.R.id.button1
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class TodoListAdapter (val context: Context, val list:ArrayList<TodoExtract>): BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val item= list[position]

        //투두리스트 출력하는 view2
        if (!item.isCategory) {

            val view2: View = LayoutInflater.from(context).inflate(R.layout.todo_item, null) //투두

            val titleCategory = view2.findViewById<TextView>(R.id.textView_title)
            val checkbox = view2.findViewById<CheckBox>(R.id.checkBox_check)
            checkbox.setOnClickListener {
                Log.i("log message", position.toString()) //인덱스 확인용
                //체크박스 변동마다 함수 호출
                modifyCheckbox(position)
            }

            titleCategory.text = item.title
            if (item.check) checkbox.setChecked(true)

            return view2
        }

        //카테고리 출력하는 view1
        val view1: View = LayoutInflater.from(context).inflate(R.layout.todo_category, null) //과목

        val titleCategory = view1.findViewById<TextView>(R.id.textView_title)
        titleCategory.text = item.title

        val buttonPlus = view1.findViewById<Button>(R.id.button_plus)
        buttonPlus.setOnClickListener {
            Log.i("log message", position.toString()) //인덱스 확인용
            //메시지 띄워서 투두 제목 입력.
            addTodo(position) //할 일 추가 해주는 함수
        }

        return view1
    }

    override fun getItem(position: Int): TodoExtract {
        return list[position]
    }
    override fun getCount(): Int {
        return list.size
    }
    override fun getItemId(position: Int): Long = position.toLong()

    //과목 +버튼 누르면 투두 추가할 수 있는 기능.
    fun addTodo (position:Int) {
        val et = EditText(context) //et.text
        et.setHint("추가내용을 여기에 입력")
        
        //다이얼로그
        val builder = AlertDialog.Builder(context)
            .setTitle(list[position].title+" 과목에 일정 추가")
            .setMessage("투두리스트에 추가할 내용을 입력하세요.")
            .setView(et)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener{ dialog, which ->
                    //포지션 +1에 리스트 추가
                    list.add(position+1, TodoExtract(false, et.text.toString(), false))
                    notifyDataSetChanged() //어댑터에게 갱신되었다고 알리기
                    Toast.makeText(context, "추가 완료 : " + et.text, Toast.LENGTH_SHORT).show()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, which ->
                    Log.i("log message2", "추가 취소")
                })
        builder.show()
    }

    fun modifyCheckbox (position: Int) {

        var item : TodoExtract = getItem(position)
        list.removeAt(position)

        if (item.check){
            list.add(position, TodoExtract(false, item.title, false))
            Log.i("log message2", item.title+"이 체크박스 해제")
        }
        else
            list.add(position, TodoExtract(false, item.title, true))
    }


}