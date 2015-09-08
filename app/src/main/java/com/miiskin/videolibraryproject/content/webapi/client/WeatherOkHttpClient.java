package com.miiskin.videolibraryproject.content.webapi.client;

import android.content.Context;
import android.support.annotation.NonNull;

import com.miiskin.videolibraryproject.utils.StreamUtils;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by ustimov on 01.08.2015.
 */
public class WeatherOkHttpClient {

    private static final int CONNECTION_TIMEOUT = 40;
    private static final int WRITE_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 20;

    @NonNull
    public static Client create(final Context context) {
        try {
            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            return new WeatherClient(okHttpClient);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to create OkHttp client", e);
        }
    }

    private WeatherOkHttpClient() {
    }

    private static class WeatherClient implements Client {

        private final Client mClient;

        public WeatherClient(final OkHttpClient okHttpClient) {
            mClient = new OkClient(okHttpClient);
        }

        @Override
        public Response execute(final Request request) throws IOException {
            final Response response = mClient.execute(request);

            final TypedInput inputBody = response.getBody();
            final String responseBody = (inputBody == null ? "" : StreamUtils.readFully(inputBody));
            final String mimeType = (inputBody == null ? null : inputBody.mimeType());
            final byte[] data = (responseBody == null ? new byte[0] : responseBody.getBytes());

            return new Response(response.getUrl(), response.getStatus(), response.getReason(),
                    response.getHeaders(), new TypedByteArray(mimeType, data));
        }

    }

}
