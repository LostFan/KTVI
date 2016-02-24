package org.lostfan.ktv.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FormSize {

    private static final String FILE_NAME = "form_sizes";

    private static Map<String, FormSize> formSizes;

    public static void setFormSize(String formName, int width, int height) {
        if (formSizes == null) {
            load();
        }

        FormSize formSize = formSizes.get(formName);
        if (formSize == null) {
            formSize = new FormSize();
            formSizes.put(formName, formSize);
            formSize.formName = formName;
        }

        formSize.width = width;
        formSize.height = height;

        save();
    }

    public static FormSize getFormSize(String formName) {
        if (formSizes == null) {
            load();
        }

        return formSizes.get(formName);
    }

    private static void load() {
        formSizes = new HashMap<>();

        File formSizesFile = new File(FILE_NAME);
        if (formSizesFile.exists() && !formSizesFile.isDirectory()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(formSizesFile));
                String line;
                while((line = br.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line, "=:");
                    if (tokenizer.countTokens() < 3) {
                        continue;
                    }
                    FormSize formSize = new FormSize();
                    formSize.formName = tokenizer.nextToken();
                    try {
                        formSize.width = Integer.parseInt(tokenizer.nextToken());
                        formSize.height = Integer.parseInt(tokenizer.nextToken());
                    } catch (NumberFormatException ex) {
                        continue;
                    }

                    formSizes.put(formSize.formName, formSize);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void save() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (FormSize formSize : formSizes.values()) {
                bw.write(String.format("%s=%d:%d", formSize.getFormName(), formSize.getWidth(), formSize.getHeight()));
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String user;
    private String formName;
    private int width;
    private int height;

    public String getUser() {
        return user;
    }

    public String getFormName() {
        return formName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
