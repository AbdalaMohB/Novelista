package com.hatman.novelista.fileUtils

import android.content.Context
import java.io.File
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Date
import com.hatman.novelista.Rvtools.RvAdapter.Dataframe
object FileManager{

        fun open(file:File):String{
            val inputStream: InputStream = file.inputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val inputString = bufferedReader.use { it.readText() }
            return inputString
        }
    fun writeTo(file:File, text: String){
        file.writeText(text)
    }
        fun mkdir(parent:File, name: String): Boolean{
            val newStory= File(parent, name)
            return newStory.mkdir()
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
}