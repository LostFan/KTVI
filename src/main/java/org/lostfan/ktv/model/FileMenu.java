package org.lostfan.ktv.model;

public enum FileMenu {

    EDIT_PERIOD("menu.file.period"),
    CONVERTER_10000_TO_1("menu.file.converter10000To1"),
    CONVERTER_1_TO_10000("menu.file.converter1To10000");

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
