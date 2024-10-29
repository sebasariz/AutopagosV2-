/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.acciones.pago.support.HttpAutomatizacionReaderBBVA;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class TimerTaskOnline extends TimerTask {

    long tiempoAutopagos = 900000;
    PropertiesAccess propertiesAccess = new PropertiesAccess();

    @Override
    public void run() {
        System.out.println("daemon online");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConfigJpaController configJpaController = new ConfigJpaController(manager);
        Config configAuto = configJpaController.findConfig(1);
        try {

            //Aqui tengo todo el espacio para verificar las transacciones de ONLINE
            //obtener comprobantes PSE de online que se enceuntren en estado pendiente
            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            ArrayList<ComprobanterecaudoPSE> comprobanterecaudoPSEs = comprobanterecaudoPSEJpaController.getComprobantesPendientesOnline();
            //agregar los comprobantes que son de autopagos 
            comprobanterecaudoPSEs.addAll(comprobanterecaudoPSEJpaController.getComprobantesAutopagosPendintes());
            if (comprobanterecaudoPSEs.size() > 0) {
                HttpAutomatizacionReaderBBVA httpAutomatizacionReader = new HttpAutomatizacionReaderBBVA(configAuto.getAutopagosDaemonUsuario(), configAuto.getAutopagosDaemonPass());
                httpAutomatizacionReader.initGetToken();
                String retorno = httpAutomatizacionReader.initLogin();
                if (!retorno.contains("En este momento se encuentra un")) {
                    httpAutomatizacionReader.getData();
                    httpAutomatizacionReader.getTable();
                    for (ComprobanterecaudoPSE comprobanterecaudoPSE : comprobanterecaudoPSEs) {
                        int estado = httpAutomatizacionReader.getBusquedaId(comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "");
                        if (4 == comprobanterecaudoPSE.getEstadoIdestado().getIdestado() || 3 == comprobanterecaudoPSE.getEstadoIdestado().getIdestado()) {
                            ChangeComprobantePSE.cambioEstado(comprobanterecaudoPSE, estado, "");
                        }
                    }
                    httpAutomatizacionReader.salidaSegura();
                } else {
                    System.out.println("problema con el cierre de sesion enviar correo inmediatamente");
                }
            }
//          

        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<String> strings = new ArrayList<>();
            strings.add(configAuto.getAutopagosEmailSoporte());
            String templateError = CorreoZoho.getTemplate(CorreoZoho.errorTemplate);
            templateError = templateError.replace("$server", propertiesAccess.SERVER);
            templateError = templateError.replace("$fecha", new Date().toString());
            templateError = templateError.replace("$error", e.getMessage() + " " + e.getLocalizedMessage());
            CorreoZoho correoZoho = new CorreoZoho("Error Daemon facturacion ONLINE", templateError, strings);
            correoZoho.start();
        }
        if (configAuto.getAutopaogsDaemon()) {
            //daemon online
            TimerTaskOnline timerTaskOnline = new TimerTaskOnline();
            VariablesSession.timer.schedule(timerTaskOnline, tiempoAutopagos);
            VariablesSession.timer.purge();

        }
    }

}
