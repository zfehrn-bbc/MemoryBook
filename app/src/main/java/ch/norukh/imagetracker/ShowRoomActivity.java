package ch.norukh.imagetracker;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import ch.norukh.imagetracker.camera.Camera;
import ch.norukh.imagetracker.database.LocImageDAO;
import ch.norukh.imagetracker.gps.GPSTracker;
import ch.norukh.imagetracker.model.LocImage;

public class ShowRoomActivity extends AppCompatActivity {

    public static final int THUMBSIZE = 150;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showroom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        supportPostponeEnterTransition();
        setSupportActionBar(toolbar);

        Uri image = getIntent().getParcelableExtra("image");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(image.getLastPathSegment());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        try {
            StringBuilder sb = new StringBuilder();
            ExifInterface exifInterface = new ExifInterface(image.getPath());

            sb.append("Latitude: ");
            sb.append(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            sb.append("\n");
            sb.append("Longitude: ");
            sb.append(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

            TextView textView = (TextView) findViewById(R.id.showroom_text);
            textView.setText(sb.toString());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load(image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabShow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fabShow), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorAccent));
        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
}
