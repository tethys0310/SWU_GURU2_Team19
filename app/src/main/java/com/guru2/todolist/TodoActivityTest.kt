package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class TodoActivityTest : AppCompatActivity() {

    //리스트뷰로 구현. 카테고리 클래스랑 투두 클래스 둘 다 먹어주는 클래스가 필요할 것 같음. 그걸로 리스트 만들어야겠지...

    //캘린더에 투두가 어떤 방식으로 들어가게 될지를 모르겠어서... 추후 프레그먼트로 구현해야하나? 고민중
    //앱화면이 3개 이상이긴 해야하니까 따로 분리하긴 해야할텐데 그러면 사실 액티비티가 맞긴 함

    //아이템 클릭 리스너에 bottom sheet modal로 수정창 띄울 예정

    //예시로 사용할 데이터들 ...
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
        //1. 어레이리스트 카테고리를 가져온다

        //리턴용
        val result : ArrayList<TodoExtract> = arrayListOf()

        for (i in 0 until arrayCategory.size) { //2. 카테고리 저장
            result.add(TodoExtract(true, arrayCategory[i].title, false))
            if (arrayCategory[i].todoArray.isNotEmpty()) {
                for (j in 0 until arrayCategory[i].todoArray.size) //3. 카테고리 안 투두 저장
                    result.add(TodoExtract(false, arrayCategory[i].todoArray[j].title, arrayCategory[i].todoArray[j].check))
            }
        }

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

        //작동을 안하는 ... 아이템클릭리스너...
        listViewTodo.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->
            Toast.makeText(this, "클릭!", Toast.LENGTH_LONG).show()
        })

        //메인화면 가는 버튼
        buttonMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}