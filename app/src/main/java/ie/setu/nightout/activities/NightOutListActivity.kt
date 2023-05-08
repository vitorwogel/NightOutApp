package ie.setu.nightout.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.nightout.R
import ie.setu.nightout.adapters.NightOutAdapter
import ie.setu.nightout.adapters.NightOutListener
import ie.setu.nightout.databinding.ActivityNightoutListBinding
import ie.setu.nightout.main.MainApp
import ie.setu.nightout.models.NightOutModel
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber.i

class NightOutListActivity : AppCompatActivity(), NightOutListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityNightoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNightoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val nightOutAdapter = NightOutAdapter(app.locations.findAll() as MutableList<NightOutModel>,this)
        binding.recyclerView.adapter = nightOutAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle query submission if needed
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Filter the elements based on the new search query
                nightOutAdapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, NightOutActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.locations.findAll().size)
            }
        }

    override fun onNightOutClick(location: NightOutModel)
    {
        val launcherIntent = Intent(this, NightOutActivity::class.java)
        launcherIntent.putExtra("loc_edit", location)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.locations.findAll().size)
            }
        }

}

