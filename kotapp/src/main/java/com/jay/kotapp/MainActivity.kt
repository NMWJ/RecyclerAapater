package com.jay.kotapp

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jay.kotapp.bean.User

import com.jay.kotapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)

        binding.user = User("Test","User")

    }
}
