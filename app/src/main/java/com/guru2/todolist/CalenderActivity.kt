package com.guru2.todolist

import android.annotation.SuppressLint
import java.io.FileInputStream
import java.io.FileOutputStream
import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class CalenderActivity : MenuTestActivity() {
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var calendarView: CalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button
    lateinit var saveBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var title: TextView
    lateinit var contextEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        setupBottomNavigationBar(R.id.nav_todo)

        // UI값 생성
        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)
        saveBtn = findViewById(R.id.saveBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        updateBtn = findViewById(R.id.updateBtn)
        diaryContent = findViewById(R.id.diaryContent)
        title = findViewById(R.id.title)
        contextEditText = findViewById(R.id.contextEditText)

        title.text = "SWUCHEDULE"

        // 투두 데이터를 가져옴
        loadTodoData()

        // 버튼 클릭 리스너 설정
        saveBtn.setOnClickListener {
            val content = contextEditText.text.toString()
            if (content.isNotEmpty()) {
                saveDiary(fname, content)
                contextEditText.visibility = View.INVISIBLE
                saveBtn.visibility = View.INVISIBLE
                updateBtn.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE
                diaryContent.text = content
                diaryContent.visibility = View.VISIBLE
            }
        }

        updateBtn.setOnClickListener {
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            contextEditText.setText(diaryContent.text)
            saveBtn.visibility = View.VISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        }

        deleteBtn.setOnClickListener {
            removeDiary(fname)
            // 삭제 후 UI 업데이트
            contextEditText.setText("")
            contextEditText.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        }
    }

    // Supabase 클라이언트 생성
    private fun getClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://kdnfwewdwqzzhpafrnbq.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkbmZ3ZXdkd3F6emhwYWZybmJxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjIyNTY0MjYsImV4cCI6MjAzNzgzMjQyNn0.oeNfN9TP-axgJMeKte296B4FkEvMX4gs63k6kqWQTkE"
        ) {
            install(Postgrest)
        }
    }

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String, todoMap: Map<Int, List<Todo>>) {
        fname = "$userID$cYear-${cMonth}-$cDay.txt"
        var fileInputStream: FileInputStream
        try {
            fileInputStream = openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)

            // 날짜를 Int형으로 변환
            val selectedDate = cYear * 10000 + cMonth * 100 + cDay
            val todosForSelectedDate = todoMap[selectedDate]

            if (str.isEmpty() && (todosForSelectedDate == null || todosForSelectedDate.isEmpty())) {
                contextEditText.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
            } else {
                val todoDescriptions = todosForSelectedDate?.joinToString("\n") { it.title.orEmpty() } ?: ""
                contextEditText.visibility = View.INVISIBLE
                diaryContent.visibility = View.VISIBLE
                diaryContent.text = "$str\n$todoDescriptions"
                saveBtn.visibility = View.INVISIBLE
                updateBtn.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            contextEditText.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        }
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        try {
            deleteFile(readDay)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?, content: String) {
        try {
            val fileOutputStream = openFileOutput(readDay, MODE_PRIVATE)
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 투두 데이터를 가져오는 함수
    private suspend fun getTodoData(): Map<Int, List<Todo>> {
        val todoMap = mutableMapOf<Int, List<Todo>>()
        try {
            val client = getClient()
            val supabaseResponse = client.postgrest["todos"].select()
            val data: List<Todo> = supabaseResponse.decodeList<Todo>()
            Log.d("supabase", data.toString())
            todoMap.putAll(data.groupBy { it.day })
        } catch (e: Exception) {
            Log.e("supabase", "Todo 받아오기 중 에러 발생", e)
        }
        return todoMap
    }

    // 데이터 로드
    private fun loadTodoData() {
        lifecycleScope.launch {
            val todoMap = getTodoData()
            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                diaryTextView.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                contextEditText.visibility = View.VISIBLE
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
                contextEditText.setText("")
                checkDay(year, month + 1, dayOfMonth, userID, todoMap)
            }
        }
    }
}
