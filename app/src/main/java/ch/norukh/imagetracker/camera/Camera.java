package ch.norukh.imagetracker.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Nico on 02.01.2016.
 */
public class Camera {

    public static final int PICK_PICTURE = 1;
    public static final int TAKE_PICTURE = 2;
    private Uri uriSavedImage;
    private Timestamp timestamp;

    public void createCamera(Activity activity) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(this.timestamp);

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, "QR_" + timeStamp + ".jpg");
        uriSavedImage = Uri.fromFile(image);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uriSavedImage);
        activity.sendBroadcast(mediaScanIntent);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        activity.startActivityForResult(cameraIntent, TAKE_PICTURE);
    }

    public static Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getUriSavedImage() {
        return uriSavedImage;
    }

    public void setUriSavedImage(Uri uriSavedImage) {
        this.uriSavedImage = uriSavedImage;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
