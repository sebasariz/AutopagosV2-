/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class TimerTaskSipar extends TimerTask {

    PropertiesAccess propertiesAccess = new PropertiesAccess();
    ComprobanterecaudoPSE comprobanterecaudoPSE;

    public TimerTaskSipar(ComprobanterecaudoPSE comprobanterecaudoPSE) {
        this.comprobanterecaudoPSE = comprobanterecaudoPSE;
    }

    @Override
    public void run() {
        System.out.println("daemon sipar");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
        comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
        if (comprobanterecaudoPSE.getEstadoIdestado().getIdestado() == 4) {
            try {
                System.out.println("cambio de estado");
                ArrayList<String> strings = new ArrayList<>();
                strings.add("alejandro.gomez@autopagos.co");
                strings.add("sebasariz@autopagos.co");

                CorreoZoho correo = new CorreoZoho("Cambio de estado", "cambio de estado comprobante: " + comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE(), strings);
                correo.start();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("estado", 3);
                ChangeComprobantePSE.cambioEstadoAutored(comprobanterecaudoPSE, jSONObject);
            } catch (Exception ex) {
                Logger.getLogger(TimerTaskSipar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
