package us.dybowski.popularmovies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    Activity mActivity;
    private final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Activity activity, List<Trailer> trailers) {
        super(activity, 0, trailers);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View itemViewGroup, ViewGroup parent) {
        if(itemViewGroup == null) {
            itemViewGroup = LayoutInflater.from(mActivity).inflate(R.layout.list_item_trailer, parent, false);
        }
        ((TextView)itemViewGroup.findViewById(R.id.trailer_text_view)).setText("Trailer " + Integer.toString(position + 1));

        return itemViewGroup;
    }
}
