package org.lostfan.ktv.utils;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RenderedServicesLoader {


    public RenderedServicesLoader() {
    }

    public List<RenderedService> load(File file) {
        List<RenderedService> renderedServices = new ArrayList<>();
        if(file.getName().endsWith(".txt")) {
            renderedServices = loadTXT(file);
        }

        return renderedServices;
    }

    private List<RenderedService> loadTXT(File file) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String str[] = sCurrentLine.split(";");
                try {
                    RenderedService renderedService = new RenderedService();
                    renderedService.setSubscriberAccount(Integer.parseInt(str[0]));
                    renderedService.setDate(createDate(str[1]));
//                    renderedService.setBankFileName(file.getName());
//                    renderedService.setPrice(Integer.parseInt(str[6].split("\\.")[0]));
                    renderedService.setPrice(new BigDecimal(str[3]));
                    renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
                    renderedServices.add(renderedService);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return renderedServices;
    }

    private LocalDate createDate(String s) {
        return LocalDate.of(Integer.parseInt(s.substring(4, 8)), Integer.parseInt(s.substring(2, 4)), Integer.parseInt(s.substring(0, 2)));
    }

}
