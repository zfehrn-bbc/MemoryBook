package ch.norukh.imagetracker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Nico Fehr
 * Created by Nico on 03.01.2016.
 */
public class QuadratImageView extends ImageView {

    public QuadratImageView(Context context) {
        super(context);
    }

    public QuadratImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuadratImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
