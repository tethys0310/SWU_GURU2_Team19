package com.guru2.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get


class TodoActivityTest : AppCompatActivity() {

    //캘린더에 투두가 어떤 방식으로 들어가게 될지를 모르겠어서... 액티비티로 구현.
    //리스트뷰로 구현. 카테고리 클래스랑 투두 클래스 둘 다 먹어주는 클래스가 필요할 것 같음. 그걸로 리스트 만들어야겠지...
    //아이템 클릭 리스너 통해서 투두 수정, 삭제 기능 구현. 알럿 다이얼로그로 구현.

    //예시로 사용할 데이터들 ...
    //데이터베이스에서 가져올 때 이런 형태로 가져와야? 할 듯...
    val exCategoryList = arrayListOf<Category> (
        Category("GURU1", arrayListOf(
            Todo("마일스톤 플래너 작성", false),
            Todo("과제1 제출", false))
        ),
        Category("GURU2", arrayListOf(
            Todo("과제1 제출", true),
            Todo("과제2 제출", true),
            Todo("해커톤 진행", false))
        )
    )

    fun makeArrayTodoExtract (arrayCategory: ArrayList<Category>) : ArrayList<TodoExtract> {

        //TodoListAdapter에 넣기 위한 어레이리스트 만드는 용도

        //리턴용
        val result : ArrayList<TodoExtract> = arrayListOf()

        for (i in 0 until arrayCategory.size) { //카테고리 저장
            result.add(TodoExtract(true, arrayCategory[i].title, false))
            if (arrayCategory[i].todoArray.isNotEmpty()) {
                for (j in 0 until arrayCategory[i].todoArray.size) //카테고리 안 투두 저장
                    result.add(TodoExtract(false, arrayCategory[i].todoArray[j].title, arrayCategory[i].todoArray[j].check))
            }
        }

        return result
    }

    fun breakArrayTodoExtract (arrayTodoExtract: ArrayList<TodoExtract>) : ArrayList<Category> {
        //데이터베이스에 넣기 전 어레이리스트 만드는 용도

        val result : ArrayList<Category> = arrayListOf() //리턴용
        var counter = -1 //카테고리 수 카운터

        for (i in 0 until arrayTodoExtract.size) {
            if (arrayTodoExtract[i].isCategory == true) {
                //카테고리 저장
                counter += 1 //다음 카테고리 저장 카운터로 넘겨주기
                result.add(Category(arrayTodoExtract[i].title, arrayListOf()))
            }
            else {
                result[counter].addTodo(Todo(arrayTodoExtract[i].title, arrayTodoExtract[i].check))
            }
        }
        return result
    }

    fun modifyTodoExtract (array: ArrayList<TodoExtract>, position:Int, msg:String, check:Boolean) {
        //이름 수정용.
        array.removeAt(position)
        array.add(position, TodoExtract(false, msg, check))

        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_test)

        //아이템 선언
        val listViewTodo : ListView = findViewById(R.id.listView_todo)
        val buttonMain : Button = findViewById(R.id.button_main)

        //카테고리 어레이리스트를 투두익스트렉 어레이리스트로 변환
        val result : ArrayList<TodoExtract> = makeArrayTodoExtract(exCategoryList)

        //어댑터에 투두익스트렉 어레이리스트 삽입 후 화면 출력
        val adapter = TodoListAdapter(this, result)
        listViewTodo.adapter = adapter

        //카테고리 클릭 했을 때 작동하는 아이템 클릭 리스너. 수정 및 삭제 기능은 이 곳에, 추가 기능은 어댑터에.
        listViewTodo.setOnItemClickListener { parent, view, position, id ->
            //화면부분
            val item = result[position]
            val et = EditText(this) //et.text
            et.setHint("수정내용을 여기에 입력")

            //먼저 다이얼로그로 구현하고 상황봐서 바텀싯으로 구현
            val builder = AlertDialog.Builder(this) //수정? 삭제? 취소? 셋 중에 뭐할거냐고 묻는 다이얼로그
                .setTitle(item.title)
                .setMessage("\n수정할 내용 하단에 입력 후 확인\n수정사항 없으면 취소\n\n")
                .setView(et)
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, which ->
                        modifyTodoExtract (result, position, et.text.toString(), item.check) //수정 해주는 함수
                        adapter.notifyDataSetChanged()
                        Log.i("log message", et.text.toString()+" 수정 완료")
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("log message", item.title+" 수정 취소")
                    })
                .setNeutralButton("삭제",
                    DialogInterface.OnClickListener{ dialog, which -> //삭제 버튼 누를 때 유저에게 진짜 삭제할거냐고 되물어야함
                        val really = AlertDialog.Builder(this)
                        .setTitle("정말로 " + item.title + "을(를) 삭제하나요?")
                        .setPositiveButton("예",
                            DialogInterface.OnClickListener { dialog, which ->
                                result.removeAt(position)
                                adapter.notifyDataSetChanged() //어댑터에게 갱신되었다고 알리기
                                Toast.makeText(this, item.title+" 일정을 정상적으로 삭제했습니다.", Toast.LENGTH_SHORT).show()
                                Log.i("log message", item.title+" 삭제 완료")
                            })
                        .setNegativeButton("아니오",
                            DialogInterface.OnClickListener { dialog, which ->
                                Log.i("log message", item.title+" 삭제 취소")
                            })
                    really.show()
                })
            builder.show()
        }

        //메인화면 가는 버튼
        buttonMain.setOnClickListener {
            //메인화면 넘어가기 전에 리스트 저장 잊지 말 것. DB 연동 들어가야 함.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}