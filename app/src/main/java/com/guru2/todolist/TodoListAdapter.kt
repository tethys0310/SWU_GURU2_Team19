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
            //메시지 띄워서 투두 제목 입력 ... 할건데. 액티비티 테스트 쪽에서 하는게 나으려나.
            msg(position)

            //포지션 +1에 리스트 추가
            //갱신
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

    fun msg (position:Int) {
        Log.i("log message2", position.toString()) //여기까지 잘 작동된다. 확인.

        val et = EditText(context) //et.text
        et.setHint("추가내용을 여기에 입력")
        //왜 출력이 안될까...
        val builder = AlertDialog.Builder(context)
            .setTitle(list[position].title+" : 투두 추가")
            .setMessage("투두리스트에 추가할 내용을 입력하세요.")
            .setView(et)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener{ dialog, which ->
                    //사이에 수정 해주는 함수 하나 들어가야 할 듯?
                    //몇 번 눌렸는지 인덱스 찾아주고, 인덱스 매개변수로 보내서 함수로 처리
                    //이후로 수정내용 다시 디스플레이... 가 되려나?
                    Toast.makeText(context, "수정완료 : " + et.text, Toast.LENGTH_SHORT).show()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                })
        builder.show()


    }


}