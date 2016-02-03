package ch.norukh.imagetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.norukh.imagetracker.R;
import ch.norukh.imagetracker.model.LocImage;

/**
 * Created by Nico on 02.01.2016.
 */
public class ImageArrayAdapter extends ArrayAdapter<LocImage> {

    private LayoutInflater inflater;
    private List<LocImage> mItems;

    public ImageArrayAdapter(Context context, List<LocImage> items, LayoutInflater inflater) {
        super(context, -1, items);
        this.mItems = items;
        this.inflater = inflater;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.image_adapter, null);
        final LocImage locImage = mItems.get(pos);

        ((ImageView) convertView.findViewById(R.id.imageThumb)).setImageBitmap(locImage.getThumb());
        ((TextView) convertView.findViewById(R.id.dateField)).setText(locImage.getImage().getLastPathSegment());

        return convertView;
    }
}
