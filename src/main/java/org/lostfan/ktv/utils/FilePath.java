package org.lostfan.ktv.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FilePath {

    private static final String FILE_NAME = "file_paths";

    private static Map<String, FilePath> filePaths;

    public static void setFilePath(String formName, String path) {
        if (filePaths == null) {
            load();
        }

        FilePath filePath = filePaths.get(formName);
        if (filePath == null) {
            filePath = new FilePath();
            filePaths.put(formName, filePath);
            filePath.formName = formName;
        }

        filePath.path = path;

        save();
    }

    public static FilePath getFilePath(String formName) {
        if (filePaths == null) {
            load();
        }

        return filePaths.get(formName);
    }

    private static void load() {
        filePaths = new HashMap<>();

        File filePathsFile = new File(FILE_NAME);
        if (filePathsFile.exists() && !filePathsFile.isDirectory()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filePathsFile));
                String line;
                while ((line = br.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line, "*");
                    if (tokenizer.countTokens() < 2) {
                        continue;
                    }
                    FilePath filePath = new FilePath();
                    filePath.formName = tokenizer.nextToken();
                    try {
                        filePath.path = tokenizer.nextToken();
                    } catch (NumberFormatException ex) {
                        continue;
                    }

                    filePaths.put(filePath.formName, filePath);
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
            for (FilePath filePath : filePaths.values()) {
                bw.write(String.format("%s*%s", filePath.getFormName(), filePath.getPath()));
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
    private String path;

    public String getUser() {
        return user;
    }

    public String getFormName() {
        return formName;
    }

    public String getPath() {
        return path;
    }
}
