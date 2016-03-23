package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;
import org.lostfan.ktv.model.entity.BaseModel;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.TurnoverReportExcel;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TurnoverReportModel extends BaseObservable implements BaseModel {

    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private StreetDAO streetDAO = DAOFactory.getDefaultDAOFactory().getStreetDAO();
    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();

    public TurnoverReportModel() {
    }

    @Override
    public String getEntityNameKey() {
        return "turnoverSheet";
    }

    @Override
    public List<EntityField> getFields() {
        return new ArrayList<>();
    }

    public Service getService(Integer id) {
        return serviceDAO.get(id);
    }

    public List<Service> getAllServices() {
        return  serviceDAO.getAll();
    }

    public List<TurnoverSheetTableDTO> getTurnoverSheetDataByAdditionalServices(LocalDate date) {
        List<Service> services = getAllServices().stream().filter(e -> e.isAdditionalService()).collect(Collectors.toList());
        List<TurnoverSheetTableDTO> turnoverSheetTableDTOs = new ArrayList<>();
        Integer count = 1;
        for (Service service : services) {
            notifyObservers(100 * count++ / services.size());
            turnoverSheetTableDTOs.addAll(getTurnoverSheetData(date, service.getId()));
        }
        notifyObservers(100);
        return turnoverSheetTableDTOs;
    }


    public List<TurnoverSheetTableDTO> getTurnoverSheetData(LocalDate date, Integer serviceId) {
        List<TurnoverSheetTableDTO> turnoverSheetTableDTOs;
        final boolean isAddAllNullUsers;
        if(serviceId == FixedServices.SUBSCRIPTION_FEE.getId()) {
            isAddAllNullUsers = true;
        } else {
            isAddAllNullUsers = false;
        }

        Map<Integer, TurnoverSheetTableDTO> tableDTOHashMap = new HashMap<>();
        Map<Integer, Integer> beginPeriodDebit = renderedServiceDAO.getAllRenderedServicesPriceForSubscriberByServiceIdBeforeDate(serviceId, date);
        beginPeriodDebit.forEach((k, v) -> {
                    TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                    turnoverSheetTableDTO.setSubscriberAccount(k);
                    turnoverSheetTableDTO.setServiceId(serviceId);
                    if(v > 0) {
                        turnoverSheetTableDTO.setBroughtForwardBalanceDebit(v);
                    } else {
//                        turnoverSheetTableDTO.setBroughtForwardBalanceDebit(-1* v);
                    }
                    tableDTOHashMap.put(k, turnoverSheetTableDTO);
                }
        );
        Map<Integer, Integer> beginPeriodCredit = paymentDAO.getAllPaymentsPriceForSubscriberToDate(serviceId, date);
        beginPeriodCredit.forEach((k, v) -> {
            if (tableDTOHashMap.containsKey(k)) {
                Integer broughtForwardBalanceDebit = tableDTOHashMap.get(k).getBroughtForwardBalanceDebit();
                if(broughtForwardBalanceDebit <= v) {
                    tableDTOHashMap.get(k).setBroughtForwardBalanceDebit(0);
                    tableDTOHashMap.get(k).setBroughtForwardBalanceCredit(v - broughtForwardBalanceDebit);
                } else {
                    tableDTOHashMap.get(k).setBroughtForwardBalanceDebit(broughtForwardBalanceDebit - v);
                }
            } else {
                TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                turnoverSheetTableDTO.setSubscriberAccount(k);
                turnoverSheetTableDTO.setServiceId(serviceId);
                if(v > 0) {
                    turnoverSheetTableDTO.setBroughtForwardBalanceCredit(v);
                } else {
                    turnoverSheetTableDTO.setBroughtForwardBalanceDebit(-1* v);
                }
                tableDTOHashMap.put(k, turnoverSheetTableDTO);
            }
                }
        );
        Map<Integer,Integer> allRenderedServicesPriceInMonthForSubscriberByServiceId = renderedServiceDAO.getAllRenderedServicesPriceInMonthForSubscriberByServiceId(serviceId, date);
        allRenderedServicesPriceInMonthForSubscriberByServiceId.forEach((k, v) -> {
            if (tableDTOHashMap.containsKey(k)) {
                tableDTOHashMap.get(k).setTurnoverBalanceDebit(v);
            } else {
                TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                turnoverSheetTableDTO.setSubscriberAccount(k);
                turnoverSheetTableDTO.setServiceId(serviceId);
                turnoverSheetTableDTO.setTurnoverBalanceDebit(v);
                tableDTOHashMap.put(k, turnoverSheetTableDTO);
            }
        });
        Map<Integer,Integer> allPaymentsPriceInMonthForSubscriberByServiceId = paymentDAO.getAllPaymentsPriceInMonthForSubscriberByServiceId(serviceId, date);
        allPaymentsPriceInMonthForSubscriberByServiceId.forEach((k, v) -> {
            if (tableDTOHashMap.containsKey(k)) {
                tableDTOHashMap.get(k).setTurnoverBalanceCredit(v);
            } else {
                TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                turnoverSheetTableDTO.setSubscriberAccount(k);
                turnoverSheetTableDTO.setServiceId(serviceId);
                turnoverSheetTableDTO.setTurnoverBalanceCredit(v);
                tableDTOHashMap.put(k, turnoverSheetTableDTO);
            }
        });
        Map<Integer, Integer> endPeriodDebit = renderedServiceDAO.getAllRenderedServicesPriceForSubscriberByServiceIdBeforeDate(serviceId, date.plusMonths(1));
        endPeriodDebit.forEach((k, v) -> {
            if (tableDTOHashMap.containsKey(k)) {
                tableDTOHashMap.get(k).setCarriedForwardBalanceDebit(v);
            } else {
                TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                turnoverSheetTableDTO.setSubscriberAccount(k);
                turnoverSheetTableDTO.setServiceId(serviceId);
                turnoverSheetTableDTO.setCarriedForwardBalanceDebit(v);
                tableDTOHashMap.put(k, turnoverSheetTableDTO);
            }
                }
        );
        Map<Integer, Integer> endPeriodCredit = paymentDAO.getAllPaymentsPriceForSubscriberToDate(serviceId, date.plusMonths(1));
        endPeriodCredit.forEach((k, v) -> {
                    TurnoverSheetTableDTO dto = tableDTOHashMap.get(k);
                    if (dto != null) {
                        Integer carriedForwardBalanceDebit = dto.getCarriedForwardBalanceDebit();
                        if(carriedForwardBalanceDebit <= v) {
                            dto.setCarriedForwardBalanceDebit(0);
                            dto.setCarriedForwardBalanceCredit(v - carriedForwardBalanceDebit);
                        } else {
                            dto.setCarriedForwardBalanceDebit(carriedForwardBalanceDebit - v);
                        }
                    } else {
                        TurnoverSheetTableDTO turnoverSheetTableDTO = new TurnoverSheetTableDTO();
                        turnoverSheetTableDTO.setSubscriberAccount(k);
                        turnoverSheetTableDTO.setServiceId(serviceId);
                        turnoverSheetTableDTO.setCarriedForwardBalanceCredit(v);
                        tableDTOHashMap.put(k, turnoverSheetTableDTO);
                    }

                }
        );

        turnoverSheetTableDTOs = tableDTOHashMap.values().stream().sorted((dto1, dto2) -> dto1.getSubscriberAccount() - dto2.getSubscriberAccount())
                .filter(e -> isAddAllNullUsers
                        ||  e.getBroughtForwardBalanceDebit() - e.getBroughtForwardBalanceCredit() != 0
                        ||  e.getTurnoverBalanceDebit() != 0
                        ||  e.getTurnoverBalanceCredit() != 0
                        ||  e.getCarriedForwardBalanceDebit() - e.getCarriedForwardBalanceCredit() != 0 )
                .collect(Collectors.toList());

        for (TurnoverSheetTableDTO turnoverSheetTableDTO : turnoverSheetTableDTOs) {
            turnoverSheetTableDTO.setSubscriber(subscriberDAO.get(turnoverSheetTableDTO.getSubscriberAccount()));
            if(turnoverSheetTableDTO.getSubscriber() != null) {
                turnoverSheetTableDTO.setSubscriberStreet(streetDAO.get(turnoverSheetTableDTO.getSubscriber().getStreetId()));
            }
        }

        for (TurnoverSheetTableDTO turnoverSheetTableDTO : turnoverSheetTableDTOs) {
            Integer carriedForwardBalance =
                    turnoverSheetTableDTO.getBroughtForwardBalanceDebit() - turnoverSheetTableDTO.getBroughtForwardBalanceCredit()
                    + turnoverSheetTableDTO.getTurnoverBalanceDebit() - turnoverSheetTableDTO.getTurnoverBalanceCredit();
            if(carriedForwardBalance != turnoverSheetTableDTO.getCarriedForwardBalanceDebit() - turnoverSheetTableDTO.getCarriedForwardBalanceCredit()) {
                System.out.println(turnoverSheetTableDTO.getSubscriberAccount());
            }
//            if (carriedForwardBalance >= 0) {
//                turnoverSheetTableDTO.setCarriedForwardBalanceDebit(carriedForwardBalance);
//            } else {
//                turnoverSheetTableDTO.setCarriedForwardBalanceCredit(-1 * carriedForwardBalance);
//            }
        }
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getBroughtForwardBalanceCredit()).sum());
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getBroughtForwardBalanceDebit()).sum());
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getTurnoverBalanceCredit()).sum());
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getTurnoverBalanceDebit()).sum());
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getCarriedForwardBalanceCredit()).sum());
        System.out.println(turnoverSheetTableDTOs.stream().mapToInt(i -> i.getCarriedForwardBalanceDebit()).sum());
        return turnoverSheetTableDTOs;
    }

    public String generateExcelReport(Boolean isAdditional,
                                      Integer serviceId, LocalDate date) {
        TurnoverReportExcel turnoverReportExcel = new TurnoverReportExcel(this);
        return turnoverReportExcel.generate(isAdditional, serviceId, date);
    }

}
