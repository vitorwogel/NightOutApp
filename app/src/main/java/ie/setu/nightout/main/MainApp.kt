package ie.setu.nightout.main

import android.app.Application
import ie.setu.nightout.models.NightOutMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val locations = NightOutMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("NightOut started")
    }
}
