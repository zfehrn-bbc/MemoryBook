package ch.norukh.imagetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ch.norukh.imagetracker.R;
import ch.norukh.imagetracker.model.LocImage;

/**
 * Created by Nico on 24.01.2016.
 */
public class ImageArrayAdapterList extends ArrayAdapter<LocImage> {

    private LayoutInflater inflater;
    private List<LocImage> mItems;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    public ImageArrayAdapterList(Context context, List<LocImage> items, LayoutInflater inflater) {
        super(context, -1, items);
        this.mItems = items;
        this.inflater = inflater;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.image_adapter_list, null);
        final LocImage locImage = mItems.get(pos);

        LatLng ll = locImage.getLocation();

        ((ImageView) convertView.findViewById(R.id.imageViewList)).setImageBitmap(locImage.getThumb());
        ((TextView) convertView.findViewById(R.id.textViewOrtList)).setText(locImage.getCity());
        ((TextView) convertView.findViewById(R.id.textViewDatumList)).setText(sdf.format(locImage.getTimestamp()));

        return convertView;
    }
}
