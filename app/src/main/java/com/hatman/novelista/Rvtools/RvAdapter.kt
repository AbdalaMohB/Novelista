package com.hatman.novelista.Rvtools
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hatman.novelista.R

class RvAdapter(private var dataSet: MutableList<Dataframe>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    private lateinit var onclick:(view: View)->Unit
    private lateinit var onsettingsclick:(view: View)->Unit
    // TODO:  ONCLICK DEFINED HERE IS GLOBAL
    // TODO: IF YOU MAKE A GLOBAL ONCLICK, YOU HAVE TO KNOW TO CHANGE THE ONCLICK IN OTHER AREAS
    // TODO: or the same onlick event will happen everywhere
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define click listener for the ViewHolder's View
        val title: TextView = view.findViewById(R.id.storyTitle)
        val frame: CardView = view.findViewById(R.id.frame)
        val date: TextView=view.findViewById(R.id.datetime)
        // TODO: IF YOU WANNA MAKE EVERY ONCLICK SEPARATE DEFINE THE ONCLICK HERE
        //private lateinit var onclick:(view: View)->Unit
    }
    data class Dataframe(val title: String, val datetime:String, val status:Int=0)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_frame, parent, false)
        view.findViewById<ConstraintLayout>(R.id.frameConst).setOnClickListener { onclick(it) }
        view.findViewById<ImageButton>(R.id.settings_button).setOnClickListener{onsettingsclick(it)}
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =dataSet.size
    public fun newDataset(newSet: MutableList<Dataframe>){
        dataSet=newSet
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].title
        holder.date.text= dataSet[position].datetime
        val status=dataSet[position].status
        val color=when(status){
            1 -> R.color.error
            2 -> R.color.done
            else -> android.R.color.transparent
        }
        holder.frame.setCardBackgroundColor(holder.frame.resources.getColor(color, holder.frame.context.theme))
    }
    public fun setOnClickListener(onclicker: (v: View) -> Unit){
        onclick=onclicker
    }
    public fun setOnSettingsClickListener(onclicker: (v: View) -> Unit){
        onsettingsclick=onclicker
    }
}