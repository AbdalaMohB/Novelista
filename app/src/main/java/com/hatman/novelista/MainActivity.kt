package com.hatman.novelista

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hatman.novelista.Rvtools.RvAdapter
import com.hatman.novelista.database.DBHandler
import com.hatman.novelista.fileUtils.FileManager
import com.hatman.novelista.popups.StoryDialog
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var popup:PopupMenu
    private lateinit var dialog: StoryDialog
    private lateinit var stories: File
    lateinit var customAdapter: RvAdapter
    lateinit var db: DBHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar=findViewById<Toolbar>(R.id.mainToolBar)
        val icon=ResourcesCompat.getDrawable(resources, R.drawable.settings_24px, theme)
        icon?.setTint(getColor(R.color.clicked))
        toolbar.overflowIcon=icon
        setSupportActionBar(toolbar)
        findViewById<ImageButton>(R.id.mainSettingsButton).setOnClickListener {
            toSettings()
        }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            addStory()
        }
        db=DBHandler(this)
        dialog= StoryDialog{ it ->
           FileManager.mkdir(stories, it, this)
            val story= FileManager.getFile(stories, it)
            FileManager.mkdir(story, "chapters")
            FileManager.mkdir(story, "ideas")
            FileManager.mkdir(story, "characters")
            customAdapter.newDataset(getDataset())
            customAdapter.notifyItemInserted(customAdapter.itemCount-1)
        }
        stories=getDir("Stories", MODE_APPEND)
        customAdapter= RvAdapter(getDataset())
        populateRv()
    }
private fun showMenu(v: View){
    popup = PopupMenu(this, v)
    val inflater: MenuInflater = popup.menuInflater
    inflater.inflate(R.menu.story_settings_menu, popup.menu)
    popup.show()
    popup.setOnMenuItemClickListener {onMenuItemClick(it, v.parent as View)}
}
    private fun toStory(view: View){
        val intent=Intent(this@MainActivity, StoryActivity::class.java)
        val storyName=view.findViewById<TextView>(R.id.storyTitle).text.toString()
        intent.putExtra("name", storyName)
        startActivity(intent)
    }
    private fun toSettings(){
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
    }

//    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
//        // TODO: MAKE THIS SEND YOU TO SETTINGS ACTIVITY
//        //startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//       // menuInflater.inflate(R.menu.empty_menu, menu)
//        return true
//    }

//    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
//        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//        return true
//        //return super.onMenuOpened(featureId, menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // TODO: add appbar menu functionality here
//        return when (item.itemId) {
//            R.id.item1 -> {
//
//                true
//            }
//            R.id.item2 -> {
//
//                true
//            }
//            else -> false
//        }
//        //return super.onOptionsItemSelected(item)
//    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onMenuItemClick(item: MenuItem, parent:View): Boolean {
        val title=parent.findViewById<TextView>(R.id.storyTitle).text.toString()
        return when (item.itemId) {
            R.id.del -> {
                FileManager.remove(stories, title)
                db.deleteStory(title)
                customAdapter.newDataset(getDataset())
                customAdapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }
    private fun populateRv(){
        customAdapter.setOnClickListener {
            toStory(it)
        }
        customAdapter.setOnSettingsClickListener{ v ->
            showMenu(v)
        }
        val recyclerView: RecyclerView = findViewById(R.id.listrv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
    private fun getDataset(): MutableList<RvAdapter.Dataframe>{
        return FileManager.getDirData(stories, this, true)
    }

    private fun addStory(){
        dialog.show(supportFragmentManager, "STORY_DIALOG")
    }

    override fun onResume() {
        super.onResume()
        //TODO! add a way to check if something changed and what changed if possible
        // so we don't have to rerender everything
        customAdapter.newDataset(getDataset())
        customAdapter.notifyDataSetChanged()
    }
    override fun onDestroy() {
        db.closeDB()
        super.onDestroy()
    }
}