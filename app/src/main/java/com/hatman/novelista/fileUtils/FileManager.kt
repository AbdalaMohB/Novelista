package com.hatman.novelista.fileUtils

import android.content.Context
import java.io.File
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Date
import com.hatman.novelista.Rvtools.RvAdapter.Dataframe
import java.io.FileReader
import com.hatman.novelista.database.DBHandler
object FileManager{
        fun open(file:File):String{
            val inputStream: InputStream = file.inputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val inputString = bufferedReader.use { it.readText() }
            return inputString
        }
    fun read(file:File): BufferedReader {
        val reader : BufferedReader= BufferedReader(FileReader(file))
        return reader
    }
    fun writeTo(file:File, text: String){
        file.writeText(text)
    }
        fun mkdir(parent:File, name: String): Boolean{
            val newStory= File(parent, name)
            return newStory.mkdir()
        }
    fun mkdir(parent:File, name: String, context: Context): Boolean{
        val newStory= File(parent, name)
        if (newStory.mkdir()){
            DBHandler(context).addStory(name)
            return true
        }
        return false
    }
    fun getFile(parent:File, name: String): File{
        val item= File(parent, name)
        return item
    }
    fun remove(parent:File, name: String): Boolean{
        val item= File(parent, name)
        if (item.isDirectory) return item.deleteRecursively()
        return item.delete()
    }
    fun touch(parent:File, name: String): Boolean{
        var processedName=name
        if (!name.endsWith(".html")){
            processedName="${name}.html"
        }
        val file:File=File(parent, processedName)
        return file.createNewFile()
    }
    fun touch(parent:File, name: String, context: Context, type:Int): Boolean{
        if (touch(parent, name)){
            val p=parent.parent!!.split("/").last()
            DBHandler(context).addPart(p, name, type)
            return true
        }
        return false
    }

        fun getDirData(dir: File): MutableList<Dataframe> {
            if (dir.isDirectory){
                val datalist:MutableList<Dataframe> = mutableListOf()
                val files=dir.listFiles()
                for (file:File in files!!){
                    val date=Date(file.lastModified()).toString()
                    val name=file.name.removeSuffix(".html")

                    datalist.add(Dataframe(title = name, datetime = date))
                }
                return datalist
            }
            return mutableListOf()

        }
    fun getDirData(dir: File, context: Context, isRoot: Boolean=false): MutableList<Dataframe> {
        if (dir.isDirectory){
            val db= DBHandler(context)
            val dn=dir.name
            val datalist:MutableList<Dataframe> = mutableListOf()
            val files=dir.listFiles()
            for (file:File in files!!){
                val date=Date(file.lastModified()).toString()
                val name=file.name.removeSuffix(".html")
                if (isRoot) {
                    val status=db.getStoryStatus(name)
                    datalist.add(Dataframe(title = name, datetime = date, status = status))
                }
                else{
                    val type=when(dn){
                        "chapters" -> 0
                        "ideas" -> 1
                        "characters" -> 2
                        else -> -1
                    }
                    val par=dir.parent!!.split("/").last()
                    val status=db.getPartStatus(name, par, type)
                    datalist.add(Dataframe(title = name, datetime = date, status = status))
                }
            }
            return datalist
        }
        return mutableListOf()

    }
}