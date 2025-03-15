package com.hatman.novelista.Rvtools
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hatman.novelista.R

class RvAdapterLite(private val dataSet: MutableList<RvAdapterLite.Dataframe>) : RecyclerView.Adapter<RvAdapterLite.ViewHolder>(){
    private lateinit var onClickAction:(view: View)->Unit
    // TODO:  ONCLICK DEFINED HERE IS GLOBAL
    // TODO: IF YOU MAKE A GLOBAL ONCLICK, YOU HAVE TO KNOW TO CHANGE THE ONCLICK IN OTHER AREAS
    // TODO: or the same onlick event will happen everywhere
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define click listener for the ViewHolder's View
        val title: TextView = view.findViewById(R.id.storyTitle)
        // TODO: IF YOU WANNA MAKE EVERY ONCLICK SEPARATE DEFINE THE ONCLICK HERE
        //private lateinit var onclick:(view: View)->Unit
    }
    data class Dataframe(val title: String)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_frame_lite, parent, false)
        view.findViewById<ConstraintLayout>(R.id.frameConst).setOnClickListener { onClickAction(it) }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].title
    }
    public fun setOnClickListener(onClick: (v: View) -> Unit){
        onClickAction=onClick
    }
}
