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

            /******   CoroutinesScope --> LifeTime Of A Coroutines
                    Coroutines Context --> Execute coroutines in Thread or ThreadPools
             ******/
            GlobalScope.launch {
                doTask()
            }
        }
    }

    private suspend fun doTask() {
        /* Coroutine Builders -->
         launch - "Fire or Forget" , return a job type Object
         async - wait for result, return a Deferred type Object
         */

        CoroutineScope(Dispatchers.IO).launch {
            /*    Sequential Task Execution    */

            // Parent Child RelationShip
            Log.d(TAG, "Parent -> Started")
            Log.d(TAG, "Parent, Thread Name -> ${Thread.currentThread().name}")

            /*  **** Children ****  */
            withContext(Dispatchers.Main) { task(1) }
            withContext(Dispatchers.IO) { task(2) }
            withContext(Dispatchers.Default) { task(3) }

            Log.d(TAG, "Parent -> Ended")
        }

        withContext(Dispatchers.Main) { task(4) }

        delay(5000)
        Log.d(TAG, "Parent -> Completed")

    }

    private suspend fun task(number: Int) {
        Log.d(TAG, "Task - $number -> Started")
        Log.d(TAG, "Task $number  Thread Name -> ${Thread.currentThread().name}")
        delay(1000)
        Log.d(TAG, "Task - $number -> Ended")
    }


}


/*    LogCat --> Result
 MyTag                   com.example.coroutines               D  Parent -> Started
 MyTag                   com.example.coroutines               D  Parent, Thread Name -> DefaultDispatcher-worker-2
                    // Tasks; 1 -> 2 -> 3 :  executes sequentially
 MyTag                   com.example.coroutines               D  Task - 4 -> Started
 MyTag                   com.example.coroutines               D  Task 4  Thread Name -> main
 MyTag                   com.example.coroutines               D  Task - 1 -> Started
 MyTag                   com.example.coroutines               D  Task 1  Thread Name -> main
 MyTag                   com.example.coroutines               D  Task - 4 -> Ended
 MyTag                   com.example.coroutines               D  Task - 1 -> Ended

 MyTag                   com.example.coroutines               D  Task - 2 -> Started
 MyTag                   com.example.coroutines               D  Task 2  Thread Name -> DefaultDispatcher-worker-2
 MyTag                   com.example.coroutines               D  Task - 2 -> Ended

 MyTag                   com.example.coroutines               D  Task - 3 -> Started
 MyTag                   com.example.coroutines               D  Task 3  Thread Name -> DefaultDispatcher-worker-2
 MyTag                   com.example.coroutines               D  Task - 3 -> Ended

 MyTag                   com.example.coroutines               D  Parent -> Ended
 MyTag                   com.example.coroutines               D  Parent -> Completed

 */
