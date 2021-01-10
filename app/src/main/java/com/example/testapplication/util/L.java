package com.example.testapplication.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * 로그 관련 유틸
 */
public class L {
    public static final Boolean DEBUG = true; // false면 모든 로그 안 나옴

    public static void i(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            Log.e(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            Log.d(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            Log.v(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            Log.w(tag, message);
        }
    }

    public static void print(String message) {
        if (!DEBUG) return;
        final int MAX_LOG_LENGTH = 200;
        // Split by line, then ensure each line can fit into Log's maximum length.
        int n = 0;
        if (message == null) {
            Log.d("[PRINT:"+n+"]", "message is null");
            return;
        }
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                Log.d("[PRINT:"+n+"]", message.substring(i, end));
                n++;
                i = end;
            } while (i < newline);
        }
    }
}
