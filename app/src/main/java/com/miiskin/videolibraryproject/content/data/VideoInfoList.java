package com.miiskin.videolibraryproject.content.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoInfoList implements Parcelable {

    @SerializedName("page")
    public int mPage;

    @SerializedName("results")
    public VideoInfo[] mVideoInfo;

    @SerializedName("total_pages")
    public int mTotalPages;

    @SerializedName("total_results")
    public int mTotalResults;

    public VideoInfoList() {
    }

    /* package */ VideoInfoList(final Parcel src) {
        mPage = src.readInt();
        mVideoInfo = (VideoInfo[])src.readParcelableArray(VideoInfo[].class.getClassLoader());
        mTotalPages = src.readInt();
        mTotalResults = src.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPage);
        dest.writeParcelableArray(mVideoInfo, flags);
        dest.writeInt(mTotalPages);
        dest.writeInt(mTotalResults);
    }

    public static final Parcelable.Creator<VideoInfoList> CREATOR = new Parcelable.Creator<VideoInfoList>() {

        @Override
        public VideoInfoList createFromParcel(final Parcel in) {
            return new VideoInfoList(in);
        }

        @Override
        public VideoInfoList[] newArray(final int size) {
            return new VideoInfoList[size];
        }

    };
}
