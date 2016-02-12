package org.lostfan.ktv.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FormSize {

    private static final File FILE = new File("form sizes.txt");;

    private String user;
    private String formName;
    private int width;
    private int height;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static void saveFormSize(String formName, Integer width, Integer height) {
        FileOutputStream fop;
        String content = new StringBuffer(formName)
                .append(";")
                .append(width)
                .append(";")
                .append(height)
                .toString();
        if(!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (FILE.exists() && !FILE.isDirectory()) {
            try {

                FileReader fr = new FileReader(FILE);
                BufferedReader br = new BufferedReader(fr);
                StringBuffer lines = new StringBuffer();
                String line;
                while((line = br.readLine()) != null) {
                    if(line.split(";")[0].equals(formName)) {
                        lines.append(content);
                    } else {
                        lines.append(line);
                    }
                    lines.append("\n");
                }
                if(!lines.toString().contains(content)) {
                    lines.append(content);
                    lines.append("\n");
                }
                byte[] contentInBytes = lines.toString().getBytes();
                br.close();

                fop = new FileOutputStream(FILE);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();

                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static FormSize getFormSize(String formName) {
        if (FILE.exists() && !FILE.isDirectory()) {
            try {
                FileReader fr = new FileReader(FILE);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while((line = br.readLine()) != null) {
                    if (line.split(";")[0].equals(formName)) {
                        FormSize formSize = new FormSize();
                        formSize.setFormName(formName);
                        formSize.setWidth(Integer.parseInt(line.split(";")[1]));
                        formSize.setHeight(Integer.parseInt(line.split(";")[2]));
                        return formSize;
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
