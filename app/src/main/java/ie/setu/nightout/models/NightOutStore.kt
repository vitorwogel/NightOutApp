package ie.setu.nightout.models

interface NightOutStore {
    fun findAll(): List<NightOutModel>
    fun create(location: NightOutModel)
    fun update(location: NightOutModel)
}
