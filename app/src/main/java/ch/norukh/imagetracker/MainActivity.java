package ch.norukh.imagetracker;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.norukh.imagetracker.camera.Camera;
import ch.norukh.imagetracker.database.LocImageDAO;
import ch.norukh.imagetracker.fragments.ImagesFragment;
import ch.norukh.imagetracker.gps.CityFinder;
import ch.norukh.imagetracker.gps.GPSTracker;
import ch.norukh.imagetracker.model.LocImage;

/**
 * @author Nico Fehr
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Camera camera;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Tab stuff
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //§

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera = new Camera();
                camera.createCamera((Activity) view.getContext());
                //getSupportFragmentManager().beginTransaction().add(new CameraFragment(), "Camera").commit();
            }
        });

        // Location startup snackbar
        gpsTracker = new GPSTracker(getApplicationContext());
        if(!gpsTracker.canGetLocation()) {
            showSettingsAlert();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == Camera.PICK_PICTURE) {

            } else if (requestCode == Camera.TAKE_PICTURE) {

                Timestamp timestamp = camera.getTimestamp();
                Uri image = camera.getUriSavedImage();
                Bitmap thumb = ThumbnailUtils.extractThumbnail(Camera.decodeFile(image.getPath()), ShowRoomActivity.THUMBSIZE, ShowRoomActivity.THUMBSIZE);

                double lat = gpsTracker.getLatitude();
                double lng = gpsTracker.getLongitude();
                LatLng latLngLoc = new LatLng(lat, lng);

                try {
                    ExifInterface exifInterface = new ExifInterface(image.getPath());
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(lat));
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(lng));
                    exifInterface.saveAttributes();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Find current City
                CityFinder cityFinder = new CityFinder(getApplicationContext());
                String currCity = cityFinder.findCity(lat, lng);

                final LocImage locImage = new LocImage(image, thumb, latLngLoc, timestamp, currCity);
                LocImageDAO locImageDAO = new LocImageDAO(this.getApplicationContext());
                locImageDAO.createLocImage(locImage);

                Intent intent = new Intent(this.getApplicationContext(), ShowRoomActivity.class);
                intent.putExtra("image", camera.getUriSavedImage());
                startActivity(intent);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    public void showSettingsAlert(){
        Snackbar snackbar = Snackbar
                .make(viewPager, R.string.gpsString, Snackbar.LENGTH_LONG)
                .setAction(R.string.enableString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                });

        snackbar.show();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add fragments here
        adapter.addFragment(new ImagesFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.mapAction) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
