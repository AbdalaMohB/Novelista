package com.hatman.novelista

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import com.hatman.novelista.Rvtools.RvAdapter
import com.hatman.novelista.ui.main.SectionsPagerAdapter
import com.hatman.novelista.databinding.ActivityStoryBinding
import com.hatman.novelista.fileUtils.FileManager
import com.hatman.novelista.popups.StoryDialog
import java.io.File

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var dialog: StoryDialog
    private lateinit var parts: List<File>
    private lateinit var adapters: List<RvAdapter>
    private var current=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyName= intent.extras?.getString("name").toString()
        val story=File(getDir("Stories", MODE_APPEND), storyName)
        parts=listOf(
            File(story, "chapters"),
            File(story, "ideas"),
            File(story, "characters")
        )
        val data=getDataset()
        adapters = listOf(
            RvAdapter(data[0]),
            RvAdapter(data[1]),
            RvAdapter(data[2]),
        )
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, storyName, adapters)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab
        dialog= StoryDialog{ it ->
            FileManager.touch(parts[current], it)
            val datas=when(current){
                0 -> getChaps()
                1 -> getIdeas()
                2 -> getChars()
                else -> mutableListOf()
            }
            adapters[current].newDataset(datas)
            adapters[current].notifyItemInserted(datas.size-1)
        }
        fab.setOnClickListener { view ->
            current= viewPager.currentItem
            dialog.show(supportFragmentManager, "STORY_DIALOG")
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()

        }
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