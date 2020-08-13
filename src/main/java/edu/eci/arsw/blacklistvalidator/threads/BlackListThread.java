package edu.eci.arsw.blacklistvalidator.threads;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;

public class BlackListThread extends Thread {
    private static final int BLACK_LIST_ALARM_COUNT = 3;
    private int ocurrenceCount, initial_server, final_server;
    HostBlacklistsDataSourceFacade facade =HostBlacklistsDataSourceFacade.getInstance();
    private String ipAddress;
    private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    public BlackListThread(int initial_server, int final_server, String ipAddress) {
        this.initial_server = initial_server;
        this.final_server = final_server;
        this.ipAddress = ipAddress;
    }


    public static int getBlackListAlarmCount() {
        return BLACK_LIST_ALARM_COUNT;
    }

    public int getOcurrenceCount() {
        return ocurrenceCount;
    }

    public void setOcurrenceCount(int ocurrenceCount) {
        this.ocurrenceCount = ocurrenceCount;
    }

    public int getInitial_server() {
        return initial_server;
    }

    public void setInitial_server(int initial_server) {
        this.initial_server = initial_server;
    }

    public int getFinal_server() {
        return final_server;
    }

    public void setFinal_server(int final_server) {
        this.final_server = final_server;
    }

    public HostBlacklistsDataSourceFacade getFacade() {
        return facade;
    }

    public void setFacade(HostBlacklistsDataSourceFacade facade) {
        this.facade = facade;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }

    public void setBlackListOcurrences(LinkedList<Integer> blackListOcurrences) {
        this.blackListOcurrences = blackListOcurrences;
    }


    public void run() {
        for (int i = initial_server; i < final_server && ocurrenceCount<BLACK_LIST_ALARM_COUNT; i++) {
            if (facade.isInBlackListServer(i,ipAddress)){
                blackListOcurrences.add(i);
                System.out.println("server"+i);
                ocurrenceCount++;
            }
        }
        if (ocurrenceCount>=BLACK_LIST_ALARM_COUNT){
            facade.reportAsNotTrustworthy(ipAddress);
        }
        else{
            facade.reportAsTrustworthy(ipAddress);
        }


        //LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, facade.getRegisteredServersCount()});

    }
}
