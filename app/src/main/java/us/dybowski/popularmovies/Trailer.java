package us.dybowski.popularmovies;

/**
 * Created by DD8312 on 10/6/2016.
 */

public class Trailer {
    private String mKey;
    private String mSite;
    private String mSize;
    private String mName;

    public Trailer(String mKey, String mName, String mSite) {
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
    }

    public String getKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }
}
