package ie.setu.nightout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ie.setu.nightout.databinding.CardNightoutBinding
import ie.setu.nightout.models.NightOutModel
import com.squareup.picasso.Picasso
import java.util.*

class NightOutAdapter constructor(private var locations: MutableList<NightOutModel>,
                                   private val listener: NightOutListener
) :
    RecyclerView.Adapter<NightOutAdapter.MainHolder>(), Filterable {

    private var dataListFiltered: MutableList<NightOutModel> = locations

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardNightoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val location = locations[holder.adapterPosition]
        holder.bind(location, listener)
    }

    override fun getItemCount(): Int = locations.size

    class MainHolder(private val binding : CardNightoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(location: NightOutModel, listener: NightOutListener) {
            if (location.title.length > 14) {
                location.title = location.title.substring(0, 11) + "..."
            }
            binding.locationTitle.text = location.title
            binding.description.text = location.description
            Picasso.get().load(location.image).resize(200,200).into(binding.imageIcon)
            binding.ratingTextView.text = location.rating.toString()
            binding.root.setOnClickListener { listener.onNightOutClick(location) }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<NightOutModel>()
                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(locations)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (item in locations) {
                        if (item.title.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataListFiltered.clear()
                dataListFiltered.addAll(results?.values as MutableList<NightOutModel>)
                notifyDataSetChanged()
            }
        }
    }
}


interface NightOutListener {
    fun onNightOutClick(location: NightOutModel)
}
