package org.lostfan.ktv.model;


import java.io.*;;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConverterModel {


    private FileMenu conversionType;

    public ConverterModel(FileMenu conversionType) {
        this.conversionType = conversionType;
    }

    public String getName() {
        return conversionType.getCode();
    }

    public String convert(File[] files) {
        for (File file : files) {
            loadPost(file);
        }
        return null;
    }

    private String loadPost(File file) {
        List<String> strings = new ArrayList<>();
        String fileName = file.getName();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            boolean isFirstRow = true;
            while ((sCurrentLine = br.readLine()) != null) {
                if(fileName.substring(fileName.length() -3).equals("210")) {
                    strings.add(create210Row(sCurrentLine));
                } else if (fileName.substring(fileName.length() -3).equals("202")) {
                    if(isFirstRow) {
                        strings.add(create202FirstRow(sCurrentLine));
                        isFirstRow = false;
                    } else {
                        strings.add(create202Row(sCurrentLine));
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return saveToFile(strings,fileName);
    }

    private String create210Row(String sCurrentLine)  {
        StringBuffer newRow = new StringBuffer();

        String str[] = sCurrentLine.split("\\^");

        for (int i = 0; i < str.length; i++) {
            if(i == 6 || i == 7 || i == 8) {
                String newPrice = convert(str[i]);
                newRow.append(newPrice);
            }
            else {
                newRow.append(str[i]);
            }
            newRow.append("^");
        }
        return newRow.toString();
    }

    private String create202Row(String sCurrentLine)  {
        StringBuffer newRow = new StringBuffer();

        String str[] = sCurrentLine.split("\\^");

        for (int i = 0; i < str.length; i++) {
            if(i == 5) {
                String newPrice = convert(str[i]);
                newRow.append(newPrice);
            }
            else {
                newRow.append(str[i]);
            }
            newRow.append("^");
        }
        return newRow.toString();
    }

    private String create202FirstRow(String sCurrentLine)  {
        StringBuffer newRow = new StringBuffer();

        String str[] = sCurrentLine.split("\\^");

        for (int i = 0; i < str.length; i++) {
            if(i == 10) {
                String newPrice = convert(str[i]);
                newRow.append(newPrice);
            }
            else {
                newRow.append(str[i]);
            }
            newRow.append("^");
        }
        return newRow.toString();
    }

    private String convert(String str) {
        if(conversionType == FileMenu.CONVERTER_10000_TO_1) {
            Double aDouble = Double.parseDouble(str) / 10000;
            return aDouble.toString();
        }
        if(conversionType == FileMenu.CONVERTER_1_TO_10000) {
            Double aDouble = Double.parseDouble(str) * 10000;
            return aDouble.toString();
        }
        return str;
    }

    private String saveToFile(List<String> rows, String name) {
        String message = null;

        DateTimeFormatter yearMonthDayFormatter = DateTimeFormatter.ofPattern("YYYYMM");
        BufferedWriter bw = null;
        try {
            File file = new File("Converted files");
            file.mkdir();
            bw = new BufferedWriter(new FileWriter(file.getPath() + "\\" +
                    name));
            for (String row : rows) {
                bw.write(row);
                bw.newLine();
            }
        } catch (IOException ex) {
            if (ex instanceof FileNotFoundException) {

                message = "message.fileIsUsed";
            } else {
                message = "message.fail";
            }
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    if (e instanceof FileNotFoundException) {

                        message = "message.fileIsUsed";
                    } else {
                        message = "message.fail";
                    }
                }
            }
        }
        return message;
    }
}
