package org.lostfan.ktv.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public final class ResourceBundles {

    private static Locale defaultLocale = new Locale("ru");

    private static ResourceBundle entityBundle = ResourceBundle.getBundle("EntityBundle", defaultLocale);
    private static ResourceBundle guiBundle = ResourceBundle.getBundle("GuiBundle", defaultLocale);

    public static ResourceBundle getEntityBundle() {
        return entityBundle;
    }

    public static ResourceBundle getGuiBundle() {
        return guiBundle;
    }

    private ResourceBundles() { }
}
