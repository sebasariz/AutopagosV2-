/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.acciones.autored.smtpdaemon.SMTPServerAutopagos;
import java.text.SimpleDateFormat;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author sebastianarizmendy
 */
public class TimmerInitContextListener implements ServletContextListener {
    PropertiesAccess propertiesAcces = new PropertiesAccess();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    SMTPServerAutopagos sMTPServerAutopagos = new SMTPServerAutopagos();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (VariablesSession.context == null) {
            VariablesSession.context = sce.getServletContext().getRealPath("");
        }
        sMTPServerAutopagos.startServer(25, null);
        DaemonsControl.startStopDaemons();
        System.out.println("===============START DEAMOS====================START SMTP ==================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sMTPServerAutopagos.stopServer();
        System.out.println("==============STOP SMTP===========================");
    }

}
