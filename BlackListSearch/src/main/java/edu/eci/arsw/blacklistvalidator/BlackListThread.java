package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackListThread extends Thread {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int start,final_server;
    private String ipAddress;
    private AtomicInteger ocurrencesCount;
    private AtomicInteger checkedListsCount;
    private HostBlacklistsDataSourceFacade skds;
    private CopyOnWriteArrayList<Integer> blackListOcurrence;

    public BlackListThread(int start, int final_server, String ipAddress, AtomicInteger ocurrencesCount, CopyOnWriteArrayList<Integer> blackListOcurrence, HostBlacklistsDataSourceFacade skds, AtomicInteger checkedListsCount) {
        this.start = start;
        this.final_server = final_server;
        this.ipAddress = ipAddress;
        this.ocurrencesCount = ocurrencesCount;
        this.blackListOcurrence = blackListOcurrence;
        this.checkedListsCount = checkedListsCount;
        this.skds = skds;

    }

    @Override
    public void run() {
        for (int i = start; i < final_server && ocurrencesCount.get() < BLACK_LIST_ALARM_COUNT; i++) {
            checkedListsCount.getAndIncrement();
            if (skds.isInBlackListServer(i, ipAddress)) {
                blackListOcurrence.add(i);
                ocurrencesCount.getAndIncrement();
            }
        }
    }

}




