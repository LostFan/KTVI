package org.lostfan.ktv.model;

public enum FileMenu {

    EDIT_PERIOD("menu.file.period");

    private final String code;

    public static FileMenu of(String code) {
        for (FileMenu fileMenu : FileMenu.values()) {
            if (fileMenu.code.equals(code)) {
                return fileMenu;
            }
        }
        return null;
    }

    FileMenu(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
