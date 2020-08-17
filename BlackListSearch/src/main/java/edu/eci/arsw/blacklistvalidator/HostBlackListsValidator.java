/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;

    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress,int numberThreads){

        CopyOnWriteArrayList<Integer> blackListOcurrences = new CopyOnWriteArrayList();
        
        AtomicInteger ocurrencesCount=new AtomicInteger(0);
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        AtomicInteger checkedListsCount=new AtomicInteger(0);

        BlackListThread[] threads = new BlackListThread[numberThreads];
        int start=0;
        int amount = skds.getRegisteredServersCount()/numberThreads;
        for(int i=0;i<numberThreads;i++){
            if(i==numberThreads-1){
                int extra = skds.getRegisteredServersCount()%numberThreads;
                threads[i]=new BlackListThread(start , start+amount+extra, ipaddress, ocurrencesCount, blackListOcurrences, skds, checkedListsCount);
            }
            else {
                threads[i]=new BlackListThread(start , start+amount, ipaddress, ocurrencesCount, blackListOcurrences, skds, checkedListsCount);
            }
            threads[i].start();
            start+=amount;

        }

        for(int i=0;i<numberThreads;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (ocurrencesCount.get() >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount.get(), skds.getRegisteredServersCount()});
        return blackListOcurrences;

    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
