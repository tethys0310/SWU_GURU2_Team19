package com.guru2.todolist

import android.content.Intent
import android.widget.Button

class HomeActivity {

    val buttonTodo : Button = findViewById(R.id.button_todo)

    buttonTodo.setOnClickListener {
        val intent = Intent(this, TodoActivityTest::class.java)
        startActivity(intent)
    }


}