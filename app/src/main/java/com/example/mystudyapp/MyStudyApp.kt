package com.example.mystudyapp

import android.app.Application

class MyStudyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // baca pengaturan terakhir dan terapkan ke seluruh aplikasi
        SettingsManager.terapkanKeAplikasi(this)
    }
}