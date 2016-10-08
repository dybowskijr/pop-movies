package us.dybowski.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private long mMovieId = 0;

    public TextView mTitleTextView;
    public ImageView mPosterImageView;
    public TextView mReleaseYearTextView;
    public TextView mRuntimeTextView;
    public TextView mOverview;
    public CheckBox mFavoriteCheckBox;

    public ReviewAdapter mReviewAdapter;
    public ListView mReviewListView;

    public TrailerAdapter mTrailerAdapter;
    public ListView mTrailerListView;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getLong("MOVIEID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_detail, container, false);
        FetchMovieTask fetchMovieTask = new FetchMovieTask(this);
        fetchMovieTask.execute(mMovieId);

        mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        mReviewListView = (ListView)rootView.findViewById(R.id.review_listview);
        mReviewListView.setAdapter(mReviewAdapter);
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(this);
        fetchReviewsTask.execute(mMovieId);

        mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        mTrailerListView = (ListView)rootView.findViewById(R.id.trailer_listview);
        mTrailerListView.setAdapter(mTrailerAdapter);
        FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(this);
        fetchTrailersTask.execute(mMovieId);


        mRuntimeTextView = (TextView)rootView.findViewById(R.id.runtime_textview);
        mPosterImageView = (ImageView)rootView.findViewById(R.id.poster_imageview);
        mTitleTextView = (TextView)rootView.findViewById(R.id.title_textview);
        mReleaseYearTextView = (TextView)rootView.findViewById(R.id.releaseyear_textview);
        mOverview = (TextView)rootView.findViewById(R.id.overview_textview);
        mFavoriteCheckBox = (CheckBox)rootView.findViewById(R.id.checkbox_favorite);
        mFavoriteCheckBox.setVisibility(View.VISIBLE);

        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (true) {
                    Movie m = (Movie)adapterView.getItemAtPosition(position);
                    Log.i("POSITION", Integer.toString(position));
                    Log.i("MOVIEID", Long.toString(m.getMovieId()));
                    ((MainActivityFragment.Callback) getActivity())
                            .onItemSelected(m.getMovieId());
                }

            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
