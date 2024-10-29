/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class DaemonsControl {

    public static void startStopDaemons() {

        //aqui obtengo el timer para saber como correrlo
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConfigJpaController configJpaController = new ConfigJpaController(manager);
        Config configAuto = configJpaController.findConfig(1);

        
        VariablesSession.timer.purge();
        
        long fechaFacturacion = configAuto.getAutopagosDaemonFechaFacturacion().longValue();
        if (configAuto.getAutopagosDaemonFacturacionActivo()) {
            //daemon de facturacion
            Calendar calendar = Calendar.getInstance();
            Calendar calendarFecha = Calendar.getInstance();
            calendarFecha.setTimeInMillis(fechaFacturacion);
            calendarFecha.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
            calendarFecha.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendarFecha.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            if (calendar.getTimeInMillis() > calendarFecha.getTimeInMillis()) {
                //aumentarle un dia
                calendarFecha.add(Calendar.DAY_OF_YEAR, 1);
            }
            System.out.println("activando daemon de facturacion");
            TimerTaskFacturacionSipar timerTaskFacturacionSipar = new TimerTaskFacturacionSipar(); 
            VariablesSession.timer.schedule(timerTaskFacturacionSipar, calendarFecha.getTime());
        } 
    }
}
