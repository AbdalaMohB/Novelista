package com.hatman.novelista.database
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DB_NAME: String = "coursedb"

// below int is our database version
private val DB_VERSION: Int = 1

// below variable is for our table name.
private val TABLE_NAME: String = "mycourses"

// below variable is for our id column.
private val ID_COL: String = "id"

// below variable is for our course name column
private val NAME_COL: String = "name"

// below variable id for our course duration column.
private val DURATION_COL: String = "duration"

// below variable for our course description column.
private val DESCRIPTION_COL: String = "description"

// below variable is for our course tracks column.
private val TRACKS_COL: String = "tracks"
class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        if (db==null){
            return
        }
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DURATION_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + TRACKS_COL + " TEXT)")


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db==null){
            return
        }
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME");
        onCreate(db);
    }

}
