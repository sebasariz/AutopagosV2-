/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import java.util.Timer;

/**
 *
 * @author Usuario
 */
public class VariablesSession {

    static public Timer timer=new Timer();  
    static public String context;
    
     
  
    public static String getContext() {
        return context;
    }

    public static void setContext(String context) {
        VariablesSession.context = context;
    }

    
     
 
}
