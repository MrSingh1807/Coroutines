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

            /*   CoroutinesScope --> LifeTime Of A Coroutines
                Coroutines Context --> Execute coroutines in Thread or ThreadPools
            */

            GlobalScope.launch {
                doTask()
            }
        }
    }

    private suspend fun doTask() {
        // Sequential Execution    ( Sequence  -->  2, 3 ,1 )


        /* Coroutine Builders -->
         launch - "Fire or Forget" , return a job type Object
         async - wait for result, return a Deferred type Object
         */
        CoroutineScope(Dispatchers.IO).launch {
            // Parent Child RelationShip

            Log.d(TAG, "Parent -> Started")
            Log.d(TAG, Thread.currentThread().name)


            /*  **** Children ****  */
            launch { task(1) }.join()
            launch { task(2) }
            // Scenario Case --> Task - 2 will not start until Task- 1 is complete


            Log.d(TAG, "Parent -> Ended")
        }

        /*
        if condition - task 3 will not start until parent complete, than use {.join()} function
         */

        coroutineScope { task(3) }      // Task - 3, is not bound

        delay(5000)
        Log.d(TAG, "Parent -> Completed")


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