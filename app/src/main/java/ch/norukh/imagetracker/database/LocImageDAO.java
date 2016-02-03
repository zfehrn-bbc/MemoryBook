package ch.norukh.imagetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.norukh.imagetracker.ShowRoomActivity;
import ch.norukh.imagetracker.camera.Camera;
import ch.norukh.imagetracker.model.LocImage;

/**
 * Created by Nico on 02.01.2016.
 */
public class LocImageDAO extends DatabaseDAO {

    public LocImageDAO(Context context) {
        super(context);
    }

    private String[] allColumns = {
            LocImageSQL.COLUMN_ID,
            LocImageSQL.COLUMN_FILE,
            LocImageSQL.COLUMN_LAT,
            LocImageSQL.COLUMN_LNG,
            LocImageSQL.COLUMN_TIME,
            LocImageSQL.COLUMN_CITY
    };

    public LocImage createLocImage(LocImage locImage) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(LocImageSQL.COLUMN_FILE, locImage.getImage().getPath());
        values.put(LocImageSQL.COLUMN_LAT, locImage.getLocation().latitude);
        values.put(LocImageSQL.COLUMN_LNG, locImage.getLocation().longitude);
        values.put(LocImageSQL.COLUMN_TIME, locImage.getTimestamp().getTime());
        values.put(LocImageSQL.COLUMN_CITY, locImage.getCity());

        long insertId = db.insert(LocImageSQL.TABLE_IMAGE, null, values);
        Cursor cursor = db.query(LocImageSQL.TABLE_IMAGE, allColumns, LocImageSQL.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        LocImage imageLoc = this.cursorToLocImage(cursor);
        cursor.close();
        close();
        return imageLoc;
    }

    public void removeLocImage(LocImage locImage) {
        try{
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long id = locImage.getId();
        db.delete(LocImageSQL.TABLE_IMAGE, LocImageSQL.COLUMN_ID + " = " + id, null);
        close();
    }

    public LocImage findImageById(long id) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.query(LocImageSQL.TABLE_IMAGE, allColumns, LocImageSQL.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        LocImage imageLoc = this.cursorToLocImage(cursor);
        cursor.close();
        close();
        return imageLoc;
    }

    public List<LocImage> getAllImages() {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<LocImage> images = new ArrayList<LocImage>();
        Cursor cursor = db.query(LocImageSQL.TABLE_IMAGE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocImage locImage = cursorToLocImage(cursor);
            images.add(locImage);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return images;
    }

    private LocImage cursorToLocImage(Cursor cursor) {
        LocImage locImage = new LocImage();
        locImage.setId(cursor.getLong(0));
        locImage.setImage(Uri.fromFile(new File(cursor.getString(1))));
        locImage.setLocation(new LatLng(cursor.getDouble(2), cursor.getDouble(3)));
        locImage.setThumb(ThumbnailUtils.extractThumbnail(Camera.decodeFile(cursor.getString(1)), ShowRoomActivity.THUMBSIZE, ShowRoomActivity.THUMBSIZE));
        locImage.setTimestamp(new Timestamp(cursor.getLong(4)));
        locImage.setCity(cursor.getString(5));
        return locImage;
    }
}
