package com.miiskin.videolibraryproject.content.webapi.client;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created on 03.07.2015.
 */
public class VideoErrorHandler implements ErrorHandler {

    public static final int ERROR_CODE_OK = 200;
    public static final int ERROR_CODE_NETWORK = -1;
    public static final int ERROR_CODE_CONVERSION = -2;
    public static final int ERROR_CODE_UNEXPECTED = -3;

    public VideoErrorHandler() {
    }

    @Override
    public Throwable handleError(final RetrofitError cause) {
        final Response r = cause.getResponse();
        switch (cause.getKind()) {
            case CONVERSION:
                return new RequestException("Response parse error",
                        cause.getMessage(), ERROR_CODE_CONVERSION);

            case NETWORK:
                return new RequestException("Network error",
                        cause.getMessage(), ERROR_CODE_NETWORK);

            case UNEXPECTED:
                return new RequestException("Unexpected response error",
                        cause.getMessage(), ERROR_CODE_UNEXPECTED);

            case HTTP:
                if (r != null) {
                    final int status = r.getStatus();
                    switch (status) {
                        case 404:
                            return new RequestException("Not found",
                                    "Url " + r.getUrl() + " not found", status);

                        case 500:
                        case 501:
                        case 502:
                        case 503:
                            return new RequestException("Internal server error", status);

                        default:
                            return new RequestException(cause.getMessage(), status);
                    }
                }
                break;
        }

        return new RequestException(cause.getMessage(), ERROR_CODE_UNEXPECTED);
    }

    public static class RequestException extends Exception {

        private final String mTitle;
        private final int mCode;

        public RequestException(final String message, final int code) {
            this("Error", message, code);
        }

        public RequestException(final String title, final String message, final int code) {
            super(message);
            mTitle = title;
            mCode = code;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getCode() {
            return mCode;
        }

    }

}
