package ch.norukh.imagetracker.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @autor Nico Fehr
 * Created by Nico on 02.01.2016.
 */
public class LocImage implements Serializable {

    private long id;
    private Uri image;
    private Bitmap thumb;
    private LatLng location;
    private Timestamp timestamp;
    private String city;

    public LocImage() {

    }

    public LocImage(Uri image, Bitmap thumb, LatLng location, Timestamp timestamp, String city) {
        this.image = image;
        this.thumb = thumb;
        this.location = location;
        this.timestamp = timestamp;
        this.city = city;
    }

    @Override
    public String toString() {
        return "LocImage{" +
                "id=" + id +
                ", image=" + image +
                ", thumb=" + thumb +
                ", location=" + location +
                ", timestamp=" + timestamp +
                ", city='" + city + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
