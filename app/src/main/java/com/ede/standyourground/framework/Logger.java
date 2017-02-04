package com.ede.standyourground.framework;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by Eddie on 2/4/2017.
 */

public class Logger {
    private Class clazz;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public Logger(Class clazz) {
        this.clazz = clazz;
    }

    public void i(String message, Object... format) {
        String tag = createTag();
        Log.i(tag, String.format(message, format));
    }

    public void w(String message, Object... format) {
        String tag = createTag();
        Log.w(tag, String.format(message, format));
    }

    public void e(String message, Object... format) {
        String tag = createTag();
        Log.e(tag, String.format(message, format));
    }

    public void d(String message, Object... format) {
        String tag = createTag();
        Log.d(tag, String.format(message, format));
    }

    private String createTag() {
        return clazz.getName() + " [" + Thread.currentThread().getName() + "] " + dateFormat.format(System.currentTimeMillis());
    }
}
