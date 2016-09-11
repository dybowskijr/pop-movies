package us.dybowski.popularmovies;

/**
 * Created by DD8312 on 8/28/2016.
 */
public class Movie {
    private String id;
    private String posterPath;

    Movie(String id, String posterPath) {
        setId(id);
        setPosterPath(posterPath);

    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
