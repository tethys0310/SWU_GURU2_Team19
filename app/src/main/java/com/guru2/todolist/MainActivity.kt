package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonTodo : Button = findViewById(R.id.button_todo)

        buttonTodo.setOnClickListener {
            val intent = Intent(this, TodoActivityTest::class.java)
            startActivity(intent)
        }

    }
}