package com.miiskin.videolibraryproject.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.miiskin.videolibraryproject.content.dao.Identify;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoInfo implements Identify, Parcelable, Cloneable{

    private int mId = INVALID_ID;

    @SerializedName("adult")
    private Boolean mAdult;
    @SerializedName("genre_ids")
    private Genre[] mGenres;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("video")
    private String mVideo;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @SerializedName("vote_count")
    private Long mVoteCount;

    public VideoInfo() {
    }

    /* package */ VideoInfo(final Parcel src) {
        mId = src.readInt();
        mAdult = (Boolean)src.readValue(Boolean.class.getClassLoader());
        mGenres = (Genre[])src.readParcelableArray(Genre[].class.getClassLoader());
        mOriginalLanguage = src.readString();
        mOriginalTitle = src.readString();
        mOverview = src.readString();
        mReleaseDate = src.readString();
        mPosterPath = src.readString();
        mPopularity = src.readDouble();
        mTitle = src.readString();
        mVideo = src.readString();
        mVoteAverage = src.readDouble();
        mVoteCount = src.readLong();
    }

    public void setId(int id) {
        mId = id;
    }

    public Boolean getAdult() {
        return mAdult;
    }

    public void setAdult(Boolean adult) {
        mAdult = adult;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double popularity) {
        mPopularity = popularity;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVideo() {
        return mVideo;
    }

    public void setVideo(String video) {
        mVideo = video;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public Long getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(Long voteCount) {
        mVoteCount = voteCount;
    }

    public Genre[] getGenres() {
        return mGenres;
    }

    public void setGenres(Genre[] genres) {
        mGenres = genres;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeValue(mAdult);
        dest.writeParcelableArray(mGenres, flags);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterPath);
        dest.writeDouble(mPopularity);
        dest.writeString(mTitle);
        dest.writeString(mVideo);
        dest.writeDouble(mVoteAverage);
        dest.writeLong(mVoteCount);
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {

        @Override
        public VideoInfo createFromParcel(final Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(final int size) {
            return new VideoInfo[size];
        }

    };

}
