package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class TodoActivityTest : AppCompatActivity() {
    //리사이클러뷰 연습 삼아 구현
    //리사이클러뷰 말고 리스트뷰로 구현 > 투두리스트에는 많은 아이템이 들어가지 않으니까.. 클릭리스너 구현이 용이한 리스트뷰로

    //캘린더에 투두가 어떤 방식으로 들어가게 될지를 모르겠어서... 추후 프레그먼트로 구현해야하나? 고민중
    //앱화면이 3개 이상이긴 해야하니까 따로 분리하긴 해야할텐데 그러면 액티비티가 맞긴 함

    //아이템 클릭 리스너에 bottom sheet modal로 수정창 띄울 예정

    //예시로 사용할 데이터들
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

    var exTodoList = arrayListOf<Todo>( //예시로 사용할 투두만 들어간 리스트
        Todo("마일스톤 플래너 작성", false),//구루1
        Todo("과제1 제출", false), //구루1
        Todo("과제1 제출", true), //구루2
        Todo("과제2 제출", true), //구루2
        Todo("해커톤 진행", false) //구루2
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_test)

        //아이템 선언
        val listViewTodo : ListView = findViewById(R.id.listView_todo)
        val buttonMain : Button = findViewById(R.id.button_main)

        //어댑터
        //투두 테스트버전
        /*
        val adapter = TodoListAdapter(this, exTodoList)
        listViewTodo.adapter = adapter
        */

        //카테고리 테스트버전
        val adapter = TodoListAdapter(this, exCategoryList)
        listViewTodo.adapter = adapter

        listViewTodo.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->
            Toast.makeText(this, "클릭!", Toast.LENGTH_LONG).show()
        })

        //리스트 뷰 리스트 선언
        val itemList : ArrayList<String>;


        //메인화면 가는 버튼
        buttonMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}