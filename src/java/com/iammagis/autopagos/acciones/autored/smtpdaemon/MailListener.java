/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.autored.smtpdaemon;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.support.ChangeComprobantePSE;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONException;
import org.json.JSONObject;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
 *
 * @author Usuario
 */
public class MailListener implements SimpleMessageListener {

    private final MailSaver saver;
    private final PropertiesAccess propertiesAccess = new PropertiesAccess();

    public MailListener(MailSaver saver) {
        this.saver = saver;
    }

    @Override
    public boolean accept(String from, String recipient) {
        return true;
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) throws IOException {
        try {
            String dataReceive = saver.saveEmailAndNotify(from, recipient, data);
            System.out.println("from");
            System.out.println("recipient: " + recipient);
            System.out.println("dataReceive: " + dataReceive);
            dataReceive = dataReceive.replaceAll(" ", "");
            if (recipient.equals(propertiesAccess.CORREOAVVILLAS)) {
                System.out.println("Entrando por AVVillas");
                System.out.println("dataReceive: " + dataReceive);
                System.out.println("entre por avvillas");
                System.out.println("dataReceive.split(\"<strong>n=C3=BAmero<span>\").length: " + dataReceive.split("<strong>n=C3=BAmero<span>").length);
                System.out.println("is: " + dataReceive != null);
                if (dataReceive != null && dataReceive.split("<strong>n=C3=BAmero<span>").length > 1) {
                    System.out.println("numero");
                    String numeroTransaccion = dataReceive.split("<strong>n=C3=BAmero<span>")[1].split("</span></strong>")[0];
                    System.out.println("numeroTransaccion: " + numeroTransaccion);

                    if (!numeroTransaccion.equals("")) {
                        //aqui vamos a organizar la accion al recibir el mail
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("aprobacion", numeroTransaccion);
                        jSONObject.put("estado", 1);
                        if (dataReceive.toUpperCase().contains("RECHAZADA")) {
                            System.out.println("rechazada");
                            jSONObject.put("estado", 3);
                        } else if (dataReceive.toUpperCase().contains("APROBADA")) {
                            System.out.println("aprobada");
                            jSONObject.put("estado", 2);
                        } else {
                            System.out.println("no encontre nada =======");
                        }
//                    System.out.println("jSONObject: " + jSONObject);
                        //cambiamos el estado de la transaccion
                        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
                        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
                        ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findByIdAvvillas(numeroTransaccion);
                        if (comprobanterecaudoPSE != null && (comprobanterecaudoPSE.getEstadoIdestado().getIdestado() == 4
                                || comprobanterecaudoPSE.getEstadoIdestado().getIdestado() == 3)) {
                            //puedo seguir trabajando y cambiar lso estados
//                            ChangeComprobantePSE.cambioEstadoAutored(comprobanterecaudoPSE, jSONObject, 0 + "");
                            System.out.println("entrando por cambio de estado autored avvillas");
                            ChangeComprobantePSE.cambioEstadoAutored(comprobanterecaudoPSE, jSONObject);
                            //cambio de estado de facturas de acuerdo al estado
                        }
                    }
                }
            } else if (recipient.equals(propertiesAccess.CORREOBANCOLOMBIA)) {
                dataReceive = dataReceive.replaceAll(" ", "").replaceAll("	", "").replaceAll("	", "").replace("=\n", "");
//                System.out.println("dataReceive: " + dataReceive);
                System.out.println("Entrando por BANCOLOMBIA");
                dataReceive = dataReceive.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "").replaceAll(" ", "").replaceAll("=", "");
                String numeroTransaccion = "";
                String cus = "";
                if (dataReceive.contains("CUS:")) {
                    cus = dataReceive.split("CUS:</td>0D0A<tdbgcolor3D\"#dedede\"class3D\"font\">0D0A")[1].split("</td>")[0];
                }
                if (dataReceive.contains("Concepto:")) {
                    numeroTransaccion = dataReceive.split("AConcepto:</td>0D0A<tdclass3D\"font\">0D0A")[1]
                            .split("_")[0].replaceAll("[^\\d.]", "");
                    if (numeroTransaccion.contains("_")) {
                        numeroTransaccion = numeroTransaccion.split("_")[0];
                    }
                    System.out.println("idReferencia: " + numeroTransaccion);
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("aprobacion", cus);
                if (dataReceive.contains("probada")) {
                    System.out.println("aprobada");
                    jSONObject.put("estado", 2);
                } else {
                    System.out.println("rechazada");
                    jSONObject.put("estado", 3);
                }
//                    System.out.println("jSONObject: " + jSONObject);
                //cambiamos el estado de la transaccion

                EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
                ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
                try {
                    ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(Integer.parseInt(numeroTransaccion));
                    if (comprobanterecaudoPSE != null) {
                        //puedo seguir trabajando y cambiar lso estados
//                        ChangeComprobantePSE.cambioEstado(comprobanterecaudoPSE, jSONObject.getInt("estado"), jSONObject.getString("cus"));
                        ChangeComprobantePSE.cambioEstadoAutored(comprobanterecaudoPSE, jSONObject);
                        //cambio de estado de facturas de acuerdo al estado
                    }
                } catch (NumberFormatException e) {

                }

            } else if (recipient.equals(propertiesAccess.CORREOBBVA)) {

            }
        } catch (JSONException ex) {
            Logger.getLogger(MailListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MailListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws JSONException, Exception {
        JSONObject jSONObject = new JSONObject();
        String numeroTransaccion = "5742";
        jSONObject.put("estado", 3);
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
        ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(Integer.parseInt(numeroTransaccion));
        System.out.println("comprobanterecaudoPSE: " + comprobanterecaudoPSE);
        if (comprobanterecaudoPSE != null) {
            System.out.println("entrando al comrpobante");
            //puedo seguir trabajando y cambiar lso estados
            ChangeComprobantePSE.cambioEstado(comprobanterecaudoPSE, jSONObject.getInt("estado"), 0 + "");
            //cambio de estado de facturas de acuerdo al estado
        }
    }
}
