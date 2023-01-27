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

    private suspend fun doTask() {
        // Sequential Execution    ( Sequence  -->  2, 3 ,1 )

        /*   CoroutinesScope --> LifeTime Of A Coroutines
            Coroutines Context --> Execute coroutines in Thread or ThreadPools
         */

        /* Coroutine Builders -->
         launch - "Fire or Forget" , return a job type Object
         async - wait for result, return a Deferred type Object
         */
        CoroutineScope(Dispatchers.IO).launch {
            // Parent Child RelationShip

            /*  **** Children ****  */
            val job2 = async { task(2) }
            job2.await()
            val job3 = async { task(3) }
            job3.await()
            val job1 = async { task(1) }
            job1.await()

        }


  //  --> Your Suitability
         /*               or
        
        val job2 =  CoroutineScope(Dispatchers.IO).async {
            task(2)
        }
        job2.await()

        val job3 =  CoroutineScope(Dispatchers.IO).async {
            task(3)
        }
        job3.await()

        val job1 =  CoroutineScope(Dispatchers.IO).async {
            task(1)
        }
        job1.await()
         */

         
  //          or

         /* 
         CoroutineScope(Dispatchers.IO).launch {
            // Parent Child RelationShip
            
            task(2)
             task(3)
              task(1)
              }
            
         */
         

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
