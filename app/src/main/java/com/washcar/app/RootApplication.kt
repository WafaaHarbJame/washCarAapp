package com.washcar.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.messaging.FirebaseMessaging
import com.washcar.app.Utils.SharedPManger
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import java.util.*


class RootApplication : LocalizationApplication() {


    @get:Synchronized
    var sharedPManger: SharedPManger? = null

    companion object {

        var instance: RootApplication? = null
            private set
        var changesMap: MutableMap<String, Any> =
            HashMap()

        var fireStoreDB: FirebaseFirestore? = null


        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }


    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getDefaultLanguage(base: Context): Locale {
        return Locale("en")

    }

//    override fun getDefaultLanguage(base: Context): Locale {
//        TODO("Not yet implemented")
//
//
//    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPManger = SharedPManger(this)

        fireStoreDB = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .build()
        fireStoreDB?.firestoreSettings = settings


        var appLanguage = UtilityApp.language
        if (appLanguage == null)
            appLanguage = Constants.English
        UtilityApp.language = appLanguage

        FirebaseMessaging.getInstance().subscribeToTopic("promotional_$appLanguage")

        instance = this
    }

    fun getDefaultLanguage(): Locale {
        return Locale("en")
    }

}