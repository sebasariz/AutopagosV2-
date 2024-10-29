/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.EstadoJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class ChangeComprobantePSE {

    static DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    static SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
    static PropertiesAccess propertiesAccess = new PropertiesAccess();

//
    public static void cambioEstado(ComprobanterecaudoPSE comprobanterecaudoPSE, int estado, String cus) throws Exception {
        ArrayList<Transaccion> transaccions = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection());
        System.out.println("transaccions: " + transaccions.size());
        double valorComision = 0;
        double valor = 0;
        if (!transaccions.isEmpty()) {

            //editamos todos los referentes al comprobante
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            EstadoJpaController estadoJpaController = new EstadoJpaController(manager);
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            comprobanterecaudoPSE.setFechaRespuesta(BigInteger.valueOf(System.currentTimeMillis()));
            if (comprobanterecaudoPSE.getIntento() != null && comprobanterecaudoPSE.getIntento()) {
                estado = 3;
            }
            comprobanterecaudoPSE.setEstadoIdestado(new Estado(estado));
            comprobanterecaudoPSE.setIntento(Boolean.TRUE);
            System.out.println("comprobanterecaudoPSE: ID: " + comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
            comprobanterecaudoPSEJpaController.edit(comprobanterecaudoPSE);
            //editamos las transacciones
            if (!transaccions.isEmpty()) {
                if (transaccions.get(0).getFacturaIdfactura() != null) {
                    Factura factura = transaccions.get(0).getFacturaIdfactura();
                    factura.setEstadoIdestado(new Estado(estado));
                    facturaJpaController.edit(factura);
                } else if (transaccions.get(0).getFacturaAutopagosIdfacturaAutopagos() != null) {
                    FacturaAutopagos facturaAutopagos = transaccions.get(0).getFacturaAutopagosIdfacturaAutopagos();
                    facturaAutopagos.setEstadoIdestado(new Estado(estado));
                    facturaAutopagosJpaController.edit(facturaAutopagos);
                }

            }
            for (Transaccion transaccion : transaccions) {
                if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 3) {
                    valorComision += transaccion.getValor();
                }
                if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                    valor += transaccion.getValor();
                }
                transaccion.setEstadoIdestado(new Estado(estado));
                transaccionJpaController.edit(transaccion);
            }

            //si esta aprobado, sumamos los valores al global para indicadores
            //envio de correo
            ArrayList<String> correos = new ArrayList<>();
            String[] emails = {};
            if (transaccions.get(0).getFacturaIdfactura() != null) {
                emails = transaccions.get(0).getFacturaIdfactura().getEmail().split(",");
                correos.addAll(Arrays.asList(emails));
                //agregamos los correos de la compañia
                ArrayList<Usuario> usuariosConvenio = usuarioJpaController.getUsuariosByTipoAndConvenio(2, transaccions.get(0).getConveniosIdconvenios().getIdconvenios());
                usuariosConvenio.addAll(usuarioJpaController.getUsuariosByTipoAndConvenio(3, transaccions.get(0).getConveniosIdconvenios().getIdconvenios()));
                ArrayList<String> usuariosConveniosString = new ArrayList<>();
                for(Usuario usuario:usuariosConvenio){
                    usuariosConveniosString.add(usuario.getEmail());
                }
                correos.addAll(usuariosConveniosString);
            }

            if (emails.length > 0) {
                if (estado == 2) {
                    //si esta pago sumo los valores

                    ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
                    //envio el mail
                    String tabla = "";
                    for (Transaccion transaccion : transaccions) {
                        Estado estadoTransaccion = estadoJpaController.findEstado(transaccion.getEstadoIdestado().getIdestado());
                        if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {

                            tabla += "<tr>"
                                    + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaIdfactura().getReferencia() + "</p></td>"
                                    + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo() + "\"></td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                    + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                    + "</tr>"
                                    + "<tr>"
                                    + "<td colspan=\"5\" style=\"padding:0;\">"
                                    + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                    + "</td>"
                                    + "</tr>";
                            //es de sipar
                        } else if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 7) {
                            //es de autopagos 
                            tabla += "<tr>"
                                    + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getIdfacturaAutopagos() + "</p>	</td>"
                                    + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logo-banner-principal-2.png\"></td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                    + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                    + "</tr>"
                                    + "<tr>"
                                    + "<td colspan=\"5\" style=\"padding:0;\">"
                                    + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                    + "</td>"
                                    + "</tr>";
                        }
                    }

                    String templaString = CorreoZoho.getTemplate(CorreoZoho.pagoUsuarioTemplate);

                    templaString = templaString.replace("$horaTransaccion", simpledateFormat.format(new Date(comprobanterecaudoPSE.getFecha().longValue())));
                    templaString = templaString.replace("$cusTransaccion", cus);
                    templaString = templaString.replace("$idTransaccion", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "");
                    templaString = templaString.replace("$valor-total", comprobanterecaudoPSE.getValorTotal() + "");

                    templaString = templaString.replace("$costo-trx", decimalFormat.format(valorComision));
                    templaString = templaString.replace("$costo-total", decimalFormat.format(valor + valorComision));
                    templaString = templaString.replace("$numero", transaccions.size() + "");
                    templaString = templaString.replaceAll("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$direccion", propertiesAccess.DIRECCION);

                    templaString = templaString.replace("$tabla", tabla);

                    CorreoZoho correoZoho = new CorreoZoho("Transaccion APROBADA AUTOPAGOS", templaString, correos);
                    correoZoho.start();

                    //sumamos los valores recaudados
                    Config config = configJpaController.findConfig(1);
                    if (transaccions.get(0).getFacturaIdfactura() != null) {
                        Factura factura = transaccions.get(0).getFacturaIdfactura();

                        Convenios convenios = factura.getConveniosIdconvenios();
                        //enviamos el retorno al canal transaccional si lo tiene
                        if (convenios.getDireccionRespuesta() != null && !convenios.getDireccionRespuesta().equals("")) {
                            JSONObject jSONObjectRetorno = new JSONObject();
                            jSONObjectRetorno.put("estado", 1);
                            jSONObjectRetorno.put("id", transaccions.get(0).getFacturaIdfactura().getIdfactura());
                            jSONObjectRetorno.put("referencia", transaccions.get(0).getFacturaIdfactura().getReferencia());
                            jSONObjectRetorno.put("valor", transaccions.get(0).getFacturaIdfactura().getValor());
                            HttpResponseSender httpResponseSender = new HttpResponseSender();
                            httpResponseSender.sendJson(convenios.getDireccionRespuesta(), jSONObjectRetorno);
//                          System.out.println("sali de darle respuesta a la url");
                        }

                        if (transaccions.get(0).getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 1) {
                            //sipar
                            config.setSiparNumeroFacturasPagadas(config.getSiparNumeroFacturasPagadas() + 1);
                            config.setSiparValorTotalRecaudado(config.getSiparValorTotalRecaudado() + valor);
                            //aumentamos el valor de transacciones y recaudo del convenio
                            convenios.setValorRecaudado(convenios.getValorRecaudado() + valor);
                            convenios.setNumeroTransaccionesPlanPost(convenios.getNumeroTransaccionesPlanPost() + 1);
                        } else if (transaccions.get(0).getFacturaIdfactura().getConveniosIdconvenios().getTipoConvenio() == 2) {
                            //online
                            convenios.setValorComisiones(convenios.getValorComisiones() + valorComision);
                            convenios.setValorRecaudado(convenios.getValorRecaudado() + valor);
                            config.setOnlineCobrosRecibidos(config.getOnlineCobrosRecibidos() + 1);
                        }

                        conveniosJpaController.edit(convenios);
                    } else if (transaccions.get(0).getFacturaAutopagosIdfacturaAutopagos() != null) {
                        //es un pago de autopagos
                        System.out.println("es un pago de autopagos");

                    }
                    configJpaController.edit(config);

                } else if (estado == 3) {
                    int numeroFacturas = 0;
                    String tabla = "";
                    for (Transaccion transaccion : transaccions) {
                        Estado estadoTransaccion = estadoJpaController.findEstado(transaccion.getEstadoIdestado().getIdestado());
                        if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {

                            numeroFacturas++;
                            tabla += "<tr>"
                                    + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaIdfactura().getReferencia() + "</p></td>"
                                    + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo() + "\"></td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                    + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                    + "</tr>"
                                    + "<tr>"
                                    + "<td colspan=\"5\" style=\"padding:0;\">"
                                    + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                    + "</td>"
                                    + "</tr>";
                            //es de sipar
                        } else if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 7) {
                            //es de autopagos
                            numeroFacturas++;

                            tabla += "<tr>"
                                    + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getIdfacturaAutopagos() + "</p>	</td>"
                                    + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logo-banner-principal-2.png\"></td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                    + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                    + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                    + "</tr>"
                                    + "<tr>"
                                    + "<td colspan=\"5\" style=\"padding:0;\">"
                                    + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                    + "</td>"
                                    + "</tr>";
                        }
                    }

                    //pago rechazado de sipar o de autopagos
                    String templaString = CorreoZoho.getTemplate(CorreoZoho.noPagoUsuarioTemplate);
                    templaString = templaString.replace("$cusTransaccion", cus);
                    templaString = templaString.replace("$horaTransaccion", simpledateFormat.format(new Date(comprobanterecaudoPSE.getFecha().longValue())));
                    templaString = templaString.replace("$idTransaccion", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "");

                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$numero", numeroFacturas + "");
                    templaString = templaString.replace("$valor-comision", decimalFormat.format(valorComision));
                    templaString = templaString.replace("$valor-total", decimalFormat.format(valor));

                    templaString = templaString.replace("$tabla", tabla);
                    templaString = templaString.replace("$comision", Math.round(valorComision) + "");
                    templaString = templaString.replace("$total", decimalFormat.format(valor + valorComision) + "");
                    CorreoZoho correoZoho = new CorreoZoho("Transaccion RECHAZADA AUTOPAGOS", templaString, correos);
                    correoZoho.start();
                }
            }
        } else {
            System.out.println("no existen transacciones asociadas");
        }
    }

    public static void cambioEstadoAutored(ComprobanterecaudoPSE comprobanterecaudoPSE, JSONObject jSONObject) throws Exception {
        //este es el cambi oen la red piramidal que creamos
        ArrayList<Transaccion> transaccions = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection());
        double valorComision = 0;
        double valor = 0;
        System.out.println("jSONObject: " + jSONObject);
        if (!transaccions.isEmpty()) {

            //editamos todos los referentes al comprobante
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            EstadoJpaController estadoJpaController = new EstadoJpaController(manager);
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            comprobanterecaudoPSE.setFechaRespuesta(BigInteger.valueOf(System.currentTimeMillis()));
            comprobanterecaudoPSE.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.edit(comprobanterecaudoPSE);

            //si esta aprobado, sumamos los valores al global para indicadores
            //envio de correo
            ArrayList<String> correos = new ArrayList<>();
            String[] emails = {};
            if (jSONObject.getInt("estado") == 2) {
                //envio el mail TRANSACCCION APROBADA EL PAGO FUE REALIZADO POR EL USUARIO, AHORA VAMOS A PAGAR EN FULLCARGA
                String tabla = "";
                //sumamos los valores recaudados
                Config config = configJpaController.findConfig(1);
                double comision = 0;
                int numeroFacturas = 0;
                Usuario usuario = null;

                for (Transaccion transaccion : transaccions) {
                    if (transaccion.getFacturaIdfactura() != null) {
                        Factura factura = facturaJpaController.findFactura(transaccion.getFacturaIdfactura().getIdfactura());
                        factura.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));
                        factura.setValor(0D);
                        factura.setFechaEmision(BigInteger.valueOf(System.currentTimeMillis()));
                        factura = facturaJpaController.edit(factura);
//                        System.out.println("factura: " + factura.getEstadoIdestado().getIdestado());
                        transaccion.setFacturaIdfactura(factura);
                    } else if (transaccion.getFacturaAutopagosIdfacturaAutopagos() != null) {
                        FacturaAutopagos facturaAutopagos = transaccion.getFacturaAutopagosIdfacturaAutopagos();
                        facturaAutopagos.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));
                        facturaAutopagos = facturaAutopagosJpaController.edit(facturaAutopagos);
                        transaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                    }

                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 3) {
                        valorComision += transaccion.getValor();
                    }
                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                        valor += transaccion.getValor();
                    }
                    transaccion.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));

                    Estado estadoTransaccion = estadoJpaController.findEstado(jSONObject.getInt("estado"));
                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                        emails = transaccion.getFacturaIdfactura().getEmail().split(",");

                        if (usuario == null) {
                            usuario = transaccion.getUsuarioidUsuario();
                        }
