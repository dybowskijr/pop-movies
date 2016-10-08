package us.dybowski.popularmovies;

import android.app.Activity;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    Activity mActivity;
    private final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    public ReviewAdapter(Activity activity, List<Review> reviews) {
        super(activity, 0, reviews);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View itemViewGroup, ViewGroup parent) {
        if(itemViewGroup == null) {
            itemViewGroup = LayoutInflater.from(mActivity).inflate(R.layout.list_item_review, parent, false);
        }
        Review r = getItem(position);
        ((TextView)itemViewGroup.findViewById(R.id.review_text_view)).setText(r.getReviewText());
        ((TextView)itemViewGroup.findViewById(R.id.reviewer_text_view)).setText(r.getReviewer());
        Log.i(LOG_TAG, r.getReviewText());

        return itemViewGroup;
    }
}
