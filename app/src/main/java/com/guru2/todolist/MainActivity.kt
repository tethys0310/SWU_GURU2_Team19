package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.util.Identity.decode
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toDatePeriod
import kotlinx.serialization.Serializable
import java.sql.Time
import java.util.Date

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: ImageButton
    lateinit var editTextId: EditText
    lateinit var editTextPassword: EditText
    lateinit var btnRegister: Button
    var DB:DBHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB = DBHelper(this)

        btnLogin = findViewById(R.id.btnLogin)
        editTextId = findViewById(R.id.editTextId)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnRegister = findViewById(R.id.btnRegister)

        // 로그인 버튼 클릭
        btnLogin!!.setOnClickListener {
            val user = editTextId!!.text.toString()
            val pass = editTextPassword!!.text.toString()

            // 빈칸 제출시 Toast
            if (user == "" || pass == "") {
                Toast.makeText(this@MainActivity, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                val checkUserpass = DB!!.checkUserpass(user, pass)
                // id 와 password 일치시
                if (checkUserpass == true) {
                    Toast.makeText(this@MainActivity, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@MainActivity, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 회원가입 버튼 클릭시
        btnRegister.setOnClickListener {
            val loginIntent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(loginIntent)
        }

        // users 데이터 받아오기 예시
        getUserData()

        // users 데이터 삽입 예시
        // setUserData(User("1","123", "랄랄라",2022555555))

        // users 데이터 수정 예시
        // updateUserData(User("1","456", "와하하",2022777777))

        // users 데이터 삭제 예시
        // deleteUserData(User("1","456", "와하하",2022777777))



        // subjects 데이터 받아오기 예시
        getSubjectData()

        // subjects 데이터 삽입 예시
        // setSubjectData(Subject("샘플!", "경건회", "목", 6,6,0, "몰라", "대강당"))

        // subjects 데이터 수정 예시
        // updateSubjectData(Subject("샘플!", "랄랄라", "토", 1,1,100, "야옹이", "우리집"))

        // subjects 데이터 삭제 예시
        // deleteSubjectData(Subject("샘플!", "랄랄라", "토", 1,1,100, "야옹이", "우리집"))



        // enrolls 데이터 받아오기 예시
        getEnrollData()

        // enrolls 데이터 삽입 예시
        // setEnrollData(Enroll("sample3", "sample2", "#0000FF"))

        // enrolls 데이터 수정 예시
        // updateEnrollData(Enroll("sample3", "sample2", "#00FFFF"))

        // enrolls 데이터 삭제 예시
        // deleteEnrollData(Enroll("sample3", "sample2", "#00FFFF"))


        // todos 데이터 받아오기 예시
        getTodoData()

        // todos 데이터 삽입 예시
        // setTodoData(Todo("123123", "sample1", "sample1", "노트북 챙기기",
        //     2024, 9, 12, false))

        // todos 데이터 수정 예시
        // updateTodoData(Todo("123123", "sample1", "sample1", "까먹기",
        //             2024, 12, 25, true))

        // todos 데이터 삭제 예시
        // deleteTodoData(Todo("123123", "sample1", "sample1", "까먹기",
        //     2024, 12, 25, true))


        // schedules 데이터 받아오기 예시
        getScheduleData()

        // schedules 데이터 삽입 예시
        // setScheduleData(Schedule("123123", "sample2", "sample2", "큰 일",
        //     "엄청 중요함",2024, 9, 12))

        // schedules 데이터 수정 예시
        // updateScheduleData(Schedule("123123", "sample2", "sample2", "작은 일",
        //     "별로 안 중요",2024, 10, 4))

        // schedules 데이터 삭제 예시
        // deleteScheduleData(Schedule("123123", "sample2", "sample2", "작은 일",
        //      "별로 안 중요",2024, 10, 4))


        // shuttles 데이터 받아오기 예시
        getShuttleData()

        // shuttles 데이터 삽입 예시
        // setShuttleData(Shuttle("123","태릉입구", 13,15))

        // shuttles 데이터 수정 예시
        // updateShuttleData(Shuttle("123","서울여대", 8,40))

        // shuttles 데이터 삭제 예시
        // deleteShuttleData(Shuttle("123","서울여대", 8,40))

    }


    /* ====== users ====== */

    // user 정보 받아오기
    private fun getUserData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["users"].select()
                val data: List<User> = supabaseResponse.decodeList<User>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "User 받아오기 중 에러 발생")
            }
        }
    }

    // user 정보 생성하기
    private fun setUserData(user: User){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["users"].insert(user)
                Log.e("supabase", "User 생성 성공: $user")
            }catch (e: Exception){
                Log.e("supabase", "User 생성 중 에러 발생")
            }
        }
    }

    // user 정보 수정하기
    private fun updateUserData(user: User){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["users"].update(
                    {
                        set("pw", user.pw)
                        set("name", user.name)
                        set("stu_num", user.stu_num)
                    }
                ){
                    filter {
                        eq("id", user.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "User 수정 성공: $user")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "User 수정 중 오류 발생")
            }
        }
    }

    // user 정보 삭제하기
    private fun deleteUserData(user: User){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["users"].delete {
                    filter {
                        eq("id", user.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "User 삭제 성공: $user")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "User 삭제 중 오류 발생")
            }
        }
    }


    /* ====== subjects ====== */

    // subjects 정보 받아오기
    private fun getSubjectData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["subjects"].select()
                val data: List<Subject> = supabaseResponse.decodeList<Subject>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "Subject 받아오기 중 에러 발생")
            }
        }
    }

    // subjects 정보 생성하기
    private fun setSubjectData(subject: Subject){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["subjects"].insert(subject)
                Log.e("supabase", "Subject 생성 성공: $subject")
            }catch (e: Exception){
                Log.e("supabase", "Subject 생성 중 에러 발생")
            }
        }
    }

    // subjects 정보 수정하기
    private fun updateSubjectData(subject: Subject){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["subjects"].update(
                    {
                        set("title", subject.title)
                        set("dayOfWeek", subject.dayOfWeek)
                        set("start", subject.start)
                        set("end", subject.end)
                        set("credit", subject.credit)
                        set("professor", subject.professor)
                        set("sub_class", subject.sub_class)
                    }
                ){
                    filter {
                        eq("id", subject.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Subject 수정 성공: $subject")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Subject 수정 중 오류 발생")
            }
        }
    }

    // subjects 정보 삭제하기
    private fun deleteSubjectData(subject: Subject){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["subjects"].delete {
                    filter {
                        eq("id", subject.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Subject 삭제 성공: $subject")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Subject 삭제 중 오류 발생")
            }
        }
    }


    /* ====== enrolls ====== */

    // enrolls 정보 받아오기
    private fun getEnrollData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["enrolls"].select()
                val data: List<Enroll> = supabaseResponse.decodeList<Enroll>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "Enroll 받아오기 중 에러 발생")
            }
        }
    }

    // enrolls 정보 생성하기
    private fun setEnrollData(enroll: Enroll){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["enrolls"].insert(enroll)
                Log.e("supabase", "Enroll 생성 성공: $enroll")
            }catch (e: Exception){
                Log.e("supabase", "Enroll 생성 중 에러 발생")
            }
        }
    }

    // enrolls 정보 수정하기
    private fun updateEnrollData(enroll: Enroll){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["enrolls"].update(
                    {
                        set("user_id", enroll.user_id)
                        set("subject_id", enroll.subject_id)
                        set("color", enroll.color)
                    }
                ){
                    filter {
                        eq("user_id", enroll.user_id)
                        eq("subject_id", enroll.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Enroll 수정 성공: $enroll")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Enroll 수정 중 오류 발생")
            }
        }
    }

    // enrolls 정보 삭제하기
    private fun deleteEnrollData(enroll: Enroll){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["enrolls"].delete {
                    filter {
                        eq("user_id", enroll.user_id)
                        eq("subject_id", enroll.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Enroll 삭제 성공: $enroll")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Enroll 삭제 중 오류 발생")
            }
        }
    }


    /* ====== todos ====== */

    // todos 정보 받아오기
    private fun getTodoData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["todos"].select()
                val data: List<Todo> = supabaseResponse.decodeList<Todo>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "Todo 받아오기 중 에러 발생")
            }
        }
    }

    // todos 정보 생성하기
    private fun setTodoData(todo: Todo){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["todos"].insert(todo)
                Log.e("supabase", "Todo 생성 성공: $todo")
            }catch (e: Exception){
                Log.e("supabase", "Todo 생성 중 에러 발생")
            }
        }
    }

    // todos 정보 수정하기
    private fun updateTodoData(todo: Todo){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["todos"].update(
                    {
                        set("id", todo.id)
                        set("user_id", todo.user_id)
                        set("subject_id", todo.subject_id)
                        set("title", todo.title)
                        set("year", todo.year)
                        set("month", todo.month)
                        set("day", todo.day)
                        set("done", todo.done)
                    }
                ){
                    filter {
                        eq("id", todo.id)
                        eq("user_id", todo.user_id)
                        eq("subject_id", todo.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Todo 수정 성공: $todo")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Todo 수정 중 오류 발생")
            }
        }
    }

    // todos 정보 삭제하기
    private fun deleteTodoData(todo: Todo){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["todos"].delete {
                    filter {
                        eq("id", todo.id)
                        eq("user_id", todo.user_id)
                        eq("subject_id", todo.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Todo 삭제 성공: $todo")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Todo 삭제 중 오류 발생")
            }
        }
    }


    /* ====== schedules ====== */

    // schedules 정보 받아오기
    private fun getScheduleData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["schedules"].select()
                val data: List<Schedule> = supabaseResponse.decodeList<Schedule>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "Schedule 받아오기 중 에러 발생")
            }
        }
    }

    // schedules 정보 생성하기
    private fun setScheduleData(schedule: Schedule){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["schedules"].insert(schedule)
                Log.e("supabase", "Schedule 생성 성공: $schedule")
            }catch (e: Exception){
                Log.e("supabase", "Schedule 생성 중 에러 발생")
            }
        }
    }

    // schedules 정보 수정하기
    private fun updateScheduleData(schedule: Schedule){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["schedules"].update(
                    {
                        set("id", schedule.id)
                        set("user_id", schedule.user_id)
                        set("subject_id", schedule.subject_id)
                        set("title", schedule.title)
                        set("sub", schedule.sub)
                        set("year", schedule.year)
                        set("month", schedule.month)
                        set("day", schedule.day)
                    }
                ){
                    filter {
                        eq("id", schedule.id)
                        eq("user_id", schedule.user_id)
                        eq("subject_id", schedule.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Schedule 수정 성공: $schedule")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Schedule 수정 중 오류 발생")
            }
        }
    }

    // schedules 정보 삭제하기
    private fun deleteScheduleData(schedule: Schedule){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["schedules"].delete {
                    filter {
                        eq("id", schedule.id)
                        eq("user_id", schedule.user_id)
                        eq("subject_id", schedule.subject_id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Schedule 삭제 성공: $schedule")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Schedule 삭제 중 오류 발생")
            }
        }
    }


    /* ====== shuttles ====== */

    // shuttles 정보 받아오기
    private fun getShuttleData(){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["shuttles"].select()
                val data: List<Shuttle> = supabaseResponse.decodeList<Shuttle>()
                Log.d("supabase", data.toString())
            }catch (e: Exception){
                Log.e("supabase", "Shuttle 받아오기 중 에러 발생")
            }
        }
    }

    // shuttles 정보 생성하기
    private fun setShuttleData(shuttle: Shuttle){
        lifecycleScope.launch {
            try{
                val client = getClient()
                val supabaseResponse = client.postgrest["shuttles"].insert(shuttle)
                Log.e("supabase", "Shuttle 생성 성공: $shuttle")
            }catch (e: Exception){
                Log.e("supabase", "Shuttle 생성 중 에러 발생")
            }
        }
    }

    // shuttles 정보 수정하기
    private fun updateShuttleData(shuttle: Shuttle){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["shuttles"].update(
                    {
                        set("id", shuttle.id)
                        set("depart", shuttle.depart)
                        set("depart_hour", shuttle.depart_hour)
                        set("depart_min", shuttle.depart_min)
                    }
                ){
                    filter {
                        eq("id", shuttle.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Shuttle 수정 성공: $shuttle")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Shuttle 수정 중 오류 발생")
            }
        }
    }

    // shuttles 정보 삭제하기
    private fun deleteShuttleData(shuttle: Shuttle){
        lifecycleScope.launch {
            try {
                val client = getClient()
                // 데이터 수정 요청
                client.postgrest["shuttles"].delete {
                    filter {
                        eq("id", shuttle.id)
                    }
                }

                // 수정 성공 시 로그 기록
                Log.d("supabase", "Shuttle 삭제 성공: $shuttle")
            } catch (e: Exception) {
                // 수정 중 발생한 오류를 처리
                Log.e("supabase", "Shuttle 삭제 중 오류 발생")
            }
        }
    }


    // 데이터 베이스 client 받아 오기
    private fun getClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://kdnfwewdwqzzhpafrnbq.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkbmZ3ZXdkd3F6emhwYWZybmJxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjIyNTY0MjYsImV4cCI6MjAzNzgzMjQyNn0.oeNfN9TP-axgJMeKte296B4FkEvMX4gs63k6kqWQTkE"
        ) {
            install(Postgrest)
        }
    }
}

@Serializable
data class User(
    val id: String,
    val pw: String,
    val name: String,
    val stu_num: Int
)

@Serializable
data class Subject(
    val id: String,
    val title: String,
    val dayOfWeek: String,  // 요일 속성 추가
    val start: Int,         // 시작 "교시" 수정
    val end: Int,           // 끝 "교시" 수정
    val credit: Int,
    val professor: String,
    val sub_class: String
)

@Serializable
data class Enroll(
    val user_id: String,
    val subject_id: String,
    val color: String
)

@Serializable
data class Todo(
    val id: String,
    val user_id: String,
    val subject_id: String,
    val title: String,
    val year: Int,          // date를 년 월 일로 쪼개어 표현
    val month: Int,
    val day: Int,
    val done: Boolean
)

@Serializable
data class Schedule(
    val id: String,
    val user_id: String,
    val subject_id: String,
    val title: String,
    val sub: String,
    val year: Int,          // date를 년 월 일로 쪼개어 표현
    val month: Int,
    val day: Int,
)

@Serializable
data class Shuttle(
    val id: String,
    val depart: String,
    val depart_hour: Int,      // time을 시 분으로 쪼개어 표현
    val depart_min: Int
)