package ch.norukh.imagetracker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.norukh.imagetracker.GalleryActivity;
import ch.norukh.imagetracker.R;
import ch.norukh.imagetracker.adapter.ImageArrayAdapter;
import ch.norukh.imagetracker.adapter.ImageArrayAdapterList;
import ch.norukh.imagetracker.database.LocImageDAO;
import ch.norukh.imagetracker.model.LocImage;

/**
 * Created by Nico on 02.01.2016.
 */
public class ImagesFragment extends Fragment {

    private LayoutInflater inflater;
    private ListView gridView;
    private Logger LOG = Logger.getLogger(ImagesFragment.class.getName());

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.images_fragment, container, false);
        this.inflater = inflater;
        gridView = (ListView) view.findViewById(R.id.imagesGrid);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final LocImageDAO imageDAO = new LocImageDAO(getContext());

        // Remove invalid images
        for(LocImage image : imageDAO.getAllImages()) {
            File imageFile = new File(image.getImage().getPath());
            if(!imageFile.exists()) {
                LOG.severe(image.toString());
                imageDAO.removeLocImage(image);
            }
        }

        if(imageDAO.getAllImages().size()==0) {
            List<String> noImages = new ArrayList<>();
            noImages.add(getString(R.string.noImagesString));
            ArrayAdapter<String> noImagesAdapter = new ArrayAdapter<String>(getContext(), R.layout.standard_list_item, noImages);
            gridView.setAdapter(noImagesAdapter);

        } else {
            final ArrayAdapter<LocImage> imagesAdapter = new ImageArrayAdapterList(getContext(), imageDAO.getAllImages(), inflater);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), GalleryActivity.class);
                    intent.putExtra("itemId", ((LocImage) parent.getItemAtPosition(position)).getId());
                    intent.putExtra("itemPos", position);
                    startActivity(intent);
                }
            });
            //
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    String[] actions_normal = new String[]{"Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //Set Title with image name
                    final LocImage locImage = (LocImage) parent.getItemAtPosition(position);
                    builder.setTitle(locImage.getImage().getLastPathSegment());
                    //
                    builder.setItems(actions_normal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    imageDAO.removeLocImage(locImage);
                                    File file = new File(locImage.getImage().getPath());
                                    if (file.delete()) {
                                        onResume();
                                    }
                                    break;
                            }
                        }
                    });
                    builder.show();
                    return false;
                }
            });
            gridView.setAdapter(imagesAdapter);
        }
    }
}
