package com.guru2.todolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

open class MenuTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    protected fun setupBottomNavigationBar(selectedItemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_main)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_todo -> {
                    val intent = Intent(this, TodoActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_calender -> {
                    val intent = Intent(this, CalenderActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        bottomNavigationView.selectedItemId = selectedItemId
    }
}