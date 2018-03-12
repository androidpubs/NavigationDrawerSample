/*
 *
 *  * Copyright 2015 Van Shu
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.icit.android.apps.navigationdrawersample.util;

/**
 * Created by Joe on 2018-03-12.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.icit.android.apps.navigationdrawersample.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


/**
 * An assortment of UI helpers.
 */
public class UIUtils {
    /**
     * Factor applied to session color to derive the background color on panels and when
     * a session photo could not be downloaded (or while it is being downloaded)
     */
    public static final float SESSION_BG_COLOR_SCALE_FACTOR = 0.75f;
    public static final String TARGET_FORM_FACTOR_HANDSET = "handset";
    public static final String TARGET_FORM_FACTOR_TABLET = "tablet";
    public static final String GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus";
    public static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    public static final int ANIMATION_FADE_IN_TIME = 250;
    public static final String TRACK_ICONS_TAG = "tracks";
    private static final String TAG = UIUtils.class.toString();
    private static final float SESSION_PHOTO_SCRIM_ALPHA = 0.25f; // 0=invisible, 1=visible image
    private static final float SESSION_PHOTO_SCRIM_SATURATION = 0.2f; // 0=gray, 1=color image
    /**
     * Flags used with {@link android.text.format.DateUtils#formatDateRange}.
     */
    private static final int TIME_FLAGS = DateUtils.FORMAT_SHOW_TIME
            | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
    /**
     * Regex to search for HTML escape sequences.
     * <p/>
     * <p></p>Searches for any continuous string of characters starting with an ampersand and ending with a
     * semicolon. (Example: &amp;)
     */
    private static final Pattern REGEX_HTML_ESCAPE = Pattern.compile(".*&\\S;.*");
    private static final int BRIGHTNESS_THRESHOLD = 130;
    private static final long sAppLoadTime = System.currentTimeMillis();
    private static CharSequence sNowPlayingText;
    private static CharSequence sLivestreamNowText;
    private static CharSequence sLivestreamAvailableText;
    private static SimpleDateFormat sDayOfWeekFormat = new SimpleDateFormat("E");
    private static DateFormat sShortTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);


    /**
     * Calculate whether a color is light or dark, based on a commonly known
     * brightness formula.
     *
     * @see {@literal http://en.wikipedia.org/wiki/HSV_color_space%23Lightness}
     */
    public static boolean isColorDark(int color) {
        return ((30 * Color.red(color) +
                59 * Color.green(color) +
                11 * Color.blue(color)) / 100) <= BRIGHTNESS_THRESHOLD;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    // Whether a feedback notification was fired for a particular session. In the event that a
    // feedback notification has not been fired yet, return false and set the bit.
    public static boolean isFeedbackNotificationFiredForSession(Context context, String sessionId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String key = String.format("feedback_notification_fired_%s", sessionId);
        boolean fired = sp.getBoolean(key, false);
        sp.edit().putBoolean(key, true).commit();
        return fired;
    }

    // Clear the flag that says a notification was fired for the given session.
    // Typically used to debug notifications.
    public static void unmarkFeedbackNotificationFiredForSession(Context context, String sessionId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String key = String.format("feedback_notification_fired_%s", sessionId);
        sp.edit().putBoolean(key, false).commit();
    }

    // Shows whether a notification was fired for a particular session time block. In the
    // event that notification has not been fired yet, return false and set the bit.
    public static boolean isNotificationFiredForBlock(Context context, String blockId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String key = String.format("notification_fired_%s", blockId);
        boolean fired = sp.getBoolean(key, false);
        sp.edit().putBoolean(key, true).commit();
        return fired;
    }

    public static long getCurrentTime(final Context context) {
//        if (BuildConfig.DEBUG) {
//            return context.getSharedPreferences("mock_data", Context.MODE_PRIVATE)
//                    .getLong("mock_current_time", System.currentTimeMillis())
//                    + System.currentTimeMillis() - sAppLoadTime;
////            return ParserUtils.parseTime("2012-06-27T09:44:45.000-07:00")
////                    + System.currentTimeMillis() - sAppLoadTime;
//        } else {
            return System.currentTimeMillis();
//        }
    }





    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

//    public static void setUpButterBar(View butterBar, String messageText, String actionText,
//                                      View.OnClickListener listener) {
//        if (butterBar == null) {
//            LOGE(TAG, "Failed to set up butter bar: it's null.");
//            return;
//        }
//
//        TextView textView = (TextView) butterBar.findViewById(R.id.butter_bar_text);
//        if (textView != null) {
//            textView.setText(messageText);
//        }
//
//        Button button = (Button) butterBar.findViewById(R.id.butter_bar_button);
//        if (button != null) {
//            button.setText(actionText == null ? "" : actionText);
//            button.setVisibility(!TextUtils.isEmpty(actionText) ? View.VISIBLE : View.GONE);
//        }
//
//        button.setOnClickListener(listener);
//        butterBar.setVisibility(View.VISIBLE);
//    }

    public static float getProgress(int value, int min, int max) {
        if (min == max) {
            throw new IllegalArgumentException("Max (" + max + ") cannot equal min (" + min + ")");
        }

        return (value - min) / (float) (max - min);
    }

    // Desaturates and color-scrims the image
    public static ColorFilter makeSessionImageScrimColorFilter(int sessionColor) {
        float a = SESSION_PHOTO_SCRIM_ALPHA;
        float sat = SESSION_PHOTO_SCRIM_SATURATION; // saturation (0=gray, 1=color)
        return new ColorMatrixColorFilter(new float[]{
                ((1 - 0.213f) * sat + 0.213f) * a, ((0 - 0.715f) * sat + 0.715f) * a, ((0 - 0.072f) * sat + 0.072f) * a, 0, Color.red(sessionColor) * (1 - a),
                ((0 - 0.213f) * sat + 0.213f) * a, ((1 - 0.715f) * sat + 0.715f) * a, ((0 - 0.072f) * sat + 0.072f) * a, 0, Color.green(sessionColor) * (1 - a),
                ((0 - 0.213f) * sat + 0.213f) * a, ((0 - 0.715f) * sat + 0.715f) * a, ((1 - 0.072f) * sat + 0.072f) * a, 0, Color.blue(sessionColor) * (1 - a),
                0, 0, 0, 0, 255
        });
    }

    private static final int[] RES_IDS_ACTION_BAR_SIZE = { R.attr.actionBarSize };
    /** Calculates the Action Bar height in pixels. */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }

    public static int scaleColor(int color, float factor, boolean scaleAlpha) {
        return Color.argb(scaleAlpha ? (Math.round(Color.alpha(color) * factor)) : Color.alpha(color),
                Math.round(Color.red(color) * factor), Math.round(Color.green(color) * factor),
                Math.round(Color.blue(color) * factor));
    }
}
