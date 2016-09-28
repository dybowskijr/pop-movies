package us.dybowski.popularmovies;

/**
 * Created by DD8312 on 8/28/2016.
 */
public class Movie {
    private String id;
    private String posterPath;
    private String releaseDate;
    private String runtime;
    private String title;
    private String voteAverage;
    private String overview;

    Movie(String id, String posterPath) {
        setId(id);
        setPosterPath(posterPath);

    }

    public Movie(String id, String voteAverage, String overview, String posterPath,
                 String releaseDate, String runtime, String title) {
        this.voteAverage = voteAverage;
        this.id = id;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.title = title;
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

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }
}
