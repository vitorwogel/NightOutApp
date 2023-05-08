package ie.setu.nightout.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.nightout.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "placemarks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<NightOutModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class NightOutJSONStore(private val context: Context) : NightOutStore {

    var places = mutableListOf<NightOutModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<NightOutModel> {
        logAll()
        return places
    }

    override fun create(place: NightOutModel) {
        place.id = generateRandomId()
        places.add(place)
        serialize()
    }


    override fun update(place: NightOutModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(places, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        places = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        places.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