//                        System.out.println("ojo que este es el pago de full carga");
//                        JSONObject jSONObjectRespuesta = new JSONObject();
//                        jSONObjectRespuesta.put("codigo", "00");
//                        jSONObjectRespuesta.put("codfullcarga", "1234567");

                        numeroFacturas++;
                        tabla += "<tr>"
                                + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaIdfactura().getReferencia() + "</p></td>"
                                + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo() + "\"></td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(valor) + "</span></p></td>"
                                + "</tr>"
                                + "<tr>"
                                + "<td colspan=\"5\" style=\"padding:0;\">"
                                + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                + "</td>"
                                + "</tr>";

                        //transacciones a pagar facturas
                        //actualizamos los datos del dashboard
                        config.setAutoredNumeroFacturasPagadas(config.getAutoredNumeroFacturasPagadas() + 1);
                        config.setAutoredNumeroTransacciones(config.getAutoredNumeroTransacciones() + 1);
                        Factura factura = transaccion.getFacturaIdfactura();
                        comision += factura.getConveniosIdconvenios().getComisionTerceroVariable() + factura.getConveniosIdconvenios().getComisionTerceroFija();

                    }
                    transaccionJpaController.edit(transaccion);
                }
                if (usuario != null && comprobanterecaudoPSE != null) { 
                    //entregar los saldos en autored de acuerdo a los niveles 
                    DistroComision distroComision = new DistroComision();
                    int comisionUsuarios = distroComision.distribuirEscalaRedAutored(usuario);
                    config.setAutoredComisionActualUsuariosAutored(config.getAutoredComisionActualUsuariosAutored() + comisionUsuarios); 
                    usuario = usuarioJpaController.findUsuario(usuario.getIdUsuario());
                    if (usuario != null && usuario.getPuntos() != null) {
                        int puntos = usuario.getPuntos();
                        int creditos = comprobanterecaudoPSE.getCreditos();

                        if (puntos != 0 && creditos != 0) {
                            usuario.setPuntos(usuario.getPuntos() - comprobanterecaudoPSE.getCreditos());
                            usuarioJpaController.edit(usuario);
                        }
                    }

                }

                config.setAutoredComisionActualUsuariosAutored(config.getAutoredComisionActualUsuariosAutored() + comision);
                config.setAutoredNumeroTransacciones(config.getAutoredNumeroTransacciones() + 1);

                correos.addAll(Arrays.asList(emails));
                ArrayList<Usuario> usuariosConvenio = usuarioJpaController.getUsuariosByTipoAndConvenio(2, transaccions.get(0).getConveniosIdconvenios().getIdconvenios());
                usuariosConvenio.addAll(usuarioJpaController.getUsuariosByTipoAndConvenio(3, transaccions.get(0).getConveniosIdconvenios().getIdconvenios()));
                ArrayList<String> usuariosConveniosString = new ArrayList<>();
                for(Usuario usuario1:usuariosConvenio){
                    usuariosConveniosString.add(usuario1.getEmail());
                }
                correos.addAll(usuariosConveniosString);
                
                if (!correos.isEmpty()) {
                    String templaString = CorreoZoho.getTemplate(CorreoZoho.pagoUsuarioTemplate);
                    templaString = templaString.replace("$idTransaccion", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "");
                    templaString = templaString.replace("$horaTransaccion", simpledateFormat.format(new Date()));
                    templaString = templaString.replace("$tabla", tabla);
                    System.out.println("=================");
                    System.out.println("tabla: " + tabla);
                    System.out.println("=================");
                    if (jSONObject.has("aprobacion")) {
                        templaString = templaString.replace("$cusTransaccion", jSONObject.getInt("aprobacion") + "");
                    } else {
                        templaString = templaString.replace("$cusTransaccion", jSONObject.toString());
                    }
                    templaString = templaString.replace("$valor-total", decimalFormat.format(valor));

                    templaString = templaString.replace("$costo-trx", decimalFormat.format(valorComision));
                    templaString = templaString.replace("$costo-total", decimalFormat.format(valor + valorComision));
                    templaString = templaString.replace("$numero", numeroFacturas + "");
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$direccion", propertiesAccess.DIRECCION);

                    templaString = templaString.replace("$tabla", tabla);

                    CorreoZoho correoZoho = new CorreoZoho("Transaccion APROBADA AUTOPAGOS", templaString, correos);
                    correoZoho.start();
                }

                configJpaController.edit(config);

            } else if (jSONObject.getInt("estado") == 3) {
                // TRANSACCION RECHAZADA PAGO NO REALIZADO
                int numeroFacturas = 0;
                String tabla = "";
                for (Transaccion transaccion : transaccions) {
                    if (transaccion.getFacturaIdfactura() != null) {
                        Factura factura = facturaJpaController.findFactura(transaccion.getFacturaIdfactura().getIdfactura());
                        factura.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));
                        factura = facturaJpaController.edit(factura);
                        System.out.println("factura rechazada: " + factura.getEstadoIdestado().getIdestado());
                        transaccion.setFacturaIdfactura(factura);
                    } else if (transaccion.getFacturaAutopagosIdfacturaAutopagos() != null) {
                        FacturaAutopagos facturaAutopagos = transaccion.getFacturaAutopagosIdfacturaAutopagos();
                        facturaAutopagos.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));
                        facturaAutopagos = facturaAutopagosJpaController.edit(facturaAutopagos);
                        transaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                    }

                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 3) {
                        valorComision += transaccion.getValor();
                    }
                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                        valor += transaccion.getValor();
                    }
                    transaccion.setEstadoIdestado(new Estado(jSONObject.getInt("estado")));

                    Estado estadoTransaccion = estadoJpaController.findEstado(jSONObject.getInt("estado"));
                    if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                        emails = transaccion.getFacturaIdfactura().getEmail().split(",");
                        numeroFacturas++;
                        tabla += "<tr>"
                                + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaIdfactura().getReferencia() + "</p></td>"
                                + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo() + "\"></td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                + "</tr>"
                                + "<tr>"
                                + "<td colspan=\"5\" style=\"padding:0;\">"
                                + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                + "</td>"
                                + "</tr>";
                        //es de sipar
                    } else if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 7) {
                        //es de autopagos
                        numeroFacturas++;

                        tabla += "<tr>"
                                + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getIdfacturaAutopagos() + "</p>	</td>"
                                + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logo-banner-principal-2.png\"></td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaAutopagosIdfacturaAutopagos().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + estadoTransaccion.getNombre() + " </span></p> </td>"
                                + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                + "</tr>"
                                + "<tr>"
                                + "<td colspan=\"5\" style=\"padding:0;\">"
                                + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                + "</td>"
                                + "</tr>";
                    }
                    transaccionJpaController.edit(transaccion);
                }
                correos.addAll(Arrays.asList(emails));
                if (!correos.isEmpty()) {
                    //pago rechazado de sipar o de autopagos
                    String templaString = CorreoZoho.getTemplate(CorreoZoho.noPagoUsuarioTemplate);
                    templaString = templaString.replace("$idTransaccion", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "");
                    templaString = templaString.replace("$horaTransaccion", simpledateFormat.format(new Date()));
                    templaString = templaString.replace("$tabla", tabla);
                    if (jSONObject.has("aprobacion")) {
                        templaString = templaString.replace("$cusTransaccion", jSONObject.getInt("aprobacion") + "");
                    } else {
                        templaString = templaString.replace("$cusTransaccion", "");
                    }
                    templaString = templaString.replace("$valor-total", decimalFormat.format(valor));
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$server", propertiesAccess.SERVER);
                    templaString = templaString.replace("$direccion", propertiesAccess.DIRECCION);
                    templaString = templaString.replace("$numero", numeroFacturas + "");
                    templaString = templaString.replace("$motivo", "Request error blank");
                    templaString = templaString.replace("$valor-comision", decimalFormat.format(valorComision));

                    templaString = templaString.replace("$tabla", tabla);
                    templaString = templaString.replace("$comision", Math.round(valorComision) + "");
                    templaString = templaString.replace("$total", decimalFormat.format(valor + valorComision) + "");
                    CorreoZoho correoZoho = new CorreoZoho("Transaccion RECHAZADA AUTOPAGOS", templaString, correos);
                    correoZoho.start();
                }
            }
        } else {
            System.out.println("no existen transacciones asociadas");
        }
    }
}
