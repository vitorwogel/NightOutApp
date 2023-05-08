package ie.setu.nightout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.nightout.databinding.CardNightoutBinding
import ie.setu.nightout.models.NightOutModel
import com.squareup.picasso.Picasso

class NightOutAdapter constructor(private var locations: List<NightOutModel>,
                                   private val listener: NightOutListener
) :
    RecyclerView.Adapter<NightOutAdapter.MainHolder>() {

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
}


interface NightOutListener {
    fun onNightOutClick(location: NightOutModel)
}
