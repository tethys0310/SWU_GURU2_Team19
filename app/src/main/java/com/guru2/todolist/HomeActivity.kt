package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : MenuTestActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        setupBottomNavigationBar(R.id.nav_home)
//        val buttonTodo: Button = findViewById(R.id.button_todo)
//
//        buttonTodo.setOnClickListener {
//            val intent = Intent(this, TodoActivity::class.java)
//            startActivity(intent)
//
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}
   