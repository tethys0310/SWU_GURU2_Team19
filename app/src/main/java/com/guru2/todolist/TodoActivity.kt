package com.guru2.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class TodoActivity : MenuTestActivity() {

    //캘린더에 투두가 어떤 방식으로 들어가게 될지를 모르겠어서... 액티비티로 구현.
    //리스트뷰로 구현. 카테고리 클래스랑 투두 클래스 둘 다 먹어주는 클래스(투두익스트랙) 존재. 그걸로 리스트뷰 만들어진다.
    //아이템 클릭 리스너 통해서 투두 수정, 삭제 기능 구현. 알럿 다이얼로그로 구현.

    //DB연결
    // 데이터 베이스 client 받아 오기
    private fun getClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://kdnfwewdwqzzhpafrnbq.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkbmZ3ZXdkd3F6emhwYWZybmJxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjIyNTY0MjYsImV4cCI6MjAzNzgzMjQyNn0.oeNfN9TP-axgJMeKte296B4FkEvMX4gs63k6kqWQTkE"
        ) {
            install(Postgrest)
        }
    }

    //TodoListAdapter에 넣기 위한 어레이리스트 만드는 용도
    fun makeArrayTodoExtract (arrayCategory: ArrayList<Category>) : ArrayList<TodoExtract> {
        //리턴용
        val result : ArrayList<TodoExtract> = arrayListOf()

        for (i in 0 until arrayCategory.size) { //카테고리 저장
            result.add(TodoExtract(true, arrayCategory[i].id, arrayCategory[i].title, false))
            if (arrayCategory[i].todoArray.isNotEmpty()) {
                for (j in 0 until arrayCategory[i].todoArray.size) { //카테고리 안 투두 저장
                    result.add(
                        TodoExtract(
                            false,
                            "",
                            arrayCategory[i].todoArray[j].title,
                            arrayCategory[i].todoArray[j].check
                        )
                    )
                }
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
                result.add(Category(arrayTodoExtract[i].id, arrayTodoExtract[i].title, arrayListOf()))
            }
            else {
                result[counter].addTodo(Todos(arrayTodoExtract[i].title, arrayTodoExtract[i].check))
            }
        }
        return result
    }

    fun modifyTodoExtract (array: ArrayList<TodoExtract>, position:Int, msg:String, check:Boolean) {
        //이름 수정용.
        array.removeAt(position)
        array.add(position, TodoExtract(false,"", msg, check))

        return
    }

    /* ====== DB관련 ====== */

    // DB에서부터 과목 정보 받아오기
    private suspend fun getSubjectOnDB(client : SupabaseClient) : List<Subject> {

        var data: List<Subject> = arrayListOf()

        try {
            val supabaseResponse = client.postgrest["subjects"].select()

            //배열로 들어오는 Subject들
            data = supabaseResponse.decodeList<Subject>()
            }catch (e: Exception){
                Log.e("supabase", "Subject 받아오기 중 에러 발생")
            }
        return data
    }

    // DB에서부터 과목 ID 기반으로 하여금 투두 정보 받아오기
    private suspend fun getTodoOnDB(client : SupabaseClient, subject_id : String) : List<Todo> {
        var data: List<Todo> = arrayListOf()

        try {
            val supabaseResponse = client.postgrest["todos"].select{ 
                filter {
                    eq("subject_id", subject_id) //매개변수로 아이디 지정
                }
            }
            data = supabaseResponse.decodeList<Todo>() //아이디와 일치하는 투두만 저장됨
            Log.d("supabase", data.toString())
        }catch (e: Exception){
            Log.e("supabase", "Todo 받아오기 중 에러 발생")
        }
        return data
    }

    // 받아온 정보 가공하기
    private fun getOnDB(client : SupabaseClient) : ArrayList<TodoExtract>{
        val categoryList: ArrayList<Category> = arrayListOf()
        var subjectList: List<Subject> = listOf()
        var todosList: List<Todo> = listOf()

        runBlocking<Unit> { //과목 불러오기 위해 잠깐 블락
            val subject : Deferred<List<Subject>> = async { getSubjectOnDB(client) }
            subjectList = subject.await()
        }

        //Subject(id=아이디, title=과목명, dayOfWeek=요일, start=시작, end=끝, credit=학점, professor=교수명, sub_class=위치)
        //Category(id=아이디, title=과목명, todoArray=투두 들어가는 어레이)
        //투두에서 필요한 건 과목명 뿐... subject 관련하여서는 여기에서는 수정 없을 예정

        for (i in subjectList.indices) {
            categoryList.add(Category(subjectList[i].id, subjectList[i].title, arrayListOf()))
            //카테고리 내 투두 배열에 투두 추가 전 투두 불러오는 작업
            runBlocking<Unit> {
                val todo : Deferred<List<Todo>> = async { getTodoOnDB(client, subjectList[i].id) } //과목ID 요구
                todosList = todo.await()
            }
            if (todosList.isNotEmpty()) { //검색 결과 존재시
                for (j in todosList.indices) {
                    categoryList[i].todoArray.add(Todos(todosList[j].title, todosList[j].done)) //카테고리 클래스의 배열 안에 투두 추가
                }
            }
        }

        val result: ArrayList<TodoExtract> = makeArrayTodoExtract(categoryList) //카테고리 배열로 투두익스트랙 배열 만들기

        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_test)
        setupBottomNavigationBar(R.id.nav_todo)

        //아이템 선언
        val listViewTodo : ListView = findViewById(R.id.listView_todo)
        val buttonMain : Button = findViewById(R.id.button_main)
        var result : ArrayList<TodoExtract> = arrayListOf()
        //슈퍼베이스 클라이언트가 자꾸 꺼졌다 켜졌다 하길래 아예 변수로 남겨뒀어요. 문제가 있다면 알려주시길...
        val client : SupabaseClient = getClient()


        //카테고리 어레이리스트를 투두익스트렉 어레이리스트로 변환
        runBlocking<Unit> { //결과 나오기 전까지 기다리기
            var listCategory = async{ getOnDB(client) }
            result = listCategory.await()
        }
        
        //어댑터에 투두익스트렉 어레이리스트 삽입 후 화면 출력
        val adapter = TodoListAdapter(this, client, result)
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
                .setNeutralButton("삭제"
                ) { dialog, which -> //삭제 버튼 누를 때 유저에게 진짜 삭제할거냐고 되물어야함
                    val really = AlertDialog.Builder(this)
                        .setTitle("정말로 " + item.title + "을(를) 삭제하나요?")
                        .setPositiveButton("예"
                        ) { dialog, which ->
                            result.removeAt(position)
                            adapter.notifyDataSetChanged() //어댑터에게 갱신되었다고 알리기
                            Toast.makeText(
                                this,
                                item.title + " 일정을 정상적으로 삭제했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i("log message", item.title + " 삭제 완료")
                        }
                        .setNegativeButton("아니오"
                        ) { dialog, which ->
                            Log.i("log message", item.title + " 삭제 취소")
                        }
                    really.show()
                }
            builder.show()
        }

        //메인화면 가는 버튼
        buttonMain.setOnClickListener {
            //리스트뷰 분해
            //exCategoryList = breakArrayTodoExtract(result)

            //메인화면 넘어가기 전에 리스트 저장 잊지 말 것. DB 연동 들어가야 함.
            //Log.i("log message", exCategoryList[1].todoArray[0].title) //구루2에 추가한 1번째 투두 확인용. 잘 작동함!
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}