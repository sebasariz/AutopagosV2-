/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Dispositivo;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import static com.iammagis.autopagos.jpa.beans.support.VariablesSession.context;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.DispositivoJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.PushNotificationPayload;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class TimerTaskFacturacionSipar extends TimerTask {

    static PropertiesAccess propertiesAccess = new PropertiesAccess();
    static NumberFormat numberFormat = new DecimalFormat("###,###.00");
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    static EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
    static FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
    static DispositivoJpaController dispositivoJpaController = new DispositivoJpaController(manager);
    static Sender sender = new Sender(propertiesAccess.KEYGOOGLE);

//                emails.add(propertiesAccess.CORREO);
    @Override
    public void run() {

        System.out.println("daemon facturacion sipar date: " + new Date() + " =====");

        ConfigJpaController configJpaController = new ConfigJpaController(manager);
        Config configAuto = configJpaController.findConfig(1);
        Calendar calendar = Calendar.getInstance();
        try {
            //aqui tengo todo el espacio para generar la facturacion de SIPAR
            //analizo los convenios que sean de tipo SIPAR
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            ArrayList<Convenios> convenioses = conveniosJpaController.getConveniosByTipo(1);
            for (Convenios convenios : convenioses) {
                ArrayList<String> emails = new ArrayList<>();
                ArrayList<Usuario> usuarios = new ArrayList<>(convenios.getUsuarioCollection());
                for (Usuario usuario : usuarios) {
                    emails.add(usuario.getEmail());
                }
                System.out.println("emails: " + emails);
                //verificamos si la fecha le corresponde 
                Calendar calendarConvenio = Calendar.getInstance();
                calendarConvenio.setTimeInMillis(convenios.getFechaCortePlanPost().longValue());

                //crear factura de ser necesario
//                System.out.println("calendar.get(Calendar.DAY_OF_MONTH): " + calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendarConvenio.get(Calendar.DAY_OF_MONTH));
                if (calendar.get(Calendar.DAY_OF_MONTH) == calendarConvenio.get(Calendar.DAY_OF_MONTH)) {
                    //es el mismo dia de la creacion de la facturacion 
                    if (convenios.getActivo()) {
                        FacturaCreator.generarFacturaAutopagos(convenios, emails);
                    }
                } else {
                    //recordar si existen facturas pendientes o realizar el cierre si la factura esta vencida
                    ArrayList<FacturaAutopagos> facturaAutopagoses = new ArrayList<>(convenios.getFacturaAutopagosCollection());
                    if (!facturaAutopagoses.isEmpty()) {
                        FacturaAutopagos facturaAutopagos = facturaAutopagoses.get(facturaAutopagoses.size() - 1);
                        //verificar si la facura ya esta paga o no se enceuntra paga 
                        if (facturaAutopagos.getEstadoIdestado().getIdestado() != 2) {
                            Calendar calendarRecordatorioCierre = Calendar.getInstance();
                            calendarRecordatorioCierre.setTimeInMillis(convenios.getFechaCortePlanPost().longValue());
                            calendarRecordatorioCierre.add(Calendar.DAY_OF_YEAR, 8);

                            if (calendar.get(Calendar.DAY_OF_MONTH) == calendarRecordatorioCierre.get(Calendar.DAY_OF_MONTH)) {
                                //se encuentra suspender el servicio
                                FacturaCreator.suspenderFactura(facturaAutopagos, emails);

                            } else {
                                //enviar el recordatorio de factura pendiente de pago
                                FacturaCreator.recordarFactura(facturaAutopagos, emails);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<String> strings = new ArrayList<>();
            strings.add(configAuto.getAutopagosEmailSoporte());
            String templateError = CorreoZoho.getTemplate(CorreoZoho.errorTemplate);
            templateError = templateError.replace("$server", propertiesAccess.SERVER);
            templateError = templateError.replace("$fecha", new Date().toString());
            templateError = templateError.replace("$error", e.getMessage() + " " + e.getLocalizedMessage());
            CorreoZoho correoZoho = new CorreoZoho("Error Daemon facturacion SIPAR", templateError, strings);
            correoZoho.start();
        }
        //aqui vamos a verificar las facturas que existen nuevas para enviar el push y renovarla
        //obtenemos las facturas que no tienen fecha de emision, pues nunca se han encontrado, y lleva 1 mes o mas sin buscarse
        //o las que su fecha de emision es la de hoy
        //o las que su fecha de emision ya paso y no se ha podido actualizar

        //tengo las facturas iniciales 
        Calendar calendarBusqueda = Calendar.getInstance();
        Calendar calendarFechaEmision = Calendar.getInstance();
        calendarBusqueda.add(Calendar.DAY_OF_YEAR, -9);
        ArrayList<Factura> facturas = facturaJpaController.getfacturasSinPrimeraBusqueda(calendarBusqueda.getTimeInMillis());///cambiar fecha de creacion por hoy
        System.out.println("==================== FACTURAS NUEVAS ====================== " + facturas.size());

        //tengo las facturas que fecha de emision es hoy
        calendarFechaEmision.add(Calendar.MONTH, -1);
        calendarFechaEmision.add(Calendar.DAY_OF_YEAR, +3);
        ArrayList<Factura> facturasRenovacion = facturaJpaController.getFacturasRenovacion(calendarFechaEmision.getTimeInMillis());
        facturas.addAll(facturasRenovacion);
        System.out.println("==================== FACTURAS REMEMBER ====================== " + facturasRenovacion.size());
//        for (Factura factura : facturasRenovacion) {
        searchPush(facturas);
//        new Thread(new SearchSendPush(facturas)).start();

        long fechaFacturacion = configAuto.getAutopagosDaemonFechaFacturacion().longValue();
        if (configAuto.getAutopagosDaemonFacturacionActivo()) {
            //daemon de facturacion
            Calendar calendarOriginal = Calendar.getInstance();
            calendarOriginal.setTimeInMillis(fechaFacturacion);
            Calendar calendarFecha = Calendar.getInstance();
            calendarFecha.set(Calendar.HOUR, calendarOriginal.get(Calendar.HOUR));
            calendarFecha.set(Calendar.MINUTE, calendarOriginal.get(Calendar.MINUTE));
            calendarFecha.add(Calendar.DAY_OF_YEAR, 1);
            TimerTaskFacturacionSipar timerTaskFacturacionSipar = new TimerTaskFacturacionSipar();
            VariablesSession.timer.schedule(timerTaskFacturacionSipar, calendarFecha.getTime());
            VariablesSession.timer.purge();
        }

    }

//    private class SearchSendPush implements Runnable {
//        ArrayList<Factura> facturas;
//
//        public SearchSendPush(ArrayList<Factura> facturas) {
//            this.facturas = facturas;
//        }
//        @Override
//        public void run() {
    private void searchPush(ArrayList<Factura> facturas) {
        String facturasValidadas = "<table>";
        facturasValidadas += "<tr><td>Factura</td><td>Referencia</td><td>Convenio</td><td>Valor</td><td>Respuesta</td></tr>";
        for (Factura factura : facturas) {
            try {
                //buscamos la factura en winred    
//                    JSONObject jSONObjectRespuesta = new JSONObject();
//                    jSONObjectRespuesta.put("codigo", "00");
//                    jSONObjectRespuesta.put("estado", true);
//                    jSONObjectRespuesta.put("valor", 123123);
//                JSONObject jSONObjectRespuesta = FullCargaAutopagosServices.getEstado(factura.getConveniosIdconvenios(), factura.getReferencia());
//                ArrayList<String> tokensAndroid = new ArrayList<>();
//                ArrayList<String> tokensIOS = new ArrayList<>();
//                System.out.println("Factura nueva: " + factura.getNombre() + "Ref: " + factura.getReferencia() + " convenio: " + factura.getConveniosIdconvenios().getNombre() + " jSONObjectRespuesta: " + jSONObjectRespuesta);
//                facturasValidadas += "<tr><td>" + factura.getNombre() + "</td><td>" + factura.getReferencia() + "</td><td>" + factura.getConveniosIdconvenios().getNombre() + "</td><td>" + factura.getValor() + "</td><td>" + jSONObjectRespuesta + "</td></tr>";
//                if (jSONObjectRespuesta.has("estado") && jSONObjectRespuesta.has("codigo") && jSONObjectRespuesta.getBoolean("estado") && jSONObjectRespuesta.getString("codigo").equals("00") && jSONObjectRespuesta.getDouble("valor") != 0) {
//                    factura.setFechaEmision(BigInteger.valueOf(System.currentTimeMillis()));
//                    factura.setValor(jSONObjectRespuesta.getDouble("valor"));
//                    factura.setEstadoIdestado(new Estado(1));
//                    factura = facturaJpaController.edit(factura);
//                    //enviamos el push
//                    ArrayList<Dispositivo> dispositivos = new ArrayList<>();
//                    ArrayList<Usuario> usuarios = new ArrayList<>(factura.getUsuarioCollection());
//                    for (Usuario usuario : usuarios) {
//                        dispositivos = new ArrayList<>(usuario.getDispositivoCollection());
//                        for (Dispositivo dispositivo : dispositivos) {
//                            System.out.println("enviando a dispositivo: " + dispositivo.getToken());
//                            if (dispositivo.getTipo() == 1) {
//                                tokensAndroid.add(dispositivo.getToken());
//                            } else if (dispositivo.getTipo() == 1) {
//                                tokensIOS.add(dispositivo.getToken());
//                            }
//                        }
//                    }
//                    //enviamos a android
//                    String mensaje = "valor de $" + numberFormat.format(factura.getValor()) + " , se encuentra disponible.";
//                    if (mensaje.length() > 1000) {
//                        mensaje = mensaje.substring(0, 1000) + "[...]";
//                    }
//                    if (!tokensAndroid.isEmpty()) {
//
//                        Message.Builder builder = new Message.Builder();
//                        builder.addData("title", "Tu factura: " + factura.getNombre());
//                        builder.addData("message", mensaje);
//                        Message message = builder.build();
//                        MulticastResult multicastResult = sender.sendNoRetry(message, tokensAndroid);
//                        ArrayList<Result> results = new ArrayList<>(multicastResult.getResults());
//                        for (int i = 0; i < results.size(); i++) {
//                            Result result = results.get(i);
//                            if (result.getErrorCodeName() != null) {
//                                dispositivoJpaController.destroy(dispositivos.get(i).getIddispositivo());
//                            }
//                        }
//
//                    }

                    //enviando a los dispositivos IOS
//                    if (!tokensIOS.isEmpty()) {
//                        //aqui enviamos a los ios la notificacion
//                        //E36E69763FA72DFB1F45FA3CC1B0BF62F1A7CE6D7D0290B6B67FE6094C9A920A
//                        PushNotificationPayload payload = PushNotificationPayload.complex();
//                        payload.addAlert("Tu factura: " + factura.getNombre() + " por " + mensaje);
//                        payload.addBadge(1);
//                        /* Push your custom payload */
//                        ArrayList<Device> devices = new ArrayList<>();
//                        for (String token : tokensIOS) {
//                            Device device = new BasicDevice(token);
//                            devices.add(device);
//                        }
//
//                        Push.payload(payload, context + "/tmp/autopagos.p12", "sanignacio", true, devices);
//                    }
//                } else if (jSONObjectRespuesta.has("codigo") && jSONObjectRespuesta.getString("codigo").equals("02")
//                        && jSONObjectRespuesta.getString("msg").contains("FACTURA NO EXISTE")) {
//                    //si la fecha de emision es superior a 60 dias eliminela
////                    Calendar calendarActual = Calendar.getInstance();
////                    calendarActual.add(Calendar.MONTH, -2);
//                    TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
////                    if ((factura.getFechaEmision() != null && factura.getFechaEmision().longValue() < calendarActual.getTimeInMillis())
////                            || factura.getFechaCreacion().longValue() < calendarActual.getTimeInMillis()) {
//                    //eliminamos la factura
//                    ArrayList<Transaccion> transaccions = new ArrayList<>(factura.getTransaccionCollection());
//                    for (Transaccion transaccion : transaccions) {
//                        transaccion.setFacturaIdfactura(null);
//                        transaccionJpaController.edit(transaccion);
//                    }
//                    facturaJpaController.destroy(factura.getIdfactura());
////                    }
//
//                }
            } catch (Exception ex) {
                Logger.getLogger(TimerTaskFacturacionSipar.class.getName()).log(Level.SEVERE, null, ex);
                String mensaje = "Se presento un error en la busuqeda de la factura ID:" + factura.getIdfactura() + " <br>"
                        + "Error: " + ex.getMessage();
                ArrayList<String> strings = new ArrayList<>();
                strings.add("sebasariz@iammagis.com");
                CorreoZoho correoZoho = new CorreoZoho("Error facturas deamon renovación", mensaje, strings);
                correoZoho.start();
            }
        }
        facturasValidadas += "</table>";
        ArrayList<String> strings = new ArrayList<>();
        strings.add("sebasariz@iammagis.com");
        strings.add("tatogomez357@gmail.com");
        CorreoZoho correoZoho = new CorreoZoho("Facturas Daemon", facturasValidadas, strings);
        correoZoho.start();

    }

    public static void main(String[] args) throws InvalidDeviceTokenFormatException, CommunicationException, KeystoreException, JSONException {

        String dir = System.getProperty("user.dir");
        System.out.println("dir: " + dir);

        PushNotificationPayload payload = PushNotificationPayload.complex();
        payload.addAlert("Tu factura: 123 por valor de $123.000 , se encuentra disponible.");
        payload.addCustomDictionary("mensaje", "La factura tal se encuentra disponile para pagar");
        payload.addBadge(1);
        payload.addSound("beep.wav");
        /* Push your custom payload */
        ArrayList<Device> devices = new ArrayList<>();
        Device device = new BasicDevice("E36E69763FA72DFB1F45FA3CC1B0BF62F1A7CE6D7D0290B6B67FE6094C9A920A");
        devices.add(device);
        Push.payload(payload, dir + "/autopagos.p12", "sanignacio", false, devices);
    }

}
