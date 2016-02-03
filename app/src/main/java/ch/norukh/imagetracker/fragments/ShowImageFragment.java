package ch.norukh.imagetracker.fragments;

/**
 * Created by Nico on 10.01.2016.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ch.norukh.imagetracker.R;
import ch.norukh.imagetracker.database.LocImageDAO;
import ch.norukh.imagetracker.model.LocImage;

public class ShowImageFragment extends Fragment {

    private LocImage locImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView rootView = (ImageView) inflater.inflate(
                R.layout.image_adapter_view, container, false);
        locImage = (LocImage) getArguments().getSerializable("image");
        Log.e("ShowImageFragment", locImage.toString());
        rootView.setImageURI(locImage.getImage());
        return rootView;
    }

    public static ShowImageFragment newInstance(long id, Context context) {
        ShowImageFragment showImageFragment = new ShowImageFragment();

        Log.e("ShowImageFragmentDAO", ""+id);
        LocImageDAO dao = new LocImageDAO(context);

        Bundle args = new Bundle();
        args.putSerializable("image", dao.findImageById(id));
        showImageFragment.setArguments(args);
        return showImageFragment;
    }
}