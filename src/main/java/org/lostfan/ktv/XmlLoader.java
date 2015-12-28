package org.lostfan.ktv;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.domain.*;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XmlLoader {

    public static void main(String[] args) {
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());

        new XmlLoader().load();
        ConnectionManager.getManager().close();
    }

    public void load() {
        loadStreets();
        loadTariffs();
        loadTariffPrices();
        loadServices();
        loadServicePrices();
        loadDisconnectionReasons();
        loadSubscribers();
//        loadConnections();
//        loadDisconnections();
//        loadBalances();
    }

    private void loadStreets() {
        NodeList nodes = getNodes("xml/streets.xml", "Street");
        if (nodes == null) {
            System.out.println("No streets loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            Street street = new Street();
            street.setId(parseInt(element, "Id"));
            street.setName(element.getElementsByTagName("Name").item(0).getTextContent());
            DAOFactory.getDefaultDAOFactory().getStreetDAO().save(street);
        }
        System.out.println(nodes.getLength() + " streets have been loaded");
    }

    private void loadTariffs() {
        NodeList nodes = getNodes("xml/tariffs.xml", "Tariff");
        if (nodes == null) {
            System.out.println("No tariffs loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            Tariff tariff = new Tariff();
            tariff.setId(parseInt(element, "Id"));
            tariff.setName(parseString(element, "Name"));
            tariff.setDigital(parseBoolean(element, "Digital"));
            tariff.setChannels(parseString(element, "ChannelsNumber"));
            DAOFactory.getDefaultDAOFactory().getTariffDAO().save(tariff);
        }
        System.out.println(nodes.getLength() + " tariffs have been loaded");
    }

    private void loadTariffPrices() {
        NodeList nodes = getNodes("xml/tariff_price.xml", "TariffPrice");
        if (nodes == null) {
            System.out.println("No tariff prices loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            TariffPrice tariffPrice = new TariffPrice();
            // ID ??
            tariffPrice.setTariffId(parseInt(element, "Tariff"));
            tariffPrice.setPrice(parseInt(element, "Price"));
            tariffPrice.setDate(parseDate(element, "Date"));
            DAOFactory.getDefaultDAOFactory().getTariffDAO().saveTariffPrice(tariffPrice);
        }
        System.out.println(nodes.getLength() + " tariff prices have been loaded");
    }

    private void loadServices() {
        NodeList nodes = getNodes("xml/services.xml", "Service");
        if (nodes == null) {
            System.out.println("No services loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            Service service = new Service();
            service.setId(parseInt(element, "Id"));
            service.setName(element.getElementsByTagName("Name").item(0).getTextContent());
            // Set Refill
            service.setAdditionalService(parseBoolean(element, "AdditionalService"));
            DAOFactory.getDefaultDAOFactory().getServiceDAO().save(service);
        }
        System.out.println(nodes.getLength() + " services have been loaded");
    }

    private void loadServicePrices() {
        NodeList nodes = getNodes("xml/service_price.xml", "ServicePrice");
        if (nodes == null) {
            System.out.println("No service prices loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            ServicePrice servicePrice = new ServicePrice();
            // ID goes here
            servicePrice.setPrice(parseInt(element, "Price"));
            servicePrice.setServiceId(parseInt(element, "Service"));
            servicePrice.setDate(parseDate(element, "Date"));
            DAOFactory.getDefaultDAOFactory().getServiceDAO().savePrice(servicePrice);
        }
        System.out.println(nodes.getLength() + " service prices have been loaded");
    }

    private void loadSubscribers() {
        NodeList nodes = getNodes("xml/subscribers.xml", "Subscriber");
        if (nodes == null) {
            System.out.println("No subscribers loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            Subscriber subscriber = new Subscriber();
            subscriber.setAccount(parseInt(element, "Id"));
            subscriber.setName(parseString(element, "Name"));
            subscriber.setStreetId(parseInt(element, "Street"));
            subscriber.setBalance(0);
            subscriber.setHouse(parseString(element, "House"));
            subscriber.setBuilding(parseString(element, "Housing"));
            subscriber.setPostcode(parseString(element, "Index"));
            subscriber.setPhone(parseString(element, "Phone"));
            DAOFactory.getDefaultDAOFactory().getSubscriberDAO().save(subscriber);
        }
        System.out.println(nodes.getLength() + " subscribers have been loaded");
    }

    private void loadConnections() {
        // TODO: implement
    }

    private void loadDisconnectionReasons() {
        NodeList nodes = getNodes("xml/reasons_of_disconnect.xml", "ReasonOfDisconnect");
        if (nodes == null) {
            System.out.println("No disconnection reasons loaded.");
            return;
        }

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);

            DisconnectionReason disconnectionReason = new DisconnectionReason();
            disconnectionReason.setId(parseInt(element, "Id"));
            disconnectionReason.setName(parseString(element, "Name"));
            DAOFactory.getDefaultDAOFactory().getDisconnectionReasonDAO().save(disconnectionReason);
        }
        System.out.println(nodes.getLength() + " disconnection reasons have been loaded");
    }

    private void loadDisconnections() {
        // TODO: implement
    }

    private void loadBalances() {
        // TODO: implement
    }

    private NodeList getNodes(String fileName, String nodeName) {
        File xmlFile = new File(fileName);

        NodeList nodes = null;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            document.getDocumentElement().normalize();

            nodes = document.getDocumentElement().getElementsByTagName(nodeName);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return nodes;
    }

    private Boolean parseBoolean(Element element, String nodeName) {
        return element.getElementsByTagName(nodeName).item(0).getTextContent().equals("Да");
    }

    private String parseString(Element element, String nodeName) {
        return element.getElementsByTagName(nodeName).item(0).getTextContent();
    }

    private LocalDate parseDate(Element element, String nodeName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
        LocalDate date = LocalDate.parse(element.getElementsByTagName(nodeName).item(0).getTextContent(), formatter);
        return date;
    }

    private Integer parseInt(Element element, String nodeName) {
        String str = element.getElementsByTagName(nodeName).item(0).getTextContent();
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
