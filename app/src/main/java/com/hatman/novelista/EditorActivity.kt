package com.hatman.novelista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hatman.novelista.EditorUtils.EditorSetup
import com.hatman.novelista.EditorUtils.RoutineHandler
import com.hatman.novelista.fileUtils.FileManager
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.takeWhile
import java.io.File

class EditorActivity : AppCompatActivity() {
    // TODO: Make this activity take a filename in intent extras and have it read file for html
    lateinit var file:File
    lateinit var editor: EditorSetup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        val extras=intent.extras
        val storyName: String=extras?.getString("story").toString()
        val fileName: String=extras?.getString("name").toString()
        var page: Int=extras?.getInt("page")!!.toInt()
        var root=getDir("Stories", MODE_APPEND)
        val pageName=when(page){
            0 -> "chapters"
            1 -> "ideas"
            2 -> "characters"
            else -> ""
        }
        root=FileManager.getFile(root, storyName)
        root= FileManager.getFile(root, pageName)
        file= FileManager.getFile(root, "${fileName}.html")
        editor=EditorSetup(findViewById(R.id.edlay), readFile())
    }

    override fun onDestroy() {
        // TODO: save file on destroy
        RoutineHandler.newCoroutine {
            val content = editor.getText()
            if (compare(content)) {
                FileManager.writeTo(file, content)
            }
        }
        super.onDestroy()
    }
    fun readFile(): String{
        return FileManager.open(file)
    }
    fun compare(html: String): Boolean {
        val reader=FileManager.read(file)
        var ch=reader.read()
        var idx=0
        val size=html.length
        while (ch != -1){
            if (idx==size){
                return true
            }
            if ( html[idx].code !=ch){
                return true
            }
            ch=reader.read()
            idx+=1
        }
        return idx!=size
    }
}