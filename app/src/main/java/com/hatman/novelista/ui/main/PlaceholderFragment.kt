package com.hatman.novelista.ui.main

import android.content.Context.MODE_APPEND
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatman.novelista.EditorActivity
import com.hatman.novelista.R
import com.hatman.novelista.Rvtools.RvAdapter
import com.hatman.novelista.databinding.FragmentStoryBinding
import com.hatman.novelista.fileUtils.FileManager
import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(val storyName:String, var adapters: List<RvAdapter>) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentStoryBinding? = null
    private var extras=activity?.intent?.extras
    private lateinit var story: File
    private lateinit var parts: List<File>
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerviewlo: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        story=File(activity?.getDir("Stories", MODE_APPEND), storyName)
        parts=listOf(
            File(story, "chapters"),
            File(story, "ideas"),
            File(story, "characters")
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        val root = binding.root
        // TODO: LINE BELOW THIS IS WHERE WE NEED TO CHANGE STUFF  
         recyclerviewlo = binding.listfs
        //val dataset = getDataset()


        val recyclerView: RecyclerView = root.findViewById(R.id.listfs)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        pageViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
            val customAdapter = adapters[it-1]
            customAdapter.setOnClickListener {v -> toEditor(it, v.parent as View) }
            customAdapter.setOnSettingsClickListener { v -> showMenu(v, it-1) }
            recyclerView.adapter = customAdapter
        })
        return root
    }
    private fun toEditor(page: Int, view: View){
        val intent=Intent(this.context, EditorActivity::class.java)
        val itemName=view.findViewById<TextView>(R.id.storyTitle).text.toString()
        intent.putExtra("name", itemName)
        intent.putExtra("story", storyName)
        intent.putExtra("page", page)
        startActivity(intent)
    }
    private fun showMenu(v: View, idx: Int) {

            val popup = androidx.appcompat.widget.PopupMenu(this.requireContext(), v)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.settings_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener { onMenuItemClick(it, v.parent as View, idx) }

    }

    private fun onMenuItemClick(item: MenuItem, view: View, page: Int): Boolean {
        val title=view.findViewById<TextView>(R.id.storyTitle).text.toString()
        return when (item.itemId) {
            R.id.item1 -> {

                true
            }
            R.id.item2 -> {
                FileManager.remove(parts[page], "${title}.html")
                val data=when(page){
                    0 -> getChaps()
                    1 -> getIdeas()
                    2 -> getChars()
                    else -> mutableListOf()
                }
                adapters[page].newDataset(data)
                adapters[page].notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, storyName: String, adapters: List<RvAdapter>): PlaceholderFragment {
            return PlaceholderFragment(storyName, adapters).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    public fun reset(idx: Int){
        val data=when(idx){
            0 -> getChaps()
            1 -> getIdeas()
            2 -> getChars()
            else -> mutableListOf()
        }
        adapters[idx].newDataset(data)
        adapters[idx].notifyItemInserted(adapters[idx].itemCount-1)
    }
    fun getDataset(): List<MutableList<RvAdapter.Dataframe>>{
       return listOf(
           FileManager.getDirData(parts[0]),
           FileManager.getDirData(parts[1]),
           FileManager.getDirData(parts[2]),

        )
    }
    fun getChaps(): MutableList<RvAdapter.Dataframe>{
        return FileManager.getDirData(parts[0])

    }
    fun getIdeas(): MutableList<RvAdapter.Dataframe>{
        return FileManager.getDirData(parts[1])
    }
    fun getChars():MutableList<RvAdapter.Dataframe>{
        return FileManager.getDirData(parts[2])
    }
}