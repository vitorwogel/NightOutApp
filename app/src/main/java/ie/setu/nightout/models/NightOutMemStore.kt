package ie.setu.nightout.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class NightOutMemStore : NightOutStore {

    val locations = ArrayList<NightOutModel>()

    override fun findAll(): List<NightOutModel> {
        return locations
    }

    override fun create(location: NightOutModel) {
        location.id = getId()
        locations.add(location)
        logAll()
    }

    override fun update(location: NightOutModel) {
        var foundLocation: NightOutModel? = locations.find { p -> p.id == location.id }
        if (foundLocation != null) {
            foundLocation.title = location.title
            foundLocation.description = location.description
            foundLocation.image = location.image
            foundLocation.rating = location.rating
            logAll()
        }
    }

    private fun logAll() {
        locations.forEach { i("$it") }
    }
}