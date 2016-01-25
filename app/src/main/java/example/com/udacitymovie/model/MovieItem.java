package example.com.udacitymovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vppl-10132 on 1/5/2016.
 */

public class MovieItem implements Parcelable {

    private String page = "page";
    private String total_results = "total_results";
    private String total_pages = "total_pages";
    private String poster_path = "poster_path";
    private String adult = "adult";
    private String overview = "overview";
    private String release_date = "release_date";
    private String genre_ids = "genre_ids";
    private String id = "id";
    private String original_title = "original_title";
    private String original_language = "original_language";
    private String title = "title";
    private String backdrop_path = "backdrop_path";
    private String popularity = "popularity";
    private String vote_count = "vote_count";
    private String video = "video";
    private String vote_average = "vote_average";

    public MovieItem() {

    }
    public MovieItem(String id,
                     String poster_path,
                     String title,
                     String original_title,
                     String overview,
                     String vote_average,
                     String release_date) {

        this.id = id;
        this.poster_path= poster_path;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }



    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public MovieItem(Parcel in) {
        page = in.readString();
        total_results = in.readString();
        total_pages = in.readString();
        poster_path = in.readString();
        adult = in.readString();
        overview = in.readString();
        release_date = in.readString();
        genre_ids = in.readString();
        id = in.readString();
        original_title = in.readString();
        original_language = in.readString();
        backdrop_path = in.readString();
        title = in.readString();
        popularity = in.readString();
        vote_count = in.readString();
        video = in.readString();
        vote_average = in.readString();

    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(page);
        dest.writeString(total_results);
        dest.writeString(total_pages);
        dest.writeString(poster_path);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(genre_ids);
        dest.writeString(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(backdrop_path);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(vote_count);
        dest.writeString(video);
        dest.writeString(vote_average);
    }
}
