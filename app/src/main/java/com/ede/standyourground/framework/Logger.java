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
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.i(Logger.class.getName(), e.getMessage());
        }

        if (message.length() > 4000) {
            Log.i(tag, formattedMessage);
            i(formattedMessage.substring(4000));
        } else {
            Log.i(tag, formattedMessage);
        }
    }

    public void w(String message, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.w(Logger.class.getName(), e.getMessage());
        }

        if (message.length() > 4000) {
            Log.w(tag, formattedMessage);
            w(formattedMessage.substring(4000));
        } else {
            Log.w(tag, formattedMessage);
        }
    }

    public void e(String message, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.e(Logger.class.getName(), e.getMessage());
        }

        if (message.length() > 4000) {
            Log.e(tag, formattedMessage);
            e(formattedMessage.substring(4000));
        } else {
            Log.e(tag, formattedMessage);
        }
    }

    public void d(String message, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.d(Logger.class.getName(), e.getMessage());
        }

        if (message.length() > 4000) {
            Log.d(tag, formattedMessage);
            d(formattedMessage.substring(4000));
        } else {
            Log.d(tag, formattedMessage);
        }
    }

    public void d(String message, Throwable t, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.d(Logger.class.getName(), e.getMessage(), t);
        }

        if (message.length() > 4000) {
            Log.d(tag, formattedMessage, t);
            d(formattedMessage.substring(4000), t);
        } else {
            Log.d(tag, formattedMessage, t);
        }
    }

    public void w(String message, Throwable t, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.w(Logger.class.getName(), e.getMessage(), t);
        }

        if (message.length() > 4000) {
            Log.w(tag, formattedMessage, t);
            w(formattedMessage.substring(4000), t);
        } else {
            Log.w(tag, formattedMessage, t);
        }
    }

    public void i(String message, Throwable t, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.i(Logger.class.getName(), e.getMessage(), t);
        }

        if (message.length() > 4000) {
            Log.i(tag, formattedMessage, t);
            i(formattedMessage.substring(4000), t);
        } else {
            Log.i(tag, formattedMessage, t);
        }
    }

    public void e(String message, Throwable t, Object... format) {
        String tag = createTag();
        String formattedMessage = "";
        try {
            formattedMessage = String.format(message, format);
        } catch (IllegalFormatConversionException e) {
            Log.e(Logger.class.getName(), e.getMessage(), t);
        }

        if (message.length() > 4000) {
            Log.e(tag, formattedMessage, t);
            e(formattedMessage.substring(4000), t);
        } else {
            Log.e(tag, formattedMessage, t);
        }
    }

    private String createTag() {
        return clazz.getSimpleName() + " [" + Thread.currentThread().getName() +"] " + dateFormat.format(System.currentTimeMillis());
    }
}
