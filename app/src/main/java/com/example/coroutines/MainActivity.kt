package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MyTag"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.executeTaskBTN.setOnClickListener {
            // if you want to block your Thread --> use runBlocking {  }

            Log.d(TAG, "Working Start")
            runBlocking (Dispatchers.IO){
                launch { task(1) }
                launch { task(2) }
            }

            // When coroutine suspend after task 2 started, But Line No - 29 not execute
             //  Bcz our thread, which is used by coroutine, is still blocked
            Log.d(TAG, "Working End")
        }
    }

    private suspend fun task(number: Int) {
        Log.d(TAG, "Task - $number -> Started")
        delay(1000)
        Log.d(TAG, "Task - $number -> Ended")
    }
}

/*   LogCat Results
MyTag                   com.example.coroutines               D  Working Start

MyTag                   com.example.coroutines               D  Task - 1 -> Started
MyTag                   com.example.coroutines               D  Task - 2 -> Started
MyTag                   com.example.coroutines               D  Task - 1 -> Ended
MyTag                   com.example.coroutines               D  Task - 2 -> Ended

MyTag                   com.example.coroutines               D  Working End

 */