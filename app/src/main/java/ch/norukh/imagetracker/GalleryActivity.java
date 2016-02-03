package ch.norukh.imagetracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import ch.norukh.imagetracker.database.LocImageDAO;
import ch.norukh.imagetracker.fragments.ShowImageFragment;
import ch.norukh.imagetracker.model.LocImage;

public class GalleryActivity extends AppCompatActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LocImageDAO dao = new LocImageDAO(getApplicationContext());
        List<LocImage> imageList = dao.getAllImages();
        int currentItem = this.getIntent().getIntExtra("itemPos", 0);

        mPager = (ViewPager) findViewById(R.id.viewpagerImage);
        mPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager(), imageList.size(), imageList);
        mPager.setCurrentItem(currentItem);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class GalleryPagerAdapter extends FragmentStatePagerAdapter {
        private final int num_pages;
        private List<LocImage> locImages;
        public GalleryPagerAdapter(FragmentManager fm, int num_pages, List<LocImage> locImages) {
            super(fm);
            this.locImages = locImages;
            this.num_pages = num_pages;
        }

        @Override
        public Fragment getItem(int position) {
            return ShowImageFragment.newInstance(locImages.get(position).getId(), getApplicationContext());
        }

        @Override
        public int getCount() {
            return num_pages;
        }
    }

}
