package com.miiskin.videolibraryproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;

import java.util.Locale;

/**
 * Created by ustimov on 01.08.2015.
 */
public class ApplicationUtils {

    public static boolean hasPermission(final Context context, final String permission) {
        return (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static void goToUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void lockOrientation(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private ApplicationUtils() {
    }

    public static void setLocale(Resources res, String languageCode) {
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);
        Configuration conf = res.getConfiguration();
        conf.locale = newLocale;
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

}