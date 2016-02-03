package ch.norukh.imagetracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Nico on 02.01.2016.
 */
public class DatabaseDAO {

    protected DatabaseManager dbHelper;
    protected SQLiteDatabase db;

    public DatabaseDAO(Context context) {
        dbHelper = new DatabaseManager(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
