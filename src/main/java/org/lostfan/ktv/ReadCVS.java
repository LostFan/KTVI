package org.lostfan.ktv;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.postgre.PostGreDaoFactory;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.PostgreConnectionManager;
import org.lostfan.ktv.utils.ResourceBundles;
//
public class ReadCVS {
    public static void main(String[] args) {
        ReadCVS obj = new ReadCVS();
        ConnectionManager.setManager(new PostgreConnectionManager());
        DAOFactory.setDefaultDAOFactory(new PostGreDaoFactory());
//
//
        obj.run();
        ConnectionManager.getManager().close();

////        try {
////            Thread.sleep(15000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
    }
    //
//    // steps
//    //create services
//    //load subscribers
//    //load subs fees
//    //load connections - rend services
//    //load addit services
//    //load payments
//    //create connections
//    //create sessions  and subs tariffs
//
//
    public void run() {
//        getPayments();
//        getSubscribersIdWithoutConnection();
//        getABFile();
        createDisconnect();
//        showWrongSubscriptionFees();
//
//        loadTariffs();
//        createServices();
//        loadStreets();
//        loadSubscribers();
//        loadSubscriptionFees();
//        loadConnections();
//        loadAdditionalServices();
//        loadPayments();
//        isEqaual();
//        updateTariffsInSubscriberTariffs();



//        createFirstSessionAndSubscriberTariff();


//        for (Payment payment :  DAOFactory.getDefaultDAOFactory().getPaymentDAO().getByDate(LocalDate.of(2003,03,30))) {
//            DAOFactory.getDefaultDAOFactory().getPaymentDAO().delete(payment.getId());
//
//        }


//        compareSubFees();
//        compareCon();






//        loadTariffPrices();
//        loadServices();
//        loadServicePrices();
//        loadDisconnectionReasons();
//        loadSubscribers();
    }



    //
//    private void compareSubFees() {
//        Map<Integer, Integer> mapFile = getABFile();
//        Map<Integer, Integer> mapBase = getBase("ABON.txt");
//        Map<Integer, Integer> mapBase2 = getBase("2.txt");
//        int size = 0;
//        int sum = 0;
//        System.out.println(mapBase.size());
//
//        for (Integer integer : mapBase.keySet()) {
//            if( mapFile.get(integer).intValue() !=  mapBase.get(integer).intValue()) {
//                System.out.println(integer + "\t" + mapFile.get(integer) + "\t" + mapBase.get(integer) + "\t" + (mapBase.get(integer) - mapFile.get(integer)));
//                size++;
//                sum += mapBase.get(integer).intValue() - mapFile.get(integer).intValue();
//            }
//        }
//        for (Integer integer : mapFile.keySet()) {
//            if(mapBase.get(integer) == null && mapFile.get(integer)!=0) {
//                System.out.println(integer + "\t" + mapFile.get(integer) + "\t" + 0);
//                sum+= -mapFile.get(integer);
//                size++;
//            }
//
//        }
//        System.out.println(sum);
//        System.out.println(mapFile.size());
//
//        for (Integer integer : mapBase2.keySet()) {
//            if(mapBase.get(integer) == null) {
//                System.out.println(integer + "\t" + mapBase2.get(integer) + "\t");
//            }
//        }
//    }
//
//    private void compareCon() {
//        Map<Integer, Integer> mapFile = getUSFile();
//        Map<Integer, Integer> mapBase = getBase("conn.txt");
//        for (Integer integer : mapBase.keySet()) {
//            if(mapBase.get(integer).intValue()< 0) {
//
//                if(integer <=5632) {
//                    Payment payment = new Payment();
//                    payment.setSubscriberAccount(integer);
//                    payment.setPrice(mapBase.get(integer).doubleValue() * -1);
//                    RenderedService renderedService = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO()
//                            .getBySubscriber(integer)
//                            .stream().filter(renderedServiceIn -> renderedServiceIn.getServiceId() == 2)
//                            .findFirst().get();
//                    payment.setDate(renderedService.getDate());
//                    payment.setServicePaymentId(renderedService.getServiceId());
//                    payment.setRenderedServicePaymentId(renderedService.getId());
//                }
//                System.out.println(integer + "\t" + mapBase.get(integer));
//            }
//
//        }
////        int size = 0;
////        int sum = 0;
////        System.out.println(mapFile.size());
////
////        for (Integer integer : mapFile.keySet()) {
////            if(!mapBase.containsKey(integer)) {
////                System.out.println(integer);
////                continue;
////            }
////            if( mapFile.get(integer).intValue() !=  mapBase.get(integer).intValue()) {
////                System.out.println(integer + "\t" + mapFile.get(integer) + "\t" + mapBase.get(integer) + "\t" + (mapBase.get(integer) - mapFile.get(integer)));
////                size++;
////                sum += mapBase.get(integer).intValue() - mapFile.get(integer).intValue();
////            }
////        }
////        for (Integer integer : mapFile.keySet()) {
////            if(mapBase.get(integer) == null && mapFile.get(integer)!=0) {
////                System.out.println(integer + "\t" + mapFile.get(integer) + "\t" + 0);
////                sum+= -mapFile.get(integer);
////                size++;
////            }
////
////        }
////        System.out.println(sum);
////        System.out.println(mapFile.size());
//
//    }
//
    public Map<Integer, BigDecimal> getABFile() {
        String csvFile = "BASES_KTVI/OB_AB_06.TXT";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\t";

        Map<Integer, BigDecimal> map = new HashMap<>();
        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            BigDecimal sum1 = BigDecimal.ZERO;
            BigDecimal sum2 = BigDecimal.ZERO;
            int count = 0 ;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if(row[0].length() >0 && row[0].substring(0,1).equals("7")) {
//                    System.out.println(parseInt(line.substring(0, 7).trim()));
//                    System.out.println(parseInt(line.substring(68, 75).trim()));
//                    System.out.println(parseInt(line.substring(75, 82).trim()));
//                    sum1 = sum1.add(parseBigDecimal(line.substring(83, 89).trim()));
//                    sum2 = sum2.add(parseBigDecimal(line.substring(90, 96).trim()));
//                    map.put(parseInt(line.substring(0, 7).trim()),parseBigDecimal(line.substring(83, 89).trim()).add(parseBigDecimal(line.substring(90, 96).trim()).negate()));
//                    System.out.println(line.substring(0, 7).trim() + "\t" + parseBigDecimal(line.substring(83, 89).trim()) + " \t" + parseBigDecimal(line.substring(90, 96).trim()));
                   count++;
                    sum1 = sum1.add(parseBigDecimal(line.substring(96, 103).trim()));
                    sum2 = sum2.add(parseBigDecimal(line.substring(103, 110).trim()));
                    map.put(parseInt(line.substring(0, 7).trim()),parseBigDecimal(line.substring(96, 103).trim()).add(parseBigDecimal(line.substring(103, 110).trim()).negate()));
                    System.out.println(line.substring(0, 7).trim() + "\t" + parseBigDecimal(line.substring(96, 103).trim()) + " \t" + parseBigDecimal(line.substring(103, 110).trim()));

                }
            }
            System.out.println("\t" + sum1 + "\t" + sum2 + " \t");
            System.out.println("\t" + count);
//            System.out.println(size + " streets have been loaded");
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
        return map;
    }
