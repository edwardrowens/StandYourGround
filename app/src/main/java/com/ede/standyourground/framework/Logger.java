package com.ede.standyourground.framework;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.IllegalFormatConversionException;

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
        try {
            Log.i(tag, String.format(message, format));
        } catch (IllegalFormatConversionException e) {
            Log.i(Logger.class.getName(), e.getMessage());
        }
    }

    public void w(String message, Object... format) {
        String tag = createTag();
        try {
            Log.w(tag, String.format(message, format));
        } catch (IllegalFormatConversionException e) {
            Log.w(Logger.class.getName(), e.getMessage());
        }
    }

    public void e(String message, Object... format) {
        String tag = createTag();
        try {
            Log.e(tag, String.format(message, format));
        } catch (IllegalFormatConversionException e) {
            Log.e(Logger.class.getName(), e.getMessage());
        }
    }

    public void d(String message, Object... format) {
        String tag = createTag();
        try {
            Log.d(tag, String.format(message, format));
        } catch (IllegalFormatConversionException e) {
            Log.d(Logger.class.getName(), e.getMessage());
        }
    }

    private String createTag() {
        return clazz.getName() + " [" + Thread.currentThread().getName() + "] " + dateFormat.format(System.currentTimeMillis());
    }
}
