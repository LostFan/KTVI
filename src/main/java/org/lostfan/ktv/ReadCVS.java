package org.lostfan.ktv;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;


public class ReadCVS {
    public static void main(String[] args) {
        ReadCVS obj = new ReadCVS();
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());


        obj.run();
        ConnectionManager.getManager().close();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // steps
    //create services
    //load subscribers
    //load subs fees
    //load connections - rend services
    //load addit services
    //load payments
    //create connections
    //create sessions  and subs tariffs


    public void run() {
//        getPayments();
//        getSubscribersIdWithoutConnection();
//        createServices();
//        loadStreets();
//        loadSubscribers();
//        loadSubscriptionFees();
//        loadConnections();
//        loadAdditionalServices();
        loadPayments();

//        loadTariffs();
//        loadTariffPrices();
//        loadServices();
//        loadServicePrices();
//        loadDisconnectionReasons();
//        loadSubscribers();
    }

    public List<Integer> getSubscribersIdWithoutConnection() {
            List<Integer> ids = new ArrayList<>();
            try {
                PreparedStatement preparedStatement = ConnectionManager.getManager().getConnection().prepareStatement("SELECT \"subscriber\".\"account\" FROM \"subscriber\"\n" +
                        "        LEFT JOIN (SELECT * from \"rendered_service\" WHERE \"service_id\" = 2) ON \"subscriber_account\" = \"subscriber\".\"account\"\n" +
                        "        where \"subscriber_account\" IS NULL");
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("account"));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        for (Integer id : ids) {
            System.out.println(id);
        }
            return ids;

    }
    public void getPayments() {
        List<RenderedService> renderedServiceList = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getRenderedServicesByServiceId(2);
//        renderedServiceList.stream()
//        SELECT * FROM "subscriber"
//        inner join "service" on ("account"="id" +10)

//        SELECT * FROM "rendered_service"
//        right join "subscriber" on ("subscriber_account"="account")
//        where "service_id" = 2

//        SELECT "account" FROM "rendered_service"
//        right OUTER  join "subscriber" on ("subscriber_account"="account")
//        where "service_id" = 2   GROUP BY "account" HAVING COUNT("account")>1

//        SELECT "subscriber"."account" FROM "subscriber"
//        LEFT JOIN (SELECT * from "rendered_service" WHERE "service_id" = 2) ON "subscriber_account" = "subscriber"."account"
//        where "subscriber_account" IS NULL
    }
    public void createServices() {
        Service service = new Service();
        service.setName("Абонентская плата");
        service.setId(FixedServices.SUBSCRIPTION_FEE.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Подключение");
        service.setId(FixedServices.CONNECTION.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);

        service = new Service();
        service.setName("Повторное подключение");
        service.setId(3);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Отключение");
        service.setId(4);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Смена тарифного плана");
        service.setId(5);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Настройка телевизора");
        service.setAdditionalService(true);
        service.setId(6);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Материалы");
        service.setAdditionalService(true);
        service.setId(7);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Работа по замене материалов");
        service.setAdditionalService(true);
        service.setId(8);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Подключение доп. ТВ");
        service.setAdditionalService(true);
        service.setId(9);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName("Работа по подключениям");
        service.setId(10);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
    }
    public void loadStreets() {
        String csvFile = "BASES/SP_OB.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                Street street = new Street();
                String[] row = line.split(cvsSplitBy);
                if(parseInt(row[0]) != 5) {
                    continue;
                }
                street.setId(parseInt(row[1]));
                street.setName(row[2]);
                DAOFactory.getDefaultDAOFactory().getStreetDAO().save(street);
                size++;
            }
            System.out.println(size + " streets have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No subscribers loaded.");
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

    public void loadTariffs() {
        String csvFile = "BASES/SP_OB.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                Street street = new Street();
                String[] row = line.split(cvsSplitBy);
                if(parseInt(row[0]) != 5) {
                    continue;
                }
                street.setId(parseInt(row[1]));
                street.setName(row[2]);
                DAOFactory.getDefaultDAOFactory().getStreetDAO().save(street);
                size++;
            }
            System.out.println(size + " subscribers have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No subscribers loaded.");
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

    public void loadSubscribers() {
        String csvFile = "BASES/ABONENT.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                Subscriber subscriber = new Subscriber();
                String[] row = line.split(cvsSplitBy);

                subscriber.setId(parseInt(row[1]));
                subscriber.setName(row[2] + " " + row[3] + " "  + row[4]);
                subscriber.setStreetId(parseInt(row[5]));
                subscriber.setBalance(0);
                subscriber.setHouse(parseInt(row[6]) + row[7]);
                subscriber.setBuilding(row[8]);
                subscriber.setFlat(row[9]);
                subscriber.setPhone(row[10]);
                try {
                    String[] contractDate = row[11].split("-");
                    subscriber.setContractDate(LocalDate.of(Integer.parseInt(contractDate[2]), Integer.parseInt(contractDate[1]), Integer.parseInt(contractDate[0])));
                } catch (Exception e) {
                    System.out.println("Exception:");
                    System.out.println(row[1]);
                    System.out.println(row[11]);
                }
                DAOFactory.getDefaultDAOFactory().getSubscriberDAO().save(subscriber);
                size++;
            }
            System.out.println(size + " subscribers have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No subscribers loaded.");
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

    public void loadSubscriptionFees() {
        String csvFile = "BASES/history.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);
                if(row[0].equals("0") || row[2].equals("0") || row[5].equals("0"))
                    continue;
                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
                renderedService.setDate(LocalDate.of(parseInt(row[2]),parseInt(row[3]),1));
                renderedService.setSubscriberAccount(parseInt(row[1]));
                renderedService.setPrice(parseInt(row[5]));
                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
//                subscriber.setId(parseInt(row[1]));
//                subscriber.setName(row[2] + " " + row[3] + " "  + row[4]);
//                subscriber.setStreetId(parseInt(row[5]));
//                subscriber.setBalance(0);
//                subscriber.setHouse(row[6] + row[7]);
//                subscriber.setBuilding(row[8]);
//                subscriber.setFlat(row[9]);
//                subscriber.setPhone(row[10]);
//                String[] contractDate = row[11].split("-");
//                subscriber.setContractDate(LocalDate.of(Integer.parseInt(contractDate[2]),Integer.parseInt(contractDate[1]), Integer.parseInt(contractDate[0])));
//                DAOFactory.getDefaultDAOFactory().getSubscriberDAO().save(subscriber);
                size++;
            }
            System.out.println(size + " subscriptionFees have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No subscriptionFees loaded.");
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

    public void loadConnections() {
        String csvFile = "BASES/history.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            List<Integer> integers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);
                if(row[0].equals("1") || row[7].equals(" "))
                    continue;
                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.CONNECTION.getId());
                renderedService.setDate(parseDate(row[7]));
                renderedService.setSubscriberAccount(parseInt(row[1]));
                renderedService.setPrice(parseInt(row[5]));
//                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
//                subscriber.setId(parseInt(row[1]));
//                subscriber.setName(row[2] + " " + row[3] + " "  + row[4]);
//                subscriber.setStreetId(parseInt(row[5]));
//                subscriber.setBalance(0);
//                subscriber.setHouse(row[6] + row[7]);
//                subscriber.setBuilding(row[8]);
//                subscriber.setFlat(row[9]);
//                subscriber.setPhone(row[10]);
//                String[] contractDate = row[11].split("-");
//                subscriber.setContractDate(LocalDate.of(Integer.parseInt(contractDate[2]),Integer.parseInt(contractDate[1]), Integer.parseInt(contractDate[0])));
                if(!integers.contains(renderedService.getSubscriberAccount())) {
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                }
                integers.add(renderedService.getSubscriberAccount());
                size++;
            }
            System.out.println(size + " connections have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No connections loaded.");
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

