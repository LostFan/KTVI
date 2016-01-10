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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.dao.impl.postgre.PostGreDaoFactory;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.lostfan.ktv.utils.PostgreConnectionManager;
import org.lostfan.ktv.utils.ResourceBundles;


public class ReadCVS {
    public static void main(String[] args) {
        ReadCVS obj = new ReadCVS();
        ConnectionManager.setManager(new PostgreConnectionManager());
        DAOFactory.setDefaultDAOFactory(new PostGreDaoFactory());


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

//        SELECT "service_id", "subscriber_account",SUM("price") as "price" FROM "rendered_service"  group by "service_id", "subscriber_account"

//       SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
//        FROM "payment"  group by  "payment"."service_id", "payment". "subscriber_account" LEFT JOIN (
//                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "price"
//        FROM "rendered_service"  group by "rendered_service"."service_id", "rendered_service". "subscriber_account")
//        ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id"

//        SELECT "payment"."service_id",  "payment"."subscriber_account"  , "payment"."payment_price", "rendered_service"."rendered_service_price" ,
//                "payment"."payment_price" -  "rendered_service"."rendered_service_price"  as "Balance"
//        from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
//                FROM "payment"         group by  "payment"."service_id", "payment". "subscriber_account") as "payment" INNER JOIN (
//                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
//        FROM "rendered_service"  group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
//        ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id"
////        order by  "payment"."service_id"

//        SELECT * FROM(SELECT "payment"."service_id",  "payment"."subscriber_account"  , "payment"."payment_price", "rendered_service"."rendered_service_price" ,
//                "payment"."payment_price" -  "rendered_service"."rendered_service_price"  as "Balance"
//                from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
//                        FROM "payment" where  "payment"."date"<'2015-05-01'        group by  "payment"."service_id", "payment". "subscriber_account") as "payment" INNER JOIN (
//                        SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
//                        FROM "rendered_service" where  "rendered_service"."date"<'2015-05-01'  group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
//                ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id" where "payment"."service_id" = 1) as "table" INNER JOIN (
//                SELECT * from "subscriber") as "subscriber" ON "subscriber"."account"= "table"."subscriber_account" order by "subscriber"."street_id","subscriber"."house"
    }
    public void createServices() {
        Service service = new Service();
        service.setName(getString("subscriptionFee"));
        service.setId(FixedServices.SUBSCRIPTION_FEE.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("connection"));
        service.setId(FixedServices.CONNECTION.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);

        service = new Service();
        service.setName(getString("reconnection"));
        service.setId(3);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("disconnection"));
        service.setId(4);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("changeOfTariff"));
        service.setId(5);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("settingUpTV"));
        service.setAdditionalService(true);
        service.setId(6);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("materialsService"));
        service.setAdditionalService(true);
        service.setId(7);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("workOnTheReplacementOfMaterials"));
        service.setAdditionalService(true);
        service.setId(8);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("connectingExtTV"));
        service.setAdditionalService(true);
        service.setId(9);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("workOnConnections"));
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
                    subscriber.setContractDate(LocalDate.of(Integer.parseInt(contractDate[2]) + 2000, Integer.parseInt(contractDate[1]), Integer.parseInt(contractDate[0])));
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

//        Set<Integer> set = new HashSet<>();
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
                renderedService.setDate(LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1));
                renderedService.setSubscriberAccount(parseInt(row[1]));


                renderedService.setPrice(parseInt(row[5]));
                if(parseInt(row[2]) == 2004 && parseInt(row[3]) ==  3 && !row[4].equals("") && !row[4].equals(" ")) {
                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
                }
//                if(!set.contains(parseInt(row[1]))) {
//                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
//                }
//                set.add(parseInt(row[1]));
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
                if(row[4] == "" || row[4] == "0" || row[4] == " " ) {
                    continue;
                }
                payment.setSubscriberAccount(parseInt(row[0]));
                payment.setPrice(parseInt(row[4]));
                payment.setDate(parseDateHyphen(row[1]));
                if(parseInt(row[5]) == 2) {
                    switch (row[6]) {
                        case "001" :  payment.setServicePaymentId(3); break;
                        case "002" :  payment.setServicePaymentId(9); break;
                        case "003" :  payment.setServicePaymentId(6); break;
                        case "004" :  payment.setServicePaymentId(5); break;
                        case "005" :  payment.setServicePaymentId(7); break;
                        case "006" :  payment.setServicePaymentId(8); break;
                        case "007" :  payment.setServicePaymentId(10); break;
                    }
                    if(DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().get(parseInt(row[7]) + 1000000) != null) {
                        payment.setRenderedServicePaymentId(parseInt(row[7]) + 1000000);
                    }
                }
                if(parseInt(row[5]) == 0) {
                    payment.setServicePaymentId(2);
                    RenderedService renderedService = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getFirstRenderedServiceLessDate(2, parseInt(row[0]), LocalDate.now());
                    if (renderedService != null) {
                        payment.setRenderedServicePaymentId(renderedService.getId());
                    }
                }
                if(parseInt(row[5]) == 1) {
                    payment.setServicePaymentId(1);
                }
                DAOFactory.getDefaultDAOFactory().getPaymentDAO().save(payment);
                size++;
            }
            System.out.println(size + " payments have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No payments loaded.");
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

    private String getString(String str) {
        return ResourceBundles.getEntityBundle().getString(str);
    }

}

