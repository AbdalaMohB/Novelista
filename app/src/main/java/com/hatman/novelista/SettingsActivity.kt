package com.hatman.novelista

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatman.novelista.Rvtools.RvAdapterLite

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        populateRv()
    }
    private fun populateRv(){
        val customAdapter = RvAdapterLite(getDataset())
        customAdapter.setOnClickListener {
           //toStory()
        }
        val recyclerView: RecyclerView = findViewById(R.id.listrv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
    private fun getDataset(): MutableList<RvAdapterLite.Dataframe>{
        return mutableListOf(
            RvAdapterLite.Dataframe("Hello"),
            RvAdapterLite.Dataframe("lmao"),
            RvAdapterLite.Dataframe("stuff"))
    }
}