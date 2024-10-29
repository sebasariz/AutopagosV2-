/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class DataGrid {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    static SimpleDateFormat simpleDateFormatSinTiempo = new SimpleDateFormat("dd/MM/yyyy");
    static DecimalFormat decimalFormat = new DecimalFormat("###0.00");

    public static JSONObject getUserData(ArrayList<Usuario> usuarios) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Usuario usuario : usuarios) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(usuario.getNumeroDeDocumento());
            data.put(usuario.getNombre());
            data.put(usuario.getApellidos());
            data.put(simpleDateFormat.format(new Date(usuario.getFechaRegistro().longValue())));
            data.put(usuario.getEmail());
            data.put(usuario.getCelular());
            if (usuario.getConveniosIdconvenios() != null) {
                data.put(usuario.getConveniosIdconvenios().getNombre());
            } else {
                data.put("N/A");
            }
            jSONObject1.put("id", usuario.getIdUsuario());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getConveniosData(ArrayList<Convenios> convenioses) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Convenios convenios : convenioses) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(convenios.getNit());
            if (convenios.getLogo() != null) {
                data.put("img/logoConvenios/" + convenios.getLogo());
            } else {
                data.put("img/icono-convenio-default.png");
            }
            data.put(convenios.getNombre());
            if (convenios.getTipoConvenio() == 1) {
                data.put("SIPAR");
            } else if (convenios.getTipoConvenio() == 2) {
                data.put("Autopagos Online");
            } else if (convenios.getTipoConvenio() == 3) {
                data.put("Corresponsalía");
            }else if (convenios.getTipoConvenio() == 4) {
                data.put("Aliado");
            }
            jSONObject1.put("id", convenios.getIdconvenios());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getFacturasAutopagosData(ArrayList<FacturaAutopagos> facturaAutopagoses) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (FacturaAutopagos facturaAutopagos : facturaAutopagoses) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(facturaAutopagos.getIdfacturaAutopagos());
            data.put(simpleDateFormat.format(new Date(facturaAutopagos.getFechaEmision().longValue())));
            if (facturaAutopagos.getConveniosIdconvenios() != null) {
                data.put(facturaAutopagos.getConveniosIdconvenios().getNombre());
            } else {
                data.put("N/A");
            }
            data.put(decimalFormat.format(facturaAutopagos.getValor()));
            data.put(decimalFormat.format(facturaAutopagos.getIva()));
            data.put(decimalFormat.format(facturaAutopagos.getValor() + facturaAutopagos.getIva()));
            //ponemos de colorcitos los estados
            if (facturaAutopagos.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + facturaAutopagos.getEstadoIdestado().getNombre() + "</label>");
            } else if (facturaAutopagos.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + facturaAutopagos.getEstadoIdestado().getNombre() + "</label>");
            } else if (facturaAutopagos.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + facturaAutopagos.getEstadoIdestado().getNombre() + "</label>");
            } else if (facturaAutopagos.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + facturaAutopagos.getEstadoIdestado().getNombre() + "</label>");
            } else if (facturaAutopagos.getEstadoIdestado().getIdestado() == 5) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ffaa00;width:100%; color:#ffffff; font-size:12px;\">" + facturaAutopagos.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put("Factura^DetalleFactura.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());
            data.put("Pagar^LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());

            jSONObject1.put("id", facturaAutopagos.getIdfacturaAutopagos());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static void getGlobalIndicators(HttpServletRequest request) {
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        //usuarios
        int users = usuarioJpaController.getUsuarioCount();
        request.setAttribute("usuarios", users);
        //convenios sipar
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        int sipar = conveniosJpaController.countConveniosSIPAR();
        request.setAttribute("sipar", sipar);
        //convenios online
        int online = conveniosJpaController.countConveniosONLINE();
        request.setAttribute("online", online);
        //convenios corresponsalia
        int corresponsalia = conveniosJpaController.countConveniosCORRESPONSALIA();
        request.setAttribute("corresponsalia", corresponsalia);
        //cargamos total facturado
        FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
        double totalFacturado = facturaAutopagosJpaController.getTotalFacturado();
        request.setAttribute("totalFacturado", decimalFormat.format(totalFacturado));
        double totalIva = facturaAutopagosJpaController.getTotalIVA();
        request.setAttribute("totalIva", decimalFormat.format(totalIva));
        ConfigJpaController configJpaController = new ConfigJpaController(manager);
        Config config = configJpaController.findConfig(1);
        if (config != null) {
            request.setAttribute("config", config);
        } else {
            request.setAttribute("config", new Config());
        }
    }

    public static JSONObject getTransaccionesData(ArrayList<Transaccion> transacciones) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Transaccion transaccion : transacciones) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(transaccion.getIdTransaccion());
            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            if (transaccion.getFacturaAutopagosIdfacturaAutopagos() != null) {
                data.put("Fact autopagos: " + transaccion.getFacturaAutopagosIdfacturaAutopagos().getIdfacturaAutopagos() + " Convenio: " + transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre());
            } else {
                if (transaccion.getFacturaIdfactura() != null) {
                    data.put("Convenio: " + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + " - Referencia:" + transaccion.getFacturaIdfactura().getReferencia());
                } else {
                    data.put("Referencia:" + transaccion.getReferencia());
                }
            }

            if (transaccion.getUsuarioidUsuario() != null) {
                data.put("(" + transaccion.getUsuarioidUsuario().getEmail() + ") " + transaccion.getUsuarioidUsuario().getNombre() + " " + transaccion.getUsuarioidUsuario().getApellidos());
            } else {
                data.put("N/A");
            }
            //ponemos de colorcitos los estados
            if (transaccion.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put(decimalFormat.format(transaccion.getValor()));
            data.put(transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
            if (transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE() != null) {
                data.put("ID: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getIdcomprobanteRecaudoPSE() + " Estado: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getEstadoIdestado().getNombre());
            } else {
                data.put("N/A");
            }
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getGraphicTransactions(ArrayList<Transaccion> transacciones, long fechaInicial, long fechaFinal) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.setTimeInMillis(fechaInicial);

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTimeInMillis(fechaFinal);

        Calendar calendarIntermedio = Calendar.getInstance();
        calendarIntermedio.setTimeInMillis(fechaInicial);
        calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
        //vamos a abanzar por dias
        JSONArray arrayTRXAutored = new JSONArray();
        JSONArray arrayTRXSipar = new JSONArray();
        JSONArray arrayOnline = new JSONArray();
        int contGlobal = 0;
        while (calendarInicial.getTimeInMillis() < calendarFinal.getTimeInMillis()) {
            //mientras avance por dias
            int contTRXSipar = 0;
            int contTRXAuto = 0;
            int contOnline = 0;
            for (Transaccion transaccion : transacciones) {
                if (calendarInicial.getTimeInMillis() < transaccion.getFecha().longValue() && transaccion.getFecha().longValue() < calendarIntermedio.getTimeInMillis()) {

                    if (transaccion.getFacturaIdfactura() != null) {
                        if (transaccion.getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 1) {
                            //SIPAR
                            contTRXSipar++;
                        } else if (transaccion.getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 2) {
                            //ONLINE
                            contTRXAuto++;
                        } else if (transaccion.getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 3) {
                            //CORRESPONSALIA
                            contOnline++;
                        }
                    }
                }
            }
            arrayTRXAutored.put(new JSONArray().put(contGlobal).put(contTRXAuto));
            arrayOnline.put(new JSONArray().put(contGlobal).put(contOnline));
            arrayTRXSipar.put(new JSONArray().put(contGlobal).put(contTRXSipar));
            //adjuntamos los datos 
            calendarInicial.add(Calendar.DAY_OF_YEAR, 1);
            calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
            contGlobal++;

        }
        jSONObject.put("autored", arrayTRXAutored);
        jSONObject.put("online", arrayOnline);
        jSONObject.put("sipar", arrayTRXSipar);

        return jSONObject;
    }

    public static JSONObject getTransaccionesOnlineData(ArrayList<Transaccion> transacciones) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Transaccion transaccion : transacciones) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(transaccion.getIdTransaccion());
            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            data.put("(" + transaccion.getUsuarioidUsuario().getNumeroDeDocumento() + ") " + transaccion.getUsuarioidUsuario().getNombre() + " " + transaccion.getUsuarioidUsuario().getApellidos());
            if (transaccion.getFacturaIdfactura() != null) {
                data.put(transaccion.getFacturaIdfactura().getReferencia());
                data.put(transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre());
            } else {
                data.put("N/A");
                data.put("N/A");
            }
            //ponemos de colorcitos los estados 
            if (transaccion.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put(decimalFormat.format(transaccion.getValor()));

            data.put(transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
            if (transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE() != null) {
                data.put("ID: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getIdcomprobanteRecaudoPSE() + " Estado: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getEstadoIdestado().getNombre());
            } else {
                data.put("N/A");
            }
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getTransaccionesSiparData(ArrayList<Transaccion> transacciones) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Transaccion transaccion : transacciones) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(transaccion.getIdTransaccion());
            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            if (transaccion.getUsuarioidUsuario() != null) {
                data.put("(" + transaccion.getUsuarioidUsuario().getNumeroDeDocumento() + ") " + transaccion.getUsuarioidUsuario().getNombre() + " " + transaccion.getUsuarioidUsuario().getApellidos());
            } else {
                data.put("N/A");
            }
            if (transaccion.getFacturaIdfactura() != null) {
                data.put(transaccion.getFacturaIdfactura().getReferencia());
                data.put(transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre());
            } else {
                data.put("N/A");
                data.put("N/A");
            }
            //ponemos de colorcitos los estados
            //ponemos de colorcitos los estados
            if (transaccion.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put(decimalFormat.format(transaccion.getValor()));

            data.put(transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
            if (transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE() != null) {
                data.put("ID: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getIdcomprobanteRecaudoPSE() + " Estado: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getEstadoIdestado().getNombre());
            } else {
                data.put("N/A");
            }
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getTransaccionesAutoredData(ArrayList<Transaccion> transacciones) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Transaccion transaccion : transacciones) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(transaccion.getIdTransaccion());
            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            data.put("(" + transaccion.getUsuarioidUsuario().getEmail() + ") " + transaccion.getUsuarioidUsuario().getNombre() + " " + transaccion.getUsuarioidUsuario().getApellidos());
            if (transaccion.getFacturaIdfactura() != null) {
                data.put(transaccion.getFacturaIdfactura().getReferencia());
                data.put(transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre());
            } else {
                data.put("N/A");
                data.put("N/A");
            }
            //ponemos de colorcitos los estados 
            if (transaccion.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put(decimalFormat.format(transaccion.getValor()));

            data.put(transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
            if (transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE() != null) {
                data.put("ID: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getIdcomprobanteRecaudoPSE() + " Estado: " + transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE().getEstadoIdestado().getNombre());
            } else {
                data.put("N/A");
            }
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONArray getConveniosJsonArraySelect(ArrayList<Convenios> convenioses) throws JSONException {

        JSONArray array = new JSONArray();
        for (Convenios convenios : convenioses) {
            JSONObject jSONObjectConvenio = new JSONObject();
            jSONObjectConvenio.put("id", convenios.getIdconvenios());
            jSONObjectConvenio.put("nombre", convenios.getNombre());
            array.put(jSONObjectConvenio);
        }
        return array;

    }

    public static JSONObject getFacturasGrid(ArrayList<Factura> facturas) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Factura factura : facturas) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(factura.getReferencia());
            data.put(simpleDateFormatSinTiempo.format(new Date(factura.getFechaCreacion().longValue())));
            if (factura.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 5) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ffaa00;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            }
            if (factura.getVisto() != null && factura.getVisto()) {
                data.put("<label style=\"background-color:#00b838;width:100%; color:#ffffff; font-size:12px;\">Visto</label>");
            } else {
                data.put("<label style=\"background-color:#0033cc;width:100%; color:#ffffff; font-size:12px;\">No visto</label>");
            }
            data.put(factura.getValor());
            data.put(factura.getValorPagado());
            data.put(simpleDateFormatSinTiempo.format(new Date(factura.getFechaVencimiento().longValue())));
            if (factura.getEmail() != null) {
                data.put(factura.getEmail());
            } else {
                data.put("N/A");
            }
            data.put("<a onclick=\"sendRemember(" + factura.getIdfactura() + ");\">Recordatorio</a>");
            data.put("<a onclick=\"sendPagar(" + factura.getIdfactura() + ");\">Pagar</a>"); 
            jSONObject1.put("id", factura.getIdfactura());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getPlantillasGrid(ArrayList<FacturaTemplate> plantillas) throws JSONException {
        JSONArray datosCompletos = new JSONArray();
        for (FacturaTemplate facturaTemplate : plantillas) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(facturaTemplate.getNombre());
            data.put("Ver^javascript:preview(" + facturaTemplate.getIdfacturaTemplate() + ")");
            jSONObject1.put("id", facturaTemplate.getIdfacturaTemplate());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getTransaccionesDataConvenio(ArrayList<Transaccion> transacciones) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Transaccion transaccion : transacciones) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();

            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            if (transaccion.getFacturaAutopagosIdfacturaAutopagos() != null) {
                data.put("Fact autopagos: " + transaccion.getFacturaAutopagosIdfacturaAutopagos().getIdfacturaAutopagos() + " Convenio: " + transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre());
            } else if (transaccion.getFacturaIdfactura() != null) {
                data.put(transaccion.getFacturaIdfactura().getReferencia());
            } else {
                data.put("N/A");
            }
            if (transaccion.getFacturaIdfactura() != null) {
                data.put(transaccion.getFacturaIdfactura().getEmail());
            } else {
                data.put("N/A");
            }
            if (transaccion.getUsuarioidUsuario() != null) {
                data.put("(" + transaccion.getUsuarioidUsuario().getNumeroDeDocumento() + ") " + transaccion.getUsuarioidUsuario().getNombre() + " " + transaccion.getUsuarioidUsuario().getApellidos());
            } else {
                data.put("N/A");
            }
            //ponemos de colorcitos los estados 
            if (transaccion.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            } else if (transaccion.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + transaccion.getEstadoIdestado().getNombre() + "</label>");
            }
            data.put(decimalFormat.format(transaccion.getValor()));
            data.put(transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
            data.put(transaccion.getIdTransaccion());
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getGraphicTransactionsSipar(ArrayList<Transaccion> transacciones, long fechaInicial, long fechaFinal) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.setTimeInMillis(fechaInicial);

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTimeInMillis(fechaFinal);

        Calendar calendarIntermedio = Calendar.getInstance();
        calendarIntermedio.setTimeInMillis(fechaInicial);
        calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
        //vamos a abanzar por dias

        JSONArray arrayTRXSipar = new JSONArray();

        int contGlobal = 0;
        while (calendarInicial.getTimeInMillis() < calendarFinal.getTimeInMillis()) {
            //mientras avance por dias
            int contTRXSipar = 0;
            for (Transaccion transaccion : transacciones) {
                if (calendarInicial.getTimeInMillis() < transaccion.getFecha().longValue() && transaccion.getFecha().longValue() < calendarIntermedio.getTimeInMillis()) {

                    if (transaccion.getFacturaIdfactura() != null && transaccion.getEstadoIdestado().getIdestado() == 2) {
                        if (transaccion.getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 1) {
                            //SIPAR
                            contTRXSipar += transaccion.getValor();
                        }
                    }
                }
            }
            arrayTRXSipar.put(new JSONArray().put(contGlobal).put(contTRXSipar));
            //adjuntamos los datos 
            calendarInicial.add(Calendar.DAY_OF_YEAR, 1);
            calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
            contGlobal++;

        }
        jSONObject.put("sipar", arrayTRXSipar);

        return jSONObject;
    }

    public static JSONObject getGraphicTransactionsOnline(ArrayList<Transaccion> transacciones, long fechaInicial, long fechaFinal) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.setTimeInMillis(fechaInicial);

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTimeInMillis(fechaFinal);

        Calendar calendarIntermedio = Calendar.getInstance();
        calendarIntermedio.setTimeInMillis(fechaInicial);
        calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
        //vamos a abanzar por dias

        JSONArray arrayTRXOnline = new JSONArray();

        int contGlobal = 0;
        while (calendarInicial.getTimeInMillis() < calendarFinal.getTimeInMillis()) {
            //mientras avance por dias
            int contTRXOnline = 0;
            for (Transaccion transaccion : transacciones) {
                if (calendarInicial.getTimeInMillis() < transaccion.getFecha().longValue() && transaccion.getFecha().longValue() < calendarIntermedio.getTimeInMillis()) {

                    if (transaccion.getFacturaIdfactura() != null) {
                        if (transaccion.getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 2 && transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                            //SIPAR
                            contTRXOnline += transaccion.getValor();
                        }
                    }
                }
            }
            arrayTRXOnline.put(new JSONArray().put(contGlobal).put(contTRXOnline));
            //adjuntamos los datos 
            calendarInicial.add(Calendar.DAY_OF_YEAR, 1);
            calendarIntermedio.add(Calendar.DAY_OF_YEAR, 1);
            contGlobal++;

        }
        jSONObject.put("online", arrayTRXOnline);

        return jSONObject;
    }

    public static JSONObject getTransaccionesPagoOnline(ArrayList<Transaccion> transaccions) throws JSONException {
        JSONArray datosCompletos = new JSONArray();
        for (Transaccion transaccion : transaccions) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(simpleDateFormat.format(new Date(transaccion.getFecha().longValue())));
            data.put(decimalFormat.format(transaccion.getValor()));
            data.put(transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre());
            data.put(transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNumeroCuenta());
            data.put(transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getTipoCuentaidtipoCuenta().getNombre());
            data.put(transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getBanco());
            data.put(transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getTitularCuenta());
            jSONObject1.put("id", transaccion.getIdtransaccion());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getConveniosDataAnonimouse(ArrayList<Convenios> convenioses) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Convenios convenios : convenioses) {
            if (convenios.getTipoConvenio() == 3) {
                JSONObject jSONObject1 = new JSONObject();
                //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
                JSONArray data = new JSONArray();
                if (convenios.getLogo() != null) {
                    data.put("img/logoConvenios/" + convenios.getLogo());
                } else {
                    data.put("img/icono-convenio-default.png");
                }
                data.put(convenios.getNombre());
                data.put(convenios.getTextoGuiaTercero());
                jSONObject1.put("id", convenios.getIdconvenios());
                jSONObject1.put("data", data);
                datosCompletos.put(jSONObject1);
            }
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static JSONObject getFacturasAutoredGrid(ArrayList<Factura> facturas) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray datosCompletos = new JSONArray();

        for (Factura factura : facturas) {
            JSONObject jSONObject1 = new JSONObject();
            //AQUI SE ARMA LA TABLA DONDE SE MUESTRA LA RESPECTIVA INFORMACION DE EL  USUARIO
            JSONArray data = new JSONArray();
            data.put(factura.getReferencia());
            data.put(simpleDateFormatSinTiempo.format(new Date(factura.getFechaCreacion().longValue())));
            if (factura.getEstadoIdestado().getIdestado() == 1) {
                //pendiente
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#FFD600; width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 2) {
                //Pagado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#AEEA00;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 3) {
                //Rechazado
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ef5350;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 4) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#3498db;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } else if (factura.getEstadoIdestado().getIdestado() == 5) {
                //Comprobando pago
                data.put("<label style=\"margin-top:5px; margin-bottom: 5px; border-radius: 5px; background-color:#ffaa00;width:100%; color:#ffffff; font-size:12px;\">" + factura.getEstadoIdestado().getNombre() + "</label>");
            } 
            data.put(factura.getValor());
            data.put(factura.getConveniosIdconvenios().getNombre());
            if (factura.getEmail() != null) {
                data.put(factura.getEmail());
            } else {
                data.put("N/A");
            }
            ArrayList<Usuario> usuarios = new ArrayList<>(factura.getUsuarioCollection());
            String usuariosString = "";
            for (Usuario usuario : usuarios) {
                usuariosString += " " + usuario.getNombre() + " " + usuario.getApellidos() + " -";
            }
            if (usuariosString.length() > 2) {
                usuariosString = usuariosString.substring(0, usuariosString.length() - 1);
            }
            data.put(usuariosString);

            jSONObject1.put("id", factura.getIdfactura());
            jSONObject1.put("data", data);
            datosCompletos.put(jSONObject1);
        }
        jSONObject.put("rows", datosCompletos);
        return jSONObject;
    }

    public static Object getPagosGrid(ArrayList<Factura> facturas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
