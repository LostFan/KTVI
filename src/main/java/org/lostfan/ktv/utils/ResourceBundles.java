package org.lostfan.ktv.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public final class ResourceBundles {

    private static ResourceBundle entityBundle = ResourceBundle.getBundle("EntityBundle", new Locale("ru"));
    private static ResourceBundle guiBundle = ResourceBundle.getBundle("GuiBundle", new Locale("ru"));

    public static ResourceBundle getEntityBundle() {
        return entityBundle;
    }

    public static ResourceBundle getGuiBundle() {
        return guiBundle;
    }

    private ResourceBundles() { }
}
