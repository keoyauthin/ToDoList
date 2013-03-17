package com.shenkar.todolistproject;
 
/**
 * This class responsible to create the database of the project
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DBAdapter 
{
  //Variables
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "title";
    public static final String KEY_DESCRIPT = "description";
    public static final String KEY_TIME = "time";
    public static final String KEY_DATE = "date";
    public static final String KEY_DONE = "done";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_ALRAM = "alram";
    
    private static final String TAG = "DBAdapter";
 
    private static final String DATABASE_NAME = "tasklist";
    private static final String DATABASE_TABLE = "items";
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_CREATE =
        "create table items (_id integer primary key autoincrement, " 
        		+ "title text not null," +"date text not null,"+"time text not null,"
        		+"description text not null,"+"done text not null,"
        		+"location text not null,"+"alram text not null);";
 
    private final Context context; 
 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
 
    //Constructor
    public DBAdapter(Context ctx) 
    {
    	//get context
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
 
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
 
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
 
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
 
    //---insert a task into the database---
    public long insertTask(String name , String id ,String time,String date, String descript,String loc) 
    {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_DESCRIPT, descript);
        initialValues.put(KEY_LOCATION, loc);
        initialValues.put(KEY_DONE, "0");
        initialValues.put(KEY_ALRAM, "0");
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
     
    
    // Deleting single Task
	public void deleteTask(ItemDetails item) {
	   
	    db.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
	            new String[] { String.valueOf(item.getId()) });
	    db.close();
}

    //---retrieves all the task---
    public Cursor getAllTask() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_NAME,KEY_DATE,KEY_TIME,KEY_DESCRIPT,KEY_DONE,KEY_LOCATION,KEY_ALRAM,
        		}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
 
 
 
    //---updates task---
    public boolean updateTask(long rowId, String title , String description ) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, title);
        args.put(KEY_DESCRIPT, description);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //--update Task Alarm ---
    public boolean updateTaskAlarm(long rowId, String alram) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_ALRAM, alram);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    //-- Update Task Location Task ---
    public boolean updateTaskLocation(long rowId, String loc) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_LOCATION, loc);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //-- update Task Done ---
    public boolean updateTaskDone(long rowId, String done) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_DONE, done);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
  //--  update Task Time ---
    public boolean updateTaskTime(long rowId, String time) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_TIME, time);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
  //--  update Task Date ---
    public boolean updateTaskDate(long rowId, String date) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_DATE, date);

        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
 
}