package com.mindmatrix.nammakathey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeroAdapter(
    private val heroes: List<Hero>,
    private val onClick: (Hero) -> Unit
) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHero: ImageView = view.findViewById(R.id.imgHero)
        val tvDistrict: TextView = view.findViewById(R.id.tvDistrict)
        val tvHeroName: TextView = view.findViewById(R.id.tvHeroName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hero, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        holder.tvDistrict.text = "District: ${hero.district}"
        holder.tvHeroName.text = hero.name_en
        
        // Dynamically load image resource if it exists
        val resId = holder.itemView.context.resources.getIdentifier(hero.image_url, "drawable", holder.itemView.context.packageName)
        if (resId != 0) {
            holder.imgHero.setImageResource(resId)
        }

        holder.itemView.setOnClickListener {
            onClick(hero)
        }
    }

    override fun getItemCount() = heroes.size
}
