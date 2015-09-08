package com.miiskin.videolibraryproject.content.webapi;

import com.miiskin.videolibraryproject.content.data.VideoInfoList;
import com.miiskin.videolibraryproject.content.webapi.client.VideoErrorHandler;

import retrofit.http.GET;

/**
 * Created on 01.07.2015.
 */
public interface VideoApi {

    @GET("/numbata/5ed307d7953c3f7e716f/raw/b7887adc444188d8aa8e61d39b82950f28c03966/movies.json")
    VideoInfoList getVideoInfoList()
            throws VideoErrorHandler.RequestException;

}
