package com.miiskin.videolibraryproject.content.webapi.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.miiskin.videolibraryproject.content.data.Genre;
import com.miiskin.videolibraryproject.content.data.VideoInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by ustimov on 01.08.2015.
 */
public class VideoJsonConverter {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @NonNull
    public static Converter create() {
        final GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(DATE_FORMAT);
        builder.registerTypeAdapter(VideoInfo.class, new VideoInfoDeserializer());

        return new GsonConverter(builder.create());
    }

    private VideoJsonConverter() {
    }

    private static class VideoInfoDeserializer implements JsonDeserializer {

        @Override
        public VideoInfo deserialize(final JsonElement json, final Type typeOfT,
                                                 final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            final VideoInfo videoInfo = new VideoInfo();
            videoInfo.setId(jsonObject.get("id").getAsInt());
            videoInfo.setAdult(jsonObject.get("adult").getAsBoolean());

            Genre[] genres = null;
            if (jsonObject.has("genres")) {
                genres = context.deserialize(jsonObject.get("genres"), Genre[].class);
            } else if (jsonObject.has("genre_ids")) {
                genres = context.deserialize(jsonObject.get("genre_ids"), Genre[].class);
            }
            videoInfo.setGenres(genres);
            videoInfo.setOriginalLanguage(jsonObject.get("original_language").getAsString());
            videoInfo.setOriginalTitle(jsonObject.get("original_title").getAsString());
            videoInfo.setOverview(jsonObject.get("overview").getAsString());
            videoInfo.setReleaseDate(jsonObject.get("release_date").getAsString());
            videoInfo.setPosterPath(jsonObject.get("poster_path").getAsString());
            videoInfo.setPopularity(jsonObject.get("popularity").getAsDouble());
            videoInfo.setTitle(jsonObject.get("title").getAsString());
            videoInfo.setVideo(jsonObject.get("video").getAsString());
            videoInfo.setVoteAverage(jsonObject.get("vote_average").getAsDouble());
            videoInfo.setVoteCount(jsonObject.get("vote_average").getAsLong());

            return videoInfo;

        }

        @NonNull
        private JsonArray getJsonArray(final JsonArray jsonArray, final int index) {
            final JsonElement element = getJsonElement(jsonArray, index);
            return (element == null ? new JsonArray() : element.getAsJsonArray());
        }

        @NonNull
        private JsonObject getJsonObject(final JsonArray jsonArray, final int index) {
            final JsonElement element = getJsonElement(jsonArray, index);
            return (element == null ? new JsonObject() : element.getAsJsonObject());
        }

        @NonNull
        private String getString(final JsonArray jsonArray, final int index) {
            final JsonElement element = getJsonElement(jsonArray, index);
            return (element == null ? "" : element.getAsString());
        }

        @Nullable
        private JsonElement getJsonElement(final JsonArray jsonArray, final int index) {
            if (jsonArray.size() > index) {
                final JsonElement element = jsonArray.get(index);
                if (element != null && !element.isJsonNull()) {
                    return element;
                }
            }
            return null;
        }
    }


}
