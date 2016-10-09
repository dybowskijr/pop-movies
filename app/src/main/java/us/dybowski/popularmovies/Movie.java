package us.dybowski.popularmovies;

/**
 * Created by DD8312 on 8/28/2016.
 */
public class Movie {
    private String movieId;
    private String posterPath;
    private String releaseDate;
    private String runtime;
    private String title;
    private String voteAverage;
    private String overview;
    private long _id;

    Movie(String movieId, String posterPath) {
        setMovieId(movieId);
        setPosterPath(posterPath);

    }

    public Movie(String movieId, String voteAverage, String overview, String posterPath,
                 String releaseDate, String runtime, String title) {
        this.voteAverage = voteAverage;
        setMovieId(movieId);
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

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {

            this.movieId = movieId;
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
