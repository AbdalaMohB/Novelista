package com.hatman.novelista.database
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DB_NAME: String = "statusdb"

// below int is our database version
private val DB_VERSION: Int = 1

// below variable is for our table name.
private val TABLE_STORIES: String = "stories"
private val TABLE_PARTS: String = "parts"

// below variable is for our id column.
private val ID_COL: String = "id"

// below variable is for our course name column
private val NAME_COL: String = "name"

// below variable id for our course duration column.
// below variable is for our course tracks column.
private val TRACKS_COL: String = "tracks"
class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    var rdb: SQLiteDatabase = readableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        if (db==null){
            return
        }
//        val query1 = ("CREATE TABLE " + TABLE_NAME + " ("
//                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + NAME_COL + " TEXT,"
//                + DURATION_COL + " TEXT,"
//                + DESCRIPTION_COL + " TEXT,"
//                + TRACKS_COL + " TEXT)")
        val query2 = ("CREATE TABLE \"partTypes\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"type\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");")
        val query3= ("CREATE TABLE \"status\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");")
        val query4=("CREATE TABLE \"stories\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");")
        val query5=("CREATE TABLE \"parts\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"type\"\tINTEGER NOT NULL,\n" +
                "\t\"parent\"\tINTEGER NOT NULL,\n" +
                "\t\"status\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"parent\") REFERENCES \"stories\"(\"id\"),\n" +
                "\tFOREIGN KEY(\"status\") REFERENCES \"status\"(\"id\"),\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT),\n" +
                "\tFOREIGN KEY(\"type\") REFERENCES \"partTypes\"(\"id\")\n" +
                ");")
        val query6=("INSERT INTO partTypes (id, type)\n" +
                "values \n" +
                "(0, \"chapters\") ,\n" +
                "(1,  \"ideas\"),\n" +
                " (2, \"characters\")")
        val query7=("INSERT INTO status (id, name)\n" +
                "values \n" +
                "(0, \"unlabelled\") ,\n" +
                "(1,  \"error\"),\n" +
                " (2, \"finished\")")
        db.execSQL(query2)
        db.execSQL(query3)
        db.execSQL(query4)
        db.execSQL(query5)
        db.execSQL(query6)
        db.execSQL(query7)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db==null){
            return
        }
        db.execSQL("DROP TABLE IF EXISTS stories");
        db.execSQL("DROP TABLE IF EXISTS parts");
        db.execSQL("DROP TABLE IF EXISTS partTypes");
        db.execSQL("DROP TABLE IF EXISTS status");
        onCreate(db);
    }
    public fun addStory(storyName: String){
        //val db = writableDatabase;


        val values = ContentValues();


        values.put(NAME_COL, storyName);

        // after adding all values we are passing
        // content values to our table.
        rdb.insert(TABLE_STORIES, null, values);

        // at last we are closing our
        // database after adding database.
        //rdb.close()
    }
    public fun deleteStory(storyName: String){
        //val db = writableDatabase;
        deleteParts(storyName)
        rdb.execSQL("DELETE FROM stories WHERE name = \"$storyName\"")
        // at last we are closing our
        // database after adding database.
        //db.close()
    }
    public fun addPart(storyName: String, partName: String, partType: Int){
        val values = ContentValues();
        values.put(NAME_COL, partName);
        values.put("type", partType);
        values.put("status", 0);
        values.put("parent", getStoryId(storyName));
        // after adding all values we are passing
        // content values to our table.
        rdb.insert(TABLE_PARTS, null, values);

        // at last we are closing our
        // database after adding database.
    }
    public fun deletePart(storyName: String, partName: String, partType: Int){
        val id=getPartId(storyName, partName, partType)
        rdb.execSQL("DELETE FROM parts WHERE id = $id")
        // at last we are closing our
        // database after adding database.
    }
    public fun deleteParts(storyName: String){
        //val db = writableDatabase;
        val pid=getStoryId(storyName)
        rdb.execSQL("DELETE FROM parts WHERE parent = $pid");
        // at last we are closing our
        // database after adding database.
        //db.close()
    }
    public fun updatePartStatus(storyName: String,
                                partName: String, partType: Int,
                                newStatus:Int){
        val values = ContentValues();
        val id=getPartId(storyName, partName, partType)
        values.put(NAME_COL, partName);
        values.put("type", partType);
        values.put("status", newStatus);
        values.put("parent", getStoryId(storyName));
        // after adding all values we are passing
        // content values to our table.
        rdb.update(TABLE_PARTS, values, "id=?", arrayOf("$id"));

        // at last we are closing our
        // database after adding database.
    }
    public fun getStoryId(storyName: String): Int{
        //val db = readableDatabase;
        val query=rdb.rawQuery("SELECT id FROM $TABLE_STORIES WHERE name = \"$storyName\"", null)
        query.moveToFirst()
        if (query.count==0) return -1
        val id=query.getInt(0)
        query.close()
        //db.close()
        return id
    }
    public fun getPartId(storyName: String,
                         partName: String, partType: Int): Int{
        //val db = readableDatabase;
        val pid=getStoryId(storyName)
        if (pid==-1) return -1
        val query=rdb.rawQuery("SELECT id FROM parts WHERE parent = $pid" +
                " AND name = \"$partName\" AND type = $partType", null)
        query.moveToFirst()
        if (query.count==0) return -1
        val id=query.getInt(0)
        query.close()
        //db.close()
        return id
    }
    public fun getStoryStatus(storyName: String): Int{
        //val db = readableDatabase;
        val id=getStoryId(storyName)
        //val db = readableDatabase;
        val query=rdb.rawQuery("SELECT status FROM $TABLE_PARTS WHERE parent = $id", null)
        query.moveToFirst()
        var statuses: MutableList<Int> = mutableListOf()
        for (i in 0..<query.count){
            statuses.add(query.getInt(0))
            query.moveToNext()
        }
        query.close()
        //db.close()
        if (statuses.all { it == 2 }) return 2
        if (statuses.contains(1)) return 1
        return 0
    }
    public fun getPartStatus(partName: String, storyName: String, partType: Int): Int{
        val id=getStoryId(storyName)
        //val db = readableDatabase;
        val query=rdb.rawQuery("SELECT status FROM $TABLE_PARTS WHERE parent = $id AND name = \"$partName\" AND type = $partType", null)
        query.moveToFirst()
        if (query.count==0) return 0
        val status=query.getInt(0)
        query.close()
        //db.close()
        return status
    }
    public fun closeDB(){
        rdb.close()
    }
}