//
//    public Map<Integer, Integer> getUSFile() {
//        String csvFile = "BASES_KTVI/OB_US_11.TXT";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = "\t";
//
//        Map<Integer, Integer> map = new HashMap<>();
//        try {
//            FileInputStream fis =  new FileInputStream(csvFile);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            int sum1 = 0;
//            int sum2 = 0;
//            while ((line = br.readLine()) != null) {
//                String[] row = line.split(cvsSplitBy);
//                if(row[0].length() >0 && row[0].substring(0,1).equals("0")) {
////                    System.out.println(parseInt(line.substring(0, 7).trim()));
////                    System.out.println(parseInt(line.substring(68, 75).trim()));
////                    System.out.println(parseInt(line.substring(75, 82).trim()));
//                    sum1+=parseInt(line.substring(75, 82).trim());
//                    sum2+=parseInt(line.substring(68, 75).trim());
//                    map.put(parseInt(line.substring(0, 7).trim()),parseInt(line.substring(75, 82).trim()) - parseInt(line.substring(68, 75).trim()));
//                }
//            }
//            System.out.println(sum2 + "   " + sum1 + "   " + (sum2 - sum1));
////            System.out.println(size + " streets have been loaded");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("No subscribers loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return map;
//    }
//
//    public Map<Integer, Integer> getBase(String fileName) {
//        String csvFile = "BASES_KTVI/" + fileName;
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ";";
//
//        Map<Integer, Integer> map = new HashMap<>();
//        try {
//            FileInputStream fis =  new FileInputStream(csvFile);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            while ((line = br.readLine()) != null) {
//                String[] row = line.split(cvsSplitBy);
//                map.put(parseInt(row[1]), parseInt(row[4]));
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("No subscribers loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return map;
//    }
//
//    public List<Integer> getSubscribersIdWithoutConnection() {
//            List<Integer> ids = new ArrayList<>();
//            try {
//                PreparedStatement preparedStatement = ConnectionManager.getManager().getConnection().prepareStatement("SELECT \"subscriber\".\"account\" FROM \"subscriber\"\n" +
//                        "        LEFT JOIN (SELECT * from \"rendered_service\" WHERE \"service_id\" = 2) ON \"subscriber_account\" = \"subscriber\".\"account\"\n" +
//                        "        where \"subscriber_account\" IS NULL");
//                ResultSet rs = preparedStatement.executeQuery();
//                while (rs.next()) {
//                    ids.add(rs.getInt("account"));
//                }
//
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        for (Integer id : ids) {
//            System.out.println(id);
//        }
//            return ids;
//
//    }
//    public void getPayments() {
//        List<RenderedService> renderedServiceList = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getByService(2);
////        renderedServiceList.stream()
////        SELECT * FROM "subscriber"
////        inner join "service" on ("account"="id" +10)
//
////        SELECT * FROM "rendered_service"
////        right join "subscriber" on ("subscriber_account"="account")
////        where "service_id" = 2
//
////        SELECT "account" FROM "rendered_service"
////        right OUTER  join "subscriber" on ("subscriber_account"="account")
////        where "service_id" = 2   GROUP BY "account" HAVING COUNT("account")>1
//
////        SELECT "subscriber"."account" FROM "subscriber"
////        LEFT JOIN (SELECT * from "rendered_service" WHERE "service_id" = 2) ON "subscriber_account" = "subscriber"."account"
////        where "subscriber_account" IS NULL
//
////        SELECT "service_id", "subscriber_account",SUM("price") as "price" FROM "rendered_service"  group by "service_id", "subscriber_account"
//
////       SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
////        FROM "payment"  group by  "payment"."service_id", "payment". "subscriber_account" LEFT JOIN (
////                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "price"
////        FROM "rendered_service"  group by "rendered_service"."service_id", "rendered_service". "subscriber_account")
////        ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id"
//
////        SELECT "payment"."service_id",  "payment"."subscriber_account"  , "payment"."payment_price", "rendered_service"."rendered_service_price" ,
////                "payment"."payment_price" -  "rendered_service"."rendered_service_price"  as "Balance"
////        from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
////                FROM "payment"         group by  "payment"."service_id", "payment". "subscriber_account") as "payment" INNER JOIN (
////                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
////        FROM "rendered_service"  group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
////        ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id"
//////        order by  "payment"."service_id"
//
////        SELECT "payment"."service_id", "rendered_service"."subscriber_account",  "payment"."subscriber_account"  ,
////                (CASE WHEN "payment"."payment_price" is NULL THEN 0 ELSE "payment"."payment_price" END) AS "payment_price",
////                "rendered_service"."rendered_service_price" ,
////                (CASE WHEN "payment"."payment_price" is NULL THEN - "rendered_service"."rendered_service_price" ELSE "payment"."payment_price" - "rendered_service"."rendered_service_price" END) AS "Balance"
////        from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
////                FROM "payment" where  "payment"."date"<'2015-06-01'  AND "payment"."service_id" = 1      group by  "payment"."service_id", "payment". "subscriber_account") as "payment" RIGHT JOIN (
////                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
////        FROM "rendered_service" where  "rendered_service"."date"<'2015-06-01'  group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
////        ON ("payment"."subscriber_account" = "rendered_service"."subscriber_account") where "rendered_service"."service_id" = 1
//
////
////        SELECT * FROM(SELECT "payment"."service_id",  "payment"."subscriber_account"  , "payment"."payment_price", "rendered_service"."rendered_service_price" ,
////                "payment"."payment_price" -  "rendered_service"."rendered_service_price"  as "Balance"
////                from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
////                        FROM "payment" where  "payment"."date"<'2015-05-01'        group by  "payment"."service_id", "payment". "subscriber_account") as "payment" INNER JOIN (
////                        SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
////                        FROM "rendered_service" where  "rendered_service"."date"<'2015-05-01'  group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
////                ON "payment"."subscriber_account" = "rendered_service"."subscriber_account" AND "payment"."service_id"="rendered_service"."service_id" where "payment"."service_id" = 1) as "table" INNER JOIN (
////                SELECT * from "subscriber") as "subscriber" ON "subscriber"."account"= "table"."subscriber_account" order by "subscriber"."street_id","subscriber"."house"
//
////        SELECT "payment"."service_id", "rendered_service"."subscriber_account",  "payment"."subscriber_account"  ,
////                (CASE WHEN "payment"."payment_price" is NULL THEN 0 ELSE "payment"."payment_price" END) AS "payment_price",
////                (CASE WHEN "rendered_service"."rendered_service_price" is NULL THEN 0 ELSE "rendered_service"."rendered_service_price" END) AS "rendered_service_price",
////                (CASE WHEN "payment"."payment_price" is NULL THEN 0 ELSE "payment"."payment_price" END) -
////                (CASE WHEN "rendered_service"."rendered_service_price" is NULL THEN 0 ELSE "rendered_service"."rendered_service_price" END)AS "Balance"
////        from(SELECT "payment"."service_id", "payment"."subscriber_account", SUM("payment"."price") as "payment_price"
////                FROM "payment" where  "payment"."date"<'2015-11-01'  AND "payment"."service_id" = 1      group by  "payment"."service_id", "payment". "subscriber_account") as "payment" FULL JOIN (
////                SELECT "rendered_service". "service_id", "rendered_service"."subscriber_account",SUM("rendered_service"."price") as "rendered_service_price"
////        FROM "rendered_service" where  "rendered_service"."date"<'2015-11-01' AND "rendered_service"."service_id" = 1   group by "rendered_service"."service_id", "rendered_service". "subscriber_account") as  "rendered_service"
////        ON ("payment"."subscriber_account" = "rendered_service"."subscriber_account")
//
//
//
//    }
    public void createServices() {
        Service service = new Service();
        service.setName(getString("subscriptionFee"));
//        service.setId(FixedServices.SUBSCRIPTION_FEE.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("connection"));
//        service.setId(FixedServices.CONNECTION.getId());
        service.setAdditionalService(false);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);

        service = new Service();
        service.setName(getString("reconnection"));
        //service.setId(3);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("disconnection"));
        //service.setId(4);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("changeOfTariff"));
        //service.setId(5);
        service.setAdditionalService(true);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("settingUp"));
        service.setAdditionalService(true);
        //service.setId(6);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("materialsService"));
        service.setAdditionalService(true);
        //service.setId(7);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        service = new Service();
        service.setName(getString("workOnTheReplacementOfMaterials"));
        service.setAdditionalService(true);
        //service.setId(8);
        DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
    }
        public void loadStreets() {
        String csvFile = "BASES_KTVI/SP_OB.csv";
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
        String csvFile = "BASES_KTVI/SP_DOP.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Tariff tariffZero = new Tariff();
        tariffZero.setId(0);
        tariffZero.setName("!!!");
        DAOFactory.getDefaultDAOFactory().getTariffDAO().save(tariffZero);
        TariffPrice tariffPriceZero = new TariffPrice();
        tariffPriceZero.setTariffId(0);
        tariffPriceZero.setDate(LocalDate.of(2017,1,1));
        tariffPriceZero.setPrice(BigDecimal.ZERO);
        DAOFactory.getDefaultDAOFactory().getTariffDAO().saveTariffPrice(tariffPriceZero);

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
                Tariff tariff = new Tariff();
                tariff.setId(parseInt(row[0]));
                tariff.setName(row[1]);
                DAOFactory.getDefaultDAOFactory().getTariffDAO().save(tariff);
                TariffPrice tariffPrice = new TariffPrice();
                tariffPrice.setTariffId(parseInt(row[0]));
                tariffPrice.setDate(LocalDate.of(2017, 1, 1));
                tariffPrice.setPrice(parseBigDecimal(row[2]));
                DAOFactory.getDefaultDAOFactory().getTariffDAO().saveTariffPrice(tariffPrice);
                size++;
            }
            System.out.println(size + " tariffs have been loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No tariffs loaded.");
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
    //
