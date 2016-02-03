package ch.norukh.imagetracker.gps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 24.01.2016.
 */
public class CityFinder {

    private Geocoder gcd;
    private Context context;

    public CityFinder(Context context) {
        this.context = context;
        gcd = new Geocoder(context, Locale.getDefault());
    }

    public String findCity(double lat, double lng) {
        if(isNetworkConnected()) {
            try {
                List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                if (addresses.size() > 0)
                    return addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        } else {
            return "";
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
