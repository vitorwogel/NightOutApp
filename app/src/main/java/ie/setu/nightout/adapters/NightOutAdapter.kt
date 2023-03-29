package ie.setu.nightout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.nightout.databinding.CardNightoutBinding
import ie.setu.nightout.models.PlacemarkModel
import com.squareup.picasso.Picasso

class PlacemarkAdapter constructor(private var placemarks: List<PlacemarkModel>,
                                   private val listener: PlacemarkListener
) :
    RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardNightoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder(private val binding : CardNightoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(placemark: PlacemarkModel, listener: PlacemarkListener) {
            binding.locationTitle.text = placemark.title
            binding.description.text = placemark.description
            Picasso.get().load(placemark.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPlacemarkClick(placemark) }
        }


    }
}


interface PlacemarkListener {
    fun onPlacemarkClick(placemark: PlacemarkModel)
}
