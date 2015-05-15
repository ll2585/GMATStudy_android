package com.lukeli.gmatstudy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SQLiteUtils {
    //USES ANDROID SQLITE because this is an android app duh.


    static String DB_PATH = "/sdcard/gmatstudy/";

    public static SQLiteDatabase connect(String DB_NAME){
        File db_file = new File(DB_PATH + DB_NAME);
        return SQLiteDatabase.openOrCreateDatabase(db_file, null);
    }

    public static ArrayList<ArrayList<Object>> fetch_all(Cursor c){
        ArrayList<ArrayList<Object>> list = new ArrayList<>();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ArrayList<Object> obj = new ArrayList<>();
                for(int i = 0; i < c.getColumnCount(); i++){
                    //only one column
                    int type = c.getType(i);
                    Object val;
                    switch (type){
                        case Cursor.FIELD_TYPE_INTEGER:
                            val = c.getInt(i);
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            val = c.getString(i);
                            break;
                        default:
                            val = null;
                    }

                    obj.add(val);
                }

                list.add(obj);
            } while (c.moveToNext());
        }
        return list;
    }

}