    public void loadAdditionalServices() {
        String csvFile = "BASES/his_d.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);
                if(row.length < 5 || row[4].equals(""))
                    continue;
                RenderedService renderedService = new RenderedService();

                switch (row[1]) {
                    case "001" :  renderedService.setServiceId(3); break;
                    case "002" :  renderedService.setServiceId(9); break;
                    case "003" :  renderedService.setServiceId(6); break;
                    case "004" :  renderedService.setServiceId(5); break;
                    case "005" :  renderedService.setServiceId(7); break;
                    case "006" :  renderedService.setServiceId(8); break;
                    case "007" :  renderedService.setServiceId(10); break;
                }
                renderedService.setId(parseInt(row[2]) + 1000000);
                renderedService.setDate(parseDateHyphen(row[4]));
                renderedService.setSubscriberAccount(parseInt(row[0]));
                renderedService.setPrice(parseInt(row[5]));

                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                size++;
            }
            System.out.println(size + " additional services have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No additional services loaded.");
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

    public void loadPayments() {
        String csvFile = "BASES/kvit.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);
                Payment payment = new Payment();

                payment.setSubscriberAccount(parseInt(row[0]));
                if(parseInt(row[5]) == 2) {
                    payment.setRenderedServicePaymentId(parseInt(row[6]) + 1000000);
                }
                if(parseInt(row[5]) == 0) {
                    RenderedService renderedService = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getFirstRenderedServiceLessDate(2, parseInt(row[0]), LocalDate.now());
                    if (renderedService != null) {
                        payment.setRenderedServicePaymentId(renderedService.getId());
                    }
                }
//                DAOFactory.getDefaultDAOFactory().getPaymentDAO().save(payment);
                size++;
            }
            System.out.println(size + " additional services have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No additional services loaded.");
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

    private Boolean parseBoolean(String element) {
        return element.equals("Да");
    }


    private LocalDate parseDate(String element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(element, formatter);
        return date;
    }

    private LocalDate parseDateHyphen(String element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate date = LocalDate.parse(element, formatter);
        return date;
    }

    private Integer parseInt(String str) {
        // char code 160 is a space char
        str = str.replaceAll("\u00A0", "");
        if (str.length() == 0) {
            return null;
        }

        Integer number;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            number = null;
        }
        return number;
    }

}

