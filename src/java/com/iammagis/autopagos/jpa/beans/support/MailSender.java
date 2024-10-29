/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Factura;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class MailSender {

    static PropertiesAccess propertiesAcces = new PropertiesAccess();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    static DecimalFormat decimalFormat = new DecimalFormat("###.00");

    public static JSONObject sendMailNotification(Factura factura2, File file, int tipo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (factura2.getEmail() != null) {
            //envio correo con el cobro por ventanilla
            String template = CorreoZoho.getTemplate(CorreoZoho.solicitudPagoTemplate);
            template = template.replace("$nombre-convenio", factura2.getConveniosIdconvenios().getNombre());
            template = template.replace("$total", decimalFormat.format(factura2.getValor()));
            template = template.replace("$referencia", factura2.getReferencia());
            if (factura2.getFechaVencimiento() != null) {
                template = template.replace("$fecha-vencimiento", dateFormat.format(new Date(factura2.getFechaVencimiento().longValue())));
            } else {
                template = template.replace("$fecha-vencimiento", "N/A");
            }
            template = template.replace("$fecha", dateFormat.format(new Date(factura2.getFechaEmision().longValue())));
            template = template.replace("$server", propertiesAcces.SERVER);
            template = template.replace("$id", factura2.getIdfactura() + "");
            template = template.replace("$id", factura2.getIdfactura() + "");
            template = template.replace("$token", factura2.getConveniosIdconvenios().getCodigo());

            template = template.replace("$link", propertiesAcces.SERVER + "LoadPago.ap?token=" + factura2.getConveniosIdconvenios().getCodigo() + "&idfactura=" + factura2.getIdfactura());
            String[] emails = factura2.getEmail().split(",");
            ArrayList<String> strings = new ArrayList<>();
            for (String email : emails) {
                if (email.contains("@") && !email.equals("")) {
                    strings.add(email);
                }
            }
            if (tipo == 1) {
                template = template.replace("$title", "Tienes una nueva Factura por pagar");
                if (file != null) {
                    CorreoZoho correoZoho = new CorreoZoho("Nueva solicitud de pago", template, strings, file);
                    correoZoho.start();
                } else {
                    CorreoZoho correoZoho = new CorreoZoho("Nueva solicitud de pago", template, strings);
                    correoZoho.start();
                }
            } else {
                template = template.replace("$title", "Factura pendiente de pago");
                if (file != null) {
                    CorreoZoho correoZoho = new CorreoZoho("Recordatorio de pago", template, strings, file);
                    correoZoho.start();
                } else {
                    CorreoZoho correoZoho = new CorreoZoho("Recordatorio pago", template, strings);
                    correoZoho.start();
                }
            }
            jSONObject.put("msg", "La solicitud de pago ha sido enviada por correo electronico");
        }

        return jSONObject;
    }

}
