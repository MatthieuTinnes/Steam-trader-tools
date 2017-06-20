package com.matthieu42.steamtradertools.model;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by matthieu on 11/03/17.
 */
public final class I18n {
    private I18n() {
    }

    private static Locale locale;
    private static ResourceBundle bundle;

    public static String getMessage(String key) {
        return bundle.getString(key);
    }

    public static void setLocale(Locale locale) {
        I18n.locale = locale;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getResourceBundle() {
        return bundle;
    }

    public static void setBundle(String bundlePath, Locale locale) {

        I18n.bundle = getBundle(bundlePath,locale);
    }
}