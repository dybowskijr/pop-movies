package us.dybowski.popularmovies;



public class Review {
    private String mReviewText;
    private String mReviewer;

    public Review(String mReviewer, String mReviewText) {
        this.mReviewer = mReviewer;
        this.mReviewText = mReviewText;
    }

    public String getReviewer() {
        return mReviewer;
    }

    public void setReviewer(String mReviewer) {
        this.mReviewer = mReviewer;
    }

    public String getReviewText() {
        return mReviewText;
    }

    public void setReviewText(String mReviewText) {
        this.mReviewText = mReviewText;
    }
}
