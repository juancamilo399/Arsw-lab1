/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

import static java.lang.Runtime.getRuntime;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        long startTime = System.currentTimeMillis();
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        List<Integer> blackListOcurrences=hblv.checkHost("202.24.34.55", 50);
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        
    }
    
}