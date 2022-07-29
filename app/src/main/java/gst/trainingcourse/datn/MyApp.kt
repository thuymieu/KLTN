package gst.trainingcourse.datn

import android.app.Application

class MyApp : Application(){
    companion object{
        private lateinit var appContext: Application
        fun getContext() = appContext
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}