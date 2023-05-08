package ie.setu.nightout.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.nightout.R
import ie.setu.nightout.databinding.ActivityNightoutBinding
import ie.setu.nightout.helpers.showImagePicker
import ie.setu.nightout.main.MainApp
import ie.setu.nightout.models.Location
import ie.setu.nightout.models.NightOutModel
import timber.log.Timber.i

class NightOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNightoutBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = NightOutModel()
    lateinit var app: MainApp
    var actuaLocation = Location(-22.9587255,-44.3069911,15f)
    private var mRating = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        var edit = false

        super.onCreate(savedInstanceState)
        binding = ActivityNightoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarCancel.title = title
        setSupportActionBar(binding.toolbarCancel)

        app = application as MainApp
        i("NightOut Activity started...")

        if (intent.hasExtra("loc_edit")) {
            edit = true
            location = intent.extras?.getParcelable("loc_edit")!!
            binding.locTitle.setText(location.title)
            binding.description.setText(location.description)
            binding.btnAdd.setText(R.string.save_location)
            binding.chooseImage.setText(R.string.change_image)
            Picasso.get()
                .load(location.image)
                .into(binding.locImage)
            binding.ratingBar.numStars = mRating
        }

        binding.btnAdd.setOnClickListener() {
            location.title = binding.locTitle.text.toString()
            location.description = binding.description.text.toString()
            location.rating = mRating
            if (location.title.isEmpty()) {
                Snackbar.make(it,R.string.error_handler, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.places.update(location.copy())
                } else {
                    app.places.create(location.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.locLocation.setOnClickListener {
            i ("Set Location Pressed")
        }

        registerImagePickerCallback()

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        registerMapCallback()

        binding.locLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", actuaLocation)
            mapIntentLauncher.launch(launcherIntent)
        }

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            mRating = rating.toInt()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cancel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                val launcherIntent = Intent(this, NightOutListActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            finish()
        }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            location.image = image

                            Picasso.get()
                                .load(location.image)
                                .into(binding.locImage)
                            binding.chooseImage.setText(R.string.change_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            actuaLocation = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $actuaLocation")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}


