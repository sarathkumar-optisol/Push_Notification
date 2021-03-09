package com.example.pushnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pushnotification.databinding.ActivityMainBinding
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


const val TOPIC = "/topics/myTopic"
class MainActivity : AppCompatActivity() {

    val TAG = "MAINACTIVITY"
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.button.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG,"Response : ${Gson().toJson(response)}")
            }else{
                Log.d(TAG,response.errorBody().toString())
            }

        }catch (e : Exception){
            Log.d(TAG,e.toString())
        }
    }
}