//
//
    public void loadSubscribers() {
        String csvFile = "BASES_KTVI/ABONENT.CSV";
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
                subscriber.setHouse(parseInt(row[6]));
                subscriber.setIndex(row[7]);
                subscriber.setBuilding(row[8]);
                subscriber.setFlat(row[9]);
                subscriber.setPhone(row[10]);
                try {
                    subscriber.setContractDate(parseDate(row[11]));
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
//
//
//
    public void loadSubscriptionFees() {
        String csvFile = "BASES_KTVI/history.CSV";
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
                if(row.length < 6 || row[0].equals("0") || row[2].equals("0") || row[5].equals("0")
                        || row[1].equals("703972") || row[1].equals("708404"))
                    continue;

                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
                renderedService.setDate(LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1));
                renderedService.setSubscriberAccount(parseInt(row[1]));


                renderedService.setPrice(parseBigDecimal(row[5]));
//                if(!set.contains(parseInt(row[1]))) {
//                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
//                }
//                set.add(parseInt(row[1]));
                try {
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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


    public void showWrongSubscriptionFees() {
        String csvFile = "BASES_KTVI/history.CSV";
        String usersFile = "BASES_KTVI/users.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        Set<String> usersId = new HashSet<>();
        try {

            FileInputStream fise =  new FileInputStream(usersFile);
            br = new BufferedReader(new InputStreamReader(fise, "Cp1251"));

            while ((line = br.readLine()) != null) {
                usersId.add(line);

            }

            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));


            int size = -1;
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);
                if(row.length < 6 || row[0].equals("0") || row[2].equals("0") || row[5].equals("0")
                        || row[1].equals("703972") || row[1].equals("708404"))
                    continue;
                if(usersId.contains(row[1])) {
                    continue;
                }
                try {
                    BigDecimal ostVx = parseBigDecimal(row[4]) == null ? BigDecimal.ZERO : parseBigDecimal(row[4]);
                    if (!(parseBigDecimal(row[8]).equals(
                            ostVx.add(parseBigDecimal(row[5])).subtract(parseBigDecimal(row[6])))
                            ||
                        parseBigDecimal(row[8]).equals(
                                ostVx.add(parseBigDecimal(row[5])).subtract(parseBigDecimal(row[7]))))
                            && !(BigDecimal.ZERO.compareTo(parseBigDecimal(row[5])) == 0 && BigDecimal.ZERO.compareTo(parseBigDecimal(row[6])) == 0)) {
//                        System.out.println("\t " + parseBigDecimal(row[1]) +
//                                "\t " + LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1) +
//                                "\t " + ostVx +
//                                "\t " + parseBigDecimal(row[5]) +
//                                "\t " + parseBigDecimal(row[6]) +
//                                "\t " + parseBigDecimal(row[7]) +
//                                "\t " + parseBigDecimal(row[8]));
                        System.out.println("\t " + parseBigDecimal(row[1]) +
                                "\t " + LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1) +
                                "\t " + ostVx.add(parseBigDecimal(row[5])).subtract(parseBigDecimal(row[6])).subtract(parseBigDecimal(row[8])) +
                                "\t " + ostVx.add(parseBigDecimal(row[5])).subtract(parseBigDecimal(row[7])).subtract(parseBigDecimal(row[8])));
                        size++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.out.println("Array");
//                    System.out.println(line);
                }
//                RenderedService renderedService = new RenderedService();
//                renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
//                renderedService.setDate(LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1));
//                renderedService.setSubscriberAccount(parseInt(row[1]));



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
//
    public void loadConnections() {
        String csvFile = "BASES_KTVI/ABONENT.CSV";
        String csvFileDisc = "BASES_KTVI/USERS_LIST_Otklytchenye.CSV";
        String csvFileDel = "BASES_KTVI/USERS_LIST_Udalenye.CSV";
        String csvFileAll = "BASES_KTVI/USERS_LIST_Vse.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String cvsSplitBy2 = ";";

        Set<String> disc = new HashSet<>();
        Set<String> all = new HashSet<>();
//        try {
//            FileInputStream fis =  new FileInputStream(csvFileDisc);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            while ((line = br.readLine()) != null) {
//                if (size == -1) {
//                    size++;
//                    continue;
//                }
//                String[] row = line.split(cvsSplitBy2);
//                disc.add(row[1]);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        } catch (IOException e) {
//            System.out.println("No connections loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        try {
//            FileInputStream fis =  new FileInputStream(csvFileDel);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            while ((line = br.readLine()) != null) {
//                if (size == -1) {
//                    size++;
//                    continue;
//                }
//                String[] row = line.split(cvsSplitBy2);
//                disc.add(row[1]);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        } catch (IOException e) {
//            System.out.println("No connections loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        try {
//            FileInputStream fis =  new FileInputStream(csvFileAll);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            while ((line = br.readLine()) != null) {
//                if (size == -1) {
//                    size++;
//                    continue;
//                }
//                String[] row = line.split(cvsSplitBy2);
//                all.add(row[1]);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        } catch (IOException e) {
//            System.out.println("No connections loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            int disconnectionSize = 0;
            List<Integer> integers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (size == -1) {
                    size++;
                    continue;
                }
                String[] row = line.split(cvsSplitBy);

                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.CONNECTION.getId());
                if(row[13].equals("")) {
                    renderedService.setDate(parseDate(row[11]));
                } else {
                    renderedService.setDate(parseDate(row[13]));
                }
                renderedService.setSubscriberAccount(parseInt(row[1]));
                renderedService.setPrice(BigDecimal.ZERO);
//                renderedService.setPrice(parseBigDecimal(row[12]));

//                if(!integers.contains(renderedService.getSubscriberAccount())) {
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
//                }
//                integers.add(renderedService.getSubscriberAccount());

                SubscriberSession subscriberSession = new SubscriberSession();
                subscriberSession.setSubscriberAccount(renderedService.getSubscriberAccount());
                subscriberSession.setConnectionDate(renderedService.getDate());
                if(!all.contains(row[1]) || disc.contains(row[1])) {
//                    subscriberSession.setDisconnectionDate(parseDate(row[17]).plusDays(1));
                }
                DAOFactory.getDefaultDAOFactory().getSubscriberDAO().saveSubscriberSession(subscriberSession);

                SubscriberTariff subscriberTariff = new SubscriberTariff();
                subscriberTariff.setSubscriberAccount(renderedService.getSubscriberAccount());
                subscriberTariff.setConnectTariff(renderedService.getDate());
                if (DAOFactory.getDefaultDAOFactory().getTariffDAO().get(parseInt(row[18])) != null) {
                    subscriberTariff.setTariffId(parseInt(row[18]));
                } else {
                    subscriberTariff.setTariffId(0);
                }
//                if(parseInt(row[18]) == 0) {
//                    if(row.length == 20) {
//                        subscriberTariff.setDisconnectTariff(parseDateHyphen(row[19]));
//                    } else {
//                        subscriberTariff.setDisconnectTariff(parseDateHyphen(row[17]).plusDays(1));
//                    }
//                }

                DAOFactory.getDefaultDAOFactory().getSubscriberDAO().saveSubscriberTariff(subscriberTariff);

//                if(!all.contains(row[1]) || disc.contains(row[1])) {
//                    RenderedService disconnection = new RenderedService();
//                    disconnection.setServiceId(FixedServices.DISCONNECTION.getId());
//                    disconnection.setDate(parseDate(row[17]));
//                    disconnection.setSubscriberAccount(parseInt(row[1]));
//                    disconnection.setPrice(BigDecimal.ZERO);
//                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(disconnection);
//                    disconnectionSize++;
//                }
                size++;
            }
            System.out.println(size + " connections have been loaded");
            System.out.println(disconnectionSize + " disconnections have been loaded");
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
//
    public void loadAdditionalServices() {
        String csvFile = "BASES_KTVI/his_d.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Map<String, List<String[]>> renderedServices = new HashMap<>();
        Map<String, List<String[]>> payments = new HashMap<>();


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
                if ("1".equals(row[2])) {
                    List<String[]> list = renderedServices.get(row[0]);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(row);
                    renderedServices.put(row[0],list);
                } else {
                    List<String[]> list = payments.get(row[0]);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(row);
                    payments.put(row[0],list);
                }
//                RenderedService renderedService = new RenderedService();
//
//                switch (row[1]) {
//                    case "0" :  renderedService.setServiceId(2); break;
//                    case "1" :  renderedService.setServiceId(6); break;
//                    case "2" :  renderedService.setServiceId(6); break;
//                    case "4" :  renderedService.setServiceId(6); break;
//                    case "7" :  renderedService.setServiceId(8); break;
//                    case "9" :  renderedService.setServiceId(7); break;
//                }
//                renderedService.setId(parseInt(row[2]) + 1000000);
//                renderedService.setDate(parseDate(row[4]));
//                renderedService.setSubscriberAccount(parseInt(row[0]));
//                renderedService.setPrice(parseBigDecimal(row[5]));
//
//                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
//                size++;
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


        for (List<String[]> strings : renderedServices.values()) {
            for (String[] row : strings) {
                if(row[0].equals("717470") || row[0].equals("704654")) {
                    System.out.println();
                }
                Integer subscriberId = parseInt(row[0]);
                if (subscriberId == null || DAOFactory.getDefaultDAOFactory().getSubscriberDAO().get(subscriberId) == null) {
                    System.out.println("Wrong id " + row[0]);
                    continue;
                }

                RenderedService renderedService = new RenderedService();

                switch (row[1]) {
                    case "0":
                        renderedService.setServiceId(2);
                        break;
                    case "1":
                        renderedService.setServiceId(6);
                        break;
                    case "2":
                        renderedService.setServiceId(6);
                        break;
                    case "4":
                        renderedService.setServiceId(6);
                        break;
                    case "7":
                        renderedService.setServiceId(8);
                        break;
                    case "9":
                        renderedService.setServiceId(7);
                        break;
                }
                renderedService.setDate(parseDate(row[4]));
                renderedService.setSubscriberAccount(parseInt(row[0]));
                renderedService.setPrice(parseBigDecimal(row[5]));

                if(renderedService.getServiceId() == 2) {
                    List<RenderedService> rServices = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getBySubscriber(parseInt(row[0]));
                    rServices = rServices.stream()
                            .filter(e -> renderedService.getDate().equals(e.getDate())
                                    && renderedService.getServiceId().equals(e.getServiceId()))
                            .collect(Collectors.toList());
                    if(rServices.size() == 1) {
                        RenderedService service = rServices.get(0);
                        if(service.getId().equals("108315")) {
                            System.out.println();
                        }
                        service.setPrice(parseBigDecimal(row[5]));
                        DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().update(service);
                    }
                } else {
                    renderedService.setServiceId(3);
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                }


                List<String[]> paymentList = payments.get(row[0]);
                if (paymentList == null) {
                    continue;
                }
                paymentList = paymentList.stream().filter(e -> e[3].equals(row[3])).collect(Collectors.toList());

                List<RenderedService> rServices = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getBySubscriber(parseInt(row[0]));
                rServices = rServices.stream()
                        .filter(e -> renderedService.getDate().equals(e.getDate())
                                && renderedService.getServiceId().equals(e.getServiceId()))
                        .collect(Collectors.toList());

                if (rServices.size() == 1 && paymentList.size() == 1) {
                    Payment payment = new Payment();
                    payment.setSubscriberAccount(parseInt(row[0]));
                    payment.setPrice(parseBigDecimal(paymentList.get(0)[5]));
                    payment.setDate(parseDate(paymentList.get(0)[4]));
                    payment.setServicePaymentId(rServices.get(0).getServiceId());
                    payment.setRenderedServicePaymentId(rServices.get(0).getId());
                    DAOFactory.getDefaultDAOFactory().getPaymentDAO().save(payment);
                }
                if (rServices.size() > 1) {
                    System.out.println("!!!!! " + row[0]);
                    rServices = rServices.stream()
                            .filter(e -> renderedService.getPrice().equals(e.getPrice()))
                            .collect(Collectors.toList());
                    if(rServices.size() != 1) {
                        System.out.println("??????");
                    } else {
                        try {
                            Payment payment = new Payment();
                            payment.setSubscriberAccount(parseInt(row[0]));
                            payment.setPrice(parseBigDecimal(paymentList.get(0)[5]));
                            payment.setDate(parseDate(paymentList.get(0)[4]));
                            payment.setServicePaymentId(rServices.get(0).getServiceId());
                            payment.setRenderedServicePaymentId(rServices.get(0).getId());
                            DAOFactory.getDefaultDAOFactory().getPaymentDAO().save(payment);
                        } catch (Exception e) {
                            System.out.println(">>>>>>>> " + row);
                        }
                    }

                }
            }
//                size++;
        }
    }
//
    public void loadPayments() {
        String csvFile = "BASES_KTVI/kvit.CSV";
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
                if (DAOFactory.getDefaultDAOFactory().getSubscriberDAO().get(parseInt(row[0])) == null) {
                    continue;
                }
                if("714059".equals(row[0]) && "21.11.2016".equals(row[1])) {
                    System.out.println("?????????????");
                }
                payment.setSubscriberAccount(parseInt(row[0]));
                payment.setPrice(parseBigDecimal(row[4]));
                payment.setDate(parseDate(row[1]));
                payment.setServicePaymentId(1);
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

    private class CompareObject {
        String account;
        BigDecimal credit;
        BigDecimal debit;

        public CompareObject(String account, BigDecimal credit, BigDecimal debit) {
            this.account = account;
            this.credit = credit;
            this.debit = debit;
        }
    }

    public void isEqaual() {
        String csvFile1 = "1.TXT";
        String csvFile2 = "3.TXT";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = " ";
        Map<String, CompareObject> myMap = new HashMap<>();
        Map<String, CompareObject> hisMap = new HashMap<>();


        try {
            FileInputStream fis =  new FileInputStream(csvFile2);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {

                String[] row = line.split(cvsSplitBy);
                if (row.length < 3) {
                    continue;
                }
                size++;
                CompareObject compareObject = new CompareObject(row[0], parseBigDecimal(row[1]),parseBigDecimal(row[2]));
                myMap.put(row[0], compareObject);
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
        try {
            FileInputStream fis =  new FileInputStream(csvFile1);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {

                String[] row = line.split(cvsSplitBy);
                if (row.length < 3) {
                    continue;
                }
                size++;
                CompareObject compareObject = new CompareObject(row[0], parseBigDecimal(row[1]),parseBigDecimal(row[2]));
                hisMap.put(row[0], compareObject);
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
        Integer size = 0;

        for (String s : hisMap.keySet()) {
            CompareObject myObject = myMap.get(s);
            CompareObject hisObject = hisMap.get(s);
            if (myObject.credit.compareTo(hisObject.credit) != 0) {
                size++;
                System.out.println("Account = " + s + " credit my = " + myObject.credit + "  credit his = " + hisObject.credit);
                RenderedService renderedService = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getAllForMonth(1, parseInt(s), parseDate("01.07.2016")).get(0);
                renderedService.setPrice(renderedService.getPrice().add(hisObject.credit).subtract(myObject.credit));
//                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().update(renderedService);
            }
            if (myObject.debit.compareTo(hisObject.debit) != 0) {
                size++;
                System.out.println("Account = " + s + " debit my = " + myObject.debit + "  debit his = " + hisObject.debit);
                RenderedService renderedService = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getAllForMonth(1, parseInt(s), parseDate("01.07.2016")).get(0);
                System.out.println(renderedService.getPrice());
                renderedService.setPrice(renderedService.getPrice().add(myObject.debit).subtract(hisObject.debit));
                System.out.println(renderedService.getPrice());
//                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().update(renderedService);
            }
        }
        System.out.println(size);
    }

    private void createDisconnect() {

        String csvFile = "Udalennye.txt";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = " ";

//        Set<Integer> set = new HashSet<>();
        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                Integer subscriberAccount = parseInt(row[0]);
                if(subscriberAccount == null) {
                    continue;
                }

                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.DISCONNECTION.getId());
                renderedService.setDate(LocalDate.of(2019, 1, 1));
                renderedService.setSubscriberAccount(subscriberAccount);


                renderedService.setPrice(parseBigDecimal(row[1]));

                SubscriberSession subscriberSession =
                        DAOFactory.getDefaultDAOFactory()
                                .getSubscriberDAO().getNotClosedSubscriberSession(subscriberAccount,LocalDate.of(2019, 1, 1));
                if (subscriberSession != null) {
                    subscriberSession.setDisconnectionDate(LocalDate.of(2019, 1, 1));
                }
                try {
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                    if (subscriberSession != null) {
                        DAOFactory.getDefaultDAOFactory().getSubscriberDAO().updateSubscriberSession(subscriberSession);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    private void takSebe() {

        String csvFile = "BASES_KTVI/tak_sebe.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

//        Set<Integer> set = new HashSet<>();
        try {
            FileInputStream fis =  new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
            int size = -1;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);

                RenderedService renderedService = new RenderedService();
                renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
                renderedService.setDate(LocalDate.of(2018, 1, 2));
                renderedService.setSubscriberAccount(parseInt(row[0]));


                renderedService.setPrice(parseBigDecimal(row[1]));
//                if(!set.contains(parseInt(row[1]))) {
//                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
//                }
//                set.add(parseInt(row[1]));
                try {
                    DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

//
//    public void updateTariffsInSubscriberTariffs() {
//        String csvFile = "BASES_KTVI/history.CSV";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//
////        Set<Integer> set = new HashSet<>();
//        List<RenderedService> renderedServices = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO()
//                .getByService(FixedServices.DISCONNECTION.getId());
//
//        try {
//            FileInputStream fis = new FileInputStream(csvFile);
//            br = new BufferedReader(new InputStreamReader(fis, "Cp1251"));
//            int size = -1;
//            HashSet<Integer> hashSet = new HashSet();
//            while ((line = br.readLine()) != null) {
//                if (size == -1) {
//                    size++;
//                    continue;
//                }
//                String[] row = line.split(cvsSplitBy);
//
//                Integer subscriber_account = parseInt(row[1]);
//
//                if (row[0].equals("0") || subscriber_account == null || !row[10].equals("99")
//                        || row[8].equals("0") || row[8] == null)
//                    continue;
//
//                if (renderedServices.stream().filter(e -> e.getSubscriberAccount().equals(subscriber_account)).count() == 0) {
//                    continue;
//                }
////                hashSet.add(subscriber_account);
//                System.out.println(subscriber_account + "=" + row[8]);
//                SubscriberTariff subscriberTariff = DAOFactory.getDefaultDAOFactory().getSubscriberDAO()
//                        .getNotClosedSubscriberTariff(subscriber_account, LocalDate.of(2017, 1, 1));
//                subscriberTariff.setTariffId(parseInt(row[8]));
//                DAOFactory.getDefaultDAOFactory().getSubscriberDAO().updateSubscriberTariff(subscriberTariff);
////                RenderedService renderedService = new RenderedService();
////                renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
////                renderedService.setDate(LocalDate.of(parseInt(row[2]), parseInt(row[3]), 1));
////                renderedService.setSubscriberAccount(parseInt(row[1]));
////
////
////                renderedService.setPrice(parseInt(row[5]));
////                if(parseInt(row[2]) == 2004 && parseInt(row[3]) ==  3 && !row[4].equals("") && !row[4].equals(" ")) {
////                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
////                }
//////                if(!set.contains(parseInt(row[1]))) {
//////                    renderedService.setPrice(parseInt(row[5]) + parseInt(row[4]));
//////                }
//////                set.add(parseInt(row[1]));
////                DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().save(renderedService);
//                size++;
//            }
//            System.out.println(size + " subscriptionFees have been loaded");
//            System.out.println(renderedServices.size() + " renderedServices size");
//            System.out.println(hashSet.size() + " hashSet size");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("No subscriptionFees loaded.");
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void createFirstSessionAndSubscriberTariff() {
//        List<RenderedService> connections =  DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getByService(2);
//        for (RenderedService connection : connections) {
//            SubscriberSession subscriberSession = new SubscriberSession();
//            subscriberSession.setSubscriberAccount(connection.getSubscriberAccount());
//            subscriberSession.setConnectionDate(connection.getDate());
//
//        }
//    }
//
//    private Boolean parseBoolean(String element) {
//        return element.equals("Да");
//    }
//
//
    private LocalDate parseDate(String element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(element, formatter);
        return date;
    }
//
    private LocalDate parseDateHyphen(String element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate date = LocalDate.parse(element, formatter);
        return date;
    }
//
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

    private Double parseDouble(String str) {
        // char code 160 is a space char
        str = str.replaceAll("\u00A0", "");
        if (str.length() == 0) {
            return null;
        }

        Double number;
        try {
            number = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            number = null;
        }
        return number;
    }

    private BigDecimal parseBigDecimal(String str) {
        // char code 160 is a space char
        str = str.replaceAll("\u00A0", "");
        if (str.length() == 0) {
            return null;
        }

        BigDecimal number;
        try {
            number = new BigDecimal(str);
        } catch (NumberFormatException ex) {
            number = null;
        }
        return number;
    }

    private String getString(String str) {
        return ResourceBundles.getEntityBundle().getString(str);
    }

}
//
