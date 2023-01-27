package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MyTag"
    private lateinit var binding: ActivityMainBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.executeTaskBTN.setOnClickListener {
            GlobalScope.launch{
                doTask()
            }
        }
    }

    suspend fun doTask() {
        // Sequential Execution    ( Sequence  -->  2, 3 ,1 )

       CoroutineScope(Dispatchers.IO).launch { task(2) }.join()

       CoroutineScope(Dispatchers.IO).launch { task(3) }.join()

       CoroutineScope(Dispatchers.IO).launch { task(1) }.join()
    }

    private suspend fun task(number: Int) {
        Log.d(TAG, "Task - $number -> Started")
        delay(1000)
        Log.d(TAG, "Task - $number -> Ended")
    }

}


/*    LogCat --> Result
MyTag                   com.example.coroutines               D  Task - 2 -> Started
MyTag                   com.example.coroutines               D  Task - 2 -> Ended
MyTag                   com.example.coroutines               D  Task - 3 -> Started
MyTag                   com.example.coroutines               D  Task - 3 -> Ended
MyTag                   com.example.coroutines               D  Task - 1 -> Started
MyTag                   com.example.coroutines               D  Task - 1 -> Ended
 */
