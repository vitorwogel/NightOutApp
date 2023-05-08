package ie.setu.nightout.main

import android.app.Application
import ie.setu.nightout.models.NightOutMemStore
import ie.setu.nightout.models.NightOutStore
import ie.setu.nightout.models.NightOutJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var places: NightOutStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        places = NightOutJSONStore(applicationContext)
        i("NightOut started")
    }
}
