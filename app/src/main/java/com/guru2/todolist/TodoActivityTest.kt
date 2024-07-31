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

    //리스트뷰로 구현. 카테고리 클래스랑 투두 클래스 둘 다 먹어주는 클래스가 필요할 것 같음. 그걸로 리스트 만들어야겠지...

    //캘린더에 투두가 어떤 방식으로 들어가게 될지를 모르겠어서... 액티비티로 구현.

    //아이템 클릭 리스너에 bottom sheet modal로 수정창 띄울 예정 > 현재는 알럿 다이얼로그로 구현중

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

    fun modifyTodoExtract (arrayTodoExtract: ArrayList<TodoExtract>) : ArrayList<TodoExtract> {
        //수정용. 체크박스랑 이름 변경될 때마다 호출해야 하니까 최대한 가볍게 하고 싶은데... 되나?
        //매개변수로 인덱스까지 가져올까? 그 편이 낫겠다... 일단 좀 더 살펴보고 하기로.

        val result : ArrayList<TodoExtract> = arrayListOf()

        return result
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

        //작동을 안하는 ... 이제는 하는 ... 아이템클릭리스너...
        listViewTodo.setOnItemClickListener { parent, view, position, id ->
            val item = result[position]
            val et = EditText(this) //et.text
            et.setHint("수정내용을 여기에 입력")

            //다이얼로그로 구현하고 상황봐서 바텀싯으로 구현
            val builder = AlertDialog.Builder(this)
                .setTitle("수정하기 : " + item.title)
                .setMessage("수정할 내용 입력 후 확인\n수정사항 없으면 취소")
                .setView(et)
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, which ->
                        //사이에 수정 해주는 함수 하나 들어가야 할 듯?
                        //몇 번 눌렸는지 인덱스 찾아주고, 인덱스 매개변수로 보내서 함수로 처리
                        //이후로 수정내용 다시 디스플레이... 가 되려나?
                        Log.i("log message", et.text.toString()+" 수정 완료")
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.i("log message", item.title+" 수정 취소")
                    })
                .setNeutralButton("삭제",
                    DialogInterface.OnClickListener{ dialog, which -> //되묻기
                        val really = AlertDialog.Builder(this)
                        .setTitle("정말로 " + item.title + "을(를) 삭제하나요?")
                        .setPositiveButton("예",
                            DialogInterface.OnClickListener { dialog, which ->
                                //삭제하는 함수 넣을 예정
                                result.removeAt(position)
                                adapter.notifyDataSetChanged() //어댑터에게 갱신되었다고 알리기
                                Toast.makeText(this, item.title+" 일정을 정상적으로 삭제했습니다.", Toast.LENGTH_SHORT).show()
                                Log.i("log message", item.title+" 삭제 완료")
                            })
                        .setNegativeButton("아니오",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this, item.title+" 일정을 삭제하지 않았습니다.", Toast.LENGTH_SHORT).show()
                                Log.i("log message", item.title+" 삭제 취소")
                            })
                    really.show()
                })
            builder.show()
        }

        //과목 +버튼 누르면 투두 추가할 수 있는 기능. 이것도 일단은 다이얼로그로 구현하자

        //투두 삭제 기능

        //메인화면 가는 버튼
        buttonMain.setOnClickListener {
            //저장하자~
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}