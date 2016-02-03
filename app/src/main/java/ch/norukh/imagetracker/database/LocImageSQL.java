package ch.norukh.imagetracker.database;

/**
 * @author Nico Fehr
 *         Created by Nico on 02.01.2016.
 */
public class LocImageSQL {

    public static final String TABLE_IMAGE = "image";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CITY = "city";

    public static String createTableImages() {
        return "CREATE TABLE " + TABLE_IMAGE + " ("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_FILE + " TEXT, "
                + COLUMN_LAT + " DOUBLE, "
                + COLUMN_LNG + " DOUBLE, "
                + COLUMN_TIME + " LONG, "
                + COLUMN_CITY + " TEXT);";
    }
}
